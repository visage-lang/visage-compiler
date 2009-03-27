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

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.location.*;

/**
 * BoundSequenceSliceTest
 *
 * @author Zhiqun Chen
 */
public class BoundSequenceSliceTest extends JavaFXTestCase {

    static final boolean NOT_LAZY = false;

    public void testBounds() {
        
        IntLocation a = IntVariable.make(0);
        IntLocation b = IntVariable.make(18);
        IntLocation s = IntVariable.make(3);
            
        SequenceLocation<Integer> seq = BoundSequences.range(NOT_LAZY, a, b, s);
        
        IntLocation lower = IntVariable.make(0);
        IntLocation upper = IntVariable.make(4);
        
        SequenceLocation<Integer> slice = BoundSequences.slice(NOT_LAZY, seq.getElementType(), seq, lower, upper);
        
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        slice.addSequenceChangeListener(hl);

        assertEquals(slice, 0, 3, 6, 9, 12);
//        assertEquals(1, slice.get().getDepth());
        hl.clear();
        
        lower.set(2);
        assertEquals(slice, 6, 9, 12);
        // assertEquals(2, slice.get().getDepth());
        assertEqualsAndClear(hl, "[0, 1] => [ ]");
        
        lower.set(-1);
        assertEquals(slice, 0, 3, 6, 9, 12);
//        assertEquals(3, slice.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 0, 3 ]");
        
        lower.set(5);
        assertEquals(slice);
//        assertEquals(0, slice.get().getDepth());
        assertEqualsAndClear(hl, "[0, 4] => [ ]");
        
        lower.set(7);
        assertEquals(slice);
//        assertEquals(0, slice.get().getDepth());
        assertEqualsAndClear(hl);        
        
        lower.set(1);
        assertEquals(slice, 3, 6, 9, 12);
//        assertEquals(1, slice.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 3, 6, 9, 12 ]");
             
        upper.set(3);
        assertEquals(slice, 3, 6, 9);
//        assertEquals(2, slice.get().getDepth());
        assertEqualsAndClear(hl, "[3, 3] => [ ]"); 
        
        upper.set(7);
        assertEquals(slice, 3, 6, 9, 12, 15, 18);
//        assertEquals(3, slice.get().getDepth());
        assertEqualsAndClear(hl, "[3, 2] => [ 12, 15, 18 ]"); 
        
        upper.set(8);
        assertEquals(slice, 3, 6, 9, 12, 15, 18);
//        assertEquals(3, slice.get().getDepth());
        assertEqualsAndClear(hl);  
        
        upper.set(4);
        assertEquals(slice, 3, 6, 9, 12);
//        assertEquals(4, slice.get().getDepth());
        assertEqualsAndClear(hl, "[4, 5] => [ ]");  
        
        upper.set(1);
        assertEquals(slice, 3);
//        assertEquals(5, slice.get().getDepth());
        assertEqualsAndClear(hl, "[1, 3] => [ ]");  
        
        upper.set(0);
        assertEquals(slice);
//        assertEquals(0, slice.get().getDepth());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");    
        
        upper.set(-1);
        assertEquals(slice);
//        assertEquals(0, slice.get().getDepth());
        assertEqualsAndClear(hl);
        
        upper.set(8);
        assertEquals(slice, 3, 6, 9, 12, 15, 18);
//        assertEquals(1, slice.get().getDepth());
        assertEqualsAndClear(hl,"[0, -1] => [ 3, 6, 9, 12, 15, 18 ]");
        
        lower.set(-1);
        assertEquals(slice, 0, 3, 6, 9, 12, 15, 18);
//        assertEquals(2, slice.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 0 ]");
    }
    
    
    // the underlying sequence has been replaced
    public void testBounds2() {
  
        IntLocation a = IntVariable.make(0);
        IntLocation b = IntVariable.make(18);
        IntLocation s = IntVariable.make(3);
            
        SequenceLocation<Integer> seq = BoundSequences.range(NOT_LAZY, a, b, s);
        
        IntLocation lower = IntVariable.make(1);
        IntLocation upper = IntVariable.make(4);
        
        SequenceLocation<Integer> slice = BoundSequences.slice(NOT_LAZY, seq.getElementType(), seq, lower, upper);
        
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        slice.addSequenceChangeListener(hl);
        
        assertEquals(slice, 3, 6, 9, 12);
//        assertEquals(1, slice.get().getDepth());
        hl.clear();
        
        a.set(-3);
        assertEquals(slice, 0, 3, 6, 9);
        assertEqualsAndClear(hl, "[0, 3] => [ 0, 3, 6, 9 ]");
        
        a.set(5);
        assertEquals(slice, 8, 11, 14, 17);
        assertEqualsAndClear(hl, "[0, 3] => [ 8, 11, 14, 17 ]");
        
        a.set(10);
        assertEquals(slice, 13, 16);
        assertEqualsAndClear(hl, "[0, 1] => [ 13, 16 ]");
        
        a.set(19);
        assertEquals(slice);
        assertEqualsAndClear(hl, "[0, 0] => [ ]");
        
        b.set(34);
        assertEquals(slice, 22, 25, 28, 31);
        assertEqualsAndClear(hl, "[0, 3] => [ 22, 25, 28, 31 ]");
                
        b.set(40);
        assertEquals(slice, 22, 25, 28, 31);
        assertEqualsAndClear(hl, "[0, 3] => [ 22, 25, 28, 31 ]");
        
        b.set(26);
        assertEquals(slice, 22, 25);
        assertEqualsAndClear(hl, "[0, 1] => [ 22, 25 ]");
        
        b.set(15);
        assertEquals(slice);
        assertEqualsAndClear(hl, "[0, 0] => [ ]");
    }
}
