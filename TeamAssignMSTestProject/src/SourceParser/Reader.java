package SourceParser;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.util.ClasspathResourceLoader;

public class Reader {
	private BufferedReader reader;
	File[] allFileList;
	public ArrayList<File> javaFileList = new ArrayList<File>();
	ArrayList<String> listOfClasses = new ArrayList<String>();
	ArrayList<ClassInfo> classes;
	public static int flagy=0;
	public JavaSourceParser javaSourceParser;

	public Reader(String location) throws IOException {
		System.out.println("loc"+ location);
		
		//System.out.println(folder.getName());
		try{
			File folder = new File(location);
			File[] listOfFiles = folder.listFiles();
			//System.out.println("number of files"+listOfFiles.length);
			getAllfiles(listOfFiles);
		}
		
		catch(Exception e){
			System.out.println(e.toString());
		}
		

		for (int i = 0; i < javaFileList.size(); i++) {
			String fileName = javaFileList.get(i).getName();
			listOfClasses.add(fileName.substring(0, fileName.length() - 5));
			// readFile(javaFileList.get(i));
		}
	}

	public void getAllfiles(File[] listOfFiles) {
		//System.out.println(" vfcjbvfk");
		//System.out.println("number of files"+listOfFiles.length);
		for (File file : listOfFiles) {
			String fileName = file.getName();
			
			boolean isJavaCode = fileName.substring(
					fileName.lastIndexOf(".") + 1, fileName.length()).equals(
					"java");
			if (file.isFile()) {
				if (isJavaCode) {
					//System.out.println("........."+fileName);
					javaFileList.add(file);
				}
			} else {
				getAllfiles(file.listFiles());
			}
		}
	}

	public ArrayList<String> getClasses() {
		return listOfClasses;
	}

	public ArrayList<File> getClassFiles() {
		return javaFileList;
	}

	public ArrayList<ClassInfo> getClassMethods() {
		return classes;
	}

	public void readFile(File file) throws IOException {
		reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		System.out.println();
	}

	public void extractClassMethods() throws Exception {
		javaSourceParser = new JavaSourceParser();
		MethodVisitor visitor = new MethodVisitor();
		CompilationUnit compilationUnit=null;
		AttributeVisitor attributeVisitor = new AttributeVisitor(); 

		classes = new ArrayList<ClassInfo>();

		for (int i = 0; i < javaFileList.size(); i++) {
				visitor.initializeMethodList();
			attributeVisitor.initializeAttributeList();
			ClassInfo classinfo = new ClassInfo();
			classinfo.classNo = i;
			classinfo.className = javaFileList.get(i).getName()
					.substring(0, javaFileList.get(i).getName().length() - 5);
			classinfo.classFullName = javaFileList.get(i).getName();
try{
	compilationUnit = javaSourceParser.getCompilationunit(javaFileList
			.get(i));
}catch(Exception e){
	classes.add(classinfo);
	continue;}
			
			int flag_m,flag_a=0;
			
			System.out.println("bb  "+javaFileList.get(i).getName());
			List<BodyDeclaration> members = null;
			List<TypeDeclaration> types;
			if(compilationUnit != null){
				types = compilationUnit.getTypes();
			}
			else{
				
				continue;
			}
		//	List<TypeDeclaration> types = compilationUnit.getTypes();
			if(types!=null){
				for (TypeDeclaration type : types) {
		            members = type.getMembers();
		        }
			}
	        
	        if(members!=null){
		        for(int k = 0;k<members.size();k++){
		        	if(members.get(k) instanceof AnnotationDeclaration){
		        						
		        						System.out.println("annoted............");
		        					}
		        		        else if(members.get(k) instanceof MethodDeclaration){
		        		        	visitor.visit((MethodDeclaration)members.get(k),null);
		        
		        					}
		        					else if(members.get(k) instanceof FieldDeclaration){
		        						attributeVisitor.visit((FieldDeclaration)members.get(k), null);
		        					}
		        					
		        				}
	        }

			
			//visitor.visit(compilationUnit, null);
			//attributeVisitor.visit(compilationUnit,null);
			classinfo.methodDetails = visitor.getMethodList();
			classinfo.attributeDetails = attributeVisitor.getAttributeList();
			classinfo.constructor = javaSourceParser.constructor;
			classinfo.packageName = javaSourceParser.packageDeclaration;
			classinfo.imports = javaSourceParser.importDeclarations;
			classinfo.isInterface = javaSourceParser.isInterface;
			classinfo.isAbstractClass=javaSourceParser.isAbstractClass;
			classes.add(classinfo);
			flagy++;
		
		}
	}

	public void printClassMethods() {
		System.out.println("***Syntax Start***");
		for (int i = 0; i < classes.size(); i++) {
			
			System.out.print("Class ");
			System.out.print(classes.get(i).className);
			if (classes.get(i).constructor.parameters != null)
				System.out.print(" " + classes.get(i).constructor.parameters);
			System.out.println();
			if(classes.get(i).attributeDetails.size()>0)
				System.out.println("\tFields:");
			for (int j = 0; j < classes.get(i).attributeDetails.size(); j++) {
				System.out.println("\t\t"
						+ classes.get(i).attributeDetails.get(j).name + " "+
						classes.get(i).attributeDetails.get(j).modifier+ " "+
						classes.get(i).attributeDetails.get(j).type);
			}
			if (classes.get(i).methodDetails.size() > 0)
				System.out.println("\tMethods:");
//			else System.out.println("\tNo Methods");
			for (int j = 0; j < classes.get(i).methodDetails.size(); j++) {
				System.out.println("\t\t"
						+ classes.get(i).methodDetails.get(j).methodName);
				if(classes.get(i).methodDetails.get(j).parameters!=null){
					System.out.println("\t\t\tParameters:");
					for(int k=0;k<classes.get(i).methodDetails.get(j).parameters.size();k++){
						System.out.println("\t\t\t\t"
								+ classes.get(i).methodDetails.get(j).parameters.get(k).getId().getName());
					}
				}
			}
			
		
		}
		System.out.println("***Syntax End***");
	}
}
