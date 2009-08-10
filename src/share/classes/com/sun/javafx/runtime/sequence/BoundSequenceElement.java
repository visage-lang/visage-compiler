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

import java.lang.ref.WeakReference;
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

    public BoundSequenceElement(boolean lazy, SequenceLocation<T> seq, IntLocation index) {
        super();
        this.seq = seq;
        this.index = index;
        lastIndex = lazy? Integer.MAX_VALUE : index.get();
        bind(lazy, new AbstractBindingExpression() {
            public void compute() {
                lastIndex = BoundSequenceElement.this.index.get();
                pushValue(BoundSequenceElement.this.seq.getAsSequence().get(lastIndex));
            }
        }, index);
        seq.addSequenceChangeListener(new MySequenceListener<T>(this));
    }

    private static class MySequenceListener<T> extends ChangeListener<T> {
        private WeakReference<BoundSequenceElement<T>> weakMe;

        MySequenceListener(BoundSequenceElement bse) {
            this.weakMe = new WeakReference<BoundSequenceElement<T>>(bse);
        }

        @Override
        public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
            onChangeB(buffer, oldValue, startPos, endPos, newElements);
        }

        @Override
        public boolean onChangeB(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
            BoundSequenceElement<T> bse = (BoundSequenceElement<T>) weakMe.get();
            if (bse != null) {
                int deltaSize = (endPos-startPos) - Sequences.sizeOfNewElements(buffer, startPos, newElements);
                if (deltaSize != 0) {
                    if (startPos <= bse.lastIndex)
                        bse.invalidate();
                }
                else {
                    if (startPos <= bse.lastIndex && bse.lastIndex <= endPos)
                        bse.invalidate();
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
