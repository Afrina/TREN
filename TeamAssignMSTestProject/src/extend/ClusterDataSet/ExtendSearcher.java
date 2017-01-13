package extend.ClusterDataSet;



import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;





public class ExtendSearcher {
	String index;
	IndexReader reader;
	Stemmer stemmer;
	ArrayList<String> indexedDeveloperList;
	ArrayList<String> reportingDates;
	
	
	public ExtendSearcher(String indexPath) throws IOException{
		index = indexPath;
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		stemmer = new Stemmer();
		indexedDeveloperList = new ArrayList<String>();
		reportingDates = new ArrayList<String>();
		
	}
	
	public void prepareIndexedDeveloperList(ScoreDoc[] hits) throws IOException{
		IndexSearcher searcher = new IndexSearcher(reader);
		for(int i=0;i<hits.length;i++){
			Document d = searcher.doc(hits[i].doc);
			indexedDeveloperList.add(d.getField(LuceneConstants.Name).stringValue());
			}
	}
	public ArrayList<String> getIndexedDeveloperList(){
		return indexedDeveloperList;
	}
	public ArrayList<String> getReportingDateList(ScoreDoc[] hits) throws IOException{
		IndexSearcher searcher = new IndexSearcher(reader);
		for(int i = 0;i<hits.length;i++){
			System.out.println(searcher.doc(hits[i].doc).getField(LuceneConstants.SUMMARY).stringValue());
			reportingDates.add(searcher.doc(hits[i].doc).getField(LuceneConstants.TIME).stringValue());
		}
		return reportingDates;
	}
	public ScoreDoc[] search() throws Exception{
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(LuceneConstants.CUSTOM_STOP_WORDS_SET);
		ScoreDoc[] hits = searchDoc("*:*" , analyzer, searcher);
		
		//print();
		//reader.close();
		return hits;
	}
	public ArrayList<Map<String, Double>> getMaps(ScoreDoc[] hits) throws IOException{
		ArrayList<Map<String, Double>> docs = new ArrayList<Map<String,Double>>();
		ExtendIndeing indexing = new ExtendIndeing();
		for(int i=0;i<hits.length;i++){
			docs.add(indexing.getMap(i,index));	
		}
		return docs;
	}
	public ArrayList<Document> getReports(ScoreDoc[] hits) throws IOException{
		IndexSearcher searcher = new IndexSearcher(reader);
		ArrayList<Document> reports = new ArrayList<Document>();
		for(int i=0;i<hits.length;i++){
			reports.add(searcher.doc(hits[i].doc));
			Document d = searcher.doc(hits[i].doc);
		}
		System.out.println("seee i" + hits.length);
		return reports;
	}

	public IndexReader getReader(){
		return reader;
	}

	public ScoreDoc[] searchDoc(String word, Analyzer analyzer,IndexSearcher searcher) throws IOException, ParseException{

		QueryParser parser = new QueryParser(LuceneConstants.CONTENT, analyzer);
		String queryString = word ;
		queryString = queryString.trim();
		Query query = parser.parse(queryString);
		//System.out.println("Searching for: " + query.toString(field));
		TopDocs results = searcher.search(query, reader.maxDoc());
		ScoreDoc[] hits = results.scoreDocs;
		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");
		Document d = searcher.doc(hits[0].doc);
		
		return hits;
	}

}
