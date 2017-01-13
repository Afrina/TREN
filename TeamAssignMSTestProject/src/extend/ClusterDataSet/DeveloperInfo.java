package extend.ClusterDataSet;

import java.util.ArrayList;
import java.util.HashMap;

public class DeveloperInfo {
	public HashMap<String,Double> commitKeywords;
	public HashMap<String,Double> reportKeywords;
	String commitContent;
	String reportContent;
	public String developerName;
	public int exp;
	public DeveloperInfo(String _commitContent,String _reportContent){
		commitKeywords = new HashMap<String, Double>();
		reportKeywords = new HashMap<String, Double>();
		commitContent = _commitContent;
		reportContent = _reportContent;
		}
	
}
