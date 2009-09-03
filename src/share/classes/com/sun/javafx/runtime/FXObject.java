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


/**
 * All FX classes must extend FXObject; it acts as a marker interface, and also includes methods required for
 * object lifecyle.
 *
 * @author Brian Goetz
 * @author Jim Laskey
 */
public interface FXObject {
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
     * Var is bindee flag.  Must be non-final for binary compatibility.
     */
    public static final int VFLGS$IS_BINDEE = 3;
    
    public int      getVFLGS$small  ();
    public void     setVFLGS$small  (final int small);
    public int[]    getVFLGS$large  ();
    public void     setVFLGS$large  (final int[] large);
    public void     allocateVarBits$();
    public boolean  isVarBitSet$    (final int varNum, final int varBit);
    public boolean  setVarBit$      (final int varNum, final int varBit);
    public boolean  clearVarBit$    (final int varNum, final int varBit);

    public boolean  isInitialized$     (final int varNum);
    public boolean  setInitialized$    (final int varNum);
    public boolean  isDefaultsApplied$ (final int varNum);
    public boolean  setDefaultsApplied$(final int varNum);
    public boolean  isValidValue$      (final int varNum);
    public boolean  setValidValue$     (final int varNum);
    public boolean  clearValidValue$   (final int varNum);
    public boolean  isBindee$          (final int varNum);
    public boolean  setBindee$         (final int varNum);
    public boolean  clearBindee$       (final int varNum);

    public void     initialize$   ();
    public void     addTriggers$  ();
    public void     applyDefaults$();
    public void     applyDefaults$(final int varNum);
    public void     userInit$     ();
    public void     postInit$     ();
    public void     complete$     ();
    public int      count$        ();
}
