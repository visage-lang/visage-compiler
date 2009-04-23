/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * Common base class for binding expressions, regardless of type.  Binding expressions override compute(), and
 * must call one of the pushValue() methods as its last operation.  It must call the appropriate pushValue method for
 * the Location to which this binding expression is attached.
 *
 * @author Brian Goetz
 */
public abstract class AbstractBindingExpression extends AbstractLocationDependency implements BindingExpression {
    protected Location location;

    public int getDependencyKind() {
        return AbstractLocation.CHILD_KIND_BINDING_EXPRESSION;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (this.location != null)
            throw new IllegalStateException("Cannot reuse binding expressions");
        this.location = location;
        DependencySource[] fixedDependents = getStaticDependents();
        if (fixedDependents != null) {
            location.addDependency(fixedDependents);
        }
    }

    /**
     * Override to provide an array of static dependents
     * @return an array of static dependents, or null
     */
    protected DependencySource[] getStaticDependents() {
        return null;
    }

    protected <T extends DependencySource> T addDynamicDependent(T dep) {
        location.addDynamicDependency(dep);
        return dep;
    }

    protected <T extends DependencySource> T addStaticDependent(T dep) {
        location.addDependency(dep);
        return dep;
    }

    protected void clearDynamicDependencies() {
        location.clearDynamicDependencies();
    }

    // These unsafe casts are done to avoid the need to have a separate class for each return type.  Ugly, but reduces footprint.

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

    public<V> void pushFrom(TypeInfo<V, ?> ti, ObjectLocation<V> otherLocation) {
        switch (ti.type) {
            case INT: pushValue(((IntLocation) otherLocation).getAsInt()); break;
            case FLOAT: pushValue(((FloatLocation) otherLocation).getAsFloat()); break;
            case DOUBLE: pushValue(((DoubleLocation) otherLocation).getAsDouble()); break;
            case LONG: pushValue(((LongLocation) otherLocation).getAsLong()); break;
            case BYTE: pushValue(((ByteLocation) otherLocation).getAsByte()); break;
            case SHORT: pushValue(((ShortLocation) otherLocation).getAsShort()); break;
            case BOOLEAN: pushValue(((BooleanLocation) otherLocation).getAsBoolean()); break;
            case CHAR: pushValue(((CharLocation) otherLocation).getAsChar()); break;
            case OBJECT:
            case OTHER:
                pushValue(((ObjectLocation<?>) otherLocation).get()); break;
            default: throw new UnsupportedOperationException(ti.type.toString());
        }
    }
}
