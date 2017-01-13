package extend.ClusterDataSet;







import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import CommitParser.DeveloperCommitInfo;
import StringFormatter.StringFormatter;

public class ExtendIndeing {
	
	public Stemmer stemmer ;
	public HashMap<String, DeveloperInfo> developers;
	public final Set<String> terms = new HashSet<>();
	
	
	public ExtendIndeing(HashMap<String, DeveloperInfo> _developers){
		developers = _developers;
		stemmer = new Stemmer();
		
	}
	public ExtendIndeing(){
		stemmer = new Stemmer();
	}
	
	public String doFormatting(String identifier){
		StringFormatter formatter = new StringFormatter(identifier);
		identifier = formatter.getFormattedString();
		return identifier;
	}

	public void createIndex(){
		index(true);
	}
	public void index(boolean isNewIndex){
		String indexPath = LuceneConstants.INDEX;
		boolean create = isNewIndex;

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");

			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new StandardAnalyzer(LuceneConstants.CUSTOM_STOP_WORDS_SET);
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			if (create) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			}


			//iwc.setRAMBufferSizeMB(1024.0);

			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer);

			writer.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (Exception e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
			System.out.println(e);
		}
	}

	public void indexDocs(IndexWriter writer) throws IOException {
		//System.out.println("here");
		for (String key: developers.keySet()) {
			indexDoc( writer, key, developers.get(key));
		}
		//System.out.println("got matches: "+x);

	}
	 public static final FieldType TYPE_STORED = new FieldType();

	    static {
	        TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
	        TYPE_STORED.setTokenized(true);
	        TYPE_STORED.setStored(true);
	        TYPE_STORED.setStoreTermVectors(true);
	        TYPE_STORED.setStoreTermVectorPositions(true);
	        TYPE_STORED.freeze();
	    }

	public void indexDoc(IndexWriter writer, String name, DeveloperInfo dev) throws IOException {
		//System.out.println("stated here.....");
		// make a new, empty document
		Document doc = new Document();
		
		doc.add(new Field(LuceneConstants.CONTENT, stemmer.englishStemer(dev.commitContent+" "+dev.reportContent), TYPE_STORED));
		doc.add(new Field(LuceneConstants.Name, name, TYPE_STORED));
		
//System.out.println("dev name: "+name);
		

		if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
			writer.addDocument(doc);
		} else {
			writer.updateDocument(new Term("content",dev.commitContent+" "+dev.reportContent), doc);
		}
	}
	 Map<String, Double> getWieghts(IndexReader reader, int docId)
	            throws IOException {
	        Terms vector = reader.getTermVector(docId, LuceneConstants.CONTENT);
	        Document d = reader.document(docId);
	        //System.out.println("xxxxxxxxxxxxxxxxxxxxx "+d.getField(LuceneConstants.Name).stringValue());
	        Map<String, Integer> docFrequencies = new HashMap<>();
	        Map<String, Integer> termFrequencies = new HashMap<>();
	        Map<String, Double> tf_Idf_Weights = new HashMap<>();
	        TermsEnum termsEnum = null;
	        DocsEnum docsEnum = null;


	        termsEnum = vector.iterator();
	        BytesRef text = null;
	        while ((text = termsEnum.next()) != null) {
	            String term = text.utf8ToString();
	            if(term.contains("'")||term.contains(",")){
	            	//System.out.println(term);
	            	continue;}
	            if(isNumeric(term)){continue;}
	            int docFreq = termsEnum.docFreq();
	            docFrequencies.put(term, reader.docFreq( new Term( LuceneConstants.CONTENT, term ) ));

//	            docsEnum = termsEnum.docs(null, null);
//	            while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
	            //  termFrequencies.put(term, docsEnum.freq());
	                int freq = (int) termsEnum.totalTermFreq();
	                termFrequencies.put(term, freq);
	          //  }
	            
	            //////
	           // while ((text = termsEnum.next()) != null) {
	    			//String term = text.utf8ToString();
	    			
	    			//terms.add(term);
	    		

	            terms.add(term);
	        }

	        for ( String term : docFrequencies.keySet() ) {
	            int tf = termFrequencies.get(term);
	            int df = docFrequencies.get(term);
	            double idf = ( 1 + Math.log(reader.maxDoc()) - Math.log(df) );
	            double w = tf * idf;
	            tf_Idf_Weights.put(term, w);
	            //System.out.printf("Term: %s - tf: %d, df: %d, idf: %f, w: %f\n", term, tf, df, idf, w);
	        }

	        //System.out.println( "Printing docFrequencies:" );
	        //printMap(docFrequencies);

	        //System.out.println( "Printing termFrequencies:" );
	        //printMap(termFrequencies);

	        //System.out.println( "Printing if/idf weights:" );
	       // printMapDouble(tf_Idf_Weights);
	        return tf_Idf_Weights;
	    }
	public Set<String> getTerms() {
		// TODO Auto-generated method stub
		return terms;
	}
	public boolean isNumeric(String s){
		try  
		  {  
		    double d = Double.parseDouble(s);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;
		
	}
	public Map<String, Double> getMap(int i,String indexPath) throws IOException{
		ExtendSearcher searcher = new ExtendSearcher(indexPath);
		IndexReader reader = searcher.getReader();
		
		ExtendIndeing indexer = new ExtendIndeing();
		Map<String, Double> originalMap = indexer.getWieghts(reader, i);
	
		return originalMap;
	}
	public RealVector toRealVector(Map<String, Double> map,Set<String> terms) {
		RealVector vector = new ArrayRealVector(terms.size());
		int i = 0;
		double value = 0;
		for (String term : terms) {

			if ( map.containsKey(term) ) {
				value = map.get(term);
			}
			else {
				value = 0;
			}
			vector.setEntry(i++, value);
		}
		return vector;
	}
	
	
}
