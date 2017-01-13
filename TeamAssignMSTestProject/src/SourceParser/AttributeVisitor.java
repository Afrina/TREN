package SourceParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class AttributeVisitor extends VoidVisitorAdapter<Object>{

	public ArrayList<Attribute> attributeList;
	public void visit(FieldDeclaration n,Object arg){

		//System.out.println("field");
		Attribute attribute = new Attribute();
		attribute.modifier = n.getModifiers();
		attribute.type = n.getType().toString();
		List <VariableDeclarator> myFields = n.getVariables();
		//System.out.println("size is"+myFields.size());
		attribute.name = n.getVariables().get(0).getId().getName();
		attributeList.add(attribute);
	}

	public ArrayList<Attribute> getAttributeList(){
		return attributeList;

	}
	public void initializeAttributeList() {
		attributeList = new ArrayList<Attribute>();
	}
}

