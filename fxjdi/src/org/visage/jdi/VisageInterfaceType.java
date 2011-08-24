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

package org.visage.jdi;

import com.sun.jdi.ClassType;
import com.sun.jdi.InterfaceType;
import java.util.List;

/**
 *
 * @author sundar
 */
public class VisageInterfaceType extends VisageReferenceType implements InterfaceType {
    public VisageInterfaceType(VisageVirtualMachine fxvm, InterfaceType ifaceType) {
        super(fxvm, ifaceType);
    }

    public List<ClassType> implementors() {
        return VisageWrapper.wrapClassTypes(virtualMachine(), underlying().implementors());
    }

    public List<InterfaceType> subinterfaces() {
        return VisageWrapper.wrapInterfaceTypes(virtualMachine(), underlying().subinterfaces());
    }

    public List<InterfaceType> superinterfaces() {
        return VisageWrapper.wrapInterfaceTypes(virtualMachine(), underlying().superinterfaces());
    }

    @Override
    protected InterfaceType underlying() {
        return (InterfaceType) super.underlying();
    }

    private boolean isIsFxTypeSet = false;
    private boolean isFXType = false; 
    /**
     * JDI addition: Determines if this is a Visage type.
     *
     * @return <code>true</code> if this is a Visage type; false otherwise.
     */
    public boolean isVisageType() {
        if (!isIsFxTypeSet) {
            isIsFxTypeSet = true;
            VisageVirtualMachine fxvm = virtualMachine();
            InterfaceType fxObjType = (InterfaceType) VisageWrapper.unwrap(fxvm.fxObjectType());
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
