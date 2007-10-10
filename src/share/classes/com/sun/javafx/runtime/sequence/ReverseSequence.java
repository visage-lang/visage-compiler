/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

/**
 * Provides a view of another sequence with the elements in reverse order.  Reversed sequences should be created
 * with the static factory Sequences.reverse() rather than with the ReverseSequence constructor.  O(1) space and time
 * construction costs.
 *
 * @author Brian Goetz
 */
class ReverseSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<T> sequence;
    private final int size;

    public ReverseSequence(Sequence<T> sequence) {
        super(sequence.getElementType());
        this.sequence = sequence;
        this.size = sequence.size();
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }

    @Override
    public T get(int position) {
        return sequence.get(size - 1 - position);
    }
}

