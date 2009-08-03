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

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.InvalidateMeListener;

/**
 * BoundReverseSequence
 *
 * @author Brian Goetz
 */
class BoundReverseSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<T> location;

    BoundReverseSequence(boolean lazy, SequenceLocation<T> location) {
        super(lazy, location.getElementType());
        this.location = location;

        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    protected Sequence<? extends T> computeValue() {
        return Sequences.reverse(location.getAsSequence());
    }

    private void addTriggers() {
        if (lazy)
            location.addInvalidationListener(new InvalidateMeListener(this));
        else
            location.addSequenceChangeListener(new ChangeListener<T>() {
                public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
                    newElements = Sequences.getNewElements(buffer, startPos, newElements);
                    int sliceSize = endPos - startPos;
                    int oldSize = Sequences.sizeOfOldValue(buffer, oldValue, endPos);
                    int actualStart = oldSize - startPos - sliceSize;
                    int actualEnd = actualStart + sliceSize;
                    Sequence<? extends T> reverseElements = Sequences.reverse(newElements);
                    updateSlice(actualStart, actualEnd, reverseElements);
                }
            });
    }
}
