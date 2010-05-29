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

/*
 * This is a basic test to show that internal instance vars are properly filtered out
 * and that the internal naming scheme for user instance vars is filtered out.
 */

package com.sun.javafx.jdi.test;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.ClassType;
import com.sun.javafx.jdi.FXReferenceType;
import com.sun.javafx.jdi.FXClassType;
import com.sun.javafx.jdi.FXObjectReference;
import com.sun.jdi.Field;
import com.sun.jdi.Value;
import com.sun.jdi.VoidValue;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.event.BreakpointEvent;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import junit.framework.Assert;

public class SetValueTest extends JavafxTestBase {
    ReferenceType targetClass;
    ThreadReference mainThread;

    private static final String targetClassName = "com.sun.javafx.jdi.test.target.SetValueTarget";

    public SetValueTest() {
        super(targetClassName);
    }

    @Test
    public void testSetValue() {
        try {
            startTests();
        } catch (Exception exp) {
            exp.printStackTrace();
            Assert.fail(exp.getMessage());
        }
    }

    /********** test core **********/

    protected void runTests() throws Exception {
        writeActual("stop in = " + targetClassName);
        BreakpointEvent bpe = startTo(targetClassName + "$sub",  "stopHere", "()V");
        targetClass = bpe.location().declaringType();

        ReferenceType topClass = vm().classesByName(targetClassName).get(0);
        List<Field>allFields = targetClass.allFields();

        FXObjectReference initVals = (FXObjectReference)((FXReferenceType)topClass).FXGetValue(topClass.fieldByName("initVals"));
        FXObjectReference secondVals = (FXObjectReference)((FXReferenceType)topClass).FXGetValue(topClass.fieldByName("secondVals"));

        Map<Field, Value>secondValsValues = secondVals.FXGetValues(allFields);

        for (Field fld: allFields) {
            Value vv = secondValsValues.get(fld);
            initVals.FXSetValue(fld, vv);
        }

        for (Field fld: allFields) {
            writeActual("field " + fld + ": value = " + initVals.FXGetValue(fld));
        }

        FXClassType topClassClass = (FXClassType)topClass;
        Field statVar = topClass.fieldByName("statString");
        topClassClass.FXSetValue(statVar, vm().mirrorOf("statString2"));
        writeActual("statString = " + topClass.getValue(statVar));

        statVar = topClass.fieldByName("defStatString");
        // This should be illegal - can't set a def var
        try { 
            topClassClass.FXSetValue(statVar, vm().mirrorOf("defStatString2"));
        } catch(IllegalArgumentException ee) {
            writeActual(ee.toString());
        }

        writeActual("defStatString = " + ((FXReferenceType)topClass).FXGetValue(statVar));

        statVar = topClass.fieldByName("staticBinder");
        Value invValue = ((FXReferenceType)topClass).getValue(statVar);
        // staticBinder is invalid
        writeActual("staticBinder = " + invValue);
        writeActual("invValue: getClass() " + invValue.getClass());
        writeActual("invValue: type()     " + invValue.type());
        writeActual("invValue: instanceof VoidValue "      + (invValue instanceof VoidValue));

        // This should be illegal - can't set a bound var
        try { 
            topClassClass.FXSetValue(statVar, vm().mirrorOf(2));
        } catch(IllegalArgumentException ee) {
            writeActual("Can't set a bound var: " + ee.toString());
        }

        // This forces evaluation of staticBinder; it becomes valid
        writeActual("staticBinder = " + ((FXReferenceType)topClass).FXGetValue(statVar));

        statVar=topClass.fieldByName("staticBindee");
        try {
            // Illegal to set a var to the FXInvalidValue
            topClassClass.FXSetValue(statVar, invValue);
        } catch (Exception ee) {
            writeActual("Can't set an invalid value: " + ee.toString());
        }

        testFailed = !didTestPass();

        /*
         * resume until end
         */
        listenUntilVMDisconnect();
        
        /*
         * deal with results of test
         * if anything has called failure("foo") testFailed will be true
         */
        if (!testFailed) {
            writeActual(testClassName + ": passed");
        } else {
            throw new Exception(testClassName + ": failed");
        }
    }
}
