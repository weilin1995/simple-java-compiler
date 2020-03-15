package AST.Visitor.Assembly;

import AST.Visitor.Semantics.ClassSymbolTable;
import AST.Visitor.Semantics.GlobalSymbolTable;
import AST.Visitor.Semantics.MethodSymbolTable;
import AST.Visitor.Semantics.Type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeGenHelper {

    public enum Instruction {
        MOVE("moveq    "),
        CALL("call    "),
        PUSH("pushq    "),
        SUB("subq    "),
        POP("popq    "),
        JG("jg    "),
        JMP("jmp    "),
        RET("ret    "),
        IMUL("imulq    "),
        CMP("cmpq    ");

        String ins;

        Instruction(String ins) {
            this.ins = ins;
        }

        public String toString() {
            return this.ins;
        }
    }

    public enum Register {
        RAX("$rax"),
        RDI("$rdi");
        private String reg;

        Register(String reg) {
            this.reg = reg;
        }

        public String toString() {
            return this.reg;
        }
    }

    public static String argumentRegisterForIndex(int index) {
        // RDI always will have this
        /*

        %rax result register; also used in idiv and
imul instructions.
%rbx yes miscellaneous register
%rcx fourth argument register
%rdx third argument register; also used in
idiv and imul instructions.
%rsp stack pointer
%rbp yes frame pointer
%rsi second argument register
%rdi first argument register
%r8 fifth argument register
%r9 sixth argument register
%r10 miscellaneous register
%r11 miscellaneous register
%r12-%r15 yes miscellaneous register
         */

        switch (index) {
            case 0:
                return "%rsi";
            case 1:
                return "%rdx";
            case 2:
                return "%rcx";
            case 3:
                return "%r8";
            case 4:
                return "%r9";
            default:
                return "#Too many arguments";
        }
    }

    public static List<String> argumentRegisters(MethodSymbolTable methodSymbolTable) {
        int numArguments = methodSymbolTable.arguments.size();

        List<String> res = new ArrayList<>();
        for (int i = 0; i < numArguments; i++) {
            res.add(argumentRegisterForIndex(i));
        }
        return res;
    }

    public static List<String> getAllargumentRegisters() {

        List<String> res = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            res.add(argumentRegisterForIndex(i));
        }
        return res;
    }
}
