package AST.Visitor.Semantics;

import AST.*;
import AST.Visitor.Semantics.GlobalSymbolTable;
import AST.Visitor.Visitor;

import java.util.ArrayList;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

/*

Type checking plan:

Pass 1) Global Symbol Table
        Get all of the classes
        build up hashmap class name -> class obj
        build up symbol methodsTable for classes

Pass 2)
       For all the classes build up symbol methodsTable for methods

Pass 3)


*/

public class GlobalSymbolTableVisitor implements Visitor {
    // Display added for toy example language.  Not used in regular MiniJava

    private GlobalSymbolTable globalSymbolTable;

    public GlobalSymbolTableVisitor(GlobalSymbolTable globalSymbolTable) {
        this.globalSymbolTable = globalSymbolTable;
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
        // TODO: main class should be handled in a special way...
        ClassSymbolTable newClass = new
                ClassSymbolTable(false,
                new Type(Type.DataType.CLASS_DATATYPE, n.i1.s, null, Type.Location.OTHER_LOCATION),
                null);

        // dont add duplicate classes
        String className = n.i1.s;
        globalSymbolTable.classNames.add(className);

        if(!globalSymbolTable.table.containsKey(className)) {
            globalSymbolTable.table.put(className, newClass);
        }

        n.i1.accept(this);

        n.i2.accept(this);

        n.s.accept(this);

    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {

        ClassSymbolTable newClass = new
                ClassSymbolTable(false,
                new Type(Type.DataType.CLASS_DATATYPE, n.i.s, null, Type.Location.OTHER_LOCATION),
                null);

        // dont add duplicate classes
        String className = n.i.s;
        globalSymbolTable.classNames.add(className);

        if(!globalSymbolTable.table.containsKey(className)) {
            globalSymbolTable.table.put(className, newClass);
        }

        n.i.accept(this);

        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
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
        ClassSymbolTable newClass = new
                ClassSymbolTable(true,
                new Type(Type.DataType.CLASS_DATATYPE, n.i.s, null, Type.Location.OTHER_LOCATION),
                new Type(Type.DataType.CLASS_DATATYPE, n.j.s, null, Type.Location.OTHER_LOCATION));

        // dont add duplicate classes
        String className = n.i.s;
        globalSymbolTable.classNames.add(className);

        if(!globalSymbolTable.table.containsKey(className)) {
            globalSymbolTable.table.put(className, newClass);
        }

        n.i.accept(this);

        n.j.accept(this);

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
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

