package CommitParser;

import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DeveloperListSorting implements Comparator<DeveloperCommitInfo>{

	@Override
	public int compare(DeveloperCommitInfo developer1, DeveloperCommitInfo developer2) {
		// TODO Auto-generated method stub
		DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z");
		//DateTime dt = formatter.parseDateTime("Sat, 19 May 2001 21:35:12 +0000");
		return (formatter.parseDateTime(developer1.commitTime).isAfter(formatter.parseDateTime(developer2.commitTime)) ? -1 :
			(formatter.parseDateTime(developer1.commitTime)==formatter.parseDateTime(developer2.commitTime) ? 0 : 1));
	}

}
