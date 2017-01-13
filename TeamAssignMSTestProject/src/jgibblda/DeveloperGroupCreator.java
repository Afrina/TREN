package jgibblda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import extend.ClusterDataSet.BugReport;
import extend.ClusterDataSet.DeveloperInfo;


public class DeveloperGroupCreator {
	HashMap<String, ArrayList<DeveloperInfo>> groupedDevelopers;
	double[][] theta;
	ArrayList<String> indexedDeveloperList;
	HashMap<String, DeveloperInfo> developers;
	ArrayList<BugReport> trainReports;
	public DeveloperGroupCreator(){}
	public DeveloperGroupCreator( HashMap<String, DeveloperInfo> _developers, double[][] _theta, ArrayList<String> _indexedDeveloperList){
		developers = _developers;
		theta = _theta;
		indexedDeveloperList = _indexedDeveloperList;
		groupedDevelopers = new HashMap<String, ArrayList<DeveloperInfo>>();
	}
	public HashMap<String, ArrayList<DeveloperInfo>> getGroups(){
		return groupedDevelopers;
	}
	public DeveloperGroupCreator(ArrayList<BugReport> _trainReports, double[][] _theta){
		trainReports = _trainReports;
		theta = _theta;
		groupedDevelopers = new HashMap<String, ArrayList<DeveloperInfo>>();
	}
	public void sortDevelopers(){
		for(String key : groupedDevelopers.keySet()){
			ArrayList<DeveloperInfo> devList = groupedDevelopers.get(key);
			Collections.sort(devList, new Comparator<DeveloperInfo>(){
				public int compare(DeveloperInfo o1, DeveloperInfo o2){
					Integer a = o2.exp;
					Integer b = o1.exp;
					int result = a.compareTo(b);
					return result;

				}
			});
			groupedDevelopers.replace(key, devList);
		}


			}
	public void seperateTestTopics(double[][] testTheta, ArrayList<BugReport> testData) throws IOException{
		System.out.println("chupppp "+testTheta.length);
		HashMap<String, ArrayList<String>> seperatedTests= new HashMap<String, ArrayList<String>>();
		for(int i =0;i<testTheta.length;i++){
			double[] column = testTheta[i];
			List columnList = Arrays.asList(ArrayUtils.toObject(column));
			double maxValue = (double) Collections.max(columnList);
			int topic  = columnList.indexOf(maxValue);

			ArrayList<String> tests = seperatedTests.get(Integer.toString(topic));
			if(tests==null){
				tests = new ArrayList<String>();
				tests.add(testData.get(i).getId());
				
				seperatedTests.put(Integer.toString(topic), tests);
			}
			else{
				tests.add(testData.get(i).getId());
				seperatedTests.replace(Integer.toString(topic), tests);
			}
		}
		File f = new File("Data\\loadSeperatedTest.txt");
		BufferedWriter teamOutput = new BufferedWriter(new FileWriter(f));
		for(String key : seperatedTests.keySet()){
			teamOutput.write(key);teamOutput.newLine();
			for(String s : seperatedTests.get(key)){
				teamOutput.write("\""+s+"\",");
			}
		}
		teamOutput.close();
//		
	}
	
