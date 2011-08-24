/*
 * Copyright 2003-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import org.visage.api.tree.VisageTreeScanner;
import org.visage.api.tree.UnitTree;
import org.visage.api.tree.VariableTree;
import org.visage.api.tree.SourcePositions;

import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests that JavafxcTrees.getElement can be called from visitor
 *
 * @author Michael Chernyshov
 */
public class JFXC3525Test {

    @Test
    public void testJFXTreesGetElement() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/org/visage/tools/api/JFXC3525.visage");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file);
            JavafxcTask visageTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends UnitTree> treeList = visageTask.analyze();

            JavafxcTrees trees = JavafxcTrees.instance(visageTask);
            SourcePositions positions = trees.getSourcePositions();
            UnitTree unit = treeList.iterator().next();

            TreeFinder d = new TreeFinder(unit, positions);
            d.scan(treeList, null);

        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }



    static class TreeFinder extends VisageTreeScanner<Void,Object> {
        UnitTree unit;
        SourcePositions positions;

        TreeFinder(UnitTree unit, SourcePositions positions) {
            this.unit = unit;
            this.positions = positions;
        }

        @Override
            public Void visitVariable(VariableTree node, Object p) {
                if (node.getName().contentEquals("selector")) {
                    assertTrue(positions.getEndPosition(unit, node) - positions.getStartPosition(unit, node) == 8); // the selector variable should not have zero length!
                }
                return super.visitVariable(node, p);
            }
    }
}
