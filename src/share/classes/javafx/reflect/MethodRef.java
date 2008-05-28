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

/** A reference to a function in a class.
 * Corresponds to {@code java.lang.reflect.Method}, or
 * {@code com.sun.jdi.Methods}, respectively.
 */

public abstract class MethodRef implements MemberRef {
    protected MethodRef() {
    }

    /** Associate the method with a receiver object to yield a function. */
    public FunctionValueRef asFunction(final ObjectRef owner) {
        return new FunctionValueRef() {
            public ValueRef apply(ValueRef... arg) {
                return invoke(owner, arg);
            }
            public FunctionTypeRef getType() {
                return MethodRef.this.getType();
            }
            public boolean isNull() { return false; }
        };
    }

    public abstract FunctionTypeRef getType();

    /** Invoke this method on the given receiver and arguments. */
    public abstract ValueRef invoke(ObjectRef owner, ValueRef... arg);
    
        
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function ");
        TypeRef owner = getDeclaringType();
        if (owner != null) {
            String oname = owner.getName();
            if (oname != null) {
                sb.append(oname);
                sb.append('.');
            }
        }
        sb.append(getName());
        getType().toStringRaw(sb);
        return sb.toString();
    }
}

