/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceReplaceListener;
import com.sun.javafx.runtime.location.IntChangeListener;

/**
 * BoundSequenceSlice
 *
 * @author Brian Goetz
 */
class BoundSequenceSlice<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<T> sequenceLoc;
    private final IntLocation lowerLoc;
    private final IntLocation upperLoc;
    private int lower, upper;
    private int size;

    BoundSequenceSlice(Class<T> clazz, SequenceLocation<T> sequenceLoc, IntLocation lowerLoc, IntLocation upperLoc) {
        super(clazz);
        this.sequenceLoc = sequenceLoc;
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
    }

    protected Sequence<T> computeValue() {
        computeBounds(lowerLoc.get(), upperLoc.get());
        return sequenceLoc.get().getSlice(lower, upper);
    }

    private void computeBounds(int newLower, int newUpper) {
        lower = newLower;
        upper = newUpper;
        size = (upper >= lower) ? upper - lower + 1 : 0;
    }

    protected void initialize() {
        sequenceLoc.addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence newElements, Sequence oldValue, Sequence newValue) {
                // @@@ NYI
            }
        });
        lowerLoc.addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                // @@@ NYI
            }
        });
        upperLoc.addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                // @@@ NYI
            }
        });
    }
}
