/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

// CODING/NAMING RESTRICTIONS - see FXBase for explanation.


/**
 * All FX classes must extend FXObject; it acts as a marker interface, and also includes methods required for
 * object lifecyle.
 *
 * @author Brian Goetz
 * @author Jim Laskey
 */
public interface FXObject {
    /**
     * Var bit flags.
     */
    public static final int VFLGS$IS_INVALID = 1;
    public static final int VFLGS$NEEDS_TRIGGER = 2;
    public static final int VFLGS$IS_BOUND = 4;
    public static final int VFLGS$IS_READONLY = 8;
    public static final int VFLGS$DEFAULT_APPLIED = 16;
    public static final int VFLGS$IS_INITIALIZED = 32;
    public static final int VFLGS$AWAIT_VARINIT = 64;
    public static final int VFLGS$CYCLE = 128;
    public static final int VFLGS$IS_EAGER = 256;
    public static final int VFLGS$SEQUENCE_LIVE = 512;

    public static final int VFLGS$VALIDITY_FLAGS = VFLGS$IS_INVALID | VFLGS$NEEDS_TRIGGER;
    public static final int VFLGS$IS_BOUND_INVALID = VFLGS$IS_BOUND | VFLGS$IS_INVALID;
    public static final int VFLGS$IS_BOUND_INVALID_CYCLE = VFLGS$IS_BOUND | VFLGS$IS_INVALID | VFLGS$CYCLE;
    public static final int VFLGS$IS_BOUND_INVALID_CYCLE_AWAIT_VARINIT = VFLGS$IS_BOUND | VFLGS$IS_INVALID | VFLGS$CYCLE | VFLGS$AWAIT_VARINIT;
    public static final int VFLGS$IS_BOUND_DEFAULT_APPLIED = VFLGS$IS_BOUND | VFLGS$DEFAULT_APPLIED;
    public static final int VFLGS$IS_BOUND_DEFAULT_APPLIED_IS_INITIALIZED = VFLGS$IS_BOUND | VFLGS$DEFAULT_APPLIED | VFLGS$IS_INITIALIZED;
    public static final int VFLGS$DEFAULT_APPLIED_VARINIT = VFLGS$AWAIT_VARINIT | VFLGS$DEFAULT_APPLIED;
    public static final int VFLGS$INIT_OBJ_LIT = VFLGS$IS_INITIALIZED;
    public static final int VFLGS$INIT_OBJ_LIT_DEFAULT = VFLGS$IS_INITIALIZED | VFLGS$DEFAULT_APPLIED;
    public static final int VFLGS$INIT_DEFAULT_APPLIED_IS_INITIALIZED_READONLY = VFLGS$DEFAULT_APPLIED | VFLGS$IS_INITIALIZED | VFLGS$IS_READONLY;
    public static final int VFLGS$INIT_DEFAULT_APPLIED_IS_INITIALIZED = VFLGS$DEFAULT_APPLIED | VFLGS$IS_INITIALIZED;
    public static final int VFLGS$INIT_BOUND_READONLY = VFLGS$IS_BOUND | VFLGS$IS_READONLY | VFLGS$IS_INVALID | VFLGS$NEEDS_TRIGGER;

    public static final int VFLGS$ALL_FLAGS = -1;

    public int getFlags$(final int varNum);
    public void setFlags$(final int varNum, final int value);
    public boolean varTestBits$(final int varNum, int maskBits, int testBits);
    public boolean varChangeBits$(final int varNum, int clearBits, int setBits);
    public void restrictSet$(final int varNum);

    // dependents management
    public DependentsManager getDependentsManager$internal$();
    public void     setDependentsManager$internal$(final DependentsManager depMgr);
    public void     addDependent$        (final int varNum, FXObject dep);
    public void     removeDependent$     (final int varNum, FXObject dep);
    // Earlier 'this' object was dependent on { oldBindee, varNum }.
    // Now, change the dependence to { newBindee, varNum }
    public void     switchDependence$    (FXObject oldBindee, final int oldNum, FXObject newBindee, final int newNum);
    public void     switchBiDiDependence$(final int varNum, FXObject oldBindee, final int oldNum, FXObject newBindee, final int newNum);
    public void     notifyDependents$    (final int varNum, final int phase);
    public void     notifyDependents$    (int varNum, int startPos, int endPos, int newLength, int phase);
    public void     update$ (final FXObject src, final int varNum, final int phase);
    public void     update$ (FXObject src, int varNum, int startPos, int endPos, int newLength, int phase);
// for testing - the listener count is the number of distinct {varNum, dep} pairs
    public int      getListenerCount$();

    // instance variable access by varNum
    public Object   get$(int varNum);
    public void     set$(int varNum, Object value);
    // type of a particular instance variable
    public Class    getType$(int varNum);
    public void     be$(int varNum, Object value);
    public void     invalidate$(int varNum, int startPos, int endPos, int newLength, int phase);

    public void     initialize$   ();
    public void     initVars$     ();
    public void     applyDefaults$();
    public void     applyDefaults$(final int varNum);
    public void     userInit$     ();
    public void     postInit$     ();
    public void     complete$     ();
    public int      count$        ();

    public int size$(int varNum);
    public Object elem$(int varNum, int position);
    public boolean getAsBoolean$(int varNum, int position);
    public char getAsChar$(int varNum, int position);
    public byte getAsByte$(int varNum, int position);
    public short getAsShort$(int varNum, int position);
    public int getAsInt$(int varNum, int position);
    public long getAsLong$(int varNum, int position);
    public float getAsFloat$(int varNum, int position);
    public double getAsDouble$(int varNum, int position);
}
