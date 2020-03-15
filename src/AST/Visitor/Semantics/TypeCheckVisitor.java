package AST.Visitor.Semantics;

import AST.*;
import AST.Visitor.Visitor;

import java.util.Map;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

// Should go through each statement and type check using existing info in global symbol methodsTable.
// Should also update bindings as it goes
public class TypeCheckVisitor implements Visitor {
    // Display added for toy example language.  Not used in regular MiniJava

    private GlobalSymbolTable globalSymbolTable;

    private String currentClassName;

    private String currentMethodName;

    private ErrorChecker errorChecker;

    public TypeCheckVisitor(GlobalSymbolTable globalSymbolTable, ErrorChecker errorChecker) {
        this.globalSymbolTable = globalSymbolTable;
        this.errorChecker = errorChecker;
        currentClassName = "";
        currentMethodName = "";
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
        currentMethodName = "main";

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

        if (SemanticsHelpers.hasDuplicateName(className, "", "", globalSymbolTable)) {
            errorChecker.addSemanticError(new SemanticError("[ERROR] Duplicate Class Declaration: " + className + " in line " + n.line_number));
        }

        for (int i = 0; i < n.vl.size(); i++) {
            currentMethodName = "";
            n.vl.get(i).accept(this);
        }

        for (int i = 0; i < n.ml.size(); i++) {
            currentMethodName = "";
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

        if (SemanticsHelpers.hasDuplicateName(className, "", "", globalSymbolTable)) {
            errorChecker.addSemanticError(new SemanticError("[ERROR] Duplicate Class Declaration: " + className + " in line " + n.line_number));
        }

        for ( int i = 0; i < n.vl.size(); i++ ) {
            currentMethodName = "";
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            currentMethodName = "";
            n.ml.get(i).accept(this);
        }
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {

        if (SemanticsHelpers.hasDuplicateName(currentClassName, currentMethodName, n.i.s, globalSymbolTable)) {
            errorChecker.addSemanticError(new SemanticError("[ERROR] Duplicate Variable Declaration: " + currentClassName + " in line " + n.line_number));
        }

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

        currentMethodName = n.i.s;

        if (SemanticsHelpers.hasDuplicateName(currentClassName, currentMethodName, "", globalSymbolTable)) {
            errorChecker.addSemanticError(new SemanticError("[ERROR] Duplicate Method Declaration: " + currentClassName + " in line " + n.line_number));
        }

        // Check that the return type of the method and the return expression have equal types
        String currentMethodName = n.i.s;
        Type.DataType methodReturnDataType = Type.getDataType(n.t);
        Type.DataType actualReturnDataType = SemanticsHelpers.expressionType(
                n.e,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (methodReturnDataType != actualReturnDataType) {
            errorChecker.addSemanticError(new SemanticError("Method on line " + n.line_number + " return type and expression return types are different"));
        }

        if (methodReturnDataType == Type.DataType.REFERENCE_DATATYPE
                && actualReturnDataType == Type.DataType.REFERENCE_DATATYPE
                ) {

            if (!(n.t instanceof IdentifierType)) {
                System.out.println("SHOULD NEVER HAPPEN");
            }

            ClassSymbolTable returnClassExpected = globalSymbolTable.table.get(((IdentifierType)n.t).s);

            if (!SemanticsHelpers.isExpAssignableToType(n.e, returnClassExpected.className,
                    globalSymbolTable,
                    globalSymbolTable.table.get(currentClassName),
                    globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
            )) {
                errorChecker.addSemanticError(new SemanticError("Method on line " + n.line_number + " return type and expression return types are different"));
            }

        }

        // Check that if the method is being overridden it's types have to match exactly.
        boolean isOverriddenMethod = false;
        ClassSymbolTable currentClassSymbolTable = globalSymbolTable.table.get(currentClassName);
        ClassSymbolTable parentClassSymbolTable = null;
        if (currentClassSymbolTable.hasExtends) {
            // let's see if there exists another class with the same method name
            for (Map.Entry<String, ClassSymbolTable> item : globalSymbolTable.table.entrySet()) {
                ClassSymbolTable possibleParentSymbolTable = item.getValue();

                // The possible parent would have to have a method with the same name and be a parent
                if (possibleParentSymbolTable.methodsTable.containsKey(currentMethodName) &&
                        possibleParentSymbolTable.isParentOfLeaf(currentClassSymbolTable.extendsName, globalSymbolTable)) {
                    isOverriddenMethod = true;
                    parentClassSymbolTable = possibleParentSymbolTable;
                    break;
                }
            }

            if (isOverriddenMethod) {
                // Check return type match
                MethodSymbolTable parentMethodSymbolTable = parentClassSymbolTable.methodsTable.get(currentMethodName);
                MethodSymbolTable currentMethodSymbolTable = currentClassSymbolTable.methodsTable.get(currentMethodName);

                // Overridden method needs to have return type match exactly if it has a basic type.
                // If the type is a reference type we should have already checked this with the checks above
                if (parentMethodSymbolTable.returnType.dataType == Type.DataType.REFERENCE_DATATYPE &&
                        currentMethodSymbolTable.returnType.dataType != Type.DataType.REFERENCE_DATATYPE) {
                    errorChecker.addSemanticError(new SemanticError(
                            "Overridden method on line " + n.line_number + " does not have it's return type match that of the superclass method"
                    ));
                }


                if (parentMethodSymbolTable.arguments.size() != currentMethodSymbolTable.arguments.size()) {
                    errorChecker.addSemanticError(
                            new SemanticError(
                                    "Overridden method on line " + n.line_number + " does not have the same number of arguments as that of the superclass method"
                            ));
                }
                else {
                    // Argument types have to match exactly
                    for (int i = 0; i < parentMethodSymbolTable.arguments.size(); i++) {
                        if (!parentMethodSymbolTable.arguments.get(i).typeName.equals(currentMethodSymbolTable.arguments.get(i).typeName)) {
                            errorChecker.addSemanticError(
                                    new SemanticError(
                                            "Overridden method on line " + n.line_number + " argument types don't match that of the superclass method"
                                    ));
                        }
                    }
                }
            }
        }



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
        // e should be bool

        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.BOOLEAN_DATYPE
                )
        );
        n.e.accept(this);

        n.s1.accept(this);

        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {

        // e should be bool
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.BOOLEAN_DATYPE
                )
        );
        n.e.accept(this);

        n.s.accept(this);

    }

