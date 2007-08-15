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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ServiceLoader;

/**
 * Compiles a single JavaFX script source file and executes the resulting class.
 *
 * @author tball
 */
public class FXCompilerTestCase extends TestCase {
    private final File test;
    private final File buildDir;

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
        compile();
        execute();
    }

    private void compile() {
        File buildRoot = new File(BUILD_ROOT);
        if (!buildRoot.exists())
            fail("no " + BUILD_ROOT + " directory in " + new File(".").getAbsolutePath());

        JavafxCompiler compiler = compilerLoader.iterator().next();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        buildDir.mkdirs();
        int errors = compiler.run(null, out, err, "-d", buildDir.getPath(), test.getPath());
        if (errors != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(errors);
            sb.append(" error");
            if (errors > 1)
                sb.append('s');
            sb.append(":\n");
            sb.append(new String(err.toByteArray()));
            fail(sb.toString());
        }
    }

    private void execute() {
    }
}
