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

import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.TypeInfo;

/**
 * BoundUpcastSequence
 *
 * @author Brian Goetz
 */
public class BoundUpcastSequence<T, V extends T> extends AbstractBoundSequence<T> {

    private final SequenceLocation<V> sequence;

    public BoundUpcastSequence(TypeInfo<T> typeInfo, SequenceLocation<V> sequence) {
        super(typeInfo);
        this.sequence = sequence;
        setInitialValue(computeValue());
        addTriggers();
    }

    private Sequence<T> computeValue() {
        return Sequences.<T>upcast(sequence.get());
    }

    private void addTriggers() {
        sequence.addChangeListener(new SequenceChangeListener<V>() {
            public void onChange(int startPos, int endPos, Sequence<? extends V> newElements, Sequence<V> oldValue, Sequence<V> newValue) {
                updateSlice(startPos, endPos, newElements, Sequences.<T>upcast(newValue));
            }
        });
    }
}
