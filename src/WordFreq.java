import java.text.Normalizer;
import java.util.Arrays;
import java.util.Comparator;
public class WordFreq implements Comparator<WordFreq>{
    private int frequency;
    private String word, root, postfix, type[];
    private static final MyLinkedList <String> verbPostfix = new MyLinkedList<>("ζομαστε", "ζοταν", "ζεσαι", "ζεται", "ζεται", "ζομαι", "ομαστε", "ομασταν", "μαστε", "ομασταν" , "οσασταν", "ομουνα", "οσουνα", "ουσαμε",
                                                                                    "νησα", "νησε", "νησαμε", "νησατε", "νησαν", "νουμε", "νετε", "νουν", "ασαμε", "ασατε", "ασουμε", "σαμε", "σατε", "σατε", "σαν", "σεις", "σουμε", "αγαμε",
                                                                                    "ξουμε", "χνω", "χνεται", "σεις", "νουν", "ουσε", "ουσες", "οτανε", "ουνταν","μασταν", "ομουν", "οσουν",  "σαστε", "τομαι", "ομαι", "εθηκες", "εθηκε", "εθηκαμε", "εθηκατε", "εθηκαν",
                                                                                    "ζονται", "ονται",  "ωμαστε", "ωσαστε","μαστε", "σαστε", "ζεσαι", "στηκε", "στει", "στηκα", "στηκη", "ισεις", "σισουν", "σουμε", "ηθουμε", "θουμε", "ουμαι", "ηκαμε",
                                                                                    "ζουμε", "ιαιμαι", "εθηκα", "εσαι", "εται","οντε", "εσε", "χνω", "χνεις", "χνει", "ζουν", "νουσαμε", "ναμε", "νας", "νησει", "ουσαν", "ηθηκα",
                                                                                    "χνουμε", "χναμε", "χνατε", "χνετε", "χνεται", "χνουν", "χναν", "στει", "στηκε", "οταν", "ιζουμε", "χτω", "χτηκα", "τομαι", "τομουν", "τει", "ψει", "ιζετε", "σετε", "αξουνε", "αχνω","αξεις", "αξει", "αξουμε", "αξετε", "αξουν", "αγμενα", "αχνω",
                                                                                    "θεις", "κα", "κες", "κε", "ειται", "εμαι", "μαι", "σαι", "ναι", "νανε", "αζαν", "τσε", "ευω", "ευει", "θηκε",
                                                                                    "αινω", "αινεις", "αινει", "αινουμε", "αινετε", "αινουν", "σουν", "ζουνε", "ηκε", "ηκα", "αξα", "ζων", "ζας", "ζης", "χτει", "ναει", "εσε", "χνω", "ειτε", "ετε", "ουν", "νεται", "εψα", "αμε", "αω", "αει", "ησει",
                                                                                    "ασα", "αζεται", "ασω", "ασεις", "ασει", "ασουμε", "ασετε", "ασουν", "ηνω", "ηνεις", "ηνει", "ηνουμε", "ηνετε", "ηνουν", "αγε", "αγες", "αγε", "αγαμε", "αγατε", "αγαν", "αζω", "αζεις", "αζει", "αζουμε", "αζετε", "αζουν", "αξω",
                                                                                    "ησες", "ησαμε","ησατε","ησαν", "ησω", "ησεις", "ησα", "ησεις", "ησει", "ησουμε", "ησετε" , "ησουν", "ηθηκαν", "ηθηκε", "ησε", "αγα", "ασε",
                                                                                    "στω", "ανε", "θουν","ουμε", "αεις", "ξεις", "ζει", "ουνε", "ζουν", "θει", "είς", "ατε", "γα", "με", "γε", "εσει","ησε", "σαν", "ξα", "ξω", "ξουνε", "ξεις", "ξει", "ξουμε", "ξετε", "ξουν", "σω", "υω", "σω", "ζεις", "νεις", "νει", "ναμε", "νατε", "ναν",
                                                                                    "σει", "σεο", "σες", "σα", "σε", "αν", "ας", "αι", "ές", "θω", "εις", "ει", "νε", "ον",  "ζω", "ζα", "φα", "να", "σω", "νω", "με", "τω", "φω", "ψω", "ψα", "φε", "ψε", "ω");
    private static final MyLinkedList <String> nounPostfix = new MyLinkedList<>("μενος", "μενο", "μενα", "μενοι", "μενη", "μενες", "οτητα", "οντας", "τσες", "οτα", "στης", "ουλα", "ατα", "ατη", "ατο", "ατου", "ατος", "ατες", "ατων", "κων", "ακι", "ωση", "τσα", "ακια", "τσικα", "σος", "σεων", "σεις", "ους", "κος",
                                                                                    "ην", "κο", "ση", "ου", "κη", "οι", "τι", "ης", "ων", "ος", "ες", "s", "η", "ε", "ς", "ν", "ο", "α" );
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
    public static void findRootAndPostfix(WordFreq wf) {
        String NormalizedWord = Normalizer.normalize(wf.word, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); //removes diacritics
        for (String p : verbPostfix) {
            if (NormalizedWord.endsWith(p) && (NormalizedWord.length() - p.length()) >= 1) { // second condition: is compound word (sintheti leksi)
                String stringRoot = NormalizedWord.substring(0, NormalizedWord.length() - p.length());
                if (stringRoot.length() >= 2) {
                    wf.root = stringRoot;
                    wf.postfix = p;
                    if (NormalizedWord.endsWith("εις"))
                        wf.type = "verb or noun or adverb".split(" or ");
                    else if (NormalizedWord.endsWith("κα"))
                        wf.type = "verb or noun".split(" or ");
                    else if (NormalizedWord.endsWith("σα") && NormalizedWord.length() < 4)
                        wf.type = "noun or adjective".split(" or ");
                    else if (NormalizedWord.endsWith("κες") || NormalizedWord.endsWith("ας")){
                        wf.type = "verb or noun or adjective".split(" or ");
                    }
                    else
                        wf.type = new String[]{"verb"};
                    break;
                }
            }
        }

        for (String p : nounPostfix) {
            if (NormalizedWord.endsWith(p) && (NormalizedWord.length() - p.length()) >= 1) { // second condition: is compound word (sintheti leksi)
                String stringRoot = NormalizedWord.substring(0, NormalizedWord.length() - p.length());
                if (stringRoot.length() >= 2) {
                    if (wf.postfix == null || p.length() > wf.postfix.length()) {
                        wf.root = stringRoot;
                        wf.postfix = p;
                        wf.type = "noun or adjective".split(" or ");
                        break;
                    }
                }
            }
        }
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

    public boolean containsType(String[] typeArr){
        if (this.type == null || typeArr == null)
            return false;
        else {
            for (String thisType : type)
                for (String t : typeArr)
                    if (thisType.equals(t))
                        return true;
        }
        return false;
    }

    public static String forToString(String s, int chars){
        int i = 0; StringBuilder result = new StringBuilder();
        if (s == null)
            s = "    ";
        while (++i <= chars - s.length())
            result.append(" ");
        return result.toString();
    }


    public String toString() {
        return word + "  " + forToString(word,19) + root + " + " + postfix + forToString(root + postfix,20) + " type: " + Arrays.toString(type) + forToString(Arrays.toString(type),23) + "Repetitions: " + frequency ;
    }

    public void increaseFrequency(){ ++frequency; }
    public String getWord() { return word; }
    public String getRoot() { return root; }
    public int getFrequency() { return frequency; }
    public String getPostfix() { return postfix; }
    public String[] getType() { return type; }

}

