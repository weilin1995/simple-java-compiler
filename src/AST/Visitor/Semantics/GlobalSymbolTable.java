package AST.Visitor.Semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalSymbolTable {

    // Maps Class Name -> Class Symbol Table
    public HashMap<String, ClassSymbolTable> table;

    public List<String> classNames;

    public GlobalSymbolTable() {
        this.table = new HashMap<>();
        this.classNames = new ArrayList<String>();
    }
}
