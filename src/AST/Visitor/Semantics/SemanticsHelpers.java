package AST.Visitor.Semantics;

import AST.*;

import java.util.List;
import java.util.Map;

public class SemanticsHelpers {

    public static boolean isTypeDefinedInGlobalSymbolTable(Type t, GlobalSymbolTable globalSymbolTable) {
        if (t.dataType == Type.DataType.REFERENCE_DATATYPE && !globalSymbolTable.table.containsKey(t.typeName)) {
           return false;
        }
        return true;
    }


    public static Type getTypeForIdentifier(String identifier, GlobalSymbolTable globalSymbolTable, ClassSymbolTable classSymbolTable, MethodSymbolTable methodSymbolTable) {

        // Method symbol could be null if we are not in a method
        if (methodSymbolTable != null) {
            for (int i = 0; i < methodSymbolTable.variableDeclarations.size(); i++) {
                // check the method symble methodsTable first
                Type cur = methodSymbolTable.variableDeclarations.get(i);
                if (identifier.equals(cur.identifier)) {
                    return cur;
                }
            }

            // check methods arguments
            for (int i = 0; i < methodSymbolTable.arguments.size(); i++) {
                // check the method symbol methodsTable first
                Type arg = methodSymbolTable.arguments.get(i);
                if (identifier.equals(arg.identifier)) {
                    return arg;
                }
            }
        }

        for (int i = 0; i < classSymbolTable.variableDeclarations.size(); i++) {
            // check the method symbol methodsTable first
            Type var = classSymbolTable.variableDeclarations.get(i);
            if (identifier.equals(var.identifier)) {
                return var;
            }
        }

        // Check parents recursively
        if (classSymbolTable.hasExtends) {
            ClassSymbolTable extendsClassSymbolTable = globalSymbolTable.table.get(classSymbolTable.extendsName.typeName);
            return getTypeForIdentifier(identifier, globalSymbolTable, extendsClassSymbolTable, null);
        }

        return null;
    }

    public static Type.DataType expressionType (Exp exp1,
                                                Exp exp2,
                                                GlobalSymbolTable globalSymbolTable,
                                                ClassSymbolTable classSymbolTable,
                                                MethodSymbolTable methodSymbolTable) {

        Type.DataType t1 = expressionType(exp1, globalSymbolTable, classSymbolTable, methodSymbolTable);
        Type.DataType t2 = expressionType(exp2, globalSymbolTable, classSymbolTable, methodSymbolTable);

        if (t1 == t2) {
            return t1;
        }
        else {
            return null;
        }
    }

