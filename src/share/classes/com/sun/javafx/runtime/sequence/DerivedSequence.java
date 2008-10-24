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

/**
 * DerivedSequence
 *
 * @author Brian Goetz
 */
abstract class DerivedSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    protected final Sequence<? extends T> sequence;
    protected final int size, depth;

    public DerivedSequence(TypeInfo<T> ti, Sequence<? extends T> sequence, int size, int depth) {
        super(ti);
        this.sequence = sequence;
        this.size = size;
        this.depth = depth;
    }

    protected DerivedSequence(TypeInfo<T> ti, Sequence<? extends T> sequence, int size) {
        this(ti, sequence, size, sequence.getDepth() + 1);
    }

    protected DerivedSequence(TypeInfo<T> ti, Sequence<? extends T> sequence) {
        this(ti, sequence, sequence.size(), sequence.getDepth() + 1);
    }

    public int size() {
        return size;
    }

    @Override
    public int getDepth() {
        return depth;
    }
}
