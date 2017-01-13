package recommendation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import extend.ClusterDataSet.BugReport;
import extend.ClusterDataSet.DeveloperInfo;

public class WorkLoadHandler {

	HashMap<String, Integer> developerWorkLoads ;
	public WorkLoadHandler(){
		developerWorkLoads = new HashMap<String, Integer>();
	}
	public void calculateWorkLoad(List<BugReport> reports){
		System.out.println(reports.size());
		for(int i =0;i<reports.size();i++){
			Set<String> allDev = reports.get(i).getCommenterList();
			System.out.println(reports.get(i).getCommenterList());
			allDev.add(reports.get(i).getDeveloper());
			for(String name : allDev){
				Integer load = developerWorkLoads.get(name);
				if(load == null){
					developerWorkLoads.put(name, 1);
				}
				else{
					developerWorkLoads.replace(name, load+1);
				}
			}
		}
	}
	public HashMap<String, Integer> getDeveloperWorkLoads(){
		return developerWorkLoads;
	}
	public void createJsonForWorkLoads(){
		JSONArray allList = new JSONArray();
		for(String key : developerWorkLoads.keySet()){
			JSONObject dev = new JSONObject();
			dev.put("name", key);
			dev.put("load", developerWorkLoads.get(key));
			
			allList.add(dev);
		}
		
		

		try {

			FileWriter file = new FileWriter("Data\\workLoadjson.json");
			file.write(allList.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void print() {
		for(String dev : developerWorkLoads.keySet()){
			System.out.println(dev+" "+developerWorkLoads.get(dev));
		}
		
	}
	public void totalNewIdentifier() throws FileNotFoundException, IOException, ParseException{
		//ArrayLi
		JSONParser parser = new JSONParser();

		JSONArray allList = (JSONArray) parser.parse(new FileReader("Data\\group.json"));
		JSONObject o;ArrayList<String> names = new ArrayList<String>();
		for (int i=0; i < allList.size(); i++) {
		    o = (JSONObject) allList.get(i);
		    	JSONArray devs = (JSONArray) o.get("developers");
		    	for(int j = 0;j<devs.size();j++){
		    		
		    	}
	}
}
}