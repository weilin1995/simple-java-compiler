package AST.Visitor.Semantics;

import java.util.ArrayList;
import java.util.List;

public class ErrorChecker {
    private List<SemanticError> semanticErrorList;

    public ErrorChecker() {
        semanticErrorList = new ArrayList<>();
    }

    public void addSemanticError(SemanticError error) {
        if (error != null) {
            semanticErrorList.add(error);
            System.out.println(error.errorMessage);
        }
    }

    public boolean hasError() {
        return semanticErrorList.size() > 0;
    }
}
