/*
 * @(#)JavacTool.java	1.12 06/06/25
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Use and Distribution is subject to the Java Research License available
 * at <http://www.sun.com/software/communitysource/jrl.html>.
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
public final class JavafxTool implements JavafxCompiler {
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
    public static JavafxTool create() {
        return new JavafxTool();
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

    public JavafxTaskImpl getTask(Writer out,
                             JavaFileManager fileManager,
                             DiagnosticListener<? super JavaFileObject> diagnosticListener,
                             Iterable<String> options,
                             Iterable<String> classes,
                             Iterable<? extends JavaFileObject> compilationUnits)
    {
        final String kindMsg = "All compilation units must be of SOURCE kind";
        if (options != null)
            for (String option : options)
                option.getClass(); // null check
        if (classes != null) {
            for (String cls : classes)
                if (!SourceVersion.isName(cls)) // implicit null check
                    throw new IllegalArgumentException("Not a valid class name: " + cls);
        }
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
        return new JavafxTaskImpl(this, compiler, options, context, classes, compilationUnits);
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
