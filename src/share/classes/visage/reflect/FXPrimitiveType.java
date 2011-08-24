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

/** Represent a JVM primitive type.
 *
 * @author Per Bothner
 * @profile desktop
 */
public class VisagePrimitiveType extends VisageType {
    Class clas;
    String name;
    VisagePrimitiveType(Class clas, String name) {
        this.clas = clas;
        this.name = name;
    }
    public String getName() { return name; }

    static final VisagePrimitiveType voidType =
        new VisagePrimitiveType(Void.TYPE, "Void");

   static final VisagePrimitiveType byteType =
        new VisagePrimitiveType(Byte.TYPE, "Byte");

    static final VisagePrimitiveType shortType =
        new VisagePrimitiveType(Short.TYPE, "Short");

    static final VisagePrimitiveType integerType =
        new VisagePrimitiveType(Integer.TYPE, "Integer");

    static final VisagePrimitiveType longType =
        new VisagePrimitiveType(Byte.TYPE, "Long");

    static final VisagePrimitiveType floatType =
        new VisagePrimitiveType(Float.TYPE, "Float");
    
    static final VisagePrimitiveType doubleType =
        new VisagePrimitiveType(Double.TYPE, "Double");

    static final VisagePrimitiveType charType =
        new VisagePrimitiveType(Character.TYPE, "Character");

    static final VisagePrimitiveType booleanType =
        new VisagePrimitiveType(Boolean.TYPE, "Boolean");

    static final VisagePrimitiveType numberType = floatType;

    public VisagePrimitiveValue mirrorOf(Object value) {
        if (this == integerType || this == shortType || this == byteType ||
                this == charType)
            return new VisageIntegerValue(((Number) value).intValue(), this);
        if (this == longType)
            return new VisageLongValue(((Number) value).longValue(), this);
        if (this == floatType)
            return new VisageFloatValue(((Number) value).floatValue(), this);
        if (this == doubleType)
            return new VisageDoubleValue(((Number) value).doubleValue(), this);
        if (this == booleanType)
            return new VisageBooleanValue(((Boolean) value).booleanValue(), this);
        return null; // Should never happen.
    }
};
