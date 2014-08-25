package cs276.pe1.spell;

import java.io.File;
import java.util.*;

import cs276.util.Counter;
import cs276.util.IOUtils;
import cs276.util.StringUtils;

public class KGramSpellingCorrector implements SpellingCorrector {

    private Map<String, Set<String>> bigramIndex;
    private Counter<String> wordCounter;

    public int getCount(String word){
        return (int) wordCounter.getCount(word);
    }

	/** Initializes spelling corrector by indexing kgrams in words from a file */

	public KGramSpellingCorrector() {
        bigramIndex = new HashMap<String, Set<String>>();
        wordCounter = new Counter<String>();

		File path = new File("data/big.txt.gz");
		for (String line : IOUtils.readLines(IOUtils.openFile(path))) {
			for (String word : StringUtils.tokenize(line)) {
                if(wordCounter.containsKey(word)){
                    wordCounter.incrementCount(word);
                } else {
                    wordCounter.setCount(word, 1);
                }
                char prevLetter = '\0';
                for (int i = 0; i < word.length(); i++){
                    char c = word.charAt(i);
                    if(prevLetter == '\0') {
                        // first letter
                        String indexTerm = new StringBuilder().append("$").append(c).toString();
                        addToIndex(indexTerm, word);
                    } else if (i == word.length() - 1){
                        // last letter
                        String indexTerm1 = new StringBuilder().append(prevLetter).append(c).toString();
                        addToIndex(indexTerm1, word);
                        String indexTerm2 = new StringBuilder().append(c).append("$").toString();
                        addToIndex(indexTerm2, word);
                    } else {
                        // a letter in the middle
                        String indexTerm1 = new StringBuilder().append(prevLetter).append(c).toString();
                        addToIndex(indexTerm1, word);
                    }
                    prevLetter = c;
                }
			}
		}

	}

    private void addToIndex(String term, String word){
        if (bigramIndex.containsKey(term)) {
            bigramIndex.get(term).add(word);
        } else {
            Set<String> wordsForTerm = new HashSet<String>();
            wordsForTerm.add(word);
            bigramIndex.put(term, wordsForTerm);
        }
    }

	public List<String> corrections(String word) {
		// TODO provide spelling corrections here
        char prevLetter = '\0';
        Set<String> prevSet;
        Counter<String> counter = new Counter<String>();
        for (int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(prevLetter == '\0') {
                // first letter
                String indexTerm = new StringBuilder().append("$").append(c).toString();
                prevSet = bigramIndex.get(indexTerm);
                addSetToCounter(prevSet, counter);
            } else if (i == word.length() - 1){
                // last letter
                String indexTerm1 = new StringBuilder().append(prevLetter).append(c).toString();
                prevSet = bigramIndex.get(indexTerm1);
                addSetToCounter(prevSet, counter);
                String indexTerm2 = new StringBuilder().append(c).append("$").toString();
                prevSet = bigramIndex.get(indexTerm2);
                addSetToCounter(prevSet, counter);
            } else {
                // a letter in the middle
                String indexTerm1 = new StringBuilder().append(prevLetter).append(c).toString();
                prevSet = bigramIndex.get(indexTerm1);
                addSetToCounter(prevSet, counter);
            }
            prevLetter = c;
        }
        List<String> top = counter.topK(100);
		return top;
	}

    private void addSetToCounter(Set<String> set, Counter<String> counter) {
        if(set != null) {
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                if (counter.containsKey(next)) {
                    counter.incrementCount(next);
                } else {
                    counter.setCount(next, 1);
                }
            }
        }
    }

}
