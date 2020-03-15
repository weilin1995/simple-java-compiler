package AST.Visitor.Python;

import AST.*;
import AST.Visitor.Semantics.*;
import AST.Visitor.Semantics.Type;
import AST.Visitor.Visitor;

public class PythonGenVisitor implements Visitor {
    private GlobalSymbolTable globalSymbolTable;
    private Python python;

    private String currentClassName;
    private String currentMethodName;
    private String mainClassName;
    private int labelNumber;

    public PythonGenVisitor(GlobalSymbolTable globalSymbolTable, Python python) {
        this.globalSymbolTable = globalSymbolTable;
        this.python = python;
        currentClassName = "";
        currentMethodName = "";
        labelNumber = 0;
    }

    @Override
    public void visit(Display n) {

    }

    // MainClass m;
    // ClassDeclList cl;
    @Override
    public void visit(Program n) {
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }

        python.generateNewline();
        python.generateNewline();
        python.generateStatement("if __name__ == \"__main__\":");
        python.increaseIndent();
        python.generateWithIndent(mainClassName + "()" + ".main()");
        python.decreaseIndent();
    }

    // Identifier i1,i2;
    // Statement s;
    @Override
    public void visit(MainClass n) {


        currentMethodName = "main";
        currentClassName = n.i1.s;
        mainClassName = currentClassName;

        python.generateStatement("class " + currentClassName + ":");
        python.increaseIndent();

        python.generateStatement("def " + "__init__" + "(self):"); // start method
        python.increaseIndent();
        python.generateStatement("pass");
        python.decreaseIndent(); // end method
        python.decreaseIndent(); // end class

        python.generateNewLineAndIndent(); // start method decl
        python.generateStatement("def main(self):"); // start method body
        python.increaseIndent();
        n.i1.accept(this);

        n.i2.accept(this);

        n.s.accept(this);
        python.decreaseIndent(); // end method body
        python.decreaseIndent(); // end method decl

        python.generateNewline();
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    @Override
    public void visit(ClassDeclSimple n) {
        String className = n.i.s;
        currentClassName = className;

        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(className);

        n.i.accept(this);


        python.generateNewline();
        python.generateStatement("class " + currentClassName + "(" + "object" + ")" + ":"); // start class
        python.increaseIndent();

        python.generateStatement("def " + "__init__" + "(self):"); // start method
        python.increaseIndent();

        for (int i = 0; i < n.vl.size(); i++) {
            currentMethodName = "";
            n.vl.get(i).accept(this);

        }

        python.generateStatement("pass");
        python.decreaseIndent(); // end method

        python.generateNewline();

        for (int i = 0; i < n.ml.size(); i++) {
            currentMethodName = "";
            n.ml.get(i).accept(this);
        }

        python.decreaseIndent(); // end class
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    @Override
    public void visit(ClassDeclExtends n) {
        String className = n.i.s;
        currentClassName = className;

        String parentClass = n.j.s;

        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(className);


        n.i.accept(this);

        n.j.accept(this);


        python.generateNewline();
        python.generateStatement("class " + currentClassName + "(" + parentClass + ")" + ":"); // start class
        python.increaseIndent();

        python.generateStatement("def " + "__init__" + "(self):"); // start method
        python.increaseIndent();
        python.generateWithIndent("super(" + parentClass + ", self).__init__()");

        for ( int i = 0; i < n.vl.size(); i++ ) {
            currentMethodName = "";
            n.vl.get(i).accept(this);
        }

        python.generateNewline();
        python.generateWithIndent("pass");
        python.decreaseIndent(); // end method
        python.generateNewline();

        for ( int i = 0; i < n.ml.size(); i++ ) {
            currentMethodName = "";
            n.ml.get(i).accept(this);
        }
        python.decreaseIndent(); // end class
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {

        n.t.accept(this);

        n.i.accept(this);

        Type t = SemanticsHelpers.getTypeForIdentifier(n.i.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (t != null && t.location == Type.Location.CLASS_LOCATION) {
            python.generateStatement("self." + n.i + " = None");
        }
        else {
            python.generateStatement(n.i + " = None");
        }

    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        currentMethodName = n.i.s;


        n.t.accept(this);

        n.i.accept(this);


        python.generateWithIndent("def " + currentMethodName + "(self");

        for ( int i = 0; i < n.fl.size(); i++ ) {
            python.generate(", ");
            n.fl.get(i).accept(this);
        }

        python.generate("):"); // end of method decl

        python.generateNewline();
        python.increaseIndent();

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }

        python.generateWithIndent("return ");
        n.e.accept(this);

        python.decreaseIndent();
        python.generateNewline();
        python.generateNewline();
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);

        python.generate(n.i.s);
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
        python.generateWithIndent("if ");
        n.e.accept(this);
        python.generate(":");

        python.generateNewline();
        python.increaseIndent();
        n.s1.accept(this);
        python.decreaseIndent();

        python.generateWithIndent("else:");
        python.generateNewline();
        python.increaseIndent();
        n.s2.accept(this);
        python.decreaseIndent();

    }

    // Exp e;
    // Statement s;
    public void visit(While n) {

        python.generateWithIndent("while ");
        n.e.accept(this);
        python.generate(":");

        python.generateNewline();

        python.increaseIndent();
        n.s.accept(this);
        python.decreaseIndent();

    }

    // Exp e;
    public void visit(Print n) {
        python.generateWithIndent("print(");
        n.e.accept(this);
        python.generate(")");
        python.generateNewline();
    }

    public void visit(Sqrt n) {

    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        Type t = SemanticsHelpers.getTypeForIdentifier(n.i.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        n.i.accept(this);

        if (t != null && t.location == Type.Location.LOCAL_LOCATION) {
            python.generateWithIndent(n.i.s);
        }
        else if (t != null && t.location == Type.Location.CLASS_LOCATION) {
            python.generateWithIndent("self." + n.i.s);
        }
        else if (t != null && t.location == Type.Location.ARGUMENT_LOCATION) {
            python.generateWithIndent(n.i.s);
        }
        else {
            System.out.println("Unsupported location visit(Assign)");
        }

        python.generate(" = ");
        n.e.accept(this);
        python.generateNewline();
    }

    // i[e1] = e2;
    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {

        Type t = SemanticsHelpers.getTypeForIdentifier(n.i.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        n.i.accept(this);

        if (t != null && t.location == Type.Location.LOCAL_LOCATION) {
            python.generateWithIndent(n.i.s);
        }
        else if (t != null && t.location == Type.Location.CLASS_LOCATION) {
            python.generateWithIndent("self." + n.i.s);
        }
        else if (t != null && t.location == Type.Location.ARGUMENT_LOCATION) {
            python.generateWithIndent(n.i.s);
        }
        else {
            System.out.println("Unsupported location visit(ArrayAssign)");
        }

        python.generate("[");
        n.e1.accept(this);
        python.generate("]");

        python.generate(" = ");
        n.e2.accept(this);

        python.generateNewline();
    }

    // Exp e1,e2;
    public void visit(And n) {

        n.e1.accept(this);
        python.generate(" and ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        python.generate(" < ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    // ex1 + ex2
    public void visit(Plus n) {
        n.e1.accept(this);
        python.generate(" + ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        python.generate(" - ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        python.generate(" * ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    // e1 is ArrayType
    // e2 is int
    // e1[e2]
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        python.generate("[");
        n.e2.accept(this);
        python.generate("]");
    }

    // Exp e;
    public void visit(ArrayLength n) {
        python.generate("len(");
        n.e.accept(this);
        python.generate(")");
    }

    // Exp e;
    // Identifier i;
    // ExpList el;

    // a.Start();
    public void visit(Call n) {
        n.e.accept(this);

        python.generate("." + n.i.s + "(");
        for (int i = 0; i < n.el.size(); i++) {
            n.el.get(i).accept(this);
            if (i != n.el.size() - 1) {
                python.generate(",");
            }
        }
        python.generate(")");
    }

    // int i;
    public void visit(IntegerLiteral n) {
        python.generate(""+n.i);
    }

    // double i;
    public void visit(DoubleLiteral n) {
    }

    public void visit(True n) {
        python.generate("True");
    }

    public void visit(False n) {
        python.generate("False");
    }

    // String s;
    public void visit(IdentifierExp n) {
        Type t = SemanticsHelpers.getTypeForIdentifier(n.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (t != null && t.location == Type.Location.LOCAL_LOCATION) {
            python.generate(n.s);
        }
        else if (t != null && t.location == Type.Location.CLASS_LOCATION) {
            python.generate("self." + n.s);
        }
        else if (t != null && t.location == Type.Location.ARGUMENT_LOCATION) {
            python.generate(n.s);
        }
        else {
            System.out.println("Unsupported location visit(IdentifierExp)");
        }
    }

    public void visit(This n) {
        python.generate("self");
    }

    // Exp e;
    public void visit(NewArray n) {
        python.generate("[0] * ");
        n.e.accept(this);
    }

    public void visit(NewDoubleArray n) {

    }

    // Identifier i;
    public void visit(NewObject n) {
        String objectType = n.i.s;
        python.generate(objectType+"()");
    }

    // Exp e;
    public void visit(Not n) {
        python.generate("not ");
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {

    }
}

