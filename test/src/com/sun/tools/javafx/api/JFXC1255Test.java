/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.tree.ClassDeclarationTree;
import com.sun.javafx.api.tree.JavaFXTreePathScanner;
import com.sun.source.tree.CompilationUnitTree;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import static javax.tools.StandardLocation.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for issue JFXC-1244:  empty results list from Task.generate().
 * @author tball
 */
public class JFXC1255Test {

    @Test
    public void testJFXC1244() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            List<File> dirs = new ArrayList<File>();
            dirs.add(getTmpDir());
            fileManager.setLocation(CLASS_OUTPUT, dirs);
            
            File file = new File("test/src/com/sun/tools/javafx/api/Point.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            Iterable<? extends CompilationUnitTree> treeList = javafxTask.analyze();
            assertTrue("no parse tree(s) returned", treeList.iterator().hasNext());
            
            final JavafxcTrees trees = JavafxcTrees.instance(javafxTask);
            for (CompilationUnitTree unit : treeList) {
                JavaFXTreePathScanner scanner = new JavaFXTreePathScanner<Object,Void>() {
                    @Override
                    public Object visitClassDeclaration(ClassDeclarationTree node, Void p) {
                        JavafxcScope scope = trees.getScope(getCurrentPath());
                        Element type = trees.getElement(getCurrentPath());
                        Element owner = type.getEnclosingElement();
                        while (owner instanceof ExecutableElement)
                            owner = owner.getEnclosingElement();
                        TypeElement enclClass = scope.getEnclosingClass();
                        
                        // if this is a member class, verify enclosing class was returned
                        if (owner instanceof TypeElement) {
                            assertNotNull(enclClass);
                            assertTrue(enclClass instanceof TypeElement);
                        }
                        return super.visitClassDeclaration(node, p);
                    }
                };
                scanner.scan(unit, null);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }
    
    private static File getTmpDir() {
        try {
            File f = File.createTempFile("dummy", "file");
            f.deleteOnExit();
            File tmpdir = f.getParentFile();
            if (tmpdir != null)
                return tmpdir;
        } catch (IOException ex) {
        }
        File f = new File("test-output");
        f.mkdir();
        return f;
    }
}
