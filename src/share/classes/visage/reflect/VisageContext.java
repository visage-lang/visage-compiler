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

package visage.reflect;

/** Context for reflective operations.
 * All the various operations are based on a {@code VisageContext}.
 * This is similar to JDI's {@code VirtualMachine} interface.
 * In "normal" useage there is a single {@code VisageContext} that is
 * basically a wrapper around {@code java.lang.reflect}, but (for
 * example) for remote reflection you could have an implementation
 * based on JDI.
 * Corresponds to {@code com.sun.jdi.VirtualMachine}.
 *
 * @author Per Bothner
 * @profile desktop
 */

public abstract class VisageContext {
    /** Find context-dependent default {@code VisageContext}.
     * (For now, this always returns the same {@code LocalReflectionContext}.)
     */
    public static VisageContext getInstance() {
        // For now - later might do some more fancy searching.
        return VisageLocal.getContext();
    }

    protected VisageContext() {
    }

    public static final String MIXIN_SUFFIX = "$Mixin";
    public static final String VISAGEOBJECT_NAME =
            "org.visage.runtime.VisageObject";
    public static final String VISAGEMIXIN_NAME =
            "org.visage.runtime.VisageMixin";
    public static final String VISAGEBASE_NAME =
            "org.visage.runtime.VisageBase";
    
    /** Get the {@code VisageClassType} for the class with the given name. */
    public abstract VisageClassType findClass(String name);
    VisageType anyType = findClass("java.lang.Object");

    /** Get the {@code VisageType} for the "any" type. */
    public VisageType getAnyType() { return anyType; }

    public VisagePrimitiveType getPrimitiveType(String typeName) {
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
            return VisagePrimitiveType.voidType;
        return null;
    }

    /** Get the run-time representation of the Visage {@code Boolean} type. */
    public VisagePrimitiveType getBooleanType() {
        return VisagePrimitiveType.booleanType;
    }

    /** Get the run-time representation of the Visage {@code Character} type. */
    public VisagePrimitiveType getCharacterType() {
        return VisagePrimitiveType.charType;
    }

    /** Get the run-time representation of the Visage {@code Byte} type. */
    public VisagePrimitiveType getByteType() {
        return VisagePrimitiveType.byteType;
    }

     /** Get the run-time representation of the Visage {@code Short} type. */
    public VisagePrimitiveType getShortType() {
        return VisagePrimitiveType.shortType;
    }

   /** Get the run-time representation of the Visage {@code Integer} type. */
    public VisagePrimitiveType getIntegerType() {
        return VisagePrimitiveType.integerType;
    }

   /** Get the run-time representation of the Visage {@code Long} type. */
    public VisagePrimitiveType getLongType() {
        return VisagePrimitiveType.longType;
    }

     public VisagePrimitiveType getFloatType() {
        return VisagePrimitiveType.floatType;
    }

    public VisagePrimitiveType getDoubleType() {
        return VisagePrimitiveType.doubleType;
    }

   /** Get the run-time representation of the Visage {@code Number} type. */
    public VisagePrimitiveType getNumberType() {
        return getFloatType();
    }

    public VisageClassType getStringType() {
        return findClass("java.lang.String");
    }

    /** Get the run-time representation of the Visage {@code Void} type. */
    public VisagePrimitiveType getVoidType() {
        return VisagePrimitiveType.voidType;
    }

    /** Create a helper object for building a sequence value. */
    public VisageSequenceBuilder makeSequenceBuilder(VisageType elementType) {
        return new VisageSequenceBuilder(this, elementType);
    }

    /** Create a sequence value from one or more VisageValues.
     * Concatenates all of the input values (which might be themselves
     * sequences or null).
     * @param elementType
     * @param values the values to be concatenated
     * @return the new sequence value
     */
    public VisageValue makeSequence(VisageType elementType, VisageValue... values) {
        VisageSequenceBuilder builder = makeSequenceBuilder(elementType);
            for (int i = 0; i < values.length; i++)
                builder.append(values[i]);
        return builder.getSequence();
    }

    /** Create a sequence value from an array of singleton VisageValues.
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
    public VisageValue makeSequenceValue(VisageValue[] values, int nvalues, VisageType elementType) {
        return new VisageSequenceValue(values, nvalues, elementType);
    }
    
    /* Create an {@code Boolean} value from a {@code boolean}. */
    public VisageLocal.Value mirrorOf (boolean value) {
        return new VisageBooleanValue(value, getBooleanType());
    }

    /* Create an {@code Integer} value from an {@code char}. */
    public VisageLocal.Value mirrorOf (char value)  {
        return new VisageIntegerValue(value, getCharacterType());
    }

    /* Create an {@code Integer} value from an {@code int}. */
    public VisageLocal.Value mirrorOf (byte value)  {
        return new VisageIntegerValue(value, getByteType());
    }

    public VisageLocal.Value mirrorOf (short value)  {
        return new VisageIntegerValue(value, getShortType());
    }

    /* Create an {@code Integer} value from an {@code int}. */
    public VisageLocal.Value mirrorOf (int value)  {
        return new VisageIntegerValue(value, getIntegerType());
    }

    public VisageLocal.Value mirrorOf (long value)  {
        return new VisageLongValue(value, getLongType());
    }

    /* Create an {@code Float} value from a {@code float}. */
    public VisageLocal.Value mirrorOf (float value) {
        return new VisageFloatValue(value, getFloatType());
    }

    /* Create an {@code Double} value from a {@code double}. */
    public VisageLocal.Value mirrorOf (double value) {
        return new VisageDoubleValue(value, getDoubleType());
    }

    public abstract VisageValue mirrorOf (String value);
}
