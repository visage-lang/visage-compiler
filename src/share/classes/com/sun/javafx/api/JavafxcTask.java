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

package com.sun.javafx.api;

import com.sun.javafx.api.tree.UnitTree;
import com.sun.javafx.api.tree.Tree;
import com.sun.javafx.api.JavafxCompiler.CompilationTask;
import java.io.IOException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

/**
 * Provides access to functionality specific to the JavaFX Script compiler,
 * based on JavacTask.
 *
 * @see com.sun.source.util.JavacTask
 * @author Tom Ball
 */
public abstract class JavafxcTask implements CompilationTask {

    /**
     * Parse the specified files returning a list of abstract syntax trees.
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends UnitTree> parse()
        throws IOException;
    
    /**
     * Check the specified files for errors.
     * 
     * @return the number of compilation errors found
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract int errorCheck() throws IOException;

    /**
     * Complete all analysis prior to conversion of the JavaFX Script compiler 
     * AST to Javac AST.  
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends UnitTree> analyze() throws IOException;

    /**
     * Generate code.
     *
     * @return a list of files that were generated
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends JavaFileObject> generate() throws IOException;

    /**
     * The specified listener will receive events describing the progress of
     * this compilation task.
     */
    public abstract void setTaskListener(JavafxTaskListener taskListener);

    /**
     * Get a type mirror of the tree node determined by the specified path.
     */
    public abstract TypeMirror getTypeMirror(Iterable<? extends Tree> path);
    /**
     * Get a utility object for dealing with program elements.
     */
    public abstract Elements getElements();

    /**
     * Get a utility object for dealing with type mirrors.
     */
    public abstract Types getTypes();
}
