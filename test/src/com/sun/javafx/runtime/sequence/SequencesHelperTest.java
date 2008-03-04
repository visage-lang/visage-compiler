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

import com.sun.javafx.runtime.JavaFXTestCase;

import java.util.Comparator;

/**
 * SequencesHelperTest
 *
 * @author Michael Heinrichs
 */
public class SequencesHelperTest extends JavaFXTestCase {

    public static class DummyElement {
        public int id;
        public DummyElement(int id) { this.id = id; }
    }
    public static class DummyComparator implements Comparator<DummyElement> {
        public int compare(DummyElement o1, DummyElement o2) {
            return o1.id - o2.id;
        }
    }
    
    public Sequence<Integer> emptyInteger, singleInteger, sortedInteger, unsortedInteger;
    public Sequence<DummyElement> emptyElements, singleElements, sortedElements, unsortedElements;

    public static DummyElement[] element;
    public static DummyComparator comparator;
    
    @Override
    protected void setUp() {
        // Integer-sequences
        emptyInteger    = Sequences.emptySequence(Integer.class);
        singleInteger   = Sequences.range(1, 1);
        sortedInteger   = Sequences.range(2, 4);
        unsortedInteger = Sequences.make(Integer.class, 7, 5, 6);
        
        // 7 Dummyelements
        element = new DummyElement[7];
        for (int i=0; i<element.length; ++i)
            element[i] = new DummyElement(i);
        
        // DummyElement-sequences
        emptyElements    = Sequences.emptySequence(DummyElement.class);
        singleElements   = Sequences.make(DummyElement.class, element[0]);
        sortedElements   = Sequences.make(DummyElement.class, element[1], element[2], element[3]);
        unsortedElements = Sequences.make(DummyElement.class, element[6], element[4], element[5]);

        // Comparator
        comparator = new DummyComparator();
    }
    
    /**  <T extends Comparable> Sequence<T> sort (Sequence<T> seq) */
    public void testSortComparable() {
        // sort empty sequence
        Sequence<Integer> result = SequencesHelper.sort(emptyInteger);
        assertEquals(result, Sequences.emptySequence(Integer.class));
        
        // sort single element
        result = SequencesHelper.sort(singleInteger);
        assertEquals(singleInteger, 1);
        assertEquals(result, 1);
        
        // sort sorted sequence
        result = SequencesHelper.sort(sortedInteger);
        assertEquals(sortedInteger, 2, 3, 4);
        assertEquals(result, 2, 3, 4);
        
        // sort unsorted sequence
        result = SequencesHelper.sort(unsortedInteger);
        assertEquals(unsortedInteger, 7, 5, 6);
        assertEquals(result, 5, 6, 7);
    }
    
    /**  <T> Sequence<T> sort (Sequence<T> seq, Comparator<? super T> c) */
    public void testSortComparator() {
        // sort empty sequence
        Sequence<DummyElement> result = SequencesHelper.sort(emptyElements, comparator);
        assertEquals(result, Sequences.emptySequence(DummyElement.class));
        
        // sort single element
        result = SequencesHelper.sort(singleElements, comparator);
        assertEquals(singleElements, element[0]);
        assertEquals(result, element[0]);
        
        // sort sorted sequence
        result = SequencesHelper.sort(sortedElements, comparator);
        assertEquals(sortedElements, element[1], element[2], element[3]);
        assertEquals(result, element[1], element[2], element[3]);
        
        // sort unsorted sequence
        result = SequencesHelper.sort(unsortedElements, comparator);
        assertEquals(unsortedElements, element[6], element[4], element[5]);
        assertEquals(result, element[4], element[5], element[6]);
    }

}
