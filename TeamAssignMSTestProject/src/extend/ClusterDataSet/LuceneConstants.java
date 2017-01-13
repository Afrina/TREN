package extend.ClusterDataSet;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.util.CharArraySet;

public class LuceneConstants {
	public static final String CommitContent = "commitContent";
	public static final String ReportContent = "reportContent";
	public static final String CONTENT = "content";
	public static final String Name = "name";
	public static final String Developer = "developer";
	public static final String SUMMARY = "summary";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String TIME = "time";

	public static final String INDEX = "index";
	public static final String BUGINDEX = "C:\\Users\\A-SE\\workspace\\MSTestProject\\index";
	
	public static final String BUGREPORTINDEX = "bugReportIndex";
	public static final String TESTINDEX = "testIndex";
	public static final String LOADINDEX = "loadIndex";

	
	public static final CharArraySet CUSTOM_STOP_WORDS_SET;
	
		static {
			final List<String> stopWords =  Arrays.asList(
					"xml", "java", "a", "about", "above", "after", "again",
					"against", "all", "am", "an", "and",
					"any", "are", "aren't", "as", "at",
					"be", "because", "been", "before", "being",
					"below", "between", "both", "but", "by",
					"can't", "cannot", "could", "couldn't", "did",
					"didn't", "do", "does", "doesn't", "doing",
					"don't", "down", "during", "each", "few",
					"for", "from", "further", "had", "hadn't",
					"has", "hasn't", "have", "haven't", "having",
					"he", "he'd", "he'll", "he's", "her",
					"here", "here's", "hers", "herself", "him",
					"himself", "his", "how", "how's", "i",
					"i'd", "i'll", "i'm", "i've", "if",
					"in", "into", "is", "isn't", "it",
					"it's", "its", "itself", "let's", "me",
					"more", "most", "mustn't", "my", "myself",
					"no", "nor", "not", "of", "off",
					"on", "once", "only", "or", "other",
					"ought", "our", "ours 	ourselves", "out", "over",
					"own", "same", "shan't", "she", "she'd",
					"she'll", "she's", "should", "shouldn't", "so",
					"some", "such", "than", "that", "that's",
					"the", "their", "theirs", "them", "themselves",
					"then", "there", "there's", "these", "they",
					"they'd", "they'll", "they're", "they've", "this","they'r",
					"those", "through", "to", "too", "under",
					"until", "up", "very", "was", "wasn't",
					"we", "we'd", "we'll", "we're", "we've","we'r",
					"were", "weren't", "what", "what's", "when",
					"when's", "where", "where's", "which", "while",
					"who", "who's", "whom", "why", "why's",
					"with", "won't", "would", "wouldn't", "you",
					"you'd", "you'll", "you're", "you've", "your",
					"yours", "yourself", "yourselves", "also","\"","think",
					"\r","\n","(",")","[","]","{","}","."," ",","
			);
			final CharArraySet stopSet = new CharArraySet(stopWords, false);
			CUSTOM_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
}
}
