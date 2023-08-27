import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class BST implements WordCounter {
    public static class TreeNode {

        WordFreq wordFreqObj;
        TreeNode left, right, parent;
        int subtreeSize;

        TreeNode(WordFreq t) {
            wordFreqObj = new WordFreq(t);
        }
        TreeNode(String item) {
            this.wordFreqObj = new WordFreq(item);
        }


        public WordFreq getWordFreqObj() {
            return wordFreqObj;
        }

        public TreeNode getLeft() {
            return left;
        }

        public TreeNode getRight() {
            return right;
        }

        public TreeNode getParent() {return parent;}

        public int getSubtreeSize() {
            return subtreeSize;
        }

        public void subtreeIncrease() {
            TreeNode current = this;
            while (current.parent != null) {
                current = current.parent;
                current.subtreeSize += 1;
            }
        }
        public void setItem(WordFreq item) {
            this.wordFreqObj = item;
        }

        public void setLeft(TreeNode left) {
            this.left = left;
        }

        public void setRight(TreeNode right) {
            this.right = right;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public void setSubtreeSize(int subtreeSize) {
            this.subtreeSize = subtreeSize;
        }

        /**
         * @return 0 means same value, 1 means bigger, 2 means same object, -1 means smaller
         */
        public int compareTo(TreeNode b) {
            if (this.getWordFreqObj().key().compareToIgnoreCase(b.getWordFreqObj().key()) == 0)
                return 0;
            else if (this.getWordFreqObj().key().compareToIgnoreCase(b.getWordFreqObj().key()) >= 0)
                return 1;
            else
                return -1;
        }
        public String toString() {
            return wordFreqObj.toString();
        }
    }
    private static TreeNode head;
    private static List<String> stopWords;
    private static List<WordFreq> wordFreqList;

    public static void main(String[] args) {
        BST a = new BST();
        a.addStopWord("να", "και", "τι", "μου", "με", "το", "την", "του", "τον", "δεν", "που", "για", "τα",
                        "η", "ο", "στο", "θα", "απ", "πως", "στην", "της", "σε", "αλλα", "ότι", "από", "οι", "των", "τη", "τις", "of", "στον");
        a.load("D:\\Projects\\domes-dedomenon-2021\\3rd-assignment\\text1.txt");

        traverseR5(head);
        a.printTreeByFrequency();
    }

    @Override
    public void printTreeAlphabetically(PrintStream stream) {
        wordFreqList.sort(new Alphabetically());
        stream.println(wordFreqList.toString());
    }

    @Override
    public void printTreeByFrequency() {
        wordFreqList.sort(new WordFreq());
        StringBuilder str = new StringBuilder();

        System.out.print(wordFreqList.toString());

        List<WordFreq>.Node<WordFreq> temp = wordFreqList.getHead();
        while (temp != null) {
            String word = temp.getData().getWord();
            if (!stopWords.contains(word)) {
                str.append(temp.getData().toString()).append("\n");
            }
            temp = temp.getNext();
        }
        System.out.println(str + "\nΤhe total number of different words used is: " + wordFreqList.getSize());
    }

    static void traverseR5(TreeNode n) {
        if (n == null)
            return ;
        if (wordFreqList == null) {
            wordFreqList = new List<>();
        }
        wordFreqList.insertAtFront(n.getWordFreqObj());
        traverseR5(n.getRight());
        traverseR5(n.getLeft());
    }

    public void addStopWord(String... words){
        if (stopWords == null)
            stopWords = new List<>();
        for (String word:words){
            stopWords.insertAtBack(word);
        }
    }

    @Override
    public void insert(String item) {

        if (head == null) {
            head = new TreeNode(item);
            return ;
        }

        TreeNode current = head, temp = new TreeNode(item);

        while (true) {
                if (current.compareTo(temp) == 0) {
                    current.getWordFreqObj().setFrequency(current.getWordFreqObj().getFrequency() + 1);
                return ;
            }
            else {
                if (current.compareTo(temp) < 0) {
                    if (current.getRight() == null) {
                        current.setRight(temp);
                        temp.setParent(current);
                        temp.subtreeIncrease();
                        return;
                    } else {
                        current = current.getRight();
                    }
                } else {
                    if (current.getLeft() == null) {
                        current.setLeft(temp);
                        temp.setParent(current);
                        temp.subtreeIncrease();
                        return;
                    } else {
                        current = current.getLeft();
                    }
                }
            }
        }
    }


    @Override
    public WordFreq search(String item) {
        TreeNode current = head, temp = new TreeNode(item);

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
    private TreeNode rootInsert(TreeNode head, WordFreq element, TreeNode parent) {

        if (head == null) {
            //the BST specified by root is empty (it has 0 elements) -
            //initialize node with element and insert it at the BST
            //(do not make any rotations)
            TreeNode node = new TreeNode(element);
            node.setParent(parent);
            node.subtreeIncrease();
            return node;
        }

        //BST not empty
        //find subtree where we're going to insert element
        int result = element.key().compareToIgnoreCase(head.getWordFreqObj().key());

        if (result == 0) {
            //element equal to root
            //do not insert element in the BST
            return head;
        }

        if (result < 0) {
            //element smaller than root
            //(1) insert element at the left subtree of root (recursively)
            TreeNode leftSubtreeRoot = this.rootInsert(head.getLeft(), element, head);
            //(2) update root's left subtree
            head.setLeft(leftSubtreeRoot);
            //(3) perform a rotation at the opposite (right)
            head = this.rotateRight(head);

        }
        else {
            //element bigger than root
            //(1) insert element at the right subtree of root (recursively)
            TreeNode rightSubtreeRoot = this.rootInsert(head.getRight(), element, head);
            //(2) update root's right subtree
            head.setRight(rightSubtreeRoot);
            //(3) perform a rotation at the opposite (left)
            head = this.rotateLeft(head);
        }

        //after each rotation, return the updated BST
        return head;
    }

    public TreeNode rotateLeft(TreeNode pivot) {
        TreeNode parent = pivot.getParent();
        TreeNode child = pivot.getRight();

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
        //BST spec violation
        //pivot takes child's initial left child as its right child
        pivot.setRight(child.getLeft());
        //if child's left child exists, update it with its new parent (pivot)
        if (child.getLeft() != null) {
            child.getLeft().setParent(pivot);
        }
        //update child's new left child (pivot)
        child.setLeft(pivot);
        //return the new BST root after rotation
        return child;
    }

    private TreeNode rotateRight(TreeNode pivot) {

        TreeNode parent = pivot.getParent();
        TreeNode child = pivot.getLeft();

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
        TreeNode current = head, parent = null, deleteItem = new TreeNode(w);

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

        TreeNode replace;

        if (current.getLeft() == null)
            replace = current.getRight();
        else if (current.getRight() == null)
            replace = current.getLeft();
        else {
            TreeNode findCurrent = current.getRight();

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
    public void load(String filename) {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line, la[];
                while ((line = br.readLine()) != null) {
                    line = line.replaceAll("[^α-ωά-ώΑ-ΩΆ-Ώa-zA-Z\\s']", "").replace("\t", " ").toLowerCase();
                    la = line.split(" ");
                    for (String s : la)
                        if (!((s.isBlank() || (s.startsWith("'") ||s.endsWith("'")) &&
                                (stopWords == null || !stopWords.contains(s)))))  // if stopwords variable hasn't been initialized an exception is thrown
                                    insert(s);
                }
            }
            catch (IOException ex) {System.out.println("file name is: " + filename); ex.printStackTrace();
            }
    }

    @Override
    public int getTotalWords() {
        return traverseR(head);
    }

    private int traverseR(TreeNode h) {
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


    public int traverseR2(TreeNode h) {
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
    String TraverseR3(TreeNode h) {
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
    double TraverseR4(TreeNode h) {
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
    public void preorder(TreeNode n) {
        if (n == null)
            return ;
        if(!stopWords.contains(n.getWordFreqObj().key()))
            System.out.println(n.getWordFreqObj().key() + " ");
        preorder(n.getRight());
        preorder(n.getLeft());
    }
}
