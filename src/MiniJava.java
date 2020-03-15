import AST.False;
import AST.Program;
import AST.Statement;
import AST.Visitor.ASTVisitor;
import AST.Visitor.Assembly.Assembly;
import AST.Visitor.Assembly.CodeGenVisitor;
import AST.Visitor.PreprocessVisitor;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.Python.Python;
import AST.Visitor.Python.PythonGenVisitor;
import AST.Visitor.Semantics.*;
import AST.Visitor.Visitor;
import Parser.parser;
import Parser.sym;
import Scanner.scanner;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class MiniJava {

    static private boolean DEBUG = false;
    static private String INDENTATION = "   ";

    public static void main(String [] args) {
        if (args.length == 0) {
            System.out.println("No arguments");
            System.exit(1);
        }

        if (args[0].equals("-S")) {
            System.exit(scan(args[1]));
        }
        else if (args[0].equals("-A")) {
            System.exit(parse(args[1], new ASTVisitor(), DEBUG));
        }
        else if (args[0].equals("-P")) {
            System.exit(parse(args[1], new PrettyPrintVisitor(), DEBUG));
        }
        else if (args[0].equals("-T")) {
            System.exit(semanticCheck(args[1], DEBUG));
        }
        else if (args[0].equals("-PY")) {
            System.exit(generatePython(args[1], DEBUG));
        }
        else if (args.length == 1) {
            //System.out.println(args[0]);
            System.exit(generateCode(args[0], DEBUG));
        } else {
            System.out.println("Unexpected args");
            System.exit(1);
        }
    }

    private static int scan(String filePath) {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            scanner s = new scanner(br, sf);
            Symbol t = s.next_token();
            while (t.sym != sym.EOF){
                // print each token that we scan
                System.out.print(s.symbolToString(t) + " ");
                t = s.next_token();
            }
            System.out.print("\nLexical analysis completed");
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " +
                    e.toString());
            // print out a stack dump
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    private static int parse(String filePath, Visitor visitor, boolean debug) {
        try {

            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            Reader in = br;
            scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root;
            // replace p.parse() with p.debug_parse() in next line to see trace of
            // parser shift/reduce actions during parse

            if (debug) {
                root = p.debug_parse();
            }
            else {
                root = p.parse();
            }

            Program program = (Program) root.value;

            program.accept(new PreprocessVisitor());
            program.accept(visitor);


            System.out.print("\nParsing completed");

        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " +
                    e.toString());
            // print out a stack dump
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    private static int semanticCheck(String filePath, boolean debug) {
        try {

            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            Reader in = br;
            scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root;
            // replace p.parse() with p.debug_parse() in next line to see trace of
            // parser shift/reduce actions during parse

            if (debug) {
                root = p.debug_parse();
            }
            else {
                root = p.parse();
            }

            Program program = (Program) root.value;

            program.accept(new PreprocessVisitor());

            GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable();


            Visitor globalSymbolTableVisitor = new GlobalSymbolTableVisitor(globalSymbolTable);
            program.accept(globalSymbolTableVisitor);

            ErrorChecker errorChecker = new ErrorChecker();

            Visitor classSymbolTableVisitor = new ClassSymbolTableVisitor(globalSymbolTable, errorChecker);
            program.accept(classSymbolTableVisitor);


            if (SemanticsHelpers.hasCycleInGlobalSymbolTable(globalSymbolTable)) {
                errorChecker.addSemanticError(new SemanticError("[ERROR]" + " found cycle in symbol table"));
            }

            Visitor typeCheckVisitor = new TypeCheckVisitor(globalSymbolTable, errorChecker);
            program.accept(typeCheckVisitor);

            System.out.println("\nSemantics completed");
            System.out.println("\nDisplay Symbol Table:");
            printSymbolTable(globalSymbolTable);
            if (errorChecker.hasError()) {
                return 1;
            }
            else {
                return 0;
            }

        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " +
                    e.toString());
            // print out a stack dump
            e.printStackTrace();
            return 1;
        }
    }

    private static int generateCode(String filePath, boolean debug) {
        try {

            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            Reader in = br;
            scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root;
            // replace p.parse() with p.debug_parse() in next line to see trace of
            // parser shift/reduce actions during parse

            if (debug) {
                root = p.debug_parse();
            }
            else {
                root = p.parse();
            }

            Program program = (Program) root.value;

            program.accept(new PreprocessVisitor());

            GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable();


            Visitor globalSymbolTableVisitor = new GlobalSymbolTableVisitor(globalSymbolTable);
            program.accept(globalSymbolTableVisitor);

            ErrorChecker errorChecker = new ErrorChecker();

            Visitor classSymbolTableVisitor = new ClassSymbolTableVisitor(globalSymbolTable, errorChecker);
            program.accept(classSymbolTableVisitor);


            if (SemanticsHelpers.hasCycleInGlobalSymbolTable(globalSymbolTable)) {
                errorChecker.addSemanticError(new SemanticError("[ERROR]" + " found cycle in symbol table"));
                return 1;
            }

            Visitor typeCheckVisitor = new TypeCheckVisitor(globalSymbolTable, errorChecker);
            program.accept(typeCheckVisitor);
            if (errorChecker.hasError()) {
                return 1;
            }

            Assembly assembly = new Assembly();
            Visitor codeGenVisitor = new CodeGenVisitor(globalSymbolTable, assembly);
            program.accept(codeGenVisitor);

            System.out.println(assembly.getCode());

            return 0;

        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " +
                    e.toString());
            // print out a stack dump
            e.printStackTrace();
            return 1;
        }
    }

    private static void printSymbolTable(GlobalSymbolTable globalSymbolTable) {
        for(String key : globalSymbolTable.table.keySet()) {
            printClassSymbolTable(globalSymbolTable.table.get(key));
            System.out.println();
        }
    }

    private static void printClassSymbolTable(ClassSymbolTable classSymbolTable) {
        String indent = INDENTATION;
        System.out.print(indent + classSymbolTable.className.typeName);
        if (classSymbolTable.hasExtends) {
            System.out.print(" extends " + classSymbolTable.extendsName.typeName);
        }
        System.out.println();

        indent += INDENTATION;
        for (int i = 0; i < classSymbolTable.variableDeclarations.size(); i++) {
            Type variable = classSymbolTable.variableDeclarations.get(i);
            System.out.println(indent + variable.typeName
                    + " " + variable.identifier);
        }

        System.out.println("\n" + indent + "Methods:");
        indent += INDENTATION;
        HashMap<String, MethodSymbolTable> methodSymbolTables = classSymbolTable.methodsTable;
        for(String key : methodSymbolTables.keySet()) {
            MethodSymbolTable methodTable = methodSymbolTables.get(key);
            printMethodSymbolTable(indent, methodTable);
        }
    }

    private static int generatePython(String filePath, boolean debug) {
        try {

            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            Reader in = br;
            scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root;
            // replace p.parse() with p.debug_parse() in next line to see trace of
            // parser shift/reduce actions during parse

            if (debug) {
                root = p.debug_parse();
            }
            else {
                root = p.parse();
            }

            Program program = (Program) root.value;

            program.accept(new PreprocessVisitor());

            GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable();


            Visitor globalSymbolTableVisitor = new GlobalSymbolTableVisitor(globalSymbolTable);
            program.accept(globalSymbolTableVisitor);

            ErrorChecker errorChecker = new ErrorChecker();

            Visitor classSymbolTableVisitor = new ClassSymbolTableVisitor(globalSymbolTable, errorChecker);
            program.accept(classSymbolTableVisitor);


            if (SemanticsHelpers.hasCycleInGlobalSymbolTable(globalSymbolTable)) {
                errorChecker.addSemanticError(new SemanticError("[ERROR]" + " found cycle in symbol table"));
                return 1;
            }

            Visitor typeCheckVisitor = new TypeCheckVisitor(globalSymbolTable, errorChecker);
            program.accept(typeCheckVisitor);
            if (errorChecker.hasError()) {
                return 1;
            }

            Python python = new Python();
            Visitor pythonGenVisitor = new PythonGenVisitor(globalSymbolTable, python);
            program.accept(pythonGenVisitor);

            System.out.println(python.getCode());

            return 0;

        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " +
                    e.toString());
            // print out a stack dump
            e.printStackTrace();
            return 1;
        }
    }

    private static void printMethodSymbolTable(String indent, MethodSymbolTable methodSymbolTable) {
        System.out.print(indent + methodSymbolTable.returnType.typeName + " " + methodSymbolTable.name);
        System.out.print("(");

        List<Type> arguments = methodSymbolTable.arguments;
        for (int i = 0; i < arguments.size() - 1; i++) {
            Type arg = arguments.get(i);
            System.out.print(arg.typeName + " " + arg.identifier + ", ");
        }

        if (arguments.size() > 0) {
            Type arg = arguments.get(arguments.size() - 1);
            System.out.print(arg.typeName + " " + arg.identifier);
        }
        System.out.println(")");

        indent += INDENTATION;
        List<Type> varDec = methodSymbolTable.variableDeclarations;
        for(int i = 0; i < varDec.size(); i++) {
            Type var = varDec.get(i);
            System.out.println(indent + var.typeName + " " + var.identifier);
        }

        System.out.println();
    }
}
