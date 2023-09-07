import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Scanner;

public class WordFreqBST implements WordCounter {
    public static class WordFreqTreeNode implements Comparator<WordFreq> {
        WordFreq wordFreqObj;
        WordFreqTreeNode left, right, parent;
        int subtreeSize;
        WordFreqTreeNode(WordFreq t) {
            wordFreqObj = new WordFreq(t);
        }
        WordFreqTreeNode(String item) {
            this.wordFreqObj = new WordFreq(item);
        }

        public boolean contains(Object ob) {
            WordFreqTreeNode currentTreeNode = this;
            while (this.parent != null) //find parent node
                currentTreeNode = currentTreeNode.parent;
            return containsRecursive(currentTreeNode, ob);
        }

        public boolean containsRecursive(WordFreqTreeNode current, Object ob){
            if (current == null)
                return false;
            else if (ob instanceof WordFreq && current.wordFreqObj.compareToIgnoreCaseWithoutDiacritics((WordFreq) ob) == 0) { //intended for root words
                return true;
            }
            boolean leftContains = containsRecursive(current.left, ob);
            boolean rightContains = containsRecursive(current.right, ob);

            return leftContains || rightContains;
        }

        public WordFreq getWordFreqObj() {
            return wordFreqObj;
        }

        public WordFreqTreeNode getLeft() {
            return left;
        }

        public WordFreqTreeNode getRight() {
            return right;
        }

        public WordFreqTreeNode getParent() {return parent;}

        public int getSubtreeSize() {
            return subtreeSize;
        }

        public void subtreeIncrease() {
            WordFreqTreeNode current = this;
            while (current.parent != null) {
                current = current.parent;
                current.subtreeSize += 1;
            }
        }
        public void setItem(WordFreq item) {
            this.wordFreqObj = item;
        }

        public void setLeft(WordFreqTreeNode left) {
            this.left = left;
        }

        public void setRight(WordFreqTreeNode right) {
            this.right = right;
        }

        public void setParent(WordFreqTreeNode parent) {
            this.parent = parent;
        }

        public void setSubtreeSize(int subtreeSize) {
            this.subtreeSize = subtreeSize;
        }

        /** @return 0 means same value, 1 means bigger, 2 means same object, -1 means smaller */
        public String toString() {
            return wordFreqObj.toString();
        }
        @Override
        public int compare(WordFreq o1, WordFreq o2) {
            return o1.compareToIgnoreCaseWithoutDiacritics(o2);
        }
        public int compareTo(WordFreqTreeNode node){ return this.getWordFreqObj().compareToIgnoreCaseWithoutDiacritics(node.getWordFreqObj()); }
    }
    private static WordFreqTreeNode head;
    private static MyLinkedList<String> stopWords;
    private static MyLinkedList<WordFreq> wordFreqMyLinkedList;


    public static void main(String[] args) {
        WordFreqBST a = new WordFreqBST();
        a.addStopWord("να", "και", "τι", "μου", "με", "το", "την", "του", "τον", "δεν", "που", "για", "τα",
                        "η", "ο", "στο", "θα", "απ", "πως", "στην", "της", "σε", "αλλα", "ότι", "από", "οι", "των", "τη", "τις", "of", "στον");
        a.load("D:\\Projects\\domes-dedomenon-2021\\3rd-assignment\\text1.txt");

        traverseR5(head);
        a.printTreeAlphabetically(System.out);
        //System.out.println(WordFreq.rootWords.toString());
        //a.printTreeByFrequency(System.out);
    }


