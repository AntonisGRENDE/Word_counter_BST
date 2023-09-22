import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.*;

public class MyLinkedList<T> implements Iterable<T>{

    public class ListNode<T>{
        protected T data;
        protected ListNode<T> next;

        ListNode(T data) {
            this.data = data;
            next = null;
        }

        public T getData() {
            return data;
        }

        public ListNode<T> getNext() {
            return next;
        }

        public void setNext(ListNode<T> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "data=" + data +
                    '}';
        }
    }

    private ListNode<T> head = null;
    private ListNode<T> tail = null;
    int size = 0;

    public MyLinkedList() {}

    public MyLinkedList (T...objects){
        for (T o : objects) {
            if (head == null) {
                insertAtFront(o);
            } else {
                insertAtBack(o);
            }
        }
    }

    public boolean isEmpty() { return head == null; }

    public boolean contains(T ob) { //todo
        ListNode<T> current = head;

        while (current != null) {
            if (ob.equals(current.getData()) || ob instanceof String && current.getData() instanceof String &&
                    compareToIgnoreCaseWithoutDiacritics((String) current.getData(), (String) ob) == 0){ //intended for cognate words
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public boolean containsString(String str) {
        if (head != null && head.getData() instanceof String) {
            ListNode<String> current = (ListNode<String>) head;
            while (current != null) {
                if (current.getData().equals(str))
                    return true;
                current = current.getNext();
            }
        }
        return false;
    }
    public static int compareToIgnoreCaseWithoutDiacritics(String str, String str2) {
        String normalizedWord2 = Normalizer.normalize(str2, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return str.compareToIgnoreCase(normalizedWord2);
    }

    /**
     * @return 0 if the element was found and removed, -1 if the element was not found
     * @throws EmptyStackException if the list is empty
     */
    public int remove(T data) throws EmptyStackException {

        ListNode<T> iterator = head;
        ListNode<T> prev = null;

        while (iterator != null) {
            if (data.equals(iterator.getData())) {
                if (prev == null) {
                    head = iterator.getNext();
                } else {
                    prev.setNext(iterator.getNext());
                }

                if (iterator.getNext() == null) {
                    tail = prev;
                }
                --size;
                return 0;
            }
            prev = iterator;
            iterator =  iterator.getNext();
        }
        return -1;
    }


    public void insertAtFront(T data) {
        ListNode<T> n = new ListNode<>(data);

        if (data == null)
            throw new RuntimeException();
        else if (isEmpty()) {
            head = tail = n;
        } else {
            n.setNext(head);
            head = n;
        }
        ++size;
    }

    public void insertAtBack(T data) {
        ListNode<T> n = new ListNode<>(data);

        if (data == null)
            throw new RuntimeException();
        else if (isEmpty()) {
            head = tail = n;
        } else {
            tail.setNext(n);
            tail = n;
        }
        ++size;
    }


    public void sort(Comparator<T> comparator) {
        if (head == null || head == tail)
            return;

        ListNode<T> newHead = null;
        ListNode<T> newTail = null;

        while (head != null) {
            // get next item
            ListNode<T> swap = head;

            head = head.getNext();

            // move swap to new-sorted list
            if (newHead == null) {
                newHead = newTail = swap;
                swap.setNext(null);
            } else {
                ListNode<T> prev = null;
                ListNode<T> iterator = newHead;

                // iterate newList until we get to a point where our data is smaller or reach the end
                while (iterator != null && comparator.compare(iterator.getData(), swap.getData()) >= 0) {
                    prev = iterator;
                    iterator = iterator.getNext();
                }

                // reached the point where we should place the node
                // if prev == null then it is the head of the list
                if (prev == null)
                    newHead = swap;
                else
                    prev.setNext(swap);

                // if iterator == null then it is the tail of the list
                swap.setNext(iterator);
                if(iterator == null)
                    newTail = swap;
            }
        }
        head = newHead;
        tail = newTail;
    }


    public ListNode<T> getHead() {
        return head;
    }

    public int getSize(){
        return size;
    }

    public String toString() {
        if (isEmpty())
            return "The List is empty :(";

        else if (this.getHead().getData() instanceof WordFreq) {
            MyLinkedList<?>.ListNode<?> temp = this.getHead();
            StringBuilder str = new StringBuilder();

            while (temp != null) {
                Object data = temp.getData();
                String word = ((WordFreq) data).getWord();
                if (WordFreqBST.getStopWords() != null && !WordFreqBST.getStopWords().contains(word)) {
                    str.append(data).append("\n");
                }
                temp = temp.getNext();
            }
            return (str + "\nΤhe total number of different words written is:           " + WordFreqBST.getWordFreqList().getSize() +
                          "\nThe total number of words written is:                     " + WordFreqBST.getTotalWordsWritten() +
                          "\nYour word variety (different words divided total words)   ") + new DecimalFormat("#.#").format(WordFreqBST.getWordFreqList().getSize() * 100.0f / WordFreqBST.getTotalWordsWritten())  +"%";
        } else if (this.getHead().getData() instanceof String){
            MyLinkedList<?>.ListNode<?> temp = this.getHead();
            StringBuilder str = new StringBuilder();

            while (temp != null) {
                str.append(temp.getData()).append(", ");
                temp = temp.getNext();
            }
            return "nodes: " + str  ;
        }
        else {
            ListNode<T> current = head;
            StringBuilder ret = new StringBuilder();

            while (current != null) {
                ret.append(current.data.toString());

                if (current.getNext() != null)
                    ret.append("\n");

                current = current.next;
            }
            return ret.toString();
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private ListNode<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.getData();
            current = current.getNext();
            return data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}