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

import com.sun.javafx.api.JavafxTaskEvent;
import com.sun.javafx.api.JavafxTaskListener;
import com.sun.javafx.api.JavafxcTask;
import com.sun.tools.javafx.api.JavafxcTrees;
import com.sun.tools.javafx.api.JavafxcTool;
import com.sun.javafx.api.tree.JavaFXTreePathScanner;
import com.sun.javafx.api.tree.ClassDeclarationTree;
import com.sun.javafx.api.tree.JavaFXTreePath;
import com.sun.javafx.api.tree.UnitTree;
import javax.lang.model.element.Element;
import com.sun.javafx.api.tree.SourcePositions;

import com.sun.javafx.api.tree.VariableTree;
import com.sun.source.util.TreePath;
import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests that JavafxTaskEvent doesn't cause NPE after analyze phase.
 * 
 * @author Tom Ball
 */
public class JFXC1558Test {
    int startEvents;
    int finishEvents;

    @Test
    public void testJFXTaskEvent() throws Exception {
        startEvents = 0;
        finishEvents = 0;
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/Point.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            
            javafxTask.setTaskListener(new JavafxTaskListener() {
                public void started(JavafxTaskEvent e) {
                    startEvents++;
                }

                public void finished(JavafxTaskEvent e) {
                    finishEvents++;
                }
            });
            
            javafxTask.analyze();
            
            assertEquals("Incorrect number of task started events", 3, startEvents);
            assertEquals("Incorrect number of task finished events", 3, finishEvents);
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

}
