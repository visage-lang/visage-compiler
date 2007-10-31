/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.JavafxcTool;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import junit.framework.TestCase;

import static org.junit.Assert.*;

/**
 * Test for issue JFXC-143:  assertion error in 
 * @author tball
 */
public class JFXC143 extends TestCase {

     public void testJFXC143() throws Exception {
        JavafxcTool tool = JavafxcTool.create();
        MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
        StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
        File file = new File("test/src/com/sun/tools/javafx/api/Hello.fx");
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
        JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
        List treeList = (List)javafxTask.parse();
        System.out.println("CompilationUnitTrees: " + treeList.size());
     }
     
     class MockDiagnosticListener<T> implements DiagnosticListener<T> {
        public void report(Diagnostic<? extends T> d) {
            diagCodes.add(d.getCode());
            System.err.println(d);
        }

        public List<String> diagCodes = new ArrayList<String>();
        public int errors() {
            return diagCodes.size();
        }
    }
 }
