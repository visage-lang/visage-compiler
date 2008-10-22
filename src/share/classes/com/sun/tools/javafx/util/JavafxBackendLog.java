/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.util;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javafx.main.JavafxCompiler;
import com.sun.tools.javafx.main.Main;
import com.sun.tools.javafx.tree.JavaPretty;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Certain errors from the back-end are internal errors; Indicate this.
 *
 * @author Robert Field
 */
public class JavafxBackendLog extends Log {

    final Context context;
    final Context fxContext;
    public Env<AttrContext> env;

    protected JavafxBackendLog(Context context, final Context fxContext) {
        super(context);
        this.context = context;
        this.fxContext = fxContext;
    }

    public static void preRegister(final Context context, final Context fxContext) {
        context.put(logKey, new Context.Factory<Log>() {

            public Log make() {
                return new JavafxBackendLog(context, fxContext);
            }
        });
    }

    private void errorPreface() {
        if (env != null) {  // Only add prefix where wanted
            JCTree tree = null;
            if (env.tree != null) {
                tree = env.tree;
            } else if (env.enclMethod != null) {
                tree = env.enclMethod;
            }

            StringWriter sw = new StringWriter();
            if (tree != null) {
                try {
                    try {
                        new JavaPretty(sw, false, fxContext).printExpr(tree);
                    } finally {
                        sw.close();
                    }
                } catch (Throwable ex) {
                }
            }

            Log fxLog = Log.instance(fxContext);
            fxLog.note(
                    MsgSym.MESSAGE_JAVAFX_NOTE_INTERNAL_ERROR,
                    JavafxCompiler.version(),
                    sw.toString());
        }
    }

    /** Report an error, unless another error was already reported at same
     *  source position.
     *  @param pos    The source position at which to report the error.
     *  @param key    The key for the localized error message.
     *  @param args   Fields of the error message.
     */
    public void error(DiagnosticPosition pos, String key, Object ... args) {
        errorPreface();
        super.error(pos, key, args);
    }

    /** Report an error, unless another error was already reported at same
     *  source position.
     *  @param pos    The source position at which to report the error.
     *  @param key    The key for the localized error message.
     *  @param args   Fields of the error message.
     */
    public void error(int pos, String key, Object ... args) {
        errorPreface();
        super.error(pos, key, args);
    }
}
