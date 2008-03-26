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
package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * PointerTest
 *
 * @author Brian Goetz
 */
public class PointerTest extends JavaFXTestCase {
    public void testPointers() {
        final SequenceVariable<Integer> seqvar = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceVariable<Integer> seqexp = SequenceVariable.make(Integer.class, false,
                                                                       new SequenceBindingExpression<Integer>() {
                                                                           public Sequence<? extends Integer> computeValue() {
                                                                               return seqvar.getAsSequence();
                                                                           }
                                                                       }, seqvar);
        final IntVariable intvar1 = IntVariable.make(3);
        final IntVariable intvar2 = IntVariable.make(3);

        Pointer pseqvar = Pointer.make(seqvar);
        Pointer pseqexp = Pointer.make(seqexp);
        Pointer pintvar1 = Pointer.make(intvar1);
        Pointer pintvar2 = Pointer.make(intvar2);
        Pointer[] pointers = new Pointer[] { pseqvar, pseqexp, pintvar1, pintvar2 };
        for (int i=0; i<pointers.length; i++)
            for (int j=0; j<pointers.length; j++)
                if (i == j) {
                    assertEquals(pointers[i], pointers[j]);
                    assertEquals(pointers[i].hashCode(), pointers[j].hashCode());
                }
                else
                    assertFalse(pointers[i].equals(pointers[j]));

        Pointer anotherPintvar2 = Pointer.make(intvar2);
        assertEquals(pintvar2,  anotherPintvar2);
        assertEquals(pintvar2.hashCode(),  anotherPintvar2.hashCode());

        final DoubleLocation asDouble = Locations.asDoubleLocation(intvar1);
        Pointer pdwrapper = Pointer.make(asDouble);
        assertEquals(pintvar1, pdwrapper);
        assertEquals(pintvar1.hashCode(), pdwrapper.hashCode());

        final ObjectLocation<Integer> asObject = Locations.asObjectLocation(intvar1);
        Pointer powrapper = Pointer.make(asObject);
        assertEquals(pintvar1, powrapper);
        assertEquals(pintvar1.hashCode(), powrapper.hashCode());

        assertEquals((Integer) 3, pintvar2.get());
        assertEquals((Integer) 3, anotherPintvar2.get());
        pintvar2.set(5);
        assertEquals(5, intvar2.getAsInt());
        assertEquals((Integer) 5, pintvar2.get());
        assertEquals((Integer) 5, anotherPintvar2.get());

        assertEquals((Sequence<Integer>) pseqvar.get(), 1, 2, 3);
        pseqvar.set(Sequences.range(1, 5));
        assertEquals(seqvar, 1, 2, 3, 4, 5);
        assertEquals(seqexp, 1, 2, 3, 4, 5);
        assertEquals((Sequence<Integer>) pseqvar.get(), 1, 2, 3, 4, 5);
        assertEquals((Sequence<Integer>) pseqexp.get(), 1, 2, 3, 4, 5);
    }
}
