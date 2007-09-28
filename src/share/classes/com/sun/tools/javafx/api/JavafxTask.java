/**
 * @(#)JavacTask.java	1.5 06/06/25
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Use and Distribution is subject to the Java Research License available
 * at <http://wwws.sun.com/software/communitysource/jrl.html>.
 */

package com.sun.tools.javafx.api;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TaskListener;
import com.sun.tools.javafx.api.JavafxCompiler.CompilationTask;
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
public abstract class JavafxTask implements CompilationTask {

    /**
     * Parse the specified files returning a list of abstract syntax trees.
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends CompilationUnitTree> parse()
        throws IOException;

    /**
     * Complete all analysis prior to conversion of the JavaFX Script compiler 
     * AST to Javac AST.  
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends CompilationUnitTree> analyze() throws IOException;

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
    public abstract void setTaskListener(TaskListener taskListener);

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
