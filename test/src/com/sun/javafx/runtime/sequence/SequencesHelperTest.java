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
        singleInteger   = Sequences.make(Integer.class, 0);
        sortedInteger   = Sequences.make(Integer.class, 1, 2, 3);
        unsortedInteger = Sequences.make(Integer.class, 6, 4, 5);
        
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
    
    /** 
     * <T extends Comparable> int binarySearch (Sequence<? extends T> seq, T key) 
     * This method uses Arrays.binarySearch for sorting, which we can asume to
     * work. Only tests for the mapping are needed.
     */
    public void testBinarySearchComparable() {
        int result;
        // search in empty sequence
        result = SequencesHelper.binarySearch(emptyInteger, 1);
        assertEquals(Sequences.emptySequence(Integer.class), emptyInteger);
        assertEquals(-1, result);
        
        // single element sequence
        // successful search
        result = SequencesHelper.binarySearch(singleInteger, 0);
        assertEquals(singleInteger, 0);
        assertEquals(0, result);
        
        // unsuccessful search
        result = SequencesHelper.binarySearch(singleInteger, 1);
        assertEquals(singleInteger, 0);
        assertEquals(-2, result);
        
        // three elements sequence
        // successful search
        result = SequencesHelper.binarySearch(sortedInteger, 2);
        assertEquals(sortedInteger, 1, 2, 3);
        assertEquals(1, result);
        
        // unsuccessful search
        result = SequencesHelper.binarySearch(sortedInteger, 0);
        assertEquals(sortedInteger, 1, 2, 3);
        assertEquals(-1, result);
    }
    
    /** 
     * <T> int binarySearch(Sequence<? extends T> seq, T key, Comparator<? super T> c) 
     * This method uses Arrays.binarySearch for sorting, which we can asume to
     * work. Only tests for the mapping are needed.
     */
    public void testBinarySearchComparator() {
        int result;
        // search in empty sequence
        result = SequencesHelper.binarySearch(emptyElements, element[1], comparator);
        assertEquals(Sequences.emptySequence(DummyElement.class), emptyElements);
        assertEquals(-1, result);
        
        // single element sequence
        // successful search
        result = SequencesHelper.binarySearch(singleElements, element[0], comparator);
        assertEquals(singleElements, element[0]);
        assertEquals(0, result);
        
        // unsuccessful search
        result = SequencesHelper.binarySearch(singleElements, element[1], comparator);
        assertEquals(singleElements, element[0]);
        assertEquals(-2, result);
        
        // three elements sequence
        // successful search
        result = SequencesHelper.binarySearch(sortedElements, element[2], comparator);
        assertEquals(sortedElements, element[1], element[2], element[3]);
        assertEquals(1, result);
        
        // unsuccessful search
        result = SequencesHelper.binarySearch(sortedElements, element[0], comparator);
        assertEquals(sortedElements, element[1], element[2], element[3]);
        assertEquals(-1, result);

        // search using null-comparator
        int resultInt = SequencesHelper.binarySearch(sortedInteger, 2, null);
        assertEquals(sortedInteger, 1, 2, 3);
        assertEquals(1, resultInt);
        
        // exception if using null-operator with non-comparable elements
        try {
            result = SequencesHelper.binarySearch(sortedElements, element[2], null);
            fail("No exception thrown.");
        }
        catch (ClassCastException ex) {
            assertEquals(sortedElements, element[1], element[2], element[3]);
        }
        catch (Exception ex) {
            fail("Unexpected exception thrown: " + ex.getMessage());
        }
    }
    
    /**
     * <T extends Comparable> Sequence<T> sort (Sequence<T> seq) 
     * This method uses Arrays.sort for sorting, which we can asume to work.
     * Only tests for the mapping are needed.
     */
    public void testSortComparable() {
        Sequence<Integer> result;
        
        // sort empty sequence
        result = SequencesHelper.sort(emptyInteger);
        assertEquals(Sequences.emptySequence(Integer.class), emptyInteger);
        assertEquals(Sequences.emptySequence(Integer.class), result);
        
        // sort single element
        result = SequencesHelper.sort(singleInteger);
        assertEquals(singleInteger, 0);
        assertEquals(result, 0);
        
        // sort unsorted sequence
        result = SequencesHelper.sort(unsortedInteger);
        assertEquals(unsortedInteger, 6, 4, 5);
        assertEquals(result, 4, 5, 6);
    }
    
    /**
     * <T> Sequence<T> sort (Sequence<T> seq, Comparator<? super T> c) 
     * This method uses Arrays.sort for sorting, which we can asume to work.
     * Only tests for the mapping are needed.
     */
    public void testSortComparator() {
        Sequence<DummyElement> result;
                
        // sort empty sequence
        result = SequencesHelper.sort(emptyElements, comparator);
        assertEquals(Sequences.emptySequence(DummyElement.class), emptyElements);
        assertEquals(Sequences.emptySequence(DummyElement.class), result);
        
        // sort single element
        result = SequencesHelper.sort(singleElements, comparator);
        assertEquals(singleElements, element[0]);
        assertEquals(result, element[0]);
        
        // sort unsorted sequence
        result = SequencesHelper.sort(unsortedElements, comparator);
        assertEquals(unsortedElements, element[6], element[4], element[5]);
        assertEquals(result, element[4], element[5], element[6]);
        
        // sort using null-comparator
        Sequence<Integer> resultInt = SequencesHelper.sort(unsortedInteger, null);
        assertEquals(unsortedInteger, 6, 4, 5);
        assertEquals(resultInt, 4, 5, 6);
        
        // exception if using null-operator with non-comparable elements
        try {
            result = SequencesHelper.sort(unsortedElements, null);
            fail("No exception thrown.");
        }
        catch (ClassCastException ex) {
            assertEquals(unsortedElements, element[6], element[4], element[5]);
        }
        catch (Exception ex) {
            fail("Unexpected exception thrown: " + ex.getMessage());
        }
    }

}
