package AST.Visitor.Semantics;

import AST.*;
import AST.Visitor.Visitor;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

/*

Collects information about the classes:
1) The variable decelerations in the start of a class
2) Method names/args/var decls
*/

public class ClassSymbolTableVisitor implements Visitor {
    // Display added for toy example language.  Not used in regular MiniJava

    private GlobalSymbolTable globalSymbolTable;

    private String currentClassName;
    private ErrorChecker errorChecker;

    public ClassSymbolTableVisitor(GlobalSymbolTable globalSymbolTable, ErrorChecker errorChecker) {
        this.globalSymbolTable = globalSymbolTable;
        this.errorChecker = errorChecker;
    }


    public void visit(Display n) {
        System.out.print("display ");
        n.e.accept(this);
        System.out.print(";");
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);

        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }

    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        currentClassName = n.i1.s;

        MethodSymbolTable newMethod = new MethodSymbolTable(
                new Type(
                        null,
                        null,
                        null,
                        Type.Location.OTHER_LOCATION
                ),
                "main"
        );

        // TODO: Add statements for main class


        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(currentClassName);

        classSymbolTable.methodsTable.put("main", newMethod);

        n.i1.accept(this);

        n.i2.accept(this);

        n.s.accept(this);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        n.i.accept(this);

        String className = n.i.s;
        currentClassName = className;
        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(className);

        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
            VarDecl varDecl = n.vl.get(i);

            classSymbolTable.addVariableDeclaration(
                    new Type(
                            Type.getDataType(varDecl.t),
                            Type.getTypeString(varDecl.t),
                            varDecl.i.s,
                            Type.Location.CLASS_LOCATION
                    ),
                    globalSymbolTable
            );
        }

        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.get(i).accept(this);
        }

    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        n.i.accept(this);

        n.j.accept(this);

        String className = n.i.s;
        currentClassName = className;
        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(className);

        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
            VarDecl varDecl = n.vl.get(i);

            classSymbolTable.addVariableDeclaration(
                    new Type(
                            Type.getDataType(varDecl.t),
                            Type.getTypeString(varDecl.t),
                            varDecl.i.s,
                            Type.Location.CLASS_LOCATION
                    ),
                    globalSymbolTable
            );
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);

        n.i.accept(this);
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        // formallist is arguments
        // var decl list is variable decl

        MethodSymbolTable newMethod = new MethodSymbolTable(
                new Type(
                        Type.getDataType(n.t),
                        Type.getTypeString(n.t),
                        null,
                        Type.Location.OTHER_LOCATION
                ),
                n.i.s
        );


        for ( int i = 0; i < n.fl.size(); i++ ) {
            Formal f = n.fl.get(i);

            newMethod.addArgumentType(
                    new Type(
                            Type.getDataType(f.t),
                            Type.getTypeString(f.t),
                            f.i.s,
                            Type.Location.ARGUMENT_LOCATION
                    ),
                    globalSymbolTable,
                    errorChecker
            );
        }

        for ( int i = 0; i < n.vl.size(); i++ ) {
            VarDecl varDecl = n.vl.get(i);

            newMethod.addVarType(
                    new Type(
                            Type.getDataType(varDecl.t),
                            Type.getTypeString(varDecl.t),
                            varDecl.i.s,
                            Type.Location.LOCAL_LOCATION
                    ),
                    globalSymbolTable,
                    errorChecker
            );
        }

        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(currentClassName);
        String methodName = n.i.s;
        classSymbolTable.methodNames.add(methodName);

        // dont add duplicate methods
        if(!classSymbolTable.methodsTable.containsKey(methodName)) {
            classSymbolTable.methodsTable.put(n.i.s, newMethod);
        }

        n.t.accept(this);

        n.i.accept(this);

        for ( int i = 0; i < n.fl.size(); i++ ) {
            n.fl.get(i).accept(this);
        }

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }

        n.e.accept(this);
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);

        n.i.accept(this);
    }

    public void visit(IntArrayType n) {
    }

    public void visit(DoubleArrayType n) {

    }

    public void visit(BooleanType n) {
    }

    public void visit(IntegerType n) {
    }

    public void visit(DoubleType n) {

    }

    // String s;
    public void visit(IdentifierType n) {
    }

    // StatementList sl;
    public void visit(Block n) {
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        n.e.accept(this);

        n.s1.accept(this);

        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        n.e.accept(this);

        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
    }

    // Exp e;
    public void visit(Sqrt n) {
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        n.i.accept(this);

        n.e.accept(this);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);

        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);

        n.e2.accept(this);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);

        n.i.accept(this);

        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
        }
    }

    // int i;
    public void visit(IntegerLiteral n) {
    }

    // double i;
    public void visit(DoubleLiteral n) {
    }

    public void visit(True n) {
    }

    public void visit(False n) {
    }

    // String s;
    public void visit(IdentifierExp n) {
    }

    public void visit(This n) {
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
    }

    // Exp e;
    public void visit(NewDoubleArray n) {
        n.e.accept(this);
    }

    // Identifier i;
    public void visit(NewObject n) {
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}

