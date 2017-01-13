package CommitXMLCreatorAndMerger;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import extend.ClusterDataSet.BugReport;
import extend.ClusterDataSet.Stemmer;


public class XMLFileReader {
	String path;
	Document dom;
	Stemmer stemmer = new Stemmer();
	

	
	public Document getFormattedXml(){
		return dom;
	}


	public Element ReadXmlFile(String path){
		try {
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			Element root = doc.getDocumentElement();
			return root;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void formatXmlFile(Document doc) throws ParserConfigurationException{
	
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.newDocument();
		Element rootEle = dom.createElement("bugs");
		dom.appendChild(rootEle);
		
		NodeList bugs = doc.getElementsByTagName("bug");
		for(int i =0;i<bugs.getLength();i++){
			//System.out.println(((Element) bugs.item(i)).getElementsByTagName("bug_id").item(0).getTextContent());
			Element bug = dom.createElement("bug");
			Element id = dom.createElement("id");
			Element dup_id = dom.createElement("dup_id");
			Element short_desc = dom.createElement("short_desc");
			Element thetext = dom.createElement("thetext");
			
			//System.out.println(((Element) bugs.item(i)).getElementsByTagName("bug_id").item(0).getNodeValue());
			id.appendChild(dom.createTextNode( ((Element) bugs.item(i)).getElementsByTagName("bug_id").item(0).getTextContent()));
			dup_id.appendChild(dom.createTextNode( ((Element) bugs.item(i)).getElementsByTagName("dup_id").item(0).getTextContent()));
			short_desc.appendChild(dom.createTextNode( ((Element) bugs.item(i)).getElementsByTagName("short_desc").item(0).getTextContent()));
			thetext.appendChild(dom.createTextNode( ((Element) bugs.item(i)).getElementsByTagName("thetext").item(0).getTextContent()));
			
			bug.appendChild(id);
			bug.appendChild(dup_id);
			bug.appendChild(short_desc);
			bug.appendChild(thetext);
			
			
			rootEle.appendChild(bug);
		}	
	}
	
	public void getDuplicates(Document doc){
		try{
            
            String path="Data\\LargeDataDuplicateBugs.txt";
            File file = new File(path);
               if (!file.exists()) {
                   file.createNewFile();
               }
               FileWriter fw = new FileWriter(file.getAbsoluteFile());
               BufferedWriter bw = new BufferedWriter(fw);
               NodeList bugs = doc.getElementsByTagName("bug");
               for(int i=0;i<bugs.getLength();i++){
            	   bw.write(((Element)bugs.item(i)).getElementsByTagName("dup_id").item(0).getTextContent()+",");
               }        
               bw.close();
         }catch(Exception e){
             System.out.println(e);
         }
	}
	public List<BugReport> getBugReports(Element doc ){

		List<BugReport> reports = new ArrayList<>();

		try {
		
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("bug");
			//System.out.println("size id" +nList.getLength());

			for (int temp = 0; temp < 2500; temp++) {
//				System.out.println("\n----------------------------");

				Node nNode = nList.item(temp);

				BugReport report ;

//				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					//System.out.println("is here");
					Element eElement = (Element) nNode;
					//Element desc = (Element) eElement.getElementsByTagName("long_desc").item(0);

					String name = ((Element)eElement.getElementsByTagName("developer").item(0)).getAttribute("name");
					if (name.trim().equals(""))
						name = eElement.getElementsByTagName("developer").item(0).getTextContent();
					String summary = eElement.getElementsByTagName("short_desc").item(0).getTextContent() ;
					String description = eElement.getElementsByTagName("thetext").item(0).getTextContent();
					String id = eElement.getElementsByTagName("id").item(0).getTextContent();
					String reporting_time = eElement.getElementsByTagName("creation_time").item(0).getTextContent();
					NodeList severityList = eElement.getElementsByTagName("bug_severity");
					String severity;
					if(severityList.getLength()>0) severity = severityList.item(0).getTextContent();
//					else severity = null;
					else severity = "major";
					NodeList commenters = eElement.getElementsByTagName("comment");
					Set<String> commenterList = new HashSet<String>();
					for(int i=0;i<commenters.getLength();i++){
						String commenterName = ((Element) commenters.item(i)).getElementsByTagName("who").item(0).getTextContent();
						commenterList.add(commenterName);
						//System.out.println(commenterName);
					}
				
//					System.out.println("Reporter name : " + name);
//					System.out.println("Summary : " + summary);
//					System.out.println("Description :\n" + description);

					summary = stemmer.englishStemer(summary).trim();
					description = stemmer.englishStemer(description).trim();
					report = new BugReport(id, name, summary, description, reporting_time,commenterList,severity);

				reports.add(report);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("six s"+reports.size());
		return  reports;
	}
	public List<BugReport> getLoadBugReports(Element doc ){

		List<BugReport> reports = new ArrayList<>();

		try {
		
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("bug");
			//System.out.println("size id" +nList.getLength());

			for (int temp = 0; temp < nList.getLength(); temp++) {
//				System.out.println("\n----------------------------");

				Node nNode = nList.item(temp);

				BugReport report ;

//				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					//System.out.println("is here");
					Element eElement = (Element) nNode;
					//Element desc = (Element) eElement.getElementsByTagName("long_desc").item(0);

					String name = ((Element)eElement.getElementsByTagName("developer").item(0)).getAttribute("name");
					if (name.trim().equals(""))
						name = eElement.getElementsByTagName("developer").item(0).getTextContent();
					String summary = eElement.getElementsByTagName("short_desc").item(0).getTextContent() ;
					String description = eElement.getElementsByTagName("thetext").item(0).getTextContent();
					String id = eElement.getElementsByTagName("id").item(0).getTextContent();
					String reporting_time = eElement.getElementsByTagName("creation_time").item(0).getTextContent();
					NodeList severityList = eElement.getElementsByTagName("bug_severity");
					String severity;
					if(severityList.getLength()>0) severity = severityList.item(0).getTextContent();
//					else severity = null;
					else severity = "major";
					NodeList commenters = eElement.getElementsByTagName("comment");
					Set<String> commenterList = new HashSet<String>();
					for(int i=0;i<commenters.getLength();i++){
						String commenterName = ((Element) commenters.item(i)).getElementsByTagName("who").item(0).getTextContent();
						commenterList.add(commenterName);
						//System.out.println(commenterName);
					}
				
//					System.out.println("Reporter name : " + name);
//					System.out.println("Summary : " + summary);
//					System.out.println("Description :\n" + description);

					summary = stemmer.englishStemer(summary).trim();
					description = stemmer.englishStemer(description).trim();
					report = new BugReport(id, name, summary, description, reporting_time,commenterList,severity);

				reports.add(report);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("six s"+reports.size());
		return  reports;
	}
	
	
}
