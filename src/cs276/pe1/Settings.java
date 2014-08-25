package cs276.pe1;

/**
 * Single package that stores settings.
 * 
 * @author tim
 */
public class Settings {

	
	public static String getBigTxtPath() {
		return "data/big.txt.gz";
	}
	
	public static String getImdbPath() {
		return "data/imdb-plots-20081003.list.gz";
	}
	
	public static String getSpellTestPath() {
		return "data/spelltest.txt";
	}
	
	public static int numCorrectionsToReturn(){
		return 10;
	}
	
}
