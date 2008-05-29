/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.code;
import com.sun.tools.javac.code.Flags;
/**
 * Some Javafx-specific flags for {@code Symbols}'s {@code flags_field}.
 *
 * @author llitchev
 * @author Per Bothner
 */
public class JavafxFlags {
    private JavafxFlags() {}

    // FIXME - should in Flags.java
    protected static final long LAST_JAVA_FLAG = Flags.PROPRIETARY;

    public static final long ASSIGNED_TO = LAST_JAVA_FLAG << 1;
    public static final long INNER_ACCESS = LAST_JAVA_FLAG << 2;
    public static final long BOUND = LAST_JAVA_FLAG << 3;
    public static final long IN_INITIALIZER = LAST_JAVA_FLAG << 4;
    
    /** If this is a class that gets translated to a class and an inteface.
     * (This is used to implement multiple inheritance.)
     */
    public static final long COMPOUND_CLASS = ASSIGNED_TO;
    
    public static final long FX_CLASS = BOUND;
}
