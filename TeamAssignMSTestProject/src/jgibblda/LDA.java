/*
 * Copyright (C) 2007 by
 * 
 * 	Xuan-Hieu Phan
 *	hieuxuan@ecei.tohoku.ac.jp or pxhieu@gmail.com
 * 	Graduate School of Information Sciences
 * 	Tohoku University
 * 
 *  Cam-Tu Nguyen
 *  ncamtu@gmail.com
 *  College of Technology
 *  Vietnam National University, Hanoi
 *
 * JGibbsLDA is a free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * JGibbsLDA is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JGibbsLDA; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */

package jgibblda;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sql.rowset.spi.XmlReader;

import network.Bug;
import network.NetworkReader;

import org.apache.lucene.document.Document;
import org.kohsuke.args4j.*;

import pathGeneration.DeveloperMetaPath;
import pathGeneration.PathCalculator;
import recommendation.Recommender;
import recommendation.TrainTest;
import recommendation.WorkLoadHandler;
import extend.ClusterDataSet.BugReport;
import extend.ClusterDataSet.BugReportIndexer;
import extend.ClusterDataSet.CommitKeywordCollector;
import extend.ClusterDataSet.DeveloperInfo;
import extend.ClusterDataSet.ExtendIndeing;
import extend.ClusterDataSet.ExtendSearcher;
import extend.ClusterDataSet.LuceneConstants;
import extend.ClusterDataSet.ReportKeywordCollector;
import CommitXMLCreatorAndMerger.ChangedFileXmlCreator;
import CommitXMLCreatorAndMerger.CommitFileMerger;
import CommitXMLCreatorAndMerger.XMLFileReader;
import SourceParser.TestCreationProperties;

public class LDA {