    // Exp e;
    public void visit(Print n) {

        n.e.accept(this);

        // e should be int (Can only print integers in minijava)
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE, Type.DataType.DOUBLE_DATATYPE
                )
        );
    }

    // Exp e;
    public void visit(Sqrt n) {
        n.e.accept(this);

        // e should be double (Can only print integers in minijava)
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLE_DATATYPE
                )
        );

    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        Type leftType = SemanticsHelpers.getTypeForIdentifier(
                n.i.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        boolean isAssignable = SemanticsHelpers.isExpAssignableToType(
                n.e,
                leftType,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (!isAssignable) {
            errorChecker.addSemanticError(new SemanticError("[ERROR] expression not assignable to left side type " + n.i.s + " on line "+ n.line_number));
        }

        n.i.accept(this);

        n.e.accept(this);
    }

    // i[e1] = e2;
    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        Type leftType = SemanticsHelpers.getTypeForIdentifier(
                n.i.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (leftType != null && leftType.dataType == Type.DataType.INTARRAY_DATATYPE) {
            // i should be int array
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkIdentifierTypeEqualTo(
                            n.i,
                            Type.DataType.INTARRAY_DATATYPE,
                            globalSymbolTable,
                            globalSymbolTable.table.get(currentClassName),
                            globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                    )
            );

            // e1 should be int
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                    )
            );

            // e2 should be int
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                    )
            );
        }
        else if (leftType != null && leftType.dataType == Type.DataType.DOUBLEARRAY_DATATYPE) {
            // i should be double array
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkIdentifierTypeEqualTo(
                            n.i,
                            Type.DataType.DOUBLEARRAY_DATATYPE,
                            globalSymbolTable,
                            globalSymbolTable.table.get(currentClassName),
                            globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                    )
            );

            // e1 should be int
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                    )
            );

            // e2 should be double
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLE_DATATYPE
                    )
            );
        }
        else {
            errorChecker.addSemanticError(new SemanticError("Array should either be int or double"));
        }


        n.i.accept(this);

        n.e1.accept(this);

        n.e2.accept(this);

    }

    // Exp e1,e2;
    public void visit(And n) {

        // e1 should be bool
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e1,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.BOOLEAN_DATYPE
                )
        );

        // e2 should be bool
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e2,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.BOOLEAN_DATYPE
                )
        );


        n.e1.accept(this);

        n.e2.accept(this);

    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        // e1 should be int
        SemanticError error1 =
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                );

        if (error1 != null) {
            SemanticError error2 =
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLE_DATATYPE
                    );

            if (error2 != null) {
                errorChecker.addSemanticError(error1);
                errorChecker.addSemanticError(error2);
            }
        }

        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    // ex1 + ex2
    public void visit(Plus n) {
        SemanticError error1 =
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                );

        if (error1 != null) {
            SemanticError error2 =
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLE_DATATYPE
                    );

            if (error2 != null) {
                errorChecker.addSemanticError(error1);
                errorChecker.addSemanticError(error2);
            }
        }

        n.e1.accept(this);
        n.e2.accept(this);

    }

    // Exp e1,e2;
    public void visit(Minus n) {
        SemanticError error1 =
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                );

        if (error1 != null) {
            SemanticError error2 =
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLE_DATATYPE
                    );

            if (error2 != null) {
                errorChecker.addSemanticError(error1);
                errorChecker.addSemanticError(error2);
            }
        }

        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        SemanticError error1 =
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                );

        if (error1 != null) {
            SemanticError error2 =
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1, n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLE_DATATYPE
                    );

            if (error2 != null) {
                errorChecker.addSemanticError(error1);
                errorChecker.addSemanticError(error2);
            }
        }

        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    // e1 is ArrayType
    // e2 is int
    public void visit(ArrayLookup n) {
        Type.DataType leftDataType = SemanticsHelpers.expressionType(
                n.e1,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (leftDataType != null && leftDataType == Type.DataType.INTARRAY_DATATYPE) {
            // e1 should be int array
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INTARRAY_DATATYPE
                    )
            );

            // e2 should be int
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                    )
            );
        }
        else if (leftDataType != null && leftDataType == Type.DataType.DOUBLEARRAY_DATATYPE) {
            // e1 should be int array
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e1,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.DOUBLEARRAY_DATATYPE
                    )
            );

            // e2 should be int
            errorChecker.addSemanticError(
                    SemanticsHelpers.checkExpressionTypeEqualTo(n.e2,
                            globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                    )
            );
        }
        else {
            errorChecker.addSemanticError(new SemanticError("Incorrect type for array lookup on line " + n.line_number));
        }


        n.e1.accept(this);
        n.e2.accept(this);

    }

    // Exp e;
    public void visit(ArrayLength n) {
        // e should be int array
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INTARRAY_DATATYPE, Type.DataType.DOUBLEARRAY_DATATYPE
                )
        );

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

        errorChecker.addSemanticError(
                SemanticsHelpers.checkCallValid(
                        n,
                        globalSymbolTable,
                        globalSymbolTable.table.get(currentClassName),
                        globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                )
        );
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
        errorChecker.addSemanticError(
                SemanticsHelpers.checkIdentifierExpDefined(
                        n,
                        globalSymbolTable,
                        globalSymbolTable.table.get(currentClassName),
                        globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                )
        );
    }

    public void visit(This n) {
    }

    // Exp e;
    public void visit(NewArray n) {
        // e should be int
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                )
        );

        n.e.accept(this);
    }

    // Exp e
    public void visit(NewDoubleArray n) {
        // e should be int
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.INT_DATATYPE
                )
        );

        n.e.accept(this);

    }

    // Identifier i;
    public void visit(NewObject n) {
        SemanticsHelpers.checkIdentifierInGlobalSymbolTable(n.i.s, globalSymbolTable);
    }

    // Exp e;
    public void visit(Not n) {
        // e should be bool
        errorChecker.addSemanticError(
                SemanticsHelpers.checkExpressionTypeEqualTo(n.e,
                        globalSymbolTable, globalSymbolTable.table.get(currentClassName), globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName), Type.DataType.BOOLEAN_DATYPE
                )
        );
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
        // I think we can skip checking identifiers here, should be handled elsewhere I hope :)
    }
}

