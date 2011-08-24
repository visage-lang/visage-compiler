package org.visage.tools.api;

import org.visage.api.VisagecTask;
import com.sun.tools.mjavac.util.JavacFileManager;
import org.visage.tools.api.VisagecTool;

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
        String visageLibs = "dist/lib/shared";
        String inputDir = "src/share/classes/visage/lang";
        String outputDir = getTmpDir().getPath();
        nerrors = 0;

        VisagecTool tool = VisagecTool.create ();
        JavacFileManager manager = tool.getStandardFileManager (null, null, Charset.defaultCharset ());

        ArrayList<JavaFileObject> filesToCompile = new ArrayList<JavaFileObject> ();
        for (String file : new File (inputDir).list ())
            if (file.endsWith (".visage"))
                filesToCompile.add (manager.getFileForInput (inputDir + DIR + file));

        VisagecTask task = tool.getTask (null, null, new DiagnosticListener<JavaFileObject>() {
            public void report (Diagnostic<? extends JavaFileObject> diagnostic) {
                System.out.println ("diagnostic = " + diagnostic);
                nerrors++;
            }
        }, Arrays.asList ("-target", "1.5", "-d", outputDir, "-cp",
            visageLibs + DIR + "visagec.jar" + SEP + visageLibs + DIR + "visagert.jar" + SEP + visageLibs + DIR + "Scenario.jar" + SEP + inputDir
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
