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

import org.visage.api.VisagecTask;

import org.visage.api.tree.SourcePositions;
import org.visage.api.tree.UnitTree;
import org.visage.tools.tree.VisageClassDeclaration;
import org.visage.tools.tree.VisageScript;
import org.visage.tools.tree.VisageTree;
import org.visage.tools.tree.VisageTag;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JavacFileManager;
import com.sun.tools.mjavac.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
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
public class JFXC3614 {

    private static final String SEP = File.pathSeparator;
    private static final String DIR = File.separator;
    private String visageLibs = "dist/lib/shared";
    private String visageDeskLibs = "dist/lib/desktop";
    private String inputDir = "test/sandbox/org/visage/tools/api";
    private VisagecTrees trees;
    private UnitTree ut;
    private SourcePositions sp;
    private Context ctx;
    private Elements elements;

    @Before
    public void setup() throws IOException {
        doSetup();
    }

    private void doSetup() throws IOException {
        VisagecTool tool = VisagecTool.create();
        JavacFileManager manager = tool.getStandardFileManager(null, null, Charset.defaultCharset());

        ArrayList<JavaFileObject> filesToCompile = new ArrayList<JavaFileObject>();
        filesToCompile.add(manager.getFileForInput(inputDir + DIR + "JFXC3614.visage"));

        VisagecTask task = tool.getTask(null, null, null, Arrays.asList("-XDdisableStringFolding", "-XDpreserveTrees", "-Xjcov", "-cp",
                visageLibs + DIR + "visagec.jar" + SEP + visageLibs + DIR + "visagert.jar" + SEP + visageDeskLibs + DIR + "visage-ui-common.jar" + SEP + inputDir), filesToCompile);

        task.parse();
        Iterable analyzeUnits = task.analyze();
        trees = VisagecTrees.instance(task);

        ut = (UnitTree) analyzeUnits.iterator().next();
        sp = trees.getSourcePositions();
        ctx = ((VisagecTaskImpl) task).getContext();
        elements = ((VisagecTaskImpl) task).getElements();
    }

    @After
    public void teardown() {
        trees = null;
        ut = null;
    }

    @Test
    public void testRunPosition() throws Exception {
        // Get the script level tree and pull out the class delcaration we except
        //
        VisageClassDeclaration cd = (VisageClassDeclaration)((VisageScript)ut).defs.get(0);

        // Now get the two defs that should be in the class declaration. If the
        // bug remains fixed then the first def will be the VisageVar and the second
        // will be the run() method
        //
        List<VisageTree> cdDefs = cd.getMembers();

        // Make sure that the Variable def appears before the
        // run method in the tree.
        //
        assertEquals(cdDefs.get(0).getFXTag(), VisageTag.VAR_DEF);
        assertEquals(cdDefs.get(1).getFXTag(),VisageTag.FUNCTION_DEF);
    }
}
