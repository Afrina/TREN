package network;



import java.util.ArrayList;

/**
 * Created by peacefrog on 11/18/16.
 * Time 11:45 PM
 */
public class Bug {
	public String bugID;
	public int developerRepositoryIndex;
	public String shortDecription;
	public String longDecription;
	public ArrayList<Comment> comments;
	public ArrayList<Integer> connectedDevelopersRepositoryIndex;

	public Bug(String bugID, String shortDecription, String longDecription, ArrayList<Comment> comments) {
		this.bugID = bugID;
		this.shortDecription = shortDecription;
		this.longDecription = longDecription;
		this.comments = comments;
	}
	public Bug(String bugID){
		this.bugID = bugID;
	}
}
