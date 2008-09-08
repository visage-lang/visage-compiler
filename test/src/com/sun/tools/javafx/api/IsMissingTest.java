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
import com.sun.javafx.api.tree.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test isMissing
 * 
 * @author Robert Field
 */
public class IsMissingTest {
    Map<String,Tree> testTrees = new HashMap<String, Tree>();

    @Test
    public void testJFXTreesGetElement() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MyDiagnosticListener<? super FileObject> dl = new MyDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/IsMissing.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends UnitTree> treeList = javafxTask.analyze();
            
            TreeFinder d = new TreeFinder();
            d.scan(treeList, null);
            assertTrue("expected one missing identifier, got: " + d.missingCount, d.missingCount == 1);
            assertTrue("expected two errors", dl.errors() == 2); // no id, and id not found
            
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }
    
    static class TreeFinder extends JavaFXTreeScanner<Void,Object> {
        int missingCount = 0;

        @Override
        public Void scan(Tree node, Object p) {
            if (node != null) {
                //System.err.println("node " + node + ": missing:" + node.isMissing());
                assertFalse("node " + node + " should not be missing", node.isMissing());
            }
            return super.scan(node, p);
        }

        TreeFinder() {
        }

        @Override
        public Void visitIndexof(IndexofTree node, Object p) {
            IdentifierTree id = node.getForVarIdentifier();
            //System.err.println("indexof " + id + ", missing: " + id.isMissing());
            if (id.isMissing())
                ++missingCount;
            return null;
        }
    }

    // from MockDiagnosticListener
    class MyDiagnosticListener<T> implements DiagnosticListener<T> {

        public List<String> diagCodes = new ArrayList<String>();

        public void report(Diagnostic<? extends T> d) {
            diagCodes.add(d.getCode());
            //System.err.println("diagnostic: " + d);
        }

        public int errors() {
            return diagCodes.size();
        }
    }
}
