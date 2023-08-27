import java.util.Comparator;

public class WordFreq implements Comparator<WordFreq>{
    public String getWord() {
        return word;
    }

    private String word;
    private int frequency;

    public WordFreq(){}

    public WordFreq(String word) {
        this.word = word;
        frequency = 1;
    }
    public WordFreq(WordFreq t) {
        this.word = t.word;
        this.frequency = t.frequency;
    }

    public String key () {return word;}
    public int getFrequency() {return frequency;}
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compare(WordFreq o1, WordFreq o2) {
        return Integer.compare(o1.frequency, o2.frequency);
    }

    public static String forToString(String s){
        int i = 0; StringBuilder result = new StringBuilder();
        while (++i <= 20 - s.length())
            result.append("-");
        return result.toString();
    }

    public String toString() {
        return word + " " + forToString(word)  + " Numbers of repetition: " + frequency;
    }

}