    public static Type.DataType expressionType (Exp exp,
                                                GlobalSymbolTable globalSymbolTable,
                                                ClassSymbolTable classSymbolTable,
                                                MethodSymbolTable methodSymbolTable) {

        if (exp instanceof Plus || exp instanceof Minus || exp instanceof Times) {
            Exp e1;
            Exp e2;

            if (exp instanceof Plus) {
                e1 = ((Plus) exp).e1;
                e2 = ((Plus) exp).e2;
            }
            else if (exp instanceof Minus) {
                e1 = ((Minus) exp).e1;
                e2 = ((Minus) exp).e2;
            }
            else {
                e1 = ((Times) exp).e1;
                e2 = ((Times) exp).e2;
            }


            Type.DataType e1DataType = expressionType(e1, globalSymbolTable, classSymbolTable, methodSymbolTable);
            Type.DataType e2DataType = expressionType(e2, globalSymbolTable, classSymbolTable, methodSymbolTable);

            if (e1DataType == Type.DataType.DOUBLE_DATATYPE && e2DataType == Type.DataType.DOUBLE_DATATYPE) {
               return Type.DataType.DOUBLE_DATATYPE;
            }
            else {
                return Type.DataType.INT_DATATYPE;
            }
        } else if (exp instanceof And) {
            return Type.DataType.BOOLEAN_DATYPE;
        } else if (exp instanceof Sqrt) {
            return Type.DataType.DOUBLE_DATATYPE;
        } else if (exp instanceof LessThan) {
            return Type.DataType.BOOLEAN_DATYPE;
        } else if (exp instanceof ArrayLookup) {
            ArrayLookup arrayLookupExp = (ArrayLookup)exp;
            Type.DataType e1DataType = expressionType(arrayLookupExp.e1, globalSymbolTable, classSymbolTable, methodSymbolTable);

            if (e1DataType == Type.DataType.DOUBLEARRAY_DATATYPE) {
                return Type.DataType.DOUBLE_DATATYPE;
            }
            else {
                return Type.DataType.INT_DATATYPE;
            }
        } else if (exp instanceof ArrayLength) {
            return Type.DataType.INT_DATATYPE;
        } else if (exp instanceof Call) {
            Type callType = returnTypeForCall((Call) exp, globalSymbolTable, classSymbolTable, methodSymbolTable);
            return callType == null ? null : callType.dataType;
        } else if (exp instanceof Not) {
            return Type.DataType.BOOLEAN_DATYPE;
        } else if (exp instanceof IntegerLiteral) {
            return Type.DataType.INT_DATATYPE;
        } else if (exp instanceof DoubleLiteral) {
            return Type.DataType.DOUBLE_DATATYPE;
        } else if (exp instanceof True) {
            return Type.DataType.BOOLEAN_DATYPE;
        } else if (exp instanceof False) {
            return Type.DataType.BOOLEAN_DATYPE;
        } else if (exp instanceof IdentifierExp) {
            Type t = getTypeForIdentifier(((IdentifierExp) exp).s, globalSymbolTable, classSymbolTable, methodSymbolTable);
            return t == null ? null : t.dataType;
        } else if (exp instanceof This) {
            return Type.DataType.REFERENCE_DATATYPE;
        } else if (exp instanceof NewArray) {
            return Type.DataType.INTARRAY_DATATYPE;
        } else if (exp instanceof NewDoubleArray) {
            return Type.DataType.DOUBLEARRAY_DATATYPE;
        } else if (exp instanceof NewObject) {
            return Type.DataType.REFERENCE_DATATYPE;
        }
        else {
            System.out.println("Unknown exp in expressionType()");
        }
        return null;
    }

    public static SemanticError checkCallValid(Call call,
                                         GlobalSymbolTable globalSymbolTable,
                                         ClassSymbolTable classSymbolTable,
                                         MethodSymbolTable methodSymbolTable) {

        Type callType = SemanticsHelpers.returnTypeForCall(
                call,
                globalSymbolTable,
                classSymbolTable,
                methodSymbolTable
        );

        if (callType != null) {
            return null;
        }
        return new SemanticError("[ERROR] with method call on line " + call.line_number);
    }

    public static SemanticError checkIdentifierExpDefined(IdentifierExp identifierExp,
                                                    GlobalSymbolTable globalSymbolTable,
                                                    ClassSymbolTable classSymbolTable,
                                                    MethodSymbolTable methodSymbolTable) {

        Type t = getTypeForIdentifier(identifierExp.s, globalSymbolTable, classSymbolTable, methodSymbolTable);

        if (t != null) {
            return null;
        }

        return new SemanticError("Identifier " + identifierExp.s + " not defined " + " on line: " + identifierExp.line_number);
    }

    public static SemanticError checkExpressionTypeEqualTo(Exp exp,
                                                           GlobalSymbolTable globalSymbolTable, ClassSymbolTable classSymbolTable, MethodSymbolTable methodSymbolTable, Type.DataType ...expectedTypes) {

        Type.DataType actualDataType = SemanticsHelpers.expressionType(exp, globalSymbolTable, classSymbolTable, methodSymbolTable);

        boolean foundMatch = false;
        for (Type.DataType expectedType : expectedTypes) {
            if (actualDataType == expectedType) {
                foundMatch = true;
            }
        }

        if (foundMatch) {
            return null;
        }

        StringBuilder expectedTypeNames = new StringBuilder();
        for (Type.DataType expectedType : expectedTypes) {
            expectedTypeNames.append(expectedType);
        }

        return new SemanticError("[ERROR]" + " Wrong Type for " + exp.getClass().getSimpleName() + " in line " + exp.line_number +
                ". Expected " + expectedTypeNames.toString() + " but got " + actualDataType);
    }

