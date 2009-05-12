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

/** Represent a JVM primitive type.
 *
 * @author Per Bothner
 * @profile desktop
 */
public class FXPrimitiveType extends FXType {
    Class clas;
    String name;
    FXPrimitiveType(Class clas, String name) {
        this.clas = clas;
        this.name = name;
    }
    public String getName() { return name; }

    static final FXPrimitiveType voidType =
        new FXPrimitiveType(Void.TYPE, "Void");

   static final FXPrimitiveType byteType =
        new FXPrimitiveType(Byte.TYPE, "Byte");

    static final FXPrimitiveType shortType =
        new FXPrimitiveType(Short.TYPE, "Short");

    static final FXPrimitiveType integerType =
        new FXPrimitiveType(Integer.TYPE, "Integer");

    static final FXPrimitiveType longType =
        new FXPrimitiveType(Byte.TYPE, "Long");

    static final FXPrimitiveType floatType =
        new FXPrimitiveType(Float.TYPE, "Float");
    
    static final FXPrimitiveType doubleType =
        new FXPrimitiveType(Double.TYPE, "Double");

    static final FXPrimitiveType charType =
        new FXPrimitiveType(Character.TYPE, "Character");

    static final FXPrimitiveType booleanType =
        new FXPrimitiveType(Boolean.TYPE, "Boolean");

    static final FXPrimitiveType numberType = floatType;

    public FXPrimitiveValue mirrorOf(Object value) {
        if (this == integerType || this == shortType || this == byteType ||
                this == charType)
            return new FXIntegerValue(((Number) value).intValue(), this);
        if (this == longType)
            return new FXLongValue(((Number) value).longValue(), this);
        if (this == floatType)
            return new FXFloatValue(((Number) value).floatValue(), this);
        if (this == doubleType)
            return new FXDoubleValue(((Number) value).doubleValue(), this);
        if (this == booleanType)
            return new FXBooleanValue(((Boolean) value).booleanValue(), this);
        return null; // Should never happen.
    }
};
