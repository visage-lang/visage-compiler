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
 *   M(mixin)        B
 *    \            / |
 *     \__________/  |
 *          |        |
 *          |        |
 *          C        A
 * There  is method called foo() defined in M , B, A. When you invoke foo() in M, B
 * from another method in C say bar() its OK, but you cannot invoke foo() in A from
 * bar() in C.
 * @author srikalyanchandrashekar
 */
public class MixinTest extends JdbBase {

// @BeginTest Mixin.fx
//mixin class M {
//    public function foo() : Void {
//        println("M foo");
//    }
//}
//
//class B {
//    public function foo() : Void {
//        println("B foo");
//    }
//}
//
//class A extends B {
//    override public function foo() : Void {
//        println("A foo");
//    }
//}
//
//class C extends B, M {
//    public function bar() : Void {
//        B.foo();   //  LEGAL
//        M.foo();   //  LEGAL
//        //A.foo();   //  ILLEGAL: Not direct superclass or parent
//    }
//}
// function run() {
//     var tmp:C = C {};
//     tmp.bar();
//     println("Test ends here..");
// }
// @EndTest
    public static String B_FOO = "B foo";
    public static String M_FOO = "M foo";
    @Test(timeout=5000)
    public void testMixin() {
        try {
            //resetOutputs();//Uncomment this if you want to see the output on console
            compile("Mixin.fx");
            stop("in Mixin.javafx$run$");
            stop("in Mixin:28");
            stop("in Mixin$C:21");
            stop("in Mixin$C:22");
            stop("in Mixin$C:24");

            fxrun();

            resumeToBreakpoint();
            list();

            resumeToBreakpoint();
            list();
            resumeToBreakpoint();
            list();

            resumeToBreakpoint();
            Assert.assertTrue(contains(B_FOO));
            list();

            resumeToBreakpoint();
            Assert.assertTrue(contains(M_FOO));
            list();

            resumeToVMDeath();
            quit();
        } catch (Exception exp) {
            exp.printStackTrace();
            Assert.fail(exp.getMessage());
        }
    }
}
