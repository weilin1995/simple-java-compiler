package AST.Visitor.Semantics;

public class Binding {
    // A binding is something like x = 5; ; (binds 5 to x)
    // TODO: flesh this out
    public Type LHS;
    public Type RHS;

    public Binding(Type LHS, Type RHS) {
        this.LHS = LHS;
        this.RHS = RHS;
    }
}
