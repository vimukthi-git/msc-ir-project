package cs276.pe1.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by vimukthib on 8/24/14.
 */
public class IMDBSearcher {
    public static void main(String[] args) throws ParseException, IOException {
        IndexSearcher searcher = new IndexSearcher(FSDirectory.open(IMDBIndexer.getIndexPath()));
        TopScoreDocCollector collector = TopScoreDocCollector.create(20, true);
        //Query query = getAuthorQuery();
        Query query = getTitleQuery();
        //Query query = getAuthorQuery();
        searcher.search(query, collector);

        printResults(searcher, collector);
    }

    private static Query getAuthorQuery() throws ParseException {
        QueryParser authorParser = new QueryParser("BY", new StandardAnalyzer(Version.LUCENE_CURRENT));
        return authorParser.parse("Marco");
    }

    private static Query getPlotProximityQuery() throws ParseException {
        QueryParser parser = new QueryParser("PL", new StandardAnalyzer(Version.LUCENE_CURRENT));
        return parser.parse("\"murdered eighteen\"~5");
    }

    private static Query getTitleQuery() throws ParseException {
        QueryParser parser = new QueryParser("MV", new StandardAnalyzer(Version.LUCENE_CURRENT));
        return parser.parse("timmy");
    }

    private static void printResults(IndexSearcher searcher, TopScoreDocCollector collector) throws IOException {
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = searcher.doc(hits[i].doc);  // getting actual document
            System.out.println("Title: " + hitDoc.get("MV"));
            System.out.println("Plot: " + hitDoc.get("PL"));
            System.out.println("Author: " + hitDoc.get("BY"));
            System.out.println();
        }
    }
}
