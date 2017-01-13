package extend.ClusterDataSet;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;

public class TfIdfCalculator {
	IndexReader reader; ExtendIndeing indexer = new ExtendIndeing();
	ScoreDoc[] hits;
	ArrayList<RealVector> documentCollection;
	public TfIdfCalculator(ScoreDoc[] _hits) throws IOException{
		ExtendSearcher searcher = new ExtendSearcher(LuceneConstants.INDEX);
		reader = searcher.getReader();
		hits = _hits;
		documentCollection = new ArrayList<RealVector>();
	}
	//int id = results.scoreDocs[0].doc;
	public void prepareTermVectors() throws IOException{

		for(int i=0;i<hits.length;i++){
			Map<String, Double> termFrequencies = indexer.getWieghts(reader, hits[i].doc);
			System.out.println("numebr of terms"+indexer.getTerms().size());
		}
		

	}
	public ArrayList<RealVector> getAllVectors() throws IOException{
		for(int i=0;i<hits.length;i++){
			Map<String, Double> termFrequencies = indexer.getWieghts(reader, hits[i].doc);
			RealVector v = toRealVector(termFrequencies, indexer.getTerms());
			documentCollection.add(v);
		}
		return documentCollection;
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
	public void createWekaFileWithAttributes() throws IOException{
		File f = new File("weka\\weka-data.arff");
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write("@relation developer-expertise");
		output.newLine();
		for(String s: indexer.getTerms()){
			output.write("@attribute "+s+" "+"numeric");
			//output.write("@attribute "+s+" "+"{yes,no}");
			output.newLine();
		}
		output.newLine();
		output.newLine();
		output.write("@data");
		output.newLine();
		output.close();
	}
	public void addAttributeValues() throws IOException{
		File f = new File("weka\\weka-data.arff");
		BufferedWriter output = new BufferedWriter(new FileWriter(f, true));;
		for(int i=0;i<hits.length;i++){
			Map<String, Double> termFrequencies = indexer.getWieghts(reader, hits[i].doc);
			RealVector v = toRealVector(termFrequencies, indexer.getTerms());
			double[] x = v.toArray();
			for(int j=0;j<x.length;j++){
				if(j==x.length-1){
					output.append(Double.toString(x[j]));
//					if(x[j]==0.0){output.append("no");}
//					else{output.append("yes");}
				}else{
					output.append(Double.toString(x[j])+",");
//					if(x[j]==0.0){output.append("no,");}
//					else{output.append("yes,");}
				}
			}
			output.newLine();
		}
		output.close();
	}
}
