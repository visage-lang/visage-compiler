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

//
// CODING/NAMING RESTRICTIONS - In a perfect world, all FX classes would inherit 
// from FXBase.  However, this is not the case.  It's also possible to inherit
// from pure java classes. To accommodate this requirement, FXBase and FXObject
// are under some strict coding conventions.
//
// When an FX class inherits from a java class, then all instance fields from
// FXBase are cloned into the FX class, and accessor functions constructed for
// them.  Therefore;
//
//   - All non-static fields defined in FXBase should have a '$' in the name.
//     That '$' must not be the first character, to avoid conflict with user
//     vars.
//   - All non-static fields must have accessor methods defined in FXBase.
//     The names of the accessors must be in the form 'get' + fieldName and
//     'set' + varName.
//   - The accessor method declarations should be added to FXObject, so that 
//     java inheriting classes can define their own interface implementations.
//
//  Ex.
//      
//    In FXBase we define;
//      
//       MyClass myField$;
//      
//       public MyClass getmyField$() {
//           return myField$;
//       }
//      
//       public void setmyField$(final MyClass value) {
//           myField$ = value;
//       }
//
//     In FXObject we declare;
//
//       public MyClass getmyField$();
//       public void setmyField$(final MyClass value);
//
// When an FX class inherits from a java class, all non-static methods are 
// cloned into the FX class, with bodies that call the FXBase static version of
// method, inserting 'this' as the first argument.  Therefore;
//
//   - All functionality in FXBase should be defined in static methods, 
//     manipulating an FXObject.  The declaration of the method should have an
//     an FXObject first argument.  '$' naming conventions apply.
//   - A non-static method should be defined to relay 'this' and remaining 
//     arguments thru to the static methods.
//   - The declaration of the non-static method should be added to FXObject.
//
//  Ex.
//      
//    In FXBase we define;
//      
//       public int addIt$(int n) {
//           return addIt$(this, n);
//       }
//
//       public static int addIt$(FXObject obj, int n) {
//           return obj.count$() + n;
//       }
//
//     In FXObject we declare;
//
//       public int addIt$(int n);
//
// All supplementary initialization for FXBase objects should be added to
// the static version of initFXBase$.
// 

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

    // dependents management
    public DependentsManager DependentsManager$;
    public DependentsManager getDependentsManager$() {
        return DependentsManager$;
    }
    public void setDependentsManager$(final DependentsManager depMgr) {
        DependentsManager$ = depMgr;
    }
    public void addDependent$(final int varNum, FXObject dep) {
        addDependent$(this, varNum, dep);
    }
    public static void addDependent$(FXObject obj, final int varNum, FXObject dep) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        DependentsManager.get(obj).addDependent(obj, varNum, dep);
    }
    public void removeDependent$(final int varNum, FXObject dep) {
        removeDependent$(this, varNum, dep);
    }
    public static void removeDependent$(FXObject obj, final int varNum, FXObject dep) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        DependentsManager.get(obj).removeDependent(obj, varNum, dep);
    }
    public void notifyDependents$(final int varNum) {
        notifyDependents$(this, varNum);
    }
    public static void notifyDependents$(FXObject obj, final int varNum) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        DependentsManager.get(obj).notifyDependents(obj, varNum);
    }
    public void update$(FXObject src, final int varNum) {
        update$(this, src, varNum);
    }
    public static void update$(FXObject obj, FXObject src, final int varNum) {
    }
    public int getListenerCount$() {
        return getListenerCount$(this);
    }
    public static int getListenerCount$(FXObject src) {
        return DependentsManager.get(src).getListenerCount(src);
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
    
    public void addTriggers$() {
        addTriggers$(this);
    }
    public static void addTriggers$(FXObject obj) {
    }
    
    public static int VCNT$() { return VCNT$; }

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
