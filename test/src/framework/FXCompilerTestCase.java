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

import com.sun.javafx.api.JavafxCompiler;
import junit.framework.TestCase;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Compiles a single JavaFX script source file and executes the resulting class.
 *
 * @author tball
 */
public class FXCompilerTestCase extends TestCase {
    private final File test;
    private final File buildDir;
    private final boolean shouldRun;
    private final boolean expectCompileFailure;
    private final boolean expectRunFailure;
    private String className;
    private final List<String> auxFiles;
    private final List<String> separateFiles;

    private static final JavafxCompiler compiler = compilerLocator();

    public static final String TEST_ROOT = "test";
    public static final String BUILD_ROOT = "build/test";
    public static final String TEST_PREFIX = TEST_ROOT + File.separator;

    public FXCompilerTestCase(File test,
                              String name,
                              boolean expectCompileFailure,
                              boolean shouldRun,
                              boolean expectRunFailure,
                              Collection<String> auxFiles,
                              Collection<String> separateFiles) {
        super(name);
        this.test = test;
        this.shouldRun = shouldRun;
        this.expectCompileFailure = expectCompileFailure;
        this.expectRunFailure = expectRunFailure;
        assertTrue("path not a relative pathname", test.getPath().startsWith(TEST_PREFIX));
        this.buildDir = new File(BUILD_ROOT + File.separator + test.getParent().substring(TEST_PREFIX.length()));
        this.auxFiles = new LinkedList<String>(auxFiles);
        this.separateFiles = new LinkedList<String>(separateFiles);
    }

    @Override
    protected void runTest() throws Throwable {
        className = test.getName();
        assertTrue(className.endsWith(".fx"));
        String outputFileName = buildDir + File.separator + className + ".OUTPUT";
        String errorFileName = buildDir + File.separator + className + ".ERROR";
        String expectedFileName = test.getPath() + ".EXPECTED";
        compile();
        if (shouldRun) {
            execute(outputFileName, errorFileName);
            compare(outputFileName, expectedFileName);
        }
    }

    private void compile() throws IOException {
        ByteArrayOutputStream out;
        ByteArrayOutputStream err;

        File buildRoot = new File(BUILD_ROOT);
        if (!buildRoot.exists())
            fail("no " + BUILD_ROOT + " directory in " + new File(".").getAbsolutePath());
        buildDir.mkdirs();
        Path classpath = new CommandlineJava().createClasspath(new Project());
        classpath.createPathElement().setPath(System.getProperty("java.class.path"));
        classpath.createPathElement().setPath(buildDir.getPath());

        for (String f : separateFiles) {
            out = new ByteArrayOutputStream();
            err = new ByteArrayOutputStream();
            List<String> files = new ArrayList<String>();
            files.add(new File(test.getParent(), f).getPath());
            System.out.println("Compiling " + f);
            int errors = doCompile(buildDir.getPath(), classpath.toString(), files, out, err);
            if (errors != 0 && !expectCompileFailure) {
                dumpFile(new StringInputStream(new String(err.toByteArray())), "Compiler Output");
                System.out.println("--");
                StringBuilder sb = new StringBuilder();
                sb.append(errors).append(" error");
                if (errors > 1)
                    sb.append('s');
                sb.append(" compiling ").append(f);
                fail(sb.toString());
            } else if (errors == 0 && expectCompileFailure) {
                fail("expected compiler error");
            }
        }

        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        List<String> files = new ArrayList<String>();
        files.add(test.getPath());
        for (String f : auxFiles)
            files.add(new File(test.getParent(), f).getPath());
        System.out.println("Compiling " + test);
        int errors = doCompile(buildDir.getPath(), classpath.toString(), files, out, err);
        if (errors != 0 && !expectCompileFailure) {
            dumpFile(new StringInputStream(new String(err.toByteArray())), "Compiler Output");
            System.out.println("--");
            StringBuilder sb = new StringBuilder();
            sb.append(errors).append(" error");
            if (errors > 1)
                sb.append('s');
            sb.append(" compiling ").append(test);
            fail(sb.toString());
        } else if (errors == 0 && expectCompileFailure) {
            fail("expected compiler error");
        }
    }

    private static int doCompile(String dir, String classpath, List<String> files, OutputStream out, OutputStream err) {
        List<String> args = new ArrayList<String>();
        args.add("-target");
        args.add("1.5");
        args.add("-d");
        args.add(dir);
        args.add("-cp");
        args.add(classpath);
        for (String f : files)
            args.add(f);
        return compiler.run(null, out, err, args.toArray(new String[args.size()]));
    }

    private void execute(String outputFileName, String errorFileName) throws IOException {
        System.out.println("Running " + test);
        CommandlineJava commandLine = new CommandlineJava();
        String mainClass = className.substring(0, className.length() - ".fx".length());
        commandLine.setClassname(mainClass);
        Project project = new Project();
        Path p = commandLine.createClasspath(project);
        p.createPathElement().setPath(System.getProperty("java.class.path"));
        p.createPathElement().setPath(buildDir.getPath());

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
        BufferedReader expected = expectedFile.exists()
                ? new BufferedReader(new InputStreamReader(new FileInputStream(expectedFileName)))
                : new BufferedReader(new StringReader(""));
        BufferedReader actual = new BufferedReader(new InputStreamReader(new FileInputStream(outputFileName)));

        System.out.println("Comparing " + test);
        int lineCount = 0;
        while (true) {
            String es = expected.readLine();
            String as = actual.readLine();
            while (as != null && as.startsWith("Cobertura:"))
                as = actual.readLine();
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

    private static JavafxCompiler compilerLocator() {
        Iterator<?> iterator;
        Class<?> loaderClass;
        String loadMethodName;
        boolean usingServiceLoader;

        try {
            loaderClass = Class.forName("java.util.ServiceLoader");
            loadMethodName = "load";
            usingServiceLoader = true;
        } catch (ClassNotFoundException cnfe) {
            try {
                loaderClass = Class.forName("sun.misc.Service");
                loadMethodName = "providers";
                usingServiceLoader = false;
            } catch (ClassNotFoundException cnfe2) {
                throw new AssertionError("Failed discovering ServiceLoader");
            }
        }

        try {
            // java.util.ServiceLoader.load or sun.misc.Service.providers
            Method loadMethod = loaderClass.getMethod(loadMethodName,
                    Class.class,
                    ClassLoader.class);
            ClassLoader cl = FXCompilerTestCase.class.getClassLoader();
            Object result = loadMethod.invoke(null, JavafxCompiler.class, cl);

            // For java.util.ServiceLoader, we have to call another
            // method to get the iterator.
            if (usingServiceLoader) {
                Method m = loaderClass.getMethod("iterator");
                result = m.invoke(result); // serviceLoader.iterator();
            }

            iterator = (Iterator<?>) result;
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Failed accessing ServiceLoader: " + t);
            throw new AssertionError(); // not executed
        }

        if (!iterator.hasNext()) {
            fail("No JavaFX Script compiler found");
            throw new AssertionError(); // not executed
        }
        return (JavafxCompiler) iterator.next();
    }
}
