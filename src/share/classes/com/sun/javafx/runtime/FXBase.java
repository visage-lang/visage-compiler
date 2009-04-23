/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;

/**
 * Base class for most FX classes.  The exception being classes that inherit from java classes.
 * Any instance method created here should also have a static equivalent so it can be called 
 * around java inheritance.
 *
 * @author Jim Laskey
 */
public class FXBase implements FXObject {
    // First class base and count.
    public static final int $VAR_BASE = 0;
    public static final int $VAR_COUNT = 0;

    // The following are the end of the road for methods generated by translation.

    public FXBase() { this(false); }
    public FXBase(boolean dummy) {}
    
    public void initialize$() {}
    public void addTriggers$() {}
    public void userInit$() {}
    public void postInit$() {}

    public static void initialize$(Object receiver) {}
    public static void addTriggers$(Object receiver) {}
    public static void userInit$(Object receiver) {}
    public static void postInit$(Object receiver) {}
    
    public boolean isInitialized$(final int varNum) { return true; }
    public void applyDefaults$() {}
    public Object getDependency$(final int varNum) { return null; }
}
