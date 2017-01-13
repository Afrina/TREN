package recommendation;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import extend.ClusterDataSet.BugReport;


public class TrainTest {
	public static int counterFlag = 0;
	public int getTrainSize(int modelSize, int percent){
		return (modelSize*percent)/100;
	}
	public int getFlag(){
		return counterFlag;
	}
	public void prepareResultSet(ArrayList<String> groupedDocument, BugReport b, BufferedWriter output) throws IOException{
		HashSet<String> clusterDevelopers, bugDevelopers;ArrayList<Integer> load = new ArrayList<Integer>();
		clusterDevelopers = new HashSet<String>(); bugDevelopers = new HashSet<String>();
		for(int i = 0;i <groupedDocument.size();i++){
			clusterDevelopers.add(groupedDocument.get(i));
		}
		bugDevelopers.add(b.getDeveloper());
		for(String s : b.getCommenterList()){
			bugDevelopers.add(s);
		}
		int x = 0;
		for(String s : bugDevelopers){
			if(clusterDevelopers.contains(s)){
				x = x+ 1;
			}
		}
		output.write(clusterDevelopers.toString()); output.newLine();
		output.write(bugDevelopers.toString());output.newLine();
		output.write(bugDevelopers.size() + " "+x);
		if(x!=0) counterFlag = counterFlag + 1;
	}
	
}
