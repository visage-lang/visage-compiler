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

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.ObjectChangeListener;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundSingletonSequence
 *
 * @author Brian Goetz
 */
class BoundSingletonSequence<T, V extends T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final ObjectLocation<V> location;

    public BoundSingletonSequence(boolean lazy, TypeInfo<T, ?> typeInfo, ObjectLocation<V> location) {
        super(lazy, typeInfo);
        this.location = location;
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    protected Sequence<T> computeValue() {
        return Sequences.singleton(getElementType(), location.get());
    }

    private void addTriggers() {
        if (lazy)
            location.addInvalidationListener(new InvalidateMeListener());
        else
            location.addChangeListener(new ObjectChangeListener<V>() {
                public void onChange(V oldValue, V newValue) {
                    updateSlice(0, getRawValue().size() - 1, Sequences.singleton(getElementType(), newValue));
                }
            });
    }
}
