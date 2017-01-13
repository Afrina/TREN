package extend.ClusterDataSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import StringFormatter.StringFormatter;
import CommitParser.DeveloperCommitInfo;

public class CommitKeywordCollector {
	NodeList commits;
	HashMap<String, DeveloperInfo> developers = new HashMap<String, DeveloperInfo>();
	public CommitKeywordCollector(String path ) throws ParserConfigurationException, SAXException, IOException{
		String xmlFileLocation = path;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document doc = dbBuilder.parse(new File(xmlFileLocation));
		doc.getDocumentElement().normalize();
		commits = doc.getElementsByTagName("commit");
	}
	public HashMap<String, DeveloperInfo> getDevelopersAfterCommit(){
		return developers;
	}
	public void collectCommitContents(){
		System.out.println("collecting content");
		for(int i=0;i<commits.getLength();i++){
			//System.out.println(i);
			Element commitElement = (Element) commits.item(i);
			if(!developers.containsKey(commitElement.getElementsByTagName("author").item(0).getTextContent())){
				//System.out.println("ooonnn");
				Element changedFiles = (Element) commitElement.getElementsByTagName("changedFiles").item(0);
				String content = commitElement.getElementsByTagName("subject").item(0).getTextContent();
				if(changedFiles!= null){
					NodeList files = changedFiles.getElementsByTagName("filePath");
					for(int j=0;j<files.getLength();j++){
						StringFormatter formatter = new StringFormatter(files.item(j).getTextContent().toLowerCase());
						content+= " "+formatter.getFormattedString();
					}
				}
				DeveloperInfo dev = new DeveloperInfo(content, "");
				developers.put(commitElement.getElementsByTagName("author").item(0).getTextContent(),dev);
			}
			else{
				//System.out.println("oofff");
				DeveloperInfo dev = developers.get(commitElement.getElementsByTagName("author").item(0).getTextContent());
				Element changedFiles = (Element) commitElement.getElementsByTagName("changedFiles").item(0);
				dev.commitContent+= " "+commitElement.getElementsByTagName("subject").item(0).getTextContent();
				if(changedFiles!= null){
					NodeList files = changedFiles.getElementsByTagName("filePath");
						for(int j=0;j<files.getLength();j++){
							String[] onlyName = files.item(j).getTextContent().split(".");
							if(onlyName.length>0){StringFormatter formatter = new StringFormatter(onlyName[0].toLowerCase());
							dev.commitContent+= " "+formatter.getFormattedString();}
						}
				}
				developers.replace(commitElement.getElementsByTagName("author").item(0).getTextContent(), dev);		
			}	
		}
		System.out.println(developers.size());
	}
	public void printCommits(){
		Iterator it = developers.entrySet().iterator();
		int x=0;
		while (it.hasNext()&& x<5) {
			Map.Entry commEntry = (Map.Entry)it.next();
			System.out.println("**********"+" "+commEntry.getKey() + " = " );
			DeveloperInfo d = developers.get(commEntry.getKey());	
		x++;
				System.out.println(d.commitContent);
			
		}
	}
}
