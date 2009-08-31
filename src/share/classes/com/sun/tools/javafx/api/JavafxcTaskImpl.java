/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxTaskEvent;
import com.sun.javafx.api.JavafxTaskListener;
import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.tree.Tree;
import com.sun.javafx.api.tree.UnitTree;
import com.sun.tools.mjavac.model.JavacElements;
import com.sun.tools.mjavac.model.JavacTypes;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.util.ClientCodeException;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Options;
import com.sun.tools.javafx.comp.JavafxAttrContext;
import com.sun.tools.javafx.comp.JavafxEnv;
import com.sun.tools.javafx.main.CommandLine;
import com.sun.tools.javafx.main.Main;
import com.sun.tools.javafx.tree.JFXScript;
import com.sun.tools.javafx.tree.JFXTree;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

/**
 * JavaFX Script implementation of a JavaFX compilation task, based on
 * JavacTaskImpl.  This class extends JavafxTask to isolate the internal
 * javafx and javac compiler API from the public API.
 *
 * @see com.sun.tools.mjavac.api.JavacTaskImpl
 * @author tball
 */
public class JavafxcTaskImpl extends JavafxcTask {

    public Main compilerMain;
    private com.sun.tools.javafx.main.JavafxCompiler compiler;
    private String[] args;
    private Context context;
    private List<JavaFileObject> fileObjects;
    private Map<JavaFileObject, JFXScript> notYetEntered;
    private List<JFXScript> units;
    private JavafxTaskListener taskListener;
    private AtomicBoolean used = new AtomicBoolean();
    private Integer result = null;
    private List<JavafxEnv<JavafxAttrContext>> genList;
    boolean preserveSymbols;
    Scope namedImportScope;
    Scope starImportScope;

    JavafxcTaskImpl(JavafxcTool tool, Main compilerMain, String[] args, Context context, List<JavaFileObject> fileObjects) {
        this.compilerMain = compilerMain;
        this.args = args;
        this.context = context;
        this.fileObjects = fileObjects;
        // null checks
        compilerMain.getClass();
        args.getClass();
        context.getClass();
        fileObjects.getClass();

        Options optionTable = Options.instance(context);
        optionTable.put("-Xjcov", "-Xjcov");  // generate tree end positions
    }

    JavafxcTaskImpl(JavafxcTool tool,
                Main compilerMain,
                Iterable<String> flags,
                Context context,
                Iterable<? extends JavaFileObject> fileObjects) {
        this(tool, compilerMain, toArray(flags), context, toList(fileObjects));
    }

    static private String[] toArray(Iterable<String> flags) {
        ListBuffer<String> result = new ListBuffer<String>();
        if (flags != null)
            for (String flag : flags)
                result.append(flag);
        return result.toArray(new String[result.length()]);
    }

    static private List<JavaFileObject> toList(Iterable<? extends JavaFileObject> fileObjects) {
        if (fileObjects == null)
            return List.nil();
        ListBuffer<JavaFileObject> result = new ListBuffer<JavaFileObject>();
        for (JavaFileObject fo : fileObjects)
            result.append(fo);
        return result.toList();
    }

    public Boolean call() {
        if (!used.getAndSet(true)) {
            beginContext();
            try {
                result = compilerMain.compile(args, context,
                       namedImportScope, starImportScope, preserveSymbols, fileObjects);

            } finally {
                endContext();
            }
            compilerMain = null;
            args = null;
            context = null;
            fileObjects = null;
            return result == 0;
        } else {
            throw new IllegalStateException("multiple calls to method 'call'");
        }
    }
    private boolean compilationInProgress = false;

    private void prepareCompiler() throws IOException {
        if (!used.getAndSet(true)) {
            beginContext();
            compilerMain.registerServices(context, args);
            compilerMain.setOptions(Options.instance(context));
            compilerMain.filenames = new ListBuffer<File>();
            List<File> filenames = compilerMain.processArgs(CommandLine.parse(args));
            if (!filenames.isEmpty())
                throw new IllegalArgumentException("Malformed arguments " + filenames.toString(" "));
            compiler = com.sun.tools.javafx.main.JavafxCompiler.instance(context);
            compiler.keepComments = true;
            notYetEntered = new HashMap<JavaFileObject, JFXScript>();
            for (JavaFileObject file: fileObjects)
                notYetEntered.put(file, null);
            args = null;
            genList = List.<JavafxEnv<JavafxAttrContext>>nil();
        }
    }

