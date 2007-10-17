/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TaskListener;
import java.io.Writer;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for JavafxcTask interface and its JavafxcTaskImpl implementation.
 */
public class JavafxcTaskTest {
    
    @Test
    public void parseNullSourceList() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        Iterable<? extends CompilationUnitTree> result = task.parse();
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void errorCheckNullSourceList() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        int result = task.errorCheck();
        assertEquals(0, result);
    }

    @Test
    public void analyzeNullSourceList() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        Iterable<? extends CompilationUnitTree> result = task.analyze();
        assertNull(result);
    }

    @Test
    public void generateNullSourceList() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        Iterable<? extends JavaFileObject> result = task.generate();
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void getTypeMirror() {
        Iterable<? extends Tree> path = null;
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        TypeMirror result = task.getTypeMirror(path);
        assertNull(result);
    }

    @Test
    public void getElementsNullSourceList() {
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        Elements result = task.getElements();
        assertNotNull(result);
    }

    @Test
    public void getTypesNullSourceList() {
        JavafxcTool instance = new JavafxcTool();
        JavafxcTask task = instance.getTask(null, null, null, null, null);
        Types result = task.getTypes();
        assertNotNull(result);
    }
    
}
