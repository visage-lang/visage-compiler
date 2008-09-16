/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

/**
 * Provides a view of another sequence with the elements in reverse order.  Reversed sequences should be created
 * with the static factory Sequences.reverse() rather than with the ReverseSequence constructor.  O(1) space and time
 * construction costs.
 *
 * @author Brian Goetz
 */
class ReverseSequence<T> extends DerivedSequence<T> implements Sequence<T> {
    public ReverseSequence(Sequence<T> sequence) {
        super(sequence.getElementType(), sequence);
    }

    @Override
    public T get(int position) {
        return sequence.get(size - 1 - position);
    }
    
//    @Override
//    public void toArray(Object[] dest, int destOffset) {
//        sequence.toArray(dest, destOffset);
//        Object obj;
//        for (int i=0; i<size/2; i++) {
//            obj = dest[destOffset+i];
//            dest[destOffset+i] = dest[destOffset+size-i];
//            dest[destOffset+size-i] = obj;
//        }
//    }
}

