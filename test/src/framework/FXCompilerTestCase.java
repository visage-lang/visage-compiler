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

package framework;

import com.sun.tools.javafx.api.JavafxCompiler;
import junit.framework.TestCase;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

import java.io.*;
import java.util.ServiceLoader;

/**
 * Compiles a single JavaFX script source file and executes the resulting class.
 *
 * @author tball
 */
public class FXCompilerTestCase extends TestCase {
    private final File test;
    private final File buildDir;
    private String className;

    private static final ServiceLoader<JavafxCompiler> compilerLoader =
            ServiceLoader.load(JavafxCompiler.class);

    public static final String TEST_ROOT = "test";
    public static final String BUILD_ROOT = "build/test";
    public static final String TEST_PREFIX = TEST_ROOT + File.separator;

    public FXCompilerTestCase(File test, String name) {
        super(name);
        this.test = test;
        assertTrue("path not a relative pathname", test.getPath().startsWith(TEST_PREFIX));
        this.buildDir = new File(BUILD_ROOT + File.separator + test.getParent().substring(TEST_PREFIX.length()));
    }

    @Override
    protected void runTest() throws Throwable {
        assertTrue("compiler not found", compilerLoader.iterator().hasNext());
        className = test.getName();
        assertTrue(className.endsWith(".fx"));
        String outputFileName = buildDir + File.separator + className + ".OUTPUT";
        String errorFileName = buildDir + File.separator + className + ".ERROR";
        String expectedFileName = test.getPath() + ".EXPECTED";
        compile();
        execute(outputFileName, errorFileName);
        compare(outputFileName, expectedFileName);
    }

    private void compile() throws IOException {
        File buildRoot = new File(BUILD_ROOT);
        if (!buildRoot.exists())
            fail("no " + BUILD_ROOT + " directory in " + new File(".").getAbsolutePath());

        JavafxCompiler compiler = compilerLoader.iterator().next();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        buildDir.mkdirs();
        System.out.println("Compiling " + test);
        int errors = compiler.run(null, out, err, "-d", buildDir.getPath(), test.getPath());
        if (errors != 0) {
            dumpFile(new StringInputStream(new String(err.toByteArray())), "Compiler Output");
            System.out.println("--");
            StringBuilder sb = new StringBuilder();
            sb.append(errors).append(" error");
            if (errors > 1)
                sb.append('s');
            sb.append(" compiling ").append(test);
            fail(sb.toString());
        }
    }

    private void execute(String outputFileName, String errorFileName) throws IOException {
        System.out.println("Running " + test);
        CommandlineJava commandLine = new CommandlineJava();
        String mainClass = className.substring(0, className.length() - ".fx".length());
        commandLine.setClassname(mainClass);
        Project project = new Project();
        Path p = commandLine.createClasspath(project);
        Path.PathElement pe = p.createPathElement();
        pe.setPath(System.getProperty("java.class.path"));
        Path.PathElement pe2 = p.createPathElement();
        pe2.setPath(buildDir.getPath());

        PumpStreamHandler sh = new PumpStreamHandler(new FileOutputStream(outputFileName), new FileOutputStream(errorFileName));
        Execute exe = new Execute(sh);
        String[] strings = commandLine.getCommandline();
        exe.setCommandline(strings);
        try {
            exe.execute();
            File errorFileHandle = new File(errorFileName);
            if (errorFileHandle.length() > 0) {
                dumpFile(new FileInputStream(outputFileName), "Test Output");
                dumpFile(new FileInputStream(errorFileName), "Test Error");
                System.out.println("--");
                fail("Output written to standard error");
            }
        } catch (IOException e) {
            fail("Failure running test " + test + ": " + e.getMessage());
        }
    }

    private void compare(String outputFileName, String expectedFileName) throws IOException {
        File expectedFile = new File(expectedFileName);
        if (expectedFile.exists()) {
            System.out.println("Comparing " + test);
            BufferedReader expected = new BufferedReader(new InputStreamReader(new FileInputStream(expectedFileName)));
            BufferedReader actual = new BufferedReader(new InputStreamReader(new FileInputStream(outputFileName)));
            int lineCount = 0;
            while (true) {
                String es = expected.readLine();
                String as = actual.readLine();
                ++lineCount;
                if (es == null && as == null)
                    break;
                else if (es == null)
                    fail("Expected output for " + test + " ends prematurely at line " + lineCount);
                else if (as == null)
                    fail("Program output for " + test + " ends prematurely at line " + lineCount);
                else if (!es.equals(as))
                    fail("Program output for " + test + " differs from expected at line " + lineCount);
            }
        }
    }

    private void dumpFile(InputStream file, String header) throws IOException {
        System.out.println("--" + header + " for " + test + "--");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }
        } finally {
            reader.close();
        }
    }

}
