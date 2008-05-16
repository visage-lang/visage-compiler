/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

/** A run-time representation of a JavaFX type.
 * Corresponds to {@code java.lang.reflect.Type}.
 */

public abstract class TypeRef {
    TypeRef() {
    }

    /** Return name of type, or null ofr an unnamed type. */
    public String getName() {
        return null;
    }
    
    protected void toStringTerse(StringBuilder sb) {
        String name = getName();
        sb.append(name == null ? "<anonymous>" : name);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringTerse(sb);
        return sb.toString();
    }

    /** Get a {@code SequenceTypeRef} using this as the item type. */
    public SequenceTypeRef getSequenceType() {
        return new SequenceTypeRef(this);
    }
    
    public boolean isJfxType() {
        return true;
    }

    /** For now too conservative, if not comapring ClassRef types,
     * use equals.
     */
    public boolean isAssignableFrom(TypeRef cls) {
        if (this instanceof ClassRef && cls instanceof ClassRef)
            return ((ClassRef) this).isAssignableFrom((ClassRef) cls);
        // FIXME
        return equals(cls);
    }
}
