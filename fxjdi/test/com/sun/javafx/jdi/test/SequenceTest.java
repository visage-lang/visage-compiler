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

package com.sun.javafx.jdi.test;

import com.sun.javafx.jdi.FXSequenceReference;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.StackFrame;
import com.sun.jdi.Value;
import com.sun.jdi.event.BreakpointEvent;
import org.junit.Test;
import junit.framework.Assert;


/**
 * Basic checks for FXSequenceReference/FXSequenceType methods and sequence access
 * from debugger.
 *
 * @author sundar
 */
public class SequenceTest extends JavafxTestBase {
    private static String targetClassName = "com.sun.javafx.jdi.test.target.SequenceTarget";

    public SequenceTest() {
        super(targetClassName);
    }

    @Test
    public void testSequence() {
        try {
            startTests();
        } catch (Exception exp) {
            exp.printStackTrace();
            Assert.fail(exp.getMessage());
        }
    }

    protected void runTests() throws Exception {
        startToMain();

        // break into function printSeq(arg: Integer[])
        BreakpointEvent bpe = resumeTo(targetClassName, "printSeq",
                "(Lcom/sun/javafx/runtime/sequence/Sequence;)V");

        mainThread = bpe.thread();

        // get the top frame
        StackFrame frame = mainThread.frame(0);
        // get first argument which is Integer[]
        Value value = frame.getArgumentValues().get(0);
        Assert.assertEquals(true, value instanceof FXSequenceReference);
        FXSequenceReference seq = (FXSequenceReference) value;
        Assert.assertEquals(2, seq.size());
        IntegerValue zerothElement = seq.getAsInt(0);
        Assert.assertEquals(1729, zerothElement.intValue());
        IntegerValue firstElement = seq.getAsInt(1);
        Assert.assertEquals(9999, firstElement.intValue());


        /*
         * resume until end
         */
        listenUntilVMDisconnect();
    }
}
