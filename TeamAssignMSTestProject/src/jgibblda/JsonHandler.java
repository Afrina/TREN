package jgibblda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import network.Developer;
import network.NetworkLink;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pathGeneration.DeveloperMetaPath;
import sun.nio.ch.Net;
import extend.ClusterDataSet.BugReport;
import extend.ClusterDataSet.DeveloperInfo;

public class JsonHandler {
	public JsonHandler(){
		
	}
	HashMap<String, ArrayList<DeveloperInfo>> groupedDevelopers;
	public JsonHandler(HashMap<String, ArrayList<DeveloperInfo>> _groupedDevelopers){
		groupedDevelopers = _groupedDevelopers;
	}
	public void createJsonForOnlyGroups(){
		JSONArray allList = new JSONArray();
		for(String key : groupedDevelopers.keySet()){
			JSONObject topic = new JSONObject();
			topic.put("topic", key);
			topic.put("size", groupedDevelopers.get(key).size());
			JSONArray devs  = new JSONArray();
			if(groupedDevelopers.get(key).size()<50){
				for(DeveloperInfo d : groupedDevelopers.get(key)){
					devs.add(d.developerName);
				}
			}
			else{
				for(int i = 0;i<50;i++){
					devs.add(groupedDevelopers.get(key).get(i).developerName);
				}
			}
			topic.put("developers", devs);
			allList.add(topic);
		}
		
		

		try {

			//FileWriter file = new FileWriter("Data\\group.json");
			FileWriter file = new FileWriter("Data\\loadgroup.json");
			file.write(allList.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<String> readJsonForOnlyGroups(String index) throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();

		JSONArray allList = (JSONArray) parser.parse(new FileReader("Data\\group.json"));
		JSONObject o;ArrayList<String> names = new ArrayList<String>();
		for (int i=0; i < allList.size(); i++) {
		    o = (JSONObject) allList.get(i);
		    if(o.get("topic").equals(index)){
		    	JSONArray devs = (JSONArray) o.get("developers");
		    	if(devs.size()<50){
		    		for(int j = 0;j<devs.size();j++){
		    			names.add((String) devs.get(j));
		    		}
		    	}
		    	else{
		    		for(int j = 0;j<50;j++){
		    			names.add((String) devs.get(j));
		    		}
		    	}
		    	
		    		
		    }
		}
		return names;
	}
	public void writeForMetaPaths(HashMap<String, DeveloperMetaPath> developerMetaPaths,String fileName,
			BufferedWriter o) throws IOException{

		int x=0;
		o.write("[");o.newLine();
		for(String key : developerMetaPaths.keySet()){
			if(x==0) {o.write("{");o.newLine();} 
			else {o.write(",");o.newLine();o.write("{");o.newLine();} x++;
			o.write("\"name\":\""+key+"\",");
			o.newLine();
	
			ArrayList<ArrayList<NetworkLink>> sameBugPathList = developerMetaPaths.get(key).sameBugMetaPaths;
			o.write("\"sameBugs\":[");o.newLine(); int y=0;
			for(ArrayList<NetworkLink> path : sameBugPathList){
			
				Developer d1 = (Developer) path.get(0).firstNode.customNodes;
				Developer d2 = (Developer) path.get(path.size()-1).secondNode.customNodes;
				if(y==0){
					o.write("["+
				//"\""+d1.name+"\""+","+
				"\""+path.get(0).time+"\""+","+
				"\""+d2.name+"\""+","+
				"\""+path.get(path.size()-1).time+"\""+"]");
					o.newLine();
				}
				
				else{
					o.write(",");o.newLine();
					o.write("["+
							//"\""+d1.name+"\""+","+
							"\""+path.get(0).time+"\""+","+
							"\""+d2.name+"\""+","+
							"\""+path.get(path.size()-1).time+"\""+"]");
								o.newLine();
				}
				y++;
	
			}
			o.write("],");o.newLine();
			o.write("\"sameComps\":[");o.newLine();

			ArrayList<ArrayList<NetworkLink>> sameComponentPathList = developerMetaPaths.get(key).sameComponentMetaPaths;
			int z=0;
			for(ArrayList<NetworkLink> path : sameComponentPathList){

				Developer d1 = (Developer) path.get(0).firstNode.customNodes;
				Developer d2 = (Developer) path.get(path.size()-1).secondNode.customNodes;
				if(z==0){
					o.write("["+
						//	"\""+d1.name+"\""+","+
							"\""+path.get(0).time+"\""+","+
							"\""+d2.name+"\""+","+
							"\""+path.get(path.size()-1).time+"\""+"]");
								o.newLine();
				}
				
				else{
					o.write(",");o.newLine();
					o.write("["+
						//	"\""+d1.name+"\""+","+
							"\""+path.get(0).time+"\""+","+
							"\""+d2.name+"\""+","+
							"\""+path.get(path.size()-1).time+"\""+"]");
								o.newLine();
				}
				z++;
			}
			o.write("]");o.newLine();
			o.write("}");
		
		}
		o.write("]");o.newLine();
		o.close();
		
	}
	public void readJsonForMetaPaths(String index) throws FileNotFoundException, IOException, ParseException{
		System.out.println("an fere");
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader("Data\\group_"+index+".json"));
			String s;
			while ((s=fileReader.readLine())!=null) {
				sb.append(s);
				sb.append('\n'); //if you want the newline
			}
			System.out.println("an fere");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return names;
	}
	
	public void writeForMetaPathsTest(HashMap<String, DeveloperMetaPath> developerMetaPaths,String fileName,
			BufferedWriter o) throws IOException{

		int x=0; int flag = 0;
		o.write("[");o.newLine();
		for(String key : developerMetaPaths.keySet()){
			if(flag == 2) break; flag++;
			if(x==0) {o.write("{");o.newLine();} 
			else {o.write(",");o.newLine();o.write("{");o.newLine();} x++;
			o.write("\"name\":\""+key+"\",");
			o.newLine();
	
			ArrayList<ArrayList<NetworkLink>> sameBugPathList = developerMetaPaths.get(key).sameBugMetaPaths;
			o.write("\"sameBugs\":[");o.newLine(); int y=0;
			for(ArrayList<NetworkLink> path : sameBugPathList){
			
				Developer d1 = (Developer) path.get(0).firstNode.customNodes;
				Developer d2 = (Developer) path.get(path.size()-1).secondNode.customNodes;
				if(y==0){
					o.write("["+
			//	"\""+d1.name+"\""+","+
				"\""+path.get(0).time+"\""+","+
				"\""+d2.name+"\""+","+
				"\""+path.get(path.size()-1).time+"\""+"]");
					o.newLine();
				}
				
				else{
					o.write(",");o.newLine();
					o.write("["+
				//			"\""+d1.name+"\""+","+
							"\""+path.get(0).time+"\""+","+
							"\""+d2.name+"\""+","+
							"\""+path.get(path.size()-1).time+"\""+"]");
								o.newLine();
				}
				y++;
	
			}
			o.write("],");o.newLine();
			o.write("\"sameComps\":[");o.newLine();

			ArrayList<ArrayList<NetworkLink>> sameComponentPathList = developerMetaPaths.get(key).sameComponentMetaPaths;
			int z=0;
			for(ArrayList<NetworkLink> path : sameComponentPathList){

				Developer d1 = (Developer) path.get(0).firstNode.customNodes;
				Developer d2 = (Developer) path.get(path.size()-1).secondNode.customNodes;
				if(z==0){
					o.write("["+
						//	"\""+d1.name+"\""+","+
							"\""+path.get(0).time+"\""+","+
							"\""+d2.name+"\""+","+
							"\""+path.get(path.size()-1).time+"\""+"]");
								o.newLine();
				}
				
				else{
					o.write(",");o.newLine();
					o.write("["+
						//	"\""+d1.name+"\""+","+
							"\""+path.get(0).time+"\""+","+
							"\""+d2.name+"\""+","+
							"\""+path.get(path.size()-1).time+"\""+"]");
								o.newLine();
				}
				z++;
			}
			o.write("]");o.newLine();
			o.write("}");
		
		}
		o.write("]");o.newLine();
		o.close();
		
	}
public BugReport getReportById(ArrayList<BugReport> testData, String id){
		for(BugReport b : testData){
			if(b.getId().equals(id)){
				return b;
			}
		}
		return null;
	}
}
