/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
package launchers;

import com.sun.javafx.api.JavaFXScriptEngine;
import com.sun.javafx.runtime.SystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import junit.framework.TestCase;

/**
 * This class primarily calls into javafxrt, javafxc and javafxdoc to ensure
 * that a conformant version strings are returned for:
 *   a. System properties.
 *   b. tools javafx, javafxc, and javafxdoc.
 */
public class VersionTest extends TestCase {

    private static File distDir = null;
    private File javaExe = null;
    private File javafxExe = null;
    private File javafxcExe = null;
    private File javafxdocExe = null;

    boolean debug = false;
    private static File workingDir = null;
    private static JavaFXScriptEngine engine = null;

    static final boolean isWindows =
            System.getProperty("os.name").startsWith("Windows");

    // The golden values
    private static final String versionProp =
            SystemProperties.getProperty("javafx.version");
    private static final String fullversionProp =
            SystemProperties.getProperty("javafx.runtime.version");

    /*
     * get the path to where the dist directory lives, we use javac.jar
     * and script-api.jar to get the lib path, the parent of which is the
     * basedir.
     */
    File getDistDir() throws FileNotFoundException, IOException {
        if (distDir != null) {
            return distDir;
        }
        String javaClasspaths[] =
                System.getProperty("java.class.path", "").split(File.pathSeparator);
        for (String x : javaClasspaths) {
            if (x.endsWith("javac.jar")) {
                String path = x.substring(0, x.indexOf("lib" +
                        File.separator + "javac.jar"));
                distDir = new File(path, "dist").getAbsoluteFile();
                return distDir;
            } else if (x.endsWith("script-api.jar")) {
                String path = x.substring(0, x.indexOf("lib" +
                        File.separator + "script-api.jar"));
                distDir = new File(path, "dist").getAbsoluteFile();
                return distDir;
            }
        }
        throw new IOException("dist dir path could not be determined");
    }

    /**
     * Recursively deletes everything under dir
     */
    private void recursiveDelete(File dir) throws IOException {
        if (dir.isFile()) {
            dir.delete();
        } else if (dir.isDirectory()) {
            File[] entries = dir.listFiles();
            for (int i = 0; i < entries.length; i++) {
                if (entries[i].isDirectory()) {
                    recursiveDelete(entries[i]);
                }
                entries[i].delete();
            }
            dir.delete();
        }
    }

    /*
     * Gets a temporary clean directory for use
     */
    File getTempDirectory() throws IOException {
        File tmpDir = File.createTempFile("versionTest", ".tmp");
        if (tmpDir.exists()) {
            recursiveDelete(tmpDir);
        }
        tmpDir.mkdirs();
        return tmpDir;
    }

    /*
     * gets the path to the java.exe
     */
    File getJavaExe() {
        String javaHome = System.getProperty("java.home");
        
        File jFile = new File(new File(javaHome, "bin"), "java");
        if (isWindows) {
            jFile = new File(jFile.toString() + ".exe");
        }
        return jFile;
    }

    /*
     * returns the path to a javafx executable, do not
     * use .exe extension the method will take care of
     * it.
     */
    File getJavaFxDistExe(String exename) throws IOException {
        File exe = new File(new File(getDistDir(), "bin"),
                (isWindows) ? exename + ".exe" : exename);
        if (!exe.exists()) {
            return null;
        }
        return exe;
    }

    /*
     *  Check to see if the expected pattern was obtained
     */
    boolean checkExec(List<String> cmds, String expectedString, boolean match) {
        List<String> outputList = doExec(cmds);
        // better be the first line
        if (match) {
            if (outputList.get(0).matches(expectedString)) {
                return true;
            }
        } else {
            if (outputList.get(0).equals(expectedString)) {
                return true;
            }
        }
        
        // oh oh, something went wrong, print diagnostics to aid debugging
        System.err.println("Command line:");
        for (String x : cmds) {
            System.err.print(" " + x);
        }
        System.err.println("");
        System.err.println("Process output:");
        for (String x : outputList) {
            System.err.println(" " + x);
        }
        System.err.println("Expected string " + (match ? "match:" : "equals:") +
                ": " + expectedString);
        return false;
    }

