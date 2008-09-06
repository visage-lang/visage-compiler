/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.reflect;

/** A run-time represention of a JavaFX attribute in a class.
 * Corresponds to {@code java.lang.reflect.Field},
 * and {@code com.sun.jdi.Field}, respectively.
 */
public abstract class FXVarMember implements FXMember {
    protected FXVarMember() {
    }

    public abstract FXType getType();

    /** Get the value of the attribute in a specified object. */
    public abstract FXValue getValue(FXObjectValue obj);

    /** Set the value of the attribute in a specified object. */
    public abstract void setValue(FXObjectValue obj, FXValue newValue);

    /** Get a handle for the attribute in a specific object. */
    public FXLocation getLocation(FXObjectValue obj) {
        return new FXVarMemberLocation(obj, this);
    }

    protected abstract void initVar(FXObjectValue instance, FXValue value);
    public abstract void initValue(FXObjectValue obj, FXValue ref);
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("var ");
        FXClassType owner = getDeclaringClass();
        if (owner != null) {
            String oname = owner.getName();
            if (oname != null) {
                sb.append(oname);
                sb.append('.');
            }
        }
        sb.append(getName());
        sb.append(':');
        getType().toStringTerse(sb);
        return sb.toString();
    }
}
