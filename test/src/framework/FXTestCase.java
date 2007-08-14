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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ServiceLoader;
import junit.framework.TestCase;

/**
 * Compiles a single JavaFX script source file and executes the resulting class.
 * 
 * @author tball
 */
public class FXTestCase extends TestCase {
    File test;

    private static final ServiceLoader<JavafxCompiler> compilerLoader = 
            ServiceLoader.load(JavafxCompiler.class);
    
    public static final String TEST_ROOT = "build/test";

    public FXTestCase(File test, String name) {
        super(name);
        this.test = test;
    }

    @Override
    protected void runTest() throws Throwable {
        assertTrue("compiler not found", compilerLoader.iterator().hasNext());
        compile();
        execute();
    }
    
    private void compile() {
        File testRoot = new File(TEST_ROOT);
        if (!testRoot.exists())
            fail("no build/test directory in " + new File(".").getAbsolutePath());
        JavafxCompiler compiler = compilerLoader.iterator().next();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int errors = compiler.run(null, out, err, "-d", TEST_ROOT, test.getPath());
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
