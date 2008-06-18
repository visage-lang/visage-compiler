package com.sun.tools.javafx.api;

import com.sun.javafx.api.JavafxcTask;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javafx.api.JavafxcTool;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author David Kaspar
 */
public class GenerateTest {

    private static final String SEP = File.pathSeparator;
    private static final String DIR = File.separator;
    private int nerrors;

    @Test
    public void testGeneratePhase() throws Exception {
        String javafxLibs = "dist/lib";
        String inputDir = "src/share/classes/javafx/lang";
        String outputDir = getTmpDir().getPath();
        nerrors = 0;

        JavafxcTool tool = JavafxcTool.create ();
        JavacFileManager manager = tool.getStandardFileManager (null, null, Charset.defaultCharset ());

        ArrayList<JavaFileObject> filesToCompile = new ArrayList<JavaFileObject> ();
        for (String file : new File (inputDir).list ())
            if (file.endsWith (".fx"))
                filesToCompile.add (manager.getFileForInput (inputDir + DIR + file));

        JavafxcTask task = tool.getTask (null, null, new DiagnosticListener<JavaFileObject>() {
            public void report (Diagnostic<? extends JavaFileObject> diagnostic) {
                System.out.println ("diagnostic = " + diagnostic);
                nerrors++;
            }
        }, Arrays.asList ("-target", "1.5", "-d", outputDir, "-cp",
            javafxLibs + DIR + "javafxc.jar" + SEP + javafxLibs + DIR + "javafxrt.jar" + SEP + javafxLibs + DIR + "javafxgui.jar" + SEP + javafxLibs + DIR + "Scenario.jar" + SEP + inputDir
        ), filesToCompile);

        Iterable parseUnits = task.parse();
        assertTrue(parseUnits.iterator().hasNext());
        assertEquals(0, nerrors);
        Iterable analyzeUnits = task.analyze();
        assertTrue(analyzeUnits.iterator().hasNext());
        assertEquals(0, nerrors);
        Iterable generatedFiles = task.generate ();
        assertTrue(generatedFiles.iterator().hasNext());
        assertEquals(0, nerrors);
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
