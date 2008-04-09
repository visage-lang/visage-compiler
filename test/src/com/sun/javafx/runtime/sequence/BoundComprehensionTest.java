package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.location.*;

/**
 * BoundComprehensionTest
 *
 * @author Brian Goetz
 */
public class BoundComprehensionTest extends JavaFXTestCase {

    public void testSimpleComprehension() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundOperators.makeSimpleBoundComprehension(Integer.class, base, false,
                                                                                              new BoundOperators.IntSimpleBoundComprehensionCallback<Integer>() {
                                                                                                  public Integer computeElement(int element, int index) {
                                                                                                      return element * 2;
                                                                                                  }
                                                                                              });
        IntLocation len = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 2, 4, 6);
        assertEqualsAndClear(hl);
        assertEquals(3, len.getAsInt());

        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[3, 2] => [ 8 ]");

        base.delete(0);
        assertEquals(derived, 4, 6, 8);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        base.set(0, 0);
        assertEquals(derived, 0, 6, 8);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertFirst(-1);
        assertEquals(derived, -2, 0, 6, 8);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[0, -1] => [ -2 ]");
    }

    public void testSimpleChainedComprehension() {
        BoundOperators.IntSimpleBoundComprehensionCallback<Integer> timesTwo = new BoundOperators.IntSimpleBoundComprehensionCallback<Integer>() {
            public Integer computeElement(int element, int index) {
                return element * 2;
            }
        };
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        SequenceLocation<Integer> middle = BoundOperators.makeSimpleBoundComprehension(Integer.class, base, false, timesTwo);
        final SequenceLocation<Integer> derived = BoundOperators.makeSimpleBoundComprehension(Integer.class, middle, false, timesTwo);
        IntLocation len = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 4, 8, 12);
        assertEqualsAndClear(hl);
        assertEquals(3, len.getAsInt());

        base.insert(4);
        assertEquals(derived, 4, 8, 12, 16);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[3, 2] => [ 16 ]");

        base.delete(0);
        assertEquals(derived, 8, 12, 16);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        base.set(0, 0);
        assertEquals(derived, 0, 12, 16);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertFirst(-1);
        assertEquals(derived, -4, 0, 12, 16);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[0, -1] => [ -4 ]");
    }

    public void testSimpleIndex() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundOperators.makeSimpleBoundComprehension(Integer.class, base, true,
                                                                                              new BoundOperators.IntSimpleBoundComprehensionCallback<Integer>() {
                                                                                                  public Integer computeElement(int element, int index) {
                                                                                                      return index * 10 + element;
                                                                                                  }
                                                                                              });

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 1, 12, 23);
        assertEqualsAndClear(hl, "[0, -1] => [ 1, 12, 23 ]");

        base.insert(4);
        assertEquals(derived, 1, 12, 23, 34);
        assertEqualsAndClear(hl, "[3, 2] => [ 34 ]");

        base.delete(0);
        assertEquals(derived, 2, 13, 24);
        assertEqualsAndClear(hl, "[0, 3] => [ 2, 13, 24 ]");

        base.insertFirst(-1);
        assertEquals(derived, -1, 12, 23, 34);
        assertEqualsAndClear(hl, "[0, 2] => [ -1, 12, 23, 34 ]");

        base.set(0, 0); // 0, 2, 3, 4
        assertEquals(derived, 0, 12, 23, 34);
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertAfter(0, 1); // 0, 2, 0, 3, 4
        assertEquals(derived, 0, 12, 20, 33, 44);
        assertEqualsAndClear(hl, "[2, 3] => [ 20, 33, 44 ]");
    }

    public void testBoundComprehension() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundOperators.makeBoundComprehension(Integer.class, base, false,
                                                                                        new BoundOperators.IntBoundComprehensionCallback<Integer>() {
                                                                                            public SequenceLocation<Integer> getMappedElement(final IntLocation xLocation, IntLocation xIndexLocation) {
                                                                                                return BoundSequences.singleton(Integer.class, BoundOperators.times_ii(xLocation, IntConstant.make(2)));
                                                                                            }
                                                                                        });

        IntVariable len = IntVariable.make(false, new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 2, 4, 6);
        // @@@ jfxc-1035: if we build the loc as the compiler would, we'd expect the following:
        // assertEqualsAndClear(hl, "[0, -1] => [ 2, 4, 6 ]");
        assertEqualsAndClear(hl);
        assertEquals(3, len.getAsInt());

        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[3, 2] => [ 8 ]");

        base.delete(0);
        assertEquals(derived, 4, 6, 8);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        base.set(0, 0);
        assertEquals(derived, 0, 6, 8);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertFirst(-1);
        assertEquals(derived, -2, 0, 6, 8);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[0, -1] => [ -2 ]");
    }

    public void testBindingWrapper() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundOperators.makeSimpleBoundComprehension(Integer.class, base, false,
                                                                                              new BoundOperators.IntSimpleBoundComprehensionCallback<Integer>() {
                                                                                                  public Integer computeElement(int element, int index) {
                                                                                                      return element * 2;
                                                                                                  }
                                                                                              });
        SequenceVariable<Integer> moo = SequenceVariable.make(Integer.class);
        moo.bind(derived);
        
        assertEquals(derived, 2, 4, 6);
        assertEquals(moo, 2, 4, 6);
        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(moo, 2, 4, 6, 8);
    }

    public void testBoundIndex() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        SequenceLocation<Integer> derived = BoundOperators.makeBoundComprehension(Integer.class, base, true,
                                                                                  new BoundOperators.IntBoundComprehensionCallback<Integer>() {
                                                                                      public SequenceLocation<Integer> getMappedElement(final IntLocation xLocation, final IntLocation xIndexLocation) {
                                                                                          return SequenceVariable.make(Integer.class,
                                                                                                                       new SequenceBindingExpression<Integer>() {
                                                                                                                           public Sequence<? extends Integer> computeValue() {
                                                                                                                               return Sequences.make(Integer.class, xIndexLocation.get() * 2);
                                                                                                                           }
                                                                                                                       }, xIndexLocation);
                                                                                      }
                                                                                  });

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 0, 2, 4);
        assertEqualsAndClear(hl, "[0, -1] => [ 0, 2, 4 ]");

        base.insert(4);
        assertEquals(derived, 0, 2, 4, 6);
        assertEqualsAndClear(hl, "[3, 2] => [ 6 ]");

        base.delete(0);
        assertEquals(derived, 0, 2, 4);
        assertEqualsAndClear(hl, "[0, 0] => [ ]", "[0, 0] => [ 0 ]", "[1, 1] => [ 2 ]", "[2, 2] => [ 4 ]");

        base.insertFirst(-1);
        assertEquals(derived, 0, 2, 4, 6);
        assertEqualsAndClear(hl, "[0, -1] => [ 0 ]", "[1, 1] => [ 2 ]", "[2, 2] => [ 4 ]", "[3, 3] => [ 6 ]");

        base.set(0, 0);
        assertEquals(derived, 0, 2, 4, 6);
        assertEqualsAndClear(hl);

        base.insertAfter(0, 1);
        assertEquals(derived, 0, 2, 4, 6, 8);
        assertEqualsAndClear(hl, "[2, 1] => [ 4 ]", "[3, 3] => [ 6 ]", "[4, 4] => [ 8 ]");

        base.set(Sequences.range(1, 3));
        derived = BoundOperators.makeBoundComprehension(Integer.class, base, true,
                                                        new BoundOperators.IntBoundComprehensionCallback<Integer>() {
                                                            public SequenceLocation<Integer> getMappedElement(final IntLocation xLocation, final IntLocation xIndexLocation) {
                                                                return SequenceVariable.make(Integer.class,
                                                                                             new SequenceBindingExpression<Integer>() {
                                                                                                 public Sequence<? extends Integer> computeValue() {
                                                                                                     return Sequences.make(Integer.class, xIndexLocation.get() * 10 + xLocation.get());
                                                                                                 }
                                                                                             }, xIndexLocation, xLocation);
                                                            }
                                                        });

        hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 1, 12, 23);
        assertEqualsAndClear(hl, "[0, -1] => [ 1, 12, 23 ]");

        base.insert(4);
        assertEquals(derived, 1, 12, 23, 34);
        assertEqualsAndClear(hl, "[3, 2] => [ 34 ]");

        base.delete(0);
        assertEquals(derived, 2, 13, 24);
        assertEqualsAndClear(hl, "[0, 0] => [ ]", "[0, 0] => [ 2 ]", "[1, 1] => [ 13 ]", "[2, 2] => [ 24 ]");

        base.insertFirst(-1);
        assertEquals(derived, -1, 12, 23, 34);
        assertEqualsAndClear(hl, "[0, -1] => [ -1 ]", "[1, 1] => [ 12 ]", "[2, 2] => [ 23 ]", "[3, 3] => [ 34 ]");

        base.set(0, 0); // 0, 2, 3, 4
        assertEquals(derived, 0, 12, 23, 34);
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.set(0, 0); // 0, 2, 3, 4
        assertEquals(derived, 0, 12, 23, 34);
        assertEqualsAndClear(hl);

        base.insertAfter(0, 1); // 0, 2, 0, 3, 4
        assertEquals(derived, 0, 12, 20, 33, 44);
        assertEqualsAndClear(hl, "[2, 1] => [ 20 ]", "[3, 3] => [ 33 ]", "[4, 4] => [ 44 ]");

        base.delete(4);
        assertEquals(derived, 0, 12, 20, 33);
        assertEqualsAndClear(hl, "[4, 4] => [ ]");
    }

    public void testNestedComprehension() {
        SequenceLocation<Integer> xs = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> ys = SequenceVariable.make(Sequences.make(Integer.class, 10, 20, 30));
        SequenceLocation<Integer> comp = BoundOperators.makeBoundComprehension(Integer.class, xs, false,
                                                                               new BoundOperators.IntBoundComprehensionCallback<Integer>() {
                                                                                   public SequenceLocation<Integer> getMappedElement(final IntLocation xLocation, final IntLocation xIndexLocation) {
                                                                                       return BoundOperators.makeBoundComprehension(Integer.class, ys, false,
                                                                                                                                    new BoundOperators.IntBoundComprehensionCallback<Integer>() {
                                                                                                                                        public SequenceLocation<Integer> getMappedElement(final IntLocation yLocation, IntLocation yIndexLocation) {
                                                                                                                                            return SequenceVariable.make(Integer.class,
                                                                                                                                                                         new SequenceBindingExpression<Integer>() {
                                                                                                                                                                             public Sequence<? extends Integer> computeValue() {
                                                                                                                                                                                 return Sequences.make(Integer.class, xLocation.get() + yLocation.get());
                                                                                                                                                                             }
                                                                                                                                                                         }, xLocation, yLocation);
                                                                                                                                        }
                                                                                                                                    });
                                                                                   }
                                                                               });

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        comp.addChangeListener(hl);

        assertEquals(comp, 11, 21, 31, 12, 22, 32, 13, 23, 33);
        assertEqualsAndClear(hl, "[0, -1] => [ 11, 21, 31, 12, 22, 32, 13, 23, 33 ]");

        xs.set(0, 0);
        assertEquals(comp, 10, 20, 30, 12, 22, 32, 13, 23, 33);
        assertEqualsAndClear(hl, "[0, 0] => [ 10 ]", "[1, 1] => [ 20 ]", "[2, 2] => [ 30 ]");

        ys.set(0, 0);
        assertEquals(comp, 0, 20, 30, 2, 22, 32, 3, 23, 33);
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]", "[3, 3] => [ 2 ]", "[6, 6] => [ 3 ]");

        xs.delete(0);
        assertEquals(xs, 2, 3);
        assertEquals(comp, 2, 22, 32, 3, 23, 33);
        assertEqualsAndClear(hl, "[0, 2] => [ ]");

        ys.delete(0);
        assertEquals(ys, 20, 30);
        assertEquals(comp, 22, 32, 23, 33);
        assertEqualsAndClear(hl, "[0, 0] => [ ]", "[2, 2] => [ ]");

        xs.insert(4);
        assertEquals(comp, 22, 32, 23, 33, 24, 34);
        assertEqualsAndClear(hl, "[4, 3] => [ 24, 34 ]");

        ys.insert(40);
        assertEquals(comp, 22, 32, 42, 23, 33, 43, 24, 34, 44);
        assertEqualsAndClear(hl, "[2, 1] => [ 42 ]", "[5, 4] => [ 43 ]", "[8, 7] => [ 44 ]");
    }

    public void testDifferentTypes() {
        SequenceLocation<Integer> xs = SequenceVariable.make(Sequences.range(1, 5));
        SequenceLocation<String> derived = BoundOperators.makeBoundComprehension(String.class, xs, false,
                                                                                 new BoundOperators.IntBoundComprehensionCallback<String>() {
                                                                                     public SequenceLocation<String> getMappedElement(final IntLocation xLocation, final IntLocation unused) {
                                                                                         return SequenceVariable.make(String.class,
                                                                                                                      new SequenceBindingExpression<String>() {
                                                                                                                          public Sequence<String> computeValue() {
                                                                                                                              if (xLocation.get() % 2 == 0)
                                                                                                                                  return Sequences.make(String.class, Integer.toString(xLocation.get()), "foo");
                                                                                                                              else
                                                                                                                                  return Sequences.emptySequence(String.class);
                                                                                                                          }
                                                                                                                      }, xLocation);
                                                                                     }
                                                                                 });

        assertEquals(derived, "2", "foo", "4", "foo");
        xs.insert(6);
        assertEquals(derived, "2", "foo", "4", "foo", "6", "foo");

    }
}
