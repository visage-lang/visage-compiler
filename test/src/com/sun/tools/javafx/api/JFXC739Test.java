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
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javafx.tree.JFXClassDeclaration;
import com.sun.tools.javafx.tree.JavafxTreeScanner;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tball
 */
public class JFXC739Test {

    @Test
    public void testJFXClassDeclarationPos() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/Point.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            //Iterable<? extends CompilationUnitTree> treeList = javafxTask.parse();
            Iterator<? extends CompilationUnitTree> treeList = javafxTask.analyze().iterator();
            assertTrue(treeList.hasNext());
            
            // scan classes
            final int[] classes = new int[1];
            new JavafxTreeScanner() {
                @Override
                public void visitClassDeclaration(JFXClassDeclaration that) {
                    super.visitClassDeclaration(that);
                    classes[0]++;
                    assertTrue(that.pos().getStartPosition() >= 0);
                    assertTrue(that.pos >= 0);
                    assertTrue(that.pos().getStartPosition() == that.pos);
                }
            }.scan((JCTree)treeList.next());
            assertTrue(classes[0] == 2);
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
        
    }
}