    /*
     * execs a cmd and returns a List representation of the output
     */
    private List<String> doExec(List<String> cmds) {
        if (debug) {
            for (String x : cmds) {
                System.out.println(x);
            }
        }
        List<String> outputList = new ArrayList<String>();
        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb = pb.directory(workingDir);
        BufferedReader rdr = null;
        try {
            pb.redirectErrorStream(true);
            Process p = pb.start();
            rdr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // note: its a good idea to read the whole stream, half baked
            // reads can cause undesired side-effects.
            String in = rdr.readLine();
            while (in != null) {
                if (debug) {
                    System.out.println(in);
                }
                outputList.add(in);
                in = rdr.readLine();
            }
            p.waitFor();
            p.destroy();
            if (p.exitValue() != 0) {
                System.out.println("Error: Unexpected exit value " +
                        p.exitValue());
                return null;
            }
            return outputList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    /*
     * emit our FX code
     */
    private static String emitFx(boolean fullversion) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.print("java.lang.System.out.print(FX.getProperty(\"");
        pw.print((fullversion) ? "javafx.runtime.version" : "javafx.version");
        pw.println("\"));");
        pw.println("java.lang.System.out.flush();");
        return sw.toString();
    }

    /*
     * Create and compile an FX file, to get the version string.
     * Note: we could use the ScriptEngine, however we would like to make
     * sure the launchers (javafx and javafxc) works!.
     */
    private String getVersionPropFromFX(boolean isFullVersion) throws IOException {
        String filename = "Version";
        FileWriter fw = new FileWriter(new File(workingDir, filename + ".fx"));
        PrintWriter pw = new PrintWriter(fw);
        try {
            pw.println(emitFx(isFullVersion));
        } finally {
            if (pw != null) pw.close();
            if (fw != null) fw.close();
        }
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxcExe.toString());
        cmdsList.add(filename + ".fx");
        doExec(cmdsList);
        cmdsList.clear();
        cmdsList.add(javafxExe.toString());
        cmdsList.add(filename);
        List<String> output = doExec(cmdsList);
        return output.get(0);
    }

    /*
     * perform the mundane setups once.
     */
    @Override
    public void setUp() throws Exception {

        if (javaExe == null) {
            javaExe = getJavaExe();
        }
        if (javafxExe == null) {
            javafxExe = getJavaFxDistExe("javafx");
        }
        if (javafxcExe == null) {
            javafxcExe = getJavaFxDistExe("javafxc");
        }
        if (javafxdocExe == null) {
            javafxdocExe = getJavaFxDistExe("javafxdoc");
        }
        if (engine == null) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine scrEng = manager.getEngineByName("javafx");
            assertTrue(scrEng instanceof JavaFXScriptEngine);
            engine = (JavaFXScriptEngine) scrEng;
        }
        if (workingDir == null) {
            workingDir = getTempDirectory();
        }
        if (!workingDir.exists()) {
            workingDir.mkdirs();
        }
    }

    /*
     * clean up and be green.
     */
    @Override
    public void tearDown() {
        if (!debug) {
            if (workingDir != null && workingDir.exists()) {
                try {
                    recursiveDelete(workingDir);
                } catch (IOException ioe) { /*oh well!*/ }
            }
        }
    }

    private static final String VERSION_PATTERN_TAIL = "[1-9].[0-9].*";

    /*
     * test the runtime version string
     */
    public void testJavaFxProperties() throws ScriptException, IOException {
        // verify the strings are conformant.
        assertTrue(versionProp.matches("^" + VERSION_PATTERN_TAIL));
        assertTrue(fullversionProp.matches("^" + VERSION_PATTERN_TAIL));

        // Now use the fx method to verify the build results are the same
        String fxversion = getVersionPropFromFX(false);
        String fxfullversion = getVersionPropFromFX(true);
        assertEquals(versionProp, fxversion);
        assertEquals(fullversionProp, fxfullversion);
    }
    
    /*
     * test for javafx, using exact match
     */
    public void testJavaFxVersion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxExe.toString());
        cmdsList.add("-version");
        assertTrue(checkExec(cmdsList, "javafx " + versionProp, false));
    }

    public void testJavaFxFullversion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxExe.toString());
        cmdsList.add("-fullversion");
        assertTrue(checkExec(cmdsList, "javafx full version \"" +
                fullversionProp + "\"", false));
    }

    /*
     * test for javafxc, using regex pattern
     */
    public void testJavaFxcVersion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxcExe.toString());
        cmdsList.add("-version");
        assertTrue(checkExec(cmdsList, "^javafxc " + VERSION_PATTERN_TAIL, true));
    }

    public void testJavaFxcFullversion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxcExe.toString());
        cmdsList.add("-fullversion");
        assertTrue(checkExec(cmdsList, "^javafxc full version \"" +
                VERSION_PATTERN_TAIL + "\"", true));
    }

    /*
     * test for javafxdocm using regex pattern
     */
    public void testJavaFxDocVersion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxdocExe.toString());
        cmdsList.add("-version");
        assertTrue(checkExec(cmdsList, "^javafxdoc " +
                VERSION_PATTERN_TAIL, true));
    }

    public void testJavaFxDocFullversion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(javafxdocExe.toString());
        cmdsList.add("-fullversion");
        assertTrue(checkExec(cmdsList, "^javafxdoc full version \"" +
                VERSION_PATTERN_TAIL + "\"", true));
    }
}
