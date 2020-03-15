package AST.Visitor.Assembly;

public class Assembly {
    private StringBuilder stringBuilder;

    private boolean shouldIndent = false;
    private int numPushed = 0;

    public Assembly() {
        this.stringBuilder = new StringBuilder();
    }

    public void generate(String s) {
        if (shouldIndent) {
            s = "    " + s;
        }
        stringBuilder.append(s);
        stringBuilder.append("\n");
    }

    public void generatePush(String register) {
        numPushed++;
        generate("pushq " + register);
    }

    public void generatePop(String register) {
        numPushed--;
        generate("popq " + register);
    }

    public void generateComment(String s) {
        generate("# " + s);
    }

    public String getCode() {
        return stringBuilder.toString();
    }

    public void generatePrologue(int stackSpace) {
        generateComment("Prologue start");
        generate("pushq %rbp"); // Push the old rbp onto the stack
        generate("movq %rsp, %rbp"); // Copy rsp into rbp (rsp will be be the new rbp)
        generate("subq" + " $" + stackSpace + ", " + "%rsp"); // create some stack space and move rsp down
        generateComment("Prologue end");
    }

    public void generatEpilogue() {
        generateComment("Epilogue start");
        generate("movq %rbp, %rsp"); // Copy rbp into rsp (Move rsp back up). RSP should now point to "old rbp"
        generate("popq %rbp"); // Copy "old rbp" into rbp
        generate("ret"); // return
        generateComment("Epilogue end");
    }

    public void generateGlobal(String globalLabel) {
        shouldIndent = false;
        generate(".global" + " " + labelName(globalLabel));
        shouldIndent = true;
    }

    public void generateCall(String label) {
        boolean isStackAligned = !(numPushed % 2 == 0);

        if (!isStackAligned) {
           generatePush("$0");
        }
        generate("call " + labelName(label));
        if (!isStackAligned) {
            generatePop("%r15");
        }
    }

    public void generateLabel(String label) {
        shouldIndent = false;
        generate(labelName(label) + ":");
        shouldIndent = true;
    }

    private String labelName(String label) {
        return label;

/*        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac os x");
        if (isMacOs)
        {
            return "_" + label;
        } else {
            return label;
        }*/
    }
}
