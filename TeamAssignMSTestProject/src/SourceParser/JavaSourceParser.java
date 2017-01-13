package SourceParser;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import CustomParser.CustomParser;
import FileChecker.FileExchange;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.TypeDeclaration;

public class JavaSourceParser {

	Method constructor;
	PackageDeclaration packageDeclaration;
	List<ImportDeclaration> importDeclarations;
	boolean isInterface;
	boolean isAbstractClass;
	public static int flagx=0;
	FileExchange exchanger = new FileExchange();

	public CompilationUnit getCompilationunit(File fileName) throws Exception {
		InputStream in = null;
		CompilationUnit cu = null;
		constructor = new Method();
		try {
			exchanger.replace(fileName.getAbsolutePath());
			in = new FileInputStream(fileName);
			InputStreamReader r = new InputStreamReader(in);
			cu = JavaParser.parse(in,r.getEncoding());
			packageDeclaration = cu.getPackage();
			importDeclarations = cu.getImports();
			List<TypeDeclaration> typeDeclarations = cu.getTypes();
			if(typeDeclarations!=null){
				for (TypeDeclaration typeDec : typeDeclarations) {
					List<BodyDeclaration> members = typeDec.getMembers();
					if (typeDec instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) typeDec;
						isInterface = c.isInterface();
						if (c.getModifiers() == 1025) {
							isAbstractClass = true;
						} else
							isAbstractClass = false;
					}
					if (members != null) {
						for (BodyDeclaration member : members) {
							if (member instanceof ConstructorDeclaration) {
								ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) member;
								constructor.annotation = constructorDeclaration
										.getAnnotations();
								constructor.endingLine = constructorDeclaration
										.getEndLine();
								constructor.methodBody = constructorDeclaration
										.getBlock();
								constructor.methodName = constructorDeclaration
										.getName();
								constructor.parameters = constructorDeclaration
										.getParameters();
								constructor.startingLine = constructorDeclaration
										.getBeginLine();
								constructor.throwed = constructorDeclaration
										.getThrows();
							}
						}
					}
				}
			}
			
		} catch (Exception x) {
			CustomParser customParser = new CustomParser(fileName);
			System.out.println(fileName.getAbsolutePath());
			customParser.getClassName();
			customParser.getMethodName();
			customParser.getParameterName();
			customParser.getAttributeName();
			flagx++;
			System.out.println(" eeerrrrrrrrrrrrrrrrrrr");
		} finally {
			in.close();
		}
		return cu;
	}
}
