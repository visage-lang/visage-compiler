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

/** Context for reflective operations.
 * All the various operations are based on a {@code ReflectionContext}.
 * This is similar to JDI's {@code VirtualMachine} interface.
 * In "normal" useage there is a single {@code ReflectionContext} that is
 * basically a wrapper around {@code java.lang.reflect}, but (for
 * example) for remote reflection you could have an implementation
 * based on JDI.
 * Corresponds to {@code com.sun.jdi.VirtualMachine}.
 */

public abstract class ReflectionContext {
    /** Find context-dependent default {@code ReflectionContext}.
     * (For now, this always returns the same {@code LocalReflectionContext}.)
     */
    public static ReflectionContext getInstance() {
        // For now - later might do some more fancy searching.
        return LocalReflectionContext.getInstance();
    }

    protected ReflectionContext() {
    }

    public static final String INTERFACE_SUFFIX = "$Intf";
    public static final String FXOBJECT_NAME =
            "com.sun.javafx.runtime.FXObject";
    
    /** Get the {@code ClassRef} for the class with the given name. */
    public abstract ClassRef findClass(String name);
    TypeRef anyType = findClass("java.lang.Object");

    /** Get the {@code TypeRef} for the "any" type. */
    public TypeRef getAnyType() { return anyType; }

    /** Get the run-time representation of the JavaXF {@code Integer} type. */
    public abstract TypeRef getIntegerType();

    /** Get the run-time representation of the JavaXF {@code Number} type. */
    public abstract TypeRef getNumberType();

    /** Create a helper object for building a sequence value. */
    public abstract SequenceBuilder makeSequenceBuilder (TypeRef elementType);

    public ValueRef makeSequence(TypeRef elementType, ValueRef... values) {
        SequenceBuilder builder = makeSequenceBuilder(elementType);
            for (int i = 0; i <= values.length; i++)
                builder.append(values[i]);
        return builder.getSequence();
    }

    public abstract ValueRef mirrorOf (int value);

    public abstract ValueRef mirrorOf (double value);

    public abstract ValueRef mirrorOf (String value);
}
