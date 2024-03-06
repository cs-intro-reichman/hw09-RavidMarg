import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);
        for (int i = 0; i < windowLength; i++) {
            window += in.readChar();
        }
        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = (window + c).substring(1);
        }
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
	}

    private int numberOfElementsInList(List probs){
        int num_of_elements = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            num_of_elements += probs.listIterator(i).current.cp.count;
        }
        return num_of_elements;
    }

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
        int num_of_elements = this.numberOfElementsInList(probs);
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            double probability = (double) cd.count / num_of_elements;
            cd.p = probability;
            if (i == 0){
                cd.cp = probability;
            }
            else {
                cd.cp = probs.get(i - 1).cp + probability;
            }
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        char result = ' ';
		double random_num = randomGenerator.nextDouble();
        int num_of_elements = this.numberOfElementsInList(probs);
        for (int i = 0; i < num_of_elements; i++) {
            CharData cd = probs.get(i);
            if (cd.cp > random_num) {
                result = cd.chr;
                break;
            }
        }
        return result;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        String result = initialText;
        if ( ! (initialText.length() < windowLength)){
            String window = initialText.substring(initialText.length() - windowLength);
            result = window;
            int numberOfLetters = textLength + windowLength;
            while (result.length() < numberOfLetters) {
                List curr_list = CharDataMap.get(window);
                if (curr_list == null) {
                    break;
                }
                result += getRandomChar(curr_list);
                window = result.substring(result.length() - windowLength);
            }
        }
        return  result;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}