    public static SemanticError checkExpressionTypeEqualTo(Exp exp1,
                                                           Exp exp2,
                                                           GlobalSymbolTable globalSymbolTable, ClassSymbolTable classSymbolTable, MethodSymbolTable methodSymbolTable, Type.DataType expectedType) {

        Type.DataType actualDataType1 = SemanticsHelpers.expressionType(exp1, globalSymbolTable, classSymbolTable, methodSymbolTable);
        Type.DataType actualDataType2 = SemanticsHelpers.expressionType(exp2, globalSymbolTable, classSymbolTable, methodSymbolTable);

        if (expectedType == actualDataType1 && expectedType == actualDataType2) {
            return null;
        }

        return new SemanticError("[ERROR]" + " Expected both " + exp1.getClass() + " and " + exp2.getClass()
                + " to be of matching type: " + expectedType + " but exp1 was " + actualDataType1 + " and exp2 was " + actualDataType2);

    }

    public static SemanticError checkIdentifierInGlobalSymbolTable(String identifier, GlobalSymbolTable globalSymbolTable) {
        if (globalSymbolTable.table.containsKey(identifier)) {
            return null;
        }
        return new SemanticError("[ERROR] " + identifier + " not defined");
    }

    public static SemanticError checkIdentifierTypeEqualTo(Identifier identifier,
                                                     Type.DataType expectedType,
                                                     GlobalSymbolTable globalSymbolTable,
                                                     ClassSymbolTable classSymbolTable,
                                                     MethodSymbolTable methodSymbolTable) {

        Type t = getTypeForIdentifier(identifier.s, globalSymbolTable, classSymbolTable, methodSymbolTable);
        if (t == null) {
            return new SemanticError("[ERROR] Identifier " + identifier + " not defined" + " in line " + identifier.line_number);
        }

        if (t.dataType != expectedType) {
            return new SemanticError("[ERROR]" + " Wrong Type for " + identifier.getClass().getSimpleName() + " in line " + identifier.line_number +
                    ". Expected " + expectedType + " but got " + t.typeName);
        }
        return null;
    }

    // Method symbol table is the table NOT for the call but for the outer method
    public static Type returnTypeForCall(Call call,
                             GlobalSymbolTable globalSymbolTable,
                             ClassSymbolTable classSymbolTable,
                             MethodSymbolTable methodSymbolTable) {
        Identifier methodName = call.i;
        ClassSymbolTable leftSideClassSymbolTable = getClassForCall(call, globalSymbolTable, classSymbolTable, methodSymbolTable);
        if (leftSideClassSymbolTable == null) {
            return null;
        }

        // Get the methodTable. If the class was x.y(arg), methodTable is the table for method y
        MethodSymbolTable methodTable = leftSideClassSymbolTable.methodsTable.get(methodName.s);

        // Now let's start checking that the arguments for this method match

        // Different number of arguments.
        if (call.el.size() != methodTable.arguments.size()) {
            return null;
        }

        // Let's look at the type of each argument in the call.
        for(int i = 0; i < methodTable.arguments.size(); i++) {
            Exp argumentExp = call.el.get(i);
            Type expectedType = methodTable.arguments.get(i);

            if (!isExpAssignableToType(argumentExp, expectedType, globalSymbolTable, classSymbolTable, methodSymbolTable)) {
/*                System.out.println("[ERROR] Can't assign argument to expected type " + expectedType.typeName + " in line " + argumentExp.line_number);*/
                return null;
            }

        }

        // Everything we checked seemed ok, finally, lets lookup the return type for the method and return it.
        return leftSideClassSymbolTable.methodsTable.get(methodName.s).returnType;
    }

