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

import org.visage.api.tree.MemberSelectTree;
import org.visage.api.tree.VariableTree;
import org.visage.tools.api.*;
import org.visage.api.VisagecTask;
import org.visage.api.tree.VisageTreePath;
import org.visage.api.tree.VisageTreePathScanner;
import org.visage.api.tree.Tree;
import org.visage.api.tree.Tree.VisageKind;

import org.visage.api.tree.Tree.VisageKind;
import org.visage.api.tree.UnitTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Type;
import org.visage.tools.comp.VisageAttrContext;
import org.visage.tools.comp.VisageEnv;
import org.visage.tools.comp.VisageResolve;
import org.visage.tools.tree.VisageClassDeclaration;
import org.visage.tools.tree.VisageVar;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test makes sure that the AllTrees.visage file contains all tree constructs
 * from org.visage.api.tree.Tree.VisageKind.values().
 * 
 * @author David Strupl
 */
public class VSGC2324Test {
    private static final String testSrc = System.getProperty("test.src.dir", "test/sandbox");
    private static final String testClasses = System.getProperty("build.test.classes.dir");
    private static VisagecTask task;
    private VariableTree node;
    private VariableTree a;

    /**
     * Make sure we are able to at least analyze the test file. In other words it
     * should be compilable but this test mimics what the NetBeans editor does
     * with source files (not full compile).
     */
    @Test
    public void analyzeTest() throws Exception {
        VisagecTool instance = new VisagecTool();
        MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
        dl.printErrors = false;
        StandardJavaFileManager fm = instance.getStandardFileManager(dl, null, null);
        List<String> options = 
                Arrays.asList("-d", ".", "-sourcepath", testSrc, "-classpath", testClasses);
        File file = new File(testSrc + "/org/visage/tools/api", "VSGC2324.visage");
        Iterable<? extends JavaFileObject> files = fm.getJavaFileObjects(file);
        task = instance.getTask(null, fm, dl, null, files);
        assertNotNull("no task returned", task);
        Iterable<? extends UnitTree> result1 = task.parse();
        assertTrue("no compilation units returned", result1.iterator().hasNext());
        Iterable<? extends UnitTree> result2 = task.analyze();
        assertTrue("errors found", dl.errors() > 0);
    }
}