    @Override
    public void load(String filename) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to count words ending with a different postfix but same root as different words? Answer with yes or no");
        boolean sameOrigin = false; //scanner.nextLine().equals("yes"); //todo fix no response
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line, la[];
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[^α-ωά-ώΑ-ΩΆ-Ώa-zA-Z\\s']", "").replace("\t", " ").toLowerCase();
                la = line.split(" ");
                for (String s : la)
                    if (!(s.isBlank() || (s.startsWith("'") || s.endsWith("'"))) && !stopWords.contains(s))
                        insert(s,sameOrigin);
            }
        } catch (IOException ex) {
            System.out.println("file name is: " + filename); ex.printStackTrace();
        }
    }

    @Override
    public void insert(String string, boolean origin) {

        if (head == null) {
            head = new WordFreqTreeNode(string);
            WordFreq.rootExists(head.getWordFreqObj());
            return ;
        }

        WordFreqTreeNode nodeIter = head;
        WordFreqTreeNode newItem = new WordFreqTreeNode(string);
        while (true) {
            if (nodeIter.compareTo(newItem) == 0 || (!origin && WordFreq.rootExists(newItem.getWordFreqObj()) && nodeIter.getWordFreqObj().getWord().startsWith(newItem.getWordFreqObj().getRoot())
                    && nodeIter.getWordFreqObj().getWord().length() <= newItem.getWordFreqObj().getRoot().length() + newItem.wordFreqObj.getPostfix().length())) {
                nodeIter.getWordFreqObj().increaseFrequency();
                // if (!nodeIter.getWordFreqObj().getWord().equals(string)) //todo remove
                    //System.out.println(nodeIter.getWordFreqObj().getWord() + "  " +string);
                return ;
            } else {
                WordFreqTreeNode childNode = (nodeIter.compareTo(newItem) < 0) ? nodeIter.getRight() : nodeIter.getLeft();
                if (childNode == null) {
                    if (nodeIter.compareTo(newItem) < 0) {
                        nodeIter.setRight(newItem);
                    } else {
                        nodeIter.setLeft(newItem);
                    }
                    newItem.setParent(nodeIter);
                    newItem.subtreeIncrease();
                    return;
                } else {
                    nodeIter = childNode;
                }
            }
        }
    }

    @Override
    public void printTreeAlphabetically(PrintStream stream) {
        wordFreqMyLinkedList.sort(new Alphabetically());
        stream.println(wordFreqMyLinkedList.toString());
    }

    @Override
    public void printTreeByFrequency(PrintStream stream) {
        wordFreqMyLinkedList.sort(new WordFreq());
        stream.println(wordFreqMyLinkedList.toString());
    }

    static void traverseR5(WordFreqTreeNode n) {
        if (n == null)
            return ;
        if (wordFreqMyLinkedList == null) {
            wordFreqMyLinkedList = new MyLinkedList<>();
        }
        wordFreqMyLinkedList.insertAtFront(n.getWordFreqObj());
        traverseR5(n.getRight());
        traverseR5(n.getLeft());
    }

    public void addStopWord(String... words){
        if (stopWords == null)
            stopWords = new MyLinkedList<>();
        for (String word : words){
            stopWords.insertAtBack(word);
        }
    }

    @Override
    public WordFreq search(String item) {
        WordFreqTreeNode current = head, temp = new WordFreqTreeNode(item);

        while (true) {
            if (current == null) return null;
            else if (current.compareTo(temp) == 0){

                if (current.getWordFreqObj().getFrequency() > getMeanFrequency()) {
                    remove(current.getWordFreqObj().key());
                    rootInsert(current.getWordFreqObj());
                }
                return current.getWordFreqObj();
            }
            else if (current.compareTo(temp) < 0)
                current = current.getRight();
            else
                current = current.getLeft();
        }
    }
    public void rootInsert(WordFreq element){
        head = rootInsert(head, element, null);
    }
    private WordFreqTreeNode rootInsert(WordFreqTreeNode head, WordFreq element, WordFreqTreeNode parent) {

        if (head == null) {
            //the WordFrequencyBST specified by root is empty (it has 0 elements) -
            //initialize node with element and insert it at the WordFrequencyBST
            //(do not make any rotations)
            WordFreqTreeNode node = new WordFreqTreeNode(element);
            node.setParent(parent);
            node.subtreeIncrease();
            return node;
        }

        //WordFrequencyBST not empty
        //find subtree where we're going to insert element
        int result = element.key().compareToIgnoreCase(head.getWordFreqObj().key());

        if (result == 0) {
            //element equal to root
            //do not insert element in the WordFrequencyBST
            return head;
        }

        if (result < 0) {
            //element smaller than root
            //(1) insert element at the left subtree of root (recursively)
            WordFreqTreeNode leftSubtreeRoot = this.rootInsert(head.getLeft(), element, head);
            //(2) update root's left subtree
            head.setLeft(leftSubtreeRoot);
            //(3) perform a rotation at the opposite (right)
            head = this.rotateRight(head);

        }
        else {
            //element bigger than root
            //(1) insert element at the right subtree of root (recursively)
            WordFreqTreeNode rightSubtreeRoot = this.rootInsert(head.getRight(), element, head);
            //(2) update root's right subtree
            head.setRight(rightSubtreeRoot);
            //(3) perform a rotation at the opposite (left)
            head = this.rotateLeft(head);
        }

        //after each rotation, return the updated WordFrequencyBST
        return head;
    }

    public WordFreqTreeNode rotateLeft(WordFreqTreeNode pivot) {
        WordFreqTreeNode parent = pivot.getParent();
        WordFreqTreeNode child = pivot.getRight();

        //update pivot's parent's child with pivot's right child
        if (parent == null) head = child;

        else if (parent.getLeft() == pivot) {
            parent.setLeft(child);
        }
        else parent.setRight(child);

        child.setParent(pivot.getParent());
        //update pivot's parent with child
        pivot.setParent(child);
        //during update, child has 3 children (1 right, 1 initial left + 1 left (pivot))
        //WordFrequencyBST spec violation
        //pivot takes child's initial left child as its right child
        pivot.setRight(child.getLeft());
        //if child's left child exists, update it with its new parent (pivot)
        if (child.getLeft() != null) {
            child.getLeft().setParent(pivot);
        }
        //update child's new left child (pivot)
        child.setLeft(pivot);
        //return the new WordFrequencyBST root after rotation
        return child;
    }

    private WordFreqTreeNode rotateRight(WordFreqTreeNode pivot) {

        WordFreqTreeNode parent = pivot.getParent();
        WordFreqTreeNode child = pivot.getLeft();

        if (parent == null) {
            head = child;
        }
        else if (parent.getLeft() == pivot) {
            parent.setLeft(child);
        }
        else {
            parent.setRight(child);
        }

        child.setParent(pivot.getParent());
        pivot.setParent(child);
        pivot.setLeft(child.getRight());
        if (child.getRight() != null) { child.getRight().setParent(pivot);
        }
        child.setRight(pivot);
        return child;
    }


    @Override
    public void remove(String w) {
        // find node to delete and its parent
        WordFreqTreeNode current = head, parent = null, deleteItem = new WordFreqTreeNode(w);

        while (true) {
            if (current == null)
                return;

            if (current.compareTo(deleteItem) == 0)
                break;

            parent = current;

            if (current.compareTo(deleteItem) < 0)
                current = current.getRight();
            else
                current = current.getLeft();
        }

        WordFreqTreeNode replace;

        if (current.getLeft() == null)
            replace = current.getRight();
        else if (current.getRight() == null)
            replace = current.getLeft();
        else {
            WordFreqTreeNode findCurrent = current.getRight();

            while (true) {
                if (findCurrent.getLeft() != null)
                    findCurrent = findCurrent.getLeft();
                else
                    break;
            }

            // only has zero or one child (there is no left child!!!)
            remove(findCurrent.getWordFreqObj().key());

            findCurrent.setLeft(current.getLeft());
            findCurrent.setRight(current.getRight());

            replace = findCurrent;
        }

        if (parent == null) {
            head = replace;
        } else {
            if (parent.getLeft() == current)
                parent.setLeft(replace);

            if (parent.getRight() == current)
                parent.setRight(replace);
        }
    }


    @Override
    public int getTotalWords() {
        return traverseR(head);
    }

    private int traverseR(WordFreqTreeNode h) {
        if (h == null || stopWords.contains(h.getWordFreqObj().key()))
            return 0;
        return h.getWordFreqObj().getFrequency()
                + traverseR(h.getLeft()) + traverseR(h.getRight());
    }


    @Override
    public int getDistinctWords() {
        return head.subtreeSize + 1;
    }

    String searching;
    @Override
    public int getFrequency(String w) {
        searching = w;
        return traverseR2(head);
    }

    public int traverseR2(WordFreqTreeNode h) {
        if (h == null)
            return 0;  // not found
        else if (h.getWordFreqObj().key().equals(searching))
            return h.getWordFreqObj().getFrequency();
        return traverseR2(h.getRight()) + traverseR2(h.getLeft());
    }


    @Override
    public WordFreq getMaximumFrequency() {
        return search(TraverseR3(head));
    }
    int max = -1;
    String TraverseR3(WordFreqTreeNode h) {
        if (h == null)
            return null;
        else if (h.getWordFreqObj().getFrequency() > max)
            max = h.getWordFreqObj().getFrequency();
        return TraverseR3(h.getRight()) + TraverseR3(h.getLeft());
    }

    @Override
    public double getMeanFrequency() {
        return TraverseR4(head);
    }

    double sum = 0.0;
    double TraverseR4(WordFreqTreeNode h) {
        if (h == null)
            return 0.0;
        sum += h.getWordFreqObj().getFrequency();
        TraverseR4(h.getLeft()) ;
        TraverseR4(h.getRight());
        return sum/getDistinctWords();
    }

    @Override
    public void removeStopWord(String w) {
        if (stopWords.remove(w) == -1)
            System.out.println("Stopword does not exist");
    }

    public void printPreorder(){
        preorder(head);
        System.out.println();
    }
    public void preorder(WordFreqTreeNode n) {
        if (n == null)
            return ;
        if(!stopWords.contains(n.getWordFreqObj().key()))
            System.out.println(n.getWordFreqObj().key() + " ");
        preorder(n.getRight());
        preorder(n.getLeft());
    }

    public static MyLinkedList<String> getStopWords() {
        return stopWords;
    }
    public static MyLinkedList<WordFreq> getWordFreqList() {
        return wordFreqMyLinkedList;
    }
}
