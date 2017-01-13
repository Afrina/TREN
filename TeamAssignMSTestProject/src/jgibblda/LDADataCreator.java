package jgibblda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

import extend.ClusterDataSet.LuceneConstants;

public class LDADataCreator {
	ArrayList<Map<String, Double>> docs;
	
	public LDADataCreator(ArrayList<Map<String, Double>> _docs){
		docs = _docs;
	}
	public void createDataFile(String path) throws IOException{
		File f = new File(path);
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(Integer.toString(docs.size()));
		output.newLine();
		for(int i=0;i<docs.size();i++){
			String content = "";
			for(String key : docs.get(i).keySet()){
				if(key.length()<=2) continue;
				 content+= key+" "; 
			}
			output.write(content);
			output.newLine();
		}
		output.close();
	}
}
