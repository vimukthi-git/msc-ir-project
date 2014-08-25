package cs276.pe1.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;

import cs276.pe1.lucene.IMDBParser.MoviePlotRecord;

public class IMDBIndexer {

	public static void main(String[] argv) throws Exception {

		File indexPath = getIndexPath();
		indexPath.mkdir(); 

		@SuppressWarnings("deprecation")
		IndexWriter writer = new IndexWriter(indexPath, getLuceneAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
		int cnt = 0;
		for (MoviePlotRecord rec : IMDBParser.readRecords()) { // index each record
            addDoc(writer, rec);
			cnt++;
		}
		writer.close();
		System.out.println("Added a total of " + cnt + " records to the index.");
		System.err.println("Done"); 
	}

    private static void addDoc(IndexWriter w, MoviePlotRecord movie) throws IOException {
        Document doc = new Document();
        doc.add(new Field("MV", movie.title, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("PL", movie.plots, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("BY", movie.authors, Field.Store.YES, Field.Index.ANALYZED));
        w.addDocument(doc);
    }

	/**
	 * Get the correct analyzer to use when instantiating a QueryParser.
	 * 
	 * @return the analyzer used to build the index 
	 */
	public static Analyzer getLuceneAnalyzer() {
		return new StandardAnalyzer(Version.LUCENE_CURRENT);
	}

	/**
	 * @return the path of the IMDB index
	 */
	public static File getIndexPath() {
		return new File(new File(System.getProperty("user.home")),"IR-project-index");
	}
}
