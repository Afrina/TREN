package CommitParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CommitReader {

	String xmlFileLocation;
	Document doc;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dbBuilder;
	Element root;
	public HashMap<String, ArrayList<DeveloperCommitInfo>> classCommits;
	public ArrayList<String> totalDeveloperList = new ArrayList<String>();

	public CommitReader(String _xmlFileLocation) throws ParserConfigurationException, SAXException, IOException{
		xmlFileLocation = _xmlFileLocation;
		dbFactory = DocumentBuilderFactory.newInstance();
		dbBuilder = dbFactory.newDocumentBuilder();
		doc = dbBuilder.parse(new File(xmlFileLocation));
		doc.getDocumentElement().normalize();
		root = doc.getDocumentElement();
		classCommits = new HashMap<String, ArrayList<DeveloperCommitInfo>>();
	}

	public HashMap<String, ArrayList<DeveloperCommitInfo>> getClassCommitInfos(){
		return classCommits;
	}
	public int getTotalDeveloperNumber(){
		return totalDeveloperList.size();
	}

	public void readCommits(){
		NodeList commitList = root.getElementsByTagName("commit");
		for(int i=0;i<commitList.getLength();i++){
			//System.out.println(i);
			Element commitElement = (Element) commitList.item(i);
			if(!totalDeveloperList.contains(commitElement.getElementsByTagName("author").item(0).getTextContent())){
				totalDeveloperList.add(commitElement.getElementsByTagName("author").item(0).getTextContent());
			}
			Element changedFiles = (Element) commitElement.getElementsByTagName("changedFiles").item(0);
			if(changedFiles!= null){
				NodeList files = changedFiles.getElementsByTagName("filePath");
				for(int j=0;j<files.getLength();j++){
					ArrayList<DeveloperCommitInfo> developerList = classCommits.get(files.item(j).getTextContent().toLowerCase());
					if(developerList == null){

						ArrayList<DeveloperCommitInfo> newDeveloperList = new ArrayList<DeveloperCommitInfo>();
						DeveloperCommitInfo newCommit = new DeveloperCommitInfo();
						newCommit.commitId = commitElement.getElementsByTagName("hash").item(0).getTextContent();
						newCommit.commitTime = commitElement.getElementsByTagName("date").item(0).getTextContent();
						newCommit.developerName = commitElement.getElementsByTagName("author").item(0).getTextContent();
						newCommit.subject = commitElement.getElementsByTagName("subject").item(0).getTextContent();
						newDeveloperList.add(newCommit);

						classCommits.put(files.item(j).getTextContent().toLowerCase(), newDeveloperList);
					}
					else{
						DeveloperCommitInfo newCommit = new DeveloperCommitInfo();
						newCommit.commitId = commitElement.getElementsByTagName("hash").item(0).getTextContent();
						newCommit.commitTime = commitElement.getElementsByTagName("date").item(0).getTextContent();
						newCommit.developerName = commitElement.getElementsByTagName("author").item(0).getTextContent();
						newCommit.subject = commitElement.getElementsByTagName("subject").item(0).getTextContent();

						developerList.add(newCommit);
					}
				}			
			}
		}
		sortCommits();
		System.out.println("commits "+commitList.getLength());
	}
	public void sortCommits(){
		Iterator it = classCommits.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry commEntry = (Map.Entry)it.next();
			ArrayList<DeveloperCommitInfo> developerlist = classCommits.get(commEntry.getKey());
			Collections.sort(developerlist, new DeveloperListSorting());
			
//			ArrayList<String> nameList = new ArrayList<String>();
//			ArrayList<DeveloperCommitInfo> tempList = new ArrayList<DeveloperCommitInfo>();
//			for (int i = 0; i < developerlist.size(); i++) {
//				if(!nameList.contains(developerlist.get(i).developerName)){
//					nameList.add(developerlist.get(i).developerName);
//					tempList.add(developerlist.get(i));
//				}
//	        }
//			reducedClassCommits.replace((String) commEntry.getKey(), tempList);
		}
	}
	
	public void printCommits(){
		Iterator it = classCommits.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry commEntry = (Map.Entry)it.next();
			System.out.println("**********"+" "+commEntry.getKey() + " = " );
			ArrayList<DeveloperCommitInfo> developerlist = classCommits.get(commEntry.getKey());	
			for (int i = 0; i < developerlist.size(); i++) {
				System.out.println(developerlist.get(i).developerName + " + " + developerlist.get(i).commitTime);
			}
		}
	}
	
	
}
