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

package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.tree.JavaFXTreePathScanner;
import com.sun.javafx.api.tree.UnitTree;
import javax.lang.model.element.Element;

import com.sun.javafx.api.tree.Tree;
import com.sun.javafx.api.tree.VariableTree;
import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;

/**
 * Tests that JavafxcTrees.getElement can be called from visitor
 * 
 * @author Robert Field
 */
public class JFXC1828Test {

    @Test
    public void testJFXTreesGetElement() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/JFXC1828.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends UnitTree> treeList = javafxTask.analyze();
            
            JavafxcTrees trees = JavafxcTrees.instance(javafxTask);
            VarScanner d = new VarScanner(trees);
            d.scan(treeList, null);
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

class VarScanner<EnumSet> extends JavaFXTreePathScanner<Void,EnumSet> {
    JavafxcTrees trees;
    
    VarScanner(JavafxcTrees trees) {
        this.trees = trees;
    }
    
    @Override
    public Void visitVariable(VariableTree tree, EnumSet p) {                
        Element e = trees.getElement(getCurrentPath());
        Tree t = trees.getTree(e);
        return null;
    }
}
}
