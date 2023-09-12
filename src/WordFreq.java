import java.text.Normalizer;
import java.util.Comparator;
public class WordFreq implements Comparator<WordFreq>{
    private int frequency;
    private String word, root, postfix, type;
    private static final MyLinkedList <String> verbPostfix = new MyLinkedList<>("ζομαστε", "ζοταν", "αζεσαι", "αζεται", "ζεται", "αζομαι", "ομαστε", "ομασταν", "μαστε", "ομασταν" , "οσασταν", "ομουνα", "οσουνα", "ουσαμε", "ησαμε", "ησατε", "ησαν", "ησεις", "ησουμε", "αγαμε",
                                                                                    "αξουμε", "αχνω", "αχνεται", "ασεις", "νουν", "ασουμε", "ουσε", "ουσες", "οτανε", "ουνταν","μασταν", "ομουν", "οσουν",  "σαστε", "τομαι", "ομαι", "εθηκες", "εθηκε", "εθηκαμε", "εθηκατε", "εθηκαν",
                                                                                    "ζονται", "ονται",  "ωμαστε", "ωσαστε","μαστε", "σαστε", "αζεσαι", "αστηκε", "αστει", "στηκα", "στηκη", "ξουμε", "ξετε", "ξουν", "ισεις", "σισουν", "σουμε", "ηθουμε", "θουμε", "ουμαι", "ηκαμε",
                                                                                    "ζουμε", "ιαιμαι", "εθηκα", "εσαι", "εται","οντε", "εσε","σαμε", "σατε", "χνω", "χνεις", "χνει",
                                                                                    "χνουμε", "χναμε", "χνατε", "χνετε", "χνεται", "χνουν", "χναν", "στει", "στηκε", "οταν", "ιζουμε", "χτω", "χτηκα", "τομαι", "τομουν", "τει", "ψει", "ιζετε", "σετε",
                                                                                    "νουμε", "νετε", "νουν", "νω", "νεις", "νει", "ναμε", "νατε", "ναν", "νουμε", "θεις", "ηθηκα", "ηθηκες", "ηθηκε", "ειται", "εμαι", "μαι", "σαι", "ναι", "ησει", "ασαν", "νανε", "αζαν",
                                                                                    "αινω", "αινεις", "αινει", "αινουμε", "αινετε", "αινουν", "σουν", "ουνε", "ηκε", "ηκα", "ουσα", "ουω", "αζετε", "αζουμε", "αζει", "αξει", "αζω", "αξω", "αζα", "ασω", "ασει", "ασα",
                                                                                    "αξα", "ζων", "ζας", "ζης", "ασε", "εινει", "χτει", "ησω", "ναει", "εσε", "χνω", "ειτε", "ετε", "ουν",
                                                                                    "στω", "ανε", "θουν", "ναω", "αει","ουμε", "αεις", "ξεις", "ζει", "ζουν", "θει", "είς", "αμε", "ατε", "αγα", "αμε", "αγε", "εσει", "ησα","ησε", "σαν", "ξα", "ξω", "ξει","εσω", "σω",
                                                                                    "σει", "σεο", "σες", "σα", "σε", "ην", "αν", "αω", "αι", "ές", "θω", "εις", "ει", "νε", "ον",  "ζω", "ζα",  "σω", "νω", "με", "τω", "φω", "ψω", "ψα", "φε", "ψε", "ω");
    private static final MyLinkedList <String> nounPostfix = new MyLinkedList<>("μενος", "μενο", "μενα", "μενοι", "μενη", "μενες", "οτητα", "οντας", "οτα", "στης", "ουλα", "ατα", "ατων", "ακι", "ωση", "ακια", "σος", "σεων", "σεις", "ηση", "ους", "ση", "ου", "ας", "οι", "τι", "ης", "ων", "ος", "ες", "s", "η", "ε", "ς", "ν", "ο", "α" );
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
                    wf.type = "verb";
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
                    wf.type = "noun or adjective";
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
        return word + "  " + forToString(word,19) + root + " + " + postfix+  forToString(root + postfix,20) +"  Repetitions: " + frequency + " type: " + type;
    }

    public void increaseFrequency(){ ++frequency; }
    public String getWord() { return word; }
    public String getRoot() { return root; }
    public int getFrequency() { return frequency; }
    public String getPostfix() { return postfix; }
    public String getType() { return type; }

}

