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


/**
 * Base class for most FX classes.  The exception being classes that inherit from Java classes.
 *
 * @author Jim Laskey
 * @author Robert Field
 */
public class FXBase implements FXObject {
    /**
     * Initialize for FXBase.
     */
    public void initFXBase$() {
        initFXBase$(this);
    }
    public static void initFXBase$(FXObject obj) {
        allocateVarBits$(obj);
    }

    // First class count.
    public static final int VCNT$ = 0;
    public int count$() { return VCNT$(); }

    /**
     * Status bits for vars.  Array is for bits greater than 32, null if not needed.
     */
    public int   VFLGS$small;
    public int[] VFLGS$large;
    public int   getVFLGS$small()            { return VFLGS$small; }
    public void  setVFLGS$small(int small)   { VFLGS$small = small; }
    public int[] getVFLGS$large()            { return VFLGS$large; }
    public void  setVFLGS$large(int[] large) { VFLGS$large = large; }
    
    /**
     * Allocate var status bits.
     */
    private void allocateVarBits$() {
        allocateVarBits$(this);
    }
    private static void allocateVarBits$(FXObject obj) {
        int count = obj.count$();
      
        if (count > VFLGS$VARS_PER_WORD) {
            int length = (((count * VFLGS$BITS_PER_VAR) + 31) >> 5) - 1;
            obj.setVFLGS$large(new int[length]);
        }
    }
    
    /**
     * Test a var status bit.
     * @param varNum var identifying offset.
     * @param varBit var status bit number.
     */
    private boolean isVarBitSet$(final int varNum, final int varBit) {
        return isVarBitSet$(this, varNum, varBit);
    }
    private static boolean isVarBitSet$(FXObject obj, final int varNum, final int varBit) {
        int bit = varNum * VFLGS$BITS_PER_VAR + varBit;
        int shift = bit & 31;
        int word;
      
        if (bit >= 32) {
            int index = (bit >> 5) - 1;
            int[] large = obj.getVFLGS$large();
            word = large[index];
        } else {
            word = obj.getVFLGS$small();
        }
      
        return ((word >> shift) & 1) != 0;
    }
    
    /**
     * Set a var status bit.  Returns old state.
     * @param varNum var identifying offset.
     * @param varBit var status bit number.
     */
    private boolean setVarBit$(final int varNum, final int varBit) {
        return setVarBit$(this, varNum, varBit);
    }
    private static boolean setVarBit$(FXObject obj, final int varNum, final int varBit) {
        int bit = varNum * VFLGS$BITS_PER_VAR + varBit;
        int shift = bit & 31;
        int word;
      
        if (bit >= 32) {
            int index = (bit >> 5) - 1;
            int[] large = obj.getVFLGS$large();
            word = large[index];
            large[index] = word | (1 << shift);
        } else {
            word = obj.getVFLGS$small();
            obj.setVFLGS$small(word | (1 << shift));
        }
      
        return ((word >> shift) & 1) != 0;
    }
    
    /**
     * Clear a var status bit.  Returns old state.
     * @param varNum var identifying offset.
     * @param varBit var status bit number.
     */
    private boolean clearVarBit$(final int varNum, final int varBit) {
        return clearVarBit$(this, varNum, varBit);
    }
    private static boolean clearVarBit$(FXObject obj, final int varNum, final int varBit) {
      int bit = varNum * VFLGS$BITS_PER_VAR + varBit;
      int shift = bit & 31;
      int word;
      
        if (bit >= 32) {
            int index = (bit >> 5) - 1;
            int[] large = obj.getVFLGS$large();
            word = large[index];
            large[index] = word & ~(1 << shift);
        } else {
            word = obj.getVFLGS$small();
            obj.setVFLGS$small(word & ~(1 << shift));
        }
      
      return ((word >> shift) & 1) != 0;
    }

    public boolean isInitialized$(final int varNum) {
        return isInitialized$(this, varNum);
    }
    public static boolean isInitialized$(FXObject obj, final int varNum) {
        return isVarBitSet$(obj, varNum, VFLGS$IS_INITIALIZED);
    }
    public boolean setInitialized$(final int varNum) {
        return setInitialized$(this, varNum);
    }
    public static boolean setInitialized$(FXObject obj, final int varNum) {
        return setVarBit$(obj, varNum, VFLGS$IS_INITIALIZED);
    }
    
    public boolean isDefaultsApplied$(final int varNum) {
        return isDefaultsApplied$(this, varNum);
    }
    public static boolean isDefaultsApplied$(FXObject obj, final int varNum) {
        return isVarBitSet$(obj, varNum, VFLGS$IS_DEFAULTS_APPLIED);
    }
    public boolean setDefaultsApplied$(final int varNum) {
        return setDefaultsApplied$(this, varNum);
    }
    public static boolean setDefaultsApplied$(FXObject obj, final int varNum) {
        return setVarBit$(obj, varNum, VFLGS$IS_DEFAULTS_APPLIED);
    }

    public boolean isValidValue$(final int varNum) {
        return isValidValue$(this, varNum);
    }
    public static boolean isValidValue$(FXObject obj, final int varNum) {
        return isVarBitSet$(obj, varNum, VFLGS$IS_VALID_VALUE);
    }
    public boolean setValidValue$(final int varNum) {
        return setValidValue$(this, varNum);
    }
    public static boolean setValidValue$(FXObject obj, final int varNum) {
        return setVarBit$(obj, varNum, VFLGS$IS_VALID_VALUE);
    }
    public boolean clearValidValue$(final int varNum) {
        return clearValidValue$(this, varNum);
    }
    public static boolean clearValidValue$(FXObject obj, final int varNum) {
        return clearVarBit$(obj, varNum, VFLGS$IS_VALID_VALUE);
    }

    public boolean isBindee$(final int varNum) {
        return isBindee$(this, varNum);
    }
    public static boolean isBindee$(FXObject obj, final int varNum) {
        return isVarBitSet$(obj, varNum, VFLGS$IS_BINDEE);
    }
    public boolean setBindee$(final int varNum) {
        return setBindee$(this, varNum);
    }
    public static boolean setBindee$(FXObject obj, final int varNum) {
        return setVarBit$(obj, varNum, VFLGS$IS_BINDEE);
    }
    public boolean clearBindee$(final int varNum) {
        return clearBindee$(this, varNum);
    }
    public static boolean clearBindee$(FXObject obj, final int varNum) {
        return clearVarBit$(obj, varNum, VFLGS$IS_BINDEE);
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
        initFXBase$();
    }

    public void initialize$() {
        addTriggers$();
        applyDefaults$();
        complete$();
    }
    public static void initialize$(FXObject obj) {
        obj.addTriggers$();
        obj.applyDefaults$();
        obj.complete$();
    }

    public void complete$() {
        userInit$();
        postInit$();
    }
    public static void complete$(FXObject obj) {
        obj.userInit$();
        obj.postInit$();
    }

    public void applyDefaults$(final int varNum) {}
    public static void applyDefaults$(FXObject obj, final int varNum) {}

    public void applyDefaults$() {
        int cnt = count$();
        for (int inx = 0; inx < cnt; inx += 1) { 
            applyDefaults$(inx);
        }
    }
    public static void applyDefaults$(FXObject obj) {
        int cnt = obj.count$();
        for (int inx = 0; inx < cnt; inx += 1) { 
            obj.applyDefaults$(inx);
        }
    }
    
    public static int VCNT$() { return VCNT$; }

    public void addTriggers$  () {}
    public void userInit$     () {}
    public void postInit$     () {}
    
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
