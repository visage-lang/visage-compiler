/*
 * Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
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
public class VSGC2054Test {
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
    public void isAccessibleTest() throws Exception {
        VisagecTool instance = new VisagecTool();
        MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
        StandardJavaFileManager fm = instance.getStandardFileManager(dl, null, null);
        List<String> options = 
                Arrays.asList("-d", ".", "-sourcepath", testSrc, "-classpath", testClasses);
        File file = new File(testSrc + "/org/visage/tools/api", "VSGC2054.visage");
        Iterable<? extends JavaFileObject> files = fm.getJavaFileObjects(file);
        task = instance.getTask(null, fm, dl, null, files);
        assertNotNull("no task returned", task);
        Iterable<? extends UnitTree> result1 = task.parse();
        assertEquals("parse error(s)", 0, dl.errors());
        assertTrue("no compilation units returned", result1.iterator().hasNext());
        Iterable<? extends UnitTree> result2 = task.analyze();
        assertTrue("no compilation units returned", result2.iterator().hasNext());
        UnitTree t = result2.iterator().next();
        Visitor v = new Visitor();
        v.scan(t, t);
        assertNotNull(node);
        assertNotNull(a);
        Element cls = getClassElement(t);
        VisageTreePath ppp = VisageTreePath.getPath(t, node);
        VisageTreePath p1 = VisageTreePath.getPath(t, a);
        VisagecTrees trees = VisagecTrees.instance(task);
        Element e = trees.getElement(ppp);
        isAccessible(task, p1, cls.asType(), e);
    }
    
    
    static class MockDiagnosticListener<T> implements DiagnosticListener<T> {
	public void report(Diagnostic<? extends T> d) {
	    diagCodes.add(d.getCode());
	}

	public List<String> diagCodes = new ArrayList<String>();
        public int errors() {
            return diagCodes.size();
        }
    }
    
    private class Visitor extends VisageTreePathScanner<Void, UnitTree> {

        @Override
        public Void visitVariable(VariableTree n, UnitTree t) {
            if (n.toString().equals("public var attribute1: String;\n")) {
                node = n;
            } 
            if (n.toString().equals("variable initialization for static script only (default) var a = Test {};\n")) {
                a = n;
            }
            return super.visitVariable(n, t);
        }
        
    }

    private static Element getClassElement(UnitTree cut) {
        for (Tree tt : cut.getTypeDecls()) {
            VisageKind kk = tt.getVisageKind();
            if (kk == VisageKind.CLASS_DECLARATION) {
                VisageClassDeclaration cd = (VisageClassDeclaration) tt;
                for (Tree jct : cd.getMembers()) {
                    VisageKind k = jct.getVisageKind();
                    if (k == VisageKind.CLASS_DECLARATION) {
                        VisagecTrees trees = VisagecTrees.instance(task);
                        VisageTreePath root = new VisageTreePath(cut);
                        return trees.getElement(new VisageTreePath(root, jct));
                    }
                }
            }
        }
        return null;
    }

    private static void isAccessible(VisagecTask task, VisageTreePath p, TypeMirror type, Element member) {
        VisagecTrees trees = VisagecTrees.instance(task);
        VisagecScope scope = trees.getScope(p);
        DeclaredType dt = (DeclaredType) type;
        VisageResolve resolve = VisageResolve.instance(((VisagecTaskImpl)task).getContext());
        Object env = ((VisagecScope) scope).getEnv();
        VisageEnv<VisageAttrContext> fxEnv = (VisageEnv<VisageAttrContext>) env;
        System.out.println(" env == " + scope);
        System.out.println(" dt == " + dt);
        System.out.println(" member == " + member);
        boolean res = resolve.isAccessible(fxEnv, (Type)dt, (Symbol) member);
        assertTrue(res);
    }
}
