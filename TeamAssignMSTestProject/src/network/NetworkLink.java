package network;



/**
 * Created by peacefrog on 11/20/16.
 * Time 12:45 PM
 */
public class NetworkLink {

	public NetworkNode firstNode;
	public NetworkNode secondNode;
	public int relationType;
	public String time;

	public NetworkLink(NetworkNode firstNode, NetworkNode secondNode, int relationType, String time) {
		this.firstNode = firstNode;
		this.secondNode = secondNode;
		this.relationType = relationType;
		this.time = time;
	}
}