	public ArrayList<String> identifyCorresspondingTopic(double[][] testTheta){
		ArrayList<String> testTopics = new ArrayList<String>();
		System.out.println("gggrrrrr "+testTheta.length);
		for(int i =0;i<testTheta.length;i++){
			double[] column = testTheta[i];
			List columnList = Arrays.asList(ArrayUtils.toObject(column));
			double maxValue = (double) Collections.max(columnList);
			int topic  = columnList.indexOf(maxValue);

			testTopics.add(Integer.toString(topic));
		}
		return testTopics;
	}
	public ArrayList<DeveloperInfo> getCorresspondingDevelopers(String key){
		return groupedDevelopers.get(key);
	}
	public void groupDevelopers(){
		for(int i = 0;i<theta.length;i++){
			double[] column = theta[i];
			List columnList = Arrays.asList(ArrayUtils.toObject(column));
			double maxValue = (double) Collections.max(columnList);
			int topic  = columnList.indexOf(maxValue);

			ArrayList<DeveloperInfo> dList = groupedDevelopers.get(Integer.toString(topic));
			if(dList == null ){
				dList = new ArrayList<DeveloperInfo>();
				DeveloperInfo d = developers.get(indexedDeveloperList.get(i));
				d.developerName = indexedDeveloperList.get(i);
				if(!containsDeveloper(dList, d.developerName)){dList.add(d);}
				groupedDevelopers.put(Integer.toString(topic), dList);
			}
			else{
				DeveloperInfo d = developers.get(indexedDeveloperList.get(i));
				d.developerName = indexedDeveloperList.get(i);
				if(!containsDeveloper(dList, d.developerName)){dList.add(d);}
				groupedDevelopers.replace(Integer.toString(topic), dList);
			}
		}
	}
	public boolean containsDeveloper(ArrayList<DeveloperInfo> dList, String name){

		for(DeveloperInfo d : dList){
			if(d.developerName.equals(name)){
				return true;
			}
		}
		return false;
	}
	public void print(){
		for(String key:groupedDevelopers.keySet()){
			System.out.println("........." + key);
			for(DeveloperInfo d : groupedDevelopers.get(key)){
				System.out.println(d.developerName);
			}
		}
	}
	public ArrayList<DeveloperInfo> updateExp(ArrayList<DeveloperInfo> dList, String name){
		for(DeveloperInfo d : dList){
			if(d.developerName.equals(name)){
				d.exp = d.exp+ 1;
				break;
			}
		}
		return dList;
	}
	// for bug ne.......................................................
	public void groupDevelopersForBug(){
		for(int i = 0;i<theta.length;i++){
			double[] column = theta[i];
			List columnList = Arrays.asList(ArrayUtils.toObject(column));
			double maxValue = (double) Collections.max(columnList);
			int topic  = columnList.indexOf(maxValue);

			ArrayList<DeveloperInfo> dList = groupedDevelopers.get(Integer.toString(topic));
			if(dList == null ){
				dList = new ArrayList<DeveloperInfo>();
				//DeveloperInfo d = developers.get(indexedDeveloperList.get(i));
				DeveloperInfo d = new DeveloperInfo("", "");
				BugReport b = trainReports.get(i);
				d.developerName = b.getDeveloper();
				d.exp = 1;
				if(!containsDeveloper(dList, d.developerName)){dList.add(d);}
				else{dList = updateExp(dList, d.developerName);};
				groupedDevelopers.put(Integer.toString(topic), dList);
				for(String s : b.getCommenterList()){
					DeveloperInfo d1 = new DeveloperInfo("", "");
					d1.developerName = s;
					d.exp = 1;
					if(!containsDeveloper(dList, d1.developerName)){dList.add(d1);}
					else{dList = updateExp(dList, d.developerName);};
					groupedDevelopers.put(Integer.toString(topic), dList);
				}
			}
			else{
				DeveloperInfo d = new DeveloperInfo("", "");
				BugReport b = trainReports.get(i);
				d.developerName = b.getDeveloper();
				if(!containsDeveloper(dList, d.developerName)){dList.add(d);}
				else{dList = updateExp(dList, d.developerName);};
				groupedDevelopers.replace(Integer.toString(topic), dList);
				for(String s : b.getCommenterList()){
					DeveloperInfo d1 = new DeveloperInfo("", "");
					d1.developerName = s;
					if(!containsDeveloper(dList, d1.developerName)){dList.add(d1);}
					else{dList = updateExp(dList, d.developerName);};
					groupedDevelopers.put(Integer.toString(topic), dList);
				}
			}
		}
		sortDevelopers();
	}
	
}
