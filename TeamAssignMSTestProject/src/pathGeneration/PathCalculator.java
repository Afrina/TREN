package pathGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import network.Bug;
import network.Comment;
import network.Component;
import network.Developer;
import network.NetworkLink;
import network.Product;

public class PathCalculator {
	ArrayList<ArrayList<NetworkLink>> chunkNetworks;
	ArrayList<String> developerNameList;
	HashMap<String, DeveloperMetaPath> developerMetaPaths;
	ArrayList<CheckedDeveloper> checkedDevelopersBug;
	ArrayList<CheckedDeveloper> checkedDevelopersCom;
	public ScoreCalculator scoreCalculator;
	String bugDate;
	HashMap<String, DeveloperPathCount> proximityResult;
	public PathCalculator(){}
	public PathCalculator(ArrayList<ArrayList<NetworkLink>> _chunkNetworks, ArrayList<String> _developerNameList, String _bugDate){
		chunkNetworks = _chunkNetworks;
		developerNameList = _developerNameList;
		checkedDevelopersBug = new ArrayList<CheckedDeveloper>();
		checkedDevelopersCom = new ArrayList<CheckedDeveloper>();
		bugDate = _bugDate;

	}
	public ScoreCalculator getScoreCalculator(){
		return scoreCalculator;
	}
	public HashMap<String, DeveloperMetaPath> calculatePath(){
		CommonBugPathGenerator sameBugCalculator = new CommonBugPathGenerator(chunkNetworks, developerNameList);
		sameBugCalculator.calculatePath();
		CommonComponentPathGenerator sameComponentCalculator = new CommonComponentPathGenerator(chunkNetworks, developerNameList,sameBugCalculator.getDeveloperMetaPaths());
		sameComponentCalculator.calculatePath();

		developerMetaPaths = sameComponentCalculator.getFinalDeveloperMetaPaths();
		System.out.println(developerMetaPaths.size()+ "alreddat7y this done");
		//prunRedundantPaths(identifyRedundantPaths());
		//print();
		return developerMetaPaths;
	
	}
	public void scoreCalc(HashMap<String, DeveloperMetaPath> developerMetaPaths, String bugDate){
		scoreCalculator = new ScoreCalculator(bugDate);
		scoreCalculator.initializeCounters(developerMetaPaths);
		scoreCalculator.sortDevelopers();
		//scoreCalculator.printScore();
	}
	
