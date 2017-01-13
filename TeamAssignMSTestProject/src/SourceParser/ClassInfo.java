package SourceParser;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
	public ArrayList<Method> methodDetails = new ArrayList<Method>();
	public ArrayList<Attribute> attributeDetails = new ArrayList<Attribute>();
	public Method constructor;
	public String className;
	public int classNo;
	public PackageDeclaration packageName;
	public List<ImportDeclaration> imports;
	public boolean isInterface;
	public boolean isAbstractClass;
	public String classFullName;

}
