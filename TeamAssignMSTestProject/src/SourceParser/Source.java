package SourceParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Source {

	String sourceLocation;
	public Reader reader;
	Syntax syntax;

	public Source(String location) throws IOException {
		sourceLocation = location;
		reader = new Reader(location);
		syntax = new Syntax();

	}

	public ArrayList<String> getClasses() {
		return reader.getClasses();
	}

	public ArrayList<File> getClassFiles() {
		return reader.getClassFiles();
	}

	public Syntax getSyntax() throws Exception {
		try{reader.extractClassMethods();}
		catch(Exception e){
			System.out.println("looke errrrrrrrrrrrrrrrrrrr");
		}
		//reader.printClassMethods();
		syntax.classes = reader.getClassMethods();
		return syntax;
		
	}

}