    public static ClassSymbolTable getClassForCall(Call call, GlobalSymbolTable globalSymbolTable, ClassSymbolTable classSymbolTable, MethodSymbolTable methodSymbolTable) {
        Exp leftSide = call.e;
        Identifier methodName = call.i;

        // First let's get the type on the left side
        Type leftSideType;
        if (leftSide instanceof This) {
            // case for this.method(arg)
            leftSideType = classSymbolTable.className;
        } else {
            if (leftSide instanceof IdentifierExp) {
                // x.method(arg) case
                leftSideType = getTypeForIdentifier(((IdentifierExp) leftSide).s, globalSymbolTable, classSymbolTable, methodSymbolTable);
            } else if (leftSide instanceof Call) {
                // x.y.method(arg) case
                leftSideType = returnTypeForCall((Call) leftSide, globalSymbolTable, classSymbolTable, methodSymbolTable);
            } else if (leftSide instanceof NewObject){
                String className = ((NewObject) leftSide).i.s;
                if (!globalSymbolTable.table.containsKey(className)) {
                    return null;
                }
                leftSideType = globalSymbolTable.table.get(className).className;
            } else {
                System.out.println("Unknown type for call, can't find left side");
                return null;
            }
        }

        // left side type could be null, for example an identifier that does not exist
        if (leftSideType == null) {
            return null;
        }

        // Now that we have the type on left side type use it to get the class on the left side.
        String leftSideClassName = leftSideType.typeName;
        ClassSymbolTable leftSideClassSymbolTable;
        if (!globalSymbolTable.table.containsKey(leftSideClassName)) {
            return null;
        }
        leftSideClassSymbolTable = globalSymbolTable.table.get(leftSideClassName);

        // Check if the leftSide class has this method with this name (could be a parent class so check parents if needed)
        ClassSymbolTable classSymbolTableWithMethod = getClassSymbolTableForMethodName(globalSymbolTable, leftSideClassSymbolTable, methodName.s);
        if (classSymbolTableWithMethod == null) {
            return null;
        }

        return classSymbolTableWithMethod;
    }

    private static ClassSymbolTable getClassSymbolTableForMethodName(GlobalSymbolTable globalSymbolTable, ClassSymbolTable classSymbolTable, String methodName) {
        if (classSymbolTable.methodsTable.containsKey(methodName)) {
            return classSymbolTable;
        }

        if (classSymbolTable.hasExtends) {
            ClassSymbolTable parentSymbolTable = globalSymbolTable.table.get(classSymbolTable.extendsName.typeName);
            return getClassSymbolTableForMethodName(globalSymbolTable, parentSymbolTable, methodName);
        }
        return null;
    }


    public static boolean isExpAssignableToType(Exp exp,
                                                Type expectedType,
                                                GlobalSymbolTable globalSymbolTable,
                                                ClassSymbolTable classSymbolTable,
                                                MethodSymbolTable methodSymbolTable) {
        if (expectedType == null) {
            return false;
        }

        Type.DataType argumentType = expressionType(exp, globalSymbolTable, classSymbolTable, methodSymbolTable);

        // If we have a basic type, (not reference type) the check is simple.
        if (argumentType != Type.DataType.REFERENCE_DATATYPE) {
            return expectedType.dataType == argumentType;
        }

        // Deal with reference type argument

        String argumentClassName = "";
        if (exp instanceof This) {
            argumentClassName = classSymbolTable.className.typeName;
        } else if (exp instanceof NewObject) {
            argumentClassName = ((NewObject) exp).i.s;
        } else if (exp instanceof IdentifierExp) {
            Type idType = getTypeForIdentifier(((IdentifierExp) exp).s, globalSymbolTable, classSymbolTable, methodSymbolTable);
            if (idType == null) {
                return false;
            }
            argumentClassName = idType.typeName;
        } else if (exp instanceof Call) {
            Type callType = returnTypeForCall((Call) exp, globalSymbolTable, classSymbolTable, methodSymbolTable);
            if (callType == null) {
                return false;
            }
            argumentClassName = callType.typeName;
        } else {
            System.out.println("Should not happen");
            System.exit(0);
        }

        // We have the className of the argument

        if (!globalSymbolTable.table.containsKey(argumentClassName)) {
            //return false;
        }

        Type argumentClassType = globalSymbolTable.table.get(argumentClassName).className;


        ClassSymbolTable expectedClass = globalSymbolTable.table.get(expectedType.typeName);

        // Corner case
        if (expectedClass == null) {
            return false;
        }

        // Check if the expected class is a parent of the argumentClassType
        return expectedClass.isParentOfLeaf(argumentClassType, globalSymbolTable);
    }

