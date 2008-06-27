/*
 * Copyright 2003-2008 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.javafx.api.tree.JavaFXTreeScanner;
import com.sun.javafx.api.tree.UnitTree;
import com.sun.javafx.api.tree.IdentifierTree;
import com.sun.javafx.api.tree.MemberSelectTree;
import com.sun.javafx.api.tree.Tree;
import com.sun.javafx.api.tree.SourcePositions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
public class JFXC1138Test {
    Map<String,Tree> testTrees = new HashMap<String, Tree>();

    @Test
    public void testJFXTreesGetElement() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/JFXC1138.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends UnitTree> treeList = javafxTask.analyze();
            
            JavafxcTrees trees = JavafxcTrees.instance(javafxTask);
            SourcePositions sp = trees.getSourcePositions();
            UnitTree unit = treeList.iterator().next();
            
            TreeFinder d = new TreeFinder(unit, testTrees);
            d.scan(treeList, null);
            
            Tree t = testTrees.get("java.lang.Double.POSITIVE_INFINITY");
            testPositions(t, sp, unit, 0, 34);
            t = testTrees.get("java.lang.Double");
            testPositions(t, sp, unit, 0, 16);
            t = testTrees.get("java.lang");
            testPositions(t, sp, unit, 0, 9);
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }
    
    private void testPositions(Tree tree, SourcePositions sp, UnitTree unit, int start, int end) {
        assertNotNull(tree);
        assertEquals(start, sp.getStartPosition(unit, tree));
        assertEquals(end, sp.getEndPosition(unit, tree));
    }

    static class TreeFinder extends JavaFXTreeScanner<Void,Object> {
        UnitTree unit;
        Map<String,Tree> trees;

        TreeFinder(UnitTree unit, Map<String,Tree>trees) {
            this.unit = unit;
            this.trees = trees;
        }

        @Override
        public Void visitIdentifier(IdentifierTree node, Object p) {
            trees.put(node.toString(), node);
            return super.visitIdentifier(node, p);
        }

        @Override
        public Void visitMemberSelect(MemberSelectTree node, Object p) {
            trees.put(node.toString(), node);
            return super.visitMemberSelect(node, p);
        }
    }
}
