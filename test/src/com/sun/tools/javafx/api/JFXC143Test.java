/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxcTask;
import com.sun.tools.javafx.api.JavafxcTool;
import com.sun.javafx.api.tree.UnitTree;
import java.io.File;
import java.util.List;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for issue JFXC-143:  assertion error in 
 * @author tball
 */
public class JFXC143Test {
    private static final String testSrc = System.getProperty("test.src.dir", "test/src");

    @Test
    public void testJFXC143() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            /* The javac library uses the context classloader to load the 
             * javac implementation.  In a NetBeans module, it needs to
             * be loaded by the module's classloader to make sure that the
             * version of javac this compiler requires takes precedence
             * over the JDK's version.  
             */
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            File file = new File("test/src/com/sun/tools/javafx/api/Hello.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            List<? extends UnitTree> treeList = (List)javafxTask.parse();
            assertTrue(treeList.size() == 1);
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }
    
    @Test
    public void parseClassSource() throws Exception {
        JavafxcTool instance = new JavafxcTool();
        MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
        StandardJavaFileManager fm = instance.getStandardFileManager(dl, null, null);
        File file = new File(testSrc + "/com/sun/tools/javafx/api", "UndeclaredClass.fx");
	Iterable<? extends JavaFileObject> fileList = fm.getJavaFileObjects(file);
        JavafxcTask task = instance.getTask(null, fm, dl, null, fileList);
        assertNotNull("no task returned", task);
        Iterable<? extends UnitTree> result = task.parse();
        assertEquals("parse error(s)", 0, dl.errors());
        assertTrue("no compilation units returned", result.iterator().hasNext());
    }
}
