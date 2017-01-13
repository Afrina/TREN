package FileChecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileExchange {
	
		   

		   public void replace(String filename) {
		      String oldFileName = filename;
		      String tmpFileName = "temp.java";

		      BufferedReader br = null;
		      BufferedWriter bw = null;
		      try {
		         br = new BufferedReader(new FileReader(oldFileName));
		         bw = new BufferedWriter(new FileWriter(tmpFileName));
		         String line;
		         while ((line = br.readLine()) != null) {
		        	 String clean = line.replaceAll("[^\\n\\r\\t\\p{Print}]", "");
		        	 clean = clean.replaceAll( "[\\p{InHigh_Surrogates}]", "");
		            bw.write(clean);
		            bw.newLine();
		         }
		      } catch (Exception e) {
		         return;
		      } finally {
		         try {
		            if(br != null)
		               br.close();
		         } catch (IOException e) {
		            //
		         }
		         try {
		            if(bw != null)
		               bw.close();
		         } catch (IOException e) {
		            //
		         }
		      }
		      // Once everything is complete, delete old file..
		      File oldFile = new File(oldFileName);
		      oldFile.delete();

		      // And rename tmp file's name to old file name
		      File newFile = new File(tmpFileName);
		      newFile.renameTo(oldFile);

		   }
		
}
