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
 * Base class for most FX classes.  The exception being classes that inherit from Java classes.
 *
 * @author Jim Laskey
 * @author Robert Field
 */
public class FXBase implements FXObject {
    // First class base.
    public static final int VBASE$ = 0;

    /**
     * Constructor called from Java or from object literal with no instance variable initializers
     */
    public FXBase() {}

    /**
     * Constructor called for a (non-empty) JavaFX object literal.
     * @param dummy Marker only. Ignored.
     */
    public FXBase(boolean dummy) {}

    public void initialize$() {
        addTriggers$();
        applyDefaults$();
        complete$();
    }

    public void complete$() {
        userInit$();
        postInit$();
    }

    public void applyDefaults$new() {
        int cnt = count$();
        for (int inx = 0; inx < cnt; inx += 1) { 
            applyDefaults$(inx);
        }
    }

    public static void applyDefaults$base(FXObject rcvr) {
        int cnt = rcvr.count$();
        for (int inx = 0; inx < cnt; inx += 1) {
            rcvr.applyDefaults$(inx);
        }
    }
    
    public static int VCNT$() { return VBASE$ + 0; }

    public void addTriggers$  () {}
    public void applyDefaults$() {}
    public void userInit$     () {}
    public void postInit$     () {}

    public int      count$()                         { return VCNT$(); }
    public boolean  applyDefaults$(final int varNum) { return false; }
    public boolean  isInitialized$(final int varNum) { return true; }
    public Location loc$(final int varNum)           { return null; }
    
    public String toString() { return super.toString(); }
    
    //
    // makeInitMap$ constructs a field mapping table used in the switch portion
    // of a object literal initialization.  Each entry in the table represents
    // a field in a class.  The value in the slot is zero (no case) or the
    // switch case tag that has a value setting.
    //
    public static short [] makeInitMap$(int count, int... offsets) {
        final short [] map = new short[count];
        for (int i = 0; i < offsets.length; i++) {
            map[offsets[i]] = (short)(i + 1);
        }
        return map;
    }
}
