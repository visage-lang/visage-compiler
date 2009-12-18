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
 * This class was copied and modified from FXVariable.java. This class ignores
 * all dependent registration methods and also the state fir dependents management.
 * If you just need to store something in an FXObject without requiring any dependent
 * notification, then you can use this class.
 *
 * Currently, this class is used when calling a bound function from a non-bind
 * context. A bound function is translated to a method that accepts FXObject+varNum
 * pair for each source function parameter and returns a Pointer value. When a
 * bound function is called from a bind context, we have pass FXObject+varNum from
 * bind call site. When a bound function is called from a non-bind site, we may
 * not have FXObject+varNum for each argument expression (for eg, literal value
 * passed as an argument). We wrap each argument with a FXConstant instance so
 * that we can pass FXObject+varNum to the bound function.
 *
 * @author A. Sundararajan
 */
public final class FXConstant extends Object implements FXObject {
    public static int VCNT$ = -1;

    public static int VCNT$() {
        if (VCNT$ == -1) {
            final int $count = VCNT$ = 1;
            VOFF$value = $count + -1;
        }
        return VCNT$;
    }

    public int count$() {
        return VCNT$();
    }
    public static int VOFF$value;


    public Object $value;


    public Object get$value() {
        if (varTestBits$(FXConstant.VOFF$value, VFLGS$IS_BOUND_DEFAULT_APPLIED, 0)) {
            applyDefaults$(FXConstant.VOFF$value);
        }
        return $value;
    }


    public Object set$value(final Object varNewValue$) {
        restrictSet$(FXConstant.VOFF$value);
        varChangeBits$(FXConstant.VOFF$value, 0, VFLGS$IS_INITIALIZED);
        be$value(varNewValue$);
        return $value;
    }


    public Object be$value(final Object varNewValue$) {
        final Object varOldValue$ = $value;
        if (varOldValue$ != varNewValue$ || varTestBits$(FXConstant.VOFF$value, VFLGS$DEFAULT_APPLIED, 0)) {
            varChangeBits$(FXConstant.VOFF$value, 0, VFLGS$DEFAULT_APPLIED);
            invalidate$value(VFLGS$IS_INVALID);
            $value = varNewValue$;
            varChangeBits$(FXConstant.VOFF$value, VFLGS$IS_INVALID, 0);
            invalidate$value(VFLGS$NEEDS_TRIGGER);
            varChangeBits$(FXConstant.VOFF$value, VFLGS$NEEDS_TRIGGER, 0);
            onReplace$value(varOldValue$, varNewValue$);
        } else {
            varChangeBits$(FXConstant.VOFF$value, VFLGS$VALIDITY_FLAGS, VFLGS$DEFAULT_APPLIED);
        }
        return $value;
    }


    public void invalidate$value(final int phase$) {
        if (!varChangeBits$(FXConstant.VOFF$value, 0, phase$)) {
            notifyDependents$(FXConstant.VOFF$value, phase$);
        }
    }


    public void onReplace$value(final Object varOldValue$, final Object varNewValue$) {
    }

    public void applyDefaults$(final int varNum$) {
        final FXConstant receiver$ = this;
        if (varTestBits$(varNum$, VFLGS$DEFAULT_APPLIED, 0)) {
            switch (varNum$ - VCNT$) {
            case -1:
                return;
            }
        }
    }

    public Object get$(final int varNum$) {
        switch (varNum$ - VCNT$) {
        case -1:
            return get$value();
        }
        return FXBase.get$(this, varNum$);
    }

    public Object elem$(final int varNum$, final int pos$) {
        switch (varNum$ - VCNT$) {
        case -1:
            return null;
        }
        return FXBase.elem$(this, varNum$, pos$);
    }

    public int size$(final int varNum$) {
        switch (varNum$ - VCNT$) {
        case -1:
            return 0;
        }
        return FXBase.size$(this, varNum$);
    }

    public void set$(final int varNum$, final Object object$) {
        switch (varNum$ - VCNT$) {
        case -1:
            set$value(object$);
            return;
        }
        FXBase.set$(this, varNum$, $value);
    }

    public void be$(final int varNum$, final Object object$) {
        switch (varNum$ - VCNT$) {
        case -1:
            be$value(object$);
            return;
        }
        FXBase.be$(this, varNum$, $value);
    }