	public static void main(String args[]){
		
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		System.out.println(heapMaxSize);
		long heapSize = Runtime.getRuntime().totalMemory();System.out.println(heapSize);
		
		long heapFreeSize1 = Runtime.getRuntime().freeMemory();
		System.out.println(heapFreeSize1);
//		LDACmdOption option = new LDACmdOption();
//		CmdLineParser parser = new CmdLineParser(option);
		try {
//						if (args.length == 0){
//							System.out.println("came here"+option.dir);
//							showHelp(parser);
//							return;
//						}
						
			TestCreationProperties properties = new TestCreationProperties("RunProperties.txt");
			properties.readProperties();
			System.out.println(properties.LDADataCreator);

			/// I took the importahnt attributes from xml file
//									ChangedFileXmlCreator changeFileCreator = new ChangedFileXmlCreator(properties.onlyFileNameTxt);
//									changeFileCreator.Initialize();
//									changeFileCreator.NewTextFileReader();
//									changeFileCreator.xmlFileCreator();
//									changeFileCreator.xmlFileWriter(properties.onlyFileNameXml);
//			/// mergeed all attribute in single xml
//									CommitFileMerger commitFile = new CommitFileMerger(properties.onlyAuthorNameXml, properties.onlyFileNameXml);
//									commitFile.Initialize();
//									commitFile.mergeContents();
//									commitFile.updateContents();
//			// Parsed the single xml and do one time indexing
			//CommitKeywordCollector commitKeywordCollector = new CommitKeywordCollector(properties.mergedXml);
			//commitKeywordCollector.collectCommitContents();
			System.out.println("byhvf bud");
			//commitKeywordCollector.printCommits();
			XMLFileReader xmlFileReader = new XMLFileReader();
			List<BugReport> bugReports = xmlFileReader.getBugReports(xmlFileReader.ReadXmlFile(properties.networkXML1));
			TrainTest trainTest = new TrainTest();
			int trainDataSize = trainTest.getTrainSize(2500, 10);
			ArrayList<BugReport> trainData =  new ArrayList<BugReport>();
			ArrayList<BugReport> testData =  new ArrayList<BugReport>();
//			List<BugReport> workLoadReports = xmlFileReader.getLoadBugReports(xmlFileReader.ReadXmlFile(properties.workLoad));
//			ArrayList<String> loadIds = new ArrayList<String>(
//		    Arrays.asList("486021","486823","490698","493017","496789","497354","500503","506399","507920","488108","490765","506315"));
//			
//			ArrayList<BugReport> loadData = new ArrayList<BugReport>();
//			System.out.println("tsize is:"+workLoadReports.size());
//			for(int i=0;i<workLoadReports.size();i++){
//				if(loadIds.contains(workLoadReports.get(i).getId())){
//					testData.add(workLoadReports.get(i));
//				}
//		}
			System.out.println("tsize is:"+testData.size());
//			BugReportIndexer loadBugReportIndexer = new BugReportIndexer(loadData);
//			loadBugReportIndexer.createLoadIndex();	
			
			
			for(int i=0;i<bugReports.size()-trainDataSize-1;i++){
				trainData.add(bugReports.get(i));
			}
			for(int i=bugReports.size()-trainDataSize;i<bugReports.size();i++){
				testData.add(bugReports.get(i));
			}
			System.out.println(trainDataSize+"test datasize"+testData.size());;
			BugReportIndexer bugReportIndexer = new BugReportIndexer(trainData);
			
			
			//bugReportIndexer.createIndex();
//			ExtendSearcher reportSearcher = new ExtendSearcher(LuceneConstants.BUGREPORTINDEX);
//			ArrayList<Document> reports = reportSearcher.getReports(reportSearcher.search());
//			
//			ReportKeywordCollector reportKeywordCollector = new ReportKeywordCollector(commitKeywordCollector.getDevelopersAfterCommit(),reports,bugReports);
//			reportKeywordCollector.collectReportConetnt();
//			
//			
//			ExtendIndeing indexing = new ExtendIndeing(reportKeywordCollector.getDevelopersAfterReport());
//			indexing.createIndex();
						
//			ExtendSearcher searcher = new ExtendSearcher(LuceneConstants.INDEX);
//			LDADataCreator dataCreator = new LDADataCreator(searcher.getMaps(searcher.search()));
//			//searcher.prepareIndexedDeveloperList(searcher.search());
//			dataCreator.createDataFile(properties.LDADataCreator);
//			System.out.println("developer-profiles.dat file saved");
			//......................doing new again
			
//			ExtendSearcher searcher = new ExtendSearcher(LuceneConstants.BUGREPORTINDEX);
//			LDADataCreator dataCreator = new LDADataCreator(searcher.getMaps(searcher.search()));
//			//searcher.prepareIndexedDeveloperList(searcher.search());
//			dataCreator.createDataFile(properties.LDADataNew);
			System.out.println("developer-profilesNew.dat file saved");
			//.......................doing new again

			long heapFreeSize = Runtime.getRuntime().freeMemory();
			System.out.println(heapFreeSize);
//			Estimator estimator = null;
//			if (option.est || option.estc){
//				 estimator = new Estimator();
//				estimator.init(option);
//				estimator.estimate();
//			}
//			else if (option.inf){
//				System.out.println("............is inf");
//				Inferencer inferencer = new Inferencer();
//				inferencer.init(option);
//
//				Model newModel = inferencer.inference();
//
//				for (int i = 0; i < newModel.phi.length; ++i){
//					//phi: K * V
//					System.out.println("-----------------------\ntopic" + i  + " : ");
//					for (int j = 0; j < 10; ++j){
//						System.out.println(inferencer.globalDict.id2word.get(j) + "\t" + newModel.phi[i][j]);
//					}
//				}
//			}
			
//			DeveloperGroupCreator groupCreator = new DeveloperGroupCreator( reportKeywordCollector.getDevelopersAfterReport(),estimator.trnModel.getTheta(), searcher.getIndexedDeveloperList());
//			groupCreator.groupDevelopers();
//			groupCreator.print();
			//bug group creator
			System.out.println("I am here..........");
			//DeveloperGroupCreator groupCreator = new DeveloperGroupCreator( trainData,estimator.trnModel.getTheta());
			//groupCreator.groupDevelopersForBug();
//			JsonHandler json = new JsonHandler(groupCreator.getGroups());
//			json.createJsonForOnlyGroups();
			JsonHandler json = new JsonHandler();
			String ind = "5"; 
			//ArrayList<String> names = json.readJsonForOnlyGroups(ind);
			ArrayList<String> names = new ArrayList<String>(Arrays.asList(
					"Stefan Ocke","Stephen Haberman","Curtis Windatt","Satyam Kandula","Kent Johnson","Pascal Rapicault","Matt Whitlock","Marc Khouzam","Manoj Palat","Frederic Fusier","Mahmood Ali","Deepak Azad","Kaloyan Raev","Richard","Benjamin Muskalla","Srikanth Sankaran","Ievgen Lukash","Dani Megert","Olivier Thomann","David Audel","Jay Arthanareeswaran","Justin Corpron","Markus Keller","Rajesh","Tilak Sharma","Dave Boden","Karim Fatehi","Ayushman Jain","Michael Rennie","Ivan Motsch","Andreas Gudian","Raksha Vasisht","Jason Faust","Terry Parker","Walter Harley","Darin Wright","Christian humer"
					
					
					
					));
					
			ArrayList<String> testIds = new ArrayList<String>(
				    Arrays.asList("392099"));//  8
			String kind = "111";
			System.out.println(trainData.get(0).getReportingTime());
			System.out.println(trainData.get(trainData.size()-1).getReportingTime());
//			BugReport a = json.getReportById(testData, "486823");
//			System.out.println(a.getDeveloper());
			//json.readJsonForMetaPaths("0");
			
			
			//groupCreator.print();
			//bug group creator
			
			System.out.println("I am here..........2");
			//test xml bug report indexing
			BugReportIndexer testBugReportIndexer = new BugReportIndexer(testData);
			//testBugReportIndexer.createTestIndex();
			
//			BugReportIndexer testBugReportIndexer = new BugReportIndexer(testXmlFileReader.getBugReports(testXmlFileReader.ReadXmlFile(properties.testData)));
//			testBugReportIndexer.createTestIndex();
//			List<BugReport> testReports = testXmlFileReader.getBugReports(testXmlFileReader.ReadXmlFile(properties.testData));
//			
			
			
			ExtendSearcher testSearcher = new ExtendSearcher(LuceneConstants.TESTINDEX);
//			LDADataCreator testDataCreator = new LDADataCreator(testSearcher.getMaps(testSearcher.search()));
//			testDataCreator.createDataFile(properties.LDATestDataCreator);
//			System.out.println("testBug Reports .dat file saved");
//			
//			ExtendSearcher loadSearcher = new ExtendSearcher(LuceneConstants.LOADINDEX);
//			LDADataCreator loadDataCreator = new LDADataCreator(loadSearcher.getMaps(loadSearcher.search()));
//			loadDataCreator.createDataFile(properties.LDALoadDataCreator);
//			System.out.println("load Reports .dat file saved");
//			System.out.println(testData.get(0).getSeverity());
//			
//			ArrayList<String> reportingDates = new ArrayList<String>();
//			reportingDates = testSearcher.getReportingDateList(testSearcher.search());

			
			
			//testing data
//			LDACmdOption testOption = new LDACmdOption();
//			testOption.inf = true;
//			testOption.dir = option.dir;
//			testOption.modelName = "model-final";
//			testOption.twords = 20;
//			testOption.niters = 100;
//			//testOption.dfile = "newData.dat";
//			testOption.dfile = "testBugReports.dat";
//
//			Inferencer inferencer1 = new Inferencer(); 
//			inferencer1.init(testOption); 
//			Model newModel = inferencer1.inference(); 
//			
			
			//ajera
//			DeveloperGroupCreator groupCreator = new DeveloperGroupCreator( loadData,newModel.getTheta());
//			groupCreator.groupDevelopersForBug();
//			JsonHandler json2 = new JsonHandler(groupCreator.getGroups());
//			json2.createJsonForOnlyGroups();
//			groupCreator.seperateTestTopics(newModel.getTheta(), loadData);
			
			
			System.out.println("till nw done");
			NetworkReader reader = new NetworkReader(properties.networkXML1, properties.networkXML2);
			reader.collectNetworkElements(trainData.size());
			System.out.println("graph generation");
			
			long heapMaxSize1 = Runtime.getRuntime().maxMemory();
			System.out.println(heapMaxSize1);
			long heapSize1 = Runtime.getRuntime().totalMemory();System.out.println(heapSize1);
//			
//			long heapFreeSize11 = Runtime.getRuntime().freeMemory();System.out.println(heapFreeSize11);
//			
			//List<BugReport> workLoadReports = xmlFileReader.getBugReports(xmlFileReader.ReadXmlFile(properties.workLoad));
//			WorkLoadHandler workLoadHandler = new WorkLoadHandler();
//			workLoadHandler.calculateWorkLoad(workLoadReports);
//			workLoadHandler.createJsonForWorkLoads();
//		
	//		workLoadHandler.print();
////			
//			ArrayList<String> identifiedTopics = groupCreator.identifyCorresspondingTopic(newModel.theta);
//			System.out.println("oooooooooooooooooooooooooooooooooooooooo  "+identifiedTopics.size());
////			HashSet<String> uniqueIdentifiedTopics = new HashSet<String>();
////			HashMap<String, HashMap<String, DeveloperMetaPath>> calculatedPathList = new HashMap<String, HashMap<String,DeveloperMetaPath>>();
////			for(int i = 0;i<identifiedTopics.size();i++){
////				uniqueIdentifiedTopics.add(identifiedTopics.get(i));
////			}
////		
////			System.out.println("after dcvbfdjbg");
////			long heapSize2 = Runtime.getRuntime().totalMemory();System.out.println(heapSize2);
////			
////			long heapFreeSize2 = Runtime.getRuntime().freeMemory();
////			System.out.println(heapFreeSize2);
//			
//			
//			File f = new File("Data\\Evaluation\\Krecom3"+".txt");
//			BufferedWriter recom = new BufferedWriter(new FileWriter(f));
//			recom.write("");
//			recom.close();
//			recom = new BufferedWriter(new FileWriter(f,true));
			File fk = new File("Data\\Krecom"+kind+".txt");
			BufferedWriter Krecom = new BufferedWriter(new FileWriter(fk));
			Krecom.write("");
			Krecom.close();
			Krecom = new BufferedWriter(new FileWriter(fk,true));
			
			int flag=0;
			PathCalculator calculator;HashMap<String, DeveloperMetaPath> calculatedPath;
			calculator = new PathCalculator(reader.getNetworkChunks(), names, "");
			calculatedPath = calculator.calculatePath(); 
			reader= null; Recommender recommender ;
			for(int t = 0;t<testIds.size() ; t++){
				BugReport test = json.getReportById(testData, testIds.get(t));
				calculator.scoreCalc(calculatedPath, test.getReportingTime());
				
				long heapMaxSizen = Runtime.getRuntime().maxMemory();
				System.out.println(heapMaxSizen);
				long heapSizen = Runtime.getRuntime().totalMemory();System.out.println(heapSizen);	
				long heapFreeSizen = Runtime.getRuntime().freeMemory();System.out.println(heapFreeSizen);
				
				System.out.println(test.getId());
				System.out.println("calc done");
//				recommender = new Recommender(test,calculator.getScoreCalculator().getProximityResult(), null, names.size());
//				recom.newLine();
//				recom.write("for bug " +test.getId()+" .....................................................................");
//				recommender.recemmendation(recom);
//				recom.newLine();
//				recom.write("top n is: "+recommender.getTopN());
//				recom.newLine();
//				recom.write("Preccission is: "+recommender.getPrecission());
//				recom.newLine();
//				recom.write("Recall is : "+recommender.getRecall());
//				recom.newLine();
//				recom.write("GT is is : "+recommender.getGroundTruth());
//				recom.newLine();
//				System.out.println(testIds.size()+" "+t);
				recommender = new Recommender(test,calculator.getScoreCalculator().sortForKScore("DESC", calculator.getScoreCalculator().getProximityResult()), null, names.size());
				Krecom.newLine();
				Krecom.write("for bug " +test.getId()+" .....................................................................");
				recommender.Krecemmendation(Krecom);
				Krecom.newLine();
				Krecom.write("top n is: "+recommender.getTopN());
				Krecom.newLine();
				Krecom.write("Preccission is: "+recommender.getPrecission());
				Krecom.newLine();
				Krecom.write("Recall is : "+recommender.getRecall());
				Krecom.newLine();
				Krecom.write("GT is is : "+recommender.getGroundTruth());
				Krecom.newLine();
			}
			
			 //recom.close();
			 Krecom.close();
			long heapSize5= Runtime.getRuntime().totalMemory();System.out.println(heapSize5);
			
			long heapFreeSize5 = Runtime.getRuntime().freeMemory();
			System.out.println(heapFreeSize5);
////			File f1 = new File("Data\\group_"+ind+".json");
////			BufferedWriter teamOutput = new BufferedWriter(new FileWriter(f1));
////			teamOutput.write("");
////			teamOutput.close();
////			teamOutput = new BufferedWriter(new FileWriter(f1,true));
////			
////			File f2 = new File("Data\\grouptest_"+ind+".json");
////			BufferedWriter teamOutput2 = new BufferedWriter(new FileWriter(f2));
////			teamOutput2.write("");
////			teamOutput2.close();
////			teamOutput2 = new BufferedWriter(new FileWriter(f2,true));
////			
////			json.writeForMetaPaths(calculatedPath, ind,teamOutput);
////			json.writeForMetaPathsTest(calculatedPath, ind, teamOutput2);
//			System.out.println("writting done");
//			
//			
//			
////			System.out.println("chunk sise"+ reader.getNetworkChunks().size());
////			calculatedPath = calculator.calculatePath()
////			for(String s : uniqueIdentifiedTopics){
////				ArrayList<DeveloperInfo> developers = groupCreator.getCorresspondingDevelopers(s);
////				ArrayList<String> name = new ArrayList<String>();
////				if(developers.size()>50){
////					for(int j=0;j<50;j++){
////						name.add(developers.get(j).developerName);
////					}	
////				}
////				else{
////					for(int j=0;j<developers.size();j++){
////						name.add(developers.get(j).developerName);
////					}
////				}
////				System.out.println("team size"+developers.size());
////
////				System.out.println("nnnnnnn "+name.toString());
////				calculator = new PathCalculator(reader.getNetworkChunks(), name, reportingDates.get(flag));
////				System.out.println("chunk sise"+ reader.getNetworkChunks().size());
////				calculatedPath = calculator.calculatePath(); 
////				calculatedPathList.put(s, calculatedPath);
////				System.out.println("one down "+cb);cb++;
////				long heapSize3 = Runtime.getRuntime().totalMemory();System.out.println(heapSize3);
////				
////				long heapFreeSize3 = Runtime.getRuntime().freeMemory();
////				System.out.println(heapFreeSize3);
////			}
//			
//			System.out.println("do ultimate task");
//			File f1 = new File("Data\\result.txt");
//			BufferedWriter teamOutput = new BufferedWriter(new FileWriter(f1));
//			teamOutput.write("");
//			teamOutput.close();
//			teamOutput = new BufferedWriter(new FileWriter(f1,true));
//			for(int i=0;i<identifiedTopics.size();i++){
//				ArrayList<DeveloperInfo> developers = groupCreator.getCorresspondingDevelopers(identifiedTopics.get(i));
//				ArrayList<String> name = new ArrayList<String>();
//				if(developers.size()>50){
//					for(int j=0;j<50;j++){
//						name.add(developers.get(j).developerName);
//					}	
//				}
//				else{
//					for(int j=0;j<developers.size();j++){
//						name.add(developers.get(j).developerName);
//					}
//				}
//				System.out.println("team size"+developers.size());
//				trainTest.prepareResultSet(name, testData.get(i), teamOutput);
//				System.out.println("nnnnnnn "+name.toString());
//				PathCalculator calculator = new PathCalculator();
//				calculator.scoreCalc(calculatedPathList.get(identifiedTopics.get(i)), testData.get(i).getReportingTime());
//				Recommender recommender = new Recommender(testData.get(i),calculator.getScoreCalculator().getProximityResult(), workLoadHandler.getDeveloperWorkLoads(), 50);
//				teamOutput.newLine();
//				teamOutput.write("for bug " + i+" .....................................................................");
//				recommender.recemmendation(teamOutput);
//				teamOutput.newLine();
//				teamOutput.write("topn n score is::" + recommender.getTopN()); 
//			}
			//System.out.println("recall "+trainTest.getFlag());
			//teamOutput.close();
//			//workLoadHandler.print();
//			
			
		}
		catch (Exception e){
			System.out.println("Error in main: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	public static void showHelp(CmdLineParser parser){
		System.out.println("LDA [options ...] [arguments...]");
		parser.printUsage(System.out);
	}

}
