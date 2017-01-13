package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression;
import org.omg.stub.java.rmi._Remote_Stub;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class NetworkCreator {

	public ArrayList<NetworkLink> HN;
	public DeveloperNameDictionary nameDictionary;
	public HashMap<String, Set<String>> tempNames; 
	public HashMap<String, Set<String>> thisBugNames;
	public NetworkCreator(HashMap<String, Set<String>> _nameMap){
		HN = new ArrayList<NetworkLink>();
		nameDictionary = new DeveloperNameDictionary();
		tempNames = new HashMap<String, Set<String>>();
	}
	public void addToTempName(HashMap<String, Set<String>> tempNames, String name, String userName){
			Set<String> userNames = tempNames.get(userName);
			if(userNames == null){
				userNames = new HashSet<String>();
				userNames.add(name);
				tempNames.put(userName, userNames);
			}
			else{
				userNames.add(name);
				tempNames.replace(userName, userNames);
			}
		
	}
	public ArrayList<NetworkLink> generateBugNetwork(Element bugReport, Element bugActivity){
		thisBugNames = new HashMap<String, Set<String>>();
		Bug bug = new Bug(bugReport.getElementsByTagName("id").item(0).getTextContent());
		//System.out.println("bug id is "+bug.bugID);
		Developer reporter = new Developer(bugReport.getElementsByTagName("developer").item(0).getTextContent());
		String creationTime = bugReport.getElementsByTagName("creation_time").item(0).getTextContent();

		addToTempName(tempNames, bugReport.getElementsByTagName("developer").item(0).getTextContent(), bugReport.getElementsByTagName("developer_username").item(0).getTextContent());
		addToTempName(thisBugNames, bugReport.getElementsByTagName("developer").item(0).getTextContent(), bugReport.getElementsByTagName("developer_username").item(0).getTextContent());
		
		NetworkNode bugNetworkNode = new NetworkNode("B", bug);
		NetworkNode reporterNetworkNode = new NetworkNode("D", reporter);
		
		
		/*
		 *  add newtwork nodes inside the network Array
		 */
		NetworkLink BD = new NetworkLink(bugNetworkNode, reporterNetworkNode, 1,creationTime);
		NetworkLink DB = new NetworkLink(reporterNetworkNode, bugNetworkNode, 2,creationTime);

		HN.add(BD);
		HN.add(DB);
		
		NodeList comments = bugReport.getElementsByTagName("comment");
		for(int c=0;c<comments.getLength();c++){
			Element commentElement = (Element) comments.item(c);
			if(!containNode(commentElement.getElementsByTagName("who").item(0).getTextContent(), 5)){
				Comment comment = new Comment(commentElement.getElementsByTagName("comment_id").item(0).getTextContent()
						, commentElement.getElementsByTagName("comment_count").item(0).getTextContent(),
						commentElement.getElementsByTagName("who").item(0).getTextContent(),
						commentElement.getElementsByTagName("when").item(0).getTextContent()
						);
				String commentTime = commentElement.getElementsByTagName("when").item(0).getTextContent();
				addToTempName(tempNames, commentElement.getElementsByTagName("who").item(0).getTextContent(), commentElement.getElementsByTagName("commenter_username").item(0).getTextContent());
				addToTempName(thisBugNames, commentElement.getElementsByTagName("who").item(0).getTextContent(), commentElement.getElementsByTagName("commenter_username").item(0).getTextContent());
				
				//System.out.println("***** "+comment.commentID+comment.commentDeveloper+bug.bugID);
				NetworkNode commentNetworkNode = new NetworkNode("T", comment);
				NetworkLink BT = new NetworkLink(bugNetworkNode, commentNetworkNode, 3,commentTime);
				NetworkLink TB = new NetworkLink(commentNetworkNode, bugNetworkNode, 4,commentTime);
				HN.add(BT);
				HN.add(TB);

				Developer commentDeveloper = new Developer(comment.commentDeveloper);
				NetworkNode developerNetworkNode = new NetworkNode("D", commentDeveloper);
				NetworkLink DT = new NetworkLink(developerNetworkNode, commentNetworkNode, 5,commentTime);
				NetworkLink TD = new NetworkLink(commentNetworkNode, developerNetworkNode, 6,commentTime);
				HN.add(DT);
				HN.add(TD);
			}

		}
		/*
		 * Creating activity
		 */
		NodeList activities = bugActivity.getElementsByTagName("element");
		for(int a=1; a<activities.getLength(); a++){
			Element activity = (Element) activities.item(a);
			if(!containNode(nameDictionary.getMappedName(activity.getElementsByTagName("who").item(0).getTextContent(),tempNames,thisBugNames), 2)){
				Developer acter = new Developer(nameDictionary.getMappedName(activity.getElementsByTagName("who").item(0).getTextContent(),tempNames,thisBugNames));
				String activityTime = activity.getElementsByTagName("when").item(0).getTextContent();
				
				NetworkNode acterNetworkNode = new NetworkNode("D", acter);
				//System.out.println("===== "+acter.name+" "+bug.bugID);
				BD = new NetworkLink(bugNetworkNode, acterNetworkNode, 1,activityTime);
				DB = new NetworkLink(acterNetworkNode, bugNetworkNode, 2,activityTime);

				HN.add(BD);
				HN.add(DB);
			}

		}

		/*
		 * Creating comment
		 */
		
		Component bugComponent = new Component(bugReport.getElementsByTagName("component").item(0).getTextContent());
		NetworkNode componentNetworkNode = new NetworkNode("C", bugComponent);

		NetworkLink BC = new NetworkLink(bugNetworkNode, componentNetworkNode, 7, null);
		NetworkLink CB = new NetworkLink(componentNetworkNode, bugNetworkNode, 8, null);

		HN.add(BC);
		HN.add(CB);

		/*
		 * Creating Bug Product relation
		 */

		Product bugProduct = new Product(bugReport.getElementsByTagName("product").item(0).getTextContent());
		NetworkNode productNetworkNode = new NetworkNode("P", bugProduct);

		NetworkLink CP = new NetworkLink(componentNetworkNode, productNetworkNode, 9, null);
		NetworkLink PC = new NetworkLink(productNetworkNode, componentNetworkNode, 10, null);

		HN.add(CP);
		HN.add(PC);

		return HN;

	}

	
	public boolean containNode(String name, int x){
		Boolean flag = false;
		for(NetworkLink link : HN){
			if(link.relationType==x){
				Developer d = (Developer) link.firstNode.customNodes;
				if(d.name.equals(name)){flag=true; return flag;}
			}
		}
		return false;

	}
}
