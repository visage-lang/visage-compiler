/*
 * Copyright 2003-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.JavafxcTool;
import com.sun.javafx.api.JavafxcTrees;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.util.SourcePositions;
import java.io.File;
import java.util.List;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Customer-supplied test for JFXC-746 issue.
 */
public class JFXC746Test {
    private static final String testSrc = System.getProperty("test.src.dir", "test/src");

    @Test
    public void testJFXC746() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            /* The javac library uses the context classloader to load the
             * javac implementation. In a NetBeans module, it needs to
             * be loaded by the module's classloader to make sure that the
             * version of javac this compiler requires takes precedence
             * over the JDK's version.
             */
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/Test.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file);
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            List<? extends CompilationUnitTree> treeList = (List)javafxTask.parse();
            assertTrue(treeList.size() == 1);

            SourcePositions sp = JavafxcTrees.instance(javafxTask).getSourcePositions();
            CompilationUnitTree tree = treeList.iterator().next();
            ExpressionTree pkg = tree.getPackageName();
            long start = sp.getStartPosition(tree, pkg);
            long end = sp.getEndPosition(tree, pkg);
            String pkgName = pkg.toString();
            assertTrue(end - start + 1 == pkgName.length());
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }
}
