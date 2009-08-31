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
    // First class count.
    public static final int VCNT$ = 0;

    /**
     * Number of bits needed for each var.  Must be non-final for binary compatibility.
     */
    public static int VFLGS$BITS_PER_VAR = 4;
    
    /**
     * Number of bits needed for each var.  Must be non-final for binary compatibility.
     */
    public static int VFLGS$VARS_PER_WORD = 32 / VFLGS$BITS_PER_VAR;
    
    /**
     * Var is initialized flag.  Must be non-final for binary compatibility.
     */
    public static final int VFLGS$IS_INITIALIZED = 0;
    
    /**
     * Var is defaults applied flag.  Must be non-final for binary compatibility.
     */
    public static final int VFLGS$IS_DEFAULTS_APPLIED = 1;
    
    /**
     * Var is valid value applied flag.  Must be non-final for binary compatibility.
     */
    public static final int VFLGS$IS_VALID_VALUE = 2;
   
    /**
     * Var has dependents flag.  Must be non-final for binary compatibility.
     */
    public static final int VFLGS$HAS_DEPENDENTS = 3;
    
    /**
     * Status bits for vars.  Array is for bits greater than 32, null if not needed.
     */
    public int   VFLGS$small;
    public int[] VFLGS$large;
    
    /**
     * Allocate var status bits.
     */
    void allocateVarBits$() {
      int count = count$();
      
      if (count > VFLGS$VARS_PER_WORD) {
        int length = (((count * VFLGS$BITS_PER_VAR) + 31) >> 5) - 1;
        VFLGS$large = new int[length];
      }
    }
    
    /**
     * Test a var status bit.
     * @param varNum var identifying offset.
     * @param varBit var status bit number.
     */
    boolean isVarBitSet$(final int varNum, final int varBit) {
      int bit = varNum * VFLGS$BITS_PER_VAR + varBit;
      int shift = bit & 31;
      int word;
      
      if (bit >= 32) {
        int index = (bit >> 5) - 1;
        word = VFLGS$large[index];
      } else {
        word = VFLGS$small;
      }
      
      return ((word >> shift) & 1) != 0;
    }
    
    /**
     * Set a var status bit.  Returns old state.
     * @param varNum var identifying offset.
     * @param varBit var status bit number.
     */
    boolean setVarBit$(final int varNum, final int varBit) {
      int bit = varNum * VFLGS$BITS_PER_VAR + varBit;
      int shift = bit & 31;
      int word;
      
      if (bit >= 32) {
        int index = (bit >> 5) - 1;
        word = VFLGS$large[index];
        VFLGS$large[index] = word | (1 << shift);
      } else {
        word = VFLGS$small;
      }
      
      return ((word >> shift) & 1) != 0;
    }
    
    /**
     * Clear a var status bit.  Returns old state.
     * @param varNum var identifying offset.
     * @param varBit var status bit number.
     */
    boolean clearVarBit$(final int varNum, final int varBit) {
      int bit = varNum * VFLGS$BITS_PER_VAR + varBit;
      int shift = bit & 31;
      int word;
      
      if (bit >= 32) {
        int index = (bit >> 5) - 1;
        word = VFLGS$large[index];
        VFLGS$large[index] = word & ~(1 << shift);
      } else {
        word = VFLGS$small;
      }
      
      return ((word >> shift) & 1) != 0;
    }

    public boolean isInitialized$(final int varNum) {
      return isVarBitSet$(varNum, VFLGS$IS_INITIALIZED);
    }
    public boolean setInitialized$(final int varNum) {
      return setVarBit$(varNum, VFLGS$IS_INITIALIZED);
    }

    public boolean isDefaultsApplied$(final int varNum) {
      return isVarBitSet$(varNum, VFLGS$IS_DEFAULTS_APPLIED);
    }
    public boolean setDefaultsApplied$(final int varNum) {
      return setVarBit$(varNum, VFLGS$IS_DEFAULTS_APPLIED);
    }

    public boolean isValidValue$(final int varNum) {
      return isVarBitSet$(varNum, VFLGS$IS_VALID_VALUE);
    }
    public boolean setValidValue$(final int varNum) {
      return setVarBit$(varNum, VFLGS$IS_VALID_VALUE);
    }
    public boolean clearValidValue$(final int varNum) {
      return clearVarBit$(varNum, VFLGS$IS_VALID_VALUE);
    }

    public boolean hasDependents$(final int varNum) {
      return isVarBitSet$(varNum, VFLGS$HAS_DEPENDENTS);
    }
    public boolean setHasDependents$(final int varNum) {
      return setVarBit$(varNum, VFLGS$HAS_DEPENDENTS);
    }
    public boolean clearHasDependents$(final int varNum) {
      return clearVarBit$(varNum, VFLGS$HAS_DEPENDENTS);
    }

    /**
     * Constructor called from Java or from object literal with no instance variable initializers
     */
    public FXBase() {
      this(false);
    }

    /**
     * Constructor called for a (non-empty) JavaFX object literal.
     * @param dummy Marker only. Ignored.
     */
    public FXBase(boolean dummy) {
      allocateVarBits$();
    }

    public void initialize$() {
        addTriggers$();
        applyDefaults$();
        complete$();
    }

    public void complete$() {
        userInit$();
        postInit$();
    }

    public void applyDefaults$(final int varNum) {}

    public void applyDefaults$() {
        int cnt = count$();
        for (int inx = 0; inx < cnt; inx += 1) { 
            applyDefaults$(inx);
        }
    }

    public static void applyDefaults$(FXObject rcvr) {
        int cnt = rcvr.count$();
        for (int inx = 0; inx < cnt; inx += 1) {
            rcvr.applyDefaults$(inx);
        }
    }
    
    public static int VCNT$() { return VCNT$; }

    public void addTriggers$  () {}
    public void userInit$     () {}
    public void postInit$     () {}

    public int      count$()                         { return VCNT$(); }
    public Location loc$(final int varNum)           { return null; }
    
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
