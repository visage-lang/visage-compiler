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
 * All the various operations are based on a {@code FXContext}.
 * This is similar to JDI's {@code VirtualMachine} interface.
 * In "normal" useage there is a single {@code FXContext} that is
 * basically a wrapper around {@code java.lang.reflect}, but (for
 * example) for remote reflection you could have an implementation
 * based on JDI.
 * Corresponds to {@code com.sun.jdi.VirtualMachine}.
 */

public abstract class FXContext {
    /** Find context-dependent default {@code FXContext}.
     * (For now, this always returns the same {@code LocalReflectionContext}.)
     */
    public static FXContext getInstance() {
        // For now - later might do some more fancy searching.
        return FXLocal.getContext();
    }

    protected FXContext() {
    }

    public static final String INTERFACE_SUFFIX = "$Intf";
    public static final String FXOBJECT_NAME =
            "com.sun.javafx.runtime.FXObject";
    
    /** Get the {@code FXClassType} for the class with the given name. */
    public abstract FXClassType findClass(String name);
    FXType anyType = findClass("java.lang.Object");

    /** Get the {@code FXType} for the "any" type. */
    public FXType getAnyType() { return anyType; }

    /** Get the run-time representation of the JavaFX {@code Boolean} type. */
    public FXPrimitiveType getBooleanType() {
        return FXPrimitiveType.booleanType;
    }

    /** Get the run-time representation of the JavaFX {@code Integer} type. */
    public FXPrimitiveType getIntegerType() {
        return FXPrimitiveType.integerType;
    }

    /** Get the run-time representation of the JavaFX {@code Number} type. */
    public FXPrimitiveType getNumberType() {
        return FXPrimitiveType.numberType;
    }

    /** Get the run-time representation of the JavaFX {@code Void} type. */
    public FXPrimitiveType getVoidType() {
        return FXPrimitiveType.voidType;
    }

    /** Create a helper object for building a sequence value. */
    public FXSequenceBuilder makeSequenceBuilder(FXType elementType) {
        return new FXSequenceBuilder(this, elementType);
    }

    /** Create a sequence value from one or more FXValues.
     * Concatenates all of the input values (which might be themselves
     * sequences or null).
     * @param elementType
     * @param values the values to be concatenated
     * @return the new sequence value
     */
    public FXValue makeSequence(FXType elementType, FXValue... values) {
        FXSequenceBuilder builder = makeSequenceBuilder(elementType);
            for (int i = 0; i <= values.length; i++)
                builder.append(values[i]);
        return builder.getSequence();
    }

    /** Create a sequence value from an array of singleton FXValues.
     * This is a low-level routine than {@link #makeSequence},
     * which takes arguments than can be nul or themselves sequences.
     * @param values Input values.  (This array is re-used, not copied.
     *    It must not be modified after the method is called.)
     *    All of the values must be singleton values.
     * @param nvalues Number of items in the sequence.
     *    (We require that {@code nvalues >= values.length}.)
     * @param elementType
     * @return the new sequence value
     */
    public FXValue makeSequenceValue(FXValue[] values, int nvalues, FXType elementType) {
        return new FXSequenceValue(values, nvalues, elementType);
    }
    
    /* Create an {@code Boolean} value from a {@code boolean}. */
    public FXValue mirrorOf (boolean value) {
        return new FXBooleanValue(value, getBooleanType());
    }

    /* Create an {@code Integer} value from an {@code int}. */
    public FXValue mirrorOf (int value)  {
        return new FXIntegerValue(value, getIntegerType());
    }

    /* Create an {@code Number} value from aq {@code double}. */
    public FXValue mirrorOf (double value) {
        return new FXNumberValue(value, getNumberType());
    }

    public abstract FXValue mirrorOf (String value);
}
