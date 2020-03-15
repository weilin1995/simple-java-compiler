package AST.Visitor.Semantics;

import AST.*;

import java.util.Objects;

public class Type {
    public Type(DataType dataType, String typeName, String identifier, Location location) {
        // Tree x = ..; dataType is REFERENCE DATATYPE, typeName is Tree, id is x
        this.dataType = dataType;
        this.typeName = typeName;
        this.identifier = identifier;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return dataType == type.dataType &&
                Objects.equals(typeName, type.typeName) &&
                Objects.equals(identifier, type.identifier) &&
                location == type.location;
    }

    @Override
    public int hashCode() {

        return Objects.hash(dataType, typeName, identifier, location);
    }

    public enum DataType {
        INT_DATATYPE, // int
        DOUBLE_DATATYPE, // double
        BOOLEAN_DATYPE, // boolean
        INTARRAY_DATATYPE, // int[]
        DOUBLEARRAY_DATATYPE, // double[]
        REFERENCE_DATATYPE, // reference to another class (or itself possibly)
        CLASS_DATATYPE, // first instance of a class (should only be in global symbol methodsTable)
        INVALID_DATATYPE
    }

    public enum Location {
        LOCAL_LOCATION,
        CLASS_LOCATION,
        ARGUMENT_LOCATION,
        OTHER_LOCATION
    }

    public DataType dataType;
    public String typeName;
    public String identifier; // some things don't have identifiers (like the name of a class)
    public Location location;

    public static DataType getDataType(AST.Type astType) {
        if (astType instanceof IdentifierType) {
            return DataType.REFERENCE_DATATYPE;
        }
        else if (astType instanceof BooleanType) {
            return DataType.BOOLEAN_DATYPE;
        }
        else if (astType instanceof IntArrayType) {
            return DataType.INTARRAY_DATATYPE;
        }
        else if (astType instanceof DoubleArrayType) {
            return DataType.DOUBLEARRAY_DATATYPE;
        }
        else if (astType instanceof IntegerType) {
            return DataType.INT_DATATYPE;
        }
        else if (astType instanceof DoubleType) {
            return DataType.DOUBLE_DATATYPE;
        }
        else {
            System.out.println("invalid type");
            System.exit(0);
            return DataType.INVALID_DATATYPE;
        }
    }

    public static String getTypeString(AST.Type astType) {
        if (astType instanceof IdentifierType) {
            IdentifierType identifierType = (IdentifierType)astType;
            return identifierType.s;
        }
        else if (astType instanceof BooleanType) {
            return "BOOL";
        }
        else if (astType instanceof IntArrayType) {
            return "INTARRAY";
        }
        else if (astType instanceof DoubleArrayType) {
            return "DOUBLEARRAY";
        }
        else if (astType instanceof IntegerType) {
            return "INT";
        }
        else if (astType instanceof DoubleType) {
            return "DOUBLE";
        }
        else {
            System.out.println("invalid type");
            System.exit(0);
            return "UNKNOWN";
        }
    }
}
