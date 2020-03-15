package AST.Visitor.Semantics;

import java.util.*;

public class ClassSymbolTable {
    public boolean hasExtends;
    public Type className;
    public Type extendsName;
    public List<Type> variableDeclarations;

    // Maps Method Name -> Method Symbol Table
    public HashMap<String, MethodSymbolTable> methodsTable;

    public List<String> methodNames;

    public ClassSymbolTable(boolean hasExtends, Type className, Type extendsName) {
        this.hasExtends = hasExtends;
        this.className = className;
        this.extendsName = extendsName;
        this.methodsTable = new HashMap<>();
        this.variableDeclarations = new ArrayList<>();
        this.methodNames = new ArrayList<String>();
    }

    public void addVariableDeclaration(Type t, GlobalSymbolTable globalSymbolTable) {
        if (!SemanticsHelpers.isTypeDefinedInGlobalSymbolTable(t, globalSymbolTable)) {
            System.out.println("[ERROR]" + " Var Type " + t.typeName + " undefined");
        }
        variableDeclarations.add(t);
    }

    public boolean isParentOfLeaf(Type leaf, GlobalSymbolTable globalSymbolTable) {

        if (leaf.typeName.equals(this.className.typeName)) {
            return true;
        }

        ClassSymbolTable leafClassTable = globalSymbolTable.table.get(leaf.typeName);
        if (!leafClassTable.hasExtends) {
            return false;
        }

        return isParentOfLeaf(leafClassTable.extendsName, globalSymbolTable);
    }

    public boolean hasCycle(GlobalSymbolTable globalSymbolTable) {
        if (!this.hasExtends) {
            return false;
        }

        return hasCycleHelper(globalSymbolTable, this.className, this.extendsName);
    }

    private boolean hasCycleHelper(GlobalSymbolTable globalSymbolTable, Type startClassType, Type currentClassType) {
        ClassSymbolTable classSymbolTable = globalSymbolTable.table.get(currentClassType.typeName);

        // reached the root
        if (!classSymbolTable.hasExtends) {
            return false;
        }

        if (currentClassType.typeName.equals(startClassType.typeName)) {
            return true;
        }

        return hasCycleHelper(globalSymbolTable, startClassType, classSymbolTable.extendsName);
    }


    /***
     * Generates a list of all methods used for table creation.
     *
     *
     * We want
     * -----
     * baseClassMethod1
     * baseClassMethod2
     * -----
     * ...
     * ...
     * -----
     * thisClassMethod1
     * thisClassMethod2
     *
     *
     */

    public List<String> getMethodsInClassAndSuperClass(GlobalSymbolTable globalSymbolTable) {
        List<String> res = new ArrayList<>();
        if (hasExtends) {
            ClassSymbolTable parentSymbolTable = globalSymbolTable.table.get(this.extendsName.typeName);
            res.addAll(parentSymbolTable.getMethodsInClassAndSuperClass(globalSymbolTable));
        }

        for (String s : this.getSortedMethods()) {
            String fancyName = "$" + s + "$";
            String fullName = this.className.typeName + fancyName;

            int index = listHasStringWithSubstring(res, fancyName);
            if (index == -1) {
                res.add(fullName);
            }
            else {
               res.set(index, fullName);
            }
        }

        return res;
    }

    private static int listHasStringWithSubstring(List<String> list, String substring) {
        for (int i = 0 ; i < list.size(); i++) {
            String s = list.get(i);
            if (s.contains(substring)) {
                return i;
            }
        }
        return -1;
    }

    private List<String> getSortedMethods() {
        Collections.sort(this.methodNames);
        return this.methodNames;
    }

    public int bytesForClass(GlobalSymbolTable globalSymbolTable) {
        int bytes = 0;
        if (hasExtends) {
            ClassSymbolTable parentSymbolTable = globalSymbolTable.table.get(this.extendsName.typeName);
            bytes += (parentSymbolTable.bytesForClass(globalSymbolTable));
        }

        bytes += (variableDeclarations.size() * 8);
        if (!hasExtends) {
            bytes += 8; // for the vtable ptr
        }
        return bytes;
    }

    public int offsetForMethod(String methodName, GlobalSymbolTable globalSymbolTable) {
        if (!methodsTable.containsKey(methodName)) {
            System.out.println("SHOULD NOT HAPPEN!");
            return -1;
        }

        List<String> allMethods = getMethodsInClassAndSuperClass(globalSymbolTable);
        String fancyName = "$" + methodName + "$";
        int index = listHasStringWithSubstring(allMethods, fancyName);
        if (index == -1) {
            System.out.println("Can't find offset for method " + methodName);
        }
        return (index + 1) * 8;
    }

    public int offsetForVariable(String identifier, GlobalSymbolTable globalSymbolTable) {
        List<String> variableIdentifiers = new ArrayList<>();
        List<Type> allVariableTypes = getVariablesInClassAndSuperClass(globalSymbolTable);

        for (Type t : allVariableTypes) {
            variableIdentifiers.add(t.identifier);
        }
        // Collections.sort(variableIdentifiers);

        for (int i = 0; i < variableIdentifiers.size(); i++) {
            String varIdentifier = variableIdentifiers.get(i);
            if (varIdentifier.equals(identifier)) {
                return (i + 1) * 8;
            }
        }

        System.out.println("SHOULD NOT HAPPEN, Can't find offset for class variable");
        return -1;
    }

    public List<Type> getVariablesInClassAndSuperClass(GlobalSymbolTable globalSymbolTable) {
        List<Type> res = new ArrayList<>();
        if (hasExtends) {
            ClassSymbolTable parentSymbolTable = globalSymbolTable.table.get(this.extendsName.typeName);
            res.addAll(parentSymbolTable.getVariablesInClassAndSuperClass(globalSymbolTable));
        }
        res.addAll(variableDeclarations);
        return res;
    }
}

