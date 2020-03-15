package AST.Visitor.Assembly;

import AST.*;
import AST.Visitor.Semantics.*;
import AST.Visitor.Semantics.Type;
import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CodeGenVisitor implements Visitor {
    private static int currentIndentLevel = 0;
    private GlobalSymbolTable globalSymbolTable;
    private Assembly assembly;

    private String currentClassName;
    private String currentMethodName;
    private int labelNumber;

    public CodeGenVisitor(GlobalSymbolTable globalSymbolTable, Assembly assembly) {
        this.globalSymbolTable = globalSymbolTable;
        this.assembly = assembly;
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

        // Let's generate the data segment for all the classes
        assembly.generate(".data");
        for (Map.Entry<String, ClassSymbolTable> item : globalSymbolTable.table.entrySet()) {
            String className = item.getKey();
            ClassSymbolTable classSymbolTable = item.getValue();
            assembly.generateLabel(className + "$$");

            List<String> methodNames = classSymbolTable.getMethodsInClassAndSuperClass(globalSymbolTable);

            if (classSymbolTable.hasExtends) {
                String extendsClassName = classSymbolTable.extendsName.typeName;
                assembly.generate(".quad " + extendsClassName + "$$" + " # Has superclass");
            }
            else {
                assembly.generate(".quad 0 # No super class");
            }

            for (String methodName : methodNames) {
                assembly.generate(".quad " + methodName);
            }

        }

        assembly.generateGlobal("asm_main");
        assembly.generate(".text");
        assembly.generate("");

        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }

    }

    // Identifier i1,i2;
    // Statement s;
    @Override
    public void visit(MainClass n) {


        currentMethodName = "main";
        currentClassName = n.i1.s;
        assembly.generateLabel("asm_main");

        assembly.generatePrologue(0 /* main doesn't have any var decelerations */);

        n.i1.accept(this);

        n.i2.accept(this);

        n.s.accept(this);
        assembly.generatEpilogue();
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
    @Override
    public void visit(ClassDeclExtends n) {
        String className = n.i.s;
        currentClassName = className;

        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(className);


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
        currentMethodName = n.i.s;

        assembly.generateLabel(currentClassName + "$" + currentMethodName + "$");
        assembly.generatePrologue(n.vl.size() * 8);

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
        assembly.generatEpilogue();
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
        int ifLabel = labelNumber;
        labelNumber++;

        n.e.accept(this);

        assembly.generate("cmpq $1, %rax");
        assembly.generate("jne FALSE" + ifLabel);

        assembly.generateLabel("TRUE" + ifLabel);
        n.s1.accept(this);
        assembly.generate("jmp CONTINUE" + ifLabel);

        assembly.generateLabel("FALSE" + ifLabel);
        n.s2.accept(this);
        assembly.generate("jmp CONTINUE" + ifLabel);

        assembly.generateLabel("CONTINUE" + ifLabel);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        int whileLabel = labelNumber;
        labelNumber++;

        assembly.generateLabel("WHILE_CONDITION" + whileLabel);
        n.e.accept(this);
        assembly.generate("cmpq $1, %rax");
        assembly.generate("je WHILE_BODY" + whileLabel);
        assembly.generate("jne WHILE_CONTINUE" + whileLabel);

        assembly.generateLabel("WHILE_BODY" + whileLabel);
        n.s.accept(this);
        assembly.generate("jmp WHILE_CONDITION" + whileLabel);

        assembly.generateLabel("WHILE_CONTINUE" + whileLabel);
    }

    // Exp e;
    public void visit(Print n) {
        assembly.generateComment("Print -- Start line: " + n.line_number);
        n.e.accept(this);
        assembly.generatePush("%rdi"); // save this

        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }

        Type.DataType dataType = SemanticsHelpers.expressionType(n.e, globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                );

        if (dataType == Type.DataType.INT_DATATYPE) {
            assembly.generate("movq %rax, %rdi");
            assembly.generateCall("put");
        }
        else if (dataType == Type.DataType.DOUBLE_DATATYPE) {
            assembly.generate("movq %rax, %xmm0");
            assembly.generate("call putDouble");
        }
        else {
            System.out.println("Not supported print datatype, should never happen!");
        }

        // Restore argument registers
        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(argumentRegister);
        }

        assembly.generatePop(" %rdi"); // pop this
        assembly.generateComment("Print -- End line: " + n.line_number);
    }

    // Exp e;
    public void visit(Sqrt n) {
        n.e.accept(this);
        assembly.generate("movq %rax, %xmm0");
        assembly.generate("call doubleSqrt");
        assembly.generate("movq %xmm0, %rax");
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        assembly.generateComment("Assignment -- Start line: " + n.line_number);
        n.e.accept(this);

        // rax contains RHS

        n.i.accept(this);

        MethodSymbolTable methodSymbolTable = globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName);

        Type t = SemanticsHelpers.getTypeForIdentifier(n.i.s,
                    globalSymbolTable,
                    globalSymbolTable.table.get(currentClassName),
                    globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                );

        if (t != null && t.location == Type.Location.LOCAL_LOCATION) {
            int offset = methodSymbolTable.getStackOffsetForVariable(n.i.s);
            assembly.generate("movq %rax" + ", " + -offset + "(%rbp)");
        }
        else if (t != null && t.location == Type.Location.CLASS_LOCATION) {
            int offset = globalSymbolTable.table.get(currentClassName).offsetForVariable(n.i.s, globalSymbolTable);
            assembly.generate("movq %rax " + ",     " + offset + "(%rdi)");
        }
        else if (t != null && t.location == Type.Location.ARGUMENT_LOCATION) {
            int argumentPosition = methodSymbolTable.getArgumentPositionForType(t);
            String register = CodeGenHelper.argumentRegisterForIndex(argumentPosition);
            assembly.generate("movq %rax, " + register);
        }
        else {
            System.out.println("Unsupported location visit(Assign)");
        }
        assembly.generateComment("Assignment -- End line: " + n.line_number);

    }

    // i[e1] = e2;
    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        // rax[r10] = r11
        // move r11 into 8(rax, r10, 8)

        Type leftType = SemanticsHelpers.getTypeForIdentifier(
                n.i.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );
        assembly.generatePush("%rdi");
        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }

        n.e2.accept(this);
        assembly.generatePush("%rax");
        n.e1.accept(this);
        assembly.generatePush("%rax");

        assembly.generatePop(" %r10"); //r10 has e1
        assembly.generatePop(" %r11"); //r11 has e2

        IdentifierExp identifierExp = new IdentifierExp(n.i.s, new ComplexSymbolFactory.Location(n.line_number, 0));
        identifierExp.accept(this);

        // assembly.generate("movq %r11, 8(%rax, %r10, 8)"); // rax + (8 * r10) + 8

        assembly.generate("movq %rax, %rdi"); //jArray
        assembly.generate("movq %r10, %rsi"); //index
        if (leftType != null && leftType.dataType == Type.DataType.INTARRAY_DATATYPE) {
            assembly.generate("movq %r11, %rdx"); //value
            assembly.generateCall(" arrayAssign");
        }
        else if (leftType != null && leftType.dataType == Type.DataType.DOUBLEARRAY_DATATYPE) {
            assembly.generate("movq %r11, %xmm0"); //value
            assembly.generateCall(" doubleArrayAssign");
        }
        else {
            System.out.println("Should not happen, arrayAssign codegen");
        }

        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(argumentRegister);
        }
        assembly.generatePop(" %rdi");
    }

    // Exp e1,e2;
    public void visit(And n) {
        int andLabel = labelNumber;
        labelNumber++;

        assembly.generateComment("AND -- Start line: " + n.line_number);

        assembly.generateComment("AND E1 -- Start line: " + n.line_number);
        n.e1.accept(this); // stored in rax
        assembly.generateComment("AND E1 -- End line: " + n.line_number);

        assembly.generate("movq $1, %r10");
        assembly.generate("cmpq %r10, %rax");
        assembly.generate("je TRUE" + andLabel);
        assembly.generate("jne FALSE" + andLabel);

        assembly.generateLabel("TRUE" + andLabel);
        assembly.generateComment("AND E2 -- Start line: " + n.line_number);
        n.e2.accept(this);
        assembly.generateComment("AND E2 -- End line: " + n.line_number);
        assembly.generate("jmp CONTINUE" + andLabel);

        assembly.generateLabel("FALSE" + andLabel);
        assembly.generate("movq $0, %rax");
        assembly.generate("jmp CONTINUE" + andLabel);


        assembly.generateLabel("CONTINUE" + andLabel);

        assembly.generateComment("AND -- End line: " + n.line_number);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        int lessThanLabel = labelNumber;
        labelNumber++;

        Type.DataType dataType = SemanticsHelpers.expressionType(n.e1, n.e2, globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        assembly.generateComment("LESSTHAN -- Start line: " + n.line_number);
        if (dataType == Type.DataType.INT_DATATYPE) {
            n.e1.accept(this);
            assembly.generate("movq %rax, %r10");
            n.e2.accept(this);
            assembly.generate("movq %rax, %r11");
            assembly.generate("cmpq %r11, %r10");
            assembly.generate("jl TRUE" + lessThanLabel);
            assembly.generate("jge FALSE" + lessThanLabel);

            assembly.generateLabel("TRUE" + lessThanLabel);
            assembly.generate("movq $1, %rax");
            assembly.generate("jmp CONTINUE" + lessThanLabel);

            assembly.generateLabel("FALSE" + lessThanLabel);
            assembly.generate("movq $0, %rax");
            assembly.generate("jmp CONTINUE" + lessThanLabel);

            assembly.generateLabel("CONTINUE" + lessThanLabel);
        }
        else {
            n.e1.accept(this);
            assembly.generatePush("%rax");
            n.e2.accept(this);
            assembly.generate("movq %rax, %xmm1");
            assembly.generatePop(" %rax");
            assembly.generate("movq %rax, %xmm0");
            assembly.generate("call isDoubleLessThan");
            assembly.generate("movq $1, %r11");
            assembly.generate("cmpq %r11, %rax");
            assembly.generate("je TRUE" + lessThanLabel);
            assembly.generate("jne FALSE" + lessThanLabel);

            assembly.generateLabel("TRUE" + lessThanLabel);
            assembly.generate("movq $1, %rax");
            assembly.generate("jmp CONTINUE" + lessThanLabel);

            assembly.generateLabel("FALSE" + lessThanLabel);
            assembly.generate("movq $0, %rax");
            assembly.generate("jmp CONTINUE" + lessThanLabel);

            assembly.generateLabel("CONTINUE" + lessThanLabel);

        }
        assembly.generateComment("LESSTHAN -- Ends line: " + n.line_number);

    }

    // Exp e1,e2;
    // ex1 + ex2
    public void visit(Plus n) {
        Type.DataType dataType = SemanticsHelpers.expressionType(n.e1, n.e2, globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );


        if (dataType == Type.DataType.INT_DATATYPE) {
            assembly.generateComment("Plus -- Start line: " + n.line_number);
            n.e1.accept(this);

            assembly.generatePush("%rax");

            n.e2.accept(this);

            assembly.generatePop(" %rbx");
            assembly.generate("addq %rbx, %rax");
            assembly.generateComment("Plus -- End line: " + n.line_number);
        }
        else {
            assembly.generateComment("Plus -- Start line: " + n.line_number);
            n.e1.accept(this);

            assembly.generatePush("%rax");

            n.e2.accept(this);
            assembly.generate("movq %rax, %xmm1");

            assembly.generatePop(" %rax");
            assembly.generate("movq %rax, %xmm0");

            assembly.generate("call addDouble");
            assembly.generate("movq %xmm0, %rax");
            assembly.generateComment("Plus -- End line: " + n.line_number);

        }
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        Type.DataType dataType = SemanticsHelpers.expressionType(n.e1, n.e2, globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (dataType == Type.DataType.INT_DATATYPE) {

            assembly.generateComment("Minus -- Start line: " + n.line_number);
            n.e2.accept(this); // have to visit e2 first

            assembly.generatePush("%rax");

            n.e1.accept(this);

            assembly.generatePop(" %rbx");
            assembly.generate("subq %rbx, %rax");

            assembly.generateComment("Minus -- End line: " + n.line_number);
        }
        else {
            assembly.generateComment("Minus -- Start line: " + n.line_number);
            n.e1.accept(this);

            assembly.generatePush("%rax");

            n.e2.accept(this);
            assembly.generate("movq %rax, %xmm1");

            assembly.generatePop(" %rax");
            assembly.generate("movq %rax, %xmm0");

            assembly.generate("call minusDouble");
            assembly.generate("movq %xmm0, %rax");
            assembly.generateComment("Minus -- End line: " + n.line_number);
        }
    }

    // Exp e1,e2;
    public void visit(Times n) {
        Type.DataType dataType = SemanticsHelpers.expressionType(n.e1, n.e2, globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (dataType == Type.DataType.INT_DATATYPE) {
            assembly.generateComment("Times -- Start line: " + n.line_number);
            n.e1.accept(this);

            assembly.generatePush("%rax");

            n.e2.accept(this);

            assembly.generatePop(" %rbx");
            assembly.generate("imulq %rbx, %rax");
            assembly.generateComment("Times -- End line: " + n.line_number);
        }
        else {
            assembly.generateComment("Times -- Start line: " + n.line_number);
            n.e1.accept(this);

            assembly.generatePush("%rax");

            n.e2.accept(this);

            assembly.generate("movq %rax, %xmm1");

            assembly.generatePop(" %rax");
            assembly.generate("movq %rax, %xmm0");

            assembly.generate("call timesDouble");
            assembly.generate("movq %xmm0, %rax");

            assembly.generateComment("Times -- End line: " + n.line_number);

        }
    }

    // Exp e1,e2;
    // e1 is ArrayType
    // e2 is int
    // e1[e2]
    public void visit(ArrayLookup n) {
        Type.DataType leftDataType = SemanticsHelpers.expressionType(
                n.e1,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );
        assembly.generateComment("ArrayLookup -- Start line: " + n.line_number);
        assembly.generatePush("%rdi");

        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }

        n.e2.accept(this);
        assembly.generatePush("%rax");
        n.e1.accept(this);
        assembly.generatePush("%rax");

        assembly.generatePop(" %r10"); //r10 has e1 (pointer)
        assembly.generatePop(" %r11"); //r11 has e2 (index)


        assembly.generate("movq %r10, %rdi"); //jArray
        assembly.generate("movq %r11, %rsi"); //index

        if (leftDataType != null && leftDataType == Type.DataType.DOUBLEARRAY_DATATYPE) {
            assembly.generateCall(" doubleGetArrayElement");
        }
        else {
            assembly.generateCall(" getArrayElement");
        }

        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(argumentRegister);
        }

        assembly.generatePop(" %rdi");
        if (leftDataType != null && leftDataType == Type.DataType.DOUBLEARRAY_DATATYPE) {
            assembly.generate("movq %xmm0, %rax");
        }
        else {
            // rax already contains result
        }
        assembly.generateComment("ArrayLookup -- End line: " + n.line_number);
    }

    // Exp e;
    public void visit(ArrayLength n) {

        n.e.accept(this);

        assembly.generate("movq 0(%rax), %rax");
    }

    // Exp e;
    // Identifier i;
    // ExpList el;

    // a.Start();
    public void visit(Call n) {
        assembly.generateComment("Call -- Start line: " + n.line_number);

        assembly.generatePush("%rdi");
        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }
        n.e.accept(this);

        // e should contain this or object, so move it into rdi

        //n.i.accept(this);

        if (n.el.size() > 0) {
            assembly.generatePush("%rax");

            for (int i = 0; i < n.el.size(); i++) {
                n.el.get(i).accept(this);
                assembly.generatePush("%rax");
            }

            for (int i = n.el.size() - 1; i >= 0 ; i--) {
                // assume argument is in rax
                // move rax into the right argument register
                String argumentRegister = CodeGenHelper.argumentRegisterForIndex(i);
                assembly.generatePop(argumentRegister);
            }

            assembly.generatePop(" %rax");
        }

        assembly.generate("movq %rax, %rdi");
        ClassSymbolTable classSymbolTable = SemanticsHelpers.getClassForCall(n,
                        globalSymbolTable,
                        globalSymbolTable.table.get(currentClassName),
                        globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
                );
        int methodOffset = classSymbolTable.offsetForMethod(n.i.s, globalSymbolTable);

        // assume: rax already contains pointer to object
        assembly.generate("movq (%rax), %r10"); // pass this pointer
        assembly.generateCall("*" + methodOffset + "(%r10)");

        // Restore argument registers
        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(" " + argumentRegister);
        }

        assembly.generatePop(" %rdi");

       // assembly.generate();

        assembly.generateComment("Call -- End line: " + n.line_number);

    }

    // int i;
    public void visit(IntegerLiteral n) {
        assembly.generate("movq" + " $" + n.i + ", " + "%rax");
    }

    // double i;
    public void visit(DoubleLiteral n) {
        int doubleLabel = labelNumber;
        labelNumber++;

        assembly.generate(".data");
        assembly.generate("DOUBLE_" + doubleLabel + ": " + ".double" + " " + n.i);
        assembly.generate(".text");

        assembly.generate("movsd" + " " + "DOUBLE_" + doubleLabel + "(%rip), %xmm0");
        assembly.generate("movq %xmm0, %rax");
    }

    public void visit(True n) {
        assembly.generate("movq $1, %rax");
    }

    public void visit(False n) {
        assembly.generate("movq $0, %rax");
    }

    // String s;
    public void visit(IdentifierExp n) {
        assembly.generateComment("IdentifierExp -- Start line: " + n.line_number);
        MethodSymbolTable methodSymbolTable = globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName);

        Type t = SemanticsHelpers.getTypeForIdentifier(n.s,
                globalSymbolTable,
                globalSymbolTable.table.get(currentClassName),
                globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName)
        );

        if (t != null && t.location == Type.Location.LOCAL_LOCATION) {
            int offset = methodSymbolTable.getStackOffsetForVariable(n.s);
            assembly.generate("movq " + -offset + "(%rbp), %rax");
        }
        else if (t != null && t.location == Type.Location.CLASS_LOCATION) {
            int offset = globalSymbolTable.table.get(currentClassName).offsetForVariable(n.s, globalSymbolTable);
            assembly.generate("movq " + offset + "(%rdi)" + " , " + " %rax");
        }
        else if (t != null && t.location == Type.Location.ARGUMENT_LOCATION) {
            int argumentPosition = globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName).
                    getArgumentPositionForIdentifier(n.s);
            String registerForArgument = CodeGenHelper.argumentRegisterForIndex(argumentPosition);
            assembly.generate("movq " + registerForArgument + " , " + " %rax");
        }
        else {
            System.out.println("Unsupported location visit(IdentifierExp)");
        }
        assembly.generateComment("IdentifierExp -- End line: " + n.line_number);
    }

    public void visit(This n) {
        assembly.generate("movq %rdi, %rax");
    }

    // Exp e;
    public void visit(NewArray n) {

        assembly.generateComment("NewArray -- Start line: " + n.line_number);

        //  move all arguments into the stack
        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }

        assembly.generatePush("%rdi");
        n.e.accept(this);
        assembly.generatePush("%rax");


        assembly.generate("imulq $8, %rax");
        assembly.generate("addq $8, %rax");
        assembly.generate("movq %rax, %rdi"); // 8 * rax + 8. need an extra 8 bytes for array length
        assembly.generateCall("mjcalloc");

        assembly.generatePop(" %r10");
        assembly.generate("movq %r10, 0(%rax)");
        assembly.generatePop(" %rdi");

        // pop all arguments
        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(argumentRegister);
        }
        assembly.generateComment("NewArray -- End line: " + n.line_number);
    }

    public void visit(NewDoubleArray n) {
        assembly.generateComment("NewDoubleArray -- Start line: " + n.line_number);

        //  move all arguments into the stack
        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }

        assembly.generatePush("%rdi");
        n.e.accept(this);
        assembly.generatePush("%rax");


        assembly.generate("imulq $8, %rax");
        assembly.generate("addq $8, %rax");
        assembly.generate("movq %rax, %rdi"); // 8 * rax + 8. need an extra 8 bytes for array length
        assembly.generateCall("mjcalloc");

        assembly.generatePop(" %r10");
        assembly.generate("movq %r10, 0(%rax)");
        assembly.generatePop(" %rdi");

        // pop all arguments
        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(argumentRegister);
        }
        assembly.generateComment("NewDoubleArray -- End line: " + n.line_number);
    }

    // Identifier i;
    public void visit(NewObject n) {
        String objectType = n.i.s;
        int size = globalSymbolTable.table.get(objectType).bytesForClass(globalSymbolTable);

        assembly.generateComment("New Object -- Start line: " + n.line_number);
        assembly.generatePush("%rdi");

        List<String> argumentRegisters = CodeGenHelper.argumentRegisters(globalSymbolTable.table.get(currentClassName).methodsTable.get(currentMethodName));
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePush(argumentRegister);
        }

        assembly.generate("movq $" + size + " , " + "%rdi");
        assembly.generateCall(" mjcalloc");
        // rax now has pointer to malloc block
        assembly.generate("leaq " + objectType + "$$" + ", " + "%rdx"); // get address of method table
        assembly.generate("movq %rdx, 0(%rax)"); // store the method table in the malloc block

        Collections.reverse(argumentRegisters);
        for (String argumentRegister : argumentRegisters) {
            assembly.generatePop(" " + argumentRegister);
        }


        assembly.generatePop(" %rdi");
        assembly.generateComment("New Object -- end line: " + n.line_number);

    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);

        // Small trick to do not.
        // x = 1 - x will flip between 0 and 1
        // if x = 0; x = 1 - 0 = 1
        // if x = 1; x = 1 - 1 = 0

        assembly.generateComment("Not -- Start line: " + n.line_number);
        assembly.generatePush("%r10");
        assembly.generate("movq $1, %r10");
        assembly.generate("subq %rax, %r10");
        assembly.generate("movq %r10, %rax");
        assembly.generatePop(" %r10");
        assembly.generateComment("Not -- End line: " + n.line_number);

    }

    // String s;
    public void visit(Identifier n) {

    }
}
