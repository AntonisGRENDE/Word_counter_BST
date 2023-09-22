import java.io.PrintStream;

public interface WordCounter {
    void insert(String w, boolean sameRoot);
    WordFreq search(String w);
    void remove(String w);
    void load(String filename);
    int getTotalWords(WordFreqBST.WordFreqTreeNode h);
    int getDistinctWords();
    int getFrequency(String w, WordFreqBST.WordFreqTreeNode h);
    WordFreq getMaximumFrequency();
    double getMeanFrequency(WordFreqBST.WordFreqTreeNode h);
    void addStopWord(String... w);
    void removeStopWord(String w);
    void printTreeAlphabetically(PrintStream stream);
    void printTreeByFrequency(PrintStream stream);
}