    public void invalidate$(final int varNum$, final int startPos$, final int endPos$, final int newLength$, final int phase$) {
        switch (varNum$ - VCNT$) {
        case -1:
            invalidate$value(phase$);
            return;
        }
        FXBase.invalidate$(this, varNum$, startPos$, endPos$, newLength$, phase$);
    }

    public Class getType$(final int varNum$) {
        switch (varNum$ - VCNT$) {
        case -1:
            return java.lang.Object.class;
        }
        return FXBase.getType$(this, varNum$);
    }

    public void initVarBits$() {
    }

    public FXConstant() {
        this(false);
        initialize$();
    }

    public FXConstant(final boolean dummy) {
        initFXBase$();
    }

    public static FXConstant make() {
        return new FXConstant();
    }

    public static FXConstant make(Object init) {
        FXConstant var = new FXConstant();
        var.set$value(init);
        return var;
    }

    public DependentsManager getDependentsManager$internal$() {
        // ignore
        return null;
    }

    public void setDependentsManager$internal$(final DependentsManager value) {
        // ignore
    }

    public double getAsDouble$(final int arg0, final int arg1) {
        return FXBase.getAsDouble$(this, arg0, arg1);
    }

    public float getAsFloat$(final int arg0, final int arg1) {
        return FXBase.getAsFloat$(this, arg0, arg1);
    }

    public long getAsLong$(final int arg0, final int arg1) {
        return FXBase.getAsLong$(this, arg0, arg1);
    }

    public int getAsInt$(final int arg0, final int arg1) {
        return FXBase.getAsInt$(this, arg0, arg1);
    }

    public short getAsShort$(final int arg0, final int arg1) {
        return FXBase.getAsShort$(this, arg0, arg1);
    }

    public byte getAsByte$(final int arg0, final int arg1) {
        return FXBase.getAsByte$(this, arg0, arg1);
    }

    public char getAsChar$(final int arg0, final int arg1) {
        return FXBase.getAsChar$(this, arg0, arg1);
    }

    public boolean getAsBoolean$(final int arg0, final int arg1) {
        return FXBase.getAsBoolean$(this, arg0, arg1);
    }

    public void complete$() {
        FXBase.complete$(this);
    }

    public void postInit$() {
        FXBase.postInit$(this);
    }

    public void userInit$() {
        FXBase.userInit$(this);
    }

    public void applyDefaults$() {
        FXBase.applyDefaults$(this);
    }

    public void initialize$() {
        FXBase.initialize$(this);
    }

    public int getListenerCount$() {
        return FXBase.getListenerCount$(this);
    }

    public void update$(final FXObject arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5) {
        FXBase.update$(this, arg0, arg1, arg2, arg3, arg4, arg5);
    }

    public void update$(final FXObject arg0, final int arg1, final int arg2) {
        FXBase.update$(this, arg0, arg1, arg2);
    }

    public void notifyDependents$(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
        // ignore
    }

    public void notifyDependents$(final int arg0, final int arg1) {
        // ignore
    }

    public void switchBiDiDependence$(final int arg0, final FXObject arg1, final int arg2, final FXObject arg3, final int arg4) {
        // ignore
    }

    public void switchDependence$(final FXObject arg0, final int arg1, final FXObject arg2, final int arg3) {
        // ignore
    }

    public void removeDependent$(final int arg0, final FXObject arg1) {
        // ignore
    }

    public void addDependent$(final int arg0, final FXObject arg1) {
        // ignore
    }

    public void restrictSet$(final int arg0) {
        FXBase.restrictSet$(this, arg0);
    }

    public int getFlags$(final int arg0) {
        return FXBase.getFlags$(this, arg0);
    }

    public void setFlags$(final int arg0, final int arg1) {
        FXBase.setFlags$(this, arg0, arg1);
    }

    public boolean varChangeBits$(final int arg0, final int arg1, final int arg2) {
        return FXBase.varChangeBits$(this, arg0, arg1, arg2);
    }

    public boolean varTestBits$(final int arg0, final int arg1, final int arg2) {
        return FXBase.varTestBits$(this, arg0, arg1, arg2);
    }

    public void initFXBase$() {
        FXBase.initFXBase$(this);
    }
}
