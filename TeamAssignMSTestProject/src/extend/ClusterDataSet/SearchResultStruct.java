package extend.ClusterDataSet;

import org.apache.lucene.document.Document;

public class SearchResultStruct {
	Document doc;
	int id;
	
	public SearchResultStruct(Document _doc, int _id){
		doc = _doc;
		id = _id;
	}
}
