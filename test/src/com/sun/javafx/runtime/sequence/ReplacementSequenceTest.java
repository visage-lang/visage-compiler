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
import com.sun.javafx.runtime.TypeInfos;
import org.junit.Assert;

/**
 *
 * @author Michael Heinrichs
 */
public class ReplacementSequenceTest extends JavaFXTestCase {
    
    private Sequence<Integer> REPLACEMENT_FROM_SINGLETON_SEQUENCE;
    private Sequence<Integer> REPLACEMENT_AT_START;
    private Sequence<Integer> REPLACEMENT_IN_MIDDLE;
    private Sequence<Integer> REPLACEMENT_AT_END;
    private Sequence<Integer> OVERLAPPING_REPLACEMENT;
    private final Integer C = 7;
    private final Integer D = 8;

    @Override
    protected void setUp() {
        REPLACEMENT_FROM_SINGLETON_SEQUENCE = new ReplacementSequence<Integer>(Sequences.singleton(TypeInfos.Integer, 1), 0, 2);
        Sequence<Integer> baseSequence = Sequences.make(TypeInfos.Integer, 1, 2, 3);
        REPLACEMENT_AT_START = new ReplacementSequence<Integer>(baseSequence, 0, C);
        REPLACEMENT_IN_MIDDLE = new ReplacementSequence<Integer>(baseSequence, 1, C);
        REPLACEMENT_AT_END = new ReplacementSequence<Integer>(baseSequence, 2, C);
        OVERLAPPING_REPLACEMENT = new ReplacementSequence<Integer>(REPLACEMENT_IN_MIDDLE, 1, D);
    }
    
    public void testToArray() {
        Object[] actuals = new Object[1];
        REPLACEMENT_FROM_SINGLETON_SEQUENCE.toArray(actuals, 0);
        Assert.assertArrayEquals(new Object[] {2}, actuals);
        
        actuals = new Object[3];
        REPLACEMENT_AT_START.toArray(actuals, 0);
        Assert.assertArrayEquals(new Object[] {C, 2, 3}, actuals);
        assertEquals(REPLACEMENT_AT_START, C, 2, 3);

        REPLACEMENT_IN_MIDDLE.toArray(actuals, 0);
        Assert.assertArrayEquals(new Object[] {1, C, 3}, actuals);
        assertEquals(REPLACEMENT_IN_MIDDLE, 1, C, 3);

        REPLACEMENT_AT_END.toArray(actuals, 0);
        Assert.assertArrayEquals(new Object[] {1, 2, C}, actuals);
        assertEquals(REPLACEMENT_AT_END, 1, 2, C);

        OVERLAPPING_REPLACEMENT.toArray(actuals, 0);
        Assert.assertArrayEquals(new Object[] {1, D, 3}, actuals);
        assertEquals(OVERLAPPING_REPLACEMENT, 1, D, 3);

        // test offset
        actuals = new Object[4];
        actuals[0] = D;
        REPLACEMENT_IN_MIDDLE.toArray(actuals, 1);
        Assert.assertArrayEquals(new Object[] {D, 1, C, 3}, actuals);
        assertEquals(REPLACEMENT_IN_MIDDLE, 1, C, 3);
    }
    
}
