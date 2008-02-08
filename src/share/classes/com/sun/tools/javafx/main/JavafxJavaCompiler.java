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

package com.sun.tools.javafx.main;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.*;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.*;
import java.io.IOException;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

/**
 *
 * @author Robert
 */
public class JavafxJavaCompiler extends JavaCompiler {
    /** The context key for the compiler. */
    protected static final Context.Key<JavafxJavaCompiler> javafxJavaCompilerKey =
        new Context.Key<JavafxJavaCompiler>();
    
    List<JCCompilationUnit> modules; // Current compilation

    /** Get the JavaCompiler instance for this context. */
    public static JavafxJavaCompiler instance(Context context) {
        JavafxJavaCompiler instance = context.get(javafxJavaCompilerKey);
        if (instance == null)
            instance = new JavafxJavaCompiler(context);
        return instance;
    }

    protected JavafxJavaCompiler(Context context) {
        super(context);
    }

    @Override
    public void initProcessAnnotations(Iterable<? extends Processor> arg0) {
        // JavaFX Script doesn't support annotations
    }

    public void backEnd(List<JCCompilationUnit> externalModules, ListBuffer<JavaFileObject> results) throws IOException {
        modules = externalModules;
        this.results = results;
        compile(null, List.<String>nil(), null);
        results = null;
    }
    
    public Name.Table getNames() {
        return names;
    }
    
    /**
     * Override of JavaCompiler.generate() to catch list of generated class files.
     * Do not call directly.
     */
    @Override
    public void generate(List<Pair<Env<AttrContext>, JCClassDecl>> list) {
        generate(list, results);
    }
    ListBuffer<JavaFileObject> results = null;

    @Override
    public List<JCCompilationUnit> parseFiles(List<JavaFileObject> fileObjects) throws IOException {
        return modules;
    }
}
