package recommendation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import extend.ClusterDataSet.BugReport;
import pathGeneration.DeveloperPathCount;

public class Recommender {
	public HashMap<String, DeveloperPathCount> proximityResult;
	public HashMap<String,Integer> developerWorkLoad;
	public int teamSize;
	public String severity;
	public HashSet<String> checkList; int topn=1000; double precision, recall=0;
	public Recommender(BugReport b, HashMap<String, DeveloperPathCount> _proximityResult, 
			HashMap<String,Integer> _developerWorkLoad, int _teamSize){
		proximityResult = _proximityResult;
		developerWorkLoad = _developerWorkLoad;
		teamSize = _teamSize;
		severity = b.getSeverity();
		checkList = new HashSet<String>();
		checkList.add(b.getDeveloper());
		checkList.addAll(b.getCommenterList());
	}
	public int getTopN(){
		return topn;
	}
	public int getGroundTruth(){
		return checkList.size();
	}
	public double getPrecission(){
		return precision;
	}
	public double getRecall(){
		return recall;
	}
	public void recemmendation(BufferedWriter teamOutput) throws IOException{
		System.out.println("I am here.... "+severity);
		if(severity.equals("major") || severity.equals("blocker") || severity.equals("critical")){
			System.out.println("I am here  too.... ");
			int flag = teamSize; int topNCounter=1; 
			for(String key : proximityResult.keySet()){
				if(flag==0) break ; flag = flag-1;
				System.out.println(key+ " Recommendation  Score: "+proximityResult.get(key).totalScore);
				teamOutput.write(key+ " Recommendation  Score: "+proximityResult.get(key).totalScore);
				teamOutput.newLine();
				if(topNCounter<20){
					if(checkList.contains(key) && topn==1000){
						topn = topNCounter;
					}
				}
				topNCounter++;
			}
			int counter = 0; double commoners = 0;
			for(String key : proximityResult.keySet()){
				if(counter == 10) break;
				if(checkList.contains(key)){
					commoners = commoners + 1;
				}
				counter++;
			}
			precision = commoners / 10;
			recall = commoners / (double)checkList.size();
			
		}
		else{
			System.out.println("I am here.... "+severity);
			sortByLoad("ASceNDing");
			int flag = teamSize;
			for(String dev : developerWorkLoad.keySet()){
				if(flag == 0) break;
				flag = flag -1 ;
				System.out.println(dev+" "+developerWorkLoad.get(dev));
				teamOutput.write(dev+" "+developerWorkLoad.get(dev));
				teamOutput.newLine();
			}
		}
	}
	public void sortByLoad(String order){
		List<Entry<String, Integer>> list = new LinkedList<Entry<String,Integer>>(developerWorkLoad.entrySet());
		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>(){
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2){
				if (!order.equals("DESC"))
				{
					int result =  o1.getValue().compareTo(o2.getValue());
					if(result == 0){
						if(proximityResult.get(o1.getKey()) != null && proximityResult.get(o2.getKey())!=null){
							Double a = Double.valueOf(proximityResult.get(o1.getKey()).totalScore);
							Double b = Double.valueOf(proximityResult.get(o2.getKey()).totalScore);
							return a.compareTo(b);
						}
					}
					return result;
				}
				else
				{
					int result = o2.getValue().compareTo(o1.getValue());
					if(result == 0){
						if(proximityResult.get(o1.getKey()) != null && proximityResult.get(o2.getKey())!=null){
							Double a = Double.valueOf(proximityResult.get(o1.getKey()).totalScore);
							Double b = Double.valueOf(proximityResult.get(o2.getKey()).totalScore);
							return b.compareTo(a);
						}
					}
					return result;
				}
			}
		});
		developerWorkLoad = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list)
		{
			developerWorkLoad.put(entry.getKey(), entry.getValue());
		}		
	}
	
	public void Krecemmendation(BufferedWriter teamOutput) throws IOException{
		
			System.out.println("I am here  in K.... ");
			int flag = teamSize; int topNCounter=1; 
			for(String key : proximityResult.keySet()){
				if(flag==0) break ; flag = flag-1;
				System.out.println(key+ " k  Score: "+proximityResult.get(key).KScore);
				teamOutput.write(key+ " k  Score: "+proximityResult.get(key).KScore);
				teamOutput.newLine();
				if(topNCounter<20){
					if(checkList.contains(key) && topn==1000){
						topn = topNCounter;
					}
				}
				topNCounter++;
			}
			int counter = 0; double commoners = 0;
			for(String key : proximityResult.keySet()){
				if(counter == 10) break;
				if(checkList.contains(key)){
					commoners = commoners + 1;
				}
				counter++;
			}
			precision = commoners / 10;
			recall = commoners / (double)checkList.size();
			
		
	
	}
}
