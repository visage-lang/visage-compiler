/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.code;

/**
 *
 * @author llitchev
 */
public class JavafxFlags {
    // Traslatable to VariableDecl
    public static final int SIMPLE_JAVA = 0;
    public static final int VARIABLE = SIMPLE_JAVA + 1;
    public static final int ATTRIBUTE = VARIABLE + 1;
    public static final int LOCAL_ATTRIBUTE = ATTRIBUTE + 1;
    
    // Translatable to MethodDecl
    public static final int OPERATION = SIMPLE_JAVA + 1;
    public static final int FUNCTION = OPERATION + 1;
    public static final int LOCAL_OPERATION = FUNCTION + 1;
    public static final int LOCAL_FUNCTION = LOCAL_OPERATION +1;
    public static final int TRIGGERNEW = LOCAL_FUNCTION + 1;
    public static final int TRIGGERREPLACE = TRIGGERNEW + 1; // This represents the trigger body.
    public static final int TRIGGER = TRIGGERREPLACE + 1; // This represents the trigger body.
    // TODO: Do we need local triggers??? I don't think so. But meybe we do.
}
