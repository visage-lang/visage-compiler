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

import com.sun.javafx.api.*;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.model.JavacTypes;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.ClientCodeException;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javafx.main.CommandLine;
import com.sun.tools.javafx.main.Main;
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
 * @see com.sun.tools.javac.api.JavacTaskImpl
 * @author tball
 */
public class JavafxcTaskImpl extends JavafxcTask {

    private Main compilerMain;
    private com.sun.tools.javafx.main.JavafxCompiler compiler;
    private String[] args;
    private Context context;
    private List<JavaFileObject> fileObjects;
    private Map<JavaFileObject, JCCompilationUnit> notYetEntered;
    private List<JCCompilationUnit> units;
    private TaskListener taskListener;
    private AtomicBoolean used = new AtomicBoolean();
    private Integer result = null;

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

    @Override
    public Boolean call() {
        if (!used.getAndSet(true)) {
            beginContext();
            try {
                result = compilerMain.compile(args, context, fileObjects);
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
            notYetEntered = new HashMap<JavaFileObject, JCCompilationUnit>();
            for (JavaFileObject file: fileObjects)
                notYetEntered.put(file, null);
            args = null;
        }
    }

    private void beginContext() {
        context.put(JavafxcTaskImpl.class, this);
        if (context.get(TaskListener.class) != null) {
            context.put(TaskListener.class, (TaskListener) null);
        }
        if (taskListener != null) {
            context.put(TaskListener.class, wrap(taskListener));
        }
        if (compilationInProgress) {
            throw new IllegalStateException("Compilation in progress");
        }
        compilationInProgress = true;
    }

    private TaskListener wrap(final TaskListener tl) {
        tl.getClass(); // null check
        return new TaskListener() {

            public void started(TaskEvent e) {
                try {
                    tl.started(e);
                } catch (Throwable t) {
                    throw new ClientCodeException(t);
                }
            }

            public void finished(TaskEvent e) {
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

    public Iterable<? extends CompilationUnitTree> parse() throws IOException {
        try {
            prepareCompiler();
            units = compiler.parseFiles(fileObjects);
            for (JCCompilationUnit unit: units) {
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

        ListBuffer<JCCompilationUnit> roots = null;

        if (notYetEntered.size() > 0) {
            if (!parsed)
                parse();
            for (JavaFileObject file: fileObjects) {
                JCCompilationUnit unit = notYetEntered.remove(file);
                if (unit != null) {
                    if (roots == null)
                        roots = new ListBuffer<JCCompilationUnit>();
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

    @Override
    public Iterable<? extends CompilationUnitTree> analyze() throws IOException {
        try {
            enter();
            compiler.attribute();
            return units;
        } finally {
            if (compiler != null && compiler.log != null)
                compiler.log.flush();
        }
    }
    
    @Override
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

    @Override
    public Iterable<? extends JavaFileObject> generate() throws IOException {
        analyze();
        final ListBuffer<JavaFileObject> results = new ListBuffer<JavaFileObject>();
        compiler.generate(results);
        return results;
    }
    
    @Override
    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    public TypeMirror getTypeMirror(Iterable<? extends Tree> path) {
        if (path == null)
            return null;
        Tree last = null;
        for (Tree node : path) {
            last = node;
        }
        return ((JCTree) last).type;
    }

    @Override
    public JavacElements getElements() {
        if (context == null) {
            throw new IllegalStateException();
        }
        return JavacElements.instance(context);
    }

    @Override
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
}