	public void prunRedundantPaths(HashMap<String, ArrayList<PrunedData>> redundantPaths){
		for(String key : redundantPaths.keySet()){
			//System.out.println("goooooo"+key + developerMetaPaths.get(key).sameBugMetaPaths.size()+" "+developerMetaPaths.get(key).sameComponentMetaPaths.size());
			ArrayList<Integer> objectsBug = new ArrayList<Integer>();
			ArrayList<Integer> objectsComponent = new ArrayList<Integer>(); 
			
			ArrayList<PrunedData> tempRedundantList = redundantPaths.get(key);
			for(PrunedData data : tempRedundantList){
				if(data.type == 1){
					objectsBug.add(data.index);
				}
				else if(data.type == 2){ 
					objectsComponent.add(data.index);
				}
			}
			Collections.sort(objectsBug,  Collections.reverseOrder());
			Collections.sort(objectsComponent,  Collections.reverseOrder());
			ArrayList<ArrayList<NetworkLink>> tempBug = new ArrayList<ArrayList<NetworkLink>>();
			ArrayList<ArrayList<NetworkLink>> tempComponent = new ArrayList<ArrayList<NetworkLink>>();
//			System.out.println("arr size "+objectsBug.size());
//			System.out.println("arr size "+objectsBug.size());
			for(int i=0;i<developerMetaPaths.get(key).sameBugMetaPaths.size();i++){
				if(!objectsBug.contains(i)){
				tempBug.add(developerMetaPaths.get(key).sameBugMetaPaths.get(i));
				}
			}
		//	System.out.println("..."+tempBug.size());
			for(int i=0;i<developerMetaPaths.get(key).sameComponentMetaPaths.size();i++){
				if(!objectsComponent.contains(i)){
				tempBug.add(developerMetaPaths.get(key).sameComponentMetaPaths.get(i));
				}
			}
			DeveloperMetaPath temp = new DeveloperMetaPath();
			temp.sameBugMetaPaths = tempBug;
			temp.sameComponentMetaPaths = tempComponent;
			developerMetaPaths.replace(key, temp);
			
		}
	}
	public HashMap<String, ArrayList<PrunedData>> identifyRedundantPaths(){

		HashMap<String, ArrayList<PrunedData>> redundantPaths = new HashMap<String, ArrayList<PrunedData>>();
		for(String key : developerMetaPaths.keySet()){	
			ArrayList<ArrayList<NetworkLink>> sameBugPaths = developerMetaPaths.get(key).sameBugMetaPaths;
			ArrayList<ArrayList<NetworkLink>> sameComponentPaths = developerMetaPaths.get(key).sameComponentMetaPaths;

			Set<String> relatedBugDevelopers = new HashSet<String>();
			Set<String> relatedComponentDevelopers = new HashSet<String>();

			ArrayList<ArrayList<NetworkLink>> typeOnePath = new ArrayList<ArrayList<NetworkLink>>();
			ArrayList<ArrayList<NetworkLink>> typeTwoPath = new ArrayList<ArrayList<NetworkLink>>();
			ArrayList<ArrayList<NetworkLink>> typeThreePath = new ArrayList<ArrayList<NetworkLink>>();

			ArrayList<ArrayList<NetworkLink>> typeFourPath = new ArrayList<ArrayList<NetworkLink>>();
			ArrayList<ArrayList<NetworkLink>> typeFivePath = new ArrayList<ArrayList<NetworkLink>>();
			ArrayList<ArrayList<NetworkLink>> typeSixPath = new ArrayList<ArrayList<NetworkLink>>();
			if(sameBugPaths.size()>0){
				for(ArrayList<NetworkLink> path : sameBugPaths){
					Developer d = (Developer) path.get(path.size()-1).secondNode.customNodes;
					if(!isAlredayCheckedBug(key, d.name)){
					relatedBugDevelopers.add(d.name);
					}

					if(path.size()==2) typeOnePath.add(path);
					if(path.size()==3) typeTwoPath.add(path);
					if(path.size()==4) typeThreePath.add(path);
				}

			}
		//	System.out.println("here"+relatedBugDevelopers.toString()+ " "+typeOnePath.size()+" "+ typeTwoPath.size()+" "+typeThreePath.size());
			if(sameComponentPaths.size()>0){
				for(ArrayList<NetworkLink> path : sameComponentPaths){
					Developer d = (Developer) path.get(path.size()-1).secondNode.customNodes;
					if(!isAlredayCheckedCom(key, d.name)){
					relatedComponentDevelopers.add(d.name);
					}

					if(path.size()==4) typeFourPath.add(path);
					if(path.size()==5) typeFivePath.add(path);
					if(path.size()==6) typeSixPath.add(path);
				}

			}
//			System.out.println("here 1"+relatedComponentDevelopers.toString()+ " "+typeFourPath.size()+" "+ typeFivePath.size()+" "+typeSixPath.size());
			
			for(String relatedKey : relatedBugDevelopers){
				System.out.println("abc testing " + relatedKey);
				ArrayList<ArrayList<NetworkLink>> relatedSameBugPaths = developerMetaPaths.get(relatedKey).sameBugMetaPaths;
				System.out.println("found corres "+relatedSameBugPaths.size());
				if(relatedSameBugPaths.size()>0){
					for(int i =0;i<relatedSameBugPaths.size();i++){
						boolean flag = false;
						if(relatedSameBugPaths.get(i).size()==2){
							flag = isReverse(typeOnePath, relatedSameBugPaths.get(i));
	                        //System.out.println(flag);
						}
						else if(relatedSameBugPaths.get(i).size()==3){
							flag = isReverse(typeTwoPath, relatedSameBugPaths.get(i));
						}
						else if(relatedSameBugPaths.get(i).size()==4){
							flag = isReverse(typeThreePath, relatedSameBugPaths.get(i));
						}
						if(flag){
							ArrayList<PrunedData> prunedList = redundantPaths.get(relatedKey);
							if(prunedList == null){
								prunedList = new ArrayList<PrunedData>();
								prunedList.add(new PrunedData(i,1));
								redundantPaths.put(relatedKey, prunedList);
							}
							else{
								prunedList.add(new PrunedData(i,1));
								redundantPaths.replace(relatedKey, prunedList);
							}
						}
					}
				}
			}
			for(String relatedKey : relatedComponentDevelopers){
			//	System.out.println("def testing " + relatedKey);
				ArrayList<ArrayList<NetworkLink>> relatedSameComponentPaths = developerMetaPaths.get(relatedKey).sameComponentMetaPaths;
			//	System.out.println("found corres "+relatedSameComponentPaths.size());
				if(relatedSameComponentPaths.size()>0){
					for(int i =0;i<relatedSameComponentPaths.size();i++){
						boolean flag = false;
						if(relatedSameComponentPaths.get(i).size()==4){
							flag = isReverse(typeFourPath, relatedSameComponentPaths.get(i));
						}
						if(relatedSameComponentPaths.get(i).size()==5){
							flag = isReverse(typeFivePath, relatedSameComponentPaths.get(i));
						}
						if(relatedSameComponentPaths.get(i).size()==6){
							flag = isReverse(typeSixPath, relatedSameComponentPaths.get(i));
						}
						if(flag){
							ArrayList<PrunedData> prunedList = redundantPaths.get(relatedKey);
							if(prunedList == null){
								prunedList = new ArrayList<PrunedData>();
								prunedList.add(new PrunedData(i,2));
								redundantPaths.put(relatedKey, prunedList);
							}
							else{
								prunedList.add(new PrunedData(i,2));
								redundantPaths.replace(relatedKey, prunedList);
							}
						}
					}
				}
			}
		}
		return redundantPaths;
	}


