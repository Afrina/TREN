package network;

import java.util.ArrayList;
import java.util.HashSet;

import javax.sql.rowset.spi.XmlReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import CommitXMLCreatorAndMerger.XMLFileReader;

public class NetworkReader {
	
	public String commentPath;
	public String activityPath;
	XMLFileReader xmlReader;
	ArrayList<ArrayList<NetworkLink>> networkChunks;
	DeveloperNameDictionary nameDictionary;
	public NetworkReader(String _commentPath, String _activityPath){
		commentPath = _commentPath;
		activityPath = _activityPath;
		xmlReader = new XMLFileReader();
		networkChunks = new ArrayList<ArrayList<NetworkLink>>();
		nameDictionary = new DeveloperNameDictionary();
	}
	public ArrayList<ArrayList<NetworkLink>> getNetworkChunks(){
		return networkChunks;
	}
	public void collectNetworkElements(int tainSize){
		Element commentRoot = xmlReader.ReadXmlFile(commentPath);
		Element activityRoot = xmlReader.ReadXmlFile(activityPath);

		NodeList totalBugs = commentRoot.getElementsByTagName("bug");
		for(int i=0;i<tainSize;i++){
			nameDictionary.nameMapCreator((Element)commentRoot.getElementsByTagName("bug").item(i));
		}
		for(int i=0;i<tainSize;i++){
			nameDictionary.nameMapUpdateWithActivity((Element)activityRoot.getElementsByTagName("bug").item(i));
		}
//		System.out.println(nameDictionary.getNameMap().size());
//		for(String k : nameDictionary.getNameMap().keySet()){
//			System.out.println(k);
//			for(String s :nameDictionary.getNameMap().get(k) ){
//				System.out.println("   "+s);
//			}
//		}
		for(int i=0;i<tainSize;i++){
			NetworkCreator createHN = new NetworkCreator(nameDictionary.getNameMap());
			ArrayList<NetworkLink> HN = createHN.generateBugNetwork((Element)commentRoot.getElementsByTagName("bug").item(i), (Element)activityRoot.getElementsByTagName("bug").item(i));
			networkChunks.add(HN);
		}
	}
}
