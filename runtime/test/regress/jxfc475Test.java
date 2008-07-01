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

package regress;

import com.sun.tools.javafx.script.JavaFXScriptCompiler;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tball
 */
public class jxfc475Test {
    private static final String testApplet = "SimpleApplet";
    private static final String testAppletFile = "SimpleApplet.fx";
    private static final String testDataDir = 
            System.getProperty("test.data.dir", "test/unit/data");
    
    @Test
    public void testJFXC475() throws Exception {
        final byte[] classBytes = compileApplet();
        ClassLoader cl = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                if (name.equals(testApplet)) {
                    return defineClass(name, classBytes, 0, classBytes.length);
                }
                return super.loadClass(name);
            }
        };
        Class<?> fxClass = Class.forName("SimpleApplet", false, cl);
        assertNotNull("applet class not loaded", fxClass);
        Class<?> baseClass = Class.forName("java.applet.Applet");
        assertTrue("javafx.ui.Applet is not a subclass of java.applet.Applet", 
                   baseClass.isAssignableFrom(fxClass));
    }
    
    byte[] compileApplet() throws IOException {
        File file = new File(testDataDir + "/jfxc475", testAppletFile);
        Reader reader = new FileReader(file);
        JavaFXScriptCompiler compiler = new JavaFXScriptCompiler(getClass().getClassLoader());
        Map<String, byte[]> classes = compiler.compile(file.getPath());
        return classes.get(testApplet);
    }
}
