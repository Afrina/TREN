package SourceParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestCreationProperties {

	public boolean compareClass;
	public boolean compareMethod;
	public String projectPath;
	public String xmlPath;
	public String testPackage;
	private String propertyPath;
	public String onlyFileNameTxt, onlyFileNameXml, onlyAuthorNameXml, mergedXml, LDADataCreator,workLoad, LDADataNew;
	private BufferedReader reader;
	public String networkXML1, networkXML2, testData, LDATestDataCreator, LDALoadDataCreator;
	
	public String testBugsPath;

	public TestCreationProperties(String path) {
		propertyPath = path;
	}

	public void readProperties() throws IOException {
		File propertyFile = new File(propertyPath);
		reader = new BufferedReader(new FileReader(propertyFile));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.contains("projectPath")) {
				projectPath = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			} else if (line.contains("xmlPath")) {
				xmlPath = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			} else if (line.contains("testPackage")) {
				testPackage = line.substring(line.indexOf('=') + 1).trim();
			} else if (line.contains("compareClasses")) {
				compareClass = line.substring(line.indexOf('=') + 1).trim()
						.matches("true") ? true : false;
			} else if (line.contains("compareMethods")) {
				compareMethod = line.substring(line.indexOf('=') + 1).trim()
						.matches("true") ? true : false;
			}
			else if(line.contains("testBugs")){
				testBugsPath = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("onlyFileNameTxt")){
				onlyFileNameTxt = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("onlyFileNameXml")){
				onlyFileNameXml = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("onlyAuthorNameXml")){
				onlyAuthorNameXml = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("mergedXml")){
				mergedXml = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("LDADataCreator")){
				LDADataCreator = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("LDADataNew")){
				LDADataNew = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("networkXML1")){
				networkXML1 = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("networkXML2")){
				networkXML2 = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("testData")){
				testData = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("LDATestDataCreator")){
				LDATestDataCreator = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("LDALoadDataCreator")){
				LDALoadDataCreator = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
			else if(line.contains("workLoad")){
				workLoad = line.substring(line.indexOf('=') + 1).trim().replace("\\", "\\\\");
			}
		}
	}
}
