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
import org.visage.api.tree.JavaFXTreeScanner;
import org.visage.api.tree.ParenthesizedTree;
import org.visage.api.tree.UnitTree;

import com.sun.tools.mjavac.util.List;
import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests that the 'preverveTrees' flag actually preserves Parens AST nodes during parsing
 *
 * @author mcimadamore
 */
public class JFXC3528Test {

    @Test
    public void testPreserveParens() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/org/visage/tools/api/JFXC3528.visage");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file);
            JavafxcTask visageTask = tool.getTask(null, fileManager, dl, List.<String>of("-XDpreserveTrees"), fileObjects);
            Iterable<? extends UnitTree> treeList = visageTask.analyze();

            JavafxcTrees trees = JavafxcTrees.instance(visageTask);
            UnitTree unit = treeList.iterator().next();

            ParensFinder pf = new ParensFinder();
            pf.scan(treeList, null);

            assertEquals(true, pf.foundParens);
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

    static class ParensFinder extends JavaFXTreeScanner<Void,Object> {

        boolean foundParens = false;

        @Override
        public Void visitParenthesized(ParenthesizedTree node, Object p) {
            foundParens = true;
            return super.visitParenthesized(node, p);
        }
    }
}
