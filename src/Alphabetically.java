import java.util.Comparator;
public class Alphabetically implements Comparator<WordFreq> {

    public int compare(WordFreq first, WordFreq second) {
        return second.key().compareTo(first.key());
    }
}
