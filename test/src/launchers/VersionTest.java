
/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
package launchers;

import junit.framework.TestCase;
import java.io.IOException;
import java.util.ArrayList;
import javax.script.ScriptException;

import com.sun.javafx.runtime.SystemProperties;

public class VersionTest extends TestCase {
   private static final String VERSION_PATTERN_TAIL = "[1-9].[0-9].*";

    // The golden values
    private static final String versionProp =
            SystemProperties.getProperty("javafx.version");
    private static final String fullversionProp =
            SystemProperties.getProperty("javafx.runtime.version");
       /*
     * perform the mundane setups once.
     */
    @Override
    public void setUp() throws Exception {
        Utils.init();
    }

    /*
     * clean up and be green.
     */
    @Override
    public void tearDown() {
        Utils.reset();
    }

    /*
     * test the runtime version string
     */
    public void testJavaFxProperties() throws ScriptException, IOException {
        // verify the strings are conformant.
        assertTrue(versionProp.matches("^" + VERSION_PATTERN_TAIL));
        assertTrue(fullversionProp.matches("^" + VERSION_PATTERN_TAIL));

        // Now use the fx method to verify the build results are the same
        String fxversion = Utils.getVersionPropFromFX(false);
        String fxfullversion = Utils.getVersionPropFromFX(true);
        assertEquals(versionProp, fxversion);
        assertEquals(fullversionProp, fxfullversion);
    }
    
    /*
     * test for javafx, using exact match
     */
    public void testJavaFxVersion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(Utils.javafxExe.toString());
        cmdsList.add("-version");
        assertTrue(Utils.checkExec(cmdsList, "javafx " + versionProp, false));
    }

    public void testJavaFxFullversion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(Utils.javafxExe.toString());
        cmdsList.add("-fullversion");
        assertTrue(Utils.checkExec(cmdsList, "javafx full version \"" +
                fullversionProp + "\"", false));
    }

    /*
     * test for javafxc, using regex pattern
     */
    public void testJavaFxcVersion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(Utils.javafxcExe.toString());
        cmdsList.add("-version");
        assertTrue(Utils.checkExec(cmdsList, "^javafxc " + VERSION_PATTERN_TAIL, true));
    }

    public void testJavaFxcFullversion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(Utils.javafxcExe.toString());
        cmdsList.add("-fullversion");
        assertTrue(Utils.checkExec(cmdsList, "^javafxc full version \"" +
                VERSION_PATTERN_TAIL + "\"", true));
    }

    /*
     * test for javafxdocm using regex pattern
     */
    public void testJavaFxDocVersion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(Utils.javafxdocExe.toString());
        cmdsList.add("-version");
        assertTrue(Utils.checkExec(cmdsList, "^javafxdoc " +
                VERSION_PATTERN_TAIL, true));
    }

    public void testJavaFxDocFullversion() {
        ArrayList<String> cmdsList = new ArrayList<String>();
        cmdsList.add(Utils.javafxdocExe.toString());
        cmdsList.add("-fullversion");
        assertTrue(Utils.checkExec(cmdsList, "^javafxdoc full version \"" +
                VERSION_PATTERN_TAIL + "\"", true));
    }
}
