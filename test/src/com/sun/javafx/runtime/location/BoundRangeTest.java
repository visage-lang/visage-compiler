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

package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.sequence.BoundSequences;

/**
 * BoundRangeTest
 *
 * @author Brian Goetz
 */
public class BoundRangeTest extends JavaFXTestCase {

    static final boolean NOT_LAZY = false;

    public void testBoundIntRange() {
        IntLocation a = IntVariable.make(10);
        IntLocation b = IntVariable.make(15);    
        SequenceLocation<Integer> range = BoundSequences.range(NOT_LAZY, a, b);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        range.addSequenceChangeListener(hl);

        assertEquals(range, 10, 11, 12, 13, 14, 15);
        assertEquals(0, range.get().getDepth());
        hl.clear();
        
        a.set(8);
        assertEquals(range, 8, 9, 10, 11, 12, 13, 14, 15);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 8, 9 ]");

        a.set(11);
        assertEquals(range, 11, 12, 13, 14, 15);
//        assertEquals(2, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 2] => [ ]");

        b.set(17);
        assertEquals(range, 11, 12, 13, 14, 15, 16, 17);
//        assertEquals(3, range.get().getDepth());
        assertEqualsAndClear(hl, "[5, 4] => [ 16, 17 ]");

        b.set(14);
        assertEquals(range, 11, 12, 13, 14);
//        assertEquals(4, range.get().getDepth());
        assertEqualsAndClear(hl, "[4, 6] => [ ]");

        a.set(19);
        assertEquals(range);  // range becomes an empty sequence
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 3] => [ ]");

        a.set(11);
        assertEquals(range, 11, 12, 13, 14); // when a sequence (s) is inserted into an empty empty, s is returned
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 11, 12, 13, 14 ]");

        b.set(1);
        assertEquals(range);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 3] => [ ]");

        b.set(16);
        assertEquals(range, 11, 12, 13, 14, 15, 16);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 11, 12, 13, 14, 15, 16 ]");
    }

    // test for step other than 1
    public void testStep2() {
        IntLocation c = IntVariable.make(10);
        IntLocation d = IntVariable.make(15);    
        IntLocation s = IntVariable.make(2);    
        HistoryReplaceListener<Integer> hls = new HistoryReplaceListener<Integer>();
       
        
        SequenceLocation<Integer> stepRange = BoundSequences.range(NOT_LAZY, c, d, s);
        stepRange.addSequenceChangeListener(hls);
        
        assertEquals(stepRange, 10, 12, 14);
//        assertEquals(0, stepRange.get().getDepth());
        hls.clear();
  
        c.set(8);
        assertEquals(stepRange, 8, 10, 12,  14);
//        assertEquals(1, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[0, -1] => [ 8 ]");

        
        c.set(11);
        assertEquals(stepRange, 11,  13,  15);
//        assertEquals(0, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[0, 3] => [ 11, 13, 15 ]");

        
        d.set(17);
        assertEquals(stepRange, 11, 13, 15, 17);
//        assertEquals(1, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[3, 2] => [ 17 ]");
        
        d.set(14);
        assertEquals(stepRange, 11, 13);
//        assertEquals(2, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[2, 3] => [ ]");

        c.set(19);
        assertEquals(stepRange);
//        assertEquals(0, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[0, 1] => [ ]");

        c.set(11);
        assertEquals(stepRange, 11,13);
//        assertEquals(0, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[0, -1] => [ 11, 13 ]");

        d.set(1);
        assertEquals(stepRange);
//        assertEquals(0, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[0, 1] => [ ]");
      
        d.set(16);
        assertEquals(stepRange, 11,  13,  15);
//        assertEquals(0, stepRange.get().getDepth());
        assertEqualsAndClear(hls, "[0, -1] => [ 11, 13, 15 ]");       

    }

    // step for sequences with exclusive upper bound and step with negative value
    public void testNegativeStep() {
        IntLocation e = IntVariable.make(20);
        IntLocation f = IntVariable.make(10);    
        IntLocation ns = IntVariable.make(-3);    
        HistoryReplaceListener<Integer> hle = new HistoryReplaceListener<Integer>();
       
        
        SequenceLocation<Integer> exclusiveRange = BoundSequences.rangeExclusive(NOT_LAZY, e, f, ns );
        exclusiveRange.addSequenceChangeListener(hle);
        
        assertEquals(exclusiveRange, 20, 17, 14, 11);
//        assertEquals(0, exclusiveRange.get().getDepth());
        hle.clear();
        
        e.set(17);
        assertEquals(exclusiveRange, 17, 14, 11);
//        assertEquals(1, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, 0] => [ ]");
        
        e.set(16);
        assertEquals(exclusiveRange, 16, 13);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, 2] => [ 16, 13 ]");
        
        e.set(10);
        assertEquals(exclusiveRange);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, 1] => [ ]");
        
        f.set(0);
        assertEquals(exclusiveRange, 10, 7, 4, 1);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, -1] => [ 10, 7, 4, 1 ]");
        
        f.set(-5);
        assertEquals(exclusiveRange, 10, 7, 4, 1, -2);
