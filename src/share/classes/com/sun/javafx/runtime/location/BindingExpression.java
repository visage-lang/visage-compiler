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
 * Common base class for all binding expressions, regardless of type.  Binding expressions override compute(), and
 * must call one of the pushValue() methods as its last operation.  It must call the appropriate pushValue method for
 * the Location to which this binding expression is attached.
 *
 * @author Brian Goetz
 */
public abstract class BindingExpression extends AbstractBindingExpression {

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
}
