/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.sequence.BoundSequences;

/**
 * BoundRangeTest
 *
 * @author Brian Goetz
 */
public class BoundRangeTest extends JavaFXTestCase {
    public void testBoundIntRange() {
        IntLocation a = IntVariable.make(10);
        IntLocation b = IntVariable.make(15);
        SequenceLocation<Integer> range = BoundSequences.range(a, b);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        range.addChangeListener(hl);

        assertEquals(range, 10, 11, 12, 13, 14, 15);
        hl.clear();
        
        a.set(8);
        assertEquals(range, 8, 9, 10, 11, 12, 13, 14, 15);
        assertEqualsAndClear(hl, "[0, -1] => [ 8, 9 ]");

        a.set(11);
        assertEquals(range, 11, 12, 13, 14, 15);
        assertEqualsAndClear(hl, "[0, 2] => [ ]");

        b.set(17);
        assertEquals(range, 11, 12, 13, 14, 15, 16, 17);
        assertEqualsAndClear(hl, "[5, 4] => [ 16, 17 ]");

        b.set(14);
        assertEquals(range, 11, 12, 13, 14);
        assertEqualsAndClear(hl, "[4, 6] => [ ]");

        a.set(19);
        assertEquals(range);
        assertEqualsAndClear(hl, "[0, 3] => [ ]");

        a.set(11);
        assertEquals(range, 11, 12, 13, 14);
        assertEqualsAndClear(hl, "[0, -1] => [ 11, 12, 13, 14 ]");

        b.set(1);
        assertEquals(range);
        assertEqualsAndClear(hl, "[0, 3] => [ ]");

        b.set(16);
        assertEquals(range, 11, 12, 13, 14, 15, 16);
        assertEqualsAndClear(hl, "[0, -1] => [ 11, 12, 13, 14, 15, 16 ]");


    }
}
