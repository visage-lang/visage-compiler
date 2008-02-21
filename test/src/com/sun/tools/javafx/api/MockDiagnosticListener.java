package com.sun.tools.javafx.api;

import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;

/**
 * Saves diagnostics for later test reference.
 * 
 * @author tball
 */
class MockDiagnosticListener<T> implements DiagnosticListener<T> {

    public void report(Diagnostic<? extends T> d) {
        diagCodes.add(d.getCode());
        System.err.println(d);
    }
    public List<String> diagCodes = new ArrayList<String>();

    public int errors() {
        return diagCodes.size();
    }
}