//        assertEquals(1, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[4, 3] => [ -2 ]");
        
        f.set(-7);
        assertEquals(exclusiveRange, 10, 7, 4, 1, -2, -5);
//        assertEquals(2, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[5, 4] => [ -5 ]");
        
        f.set(-4);
        assertEquals(exclusiveRange, 10, 7, 4, 1, -2);
//        assertEquals(3, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[5, 5] => [ ]");
        
        f.set(-2);    
        assertEquals(exclusiveRange, 10, 7, 4, 1);
//        assertEquals(4, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[4, 4] => [ ]");
        
        f.set(10);
        assertEquals(exclusiveRange);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, 3] => [ ]");
        
        f.set(-10);
        assertEquals(exclusiveRange, 10, 7, 4, 1, -2, -5, -8);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, -1] => [ 10, 7, 4, 1, -2, -5, -8 ]");
        
        ns.set(3);
        assertEquals(exclusiveRange);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, 6] => [ ]");
        
        ns.set(-4);
        assertEquals(exclusiveRange, 10, 6, 2, -2, -6);
//        assertEquals(0, exclusiveRange.get().getDepth());
        assertEqualsAndClear(hle, "[0, -1] => [ 10, 6, 2, -2, -6 ]");
    }
    
    public void testBoundNumberRange() {
        FloatLocation a = FloatVariable.make(10.2f);
        FloatLocation b = FloatVariable.make(15.7f);
        SequenceLocation<Float> range = BoundSequences.range(NOT_LAZY, a, b);
                
        HistoryReplaceListener<Float> hl = new HistoryReplaceListener<Float>();
        range.addSequenceChangeListener(hl);

        assertEquals(range, 10.2f, 11.2f, 12.2f, 13.2f, 14.2f, 15.2f);
//        assertEquals(0, range.get().getDepth());
        hl.clear();
       
        a.set(8.2f);
        assertEquals(range, 8.2f, 9.2f, 10.2f, 11.2f, 12.2f, 13.2f, 14.2f, 15.2f);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 8.2, 9.2 ]");

        a.set(11.2f);
        assertEquals(range, 11.2f, 12.2f, 13.2f, 14.2f, 15.2f);
//        assertEquals(2, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 2] => [ ]");
        
        a.set(11.88f);
        assertEquals(range, 11.88f, 12.88f, 13.88f, 14.88f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 4] => [ 11.88, 12.88, 13.88, 14.88 ]");
        
        a.set(9.22f);
        assertEquals(range, 9.22f, 10.22f, 11.22f, 12.22f, 13.22f, 14.22f, 15.22f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 3] => [ 9.22, 10.22, 11.22, 12.22, 13.22, 14.22, 15.22 ]");
        
        a.set(9.22f);
        assertEquals(range, 9.22f, 10.22f, 11.22f, 12.22f, 13.22f, 14.22f, 15.22f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl);
              
   
        b.set(17.08f);
        assertEquals(range.getAsSequence(), 9.22f, 10.22f, 11.22f, 12.22f, 13.22f, 14.22f, 15.22f, 16.22f);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[7, 6] => [ 16.220001 ]");

        b.set(14.23f);
        assertEquals(range, 9.22f, 10.22f, 11.22f, 12.22f, 13.22f, 14.22f);
