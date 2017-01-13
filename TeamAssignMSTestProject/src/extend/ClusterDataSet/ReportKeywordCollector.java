package extend.ClusterDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class ReportKeywordCollector {
	HashMap<String, DeveloperInfo> developers;
	ArrayList<Document> reports;
	List<BugReport> tempReports;
	
	public ReportKeywordCollector(HashMap<String, DeveloperInfo> _developers, ArrayList<Document> _reports, List<BugReport> _tempReports){
		developers = _developers;
		reports = _reports;
		tempReports = _tempReports;
	}
	public HashMap<String, DeveloperInfo> getDevelopersAfterReport(){
		return developers;
	}
	public void collectReportConetnt(){
		for(int i=0;i<reports.size();i++){
			DeveloperInfo d = developers.get(reports.get(i).getField(LuceneConstants.Developer).stringValue());
			if(d==null){
				d = new DeveloperInfo("", reports.get(i).getField(LuceneConstants.SUMMARY).stringValue()+" "+reports.get(i).getField(LuceneConstants.DESCRIPTION).stringValue());
				developers.put(reports.get(i).getField(LuceneConstants.Developer).stringValue(), d);
			}
			else{
				//System.out.println("got same here : "+reports.get(i).getField(LuceneConstants.Developer).stringValue());
				String commitContent = d.commitContent;
				String reportContent = d.reportContent;
				reportContent+= reports.get(i).getField(LuceneConstants.SUMMARY).stringValue()+" "+reports.get(i).getField(LuceneConstants.DESCRIPTION).stringValue();
				developers.replace(reports.get(i).getField(LuceneConstants.Developer).stringValue(), new DeveloperInfo(commitContent, reportContent));
			}
			
			Set<String> commenters  = getCommenters(reports.get(i).getField(LuceneConstants.ID).stringValue());
			for(String commenterName : commenters){
				DeveloperInfo d2 = developers.get(commenterName);
				if(d2==null){
					d2 = new DeveloperInfo("", reports.get(i).getField(LuceneConstants.SUMMARY).stringValue()+" "+reports.get(i).getField(LuceneConstants.DESCRIPTION).stringValue());
					developers.put(commenterName, d);
				}
				else{
					//System.out.println("got same here : "+reports.get(i).getField(LuceneConstants.Developer).stringValue());
					String commitContent = d.commitContent;
					String reportContent = d.reportContent;
					reportContent+= reports.get(i).getField(LuceneConstants.SUMMARY).stringValue()+" "+reports.get(i).getField(LuceneConstants.DESCRIPTION).stringValue();
					developers.replace(commenterName, new DeveloperInfo(commitContent, reportContent));
				}
			}
		}
	}
	public Set<String> getCommenters(String id){
		for(int i=0;i<tempReports.size();i++){
			if(tempReports.get(i).getId().equals(id)){
				return tempReports.get(i).getCommenterList();
			}
		}
		return null;
	}
}
