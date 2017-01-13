package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DeveloperNameDictionary {
	public static HashMap<String, Set<String>> nameMap = new HashMap<String, Set<String>>();
	
	public HashMap<String, Set<String>> getNameMap(){
		return nameMap;
	}
	public String getMappedName(String key, HashMap<String, Set<String>> tempNames, HashMap<String, Set<String>> thisBugNames){
		Set<String> names = nameMap.get(key);
		if(names == null || names.size()==0){
			return key;
		}
		else{
			if(names.size()>1){
				Set<String> thisBugList = thisBugNames.get(key);
				if(thisBugList != null){
					return thisBugList.iterator().next();
				}
				else{
					Set<String> tempList = tempNames.get(key);
					if(tempList != null){
						return tempList.iterator().next();
					}
				}
				return names.iterator().next();
			}
			else{
				return names.iterator().next();
			}
		}
	}
	public void mapDeveloperUsername(String name, String userName ){
		Set<String> userNames = nameMap.get(userName);
		if(userNames == null){
			userNames = new HashSet<String>();
			userNames.add(name);
			nameMap.put(userName, userNames);
		}
		else{
			userNames.add(name);
			nameMap.replace(userName, userNames);
		}
	}
	public void nameMapUpdateWithActivity(Element bugActivity){
		NodeList activities = bugActivity.getElementsByTagName("element");
		for(int a=1; a<activities.getLength(); a++){
			Element activity = (Element) activities.item(a);
			if(!nameMap.containsKey(activity.getElementsByTagName("who").item(0).getTextContent())){
				nameMap.put(activity.getElementsByTagName("who").item(0).getTextContent(), new HashSet<String>());
			}		
		}
	}
	public void nameMapCreator(Element bugReport){

		mapDeveloperUsername(bugReport.getElementsByTagName("developer").item(0).getTextContent(), 
				bugReport.getElementsByTagName("developer_username").item(0).getTextContent());

		NodeList comments = bugReport.getElementsByTagName("comment");
		for(int c=0;c<comments.getLength();c++){
			Element commentElement = (Element) comments.item(c);
			mapDeveloperUsername(commentElement.getElementsByTagName("who").item(0).getTextContent(),
					commentElement.getElementsByTagName("commenter_username").item(0).getTextContent());
		}

	}
}

