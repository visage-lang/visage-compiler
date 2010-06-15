/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxjdi;

import junit.framework.Assert;
import org.junit.Test;

/**
 * This Test is associated with JIRA JFXC-4419
 * @author srikalyanchandrashekar
 */
public class StopAtBkpointTest extends JdbBase {

// @BeginTest StopAtBkpoint.fx
// var binder = 1.0;
// var bindee = bind binder;
// function run() {
//     println("Initial bindee is {bindee}");
//     binder = 2.0;
//     println("Bindee is {bindee} now");
//     binder = 3.0;
//     println("Bindee is {bindee} now");
//     println("StopAtBkpointTest Ends here");
// }
// @EndTest

    @Test(timeout=10000)
    public void testStopAtBkpoint() {
        try {
            resetOutputs();
            compile("StopAtBkpoint.fx");
            stop("in StopAtBkpoint.javafx$run$");
            stop("in StopAtBkpoint:6");
            stop("in StopAtBkpoint:8");
            stop("in StopAtBkpoint:9");

            fxrun();

            resumeToBreakpoint();
            System.out.println("List 1");
            list();

            resumeToBreakpoint();
            System.out.println("List 2");
            list();

            resumeToBreakpoint();
            System.out.println("List 3");
            list();

            resumeToBreakpoint();
            System.out.println("List 4");
            list();

            resumeToVMDeath();
            quit();
        } catch (Exception exp) {
            exp.printStackTrace();
            Assert.fail(exp.getMessage());
        }
    }
}
