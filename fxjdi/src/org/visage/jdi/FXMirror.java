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

import com.sun.jdi.Mirror;

/**
 *
 * @author sundar
 */
public class FXMirror implements Mirror {
    private final FXVirtualMachine fxvm;
    private final Mirror underlying;

    public FXMirror(FXVirtualMachine fxvm, Mirror underlying) {
        if (underlying == null) {
            throw new NullPointerException("underlying Mirror object is null!");
        }
        if (underlying instanceof FXMirror) {
            throw new IllegalArgumentException("repeated wrapping!!");
        }
        this.fxvm = fxvm;
        this.underlying = underlying;
    }

    public FXVirtualMachine virtualMachine() {
        return fxvm;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FXMirror) {
            o = ((FXMirror)o).underlying();
        }
        return underlying().equals(o);
    }

    @Override
    public int hashCode() {
        return underlying().hashCode();
    }

    @Override
    public String toString() {
        return underlying().toString();
    }

    protected Mirror underlying() {
        return underlying;
    }
}