
package com.sun.tools.javafx.preview;

import java.util.LinkedList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;

public class DiagnosticCollector implements DiagnosticListener {

    List<Diagnostic> diagnostics = new LinkedList<Diagnostic>();

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public void clear() {
        diagnostics.clear();
    }

    public void report(Diagnostic diagnostic) {
        diagnostics.add(diagnostic);
    }
}

