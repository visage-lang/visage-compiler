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
import com.sun.javafx.runtime.sequence.Sequence;

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
        obj.initVarBits$();
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
    
    private static void printBits$(final int bits) {
        System.err.print("(");
        if ((bits & VFLGS$IS_INVALID) != 0)             System.err.print(" Invalid");
        if ((bits & VFLGS$NEEDS_TRIGGER) != 0)          System.err.print(" NeedsTrigger");
        if ((bits & VFLGS$IS_BOUND) != 0)               System.err.print(" Bound");
        if ((bits & VFLGS$IS_READONLY) != 0)            System.err.print(" Readonly");
        System.err.print(" )");
    }
    
    private static void printBitsAction$(String title, FXObject obj, final int varNum, final int bits1, final int bits2) {
        System.err.print(title + ": " + obj + "[" + varNum + "] ");
        printBits$(bits1);
        printBits$(bits2);
        
        int index = varNum / VFLGS$VARS_PER_WORD;
        int slot = varNum % VFLGS$VARS_PER_WORD;
        int shift = slot * VFLGS$BITS_PER_VAR;
        int word;
      
        if (index > 0) {
            index--;
            int[] large = obj.getVFLGS$large$internal$();
            word = large[index];
        } else {
            word = obj.getVFLGS$small$internal$();
        }
        printBits$((word >> shift) & ((1 << VFLGS$BITS_PER_VAR) - 1));
        
        System.err.println();
    }
    
    public boolean varTestBits$(final int varNum, int maskBits, int testBits) {
        return varTestBits$(this, varNum, maskBits, testBits);
    }
    public static boolean varTestBits$(FXObject obj, final int varNum, int maskBits, int testBits) {
        //printBitsAction$("Tst", obj, varNum, maskBits, testBits);
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
        //printBitsAction$("Chg", obj, varNum, clearBits, setBits);
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
        if (varTestBits$(obj, varNum, VFLGS$IS_READONLY, VFLGS$IS_READONLY)) {
            if (varTestBits$(obj, varNum, VFLGS$IS_BOUND, VFLGS$IS_BOUND)) {
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
    public void switchDependence$(FXObject oldBindee, final int oldNum, FXObject newBindee, final int newNum) {
        switchDependence$(this, oldBindee, oldNum, newBindee, newNum);
    }
    public static void switchDependence$(FXObject obj, FXObject oldBindee, final int oldNum, FXObject newBindee, final int newNum) {
        if (oldBindee != newBindee) {
            DependentsManager.get(obj).switchDependence(obj, oldBindee, oldNum, newBindee, newNum);
        }
    }
    public void switchBiDiDependence$(final int varNum, FXObject oldBindee, final int oldNum, FXObject newBindee, final int newNum) {
        switchBiDiDependence$(this, varNum, oldBindee, oldNum, newBindee, newNum);
    }
    public static void switchBiDiDependence$(FXObject obj, final int varNum, FXObject oldBindee, final int oldNum, FXObject newBindee, final int newNum) {
        if (oldBindee != newBindee) {
            if (oldBindee != null) {
                DependentsManager.get(oldBindee).switchDependence(oldBindee, obj, varNum, null, 0);
            }
            DependentsManager.get(obj).switchDependence(obj, oldBindee, oldNum, newBindee, newNum);
            if (newBindee != null) {
                DependentsManager.get(newBindee).switchDependence(newBindee, null, 0, obj, varNum);
            }
        }
    }
    public void notifyDependents$(final int varNum, final int phase) {
        notifyDependents$(this, varNum, phase);
    }
    public static void notifyDependents$(FXObject obj, final int varNum, final int phase) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        //System.err.println("notifyDependents$: " + obj + "[" + varNum + "] " + phase);
        DependentsManager.get(obj).notifyDependents(obj, varNum, phase);
    }
    public void notifyDependents$(int varNum, int startPos, int endPos, int newLength, int phase) {
        notifyDependents$(this, varNum, startPos, endPos, newLength, phase);
    }
    public static void notifyDependents$(FXObject obj, final int varNum, int startPos, int endPos, int newLength, final int phase) {
        assert varNum > -1 && varNum < obj.count$() : "invalid varNum: " + varNum;
        //System.err.println("notifyDependents$: " + obj + "[" + varNum + "] " + phase);
        DependentsManager.get(obj).notifyDependents(obj, varNum, startPos, endPos, newLength, phase);
    }
    public void update$(FXObject src, final int varNum, final int phase) {
        update$(this, src, varNum, phase);
    }
    public static void update$(FXObject obj, FXObject src, final int varNum, final int phase) {
        //System.err.println("update$: " + obj + " " + src + "[" + varNum + "] " + phase);
    }
    public void update$(FXObject src, final int varNum, int startPos, int endPos, int newLength, final int phase) {
        update$(this, src, varNum, startPos, endPos, newLength, phase);
    }
    public static void update$(FXObject obj, FXObject src, final int varNum, int startPos, int endPos, int newLength, final int phase) {
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
    
    public void initVarBits$() { initVarBits$(this); }
    public static void initVarBits$(FXObject obj) {}

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

    public int size$(int varNum) {
        return ((Sequence<?>) get$(varNum)).size();
    }
    public static int size$(FXObject obj, int varNum) {
        return ((Sequence<?>) obj.get$(varNum)).size();
    }

    public Object get$(int varNum, int position) {
        return ((Sequence<?>) get$(varNum)).get(position);
    }
    public static Object get$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).get(position);
    }

    public boolean getAsBoolean$(int varNum, int position) {
        return getAsBoolean$(this, varNum, position);
    }
    public static boolean getAsBoolean$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsBoolean(position);
    }

    public char getAsChar$(int varNum, int position) {
        return getAsChar$(this, varNum, position);
    }
    public static char getAsChar$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsChar(position);
    }

    public byte getAsByte$(int varNum, int position) {
        return getAsByte$(this, varNum, position);
    }
    public static byte getAsByte$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsByte(position);
    }

    public short getAsShort$(int varNum, int position) {
        return getAsShort$(this, varNum, position);
    }
    public static short getAsShort$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsShort(position);
    }

    public int getAsInt$(int varNum, int position) {
        return getAsInt$(this, varNum, position);
    }
    public static int getAsInt$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsInt(position);
    }

    public long getAsLong$(int varNum, int position) {
        return getAsLong$(this, varNum, position);
    }
    public static long getAsLong$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsLong(position);
    }

    public float getAsFloat$(int varNum, int position) {
        return getAsFloat$(this, varNum, position);
    }
    public static float getAsFloat$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsFloat(position);
    }

    public double getAsDouble$(int varNum, int position) {
        return getAsDouble$(this, varNum, position);
    }
    public static double getAsDouble$(FXObject obj, int varNum, int position) {
        return ((Sequence<?>) obj.get$(varNum)).getAsDouble(position);
    }
}