	public  boolean isAlredayCheckedBug(String key, String relatedKey) {
		boolean alreadyChecked = false;
		if(checkedDevelopersBug.size()==0){
			checkedDevelopersBug.add(new CheckedDeveloper(key, relatedKey));
			return alreadyChecked;
		}
		if(checkedDevelopersBug.size()>0){
			for(int i=0;i<checkedDevelopersBug.size();i++){
				if(checkedDevelopersBug.get(i).dev1.equals(key)&&checkedDevelopersBug.get(i).dev2.equals(relatedKey)){
					alreadyChecked = true;
					return alreadyChecked;
				}
				if(checkedDevelopersBug.get(i).dev2.equals(key)&&checkedDevelopersBug.get(i).dev1.equals(relatedKey)){
					alreadyChecked = true;
					return alreadyChecked;
				}
			}
		}
		if(!alreadyChecked){
			checkedDevelopersBug.add(new CheckedDeveloper(key, relatedKey));
		}
		return alreadyChecked;
	}
	public  boolean isAlredayCheckedCom(String key, String relatedKey) {
		boolean alreadyChecked = false;
		if(checkedDevelopersCom.size()==0){
			checkedDevelopersCom.add(new CheckedDeveloper(key, relatedKey));
			return alreadyChecked;
		}
		if(checkedDevelopersCom.size()>0){
			for(int i=0;i<checkedDevelopersCom.size();i++){
				if(checkedDevelopersCom.get(i).dev1.equals(key)&&checkedDevelopersCom.get(i).dev2.equals(relatedKey)){
					alreadyChecked = true;
					return alreadyChecked;
				}
				if(checkedDevelopersCom.get(i).dev2.equals(key)&&checkedDevelopersCom.get(i).dev1.equals(relatedKey)){
					alreadyChecked = true;
					return alreadyChecked;
				}
			}
		}
		if(!alreadyChecked){
			checkedDevelopersCom.add(new CheckedDeveloper(key, relatedKey));
		}
		return alreadyChecked;
	}
	public boolean isReverse(ArrayList<ArrayList<NetworkLink>> paths, ArrayList<NetworkLink> path){
		for(ArrayList<NetworkLink> p : paths){
//			System.out.println(createPathString(p));
//			System.out.println("...."+createPathString(getReversedPath(path)));
			if(createPathString(p).equals(createPathString(getReversedPath(path)))){
				return true;
			}

		}
		return false;
	}
	public ArrayList<NetworkLink> getReversedPath(ArrayList<NetworkLink> path){
		ArrayList<NetworkLink> reversedPath = new ArrayList<NetworkLink>();
		for(int i = path.size()-1; i>=0;i--){
			NetworkLink link = new NetworkLink(path.get(i).secondNode, path.get(i).firstNode, path.get(i).relationType,path.get(i).time);
			reversedPath.add(link);
		}
		return reversedPath;
	}

