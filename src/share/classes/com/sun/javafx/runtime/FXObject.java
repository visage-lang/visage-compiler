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

import java.lang.reflect.Type;



/**
 * All FX classes must extend FXObject; it acts as a marker interface, and also includes methods required for
 * object lifecyle.
 *
 * @author Brian Goetz
 * @author Jim Laskey
 */
public interface FXObject {
    /**
     * Number of bits needed for each var.
     */
    public static final int VFLGS$BITS_PER_VAR = 4;
    
    /**
     * Number of bits needed for each var.
     */
    public static final int VFLGS$VARS_PER_WORD = 32 / VFLGS$BITS_PER_VAR;
    
    /**
     * Var is valid value applied flag (two phase bits.)
     */
    public static final int VFLGS$IS_VALID_VALUE = 0;
    public static final int VFLGS$INVAL_PHASE = 0;
    public static final int VFLGS$TRIGGER_PHASE = 1;
   
    /**
     * Var is initialized flag.  Alias to VFLGS$IS_VALID_VALUE for convenience in libraries.
     */
    public static final int VFLGS$IS_INITIALIZED = VFLGS$IS_VALID_VALUE + VFLGS$INVAL_PHASE;
    
    /**
     * Var is bound flag.
     */
    public static final int VFLGS$IS_BOUND = 2;
    
    /**
     * Var is read only flag (bound or def.)
     */
    public static final int VFLGS$IS_READONLY = 3;
    
    public void     initFXBase$     ();
    
    public int      getVFLGS$small$internal$();
    public void     setVFLGS$small$internal$(final int small);
    public int[]    getVFLGS$large$internal$();
    public void     setVFLGS$large$internal$(final int[] large);

    public boolean isVarBitSet$(final int varNum, final int varBit);
    public boolean setVarBit$(final int varNum, final int varBit);
    public boolean clearVarBit$(final int varNum, final int varBit);

    // dependents management
    public DependentsManager getDependentsManager$internal$();
    public void     setDependentsManager$internal$(final DependentsManager depMgr);
    public void     addDependent$      (final int varNum, FXObject dep);
    public void     removeDependent$   (final int varNum, FXObject dep);
    // Earlier 'this' object was dependent on { oldBindee, varNum }.
    // Now, change the dependence to { newBindee, varNum }
    public void     switchDependence$  (final int varNum, FXObject oldBindee, FXObject newBindee);
    public void     notifyDependents$  (final int varNum, final int phase);
    public void     update$ (final FXObject src, final int varNum, final int phase);
    // for testing - the listener count is the number of distinct {varNum, dep} pairs
    public int      getListenerCount$();

    // instance variable access by varNum
    public Object   get$(int varNum);
    public void     set$(int varNum, Object value);
    // type of a particular instance variable
    public Class    getType$(int varNum);

    public void     initialize$   ();
    public void     applyDefaults$();
    public void     applyDefaults$(final int varNum);
    public void     userInit$     ();
    public void     postInit$     ();
    public void     complete$     ();
    public int      count$        ();
}
