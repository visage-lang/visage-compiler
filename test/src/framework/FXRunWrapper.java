/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package framework;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

/**
 * Compiles a single JavaFX script source file and executes the resulting class.
 * Removed comparision of compiler/execution outputs.
 *
 * @author tball/baechul
 */
public class FXRunWrapper extends TestCase {
    private final String name;
    private final File testFile;
    private final File buildDir;
    private final boolean shouldRun;
    private final boolean expectCompileFailure;
    private final boolean expectRunFailure;
    private final String className;
    private final String classpath;
    private final String outputFileName;
    private final String errorFileName;
	private final String copyExpectedFileName;
    private final String expectedFileName;
    private final List<String> auxFiles;
    private final List<String> separateFiles;
    private final String param;

    public FXRunWrapper(File testFile,
                                  String name,
                                  boolean expectCompileFailure,
                                  boolean shouldRun,
                                  boolean expectRunFailure,
                                  Collection<String> auxFiles,
                                  Collection<String> separateFiles, 
                                  String param) {
        super(name);
        this.name = name;
        this.testFile = testFile;
        this.buildDir = TestHelper.makeBuildDir(testFile);
        this.shouldRun = shouldRun;
        this.expectCompileFailure = expectCompileFailure;
        this.expectRunFailure = expectRunFailure;
        this.auxFiles = new LinkedList<String>(auxFiles);
        this.separateFiles = new LinkedList<String>(separateFiles);
        this.className = testFile.getName();
        this.param = param;
        outputFileName = buildDir + File.separator + className + ".OUTPUT";
        errorFileName = buildDir + File.separator + className + ".ERROR";
		copyExpectedFileName = buildDir + File.separator + className + ".EXPECTED";
        expectedFileName = testFile.getPath() + ".EXPECTED";
        assertTrue(className.endsWith(".fx"));
        classpath = TestHelper.getClassPath(buildDir);
    }

    @Override
    protected void runTest() throws Throwable {
        System.out.println("Test(compile" + (shouldRun ? ", run" : "") + "): " + testFile);
        compile();
        if (shouldRun)
            execute(outputFileName, errorFileName);
    }

    private void compile() throws IOException {
        ByteArrayOutputStream out;
        ByteArrayOutputStream err;

        for (String f : separateFiles) {
            out = new ByteArrayOutputStream();
            err = new ByteArrayOutputStream();
            List<String> files = new ArrayList<String>();
            files.add(new File(testFile.getParent(), f).getPath());
            int errors = TestHelper.doCompile(buildDir.getPath(), classpath, files, out, err);
            if (errors != 0 && !expectCompileFailure) {
                TestHelper.dumpFile(new StringInputStream(new String(err.toByteArray())), "Compiler Output", testFile.toString());
                System.out.println("--");
                fail(String.format("%d errors compiling %s", errors, testFile));
            }
        }

        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        List<String> files = new ArrayList<String>();
        files.add(testFile.getPath());
        for (String f : auxFiles)
            files.add(new File(testFile.getParent(), f).getPath());
        int errors = 0;
        try {
            errors = TestHelper.doCompile(buildDir.getPath(), classpath, files, out, err);
        }
        catch (AssertionError e) {
            PrintWriter writer = new PrintWriter(err);
            e.printStackTrace(writer);
            writer.flush();
            errors = 1;
        }
        if (errors != 0) {
            PrintStream outputDest = expectCompileFailure? new PrintStream(new FileOutputStream(errorFileName))
                    : System.out;
            TestHelper.dumpFile(outputDest, new StringInputStream(new String(err.toByteArray())),
                                "Compiler Output", testFile.toString());
            outputDest.println("--");
            if (errors != 0 && !expectCompileFailure)
                fail(String.format("%d errors compiling %s", errors, testFile));
        }
        if (expectCompileFailure && errors == 0) {
            fail("expected compiler error");
        }
    }

    private void execute(String outputFileName, String errorFileName) throws IOException {
        CommandlineJava commandLine = new CommandlineJava();
        String mainClass = className.substring(0, className.length() - ".fx".length());
        commandLine.setClassname(mainClass);
        Project project = new Project();
        Path p = commandLine.createClasspath(project);
        p.createPathElement().setPath(System.getProperty("java.class.path"));
        p.createPathElement().setPath(buildDir.getPath());
        // for possible .fxproperties files in the test source directory
        p.createPathElement().setPath(testFile.getParent());
        
        if (param != null)
            commandLine.createArgument().setLine(param);

        PumpStreamHandler sh = new PumpStreamHandler(new FileOutputStream(outputFileName), new FileOutputStream(errorFileName));
        Execute exe = new Execute(sh);
        String[] strings = commandLine.getCommandline();
        exe.setCommandline(strings);
        try {
            exe.execute();
            File errorFileHandle = new File(errorFileName);
            if (errorFileHandle.length() > 0) {
                if (expectRunFailure)
                    return;
                TestHelper.dumpFile(new FileInputStream(outputFileName), "Test Output", testFile.toString());
                TestHelper.dumpFile(new FileInputStream(errorFileName), "Test Error", testFile.toString());
                System.out.println("--");
                fail("Output written to standard error");
            }
        }
        catch (IOException e) {
            if (!expectRunFailure)
                fail("Failure running test " + testFile + ": " + e.getMessage());
            // else success
        }
    }

}