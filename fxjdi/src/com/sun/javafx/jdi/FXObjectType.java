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

package com.sun.javafx.jdi;

import com.sun.jdi.InterfaceType;
import com.sun.jdi.Method;

/**
 * This class represents com.sun.javafx.runtime.FXObject interface type.
 *
 * @author sundar
 */
public class FXObjectType extends FXInterfaceType {
    private Method count$Method;
    private Method get$Method;
    private Method set$Method;
    private Method getType$Method;
    private Method getFlags$Method;
    private Method setFlags$Method;

    public FXObjectType(FXVirtualMachine fxvm, InterfaceType underlying) {
        super(fxvm, underlying);
        if (! underlying.name().equals(FXVirtualMachine.FX_OBJECT_TYPE_NAME)) {
            throw new IllegalArgumentException("Illegal underlying type: " + underlying);
        }
        count$Method = methodsByName("count$").get(0);
        get$Method = methodsByName("get$").get(0);
        set$Method = methodsByName("set$").get(0);
        getType$Method = methodsByName("getType$").get(0);
        getFlags$Method = methodsByName("getFlags$").get(0);
        setFlags$Method = methodsByName("setFlags$").get(0);
    }

    public Method count$Method() {
        return count$Method;
    }

    public Method get$Method() {
        return get$Method;
    }

    public Method set$Method() {
        return set$Method;
    }

    public Method getType$Method() {
        return getType$Method;
    }

    public Method getFlags$Method() {
        return getFlags$Method;
    }

    public Method setFlags$Method() {
        return setFlags$Method;
    }
}