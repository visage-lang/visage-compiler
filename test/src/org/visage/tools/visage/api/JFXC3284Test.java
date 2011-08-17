/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
package org.visage.tools.api;

import org.visage.api.JavafxcTask;
import org.visage.api.tree.JavaFXTreePathScanner;
import org.visage.api.tree.JavaFXTreePath;
import org.visage.api.tree.ReturnTree;
import org.visage.api.tree.Tree.JavaFXKind;
import org.visage.api.tree.UnitTree;
import org.visage.api.tree.SourcePositions;

import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests JavaFXTreePathScanner.visitReturn() works correctly
 * 
 * @author Anton Chechel
 * @author A. Sundararajan 
 *             * removed unwanted stuff 
 *             * changed test src path
 *             * changed package
 */
public class JFXC3284Test {
    @Test
    public void testVisitReturn() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/org/visage/tools/api/ReturnTest.visage");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file);
            JavafxcTask visageTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends UnitTree> treeList = visageTask.analyze();

            JavafxcTrees trees = JavafxcTrees.instance(visageTask);
            SourcePositions sp = trees.getSourcePositions();
            UnitTree unit = treeList.iterator().next();

            DetectorVisitor d = new DetectorVisitor(trees, sp, unit);
            d.scan(treeList, null);

            Assert.assertEquals(d.getRetCounter(), 4);

        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

    class DetectorVisitor<Void, EnumSet> extends JavaFXTreePathScanner<Void, EnumSet> {

        JavafxcTrees trees;
        SourcePositions sp;
        UnitTree unit;
        int retCounter;

        DetectorVisitor(JavafxcTrees trees, SourcePositions sp, UnitTree unit) {
            this.trees = trees;
            this.sp = sp;
            this.unit = unit;
        }

        @Override
        public Void visitReturn(ReturnTree tree, EnumSet p) {
            retCounter++;
            
            JavaFXTreePath path = trees.getPath(unit, tree);
            Assert.assertNotNull(path);

            JavaFXKind kind = tree.getJavaFXKind();
            Assert.assertEquals(kind, JavaFXKind.RETURN);

            return null;
        }

        public int getRetCounter() {
            return retCounter;
        }
    }
}
