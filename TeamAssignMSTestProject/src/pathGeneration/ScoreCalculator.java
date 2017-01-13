package pathGeneration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import network.Developer;
import network.NetworkLink;

public class ScoreCalculator {
	HashMap<String, DeveloperMetaPath> developerMetaPaths;
	HashMap<String, DeveloperPathCount> proximityResult;
	String bugDate;

	public ScoreCalculator( String _bugDate){
		//developerMetaPaths = _developerMetaPaths;
		proximityResult = new HashMap<String, DeveloperPathCount>();
		bugDate = _bugDate;
	
	}
	public HashMap<String, DeveloperPathCount> getProximityResult(){
		return proximityResult;
	}
	public void initializeCounters(HashMap<String, DeveloperMetaPath> developerMetaPaths){
		for(String dev: developerMetaPaths.keySet()){
			//System.out.println(dev + " "+developerMetaPaths.get(dev).sameBugMetaPaths.size()+" "+developerMetaPaths.get(dev).sameComponentMetaPaths.size());
			countPaths(developerMetaPaths.get(dev).sameBugMetaPaths, "B");
			countPaths(developerMetaPaths.get(dev).sameComponentMetaPaths, "C");
		}
	}

	public void countPaths(ArrayList<ArrayList<NetworkLink>> samePaths, String indicator){
		for(ArrayList<NetworkLink> path: samePaths){
			Developer d1 = (Developer) path.get(0).firstNode.customNodes;
			Developer d2 = (Developer) path.get(path.size()-1).secondNode.customNodes;

			DeveloperPathCount sourceDevCounts = proximityResult.get(d1.name);
			mapUpdate(sourceDevCounts, indicator, d1.name, path.get(0).time);

			DeveloperPathCount destinationDevCounts = proximityResult.get(d2.name);
			mapUpdate(destinationDevCounts, indicator, d2.name, path.get(path.size()-1).time);

		}
	}

	public void mapUpdate(DeveloperPathCount pathCount, String indicator, String key, String usageTime){
		double constant = 1;
		double diff = getDateDifference(usageTime, bugDate);
		if(diff==0) diff=1;
		if(pathCount == null){
			double dateDiff = constant/diff;
			if(indicator == "B") 
				{pathCount = new DeveloperPathCount(dateDiff,0,0); pathCount.KScore = 1.0;}
			else if(indicator == "C") {pathCount = new DeveloperPathCount(0,dateDiff,0); pathCount.KScore = 1.0;}
			proximityResult.put(key, pathCount);
		}
		else{
			if(indicator == "B") {pathCount.sameBug = pathCount.sameBug+(constant/diff); 
			pathCount.KScore = pathCount.KScore+1;}
			else if(indicator == "C") {pathCount.sameComponent = pathCount.sameComponent + (constant/diff);
			pathCount.KScore = pathCount.KScore+1;
			}
			DeveloperPathCount p = new DeveloperPathCount(pathCount.sameBug, pathCount.sameComponent, pathCount.sameProduct);
			p.KScore = pathCount.KScore;
			proximityResult.replace(key, p);
		}
	}
	public void printScore(){
		System.out.println("Call is here xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+proximityResult.size());
		for(String key : proximityResult.keySet()){
			System.out.println("Score "+key);
			DeveloperPathCount p = proximityResult.get(key);
			System.out.println(p.sameBug+" "+p.sameComponent+" "+p.sameProduct+" "+p.totalScore);
		}
	}
	public void sortDevelopers(){
		for(String dev : proximityResult.keySet()){
			DeveloperPathCount temp = proximityResult.get(dev);
			temp.totalScore = temp.sameBug + temp.sameComponent + temp.sameProduct;
			proximityResult.replace(dev, temp);
		}
		sortByComparator("DESC");
	}
	public void sortByComparator(String order){
		List<Entry<String, DeveloperPathCount>> list = new LinkedList<Entry<String, DeveloperPathCount>>(proximityResult.entrySet());
		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, DeveloperPathCount>>(){
			public int compare(Entry<String, DeveloperPathCount> o1, Entry<String, DeveloperPathCount> o2){
				if (!order.equals("DESC"))
				{
					return Double.valueOf(o1.getValue().totalScore).compareTo(Double.valueOf(o2.getValue().totalScore));
				}
				else
				{
					return Double.valueOf(o2.getValue().totalScore).compareTo(Double.valueOf(o1.getValue().totalScore));
				}
			}
		});
		proximityResult = new LinkedHashMap<String, DeveloperPathCount>();
		for (Entry<String, DeveloperPathCount> entry : list)
		{
			proximityResult.put(entry.getKey(), entry.getValue());
		}		
	}
	public long getDateDifference(String useageDate, String bugDate){
		//SimpleDateFormat termFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		SimpleDateFormat bugFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		try {
			Date date1 = bugFormat.parse(useageDate);
			Date date2 = bugFormat.parse(bugDate);
			long diff = Math.abs(date2.getTime() - date1.getTime());
			//System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
			return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}	
	}
	public HashMap<String, DeveloperPathCount> sortForKScore(String order, HashMap<String, DeveloperPathCount> proximityResult){
		List<Entry<String, DeveloperPathCount>> list = new LinkedList<Entry<String, DeveloperPathCount>>(proximityResult.entrySet());
		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, DeveloperPathCount>>(){
			public int compare(Entry<String, DeveloperPathCount> o1, Entry<String, DeveloperPathCount> o2){
				if (!order.equals("DESC"))
				{
					return Double.valueOf(o1.getValue().KScore).compareTo(Double.valueOf(o2.getValue().KScore));
				}
				else
				{
					return Double.valueOf(o2.getValue().KScore).compareTo(Double.valueOf(o1.getValue().KScore));
				}
			}
		});
		proximityResult = new LinkedHashMap<String, DeveloperPathCount>();
		for (Entry<String, DeveloperPathCount> entry : list)
		{
			proximityResult.put(entry.getKey(), entry.getValue());
		}
		return proximityResult;
	}
}
