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

/**
 * Common base class for all binding expressions, regardless of type.  Binding expressions override compute(), and
 * must call one of the pushValue() methods as its last operation.  It must call the appropriate pushValue method for
 * the Location to which this binding expression is attached.
 *
 * @author Brian Goetz
 */
public interface BindingExpression extends LocationDependency {

    public void pushValue(int x);

    public void pushValue(long x);

    public void pushValue(short x);

    public void pushValue(byte x);

    public void pushValue(char x);

    public void pushValue(boolean x);

    public void pushValue(float x);

    public void pushValue(double x);

    public<V> void pushValue(Sequence<? extends V> x);

    public<V> void pushValue(V x);

    public abstract void compute();

    public Location getLocation();

    public void setLocation(Location location);
}
