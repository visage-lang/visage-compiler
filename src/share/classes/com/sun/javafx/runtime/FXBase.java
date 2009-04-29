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
 *
 * @author Jim Laskey
 */
public class FXBase implements FXObject {
    // First class base and count.
    public static int VBASE$ = 0;
    public static int VCNT$ = 0;

    public FXBase()              { this(false); }
    public FXBase(boolean dummy) {}
    
    public void initialize$ () {
        addTriggers$();
        applyDefaults$();
        userInit$();
        postInit$();
    }
    
    public void addTriggers$  () {}
    public void applyDefaults$()                       {}
    public void userInit$     () {}
    public void postInit$     () {}
    
    public int      count$()                         { return 0; }
    public boolean  applyDefaults$(final int varNum) { return false; }
    public boolean  isInitialized$(final int varNum) { return true; }
    public Location loc$(final int varNum)           { return null; }
    
    public static short [] makeInitMap$(int count, int... offsets) {
        final short [] map = new short[count];
        for (int i = 0; i < offsets.length; i++) {
            map[offsets[i]] = (short)(i + 1);
        }
        return map;
    }
}
