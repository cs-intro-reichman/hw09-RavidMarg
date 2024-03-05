/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize(){
 	    return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        Node new_first = new Node(cd, this.first);
        this.first = new_first;
        this.size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        String result = "";
        Node currect = this.first;
        while (currect != null) {
            result = result + currect.cp.toString() + " ";
            currect = currect.next;
        }
        return "(" + result.substring(0, result.length() - 1) + ")";
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node current = this.first;
        int index = 0;
        int result = -1;

        if (first != null) {
            while (index < this.size || index == 0) {
                if (current.cp.chr == chr) {
                    result = index;
                    break;
                } else {
                    index++;
                    if (current.next != null) {
                        current = current.next;
                    }
                }
            }
        }
        return result;
    }



    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if (this.indexOf(chr) == -1) {
            this.addFirst(chr);
        }
        else{
            Node current = this.first;
            while (current.cp.chr != chr) {
                current = current.next;
            }
            current.cp.count++;
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        boolean result = false;
        if (this.indexOf(chr) != -1) {
            Node current = this.first;
            Node previous = null;

            while (current.cp.chr != chr) {
                previous = current;
                current = current.next;
            }
            if (previous != null) {
                previous.next = current.next;
            }
            this.size--;
            result = true;
        }
        return result;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || size <= index) {
            throw new IndexOutOfBoundsException("Index " + index + "is out of range");
        } else {
            Node current = this.first;
            int iter_index = 0;
            while (iter_index != index) {
                current = current.next;
                iter_index++;
            }
            return current.cp;
        }
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = this.first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = this.first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}
