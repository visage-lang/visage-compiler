/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
 *
 * @author Per Bothner
 * @profile desktop
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

    public static final String MIXIN_SUFFIX = "$Mixin";
    public static final String FXOBJECT_NAME =
            "com.sun.javafx.runtime.FXObject";
    public static final String FXMIXIN_NAME =
            "com.sun.javafx.runtime.FXMixin";
    public static final String FXBASE_NAME =
            "com.sun.javafx.runtime.FXBase";
    
    /** Get the {@code FXClassType} for the class with the given name. */
    public abstract FXClassType findClass(String name);
    FXType anyType = findClass("java.lang.Object");

    /** Get the {@code FXType} for the "any" type. */
    public FXType getAnyType() { return anyType; }

    public FXPrimitiveType getPrimitiveType(String typeName) {
        if (typeName.startsWith("java.lang."))
            typeName = typeName.substring(10);
        else if (typeName.indexOf('.') >= 0)
            return null;
        if (typeName.equals("Boolean"))
            return getBooleanType();
        if (typeName.equals("Character"))
            return getCharacterType();
        if (typeName.equals("Byte"))
            return getByteType();
        if (typeName.equals("Short"))
            return getShortType();
        if (typeName.equals("Integer") || typeName.equals("Int"))
            return getIntegerType();
        if (typeName.equals("Long"))
            return getLongType();
        if (typeName.equals("Float"))
            return getFloatType();
        if (typeName.equals("Double"))
            return getDoubleType();
        if (typeName.equals("Void"))
            return FXPrimitiveType.voidType;
        return null;
    }

    /** Get the run-time representation of the JavaFX {@code Boolean} type. */
    public FXPrimitiveType getBooleanType() {
        return FXPrimitiveType.booleanType;
    }

    /** Get the run-time representation of the JavaFX {@code Character} type. */
    public FXPrimitiveType getCharacterType() {
        return FXPrimitiveType.charType;
    }

    /** Get the run-time representation of the JavaFX {@code Byte} type. */
    public FXPrimitiveType getByteType() {
        return FXPrimitiveType.byteType;
    }

     /** Get the run-time representation of the JavaFX {@code Short} type. */
    public FXPrimitiveType getShortType() {
        return FXPrimitiveType.shortType;
    }

   /** Get the run-time representation of the JavaFX {@code Integer} type. */
    public FXPrimitiveType getIntegerType() {
        return FXPrimitiveType.integerType;
    }

   /** Get the run-time representation of the JavaFX {@code Long} type. */
    public FXPrimitiveType getLongType() {
        return FXPrimitiveType.longType;
    }

     public FXPrimitiveType getFloatType() {
        return FXPrimitiveType.floatType;
    }

    public FXPrimitiveType getDoubleType() {
        return FXPrimitiveType.doubleType;
    }

   /** Get the run-time representation of the JavaFX {@code Number} type. */
    public FXPrimitiveType getNumberType() {
        return getFloatType();
    }

    public FXClassType getStringType() {
        return findClass("java.lang.String");
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
            for (int i = 0; i < values.length; i++)
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
     *    (We require that {@code nvalues <= values.length}.)
     * @param elementType
     * @return the new sequence value
     */
    public FXValue makeSequenceValue(FXValue[] values, int nvalues, FXType elementType) {
        return new FXSequenceValue(values, nvalues, elementType);
    }
    
    /* Create an {@code Boolean} value from a {@code boolean}. */
    public FXLocal.Value mirrorOf (boolean value) {
        return new FXBooleanValue(value, getBooleanType());
    }

    /* Create an {@code Integer} value from an {@code char}. */
    public FXLocal.Value mirrorOf (char value)  {
        return new FXIntegerValue(value, getCharacterType());
    }

    /* Create an {@code Integer} value from an {@code int}. */
    public FXLocal.Value mirrorOf (byte value)  {
        return new FXIntegerValue(value, getByteType());
    }

    public FXLocal.Value mirrorOf (short value)  {
        return new FXIntegerValue(value, getShortType());
    }

    /* Create an {@code Integer} value from an {@code int}. */
    public FXLocal.Value mirrorOf (int value)  {
        return new FXIntegerValue(value, getIntegerType());
    }

    public FXLocal.Value mirrorOf (long value)  {
        return new FXLongValue(value, getLongType());
    }

    /* Create an {@code Float} value from a {@code float}. */
    public FXLocal.Value mirrorOf (float value) {
        return new FXFloatValue(value, getFloatType());
    }

    /* Create an {@code Double} value from a {@code double}. */
    public FXLocal.Value mirrorOf (double value) {
        return new FXDoubleValue(value, getDoubleType());
    }

    public abstract FXValue mirrorOf (String value);
}
