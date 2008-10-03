/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.tree.MemberSelectTree;
import com.sun.javafx.api.tree.VariableTree;
import com.sun.tools.javafx.api.*;
import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.tree.JavaFXTreePath;
import com.sun.javafx.api.tree.JavaFXTreePathScanner;
import com.sun.javafx.api.tree.Tree;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.javafx.api.tree.UnitTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javafx.comp.JavafxAttrContext;
import com.sun.tools.javafx.comp.JavafxEnv;
import com.sun.tools.javafx.comp.JavafxResolve;
import com.sun.tools.javafx.tree.JFXClassDeclaration;
import com.sun.tools.javafx.tree.JFXVar;
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
 * This test makes sure that the AllTrees.fx file contains all tree constructs
 * from com.sun.javafx.api.tree.Tree.JavaFXKind.values().
 * 
 * @author David Strupl
 */
public class JFXC2054Test {
    private static final String testSrc = System.getProperty("test.src.dir", "test/sandbox");
    private static final String testClasses = System.getProperty("build.test.classes.dir");
    private static JavafxcTask task;
    private VariableTree node;
    private VariableTree a;

    /**
     * Make sure we are able to at least analyze the test file. In other words it
     * should be compilable but this test mimics what the NetBeans editor does
     * with source files (not full compile).
     */
    @Test
    public void isAccessibleTest() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
        StandardJavaFileManager fm = instance.getStandardFileManager(dl, null, null);
        List<String> options = 
                Arrays.asList("-d", ".", "-sourcepath", testSrc, "-classpath", testClasses);
        File file = new File(testSrc + "/com/sun/tools/javafx/api", "JFXC2054.fx");
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
        JavaFXTreePath ppp = JavaFXTreePath.getPath(t, node);
        JavaFXTreePath p1 = JavaFXTreePath.getPath(t, a);
        JavafxcTrees trees = JavafxcTrees.instance(task);
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
    
    private class Visitor extends JavaFXTreePathScanner<Void, UnitTree> {

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
            JavaFXKind kk = tt.getJavaFXKind();
            if (kk == JavaFXKind.CLASS_DECLARATION) {
                JFXClassDeclaration cd = (JFXClassDeclaration) tt;
                for (Tree jct : cd.getMembers()) {
                    JavaFXKind k = jct.getJavaFXKind();
                    if (k == JavaFXKind.CLASS_DECLARATION) {
                        JavafxcTrees trees = JavafxcTrees.instance(task);
                        JavaFXTreePath root = new JavaFXTreePath(cut);
                        return trees.getElement(new JavaFXTreePath(root, jct));
                    }
                }
            }
        }
        return null;
    }

    private static void isAccessible(JavafxcTask task, JavaFXTreePath p, TypeMirror type, Element member) {
        JavafxcTrees trees = JavafxcTrees.instance(task);
        JavafxcScope scope = trees.getScope(p);
        DeclaredType dt = (DeclaredType) type;
        JavafxResolve resolve = JavafxResolve.instance(((JavafxcTaskImpl)task).getContext());
        Object env = ((JavafxcScope) scope).getEnv();
        JavafxEnv<JavafxAttrContext> fxEnv = (JavafxEnv<JavafxAttrContext>) env;
        System.out.println(" env == " + scope);
        System.out.println(" dt == " + dt);
        System.out.println(" member == " + member);
        boolean res = resolve.isAccessible(fxEnv, (Type)dt, (Symbol) member);
        assertTrue(res);
    }
}
