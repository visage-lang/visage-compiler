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

import com.sun.jdi.ReferenceType;
import com.sun.jdi.TypeComponent;

/**
 *
 * @author sundar
 */
public class FXTypeComponent extends FXMirror implements TypeComponent {
    public FXTypeComponent(FXVirtualMachine fxvm, TypeComponent underlying) {
        super(fxvm, underlying);
    }

    public FXReferenceType declaringType() {
        return FXWrapper.wrap(virtualMachine(), underlying().declaringType());
    }

    public String genericSignature() {
        return underlying().genericSignature();
    }

    public boolean isFinal() {
        return underlying().isFinal();
    }

    public boolean isStatic() {
        return underlying().isStatic();
    }

    public boolean isSynthetic() {
        return underlying().isSynthetic();
    }

    public String name() {
        String realName = underlying().name();
        if (realName.charAt(0) == '$') {
            return realName.substring(1);
        }
        return realName;
    }

    public String signature() {
        return underlying().signature();
    }

    public boolean isPackagePrivate() {
        return underlying().isPackagePrivate();
    }

    public boolean isPrivate() {
        return underlying().isPrivate();
    }

    public boolean isProtected() {
        return underlying().isProtected();
    }

    public boolean isPublic() {
        return underlying().isPublic();
    }

    public int modifiers() {
        return underlying().modifiers();
    }

    @Override
    protected TypeComponent underlying() {
        return (TypeComponent) super.underlying();
    }
}
