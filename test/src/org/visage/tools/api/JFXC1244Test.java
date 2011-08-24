/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.visage.tools.api;

import org.visage.api.VisagecTask;
import com.sun.source.tree.CompilationUnitTree;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class JFXC1244Test {
    private static final String testSrc = System.getProperty("test.src.dir", "test/src");

    @Test
    public void testJFXC1244() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(VisagecTool.class.getClassLoader());
            VisagecTool tool = VisagecTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            List<File> dirs = new ArrayList<File>();
            dirs.add(getTmpDir());
            fileManager.setLocation(CLASS_OUTPUT, dirs);
            
            File file = new File("test/src/org/visage/tools/api/Hello.visage");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            VisagecTask visageTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            List<? extends CompilationUnitTree> treeList = (List)visageTask.parse();
            assertTrue("no (or too many) parse tree(s) returned", treeList.size() == 1);
            
            Iterable<? extends JavaFileObject> classList = visageTask.generate();
            assertTrue("no classes returned", classList.iterator().hasNext());
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