    public static boolean hasCycleInGlobalSymbolTable(GlobalSymbolTable globalSymbolTable) {
        for (Map.Entry<String, ClassSymbolTable> item : globalSymbolTable.table.entrySet()) {
            String className = item.getKey();
            ClassSymbolTable classSymbolTable = item.getValue();

            if (classSymbolTable.hasCycle(globalSymbolTable)) {
                return true;
            }
        }

       return false;
    }

    public static boolean hasDuplicateName(String className, String methodName, String varName, GlobalSymbolTable globalSymbolTable) {


        if(methodName.isEmpty()) {
            // checking the duplicate class name
            if (varName.isEmpty()){
                if (hasDuplicateNameInStringList(globalSymbolTable.classNames, className)) {
                    return true;
                }
            } else {
                ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(className);

                if(hasDuplicateNameInTypeList(classSymbolTable.variableDeclarations, varName)) {
                    return true;
                }
            }

        } else {
            ClassSymbolTable classTable;
            if (globalSymbolTable.table.containsKey(className)) {
                classTable = globalSymbolTable.table.get(className);

            } else {
                System.out.print("THIS SHOULD NOT HAPPEN!");
                return true;
            }

            if (varName.isEmpty()) {
                // check the duplicate method name by looping through all the methods names

                if(hasDuplicateNameInStringList(classTable.methodNames, methodName)) {
                    return true;
                }
            } else {
                // check the duplicate for variable
                // first check the class var
                List<Type> classVars = classTable.variableDeclarations;

                if (hasDuplicateNameInTypeList(classVars, varName)) {
                    return true;
                }

                // then check in the method
                if (!methodName.equals("main")) {
                    MethodSymbolTable methodTable;
                    if (classTable.methodsTable.containsKey(methodName)) {
                        methodTable = classTable.methodsTable.get(methodName);
                    } else {
                        System.out.println("SHOULD NOT HAPPEN!" + "methodName is: " + methodName);
                        return true;
                    }

                    //first check the arguments
                    List<Type> methodArgs = methodTable.arguments;
                    if (hasDuplicateNameInTypeList(methodArgs, varName)) {
                        return true;
                    }

                    // check if variable name matches that of an argument in the method
                    for (Type t : methodArgs) {
                        if (t.identifier.equals(varName)) {
                            return true;
                        }
                    }

                    //then check the var
                    List<Type> methodVars = methodTable.variableDeclarations;
                    if (hasDuplicateNameInTypeList(methodVars, varName)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private static boolean hasDuplicateNameInTypeList(List<Type> list, String name) {
        boolean seen = false;
        for(int i = 0; i < list.size(); i++) {
            Type arg = list.get(i);
            if (arg.identifier.equals(name)) {
                if (seen) {
                    return true;
                } else {
                    seen = true;
                }
            }
        }
        return false;
    }

    private static boolean hasDuplicateNameInStringList(List<String> list, String name) {
        boolean seen = false;
        for(int i = 0; i < list.size(); i++) {
            String cur = list.get(i);
            if (cur.equals(name)) {
                if (seen) {
                    return  true;
                } else {
                    seen = true;
                }
            }
        }
        return false;
    }
}
