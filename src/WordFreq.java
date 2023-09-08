import java.text.Normalizer;
import java.util.Comparator;
public class WordFreq implements Comparator<WordFreq>{
    private int frequency;
    private String word, root, postfix;
    private static final MyLinkedList <String> verbPostfix = new MyLinkedList<>( "μαστε", "μασταν", "ομουν", "οσουν", "ομασταν" , "οσασταν" , "ομουνα", "οσουνα", "οτανε", "ουνταν", "σαστε", "ομαι", "εθηκες", "εθηκε", "εθηκαμε", "εθηκατε", "εθηκαν", "ονται",  "ωμαστε",
                                                                                    "ωσαστε","μαστε", "σαστε", "ξουμε", "ξετε", "ζεται", "ξουν", "ισεις", "σισουν", "σουμε", "θουμε", "ουμαι", "ηκαμε", "ζουμε", "ιαιμαι", "εθηκα", "εσαι", "εται","οντε", "εσε","σαμε", "σατε",
                                                                                    "οταν", "ιζουμε", "ιζετε", "σετε", "ουμε", "ειτε", "ουν", "ετε", "ουν", "ους", "άζω", "αζω", "αζεις", "αζει", "εις", "είς", "ηθηκα", "ηθηκες", "ηθηκε", "ειται", "εμαι", "μαι", "σαι", "ναι",
                                                                                    "αινω", "αινεις", "αινει", "αινουμε", "αινετε", "αινουν", "σουν", "νω", "νεις", "νει", "νουμε", "ουνε", "ηκε", "ηκα", "νουν", "εινει", "χτει", "ατων", "ησω", "ιζω", "αει", "εσε", "στω", "ανε",
                                                                                    "ησα","ησε", "σαν", "ξα", "ξω", "ξει", "ξεις", "σω", "σεις", "σει", "σεο", "σα", "σες", "σε", "ια", "ην", "αν", "αω", "αεις", "αει", "αμε", "ατε", "με", "αι", "οι", "ει", "ας", "ες", "ές", "ου",
                                                                                    "ον", "ζω", "ζει", "σω", "νω" );
    private static final MyLinkedList <String> nounPostfix = new MyLinkedList<>( "μενος", "μενο", "μενα", "μενοι", "μενη", "μενες", "οτητα", "οτα", "στης", "ουλα", "ατα", "ακι", "ωση", "ακια", "σος", "τι", "ης", "ων", "ος", "s", "ω", "η", "ε", "ς", "ν", "ο", "α" );
    public static MyLinkedList<String> rootWords = new MyLinkedList<>();


    public WordFreq(){}

    public WordFreq(String word) {
        this.word = word;
        frequency = 1;
        findRootAndPostfix(this);
    }
    public WordFreq(WordFreq wf) {
        this.word = wf.word;
        this.frequency = wf.frequency;
        findRootAndPostfix(wf);
    }

    /** The word is normalized before compared so that we don't need to include diacritics in our Postfix Sets.
     *  As a result all roots and postfixes are without diacritics */
    public static boolean findRootAndPostfix(WordFreq wf) {
        String NormalizedWord = Normalizer.normalize(wf.word, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); //removes diacritics

        for (String p : verbPostfix) {
            if (NormalizedWord.endsWith(p) && (NormalizedWord.length() - p.length()) >= 1) { // second condition: is compound word (sintheti leksi)
                String stringRoot = NormalizedWord.substring(0, NormalizedWord.length() - p.length());
                if (stringRoot.length() >= 2) {
                    wf.root = stringRoot;
                    wf.postfix = p;
                    return true;
                }
            }
        }

        for (String p : nounPostfix) {
            if (NormalizedWord.endsWith(p) && (NormalizedWord.length() - p.length()) >= 1) { // second condition: is compound word (sintheti leksi)
                String stringRoot = NormalizedWord.substring(0, NormalizedWord.length() - p.length());
                if (stringRoot.length() >= 2) {
                    wf.root = stringRoot;
                    wf.postfix = p;
                    return true;
                }
            }
        }

        return false;
    }

    public String key() {return word;}

    /** compares characters of a string ignoring diacritics and case */
    public int compareToIgnoreCaseWithoutDiacritics(WordFreq wf) {
        String normalizedWord1 = Normalizer.normalize(this.word, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String normalizedWord2 = Normalizer.normalize(wf.word, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalizedWord1.compareToIgnoreCase(normalizedWord2);
    }

    /** Compares frequency */
    @Override
    public int compare(WordFreq o1, WordFreq o2) {
        return Integer.compare(o1.frequency, o2.frequency);
    }

    public static String forToString(String s, int chars){
        int i = 0; StringBuilder result = new StringBuilder();
        if (s == null)
            return result.append("      ").toString();
        while (++i <= chars - s.length())
            result.append(" ");
        return result.toString();
    }

    public String toString() {
        return word + "  " + forToString(word,19) + root + " + " + postfix+  forToString(root + postfix,20) +"  Repetitions: " + frequency;
    }

    public void increaseFrequency(){ ++frequency; }
    public String getWord() { return word; }
    public String getRoot() { return root; }
    public int getFrequency() { return frequency; }
    public String getPostfix() { return postfix; }

}

