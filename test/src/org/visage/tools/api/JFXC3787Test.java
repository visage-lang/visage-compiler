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
import org.visage.api.tree.ClassDeclarationTree;
import org.visage.api.tree.VisageTreePath;

import org.visage.api.tree.VisageTreePathScanner;
import org.visage.api.tree.UnitTree;
import com.sun.tools.mjavac.util.JavacFileManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import javax.lang.model.element.Element;
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
public class JFXC3787Test {

    private static final String SEP = File.pathSeparator;
    private static final String DIR = File.separator;

    private String visageLibs = "dist/lib/shared";
    private String visageDeskLibs = "dist/lib/desktop";

    private String inputDir = "test/src/org/visage/tools/api";

    private JavafxcTrees trees;
    private UnitTree ut;

    @Before
    public void setup() throws IOException {
        JavafxcTool tool = JavafxcTool.create ();
        JavacFileManager manager = tool.getStandardFileManager (null, null, Charset.defaultCharset ());

        ArrayList<JavaFileObject> filesToCompile = new ArrayList<JavaFileObject> ();
        filesToCompile.add (manager.getFileForInput (inputDir + DIR + "JFXC3787.visage"));

        JavafxcTask task = tool.getTask (null, null, null, Arrays.asList ("-XDdisableStringFolding", "-XDpreserveTrees", "-cp",
            visageLibs + DIR + "visagec.jar" + SEP + visageLibs + DIR + "visagert.jar" + SEP + visageDeskLibs + DIR + "visage-ui-common.jar" + SEP + inputDir
        ), filesToCompile);

        task.parse();
        Iterable analyzeUnits = task.analyze();
        trees = JavafxcTrees.instance(task);
        ut = (UnitTree)analyzeUnits.iterator().next();
    }

    @After
    public void teardown() {
        trees = null;
        ut = null;
    }

    @Test
    public void testParenthesizedPositions() throws Exception {
        VisageTreePathScanner<Void, Void> scanner = new VisageTreePathScanner<Void, Void>() {

            @Override
            public Void visitClassDeclaration(ClassDeclarationTree node, Void p) {
                Element e = trees.getElement(getCurrentPath());
                VisageTreePath path = trees.getPath(e);
                assertNotNull("Returned null path for class definition!", path);
                return super.visitClassDeclaration(node, p);
            }
        };
        scanner.scan(ut, null);
    }
}
