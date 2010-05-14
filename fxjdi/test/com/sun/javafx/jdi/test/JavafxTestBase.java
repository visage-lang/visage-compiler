/*
 * Copyright 2001-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.jdi.test;

import com.sun.jdi.event.BreakpointEvent;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

/**
 * Base class for tests in which the target is a JavaFX application. This takes
 * care of setting javafxrt.jar in classpath in addition to application class path.
 * Also has utility methods for javafx methods.
 *
 * @author sundar
 */
public abstract class JavafxTestBase extends TestScaffold {
    protected static String classPath() {
        return testBuildDirectory() +
                File.pathSeparator +
                javafxrtJarPath();
    }

    protected static String javafxrtJarPath() {
        return System.getProperty("javafxrt.jar");
    }

    protected static String fxMainClassName() {
        return "com.sun.javafx.runtime.Main";
    }

    protected static String fxRunMethodName() {
        return "javafx$run$";
    }

    protected static String fxRunMethodSignature() {
        return "(Lcom/sun/javafx/runtime/sequence/Sequence;)Ljava/lang/Object;";
    }

    protected static final String[] ARGS = {
        "-J-classpath",
        classPath(),
        fxMainClassName()
    };

    protected static String[] arguments(String targetClassName) {
        String[] args = new String[ARGS.length + 1];
        System.arraycopy(ARGS, 0, args, 0, ARGS.length);
        args[args.length - 1] = targetClassName;
        return args;
    }

    String testClassName;
    File actualFile;
    PrintStream actualOut;
    BufferedReader expectedReader;  // != null means there is a .EXPECTED file

    void writeActual(String p1) {
        if (expectedReader == null) {
            failure("Call to writeActual but no .EXPECTED file.  Use println instead of writeActual");
        } else {
            actualOut.printf(p1 + "\n");
        }
    }

    boolean didTestPass() {
        try {
            BufferedReader actualReader =  new BufferedReader(new FileReader(actualFile));
            int lineNum = 0;
            while(true) {
                lineNum++;
                String actualLine = actualReader.readLine();
                String expectedLine = expectedReader.readLine();
                if (actualLine == null) {
                    if (expectedLine == null) {
                        return true;
                    }
                    println("Error: extra line in EXPECTED, line = " + lineNum);
                    println( expectedLine);
                    return false;
                }
                if (expectedLine == null) {
                    println("Error: extra line in ACTUAL: line = " + lineNum);
                    println( actualLine);
                    return false;
                }
                if (!actualLine.equals(expectedLine)) {
                    println("Error:  Output is wrong at line = " + lineNum);
                    println("  ACTUAL   = " + actualLine);
                    println("  EXPECTED = " + expectedLine);
                    return false;
                }
            }
        } catch(Exception ee) {
            println("IO Exception checking output: " + ee);
        }
        return false;
    }

    protected JavafxTestBase(String targetClassName) {
        super(arguments(targetClassName));
        testClassName = this.getClass().getSimpleName();

        String expectedFileName = System.getProperty("user.dir") + 
            (".test." + 
             this.getClass().getName()).replace(".", File.separator) + ".EXPECTED";
        try {
            expectedReader = new BufferedReader(new FileReader(expectedFileName));
            String actualName = System.getProperty("build.test.classes.dir") + 
                File.separator +
                "FilterVarsTest.ACTUAL";
            actualFile = new File(actualName);
            actualOut = new PrintStream(new FileOutputStream(actualFile));
        } catch (FileNotFoundException ee) {
            expectedReader = null;
        }
    }

    protected JavafxTestBase(String[] args) {
        super(args);
    }

    protected BreakpointEvent startToMain() {
        return startToMain(fxMainClassName());
    }
}
