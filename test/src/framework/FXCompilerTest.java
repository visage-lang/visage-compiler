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

package framework;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.tools.ant.DirectoryScanner;

/**
 * Simple JUnit test suite for the JavaFX script compiler.
 *
 * @author tball
 */
public class FXCompilerTest extends TestSuite {
    public static final String OPTIONS_RUN = "run";
    public static final String OPTIONS_EXPECT_COMPILE_FAIL = "expect-compile-fail";
    public static final String OPTIONS_CHECK_COMPILE_MSG = "check-compile-msg";
    public static final String OPTIONS_EXPECT_RUN_FAIL = "expect-run-fail";
    public static final String OPTIONS_IGNORE_STD_ERROR = "ignore-std-error";
    public static final String OPTIONS_COMPARE = "compare";

    // A list of test directories under which to look for the TEST_FX_INCLUDES patterns
    private static final String TEST_FX_ROOTS = "test.fx.roots";

    // A pattern of tests to include under the TEST_FX_ROOTS
    private static final String TEST_FX_INCLUDES = "test.fx.includes";

    // Alternatively, a list of tests to run, eg
    //   "test/regress/jfxc1043.fx test/regress/jfxc1053.fx"
    private static final String TEST_FX_LIST = "test.fx.list";


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


        String testList = System.getProperty(TEST_FX_LIST);

        if (testList == null || testList.length() == 0) {
            // Run the tests under the test roots dir, selected by the TEST_FX_INCLUDES patterns
            String testRootsString = System.getProperty(TEST_FX_ROOTS);
            if (testRootsString == null || testRootsString.length() == 0) {
                throw new Exception("Error: " + TEST_FX_ROOTS + " must be set");
            }
            String testRoots[] = testRootsString.split(" ");
            for (String root : testRoots) {
                File dir = new File(root);
                findTests(dir, tests, orphans);
            }
        } else {
            // TEST_FX_LIST contains a blank speparated list of test file names.
            String strArray[] = testList.split(" ");
            for (String ss : strArray) {
                handleOneTest(new File(ss), tests, orphans);
            }
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
        ds.setIncludes(new String[]{(pattern == null ? "**/*.fx" : pattern)});
        ds.setBasedir(dir);
        ds.scan();
        for (String s : ds.getIncludedFiles()) {
            File f = new File(dir, s);
            assert !f.isDirectory() : "ERROR: Expected file, found directory " + f;
            handleOneTest(f, tests, orphanFiles);
        }
    }

    private static void handleOneTest(File testFile,  List<Test> tests, Set<String> orphanFiles) throws Exception {
        String name = testFile.getParentFile().getName() + "/" + testFile.getName();
        assert name.lastIndexOf(".fx") > 0 : "not a JavaFX script: " + name;
        boolean isTest = false, isNotTest = false, isFxUnit = false,
            shouldRun = false, compileFailure = false, runFailure = false, checkCompilerMsg = false,
            noCompare = false, ignoreStdError = false;

        Scanner scanner = null;
        List<String> auxFiles = new ArrayList<String>();
        List<String> separateFiles = new ArrayList<String>();
        List<String> compileArgs = new ArrayList<String>();
        String param = null;
        boolean inComment = false;
        try {
            scanner = new Scanner(testFile);
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
                else if (token.equals("@test/nocompare")) {
                    isTest = true;
                    noCompare = true;
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
                else if (token.equals("@run/ignore-std-error")) {
                    shouldRun = true;
                    ignoreStdError = true;
                }
                else if (token.equals("@compilearg")) {
                    compileArgs.add(scanner.next());
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
            return;
        }
        finally {
            if (scanner != null)
                scanner.close();
        }
        if (isTest && compileFailure)
            shouldRun = runFailure = false;
        if (isTest) {
            if (isFxUnit)
                tests.add(FXUnitTestWrapper.makeSuite(testFile, name));
            else {
                Map<String, String> options = new HashMap<String, String>();
                if (compileFailure)
                    options.put(OPTIONS_EXPECT_COMPILE_FAIL, "true");
                if (shouldRun)
                    options.put(OPTIONS_RUN, "true");
                if (runFailure)
                    options.put(OPTIONS_EXPECT_RUN_FAIL, "true");
                if (checkCompilerMsg)
                    options.put(OPTIONS_CHECK_COMPILE_MSG, "true");
                if (!noCompare)
                    options.put(OPTIONS_COMPARE, "true");
                if (ignoreStdError)
                    options.put(OPTIONS_IGNORE_STD_ERROR, "true");
                tests.add(new FXRunAndCompareWrapper(testFile, name, compileArgs, options, auxFiles, separateFiles, param));
            }
        }
        else if (!isNotTest)
            orphanFiles.add(name);
    }
}
