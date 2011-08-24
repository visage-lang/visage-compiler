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

import org.visage.api.VisagecTask;
import org.visage.tools.api.VisagecTrees;
import org.visage.tools.api.VisagecTool;
import org.visage.api.tree.VisageTreePathScanner;
import org.visage.api.tree.ClassDeclarationTree;
import org.visage.api.tree.VisageTreePath;
import org.visage.api.tree.UnitTree;
import javax.lang.model.element.Element;
import org.visage.api.tree.SourcePositions;

import org.visage.api.tree.VariableTree;
import com.sun.source.util.TreePath;
import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that VisagecTrees.getElement can be called from visitor
 * 
 * @author Michael Chernyshov
 */
public class VSGC907Test {

    @Test
    public void testVisageTreesGetElement() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(VisagecTool.class.getClassLoader());
            VisagecTool tool = VisagecTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/org/visage/tools/api/Point.visage");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            VisagecTask visageTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends UnitTree> treeList = visageTask.analyze();
            
            VisagecTrees trees = VisagecTrees.instance(visageTask);
            SourcePositions sp = trees.getSourcePositions();
            UnitTree unit = treeList.iterator().next();
            
            DetectorVisitor d = new DetectorVisitor(trees, sp, unit);
            d.scan(treeList, null);
            
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

class DetectorVisitor<Void,EnumSet> extends VisageTreePathScanner<Void,EnumSet> {
    VisagecTrees trees;
    SourcePositions sp;
    UnitTree unit;
                    
    DetectorVisitor(VisagecTrees trees, SourcePositions sp, UnitTree unit) {
        this.trees = trees;
        this.sp = sp;
        this.unit = unit;
    }
            
    @Override    
    public Void visitClassDeclaration(ClassDeclarationTree tree, EnumSet p) {
        Element e = trees.getElement(getCurrentPath());
        Assert.assertNotNull(e);
        VisageTreePath pth = trees.getPath(unit, tree);
        Assert.assertNotNull(pth);

        scan(tree.getClassMembers(), null);
        return null;        
    }   
    
    @Override
    public Void visitVariable(VariableTree tree, EnumSet p) {                
        Element e = trees.getElement(getCurrentPath());
        Assert.assertNotNull(e);        
        VisageTreePath pth = trees.getPath(unit, tree);
        Assert.assertNotNull(pth);
        return null;
    }
}
}
