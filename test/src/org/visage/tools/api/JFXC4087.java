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

import org.visage.api.JavafxcTask;
import org.visage.api.tree.ForExpressionInClauseTree;
import org.visage.api.tree.IdentifierTree;
import org.visage.api.tree.VisageTreePath;

import org.visage.api.tree.VisageTreePathScanner;
import org.visage.api.tree.SourcePositions;
import org.visage.api.tree.Tree;
import org.visage.api.tree.UnitTree;
import org.visage.api.tree.VariableTree;
import org.visage.tools.tree.VisageFunctionDefinition;
import org.visage.tools.tree.VisageOverrideClassVar;
import org.visage.tools.tree.VisageTree;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JavacFileManager;
import com.sun.tools.mjavac.util.Name;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.StringTokenizer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

/**
 * This test makes sure that the AllTrees.visage file contains all tree constructs
 * from org.visage.api.tree.Tree.VisageKind.values().
 *
 * @author David Strupl
 */
public class JFXC4087 {

    private static final String SEP = File.pathSeparator;
    private static final String DIR = File.separator;
    private String visageLibs = "dist/lib/shared";
    private String visageDeskLibs = "dist/lib/desktop";
    private String inputDir = "test/sandbox/org/visage/tools/api";
    private JavafxcTrees trees;
    private UnitTree ut;
    private SourcePositions sp;
    private Context ctx;
    private Elements elements;

    @Before
    public void setup() throws IOException {
        doSetup();
    }

    private void doSetup() throws IOException {
        JavafxcTool tool = JavafxcTool.create();
        JavacFileManager manager = tool.getStandardFileManager(null, null, Charset.defaultCharset());

        ArrayList<JavaFileObject> filesToCompile = new ArrayList<JavaFileObject>();
        filesToCompile.add(manager.getFileForInput(inputDir + DIR + "JFXC4087.visage"));

        JavafxcTask task = tool.getTask(null, null, null, Arrays.asList("-XDdisableStringFolding", "-XDpreserveTrees", "-Xjcov", "-cp",
                visageLibs + DIR + "visagec.jar" + SEP + visageLibs + DIR + "visagert.jar" + SEP + visageDeskLibs + DIR + "visage-ui-common.jar" + SEP + inputDir), filesToCompile);

        task.parse();
        Iterable analyzeUnits = task.analyze();
        trees = JavafxcTrees.instance(task);

        ut = (UnitTree) analyzeUnits.iterator().next();
        sp = trees.getSourcePositions();
        ctx = ((JavafxcTaskImpl) task).getContext();
        elements = ((JavafxcTaskImpl) task).getElements();
    }

    @After
    public void teardown() {
        trees = null;
        ut = null;
    }

    @Test
    public void testInClausePosition() throws Exception {
        final int[] pos = new int[]{-1};
        final boolean[] checkContext = new boolean[]{false};

        VisageTreePathScanner<Void, Void> positionScanner = new VisageTreePathScanner<Void, Void>() {
            private boolean inClause = false;
            @Override
            public Void visitIdentifier(IdentifierTree node, Void p) {
                if (inClause) {
                    Element e = trees.getElement(getCurrentPath());
                    if (e != null && e.getSimpleName().contentEquals("seq")) {
                        pos[0] = (int)sp.getEndPosition(ut, node);
                        System.err.println("ObjectID: " + System.identityHashCode(node));
                    }
                }
                return super.visitIdentifier(node, p);
            }

            @Override
            public Void visitForExpressionInClause(ForExpressionInClauseTree node, Void p) {
                try {
                    inClause = true;
                    return super.visitForExpressionInClause(node, p);
                } finally {
                    inClause = false;
                }
            }
        };

        VisageTreePathScanner<Void, Void> accessContext = new VisageTreePathScanner<Void, Void>() {
            @Override
            public Void visitVariable(VariableTree node, Void p) {
                if (checkContext[0]) {
                    Element e = trees.getElement(getCurrentPath());
                    if (e != null) {
                        if (e.getSimpleName().contentEquals("aaa")) {
                            trees.getScope(getCurrentPath());
                        }
                    }
                }
                return super.visitVariable(node, p);
            }
        };

        int pass1, pass2;
        positionScanner.scan(ut, null);
        pass1 = pos[0];
        pos[0] = -1;
        System.err.println("End Position: pass 1 = " + pass1);
        positionScanner.scan(ut, null);
        pass2 = pos[0];
        pos[0] = -1;
        System.err.println("End Position: pass 2 = " + pass2);
        assertEquals(pass1, pass2);

        checkContext[0] = true;
        positionScanner.scan(ut, null);
        pass1 = pos[0];
        pos[0] = -1;
        System.err.println("End Position: pass 1 = " + pass1);
        accessContext.scan(ut, null);
        System.err.println("Accessed context");
        positionScanner.scan(ut, null);
        pass2 = pos[0];
        pos[0] = -1;
        System.err.println("End Position: pass 2 = " + pass2);
        assertEquals(pass1, pass2);
    }
}
