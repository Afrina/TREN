package pathGeneration;

import java.util.ArrayList;

import network.NetworkLink;

public class DeveloperMetaPath {
	public ArrayList<ArrayList<NetworkLink>> sameBugMetaPaths;
	public ArrayList<ArrayList<NetworkLink>> sameComponentMetaPaths;
	public DeveloperMetaPath(){
		sameBugMetaPaths = new ArrayList<ArrayList<NetworkLink>>();
		sameComponentMetaPaths = new ArrayList<ArrayList<NetworkLink>>();
	}
	
}
