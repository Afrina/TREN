package pathGeneration;

import java.util.ArrayList;
import java.util.HashMap;

import network.Bug;
import network.Comment;
import network.Component;
import network.Developer;
import network.NetworkLink;
import network.Product;

public class CommonComponentPathGenerator{

	ArrayList<ArrayList<NetworkLink>> chunkNetworks;
	ArrayList<String> developerNameList;
	HashMap<String, DeveloperMetaPath> developerMetaPaths;
	public CommonComponentPathGenerator(ArrayList<ArrayList<NetworkLink>> _chunkNetworks, ArrayList<String> _developerNameList,
			HashMap<String, DeveloperMetaPath> _developerMetaPaths){
		chunkNetworks = _chunkNetworks;
		developerNameList = _developerNameList;
		developerMetaPaths = _developerMetaPaths;
	}
	public HashMap<String, DeveloperMetaPath> getFinalDeveloperMetaPaths(){
		return developerMetaPaths;

	}
	public void calculatePath() {
		for(int i=0;i<developerNameList.size();i++){
			//System.out.println(i);
			String source = developerNameList.get(i);
			for(int j=0;j<developerNameList.size();j++){
				if(!developerNameList.get(j).equals(source)){
					String destination = developerNameList.get(j);
					for(int k=0;k<chunkNetworks.size();k++){
						CalculateMetaPathFour(source,destination,chunkNetworks.get(k));
						CalculateMetaPathFive(source,destination,chunkNetworks.get(k));
						CalculateMetaPathSix(source, destination, chunkNetworks.get(k));
					}
				}
			}
		}

	}
	//D–B–C–B–D
	public void CalculateMetaPathFour(String source, String destination, ArrayList<NetworkLink> HN) {
		String componentName = getNetworkComponent(HN);
		String bugId = getNetworkId(HN);
		for(NetworkLink DB : HN){
			if(DB.firstNode.nodeType=="D" && DB.secondNode.nodeType=="B"){
				Developer s = (Developer) DB.firstNode.customNodes;
				Bug DB_b = (Bug) DB.secondNode.customNodes;
				if(s.name.equals(source)){
					for(NetworkLink BC : HN){
						if(BC.firstNode.nodeType == "B" && BC.secondNode.nodeType == "C"){
							Bug BC_b = (Bug) BC.firstNode.customNodes;
							Component BC_c = (Component) BC.secondNode.customNodes;
							if(DB_b.bugID.equals(BC_b.bugID)){
								for(ArrayList<NetworkLink> tempHN : chunkNetworks){
									String tempComponentName = getNetworkComponent(tempHN);
									String tempBugId = getNetworkId(tempHN);
									if(componentName.equals(tempComponentName) && !bugId.equals(tempBugId)){
										for(NetworkLink CB : tempHN){
											if(CB.firstNode.nodeType=="C" && CB.secondNode.nodeType=="B"){
												for(NetworkLink BD : tempHN){
													if(BD.firstNode.nodeType == "B" && BD.secondNode.nodeType == "D"){
														Developer dest = (Developer) BD.secondNode.customNodes;
														if(dest.name.equals(destination)){
															ArrayList<NetworkLink> meatPath = new ArrayList<NetworkLink>();
															meatPath.add(DB);
															meatPath.add(BC);
															meatPath.add(CB);
															meatPath.add(BD);
															updateDeveloperPathListOnComponent(meatPath,  source);
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
	//D–B–C–B–T–D
	public void CalculateMetaPathFive(String source, String destination, ArrayList<NetworkLink> HN) {
		String componentName = getNetworkComponent(HN);
		String bugId = getNetworkId(HN);
		for(NetworkLink DB : HN){
			if(DB.firstNode.nodeType=="D" && DB.secondNode.nodeType=="B"){
				Developer s = (Developer) DB.firstNode.customNodes;
				if(s.name.equals(source)){
					for(NetworkLink BC : HN){
						if(BC.firstNode.nodeType == "B" && BC.secondNode.nodeType == "C"){
							for(ArrayList<NetworkLink> tempHN : chunkNetworks){
								String tempComponentName = getNetworkComponent(tempHN);
								String tempBugId = getNetworkId(tempHN);
								if(componentName.equals(tempComponentName) && !bugId.equals(tempBugId)){
									for(NetworkLink CB : tempHN){
										if(CB.firstNode.nodeType=="C" && CB.secondNode.nodeType=="B"){
											for(NetworkLink BT: tempHN){
												if(BT.firstNode.nodeType == "B" && BT.secondNode.nodeType == "T"){
													Bug BT_b = (Bug) BT.firstNode.customNodes;
													Comment BT_t = (Comment) BT.secondNode.customNodes;
													for(NetworkLink TD : tempHN){
														if(TD.firstNode.nodeType == "T" && TD.secondNode.nodeType == "D"){
															Comment TD_t = (Comment) TD.firstNode.customNodes;
															Developer d2 = (Developer) TD.secondNode.customNodes;
															if(BT_t.commentID.equals(TD_t.commentID)){
																if(d2.name.equals(destination)){
																	ArrayList<NetworkLink> meatPath = new ArrayList<NetworkLink>();
																	meatPath.add(DB);
																	meatPath.add(BC);
																	meatPath.add(CB);
																	meatPath.add(BT);
																	meatPath.add(TD);
																	updateDeveloperPathListOnComponent(meatPath, source);
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
		}
	}
	//D–T–B–C–B–T–D
	public void CalculateMetaPathSix(String source, String destination, ArrayList<NetworkLink> HN) {
		String componentName = getNetworkComponent(HN);
		String bugId = getNetworkId(HN);
		for(NetworkLink DT : HN){
			if(DT.firstNode.nodeType=="D" && DT.secondNode.nodeType=="T"){
				Developer s = (Developer) DT.firstNode.customNodes;
				Comment DT_t = (Comment) DT.secondNode.customNodes;
				if(s.name.equals(source)){
					for(NetworkLink TB : HN){
						if(TB.firstNode.nodeType == "T" && TB.secondNode.nodeType == "B"){
							Comment TB_t = (Comment) TB.firstNode.customNodes;
							if(DT_t.commentID.equals(TB_t.commentID)){
								for(NetworkLink BC : HN){
									if(BC.firstNode.nodeType == "B" && BC.secondNode.nodeType =="C"){
										for(ArrayList<NetworkLink> tempHN : chunkNetworks){
											String tempComponentName = getNetworkComponent(tempHN);
											String tempBugId = getNetworkId(tempHN);
											if(componentName.equals(tempComponentName) && !bugId.equals(tempBugId)){
												for(NetworkLink CB : tempHN){
													if(CB.firstNode.nodeType == "C" && CB.secondNode.nodeType == "B"){
														for(NetworkLink BT: tempHN){
															if(BT.firstNode.nodeType == "B" && BT.secondNode.nodeType == "T"){
																Comment BT_t = (Comment) BT.secondNode.customNodes;
																for(NetworkLink TD : tempHN){
																	if(TD.firstNode.nodeType == "T" && TD.secondNode.nodeType == "D"){
																		Comment TD_t = (Comment) TD.firstNode.customNodes;
																		if(BT_t.commentID.equals(TD_t.commentID)){
																			Developer d2 = (Developer) TD.secondNode.customNodes;
																			if(d2.name.equals(destination)){
																				ArrayList<NetworkLink> meatPath = new ArrayList<NetworkLink>();
																				meatPath.add(DT);
																				meatPath.add(TB);
																				meatPath.add(BC);
																				meatPath.add(CB);
																				meatPath.add(BT);
																				meatPath.add(TD);
																				updateDeveloperPathListOnComponent(meatPath, source);
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
					}
				}
			}
		}
	}
	public String getNetworkComponent(ArrayList<NetworkLink> HN){
		for(NetworkLink link: HN){
			if(link.relationType==8){
				Component c = (Component) link.firstNode.customNodes;
				return c.componentName;
			}
		}
		return null;
	}
	public String getNetworkId(ArrayList<NetworkLink> HN){
		for(NetworkLink link: HN){
			if(link.relationType==1){
				Bug b = (Bug) link.firstNode.customNodes;
				return b.bugID;
			}
		}
		return null;
	}
	public void updateDeveloperPathListOnComponent(ArrayList<NetworkLink> pathList, String source){

		DeveloperMetaPath developerPathMap = developerMetaPaths.get(source);
		if(developerPathMap==null){
			developerPathMap = new DeveloperMetaPath();
			developerPathMap.sameComponentMetaPaths.add(pathList);
			developerMetaPaths.put(source, developerPathMap);
		}
		else{
			developerPathMap.sameComponentMetaPaths.add(pathList);
			developerMetaPaths.replace(source, developerPathMap);
		}
	}



}