	public String createPathString(ArrayList<NetworkLink> path){
		String pathString = "";
		for(NetworkLink l : path){
			if(l.firstNode.customNodes instanceof Developer){
				Developer d = (Developer) l.firstNode.customNodes;
				pathString+=(d.name)+ " ";
				if(l.secondNode.customNodes instanceof Bug){
					Bug b =  (Bug) l.secondNode.customNodes;
					pathString+=(b.bugID)+" ";
				}
				if(l.secondNode.customNodes instanceof Comment){
					Comment t =   (Comment) l.secondNode.customNodes;
					pathString+=(t.getCommentID())+" ";
				}	
			}
			else if(l.secondNode.customNodes instanceof Bug){
				Bug b =  (Bug) l.secondNode.customNodes;
				pathString+=(b.bugID)+" ";
			}
			else if(l.secondNode.customNodes instanceof Comment){
				Comment t =   (Comment) l.secondNode.customNodes;
				pathString+=(t.getCommentID())+" ";
			}
			else if(l.secondNode.customNodes instanceof Component){
				Component c =   (Component) l.secondNode.customNodes;
				pathString+=(c.componentName)+" ";
			}
			else if(l.secondNode.customNodes instanceof Developer){
				Developer d = (Developer) l.secondNode.customNodes;
				pathString+=(d.name)+ " ";
			}
			else{
				Product p =   (Product) l.secondNode.customNodes;
				pathString+=(p.productName)+" ";
			}
		}
		return pathString;
	}
	public void print(){
		for(String key:developerMetaPaths.keySet()){
			System.out.println("i m "+ key);
			ArrayList<ArrayList<NetworkLink>> tempList = new ArrayList<ArrayList<NetworkLink>>();
					tempList.addAll(developerMetaPaths.get(key).sameBugMetaPaths);
			System.out.println("with bug"+tempList.size());
			tempList.addAll(developerMetaPaths.get(key).sameComponentMetaPaths);
			System.out.println("with com"+tempList.size());
			for(ArrayList<NetworkLink> pathList : tempList){
				for(int i=0;i<pathList.size();i++){
					if(pathList.get(i).firstNode.customNodes instanceof Developer){
						Developer d = (Developer) pathList.get(i).firstNode.customNodes;
						System.out.print(" "+d.name);
						if(pathList.get(i).secondNode.customNodes instanceof Bug){
							Bug b =  (Bug) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+b.bugID);
						}
						if(pathList.get(i).secondNode.customNodes instanceof Comment){
							Comment t =   (Comment) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+t.commentID);
						}	
					}
					else{
						if(pathList.get(i).secondNode.customNodes instanceof Developer){
							Developer d = (Developer) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+d.name);
						}
						else if(pathList.get(i).secondNode.customNodes instanceof Bug){
							Bug b = (Bug) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+b.bugID);
						}
						else if(pathList.get(i).secondNode.customNodes instanceof Component){
							Component c =  (Component) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+c.componentName);
						}
						else if(pathList.get(i).secondNode.customNodes instanceof Comment){
							Comment t =   (Comment) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+t.commentID);
						}
						else if(pathList.get(i).secondNode.customNodes instanceof Product){
							Product p =   (Product) pathList.get(i).secondNode.customNodes;
							System.out.print(" "+p.productName);
						}
					}
					System.out.println();
				}
			}
		}
	}
}
