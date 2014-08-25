package cs276.pe1.spell;

import cs276.util.StringUtils;

import java.util.*;

/**
 * Created by vimukthib on 8/24/14.
 */
public class KGramWithEditDistanceSpellingCorrector implements SpellingCorrector {

    private KGramSpellingCorrector corrector;

    public KGramWithEditDistanceSpellingCorrector(){
        corrector = new KGramSpellingCorrector();
    }

    @Override
    public List<String> corrections(final String word) {
        List<String> corrections = corrector.corrections(word);
        Collections.sort(corrections, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return (int) ((StringUtils.levenshtein(c1, word) - StringUtils.levenshtein(c2, word)) * 100);
            }
        });
//        List<String> top = corrections.subList(0, 10);
//        Collections.sort(top, new Comparator<String>() {
//            @Override
//            public int compare(String c1, String c2) {
//                return (int) (corrector.getCount(c2) - corrector.getCount(c1));
//            }
//        });
//        System.out.println("Misspelled word - " + word);
//        System.out.println("Suggestions - " + corrections);
        return corrections;
    }
}
