/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.lang;

import java.lang.Object;
import java.lang.Comparable;
import java.lang.System;
import java.util.Comparator;

import com.sun.javafx.runtime.sequence.SequencesHelper;

import java.lang.UnsupportedOperationException;


/**
 * This class contains various functions for manipulating sequences (such as 
 * sorting and searching). All of these functions are nonmutative, they do not
 * change the input-parameters, but create new instances for output.
 * 
 * @author Michael Heinrichs
 */

public class Sequences {
    
    public static function binarySearch(seq: Comparable[], value: Comparable): Integer {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function binarySearch(seq: Object[], value: Object, c: Comparator): Integer {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function copyOf(seq: Object[]): Object[] {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function fill(value: Object, size: Integer): Object[] {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function indexOf(seq: Object[], value: Object): Integer {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function max(seq: Comparable[]): Comparable {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function max(seq: Object[], c: Comparator): Object {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function min(seq: Comparable[]): Comparable {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function min(seq: Object[], c: Comparator): Object {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    public static function nextIndexOf(seq: Object[], value: Object, position: Integer): Integer {
        // TODO implement
        throw new UnsupportedOperationException();
    }
    
    /**
     * Sorts a sequence of elements, which implement the interface Comparable.
     * This method is immutative, the result is returned in a new sequence,
     * while the original sequence is left untouched.
     * 
     * @param seq The sequence to sort.
     * @return The sorted sequence.
     */
    public static function sort(seq: Comparable[]): Comparable[] {
        if (seq == null) {
            return null;
        }
        return SequencesHelper.sort(seq);
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
    public static function sort(seq: Object[], c: Comparator): Object[] {
        System.out.println("{seq}");
        if (seq == null) {
            return null;
        }
        return SequencesHelper.sort(seq, c);
    }
}