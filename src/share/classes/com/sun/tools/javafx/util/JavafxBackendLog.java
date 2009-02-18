/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.javafx.util;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
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
    private boolean dumpOccurred;

    protected JavafxBackendLog(Context context, final Context fxContext) {
        super(context);
        this.context = context;
        this.fxContext = fxContext;
        this.dumpOccurred = false; // Only once
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
                Options options = Options.instance(context);
                String dumpOnFail = options.get("DumpOnFail");
                if (!dumpOccurred && (dumpOnFail == null || !dumpOnFail.toLowerCase().startsWith("n"))) {
                    try {
                        try {
                            new JavaPretty(sw, false, fxContext).printExpr(tree);
                        } finally {
                            sw.close();
                        }
                    } catch (Throwable ex) {
                    }
                    dumpOccurred = true;
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
