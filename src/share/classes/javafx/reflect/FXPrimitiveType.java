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

class FXPrimitiveType extends FXType {
    Class clas;
    String name;
    FXPrimitiveType(Class clas, String name) {
        this.clas = clas;
        this.name = name;
    }
    public String getName() { return name; }

    static final FXType voidType =
        new FXPrimitiveType(Void.TYPE, "Void");

    static final FXType integerType =
        new FXPrimitiveType(Integer.TYPE, "Integer");

    static final FXType numberType =
        new FXPrimitiveType(Double.TYPE, "Number");

    static final FXType byteType =
        new FXPrimitiveType(Byte.TYPE, "JavaByte");

    static final FXType shortType =
        new FXPrimitiveType(Short.TYPE, "JavaShort");

    static final FXType longType =
        new FXPrimitiveType(Byte.TYPE, "JavaLong");

    static final FXType floatType =
        new FXPrimitiveType(Float.TYPE, "JavaFloat");
    
    static final FXType charType =
        new FXPrimitiveType(Character.TYPE, "JavaChar");
    static final FXType booleanType =
        new FXPrimitiveType(Boolean.TYPE, "JavaBoolean");
};
