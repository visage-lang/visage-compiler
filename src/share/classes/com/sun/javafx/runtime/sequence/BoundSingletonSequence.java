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

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.InvalidateMeListener;
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
            location.addInvalidationListener(new InvalidateMeListener(this));
        else
            location.addChangeListener(new ElementChangeListener<T, V>(this));
    }

    private static class ElementChangeListener<T, V extends T> extends WeakMeChangeListener<V> {
        ElementChangeListener(BoundSingletonSequence<T, V> bss) {
            super(bss);
        }

        @Override
        public void onChange(V oldValue, V newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(V oldValue, V newValue) {
            BoundSingletonSequence<T, V> bss = (BoundSingletonSequence<T, V>) get();
            if (bss != null) {
                bss.updateSlice(0, bss.getRawValue().size(), newValue);
                return true;
            } else {
                return false;
            }
        }
    }
}