//        assertEquals(2, range.get().getDepth());
        assertEqualsAndClear(hl, "[6, 7] => [ ]");

        a.set(19.0f);
        assertEquals(range);  // range becomes an empty sequence
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 5] => [ ]");

        a.set(11.0f);
        assertEquals(range, 11.0f, 12.0f, 13.0f, 14.0f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 11.0, 12.0, 13.0, 14.0 ]");

        b.set(14.0f);
        assertEquals(range, 11.0f, 12.0f, 13.0f, 14.0f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl);

        b.set(16.0f);
        assertEquals(range, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[4, 3] => [ 15.0, 16.0 ]");        
         
    }
    
     public void testBoundNumberRangeExclusive() {
        FloatLocation a = FloatVariable.make(10.2f);
        FloatLocation b = FloatVariable.make(15.2f);
        SequenceLocation<Float> range = BoundSequences.rangeExclusive(NOT_LAZY, a, b);
                
        HistoryReplaceListener<Float> hl = new HistoryReplaceListener<Float>();
        range.addSequenceChangeListener(hl);
        
        assertEquals(range, 10.2f, 11.2f, 12.2f, 13.2f, 14.2f);
//        assertEquals(0, range.get().getDepth());
        hl.clear();
       
        b.set(14.2f);
        assertEquals(range, 10.2f, 11.2f, 12.2f, 13.2f);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[4, 4] => [ ]");
        
        a.set(14.2f);
        assertEquals(range);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 3] => [ ]");
        
        a.set(14.1f);
        assertEquals(range, 14.1f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, -1] => [ 14.1 ]");
        
     }
     
     public void testBoundNumberStep() {
        FloatLocation a = FloatVariable.make(0.0f);
        FloatLocation b = FloatVariable.make(4.0f);
        FloatLocation s = FloatVariable.make(2.0f);
        SequenceLocation<Float> range = BoundSequences.rangeExclusive(NOT_LAZY, a, b, s);
        
        HistoryReplaceListener<Float> hl = new HistoryReplaceListener<Float>();
        range.addSequenceChangeListener(hl);
        
        assertEquals(range, 0.0f, 2.0f);
//        assertEquals(0, range.get().getDepth());
        hl.clear();
        
        s.set(3.0f);
        assertEquals(range, 0.0f, 3.0f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 1] => [ 0.0, 3.0 ]");
        
        a.set(10.2f);
        b.set(16.2f);
        hl.clear();
        
        assertEquals(range, 10.2f, 13.2f);
//        assertEquals(0, range.get().getDepth());
        hl.clear();
       
        b.set(13.2f);
        assertEquals(range, 10.2f);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[1, 1] => [ ]");
        
        a.set(2.2f);
        assertEquals(range, 2.2f, 5.2f, 8.2f, 11.2f);
//        assertEquals(0, range.get().getDepth());
        assertEqualsAndClear(hl, "[0, 0] => [ 2.2, 5.2, 8.2, 11.2 ]");
        
        b.set(17.2f);
        assertEquals(range, 2.2f, 5.2f, 8.2f, 11.2f, 14.2f);
//        assertEquals(1, range.get().getDepth());
        assertEqualsAndClear(hl, "[4, 3] => [ 14.2 ]");
        
        a.set(2.5f);
        b.set(-7.0f);
        s.set(-3.0f);
        hl.clear();
        
        assertEquals(range, 2.5f, -0.5f, -3.5f, -6.5f);
//        assertEquals(0, range.get().getDepth());
        hl.clear();    

     }
}
