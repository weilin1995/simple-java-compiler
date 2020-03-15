package AST.Visitor.Python;

public class Python {
    private StringBuilder stringBuilder;

    private final String INDENT = "    "; // 4 spaces indent

    private int indentLevel;

    public Python() {
        this.stringBuilder = new StringBuilder();
    }

    public void generateStatement(String s) {
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append(INDENT);
        }
        stringBuilder.append(s);
        generateNewline();
    }

    public void generateWithIndent(String s) {
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append(INDENT);
        }
        stringBuilder.append(s);
    }

    public void generate(String s) {
        stringBuilder.append(s);
    }

    public void generateNewline() {
        stringBuilder.append("\n");
    }

    public void increaseIndent() {
        indentLevel++;
    }

    public void generateNewLineAndIndent() {
        increaseIndent();
        generateNewline();
    }

    public void decreaseIndent() {
        indentLevel--;
    }

    public String getCode() {
        return stringBuilder.toString();
    }
}
