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

import com.sun.javafx.runtime.location.*;

/**
 * Location representing bind(x[i]), where x and i are locations.  Does not fire change triggers for modifications that
 * do not affect the value of x[i], such as inserts after i or modifications to elements other than i.  
 *
 * @author Brian Goetz
 */
class BoundSequenceElement<T> extends ObjectVariable<T> implements ObjectLocation<T> {
    private final SequenceLocation<T> seq;
    private final IntLocation index;
    private int lastIndex;

    public BoundSequenceElement(SequenceLocation<T> seq, IntLocation index) {
        super();
        this.seq = seq;
        this.index = index;
        lastIndex = index.get();
        bind(false, new ObjectBindingExpression<T>() {
            public T computeValue() {
                lastIndex = BoundSequenceElement.this.index.get();
                return BoundSequenceElement.this.seq.getAsSequence().get(lastIndex);
            }
        }, index);
        seq.addChangeListener(new MySequenceListener());
    }

    private class MySequenceListener extends SequenceChangeListener<T> {
        public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
            int deltaSize = (endPos-startPos+1) - Sequences.size(newElements);
            if (deltaSize != 0) {
                if (startPos <= lastIndex)
                    invalidate();
            }
            else {
                if (startPos <= lastIndex && lastIndex <= endPos)
                    invalidate();
            }
        }
    }
}
