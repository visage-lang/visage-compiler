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
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * Helper classes for building bound sequences of the form bind [ a, b, c], much like StringBuilder assists in building
 * Strings. BoundSequenceBuilder stores the locations building built in an array, which is automatically resized as
 * needed. It can be converted to a SequenceLocation by calling toSequence().
 *
 * @author Brian Goetz
 */
public class BoundSequenceBuilder<T> {
    private BoundCompositeSequence<T> result;

    public BoundSequenceBuilder(boolean lazy, TypeInfo<T, ?> typeInfo) {
        result = new BoundCompositeSequence<T>(lazy, typeInfo);
    }

    public BoundSequenceBuilder(boolean lazy, int initialSize, TypeInfo<T, ?> typeInfo) {
        result = new BoundCompositeSequence<T>(lazy, initialSize, typeInfo);
    }

    /** Add an existing SequenceLocation, which will be flattened */
    public void add(SequenceLocation<? extends T> seq) {
        result.add(seq);
    }

    /** Add an instance location to the sequence */
    public void add(ObjectLocation<? extends T> singleton) {
        result.add(singleton);
    }

    /** Convert to a SequenceLocation.  The elements will be copied to a new sequence, and will remain
     * in the builder */
    public SequenceLocation<T> toSequence() {
        result.toSequence();
        return result;
    }
}
