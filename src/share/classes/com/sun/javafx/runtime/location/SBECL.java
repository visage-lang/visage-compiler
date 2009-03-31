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

package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * The base class for the per-script class which combines all binding expressions
 * and change listeners
 *
 * @author Robert Field
 */
public abstract class SBECL<T> extends ChangeListener<T> implements BindingExpression {
    protected final int id;
    protected final Object arg$0;
    protected final Object arg$1;
    protected final Object[] moreArgs;
    protected final int dependents;
    private Location location;
    private final int childKind;

    /**
     * BindingExpression constructor
     * @param id
     * @param arg0
     * @param arg1
     * @param moreArgs
     * @param dependents
     */
    public SBECL(int id, Object arg0, Object arg1, Object[] moreArgs, int dependents) {
        this.id = id;
        this.arg$0 = arg0;
        this.arg$1 = arg1;
        this.moreArgs = moreArgs;
        this.dependents = dependents;
        this.childKind = AbstractLocation.CHILD_KIND_BINDING_EXPRESSION;
    }

    /**
     * ChangeListener constructor
     * @param id
     */
    public SBECL(int id) {
        this.id = id;
        this.arg$0 = null;
        this.arg$1 = null;
        this.moreArgs = null;
        this.dependents = 0;
        this.childKind = AbstractLocation.CHILD_KIND_TRIGGER;
    }

    /**
     * ChangeListener constructor
     * @param id
     * @param arg0
     * @param arg1
     * @param moreArgs
     */
    public SBECL(int id, Object arg0, Object arg1, Object[] moreArgs) {
        this.id = id;
        this.arg$0 = arg0;
        this.arg$1 = arg1;
        this.moreArgs = moreArgs;
        this.dependents = 0;
        this.childKind = AbstractLocation.CHILD_KIND_TRIGGER;
    }

    @Override
    public int getDependencyKind() {
        return childKind;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (this.location != null)
            throw new IllegalStateException("Cannot reuse binding expressions");
        this.location = location;
        if (arg$0 != null && (dependents & 0x1) != 0)  {
            location.addDependency((Location)arg$0);
        }
        if (arg$1 != null && (dependents & 0x2) != 0)  {
            location.addDependency((Location)arg$1);
        }
        if (moreArgs != null && (dependents & ~0x3) != 0) {
            for (int i = 0; i < moreArgs.length; ++i) {
                if ((dependents & (1 << (i + 2))) != 0) {
                    location.addDependency((Location)moreArgs[i]);
                }
            }
        }
    }

    public Object arg0() {
        return arg$0;
    }


    /************* cloned from AbstractBindingExpression **************/

    public void pushValue(int x) { ((IntVariable) location).replaceValue(x); }

    public void pushValue(long x) { ((LongVariable) location).replaceValue(x); }

    public void pushValue(short x) { ((ShortVariable) location).replaceValue(x); }

    public void pushValue(byte x) { ((ByteVariable) location).replaceValue(x); }

    public void pushValue(char x) { ((CharVariable) location).replaceValue(x); }

    public void pushValue(boolean x) { ((BooleanVariable) location).replaceValue(x); }

    public void pushValue(float x) { ((FloatVariable) location).replaceValue(x); }

    public void pushValue(double x) { ((DoubleVariable) location).replaceValue(x); }

    public<V> void pushValue(Sequence<? extends V> x) { ((SequenceVariable<V>) location).replaceValue(Sequences.upcast(x)); }

    public<V> void pushValue(V x) { ((ObjectVariable<V>) location).replaceValue(x); }

    public abstract void compute();

}
