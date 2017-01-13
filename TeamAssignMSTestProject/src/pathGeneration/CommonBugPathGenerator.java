package pathGeneration;

import java.util.ArrayList;
import java.util.HashMap;

import network.Bug;
import network.Comment;
import network.Developer;
import network.NetworkLink;
import network.NetworkNode;

public class CommonBugPathGenerator{


	ArrayList<ArrayList<NetworkLink>> chunkNetworks;
	ArrayList<String> developerNameList;
	HashMap<String, DeveloperMetaPath> developerMetaPaths;
	public CommonBugPathGenerator(ArrayList<ArrayList<NetworkLink>> _chunkNetworks, ArrayList<String> _developerNameList){
		chunkNetworks = _chunkNetworks;
		developerNameList = _developerNameList;
		developerMetaPaths = new HashMap<String, DeveloperMetaPath>();
	}
	public HashMap<String, DeveloperMetaPath> getDeveloperMetaPaths(){
		return developerMetaPaths;
	} 
	public void calculatePath(){
		for(int i=0;i<developerNameList.size();i++){
			String source = developerNameList.get(i);
			//System.out.println(i);
			for(int j=0;j<developerNameList.size();j++){
				if(!developerNameList.get(j).equals(source)){
					//System.out.println("checking with "+source+" "+developerNameList.get(j) );
					String destination = developerNameList.get(j);
					for(int k=0;k<chunkNetworks.size();k++){
						//						/System.out.println("here here");
						calculateMetaPathOne(source, destination, chunkNetworks.get(k));
						calculateMetaPathTwo(source, destination, chunkNetworks.get(k));
						calculateMetaPathThree(source, destination, chunkNetworks.get(k));
					}
				}		
			}
			long heapSize3 = Runtime.getRuntime().totalMemory();System.out.println(heapSize3);
			
			long heapFreeSize3 = Runtime.getRuntime().freeMemory();
			System.out.println(heapFreeSize3);
		}
	}

	public void calculateMetaPathOne(String source, String destination, ArrayList<NetworkLink> HN){
		//printHN(HN);
		for( NetworkLink l : HN){
			if(l.firstNode.nodeType=="D" && l.secondNode.nodeType=="B" ){
				Developer s = (Developer) l.firstNode.customNodes;
				if(s.name.equals(source))
					for(NetworkLink l1 : HN){
						if(l1.firstNode.nodeType=="B" && l1.secondNode.nodeType=="D"){
							Developer d = (Developer) l1.secondNode.customNodes;
							if(d.name.equals(destination)){
								//System.out.println("matches.......................");
								ArrayList<NetworkLink> metaPath = new ArrayList<NetworkLink>();
								metaPath.add(l);
								metaPath.add(l1);
								updateDeveloperPathListOnBug(metaPath, source);
							}
						}
					}
			}
		}

	}
	public void calculateMetaPathTwo(String source, String destination, ArrayList<NetworkLink> HN){
		for(NetworkLink l: HN){
			if(l.firstNode.nodeType=="D" && l.secondNode.nodeType=="B"){
				Developer d = (Developer) l.firstNode.customNodes;
				Bug b = (Bug) l.secondNode.customNodes;
				if(d.name.equals(source)){
					for(NetworkLink l1:HN ){
						if(l1.firstNode.nodeType=="B" && l1.secondNode.nodeType=="T"){
							Bug b1 = (Bug) l1.firstNode.customNodes;
							Comment t1 = (Comment) l1.secondNode.customNodes;
							if(b.bugID.equals(b1.bugID)){
								for(NetworkLink l2 : HN){
									if(l2.firstNode.nodeType=="T" && l2.secondNode.nodeType=="D"){
										Comment t2= (Comment) l2.firstNode.customNodes;
										if(t1.commentID.equals(t2.commentID)){
											Developer d2 = (Developer) l2.secondNode.customNodes;
											if(d2.name.equals(destination)){
												Bug bx = (Bug) l.secondNode.customNodes; Comment t = (Comment) l2.firstNode.customNodes;

												//System.out.println("???????????????????????? "+d.name+" "+bx.bugID+" "+t.commentID+" "+d2.name);
												ArrayList<NetworkLink> metaPath = new ArrayList<NetworkLink>();
												metaPath.add(l);
												metaPath.add(l1);
												metaPath.add(l2);
												updateDeveloperPathListOnBug(metaPath, source);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public void calculateMetaPathThree(String source, String destination, ArrayList<NetworkLink> HN){
		for(NetworkLink l: HN){
			if(l.firstNode.nodeType=="D" && l.secondNode.nodeType=="T"){
				Developer d = (Developer) l.firstNode.customNodes;
				Comment t = (Comment) l.secondNode.customNodes;
				if(d.name.equals(source)){
					for(NetworkLink l1 : HN){
						if(l1.firstNode.nodeType=="T" && l1.secondNode.nodeType=="B"){
							Comment t1 = (Comment) l1.firstNode.customNodes;
							Bug b1 = (Bug) l1.secondNode.customNodes;
							if(t.commentID.equals(t1.commentID)){
								for(NetworkLink l2: HN){
									if(l2.firstNode.nodeType =="B" && l2.secondNode.nodeType=="T"){
										Bug b2 = (Bug) l2.firstNode.customNodes;
										Comment t2 = (Comment) l2.secondNode.customNodes;
										if(b1.bugID.equals(b2.bugID)){
											for(NetworkLink l3 : HN){
												if(l3.firstNode.nodeType=="T" && l3.secondNode.nodeType=="D" ){
													Comment t3 = (Comment) l3.firstNode.customNodes;
													if(t2.commentID.equals(t3.commentID) && !t1.commentID.equals(t3.commentID)){
														Developer d2 = (Developer) l3.secondNode.customNodes;
														if(d2.name.equals(destination)){
															Bug b = (Bug) l2.firstNode.customNodes; 

															//System.out.println("ooooooooooooooooooooooo "+d.name+" "+t1.commentID+" "+b.bugID+" "+t3.commentID+" "+d2.name);												
															ArrayList<NetworkLink> metaPath = new ArrayList<NetworkLink>();
															metaPath.add(l);
															metaPath.add(l1);
															metaPath.add(l2);
															metaPath.add(l3);
															updateDeveloperPathListOnBug(metaPath, source);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public void printHN(ArrayList<NetworkLink> HN){
		for(NetworkLink l:HN){
			if(l.relationType==2){
				Bug b = (Bug) l.secondNode.customNodes;	
				//System.out.println("see "+b.bugID);
				break;
			}
		}
	}
	public void updateDeveloperPathListOnBug(ArrayList<NetworkLink> pathList, String source){
		DeveloperMetaPath developerPathMap = developerMetaPaths.get(source);
		if(developerPathMap==null){
			developerPathMap = new DeveloperMetaPath();
			developerPathMap.sameBugMetaPaths.add(pathList);
			developerMetaPaths.put(source, developerPathMap);
		}
		else{
			developerPathMap.sameBugMetaPaths.add(pathList);
			developerMetaPaths.replace(source, developerPathMap);
		}
	}


}
