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

import com.sun.javafx.runtime.Util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Comparator;
import java.lang.reflect.Array;

/**
 * SequencesHelper -- static helper methods for javafx.lang.Sequences.
 * The class javafx.lang.Sequences contains various functions for manipulating 
 * sequences (such as sorting and searching).
 *
 * @author Michael Heinrichs
 */
public final class SequencesHelper {

    // Inhibit instantiation
    private SequencesHelper() { }
    
    /**
     * Sorts a sequence of elements, which implement the interface Comparable.
     * This method is immutative, the result is returned in a new sequence,
     * while the original sequence is left untouched.
     * 
     * @param seq The sequence to sort.
     * @return The sorted sequence.
     */
    public static <T extends Comparable> Sequence<T> sort (Sequence<T> seq) {
        if (seq.isEmpty())
            return Sequences.emptySequence(seq.getElementType());
        T[] array = (T[])Array.newInstance(seq.getElementType(), seq.size());
        seq.toArray(array, 0);
        Arrays.sort(array);
        return Sequences.make(seq.getElementType(), array);
    }
    
    /**
     * Sorts a sequence of elements, which can be compared using a Comparator.
     * This method is immutative, the result is returned in a new sequence,
     * while the original sequence is left untouched.
     * 
     * @param seq The sequence to sort.
     * @param c The Comparator to compare the elements of the sequence.
     * @return The sorted sequence.
     */
    public static <T> Sequence<T> sort (Sequence<T> seq, Comparator<? super T> c) {
        if (seq.isEmpty())
            return Sequences.emptySequence(seq.getElementType());
        T[] array = (T[])Array.newInstance(seq.getElementType(), seq.size());
        seq.toArray(array, 0);
        Arrays.sort(array, c);
        return Sequences.make(seq.getElementType(), array);
    }
}
