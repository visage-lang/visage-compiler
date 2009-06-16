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

package com.sun.javafx.runtime.liveconnect;

import java.util.*;

import com.sun.java.browser.plugin2.liveconnect.v1.*;
import javafx.reflect.*;

public abstract class FXTypeDelegate implements InvocationDelegate {
    protected FXTypeDelegate() {
        FXLocal.Context context = FXLocal.getContext();
        voidType = context.getVoidType();
        booleanType = context.getBooleanType();
        integerType = context.getIntegerType();
        numberType = context.getNumberType();
    }

    protected Object unbox(Object obj) {
        if (obj == Void.TYPE) {
            return obj;
        }

        FXValue val = (FXValue) obj;
        if (val == null)
            return null;
        
        FXType type = val.getType();
        if (type instanceof FXPrimitiveType) {
            return ((FXPrimitiveValue) val).asObject();
        } else if (type instanceof FXLocal.ClassType) {
            FXLocal.ClassType classType = (FXLocal.ClassType) type;
            if (!classType.isJfxType()) {
                // Return Java values as Java objects instead of FX Script wrappers
                return ((FXLocal.ObjectValue) val).asObject();
            }
        }
        // Sequence, etc. that we don't want to convert
        return val;
    }

    //----------------------------------------------------------------------
    // Internals only below this point
    //

    // Need to know about certain primitive types
    protected FXType voidType;
    protected FXType booleanType;
    protected FXType integerType;
    protected FXType numberType;
}
