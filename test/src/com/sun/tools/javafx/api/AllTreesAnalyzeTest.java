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

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.tree.JavaFXTreePathScanner;

import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.javafx.api.tree.UnitTree;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class AllTreesAnalyzeTest {
    private static final String testSrc = System.getProperty("test.src.dir", "test/src");
    private static final String testClasses = System.getProperty("build.test.classes.dir");
    
    /**
     * Make sure we are able to at least analyze the test file. In other words it
     * should be compilable but this test mimics what the NetBeans editor does
     * with source files (not full compile).
     */
    @Test
    public void analyzeAllTreesFile() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
        StandardJavaFileManager fm = instance.getStandardFileManager(dl, null, null);
        List<String> options = 
                Arrays.asList("-d", ".", "-sourcepath", testSrc, "-classpath", testClasses);
        File file = new File(testSrc + "/com/sun/tools/javafx/api", "AllTrees.fx");
	Iterable<? extends JavaFileObject> files = fm.getJavaFileObjects(file);
        JavafxcTask task = instance.getTask(null, fm, dl, null, files);
        assertNotNull("no task returned", task);
        Iterable<? extends UnitTree> result1 = task.parse();
        assertEquals("parse error(s)", 0, dl.errors());
        assertTrue("no compilation units returned", result1.iterator().hasNext());
        Iterable<? extends UnitTree> result2 = task.analyze();
        assertTrue("no compilation units returned", result2.iterator().hasNext());
        UnitTree t = result2.iterator().next();
        Visitor v = new Visitor();
        v.scan(t, null);
    }
    
    /**
     * This test checks all the comments in file AllTrees.fx and compares them
     * to the list of com.sun.javafx.api.tree.Tree.JavaFXKind.values()
     */
    @Test
    public void haveAllTreesCovered() throws Exception {
        Set<String> testFileConstructs = new HashSet<String>();
        // commented out construct name in the test file
        Pattern p = Pattern.compile("// [A-Z|_]+");
        File f = new File(testSrc + "/com/sun/tools/javafx/api", "AllTrees.fx");
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = 
            fc.map(FileChannel.MapMode.READ_ONLY, 0, (int)fc.size());
        Charset cs = Charset.forName("8859_1");
        CharsetDecoder cd = cs.newDecoder();
        CharBuffer cb = cd.decode(bb);
        Matcher m = p.matcher(cb);
        while (m.find()) {
            // strip the initial double slash and space by taking the substring
            testFileConstructs.add(m.group().substring(3));
        }
        
        Set<String> constructs = new HashSet<String>();
        for (JavaFXKind k : JavaFXKind.values()) {
            constructs.add(k.name());
        }
        
        Set<String> missingInTestFiles = new HashSet<String>(constructs);
        missingInTestFiles.removeAll(testFileConstructs);
        assertTrue("The following " + missingInTestFiles.size() + " constructs are not in the AllTrees.fx file: " + missingInTestFiles, missingInTestFiles.isEmpty());
        
        Set<String> extraInTestFile = new HashSet<String>(testFileConstructs);
        extraInTestFile.removeAll(constructs);
        assertTrue("The following " + extraInTestFile.size() + " constructs are not in the JavaFXKinds: " + extraInTestFile, extraInTestFile.isEmpty());
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
    
    private static class Visitor extends JavaFXTreePathScanner<Void, Void> {
        // TODO: do some tests checking the positions of the trees ...
    }
}
