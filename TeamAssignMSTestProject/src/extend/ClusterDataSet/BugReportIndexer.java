package extend.ClusterDataSet;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
public class BugReportIndexer {
	


	/**
	 * Created by peacefrog on 7/2/16.
	 * 4:12 PM
	 */


		private List< BugReport> bugReports ;
		private Stemmer stemmer ;
		public BugReportIndexer(List<BugReport> bugReports) {
			this.bugReports = bugReports;
			stemmer = new Stemmer();
		}

		public BugReportIndexer() {
			stemmer = new Stemmer();
		}

		public void createIndex(){
			index(true);
		}

		public void index(boolean isNewIndex){
			String indexPath = LuceneConstants.BUGREPORTINDEX;
			boolean create = isNewIndex;

			Date start = new Date();
			try {
				System.out.println("Indexing to directory '" + indexPath + "'...");

				Directory dir = FSDirectory.open(Paths.get(indexPath));
				Analyzer analyzer = new StandardAnalyzer(LuceneConstants.CUSTOM_STOP_WORDS_SET);
				IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

				if (create) {
					// Create a new index in the directory, removing any
					// previously indexed documents:
					iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
				} else {
					// Add new documents to an existing index:
					iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
				}

				// Optional: for better indexing performance, if you
				// are indexing many documents, increase the RAM
				// buffer.  But if you do this, increase the max heap
				// size to the JVM (eg add -Xmx512m or -Xmx1g):
				//
				// iwc.setRAMBufferSizeMB(256.0);

				iwc.setRAMBufferSizeMB(1024.0);

				IndexWriter writer = new IndexWriter(dir, iwc);


				indexDocs(writer, bugReports);

				// NOTE: if you want to maximize search performance,
				// you can optionally call forceMerge here.  This can be
				// a terribly costly operation, so generally it's only
				// worth it when your index is relatively static (ie
				// you're done adding documents to it):
				//
				// writer.forceMerge(1);

				writer.close();

				Date end = new Date();
				System.out.println(end.getTime() - start.getTime() + " total milliseconds");

			} catch (IOException e) {
				System.out.println(" caught a " + e.getClass() +
						"\n with message: " + e.getMessage());
			}
		}

		static void indexDocs(final IndexWriter writer, List<BugReport> reports) throws IOException {

			for (BugReport report: reports) {
				indexDoc(writer , report);
			}

		}
		 public static final FieldType TYPE_STORED = new FieldType();

		    static {
		        TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		        TYPE_STORED.setTokenized(true);
		        TYPE_STORED.setStored(true);
		        TYPE_STORED.setStoreTermVectors(true);
		        TYPE_STORED.setStoreTermVectorPositions(true);
		        TYPE_STORED.freeze();
		    }

		/** Indexes a single document */
		static void indexDoc(IndexWriter writer, BugReport report) throws IOException {
			// make a new, empty document
			Document doc = new Document();
			Field pathField = new StringField(LuceneConstants.Developer, report.getDeveloper(), Field.Store.YES);
			doc.add(pathField);
			Field pathField1 = new StringField(LuceneConstants.ID, report.getId(), Field.Store.YES);
			doc.add(pathField1);
			doc.add(new Field(LuceneConstants.SUMMARY, Objects.equals(report.getSummary(), "") ? "null" : report.getSummary(), TYPE_STORED));
			doc.add(new Field(LuceneConstants.DESCRIPTION, Objects.equals(report.getDescription(), "") ?"null": report.getDescription() , TYPE_STORED));
			doc.add(new Field(LuceneConstants.CONTENT, report.getSummary()+" "+report.getDescription() , TYPE_STORED));
			doc.add(new Field(LuceneConstants.TIME, report.getReportingTime() , TYPE_STORED));

			if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
				// New index, so we just add the document (no old document can be there):
				System.out.println("adding " + report.getSummary());
				writer.addDocument(doc);
			} else {
				// Existing index (an old copy of this document may have been indexed) so
				// we use updateDocument instead to replace the old one matching the exact
				// path, if present:
				System.out.println("updating " + report.getSummary());
				writer.updateDocument(new Term("summary", report.getSummary()), doc);
			}
		}

		public void createTestIndex() throws IOException {
			String indexPath = LuceneConstants.TESTINDEX;
			boolean create = true;
			Date start = new Date();
			try {
				System.out.println("Indexing test directory '" + indexPath + "'...");

				Directory dir = FSDirectory.open(Paths.get(indexPath));
				Analyzer analyzer = new StandardAnalyzer(LuceneConstants.CUSTOM_STOP_WORDS_SET);
				IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
				if (create) {
					iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
				} else {
					iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
				}

				//iwc.setRAMBufferSizeMB(1024.0);
				IndexWriter writer = new IndexWriter(dir, iwc);
				indexDocs(writer, bugReports);

				writer.close();

				Date end = new Date();
				System.out.println(end.getTime() - start.getTime() + " total milliseconds for test");

			} catch (IOException e) {
				System.out.println(" caught a " + e.getClass() +
						"\n with message: " + e.getMessage());
			}
		}
		public void createLoadIndex() throws IOException {
			String indexPath = LuceneConstants.LOADINDEX;
			boolean create = true;
			Date start = new Date();
			try {
				System.out.println("Indexing test directory '" + indexPath + "'...");

				Directory dir = FSDirectory.open(Paths.get(indexPath));
				Analyzer analyzer = new StandardAnalyzer(LuceneConstants.CUSTOM_STOP_WORDS_SET);
				IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
				if (create) {
					iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
				} else {
					iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
				}

				//iwc.setRAMBufferSizeMB(1024.0);
				IndexWriter writer = new IndexWriter(dir, iwc);
				indexDocs(writer, bugReports);

				writer.close();

				Date end = new Date();
				System.out.println(end.getTime() - start.getTime() + " total milliseconds for test");

			} catch (IOException e) {
				System.out.println(" caught a " + e.getClass() +
						"\n with message: " + e.getMessage());
			}
		}

//		void addDocument(IndexWriter writer, String summary , String  desc) throws IOException {
//			Document doc = new Document();
//			doc.add(new Field(LuceneConstants.SUMMARY, summary,TYPE_STORED));
//			doc.add(new Field(LuceneConstants.DESCRIPTION, desc,TYPE_STORED));
//
//			writer.addDocument(doc);
//		}
	


}
