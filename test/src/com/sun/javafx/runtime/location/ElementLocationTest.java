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
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.BoundSequences;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * ElementLocationTest
 *
 * @author Brian Goetz
 */
public class ElementLocationTest extends JavaFXTestCase {
    public void testElementLocation() {
        SequenceLocation<Integer> seq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        IntLocation index = IntVariable.make(1);
        ObjectLocation<Integer> second = BoundSequences.element(seq, index);
        CountingListener cl = new CountingListener();
        second.addChangeListener(cl);

        assertEquals(cl.count, 0);
        assertEquals((int) second.get(), 2);
        index.setAsInt(0);
        assertEquals(cl.count, 1);
        assertEquals((int) second.get(), 1);
        seq.insertBefore(-1, 0);
        assertEquals(cl.count, 2);
        assertEquals(seq.getAsSequence(), -1, 1, 2, 3);
        assertEquals((int) second.get(), -1);
        seq.delete(0);
        assertEquals(cl.count, 3);
        assertEquals((int) second.get(), 1);

        seq.insert(4);
        assertEquals(cl.count, 3);
        assertEquals(seq.getAsSequence(), 1, 2, 3, 4);
        assertEquals((int) second.get(), 1);
        seq.delete(3);
        assertEquals(cl.count, 3);
        assertEquals((int) second.get(), 1);

        seq.setAsSequence(Sequences.range(1, 5));
        assertEquals((int) second.get(), 1);

        // @@@ These fail because of AIOOBE
//        seq.setAsSequence(Sequences.emptySequence(Integer.class));
//        assertEquals((int) second.get(), 0);
//
//        seq.setAsSequence(Sequences.range(1, 5));
//        assertEquals((int) second.get(), 1);
    }

    public void testSizeof() {
        SequenceLocation<Integer> seq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        IntLocation size = BoundSequences.sizeof(seq);
        assertEquals(3, size.getAsInt());
        seq.deleteAll();
        assertEquals(0, size.getAsInt());
        seq.insert(3);
        assertEquals(1, size.getAsInt());
    }
}
