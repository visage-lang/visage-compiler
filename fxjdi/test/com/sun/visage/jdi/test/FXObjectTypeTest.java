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
package com.sun.visage.jdi.test;

import com.sun.visage.jdi.FXObjectType;
import com.sun.visage.jdi.FXVirtualMachine;
import com.sun.visage.jdi.FXReferenceType;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import org.junit.Test;
import junit.framework.Assert;

/**
 * Basic sanity check for FXObjectType (which wraps com.sun.visage.runtime.FXObject)
 *
 * @author sundar
 */
public class FXObjectTypeTest extends JavafxTestBase {
    // any FX class will do..
    private static String targetClassName = "com.sun.visage.jdi.test.target.HelloTarget";

    public FXObjectTypeTest() {
        super(targetClassName);
    }

    @Test
    public void testFXObjectType() {
        try {
            startTests();
        } catch (Exception exp) {
            Assert.fail(exp.getMessage());
        }
    }

    protected void runTests() throws Exception {
        startToMain();
        // run till visage$run$ - so that com.sun.visage.runtime.FXObject is loaded!
        resumeTo(targetClassName, fxRunMethodName(), fxRunMethodSignature());

        // look for FXObject type
        ReferenceType rt = vm().classesByName(FXVirtualMachine.FX_OBJECT_TYPE_NAME).get(0);
        // it has to be FXObjectType
        Assert.assertEquals(true, rt instanceof FXObjectType);
        // check few methods of FXObjectType
        // We are checking for internal methods that are filtered out by FXReferenceType, so
        // we have to use the underlying JDI ReferenceType
        FXObjectType fxObjType = (FXObjectType)rt;
        Method count$Method = fxObjType.count$Method();
        Assert.assertEquals("count$", count$Method.name());
        Assert.assertEquals("()I", count$Method.signature());
        Method get$Method = fxObjType.get$Method();
        Assert.assertEquals("get$", get$Method.name());
        Assert.assertEquals("(I)Ljava/lang/Object;", get$Method.signature());
        Method set$Method = fxObjType.set$Method();
        Assert.assertEquals("set$", set$Method.name());
        Assert.assertEquals("(ILjava/lang/Object;)V", set$Method.signature());

        /*
         * resume until end
         */
        listenUntilVMDisconnect();
    }
}
