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

import java.lang.reflect.Field;

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
    public int   VFLGS$small$internal$;
    public int[] VFLGS$large$internal$;
    public int   getVFLGS$small$internal$()            { return VFLGS$small$internal$; }
    public void  setVFLGS$small$internal$(int small)   { VFLGS$small$internal$ = small; }
    public int[] getVFLGS$large$internal$()            { return VFLGS$large$internal$; }
    public void  setVFLGS$large$internal$(int[] large) { VFLGS$large$internal$ = large; }
    
    /**
     * Allocate var status bits.
     */
    private void allocateVarBits$() {
        allocateVarBits$(this);
    }
    private static void allocateVarBits$(FXObject obj) {
        int count = obj.count$();
      
        if (count > VFLGS$VARS_PER_WORD) {
            if (obj.getVFLGS$large$internal$() == null) {
                int length = (count - 1) / VFLGS$VARS_PER_WORD;
                obj.setVFLGS$large$internal$(new int[length]);
            }
        }
    }
    
    public boolean varTestBits$(final int varNum, int maskBits, int testBits) {
        return varTestBits$(this, varNum, maskBits, testBits);
    }
    public static boolean varTestBits$(FXObject obj, final int varNum, int maskBits, int testBits) {
        int index = varNum / VFLGS$VARS_PER_WORD;
        int slot = varNum % VFLGS$VARS_PER_WORD;
        int shift = slot * VFLGS$BITS_PER_VAR;
        maskBits <<= shift;
        testBits <<= shift;
        int word;
      
        if (index > 0) {
            index--;
            int[] large = obj.getVFLGS$large$internal$();
            word = large[index];
        } else {
            word = obj.getVFLGS$small$internal$();
        }
      
        return (word & maskBits) == testBits;
    }
    
    public boolean varChangeBits$(final int varNum, int clearBits, int setBits) {
        return varChangeBits$(this, varNum, clearBits, setBits);
    }
    public static boolean varChangeBits$(FXObject obj, final int varNum, int clearBits, int setBits) {
        int index = varNum / VFLGS$VARS_PER_WORD;
        int slot = varNum % VFLGS$VARS_PER_WORD;
        int shift = slot * VFLGS$BITS_PER_VAR;
        clearBits <<= shift;
        setBits <<= shift;
        int word;
      
        if (index > 0) {
            index--;
            int[] large = obj.getVFLGS$large$internal$();
            word = large[index];
            large[index] = (word & ~clearBits) | setBits;
        } else {
            word = obj.getVFLGS$small$internal$();
            obj.setVFLGS$small$internal$((word & ~clearBits) | setBits);
        }
        
        int bits = clearBits | setBits;
      
        return (word & bits) == bits;
    }
     
    public void restrictSet$(final int varNum) {
        restrictSet$(this, varNum);
    }
    public static void restrictSet$(FXObject obj, final int varNum) {
        if (varTestBits$(obj, varNum, 0, VFLGS$IS_READONLY)) {
            if (varTestBits$(obj, varNum, 0, VFLGS$IS_BOUND)) {
                throw new AssignToBoundException();
            } else {
                throw new AssignToDefException();
            }
        }
    }

    // dependents management
    public DependentsManager DependentsManager$internal$;
    public DependentsManager getDependentsManager$internal$() {
        return DependentsManager$internal$;
    }
    public void setDependentsManager$internal$(final DependentsManager depMgr) {
        DependentsManager$internal$ = depMgr;
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
    public void switchDependence$(final int varNum, FXObject oldBindee, FXObject newBindee) {
        switchDependence$(this, varNum, oldBindee, newBindee);
    }
    public static void switchDependence$(FXObject obj, final int varNum, FXObject oldBindee, FXObject newBindee) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        if (oldBindee != newBindee) {
            DependentsManager.get(obj).switchDependence(obj, varNum, oldBindee, newBindee);
        }
    }
    public void notifyDependents$(final int varNum, final int phase) {
        notifyDependents$(this, varNum, phase);
    }
    public static void notifyDependents$(FXObject obj, final int varNum, final int phase) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        DependentsManager.get(obj).notifyDependents(obj, varNum, phase);
    }
    public void update$(FXObject src, final int varNum, final int phase) {
        update$(this, src, varNum, phase);
    }
    public static void update$(FXObject obj, FXObject src, final int varNum, final int phase) {
    }
    public int getListenerCount$() {
        return getListenerCount$(this);
    }
    public static int getListenerCount$(FXObject src) {
        return DependentsManager.get(src).getListenerCount(src);
    }

    public Object get$(int varNum) {
        return get$(this, varNum);
    }
    public static Object get$(FXObject obj, int varNum) {
        throw new IllegalArgumentException("no such variable: " + varNum);
    }
    public void set$(int varNum, Object value) {
        set$(this, varNum, value);
    }
    public static void set$(FXObject obj, int varNum, Object value) {
        throw new IllegalArgumentException("no such variable: " + varNum);
    }
    public Class getType$(int varNum) {
        return getType$(this, varNum);
    }
    public static Class getType$(FXObject obj, int varNum) {
        throw new IllegalArgumentException("no such variable: " + varNum);
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
        applyDefaults$();
        complete$();
    }
    public static void initialize$(FXObject obj) {
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

    // returns varNum for a given variable of a given object
    public static int getVarNum$(FXObject obj, String varName) {
        try {
            // FIXME: dependency on generated members!
            Field field;
            try {
                field = obj.getClass().getField("VOFF$" + varName);
            } catch (NoSuchFieldException exp) {
                // may be this is a script-private member
                String className = obj.getClass().getName();
                className = className.replace('.', '$');
                StringBuilder buf = new StringBuilder();
                buf.append("VOFF$");
                buf.append(className.replace('.', '$'));
                buf.append('$');
                buf.append(varName);
                field = obj.getClass().getField(buf.toString());
            }
            return field.getInt(null);
        } catch (RuntimeException re) {
            throw (RuntimeException)re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
