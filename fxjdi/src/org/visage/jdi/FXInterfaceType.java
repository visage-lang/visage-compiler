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

package com.sun.visage.jdi;

import com.sun.jdi.ClassType;
import com.sun.jdi.InterfaceType;
import java.util.List;

/**
 *
 * @author sundar
 */
public class FXInterfaceType extends FXReferenceType implements InterfaceType {
    public FXInterfaceType(FXVirtualMachine fxvm, InterfaceType ifaceType) {
        super(fxvm, ifaceType);
    }

    public List<ClassType> implementors() {
        return FXWrapper.wrapClassTypes(virtualMachine(), underlying().implementors());
    }

    public List<InterfaceType> subinterfaces() {
        return FXWrapper.wrapInterfaceTypes(virtualMachine(), underlying().subinterfaces());
    }

    public List<InterfaceType> superinterfaces() {
        return FXWrapper.wrapInterfaceTypes(virtualMachine(), underlying().superinterfaces());
    }

    @Override
    protected InterfaceType underlying() {
        return (InterfaceType) super.underlying();
    }

    private boolean isIsFxTypeSet = false;
    private boolean isFXType = false; 
    /**
     * JDI addition: Determines if this is a JavaFX type.
     *
     * @return <code>true</code> if this is a JavaFX type; false otherwise.
     */
    public boolean isJavaFXType() {
        if (!isIsFxTypeSet) {
            isIsFxTypeSet = true;
            FXVirtualMachine fxvm = virtualMachine();
            InterfaceType fxObjType = (InterfaceType) FXWrapper.unwrap(fxvm.fxObjectType());
            if (fxObjType != null) {
                InterfaceType thisType = underlying();
                List<InterfaceType> allIfaces = thisType.superinterfaces();
                for (InterfaceType iface : allIfaces) {
                    if (iface.equals(fxObjType)) {
                        isFXType = true;
                        break;
                    }
                }
            }
        }
        return isFXType;
    }
}
