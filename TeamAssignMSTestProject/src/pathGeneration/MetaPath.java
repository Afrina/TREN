package pathGeneration;

import java.util.ArrayList;

import network.NetworkLink;

public class MetaPath {
	public ArrayList<NetworkLink> path;
	public int metaPathType;
	public MetaPath(ArrayList<NetworkLink> _path, int _metaPathType){
		path = _path;
		metaPathType = _metaPathType;
	}
}
