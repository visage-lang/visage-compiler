/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.tools.javafx.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.tools.*;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javafx.main.JavafxOption;
import com.sun.tools.javafx.main.Main;
import com.sun.tools.javafx.main.RecognizedOptions.GrumpyHelper;
import com.sun.tools.javafx.main.RecognizedOptions;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Locale;

/**
 * The Tool API implementation for the JavaFX Script compiler, based on the
 * javac compiler tool implementation.
 *
 * @author Tom Ball
 */
public final class JavafxcTool implements JavafxCompiler {
    private final Context dummyContext = new Context();

    private final PrintWriter silent = new PrintWriter(new OutputStream(){
        @Override
        public void write(int b) {}
    });

    private final Main sharedCompiler = new Main("javafx", silent);
    {
        sharedCompiler.setOptions(Options.instance(dummyContext));
    }

    /**
     * Static factory method for creating new instances of this tool.
     * @return new instance of this tool
     */
    public static JavafxcTool create() {
        return new JavafxcTool();
    }

    @Override
    public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
        if (err == null)
            err = System.err;
        for (String argument : arguments)
            argument.getClass(); // null check
        return com.sun.tools.javafx.Main.compile(arguments, new PrintWriter(err, true));
    }

    @Override
    public Set<SourceVersion> getSourceVersions() {
        return Collections.unmodifiableSet(EnumSet.range(SourceVersion.RELEASE_3,
                                                         SourceVersion.latest()));
    }

    @Override
    public JavacFileManager getStandardFileManager(
        DiagnosticListener<? super JavaFileObject> diagnosticListener,
        Locale locale,
        Charset charset) {
        Context context = new Context();
        if (diagnosticListener != null)
            context.put(DiagnosticListener.class, diagnosticListener);
        context.put(Log.outKey, new PrintWriter(System.err, true)); // FIXME
        return new JavacFileManager(context, true, charset);
    }

    @Override
    public JavafxcTask getTask(Writer out,
                             JavaFileManager fileManager,
                             DiagnosticListener<? super JavaFileObject> diagnosticListener,
                             Iterable<String> options,
                             Iterable<? extends JavaFileObject> compilationUnits)
    {
        final String kindMsg = "All compilation units must be of SOURCE kind";
        if (options != null)
            for (String option : options)
                option.getClass(); // null check
        if (compilationUnits != null) {
            for (JavaFileObject cu : compilationUnits) {
                if (cu.getKind() != JavaFileObject.Kind.SOURCE) // implicit null check
                    throw new IllegalArgumentException(kindMsg);
            }
        }

        Context context = new Context();

        if (diagnosticListener != null)
            context.put(DiagnosticListener.class, diagnosticListener);

        if (out == null)
            context.put(Log.outKey, new PrintWriter(System.err, true));
        else
            context.put(Log.outKey, new PrintWriter(out, true));

        if (fileManager == null)
            fileManager = getStandardFileManager(diagnosticListener, null, null);
        context.put(JavaFileManager.class, fileManager);
        processOptions(context, fileManager, options);
        Main compiler = new Main("javacTask", context.get(Log.outKey));
        return new JavafxcTaskImpl(this, compiler, options, context, compilationUnits);
    }

    private static void processOptions(Context context,
                                       JavaFileManager fileManager,
                                       Iterable<String> options)
    {
        if (options == null)
            return;

        Options optionTable = Options.instance(context);

        JavafxOption[] recognizedOptions =
            RecognizedOptions.getJavacToolOptions(new GrumpyHelper());
        Iterator<String> flags = options.iterator();
        while (flags.hasNext()) {
            String flag = flags.next();
            int j;
            for (j=0; j<recognizedOptions.length; j++)
                if (recognizedOptions[j].matches(flag))
                    break;

            if (j == recognizedOptions.length) {
                if (fileManager.handleOption(flag, flags)) {
                    continue;
                } else {
                    String msg = Main.getLocalizedString("err.invalid.flag", flag);
                    throw new IllegalArgumentException(msg);
                }
            }

            JavafxOption option = recognizedOptions[j];
            if (option.hasArg()) {
                if (!flags.hasNext()) {
                    String msg = Main.getLocalizedString("err.req.arg", flag);
                    throw new IllegalArgumentException(msg);
                }
                String operand = flags.next();
                if (option.process(optionTable, flag, operand))
                    // should not happen as the GrumpyHelper will throw exceptions
                    // in case of errors
                    throw new IllegalArgumentException(flag + " " + operand);
            } else {
                if (option.process(optionTable, flag))
                    // should not happen as the GrumpyHelper will throw exceptions
                    // in case of errors
                    throw new IllegalArgumentException(flag);
            }
        }
    }

    @Override
    public int isSupportedOption(String option) {
        JavafxOption[] recognizedOptions =
            RecognizedOptions.getJavacToolOptions(new GrumpyHelper());
        for (JavafxOption o : recognizedOptions) {
            if (o.matches(option))
                return o.hasArg() ? 1 : 0;
        }
        return -1;
    }
}
