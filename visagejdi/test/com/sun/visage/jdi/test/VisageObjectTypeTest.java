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
package org.visage.jdi.test;

import org.visage.jdi.VisageObjectType;
import org.visage.jdi.VisageVirtualMachine;
import org.visage.jdi.VisageReferenceType;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import org.junit.Test;
import junit.framework.Assert;

/**
 * Basic sanity check for VisageObjectType (which wraps org.visage.runtime.VisageObject)
 *
 * @author sundar
 */
public class VisageObjectTypeTest extends VisageTestBase {
    // any Visage class will do..
    private static String targetClassName = "org.visage.jdi.test.target.HelloTarget";

    public VisageObjectTypeTest() {
        super(targetClassName);
    }

    @Test
    public void testVisageObjectType() {
        try {
            startTests();
        } catch (Exception exp) {
            Assert.fail(exp.getMessage());
        }
    }

    protected void runTests() throws Exception {
        startToMain();
        // run till visage$run$ - so that org.visage.runtime.VisageObject is loaded!
        resumeTo(targetClassName, visageRunMethodName(), visageRunMethodSignature());

        // look for VisageObject type
        ReferenceType rt = vm().classesByName(VisageVirtualMachine.VISAGE_OBJECT_TYPE_NAME).get(0);
        // it has to be VisageObjectType
        Assert.assertEquals(true, rt instanceof VisageObjectType);
        // check few methods of VisageObjectType
        // We are checking for internal methods that are filtered out by VisageReferenceType, so
        // we have to use the underlying JDI ReferenceType
        VisageObjectType visageObjType = (VisageObjectType)rt;
        Method count$Method = visageObjType.count$Method();
        Assert.assertEquals("count$", count$Method.name());
        Assert.assertEquals("()I", count$Method.signature());
        Method get$Method = visageObjType.get$Method();
        Assert.assertEquals("get$", get$Method.name());
        Assert.assertEquals("(I)Ljava/lang/Object;", get$Method.signature());
        Method set$Method = visageObjType.set$Method();
        Assert.assertEquals("set$", set$Method.name());
        Assert.assertEquals("(ILjava/lang/Object;)V", set$Method.signature());

        /*
         * resume until end
         */
        listenUntilVMDisconnect();
    }
}
