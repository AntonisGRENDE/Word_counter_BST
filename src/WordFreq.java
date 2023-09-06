import java.text.Normalizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class WordFreq implements Comparator<WordFreq>{
    private int frequency;
    private String word, root, postfix;
    private static Set <String> verbPostfix = new HashSet<>(Arrays.asList("ομαι", "εσε", "ετε", "ωμαστε", "ωσαστε", "μαστε", "σαστε", "ξουμε", "ξετε", "ζεται", "ξουν", "σισεις", "σισουν", "σουμε", "σαμε", "ιζουμε", "ιζετε", "σετε", "ουμε", "ειτε", "ουν", "ετε", "ουν", "ους",
                                "άζω", "αζω", "αζεις", "αζει", "εις", "είς", "ειται", "τος", "οτα", "αινω", "αινεις", "αινει", "αινουμε", "αινετε", "αινουν", "νω", "νεις", "νει", "νουμε", "νουν", "εινει", "αει", "εσε", "στω", "ανε", "ησε",
                                "αι", "οι", "εί", "ει", "ος", "ας", "ες","ές", "ου", "ον", "κα", "ια", "νω", "κες", "κε", "καμε", "κια", "ξα", "ξω", "ξει", "ξεις", "σα", "σες", "σε", "ια", "ην", "αν", "αω", "αεις", "αει", "αμε", "ατε"));
    private static Set <String> nounPostfix = new HashSet<>(Arrays.asList("ότητα", "οτα", "στης", "τι", "κι", "κη", "κης", "ης", "τα", "s", "ω", "ώ", "ά", "ή", "η", "ε", "ό", "ς", "ν", "ο", "α" ));
    public static Set<String> rootWords = new HashSet<String>();


    public WordFreq(){}

    public WordFreq(String word) {
        this.word = word;
        frequency = 1;
        //rootExists(word);
    }
    public WordFreq(WordFreq t) {
        this.word = t.word;
        this.frequency = t.frequency;
    }

    public static boolean rootExists(WordFreq wf) {
        return checkPostfixes(wf, verbPostfix) || checkPostfixes(wf, nounPostfix);
    }

    private static boolean checkPostfixes(WordFreq wordFreqInstance, Set<String> postfix) {
        String stringRoot = "";
        String word = wordFreqInstance.word;
        for (String p : postfix){
            if (word.endsWith(p) && (word.length() - p.length()) >= 1) { // second condition: is compound word sintheti leksi
                stringRoot = word.substring(0, word.length() - p.length());
                if (stringRoot.length() >= 2) {
                    wordFreqInstance.root = stringRoot;
                    wordFreqInstance.postfix = p;
                    if (rootWords.contains(stringRoot)) {
                        return true;                        //the root exist so we need to increase the frequency
                    } else {
                        rootWords.add(stringRoot);   //if the stringRoot has been found but does not exist it must be added to the list
                        return false;
                    }
                }
            }
        }
        return false;
    }
    public String key () {return word;}

    /** compares characters of a string ignoring diacritics and case */
    public int compareTo(Object ob){
        String string = "";
        if (ob instanceof WordFreq)
            string = ((WordFreq) ob).word;
        else if (ob instanceof String)
            string = ((String) ob);
        String tempWord = removeDiacritics(string), tempWord2 = removeDiacritics(this.word);
        return tempWord.compareToIgnoreCase(tempWord2);
    }

    public static String removeDiacritics(String input){
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

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
        return word + "  " + forToString(word,15) + root + " + " + postfix+  forToString(root + postfix,10) +"  Repetitions: " + frequency;
    }

    public void increaseFrequency(){ ++frequency; }
    public String getWord() { return word; }
    public String getRoot() { return root; }
    public int getFrequency() { return frequency; }
    public String getPostfix() { return postfix; }

}

