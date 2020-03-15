package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

public class ASTVisitor implements Visitor {

  // indent by two spaces
  private static final int INDENT_AMOUNT = 2;
  private static int currentIndentLevel = 0;

  private static void printWithIndent(String string) {
      StringBuilder res = new StringBuilder();
      for (int i = 0; i < currentIndentLevel; i++) {
        res.append(" ");
      }
      res.append(string);
      System.out.println(res.toString());
  }

  private static void increaseIndent() {
    currentIndentLevel += INDENT_AMOUNT;
  }

  private static void decreaseIndent() {
    currentIndentLevel -= INDENT_AMOUNT;
  }

  private static String getASTString(ASTNode ast, boolean includeLineNumber) {
    if (includeLineNumber) {
      return ast.getClass().getSimpleName() + " " + "[line " + ast.line_number + "] ";
    }
    else {
      return ast.getClass().getSimpleName() + " ";
    }
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    System.out.print("display ");
    n.e.accept(this);
    System.out.print(";");
  }
  
  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    printWithIndent(getASTString(n, true));
    increaseIndent();
    n.m.accept(this);
    decreaseIndent();

    for ( int i = 0; i < n.cl.size(); i++ ) {
        increaseIndent();
        n.cl.get(i).accept(this);
        decreaseIndent();
    }

  }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.i1.accept(this);
    decreaseIndent();

    increaseIndent();
    n.i2.accept(this);
    decreaseIndent();

    increaseIndent();
    n.s.accept(this);
    decreaseIndent();
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.i.accept(this);

    for (int i = 0; i < n.vl.size(); i++) {
      increaseIndent();
      n.vl.get(i).accept(this);
      decreaseIndent();
    }

    for (int i = 0; i < n.ml.size(); i++) {
      increaseIndent();
      n.ml.get(i).accept(this);
      decreaseIndent();
    }

    decreaseIndent();
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.i.accept(this);
    decreaseIndent();

    increaseIndent();
    printWithIndent("EXTENDS ");
    n.j.accept(this);
    decreaseIndent();

    for ( int i = 0; i < n.vl.size(); i++ ) {
      increaseIndent();
      n.vl.get(i).accept(this);
      decreaseIndent();
    }

    for ( int i = 0; i < n.ml.size(); i++ ) {
        increaseIndent();
        n.ml.get(i).accept(this);
        decreaseIndent();
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.t.accept(this);
    decreaseIndent();

    increaseIndent();
    n.i.accept(this);
    decreaseIndent();
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    printWithIndent("Method name: ");
    increaseIndent();
    n.i.accept(this);
    decreaseIndent();
    decreaseIndent();

    increaseIndent();
    printWithIndent("Returns: ");
    increaseIndent();
    n.t.accept(this);
    decreaseIndent();
    decreaseIndent();

    increaseIndent();
    printWithIndent("Method Parameters: ");
    for ( int i = 0; i < n.fl.size(); i++ ) {
      increaseIndent();
      n.fl.get(i).accept(this);
      decreaseIndent();
    }
    for ( int i = 0; i < n.vl.size(); i++ ) {
      increaseIndent();
      n.vl.get(i).accept(this);
      decreaseIndent();
    }
    decreaseIndent();

    increaseIndent();
    printWithIndent("Method Statements: ");
    for ( int i = 0; i < n.sl.size(); i++ ) {
      increaseIndent();
      n.sl.get(i).accept(this);
      decreaseIndent();
    }
    decreaseIndent();

    printWithIndent("Method Return: ");
    increaseIndent();
    n.e.accept(this);
    decreaseIndent();
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.t.accept(this);
    decreaseIndent();

    increaseIndent();
    n.i.accept(this);
    decreaseIndent();
  }

  public void visit(IntArrayType n) {
    printWithIndent(getASTString(n, true));
  }

  public void visit(DoubleArrayType n) {
    printWithIndent(getASTString(n, true));
  }

  public void visit(BooleanType n) {
    printWithIndent(getASTString(n, true));
  }

  public void visit(IntegerType n) {
    printWithIndent(getASTString(n, true));
  }

  public void visit(DoubleType n) {
    printWithIndent(getASTString(n, true));
  }

  // String s;
  public void visit(IdentifierType n) {
    printWithIndent(getASTString(n, false) + "(" + n.s + ")");
  }

  // StatementList sl;
  public void visit(Block n) {
    printWithIndent(getASTString(n, true));

    for ( int i = 0; i < n.sl.size(); i++ ) {
      increaseIndent();
      n.sl.get(i).accept(this);
      decreaseIndent();
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();

    increaseIndent();
    n.s1.accept(this);
    decreaseIndent();


    increaseIndent();
    n.s2.accept(this);
    decreaseIndent();
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();

    increaseIndent();
    n.s.accept(this);
    decreaseIndent();
  }

  // Exp e;
  public void visit(Print n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();

  }

  // Exp e;
  public void visit(Sqrt n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();

  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.i.accept(this);
    decreaseIndent();

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.i.accept(this);
    decreaseIndent();

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();

    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();
  }

  // Exp e1,e2;
  public void visit(And n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();

    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();

    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();


    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();

  }

  // Exp e1,e2;
  public void visit(Minus n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();


    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();
  }

  // Exp e1,e2;
  public void visit(Times n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();

    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e1.accept(this);
    decreaseIndent();


    increaseIndent();
    n.e2.accept(this);
    decreaseIndent();
  }

  // Exp e;
  public void visit(ArrayLength n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();

    increaseIndent();
    n.i.accept(this);
    decreaseIndent();


    for ( int i = 0; i < n.el.size(); i++ ) {
      increaseIndent();
      n.el.get(i).accept(this);
      decreaseIndent();
    }
  }

  // int i;
  public void visit(IntegerLiteral n) {
    printWithIndent(getASTString(n, false) + "(" + n.i + ")");
  }

  // double i;
  public void visit(DoubleLiteral n) {
    printWithIndent(getASTString(n, false) + "(" + n.i + ")");
  }

  public void visit(True n) {
    printWithIndent(getASTString(n, true));
  }

  public void visit(False n) {
    printWithIndent(getASTString(n, true));
  }

  // String s;
  public void visit(IdentifierExp n) {
    printWithIndent(getASTString(n, false) + "(" + n.s + ")");
  }

  public void visit(This n) {
    printWithIndent(getASTString(n, true));
  }

  // Exp e;
  public void visit(NewArray n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();
  }

  // Exp e;
  public void visit(NewDoubleArray n) {
    printWithIndent(getASTString(n, true));

    increaseIndent();
    n.e.accept(this);
    decreaseIndent();

  }

  // Identifier i;
  public void visit(NewObject n) {
    printWithIndent(getASTString(n, false) + "(" + n.i.s + ")");
  }

  // Exp e;
  public void visit(Not n) {
    printWithIndent(getASTString(n, false));
    increaseIndent();
    n.e.accept(this);
    decreaseIndent();
  }

  // String s;
  public void visit(Identifier n) {
    printWithIndent(getASTString(n, false) + "(" + n.s + ")");
  }
}
