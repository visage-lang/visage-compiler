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
package com.sun.javafx.runtime.location;

import java.util.Map;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.sequence.*;

/**
 * SequenceBindingTest
 *
 * @author Brian Goetz
 */
public class SequenceBindingTest extends JavaFXTestCase {

    private final SequencePredicate<Integer> isOnePredicate = new SequencePredicate<Integer>() {
        public boolean matches(Sequence sequence, int index, Integer value) {
            return value == 1;
        }
    };


    public void testUnbound() {
        Sequence<Integer> seq = Sequences.range(1, 100);
        SequenceLocation<Integer> loc = SequenceVariable.make(seq);
        if (! seq.equals(loc.getAsSequence())) {
          Class<Integer> cl1 = seq.getElementType();
          Class<Integer> cl2 = loc.getAsSequence().getElementType();
          System.err.println("unb cl1:"+cl1+" cl2:"+cl2
                             +" ass:"+cl2.isAssignableFrom(cl1));
        }
        assertEquals(seq, loc.getAsSequence());
    }

    /**
     * bind first = seq[0]
     */
    public void testElementBind() {
        final SequenceLocation<Integer> seq = SequenceVariable.make(Sequences.range(1, 3));
        IntLocation firstValue = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return seq.getAsSequence().get(0);
            }
        }, seq);
        CountingSequenceListener cl = new CountingSequenceListener();
        seq.addChangeListener(cl);
        assertEquals(seq, 1, 2, 3);

        assertEquals(1, firstValue);
        seq.set(0, 3);
        assertEquals(seq, 3, 2, 3);
        assertEquals(3, firstValue);
        assertEquals(1, cl.changeCount);
        assertEquals(1, cl.replaceCount);
    }

    public void testReplaceListener() {
        final SequenceLocation<Integer> seq = SequenceVariable.make(Sequences.range(1, 3));
        CountingSequenceListener cl = new CountingSequenceListener();
        HistorySequenceListener<Integer> hl = new HistorySequenceListener<Integer>();
        seq.addChangeListener(cl);
        seq.addChangeListener(hl);

        assertEquals(seq, 1, 2, 3);
        seq.set(0, 0);
        assertEquals(seq, 0, 2, 3);
        assertEqualsAndClear(hl, "r-0-1-0");
    }

    public void testSequenceListener() {
        final SequenceLocation<Integer> seq = SequenceVariable.make(Sequences.range(1, 3));
        CountingSequenceListener cl = new CountingSequenceListener();
        HistorySequenceListener<Integer> hl = new HistorySequenceListener<Integer>();
        seq.addChangeListener(cl);
        seq.addChangeListener(hl);

        seq.insert(4);
        assertEquals(seq, 1, 2, 3, 4);
        assertEquals(1, cl.insertCount);
        assertEquals(cl.inserted, 4);
        assertEquals(cl.changeCount, cl.insertCount);
        assertEqualsAndClear(hl, "i-3-4");

        seq.insert(Sequences.range(1, 3));
        assertEquals(seq, 1, 2, 3, 4, 1, 2, 3);
        assertEquals(4, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3);
//        assertEquals(cl.changeCount, 2);
        assertEqualsAndClear(hl, "i-4-1", "i-5-2", "i-6-3");

        seq.insertAfter(10, 5);
        assertEquals(seq, 1, 2, 3, 4, 1, 2, 10, 3);
        assertEquals(5, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10);
//        assertEquals(cl.changeCount, 3);
        assertEqualsAndClear(hl, "i-6-10");

        seq.insertAfter(8, isOnePredicate);
        assertEquals(seq, 1, 8, 2, 3, 4, 1, 8, 2, 10, 3);
        assertEquals(7, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8);
//        assertEquals(cl.changeCount, 4);
        assertEqualsAndClear(hl, "i-1-8", "i-6-8");

        seq.insertAfter(Sequences.range(1, 3), 9);
        assertEquals(seq, 1, 8, 2, 3, 4, 1, 8, 2, 10, 3, 1, 2, 3);
        assertEquals(10, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3);
//        assertEquals(cl.changeCount, 5);
        assertEqualsAndClear(hl, "i-10-1", "i-11-2", "i-12-3");

        seq.insertAfter(Sequences.range(1, 3), isOnePredicate);
        assertEquals(seq, 1, 1, 2, 3, 8, 2, 3, 4, 1, 1, 2, 3, 8, 2, 10, 3, 1, 1, 2, 3, 2, 3);
        assertEquals(19, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3);
//        assertEquals(cl.changeCount, 6);
        assertEqualsAndClear(hl, "i-1-1", "i-2-2", "i-3-3", "i-9-1", "i-10-2", "i-11-3", "i-17-1", "i-18-2", "i-19-3");

        seq.insertBefore(10, 5);
        assertEquals(seq, 1, 1, 2, 3, 8, 10, 2, 3, 4, 1, 1, 2, 3, 8, 2, 10, 3, 1, 1, 2, 3, 2, 3);
        assertEquals(20, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10);
//        assertEquals(cl.changeCount, 7);
        assertEqualsAndClear(hl, "i-5-10");

        seq.insertBefore(9, isOnePredicate);
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 10, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(26, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9);
//        assertEquals(cl.changeCount, 8);
        assertEqualsAndClear(hl, "i-0-9", "i-2-9", "i-11-9", "i-13-9", "i-21-9", "i-23-9");

        seq.insertBefore(Sequences.range(1, 3), 10);
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(29, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3);
//        assertEquals(cl.changeCount, 9);
        assertEqualsAndClear(hl, "i-10-1", "i-11-2", "i-12-3");

        seq.insertBefore(Sequences.range(1, 3), new SequencePredicate<Integer>() {
            public boolean matches(Sequence sequence, int index, Integer value) {
                return value == 10;
            }
        });
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(35, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3);
//        assertEquals(cl.changeCount, 10);
        assertEqualsAndClear(hl, "i-7-1", "i-8-2", "i-9-3", "i-25-1", "i-26-2", "i-27-3");

        seq.delete(0);
        assertEquals(seq, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(1, cl.deleteCount);
        assertEquals(cl.deleted, 9);
//        assertEquals(cl.changeCount, 11);
        assertEqualsAndClear(hl, "d-0-9");

        hl.clear();
        seq.delete(-1); // no effect
        assertEquals(seq, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(1, cl.deleteCount);
        assertEquals(cl.deleted, 9);
//        assertEquals(cl.changeCount, 11);
        assertEqualsAndClear(hl);

        hl.clear();
        seq.delete(100); // no effect
        assertEquals(seq, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(1, cl.deleteCount);
        assertEquals(cl.deleted, 9);
//        assertEquals(cl.changeCount, 11);
        assertEqualsAndClear(hl);

        seq.delete(3);
        assertEquals(seq, 1, 9, 1, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(2, cl.deleteCount);
        assertEquals(cl.deleted, 9, 2);
//        assertEquals(cl.changeCount, 12);
        assertEqualsAndClear(hl, "d-3-2");

        seq.delete(isOnePredicate);
        assertEquals(seq, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(11, cl.deleteCount);
        assertEquals(cl.deleted, 9, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
//        assertEquals(cl.changeCount, 13);
        assertEqualsAndClear(hl, "d-31-1", "d-29-1", "d-23-1", "d-18-1", "d-16-1", "d-11-1", "d-5-1", "d-2-1", "d-0-1");

        seq.insertFirst(777);
        assertEquals(seq, 777, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(36, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3, 777);
//        assertEquals(cl.changeCount, 14);
        assertEqualsAndClear(hl, "i-0-777");

        seq.insertFirst(Sequences.range(33, 34));
        assertEquals(seq, 33, 34, 777, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(38, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3, 777, 33, 34);
//        assertEquals(cl.changeCount, 15);
        assertEqualsAndClear(hl, "i-0-33", "i-1-34");
    }

    public void testSequenceExpression() {
        // oneToN = bind 1..n
        final IntLocation n = IntVariable.make(0);
        final SequenceLocation<Integer> oneToN = SequenceVariable.make(Integer.class, false,
                                                                       new SequenceBindingExpression<Integer>() {
                                                                           public Sequence<Integer> computeValue() {
                                                                               return Sequences.range(1, n.getAsInt());
                                                                           }
                                                                       }, n);
        assertEquals(oneToN.getAsSequence());
        n.setAsInt(3);
        assertEquals(oneToN.getAsSequence(), 1, 2, 3);
        n.setAsInt(1);
        assertEquals(oneToN.getAsSequence(), 1);
        n.setAsInt(-1);
        assertEquals(oneToN.getAsSequence());

        // oddN = bind select t from v where t % 2 == 1
        final SequenceLocation<Integer> v = SequenceVariable.make(Sequences.range(1, 8));
        final SequenceLocation<Integer> oddN = SequenceVariable.make(Integer.class, false,
                                                                     new SequenceBindingExpression<Integer>() {
                                                                         public Sequence<Integer> computeValue() {
                                                                             return v.getAsSequence().get(new SequencePredicate<Integer>() {
                                                                                 public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
                                                                                     return value % 2 == 1;
                                                                                 }
                                                                             });
                                                                         }
                                                                     }, v);
        assertEquals(oddN.getAsSequence(), 1, 3, 5, 7);
        v.setAsSequence(Sequences.range(3, 11));
        assertEquals(oddN.getAsSequence(), 3, 5, 7, 9, 11);
    }

    /** Ensure that mutative methods on sequence expressions throw UOE */
    public void testUOE() {
        final SequenceLocation<Integer> seq = SequenceVariable.make(Integer.class, false,
                                                                    new SequenceBindingExpression<Integer>() {
                                                                        public Sequence<Integer> computeValue() {
                                                                            return Sequences.range(1, 10);
                                                                        }
                                                                    });

        assertException(AssignToBoundException.class, seq, "deleteAll");
        assertException(AssignToBoundException.class, seq, "deleteValue(T)", 1);
        assertException(AssignToBoundException.class, seq, "delete(I)", 0);
        assertException(AssignToBoundException.class, seq, "set(IT)", 0, 0);
        assertException(AssignToBoundException.class, seq, "setAsSequence(Lcom.sun.javafx.runtime.sequence.Sequence;)", Sequences.emptySequence(Integer.class));
        assertException(AssignToBoundException.class, seq, "insert(T)", 0);
        assertException(AssignToBoundException.class, seq, "insert(Lcom.sun.javafx.runtime.sequence.Sequence;)", Sequences.emptySequence(Integer.class));
        assertException(AssignToBoundException.class, seq, "insertFirst(T)", 0);
        assertException(AssignToBoundException.class, seq, "insertFirst(Lcom.sun.javafx.runtime.sequence.Sequence;)", Sequences.emptySequence(Integer.class));
        // Also insertBefore/After 
    }

    public void testNestedSequenceBinding() {
        // oneToN = bind 1..n
        // evenN = bind select x from oneToN where x % 2 == 0
        final IntLocation n = IntVariable.make(0);
        final SequenceLocation<Integer> oneToN = SequenceVariable.make(Integer.class, false,
                                                                       new SequenceBindingExpression<Integer>() {
                                                                           public Sequence<? extends Integer> computeValue() {
                                                                               return Sequences.range(1, n.getAsInt());
                                                                           }
                                                                       }, n);
        final SequenceLocation<Integer> evenN =SequenceVariable.make(Integer.class, false,
                                                                     new SequenceBindingExpression<Integer>() {
                                                                         public Sequence<? extends Integer> computeValue() {
                                                                             return oneToN.getAsSequence().get(new SequencePredicate<Integer>() {
                                                                                 public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
                                                                                     return value % 2 == 0;
                                                                                 }
                                                                             });
                                                                         }
                                                                     }, oneToN);
        assertEquals(evenN.getAsSequence());
        n.setAsInt(3);
        assertEquals(evenN.getAsSequence(), 2);
        n.setAsInt(11);
        assertEquals(evenN.getAsSequence(), 2, 4, 6, 8, 10);
        n.setAsInt(-1);
        assertEquals(evenN.getAsSequence());
    }

    public void testDependentTrigger() {
        final SequenceLocation<Integer> v = SequenceVariable.make(Sequences.make(Integer.class, 1, 2, 3));
        final SequenceLocation<Integer> b = SequenceVariable.make(Integer.class, false,
                                                                  new SequenceBindingExpression<Integer>() {
                                                                      public Sequence<Integer> computeValue() {
                                                                          return v.getAsSequence();
                                                                      }
                                                                  }, v);
        HistorySequenceListener<Integer> vh = new HistorySequenceListener<Integer>();
        HistorySequenceListener<Integer> bh = new HistorySequenceListener<Integer>();
        v.addChangeListener(vh);
        b.addChangeListener(bh);

        assertEquals(v, 1, 2, 3);
        assertEquals(b, 1, 2, 3);
        assertEquals(vh);
        assertEqualsAndClear(bh);

        v.setAsSequence(Sequences.make(Integer.class, 1, 2));
        assertEquals(v, 1, 2);
        assertEquals(b, 1, 2);
        assertEqualsAndClear(vh, "d-2-3", "d-1-2", "d-0-1", "i-0-1", "i-1-2");
        assertEqualsAndClear(bh, "d-2-3", "d-1-2", "d-0-1", "i-0-1", "i-1-2");
    }

    public void testUpcast() {
        final SequenceLocation<Integer> iloc = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Number> nloc = SequenceVariable.make(Sequences.upcast(Number.class, iloc.getAsSequence()));
        final SequenceLocation<Object> asObjects = SequenceVariable.make(Sequences.upcast(Object.class, iloc.getAsSequence()));
        assertEquals(nloc.getAsSequence(), 1, 2, 3);
        assertEquals(asObjects.getAsSequence(), 1, 2, 3);

        assertThrows(ClassCastException.class, new VoidCallable() {
            public void call() throws Exception {
                Sequence<Map> asMaps = Sequences.upcast(Map.class, (Sequence) asObjects.getAsSequence());
            }
        });
    }

    public void testBoundConcat() {
        SequenceLocation<Integer> a = SequenceVariable.make(Sequences.range(1, 2));
        SequenceLocation<Integer> b = SequenceVariable.make(Sequences.range(3, 4));
        BoundCompositeSequence<Integer> c = new BoundCompositeSequence<Integer>(Integer.class, a, b);
        HistorySequenceListener<Integer> hl = new HistorySequenceListener<Integer>();
        c.addChangeListener(hl);

        assertEquals(c, 1, 2, 3, 4);
        assertEqualsAndClear(hl, "i-0-1", "i-1-2", "i-2-3", "i-3-4");
        a.set(0, 0);
        assertEquals(c, 0, 2, 3, 4);
        assertEqualsAndClear(hl, "r-0-1-0");
        b.set(1, 5);
        assertEquals(c, 0, 2, 3, 5);
        assertEqualsAndClear(hl, "r-3-4-5");
        c.validate();

        a.insert(9);
        assertEquals(c, 0, 2, 9, 3, 5);
        assertEqualsAndClear(hl, "i-2-9");
        b.insert(19);
        assertEquals(c, 0, 2, 9, 3, 5, 19);
        assertEqualsAndClear(hl, "i-5-19");
        b.insertFirst(100);
        assertEquals(c, 0, 2, 9, 100, 3, 5, 19);
        assertEqualsAndClear(hl, "i-3-100");
        c.validate();

        a.delete(0);
        assertEquals(c, 2, 9, 100, 3, 5, 19);
        assertEqualsAndClear(hl, "d-0-0");
        b.delete(0);
        assertEquals(c, 2, 9, 3, 5, 19);
        assertEqualsAndClear(hl, "d-2-100");
        c.validate();

        a.setAsSequence(Sequences.range(1, 2));
        assertEquals(c, 1, 2, 3, 5, 19);
        assertEqualsAndClear(hl, "d-1-9", "d-0-2", "i-0-1", "i-1-2");
        c.validate();

        b.setAsSequence(Sequences.range(3, 4));
        assertEquals(c, 1, 2, 3, 4);
        assertEqualsAndClear(hl, "d-4-19", "d-3-5", "d-2-3", "i-2-3", "i-3-4");
        c.validate();

        a.deleteAll();
        assertEquals(c, 3, 4);
        assertEqualsAndClear(hl, "d-1-2", "d-0-1");
        c.validate();

        b.deleteAll();
        assertEquals(0, c.getAsSequence().size());
        assertEqualsAndClear(hl, "d-1-4", "d-0-3");
        c.validate();

        a.setAsSequence(Sequences.range(1, 2));
        assertEquals(c, 1, 2);
        assertEqualsAndClear(hl, "i-0-1", "i-1-2");
        c.validate();

        b.setAsSequence(Sequences.range(3, 4));
        assertEquals(c, 1, 2, 3, 4);
        assertEqualsAndClear(hl, "i-2-3", "i-3-4");
        c.validate();

        SequenceLocation<Integer> d = SequenceVariable.make(Sequences.range(9, 10));
        c.replaceSlice(0, 0, new SequenceLocation[] { d });
        assertEquals(c, 9, 10, 3, 4);
        assertEqualsAndClear(hl, "d-1-2", "d-0-1", "i-0-9", "i-1-10");
        a.deleteAll();
        assertEquals(c, 9, 10, 3, 4);
        assertEqualsAndClear(hl);
        c.validate();

        d.insert(11);
        assertEquals(c, 9, 10, 11, 3, 4);
        assertEqualsAndClear(hl, "i-2-11");
        c.validate();

        c.replaceSlice(2, 1, new SequenceLocation[] { d });
        assertEquals(c, 9, 10, 11, 3, 4, 9, 10, 11);
        assertEqualsAndClear(hl, "i-5-9", "i-6-10", "i-7-11");
        c.validate();

        d.delete(2);
        assertEquals(c, 9, 10, 3, 4, 9, 10);
        assertEqualsAndClear(hl, "d-2-11", "d-6-11");
        c.validate();

        b.insert(5);
        assertEquals(c, 9, 10, 3, 4, 5, 9, 10);
        assertEqualsAndClear(hl, "i-4-5");
        c.validate();

        c.replaceSlice(0, 0, new SequenceLocation[] { });
        assertEquals(c, 3, 4, 5, 9, 10);
        assertEqualsAndClear(hl, "d-1-10", "d-0-9");
        c.validate();

        b.insert(6);
        assertEquals(c, 3, 4, 5, 6, 9, 10);
        assertEqualsAndClear(hl, "i-3-6");
        c.validate();
    }

    public void testBoundReverse() {
        SequenceLocation<Integer> a = SequenceVariable.make(Sequences.make(Integer.class, 1, 2, 3));
        SequenceLocation<Integer> r = BoundSequences.reverse(a);
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        r.addChangeListener(hl);

        assertEquals(r, 3, 2, 1);
        assertEqualsAndClear(hl, "[0, -1] => [ 3, 2, 1 ]");

        a.insert(4);
        assertEquals(a, 1, 2, 3, 4);
        assertEquals(r, 4, 3, 2, 1);
        assertEqualsAndClear(hl, "[0, -1] => [ 4 ]");

        a.insertFirst(0);
        assertEquals(a, 0, 1, 2, 3, 4);
        assertEquals(r, 4, 3, 2, 1, 0);
        assertEqualsAndClear(hl, "[4, 3] => [ 0 ]");

        a.delete(0);
        assertEquals(a, 1, 2, 3, 4);
        assertEquals(r, 4, 3, 2, 1);
        assertEqualsAndClear(hl, "[4, 4] => [ ]");

        a.delete(3);
        assertEquals(a, 1, 2, 3);
        assertEquals(r, 3, 2, 1);
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        a.set(0, 2);
        assertEquals(a, 2, 2, 3);
        assertEquals(r, 3, 2, 2);
        assertEqualsAndClear(hl, "[2, 2] => [ 2 ]");

        a.set(2, 2);
        assertEquals(a, 2, 2, 2);
        assertEquals(r, 2, 2, 2);
        assertEqualsAndClear(hl, "[0, 0] => [ 2 ]");

        a.setAsSequence(Sequences.range(1, 2));
        assertEquals(a, 1, 2);
        assertEquals(r, 2, 1);
        assertEqualsAndClear(hl, "[0, 2] => [ 2, 1 ]");

    }

    public void testChainedSequenceBind() {
        final SequenceLocation<Integer> a = SequenceVariable.make(Sequences.make(Integer.class, 1, 2, 3));
        final SequenceLocation<Integer> b = SequenceVariable.make(Integer.class, false,
                                                                  new SequenceBindingExpression<Integer>() {
                                                                      public Sequence<Integer> computeValue() {
                                                                          return a.getAsSequence();
                                                                      }
                                                                  }, a);
        IntLocation i = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return b.getAsSequence().size();
            }
        }, b);
        IntLocation j = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsSequence().size();
            }
        }, a);

        assertEquals(b, 1, 2, 3);
        assertEquals(3, i);
        a.insert(4);
        assertEquals(b, 1, 2, 3, 4);
        assertEquals(4, i);
        assertEquals(4, j);
        a.setAsSequence(Sequences.make(Integer.class, 1));
        assertEquals(b, 1);
        assertEquals(1, i);
        assertEquals(1, j);
    }

    public void testBoundSingleton() {
        IntLocation i = IntVariable.make(0);
        ObjectLocation<Integer> o = Locations.asObjectLocation(i);
        SequenceLocation<Integer> s = BoundSequences.singleton(Integer.class, o);
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        s.addChangeListener(hl);

        assertEquals(s, 0);
        assertEqualsAndClear(hl, "[0, -1] => [ 0 ]");

        i.setAsInt(1);
        assertEquals(s, 1);
        assertEqualsAndClear(hl, "[0, 0] => [ 1 ]");

        o = ObjectVariable.make((Integer) null);
        s = BoundSequences.singleton(Integer.class, o);
        s.addChangeListener(hl);
        assertEquals(s);

        o.set(1);
        assertEquals(s, 1);
        assertEqualsAndClear(hl, "[0, -1] => [ 1 ]");

        o.set(2);
        assertEquals(s, 2);
        assertEqualsAndClear(hl, "[0, 0] => [ 2 ]");

        o.set(null);
        assertEquals(s);
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        o.set(3);
        assertEquals(s, 3);
        assertEqualsAndClear(hl, "[0, -1] => [ 3 ]");
    }

    public void testSliceTriggers() {
        SequenceLocation<Integer> a = SequenceVariable.make(Sequences.make(Integer.class, 1, 2, 3));
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        a.addChangeListener(hl);
        a.setAsSequence(Sequences.range(1, 2));
        assertEquals(a, 1, 2);
        assertEqualsAndClear(hl, "[0, 2] => [ 1, 2 ]");

        a.setAsSequence(Sequences.range(2, 3));
        assertEquals(a, 2, 3);
        assertEqualsAndClear(hl, "[0, 1] => [ 2, 3 ]");

        a.deleteAll();
        assertEquals(a);
        assertEqualsAndClear(hl, "[0, 1] => [ ]");

        a.insert(1);
        assertEquals(a, 1);
        assertEqualsAndClear(hl, "[0, -1] => [ 1 ]");

        a.insertFirst(0);
        assertEquals(a, 0, 1);
        assertEqualsAndClear(hl, "[0, -1] => [ 0 ]");

        a.insert(2);
        assertEquals(a, 0, 1, 2);
        assertEqualsAndClear(hl, "[2, 1] => [ 2 ]");

        a.set(1, 3);
        assertEquals(a, 0, 3, 2);
        assertEqualsAndClear(hl, "[1, 1] => [ 3 ]");

        a.delete(0);
        assertEquals(a, 3, 2);
        assertEqualsAndClear(hl, "[0, 0] => [ ]");
    }

    public void testBoundSequenceBuilder() {
        BoundSequenceBuilder<Integer> sb = new BoundSequenceBuilder<Integer>(Integer.class);
        IntLocation a = IntVariable.make(1);
        final SequenceLocation<Integer> b = SequenceVariable.make(Sequences.make(Integer.class, 4, 5, 6));
        IntLocation c = IntVariable.make(10);
        SequenceLocation<Integer> d = SequenceVariable.make(Integer.class, new SequenceBindingExpression<Integer>() {
            public Sequence<Integer> computeValue() {
                return Sequences.make(Integer.class, b.getAsSequence().size());
            }
        }, b);

        sb.add(a);
        sb.add(b);
        sb.add(c);
        sb.add(d);
        SequenceLocation<Integer> derived = sb.toSequence();
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 1, 4, 5, 6, 10, 3);
        assertEqualsAndClear(hl, "[0, -1] => [ 1, 4, 5, 6, 10, 3 ]");

        b.insert(7);
        assertEquals(derived, 1, 4, 5, 6, 7, 10, 4);
        assertEqualsAndClear(hl, "[5, 5] => [ 4 ]", "[4, 3] => [ 7 ]");

        b.delete(0);
        assertEquals(derived, 1, 5, 6, 7, 10, 3);
        assertEqualsAndClear(hl, "[6, 6] => [ 3 ]", "[1, 1] => [ ]");

        b.deleteAll();
        assertEquals(derived, 1, 10, 0);
        assertEqualsAndClear(hl, "[5, 5] => [ 0 ]", "[1, 3] => [ ]");
    }
}

