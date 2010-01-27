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
 * @author Robert Field
 */
public interface FXObject {
    /**
     * Var flag bits.
     */
    public static final int VFLGS$RESTING_STATE_BIT  =    01;
    public static final int VFLGS$BE_STATE_BIT       =    02;
    public static final int VFLGS$INVALID_STATE_BIT  =    04;
    public static final int VFLGS$DEFAULT_APPLIED    =   010;
    public static final int VFLGS$IS_INITIALIZED     =   020;
    public static final int VFLGS$AWAIT_VARINIT      =   040;
    public static final int VFLGS$CYCLE              =  0100;
    public static final int VFLGS$IS_EAGER           =  0200;
    public static final int VFLGS$SEQUENCE_LIVE      =  0400;
    public static final int VFLGS$BE_UPDATE          = 01000;
    public static final int VFLGS$IS_BOUND           = 02000;
    public static final int VFLGS$IS_READONLY        = 04000;

    /**
     * Var states
     */
    public static final int VFLGS$STATE_MASK = VFLGS$RESTING_STATE_BIT | VFLGS$BE_STATE_BIT | VFLGS$INVALID_STATE_BIT;

    public static final int VFLGS$STATE$VALID            = VFLGS$RESTING_STATE_BIT;
    public static final int VFLGS$STATE$CASCADE_INVALID  = VFLGS$INVALID_STATE_BIT;
    public static final int VFLGS$STATE$BE_INVALID       = VFLGS$BE_STATE_BIT | VFLGS$INVALID_STATE_BIT;
    public static final int VFLGS$STATE$TRIGGERED        = VFLGS$RESTING_STATE_BIT | VFLGS$INVALID_STATE_BIT;

    /**
     * Var flag groups.
     */
    public static final int VFLGS$IS_BOUND_INVALID = VFLGS$IS_BOUND | VFLGS$INVALID_STATE_BIT;
    public static final int VFLGS$IS_BOUND_INVALID_CYCLE = VFLGS$IS_BOUND | VFLGS$INVALID_STATE_BIT | VFLGS$CYCLE;
    public static final int VFLGS$IS_BOUND_INVALID_CYCLE_AWAIT_VARINIT = VFLGS$IS_BOUND | VFLGS$INVALID_STATE_BIT | VFLGS$CYCLE | VFLGS$AWAIT_VARINIT;
    public static final int VFLGS$IS_BOUND_DEFAULT_APPLIED = VFLGS$IS_BOUND | VFLGS$DEFAULT_APPLIED;
    public static final int VFLGS$IS_BOUND_DEFAULT_APPLIED_IS_INITIALIZED = VFLGS$IS_BOUND | VFLGS$DEFAULT_APPLIED | VFLGS$IS_INITIALIZED;
    public static final int VFLGS$DEFAULT_APPLIED_VARINIT = VFLGS$AWAIT_VARINIT | VFLGS$DEFAULT_APPLIED;
    public static final int VFLGS$INIT_OBJ_LIT = VFLGS$IS_INITIALIZED;
    public static final int VFLGS$INIT_OBJ_LIT_DEFAULT = VFLGS$IS_INITIALIZED | VFLGS$DEFAULT_APPLIED;
    public static final int VFLGS$INIT_DEFAULT_APPLIED_IS_INITIALIZED_READONLY = VFLGS$DEFAULT_APPLIED | VFLGS$IS_INITIALIZED | VFLGS$IS_READONLY;
    public static final int VFLGS$INIT_DEFAULT_APPLIED_IS_INITIALIZED = VFLGS$DEFAULT_APPLIED | VFLGS$IS_INITIALIZED;
    public static final int VFLGS$INIT_BOUND_READONLY = VFLGS$IS_BOUND | VFLGS$IS_READONLY | VFLGS$STATE$TRIGGERED;
    public static final int VFLGS$VALID_DEFAULT_APPLIED = VFLGS$STATE$VALID | VFLGS$DEFAULT_APPLIED;

    public static final int VFLGS$ALL_FLAGS = -1;

    /**
     * Phase transitions
     * Acceptable current states / Next state / Phase
     * Note: sequences use cascade triggerring
     */
    public static final int PHASE_TRANS$PHASE_SHIFT = 3;
    public static final int PHASE_TRANS$NEXT_STATE_SHIFT = 1 + PHASE_TRANS$PHASE_SHIFT;
    public static final int PHASE_TRANS$PHASE  = 1 << PHASE_TRANS$PHASE_SHIFT;

    public static final int PHASE$INVALIDATE  = 0;
    public static final int PHASE$TRIGGER     = PHASE_TRANS$PHASE;

    public static final int PHASE_TRANS$CASCADE_INVALIDATE  = (VFLGS$STATE$VALID)
                                                            | (VFLGS$STATE$CASCADE_INVALID << PHASE_TRANS$NEXT_STATE_SHIFT)
                                                            | PHASE$INVALIDATE;

    public static final int PHASE_TRANS$BE_INVALIDATE       = (VFLGS$STATE$VALID)
                                                            | (VFLGS$STATE$BE_INVALID      << PHASE_TRANS$NEXT_STATE_SHIFT)
                                                            | PHASE$INVALIDATE;

    public static final int PHASE_TRANS$CASCADE_TRIGGER     = (VFLGS$STATE$CASCADE_INVALID)
                                                            | (VFLGS$STATE$TRIGGERED       << PHASE_TRANS$NEXT_STATE_SHIFT)
                                                            | PHASE$TRIGGER;

    public static final int PHASE_TRANS$BE_TRIGGER          = (VFLGS$STATE$CASCADE_INVALID | VFLGS$STATE$BE_INVALID)
                                                            | (VFLGS$STATE$TRIGGERED       << PHASE_TRANS$NEXT_STATE_SHIFT)
                                                            | PHASE$TRIGGER;

    public static final int PHASE_TRANS$CLEAR_BE            = ~((VFLGS$BE_STATE_BIT)
                                                            | (VFLGS$BE_STATE_BIT          << PHASE_TRANS$NEXT_STATE_SHIFT));

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
    public void     update$ (FXObject src, int varNum, int startPos, int endPos, int newLength, int phase);
// for testing - the listener count is the number of distinct {varNum, dep} pairs
    public int      getListenerCount$();

    // instance variable access by varNum
    public Object   get$(int varNum);
    public void     set$(int varNum, Object value);
    // type of a particular instance variable
    public Class    getType$(int varNum);
    public void     seq$(int varNum, Object value);
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
