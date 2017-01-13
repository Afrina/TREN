package extend.ClusterDataSet;

import java.util.ArrayList;
import java.util.Set;


public class BugReport {
	private String developer ;
	private String summary, severity;
	private String description;
	private String allKeywords;
	private String reportingTime;
	private String id;
	private Set<String> commenterList;

	public BugReport(String _id, String _developer, String _summary, String _description,String _reportingTime, Set<String> _commenterList, String _severity) {
		developer = _developer;
		summary = _summary;
		description = _description;
		allKeywords = _summary + _description;
		reportingTime = _reportingTime;
		id = _id;
		commenterList = _commenterList;
		severity = _severity;
	}
	public BugReport(String _id, String _developer){
		id = _id;
		developer = _developer;
	}
	public String getDeveloper() {
		return developer;
	}
	public String getSeverity() {
		return severity;
	}
	public Set<String> getCommenterList(){
		return commenterList;
	}

	public void setDeveloper(String _developer) {
		developer = _developer;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String _summary) {
		summary = _summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		description = _description;
	}
	public String getallKeywords() {
		return allKeywords;
	}

	public void setallKeywords(String _allKeywords) {
		allKeywords = _allKeywords;
	}
	public String getReportingTime() {
	 return reportingTime;
	}

	public void setReportingTime(String _reportingTime) {
		reportingTime = _reportingTime;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
