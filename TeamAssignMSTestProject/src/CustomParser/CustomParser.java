package CustomParser;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.chrono.EthiopicChronology;


public class CustomParser {

	String fullContent; 
	String classBody;
	ArrayList<Character> braces = new ArrayList<Character>();
	ArrayList<String> tempMethodLines; 
	ArrayList<String> parameterLines; 
	ArrayList<String> methodLines; 
	String line;
	ArrayList<String> methods; 
	ArrayList<String> parameters;
	ArrayList<String> commentedAttributeLines,temps,tempsList,attributeLines,attributes;
	int count;
	ArrayList<LineBlock> blocks;

	public CustomParser(File file){
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String s;
			while ((s=fileReader.readLine())!=null) {
				sb.append(s);
				sb.append('\n'); //if you want the newline
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		fullContent = sb.toString();
		braces = new ArrayList<Character>();
		tempMethodLines = new ArrayList<String>();
		methodLines = new ArrayList<String>();
		parameterLines = new ArrayList<String>();
		line = "";
		methods = new ArrayList<String>();
		parameters = new ArrayList<String>();
		commentedAttributeLines = new ArrayList<String>();temps = new ArrayList<String>();tempsList = new ArrayList<String>();
		attributeLines = new ArrayList<String>();
		attributes = new ArrayList<String>();
		count=0;
		blocks = new ArrayList<LineBlock>();
	}

	public String getClassName(){
		int pub = fullContent.indexOf("public class ");
		int pri = fullContent.indexOf("private class ");
		int pro = fullContent.indexOf("protected class ");
		int abs = fullContent.indexOf("abstract class ");
		int fin = fullContent.indexOf("final class ");
		int sta = fullContent.indexOf("static class ");
		
		
		if(pub!=0){fullContent = fullContent.substring(pub);}
		else if(pri!=0){fullContent = fullContent.substring(pri);}
		else if(pro!=0){fullContent = fullContent.substring(pro);}
		else if(abs!=0){fullContent = fullContent.substring(abs);}
		else if(fin!=0){fullContent = fullContent.substring(fin);}
		else if(sta!=0){fullContent = fullContent.substring(sta);}
	
		String[] packagePart = fullContent.split("\\{", 2);
		classBody = fullContent.substring(fullContent.indexOf('{')+1, fullContent.lastIndexOf('}')-1);
		//System.out.println(packagePart[0]);	
		//System.out.println("*********************************************");
		//System.out.println(classBody);	
		//String classDeclaration = packagePart[0].substring(packagePart[0].lastIndexOf('\n'));
		//System.out.println(classDeclaration);
		String[] temps =  packagePart[0].split(" ");
		String className = temps[temps.length-1];
		System.out.println(className);
		return className;
	}
	public ArrayList<String> getMethodName(){
		try (BufferedReader reader = new BufferedReader(new StringReader(classBody))) {
			String line;
			while( (line = reader.readLine())!=null){  
				//System.out.println("......................."+line);
				commentedAttributeLines.add(line); 
				if(line.contains("{") ){
					braces.add('{');
					tempMethodLines.add(line);
				}
				if(line.contains("}")){
					braces.add('}');
					tempMethodLines.add(line);     		   
				}
				//            	   System.out.println("gooo     "+braces);
				//            	   System.out.println("doooo      "+methodLines);
				checkStatus();   	   
			}
		} catch (IOException exc) {
			System.out.println("method parser error");
		}
		for(String m: methodLines){
			String  methodDeclaration = m.substring(0, m.indexOf('('));
			String[] temps = methodDeclaration.split(" ");
			String methodName = temps[temps.length-1];
			methods.add(methodName);
		}
		System.out.println("****************going to print methods");
		System.out.println(methods);
		return methods;
	}
	public ArrayList<String> getParameterName(){
		for(String methodLine: methodLines){
			String  methodDeclaration = methodLine.substring(0, methodLine.indexOf('('));
			String[] temps = methodDeclaration.split(" ");
			String methodName = temps[temps.length-1];
			int index = methodLine.indexOf(methodName);
			int updateIndex = index+methodName.length()+1;	
			String att="";
			while(methodLine.charAt(updateIndex)!=')'){
				att+= methodLine.charAt(updateIndex);
				updateIndex++;
			}
			parameterLines.add(att);	
		}
		for(String parameterLine:parameterLines){
			if(parameterLine.length()>0){
				String[] tempParas = null;
				ArrayList<String> paras = new ArrayList<String>();
				if(parameterLine.contains(",")){
					tempParas = parameterLine.split(",");
					paras = new ArrayList(Arrays.asList(tempParas));
				}
				else{
					paras.add(parameterLine);
					//System.out.println(attrs);
				}
				for(String p:paras){
					String[] parameterName = p.split(" ");
					parameters.add(parameterName[parameterName.length-1]);
				}		
			}
		}
		System.out.println("****************going to print paremeters");
		System.out.println(parameters);
		return parameters;
	}
	public void checkStatus() {
		for(int i = 0;i<braces.size();i++){
			if(braces.get(i).equals('}')){
				if(i-1!=0){
					braces.remove(i);
					braces.remove(i-1);
					tempMethodLines.remove(i);
					tempMethodLines.remove(i-1);
					break;
				}
				else{
					methodLines.add(tempMethodLines.get(i-1));
					temps.add(tempMethodLines.get(i-1));temps.add(tempMethodLines.get(i));
					braces = new ArrayList<Character>();
					tempMethodLines = new ArrayList<String>();
					break;
				}
			}
		}	
	}
	public ArrayList<String> getAttributeName(){
		deleteLines();
		//System.out.println("ggggg"+commentedAttributeLines.size());
		ArrayList<String> indexes = new ArrayList<String>();
		for(String l:indexes){
			commentedAttributeLines.remove(l);
		}
		processAttributeLine();
		//System.out.println(attributeLines);
		getAttributes();
		System.out.println("****************going to print attributes");
		System.out.println(attributes);
		return attributes;
	}
	public void getAttributes() {
		for(String s: attributeLines){
			String[] splits = s.split(" ");
			if(splits[splits.length-1].contains("=")){
				attributes.add(splits[splits.length-1].split("=")[0]);
			}
			else{
				attributes.add(splits[splits.length-1]);
			}
		}
	}

	public void processAttributeLine(){
		for(String s:commentedAttributeLines){
			s = s.trim();
			if(s.startsWith("//") ||s.startsWith("/*")||s.startsWith("*")){
				
			}
			else{
				if(s.length()>0){
					attributeLines.add(s);
				}
			}
		}
	}
	public void deleteLines(){
		int count = 0;
		for(int i=0;i<temps.size();i=i+2){
			tempsList = new ArrayList<String>();
			//System.out.println("8888 "+count);
			for(int j=count;j<commentedAttributeLines.size();j++){
				tempsList.add(commentedAttributeLines.get(j));
			}
			//System.out.println(tempsList);
			String start = temps.get(i);
			String end = temps.get(i+1);
			//System.out.println(start+"3333333"+end);
			blocks.add(new LineBlock(tempsList.indexOf(start)+count, tempsList.indexOf(end)+count));
			//System.out.println(tempsList.indexOf(start)+"......."+tempsList.indexOf(end));
			count=count + tempsList.indexOf(end) + 1;
			//System.out.println("99999 "+count);
		}
		for(int i=blocks.size()-1;i>=0;i--){
			if(blocks.get(i).end==blocks.get(i).start){
				commentedAttributeLines.remove(blocks.get(i).start);
			}
			else{
				for(int k=blocks.get(i).end;k>=blocks.get(i).start;k--){
					commentedAttributeLines.remove(k);
				}
			}
		}
	}
}
