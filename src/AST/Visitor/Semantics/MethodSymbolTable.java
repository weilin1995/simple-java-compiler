package AST.Visitor.Semantics;

import java.util.ArrayList;
import java.util.List;

public class MethodSymbolTable {
    public Type returnType;
    public String name;
    public List<Type> variableDeclarations;
    public List<Type> arguments;


    private List<Binding> bindingList;

    public MethodSymbolTable(Type returnType, String name) {
        this.returnType = returnType;
        this.name = name;
        this.arguments = new ArrayList<>();
        this.variableDeclarations = new ArrayList<>();
    }
    public void addArgumentType(Type t, GlobalSymbolTable globalSymbolTable, ErrorChecker errorChecker) {
        if (!SemanticsHelpers.isTypeDefinedInGlobalSymbolTable(t, globalSymbolTable)) {
            errorChecker.addSemanticError(new SemanticError("[ERROR]" + " Argument Type " + t.typeName + " undefined"));
        }
        arguments.add(t);
    }

    public void addVarType(Type t, GlobalSymbolTable globalSymbolTable, ErrorChecker errorChecker) {
        if (!SemanticsHelpers.isTypeDefinedInGlobalSymbolTable(t, globalSymbolTable)) {
            errorChecker.addSemanticError(new SemanticError("[ERROR]" + " Var Type " + t.typeName + " undefined"));
        }
        variableDeclarations.add(t);
    }

    public int getStackOffsetForVariable(String identifier) {
        for (int i = 0; i < variableDeclarations.size(); i++) {
            Type t = variableDeclarations.get(i);
            if (t.identifier.equals(identifier)) {
                return (i + 1) * 8;
            }
        }
        System.out.println("SHOULD NOT HAPPEN; can't find stack offset");
        return -1;
    }

    public int getArgumentPositionForIdentifier(String identifier) {
        for (int i = 0; i < arguments.size(); i++) {
            Type t = arguments.get(i);
            if (t.identifier.equals(identifier)) {
                return i;
            }
        }
        System.out.println("SHOULD NOT HAPPEN; can't find argument position");
        return -1;
    }

    public int getArgumentPositionForType(Type t) {
        for (int i = 0; i < arguments.size(); i++) {
            Type currentT = arguments.get(i);
            if (currentT.equals(t)) {
                return i;
            }
        }
        return -1;
    }

}
