package CommitXMLCreatorAndMerger;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CommitFileMerger {
	String authorFileLocation, changeFileLocation;
	Document authorDoc, changeDoc;
	DocumentBuilderFactory dbAuthorFactory, dbChangeFactory;
	DocumentBuilder dbAuthorBuilder, dbChangeBuilder;
	Element authorRoot, changeRoot;


	public CommitFileMerger(String _authorFileLocation, String _changeFileLocation){
		authorFileLocation = _authorFileLocation;
		changeFileLocation = _changeFileLocation;
	}
	public void Initialize() throws ParserConfigurationException, SAXException, IOException{
		dbAuthorFactory = DocumentBuilderFactory.newInstance();
		dbAuthorBuilder = dbAuthorFactory.newDocumentBuilder();
		authorDoc = dbAuthorBuilder.parse(new File(authorFileLocation));
		authorDoc.getDocumentElement().normalize();
		authorRoot = authorDoc.getDocumentElement();

		dbChangeFactory = DocumentBuilderFactory.newInstance();
		dbChangeBuilder = dbChangeFactory.newDocumentBuilder();
		changeDoc = dbChangeBuilder.parse(new File(changeFileLocation));
		changeDoc.getDocumentElement().normalize();
		changeRoot = changeDoc.getDocumentElement();
	}

	public void mergeContents(){

		NodeList fileList = changeRoot.getElementsByTagName("Files");
		System.out.println("m kfc"+fileList.getLength());
		NodeList commitList = authorRoot.getElementsByTagName("commit");
		System.out.println("cn j"+commitList.getLength());



		for(int i=0;i<fileList.getLength();i++){
			//System.out.println("doing with"+ i);
			Element fileElement = (Element) fileList.item(i);
			String changeHash = fileElement.getAttribute("Hash");
			//Element e = (Element) commitList.item(i);
			//	for(int j=0;j<commitList.getLength();j++){
			Element commitElement = (Element) commitList.item(i);

			if(commitElement.getElementsByTagName("hash").item(0).getTextContent().equals(changeHash)){


				NodeList filePaths = fileElement.getElementsByTagName("FilePath");
				if(filePaths.getLength()>0){
					Element changedFiles = authorDoc.createElement("changedFiles");
					commitElement.appendChild(changedFiles);

					for(int k=0;k<filePaths.getLength();k++){

						Element filePath = authorDoc.createElement("filePath");
						filePath.appendChild(authorDoc.createTextNode(filePaths.item(k).getTextContent()));
						changedFiles.appendChild(filePath);

					}
				}
				//	break;
			}
			//}
		}
	}

	public void updateContents() throws TransformerException{
		System.out.println(" in writer for File save!");
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(authorDoc);
		StreamResult result = new StreamResult(new File("C:\\Users\\A-SE\\Documents\\MS_Thesis_Code\\ForRunTest\\commits\\logNew1.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		System.out.println("merge File saved!");
	}
}
