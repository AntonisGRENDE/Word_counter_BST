import java.util.*;

public class List<T>{
    public class Node<T>{
        protected T data;
        protected Node<T> next;

        Node(T data) {
            this.data = data;
            next = null;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

    }


    private Node<T> head = null;
    private Node<T> tail = null;
    int size = 0;


    public List() {
    }

    public boolean isEmpty() {
        return head == null;
    }

    public boolean contains(T ob) {
        Node <T> current = head;

        while (current != null) {
            if (ob.equals(current.getData()))
                return true;
            current = current.getNext();
        }
        return false;
    }

    /**
     * @return 0 if the element was found and removed, -1 if the element was not found
     * @throws EmptyStackException if the list is empty
     */
    public int remove(T data) throws EmptyStackException {
        if (isEmpty()) throw new EmptyStackException();

        Node<T> iterator = head;
        Node<T> prev = null;

        while (iterator != null) {
            if (data.equals(iterator.getData())) {
                if (prev == null) {
                    head = iterator.getNext();
                } else {
                    prev.setNext(iterator.getNext());
                }

                if (iterator.getNext() ==null) {
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
        Node<T> n = new Node<>(data);

        if (isEmpty()) {
            head = tail = n;
        } else {
            n.setNext(head);
            head = n;
        }
        ++size;
    }

    public void insertAtBack(T data) {
        Node<T> n = new Node<>(data);

        if (isEmpty()) {
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

        Node<T> newHead = null;
        Node<T> newTail = null;

        while (head != null) {
            // get next item
            Node<T> swap = head;

            head = head.getNext();

            // move swap to new-sorted list
            if (newHead == null) {
                newHead = newTail = swap;
                swap.setNext(null);
            } else {
                Node<T> prev = null;
                Node<T> iterator = newHead;

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


    public Node<T> getHead() {
        return head;
    }

    public int getSize(){
        return size;
    }

    public String toString() {
        if (isEmpty()) return "List is empty :(";

//        if (this.getHead().getData().getClass().getName().equals("WordFreq")) {
//            List<WordFreq>.Node<WordFreq> temp = (List<WordFreq>.Node<WordFreq>) this.getHead();
//        }

        Node<T> current = head;
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