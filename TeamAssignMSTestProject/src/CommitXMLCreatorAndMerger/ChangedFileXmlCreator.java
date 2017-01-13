package CommitXMLCreatorAndMerger;

import java.beans.FeatureDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChangedFileXmlCreator {
	String fileLocation;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	ArrayList<ArrayList<String>> changedFileList;
	ArrayList<String> changedFiles;

	public ChangedFileXmlCreator(String _fileLocation){
		fileLocation = _fileLocation;
	}
	public void Initialize() throws ParserConfigurationException{
		changedFileList = new ArrayList<ArrayList<String>>();
		changedFiles =  new ArrayList<String>();
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.newDocument();
	}
	public void textFileReader(){
		System.out.println("go read");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileLocation));
			String line; int lineNumber=0; int firstLoopChecker=0;
			while ((line = reader.readLine()) != null) {
				if( line.contains("<hash>") && line.contains("</hash>") ){
					line=line.replaceAll("<hash>", "");
					line=line.replaceAll("</hash>", "");
					if(firstLoopChecker == 0){firstLoopChecker++; changedFiles.add(line);}
					else{
						changedFileList.add(changedFiles);
						changedFiles=new ArrayList<String>();
						changedFiles.add(line);
					}
				}
				else if(line.equals("")){

				}else{
					String isJava = line.substring(line.length()-5);
					System.out.println(isJava);
					if(isJava.equals(".java")){
						
						String temp = line.substring(0, line.length()-5);
						int index = temp.lastIndexOf('/');
						line = temp.substring(index+1);
						changedFiles.add(line);
					}
				}
			}
			changedFileList.add(changedFiles);
		} catch (Exception e) {
			return;
		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {
				//
			}
		}
		//		System.out.println("file size"+changedFileList.size());
		//		System.out.println("File Content printer");
		//		for(int i=0;i<changedFileList.size();i++){
		//			for(int j=0;j<changedFileList.get(i).size();j++){
		//				System.out.println(changedFileList.get(i).get(j));
		//			}
		//		}
	}
	
	public void NewTextFileReader(){
		System.out.println("go read");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileLocation));
			String line; int lineNumber=0; int firstLoopChecker=0;
			while ((line = reader.readLine()) != null) {
				if( line.contains("<hash>") && line.contains("</hash>") ){
					line=line.replaceAll("<hash>", "");
					line=line.replaceAll("</hash>", "");
					if(firstLoopChecker == 0){firstLoopChecker++; changedFiles.add(line);}
					else{
						changedFileList.add(changedFiles);
						changedFiles=new ArrayList<String>();
						changedFiles.add(line);
					}
				}
				else if(line.equals("")){

				}else{
					//String isJava = line.substring(line.length()-5);
					//System.out.println(isJava);
					//if(isJava.equals(".java")){
						
						//String temp = line.substring(0, line.length()-5);
						int index = line.lastIndexOf('/');
						line = line.substring(index+1);
						changedFiles.add(line);
					//}
				}
			}
			changedFileList.add(changedFiles);
		} catch (Exception e) {
			return;
		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {
				//
			}
		}
		//		System.out.println("file size"+changedFileList.size());
		//		System.out.println("File Content printer");
		//		for(int i=0;i<changedFileList.size();i++){
		//			for(int j=0;j<changedFileList.get(i).size();j++){
		//				System.out.println(changedFileList.get(i).get(j));
		//			}
		//		}
	}

	public void xmlFileCreator(){
		System.out.println("in creator");
		System.out.println("file size"+changedFileList.size());
		Element go = doc.createElement("go");
		doc.appendChild(go); 
		for(int i=0;i<changedFileList.size();i++){
			Element files = doc.createElement("Files");
			go.appendChild(files); 
			int flag=0;
			for(int j=0;j<changedFileList.get(i).size();j++){
				if(flag==0){
					Attr hash = doc.createAttribute("Hash");
					hash.setValue(changedFileList.get(i).get(j));
					files.setAttributeNode(hash);
					flag++;
					//System.out.println(changedFileList.get(i).get(j));
				}
				else{
					Element filePath = doc.createElement("FilePath");
					filePath.appendChild(doc.createTextNode(changedFileList.get(i).get(j)));
					files.appendChild(filePath);
					//System.out.println(changedFileList.get(i).get(j));
				}
			}
		}
	}
	public void xmlFileWriter(String path) throws TransformerException{
		System.out.println("in writer!");
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		System.out.println("File saved!");
	}

}