    private void beginContext() {
        context.put(JavafxcTaskImpl.class, this);
        if (context.get(JavafxTaskListener.class) != null) {
            context.put(JavafxTaskListener.class, (JavafxTaskListener) null);
        }
        if (taskListener != null) {
            context.put(JavafxTaskListener.class, wrap(taskListener));
        }
        if (compilationInProgress) {
            throw new IllegalStateException("Compilation in progress");
        }
        compilationInProgress = true;
    }

    private JavafxTaskListener wrap(final JavafxTaskListener tl) {
        tl.getClass(); // null check
        return new JavafxTaskListener() {

            public void started(JavafxTaskEvent e) {
                try {
                    tl.started(e);
                } catch (Throwable t) {
                    throw new ClientCodeException(t);
                }
            }

            public void finished(JavafxTaskEvent e) {
                try {
                    tl.finished(e);
                } catch (Throwable t) {
                    throw new ClientCodeException(t);
                }
            }
        };
    }

    private void endContext() {
        compilationInProgress = false;
    }

    public Iterable<? extends UnitTree> parse() throws IOException {
        try {
            prepareCompiler();
            units = compiler.parseFiles(fileObjects);
            for (JFXScript unit: units) {
                JavaFileObject file = unit.getSourceFile();
                if (notYetEntered.containsKey(file))
                    notYetEntered.put(file, unit);
            }
            return units;
        }
        finally {
            parsed = true;
            if (compiler != null && compiler.log != null)
                compiler.log.flush();
        }
    }

    private boolean parsed = false;

    void enter() throws IOException {
        prepareCompiler();

        ListBuffer<JFXScript> roots = null;

        if (notYetEntered.size() > 0) {
            if (!parsed)
                parse();
            for (JavaFileObject file: fileObjects) {
                JFXScript unit = notYetEntered.remove(file);
                if (unit != null) {
                    if (roots == null)
                        roots = new ListBuffer<JFXScript>();
                    roots.append(unit);
                }
            }
            notYetEntered.clear();
        }

        if (roots != null)
            try {
                compiler.enterTrees(roots.toList());
            }
            finally {
                if (compiler != null && compiler.log != null)
                    compiler.log.flush();
            }
    }

    public Iterable<? extends UnitTree> analyze() throws IOException {
        try {
            enter();
            genList = genList.appendList(compiler.attribute());
            return units;
        } finally {
            if (compiler != null && compiler.log != null)
                compiler.log.flush();
        }
    }
    
    public int errorCheck() throws IOException {
        try {
            enter();
            compiler.errorCheck();
        } finally {
            if (compiler != null && compiler.log != null)
                compiler.log.flush();
        }
        return compiler.errorCount();
    }

    public Iterable<? extends JavaFileObject> generate() throws IOException {
        analyze();
        final ListBuffer<JavaFileObject> results = new ListBuffer<JavaFileObject>();
        compiler.generate(genList, results);
        return results;
    }
    
    public void setTaskListener(JavafxTaskListener taskListener) {
        this.taskListener = taskListener;
    }

    public TypeMirror getTypeMirror(Iterable<? extends Tree> path) {
        if (path == null)
            return null;
        Tree last = null;
        for (Tree node : path) {
            last = node;
        }
        return ((JFXTree) last).type;
    }

    public JavacElements getElements() {
        if (context == null) {
            throw new IllegalStateException();
        }
        return JavacElements.instance(context);
    }

    public JavacTypes getTypes() {
        if (context == null) {
            throw new IllegalStateException();
        }
        return JavacTypes.instance(context);
    }

    /**
     * For internal use by Sun Microsystems only.  This method will be
     * removed without warning.
     */
    public Context getContext() {
        return context;
    }

    public void setPreserveSymbols(Scope namedImportScope, Scope starImportScope, boolean preserve) {
        this.preserveSymbols = preserve;
        this.namedImportScope = namedImportScope;
        this.starImportScope = starImportScope;
    }
}
