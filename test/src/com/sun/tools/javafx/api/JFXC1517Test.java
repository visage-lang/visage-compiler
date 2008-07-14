package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxcTask;
import com.sun.javafx.api.tree.UnitTree;
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
 * Test for issue JFXC-1517: sizeof operator toString() fails
 * @author tball
 */
public class JFXC1517Test {

    @Test
    public void testJFXC1517() throws Exception {
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {            
            Thread.currentThread().setContextClassLoader(JavafxcTool.class.getClassLoader());
            JavafxcTool tool = JavafxcTool.create();
            MockDiagnosticListener<? super FileObject> dl = new MockDiagnosticListener<FileObject>();
            
            StandardJavaFileManager fileManager = tool.getStandardFileManager(dl, null, null);
            List<File> dirs = new ArrayList<File>();
            dirs.add(getTmpDir());
            fileManager.setLocation(CLASS_OUTPUT, dirs);
            
            File file = new File("test/src/com/sun/tools/javafx/api/JFXC1517.fx");
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(file); 
            JavafxcTask javafxTask = tool.getTask(null, fileManager, dl, null, fileObjects);
            List<? extends UnitTree> treeList = (List)javafxTask.parse();
            assertTrue("no (or too many) parse tree(s) returned", treeList.size() == 1);

            UnitTree unit = treeList.get(0);
            unit.toString();  // will throw assertion error if reported error exists
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
