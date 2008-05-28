/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
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
import java.util.Locale;
import java.util.Scanner;

import java.util.Set;
import java.util.TreeSet;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.tools.ant.DirectoryScanner;

/**
 * Simple JUnit test suite for the JavaFX script compiler.
 *
 * @author tball
 */
public class FXCompilerTest extends TestSuite {
    private static final String[] TEST_ROOTS = {
            "test/features",
            "test/regress",
            "test/fxunit",
            "test/should-fail",
            "test/currently-failing",
            "test/functional"
    };
    private static final String TEST_FX_INCLUDES = "test.fx.includes";

    /**
     * Creates a test suite for this directory's .fx source files.  This
     * method is called reflectively by the JUnit test runner.
     *
     * @param directory the top-level directory to scan for .fx source files.
     * @return a Test which
     */
    public static Test suite() throws Exception {
        Locale.setDefault(new Locale(""));
        List<Test> tests = new ArrayList<Test>();
        Set<String> orphans = new TreeSet<String>();
        for (String root : TEST_ROOTS) {
            File dir = new File(root);
            findTests(dir, tests, orphans);
        }
        // Collections.sort(tests);
        return new FXCompilerTest(tests, orphans);
    }

    public FXCompilerTest(List<Test> tests, Set<String> orphans) {
        super();
        if (System.getProperty(TEST_FX_INCLUDES) == null)
            addTest(new OrphanTestFinder(orphans));
        for (Test t : tests)
            addTest(t);
    }

    private static void findTests(File dir, List<Test> tests, Set<String> orphanFiles) throws Exception {
        String pattern = System.getProperty(TEST_FX_INCLUDES);
        DirectoryScanner ds = new DirectoryScanner();
        ds.setIncludes(new String[] { (pattern == null ? "**/*.fx" : pattern) });
        ds.setBasedir(dir);
        ds.scan();
        final Set<File> included = new TreeSet<File>();
        for (String s : ds.getIncludedFiles())
            included.add(new File(dir, s));
        File[] children = dir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                String name = f.getName();
                if (f.isDirectory())
                    return !name.equals("SCCS");
                return name.endsWith(".fx") && included.contains(f);
            }
        });
        for (File f : children) {
            String name = f.getParentFile().getName() + "/" + f.getName();
            if (f.isDirectory())
                findTests(f, tests, orphanFiles);
            else {
                assert name.lastIndexOf(".fx") > 0 : "not a JavaFX script: " + name;
                boolean isTest = false, isNotTest = false, isFxUnit = false, shouldRun = false, compileFailure = false, runFailure = false, checkCompilerMsg = false;

                Scanner scanner = null;
                List<String> auxFiles = new ArrayList<String>();
                List<String> separateFiles = new ArrayList<String>();
                String param = null;
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
                        else if (!inComment)
                            continue;

                        if (token.equals("@test"))
                            isTest = true;
                        else if (token.equals("@test/fail")) {
                            isTest = true;
                            compileFailure = true;
                        }
                        else if (token.equals("@test/compile-error")) {
                            isTest = true;
                            compileFailure = true;
                            checkCompilerMsg = true;
                        }
                        else if (token.equals("@test/warning")) {
                            isTest = true;
                            checkCompilerMsg = true;
                        }
                        else if (token.equals("@test/fxunit")) {
                            isTest = true;
                            isFxUnit = true;
                        }
                        else if (token.equals("@subtest"))
                            isNotTest = true;
                        else if (token.equals("@run"))
                            shouldRun = true;
                        else if (token.equals("@run/fail")) {
                            shouldRun = true;
                            runFailure = true;
                        }
                        else if (token.equals("@run/param")) {
                            shouldRun = true;
                            param = scanner.nextLine();
                        }
                        else if (token.equals("@compilefirst"))
                            separateFiles.add(scanner.next());
                        else if (token.equals("@compile/fail")) {
                            auxFiles.add(scanner.next());
                            compileFailure = true;
                        }
                        else if (token.equals("@compile"))
                            auxFiles.add(scanner.next());
                    }
                }
                catch (Exception ignored) {
                    continue;
                }
                finally {
                    if (scanner != null)
                        scanner.close();
                }
                if (isTest && compileFailure)
                    shouldRun = runFailure = false;
                if (isTest) {
                    if (isFxUnit)
                        tests.add(FXUnitTestWrapper.makeSuite(f, name));
                    else
                        tests.add(new FXRunAndCompareWrapper(f, name, compileFailure, shouldRun, runFailure, checkCompilerMsg, auxFiles, separateFiles, param));
                }
                else if (!isNotTest)
                    orphanFiles.add(name);
            }
        }
    }
}
