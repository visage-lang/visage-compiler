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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import junit.framework.*;

/**
 * Simple JUnit test suite for the JavaFX script compiler.
 * 
 * @author tball
 */
public class FXCompilerTest extends TestSuite {
    private static final String TEST_ROOT = "test";

    /**
     * Creates a test suite for this directory's .fx source files.  This 
     * method is called reflectively by the JUnit test runner.
     * 
     * @param directory the top-level directory to scan for .fx source files.
     * @return a Test which 
     */
    public static Test suite() {
        List<Test> tests = new ArrayList<Test>();
        File dir = new File(TEST_ROOT);
        findTests(dir, tests);
        return new FXCompilerTest(tests);
    }
    
    public FXCompilerTest(List<Test> tests) {
        super();
        for (Test t : tests)
            addTest(t);
    }

    private static void findTests(File dir, List<Test> tests) {
        File[] children = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                if (f.isDirectory())
                    return !name.equals("SCCS");
                return name.endsWith(".fx");
            }
        });
        for (File f : children) {
            String name = f.getName();
            if (f.isDirectory())
                findTests(f, tests);
            else {
                assert name.lastIndexOf(".fx") > 0 : "not a JavaFX script: " + name;
                boolean isTest = false, shouldRun = false;

                Scanner scanner = null;
                boolean inComment = false;
                try {
                    scanner = new Scanner(f);
                    while (scanner.hasNext()) {
                        // TODO: Scan for /ref=file qualifiers, etc, to determine run behavior
                        String token = scanner.next();
                        if (token.startsWith("/*"))
                            inComment = true;
                        else if (token.endsWith(("*/")))
                            inComment = false;
                        else if (inComment && token.equals("@test"))
                            isTest = true;
                        else if (inComment && token.equals("@run"))
                            shouldRun = true;
                    }
                } catch (Exception ignored) {
                    continue;
                } finally {
                    if (scanner != null)
                        scanner.close();
                }
                if (isTest)
                    tests.add(new FXCompilerTestCase(f, name, shouldRun));
            }
        }
    }
}
