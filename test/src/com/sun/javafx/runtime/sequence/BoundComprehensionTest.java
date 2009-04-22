package com.sun.javafx.runtime.sequence;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.*;
import framework.FXObjectFactory;

/**
 * BoundComprehensionTest
 *
 * @author Brian Goetz
 */
public class BoundComprehensionTest extends JavaFXTestCase {

    static final boolean NOT_LAZY = false;

    public void testSimpleComprehension() {
        SequenceLocation<Integer> base = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundSequences.makeSimpleBoundComprehension(NOT_LAZY, TypeInfo.Integer, base, false,
                                                                                              new BoundSequences.SimpleBoundComprehensionCallback<Integer, Integer>() {
                                                                                                  public Integer computeElement$(Integer element, int index) {
                                                                                                      return element * 2;
                                                                                                  }
                                                                                              });
        IntLocation len = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addSequenceChangeListener(hl);

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
        BoundSequences.SimpleBoundComprehensionCallback<Integer, Integer> timesTwo = new BoundSequences.SimpleBoundComprehensionCallback<Integer, Integer>() {
            public Integer computeElement$(Integer element, int index) {
                return element * 2;
            }
        };
        SequenceLocation<Integer> base = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        SequenceLocation<Integer> middle = BoundSequences.makeSimpleBoundComprehension(NOT_LAZY, TypeInfo.Integer, base, false, timesTwo);
        final SequenceLocation<Integer> derived = BoundSequences.makeSimpleBoundComprehension(NOT_LAZY, TypeInfo.Integer, middle, false, timesTwo);
        IntLocation len = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addSequenceChangeListener(hl);

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
        SequenceLocation<Integer> base = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundSequences.makeSimpleBoundComprehension(NOT_LAZY, TypeInfo.Integer, base, true,
                                                                                              new BoundSequences.SimpleBoundComprehensionCallback<Integer, Integer>() {
                                                                                                  public Integer computeElement$(Integer element, int index) {
                                                                                                      return index * 10 + element;
                                                                                                  }
                                                                                              });

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addSequenceChangeListener(hl);

        assertEquals(derived, 1, 12, 23);
        assertEquals(hl);
        // was: assertEqualsAndClear(hl, "[0, -1] => [ 1, 12, 23 ]");

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
        SequenceLocation<Integer> base = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundSequences.makeBoundComprehension(NOT_LAZY, TypeInfo.Integer, TypeInfo.Integer, base, false,
                                                                                        new BoundSequences.BoundComprehensionCallback<Integer, Integer, IntLocation>() {
                                                                                            public SequenceLocation<Integer> computeElements$(final IntLocation xLocation, IntLocation xIndexLocation) {
                                                                                                return BoundSequences.singleton(NOT_LAZY, TypeInfo.Integer, op_int(false, xLocation, IntConstant.make(2), Operator.TIMES));
                                                                                            }
        });

        IntVariable len = IntVariable.make(false, new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addSequenceChangeListener(hl);

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
//        assertEquals(derived, 4, 6, 8);  //TODO: why broken?
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
        SequenceLocation<Integer> base = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = BoundSequences.makeSimpleBoundComprehension(NOT_LAZY, TypeInfo.Integer, base, false,
                                                                                              new BoundSequences.SimpleBoundComprehensionCallback<Integer, Integer>() {
                                                                                                  public Integer computeElement$(Integer element, int index) {
                                                                                                      return element * 2;
                                                                                                  }
                                                                                              });
        SequenceVariable<Integer> moo = SequenceVariable.make(TypeInfo.Integer);
        moo.bind(false, derived);

        assertEquals(derived, 2, 4, 6);
        assertEquals(moo, 2, 4, 6);
        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(moo, 2, 4, 6, 8);
    }

    public void testBoundIndex() {
        SequenceLocation<Integer> base = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        SequenceLocation<Integer> derived = BoundSequences.makeBoundComprehension(NOT_LAZY, TypeInfo.Integer, TypeInfo.Integer, base, true,
                                                                                  new BoundSequences.BoundComprehensionCallback<Integer, Integer, IntLocation>() {
                                                                                      public SequenceLocation<Integer> computeElements$(final IntLocation xLocation, final IntLocation xIndexLocation) {
                                                                                          return SequenceVariable.make(TypeInfo.Integer,
                                                                                                                       new SequenceBindingExpression<Integer>() {
                                                                                                                           public Sequence<Integer> computeValue() {
                                                                                                                               return Sequences.make(TypeInfo.Integer, xIndexLocation.get() * 2);
                                                                                                                           }
                                                                                                                       }, xIndexLocation);
                                                                                      }
                                                                                  });

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addSequenceChangeListener(hl);

        assertEquals(derived, 0, 2, 4);
        assertEquals(hl);
        // was: assertEqualsAndClear(hl, "[0, -1] => [ 0, 2, 4 ]");

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
        derived = BoundSequences.makeBoundComprehension(NOT_LAZY, TypeInfo.Integer, TypeInfo.Integer, base, true,
                                                        new BoundSequences.BoundComprehensionCallback<Integer, Integer, IntLocation>() {
                                                            public SequenceLocation<Integer> computeElements$(final IntLocation xLocation, final IntLocation xIndexLocation) {
                                                                return SequenceVariable.make(TypeInfo.Integer,
                                                                                             new SequenceBindingExpression<Integer>() {
                                                                                                 public Sequence<Integer> computeValue() {
                                                                                                     return Sequences.make(TypeInfo.Integer, xIndexLocation.get() * 10 + xLocation.get());
                                                                                                 }
                                                                                             }, xIndexLocation, xLocation);
                                                            }
                                                        });

        hl = new HistoryReplaceListener<Integer>();
        derived.addSequenceChangeListener(hl);

        assertEquals(derived, 1, 12, 23);
        assertEquals(hl);
        // was: assertEqualsAndClear(hl, "[0, -1] => [ 1, 12, 23 ]");

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
        SequenceLocation<Integer> xs = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        final SequenceLocation<Integer> ys = SequenceVariable.make(TypeInfo.Integer, Sequences.make(TypeInfo.Integer, 10, 20, 30));
        SequenceLocation<Integer> comp = BoundSequences.makeBoundComprehension(NOT_LAZY, TypeInfo.Integer, TypeInfo.Integer, xs, false,
                                                                               new BoundSequences.BoundComprehensionCallback<Integer, Integer, IntLocation>() {
                                                                                   public SequenceLocation<Integer> computeElements$(final IntLocation xLocation, final IntLocation xIndexLocation) {
                                                                                       return BoundSequences.makeBoundComprehension(NOT_LAZY, TypeInfo.Integer, TypeInfo.Integer, ys, false,
                                                                                                                                    new BoundSequences.BoundComprehensionCallback<Integer, Integer, IntLocation>() {
                                                                                                                                        public SequenceLocation<Integer> computeElements$(final IntLocation yLocation, IntLocation yIndexLocation) {
                                                                                                                                            return SequenceVariable.make(TypeInfo.Integer,
                                                                                                                                                                         new SequenceBindingExpression<Integer>() {
                                                                                                                                                                             public Sequence<Integer> computeValue() {
                                                                                                                                                                                 return Sequences.make(TypeInfo.Integer, xLocation.get() + yLocation.get());
                                                                                                                                                                             }
                                                                                                                                                                         }, xLocation, yLocation);
                                                                                                                                        }
                                                                                                                                    });
                                                                                   }
                                                                               });

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        comp.addSequenceChangeListener(hl);

        assertEquals(comp, 11, 21, 31, 12, 22, 32, 13, 23, 33);
        assertEquals(hl);
        // was: assertEqualsAndClear(hl, "[0, -1] => [ 11, 21, 31, 12, 22, 32, 13, 23, 33 ]");

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
        SequenceLocation<Integer> xs = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 5));
        SequenceLocation<String> derived = BoundSequences.makeBoundComprehension(NOT_LAZY, TypeInfo.String, TypeInfo.Integer, xs, false,
                                                                                 new BoundSequences.BoundComprehensionCallback<Integer, String, IntLocation>() {
                                                                                     public SequenceLocation<String> computeElements$(final IntLocation xLocation, final IntLocation unused) {
                                                                                         return SequenceVariable.make(TypeInfo.String,
                                                                                                                      new SequenceBindingExpression<String>() {
                                                                                                                          public Sequence<String> computeValue() {
                                                                                                                              if (xLocation.get() % 2 == 0)
                                                                                                                                  return Sequences.make(TypeInfo.String, Integer.toString(xLocation.get()), "foo");
                                                                                                                              else
                                                                                                                                  return TypeInfo.String.emptySequence;
                                                                                                                          }
                                                                                                                      }, xLocation);
                                                                                     }
                                                                                 });

        assertEquals(derived, "2", "foo", "4", "foo");
        xs.insert(6);
        assertEquals(derived, "2", "foo", "4", "foo", "6", "foo");

    }

    private interface Foo extends FXObject {
        public SequenceLocation<String> get$x();
    }

    public void testJfxc1067a() {
        final List<String> list = new ArrayList<String>();

        final FXObjectFactory<Foo> fooFactory = new FXObjectFactory<Foo>(Foo.class, new String[]{"x"}) {
            @Override
            public void postInit(Foo receiver) {
                list.add(receiver.get$x().getAsSequence().get(0));
            }
        };

        SequenceLocation<String> stringSequence = SequenceVariable.make(TypeInfo.String, Sequences.make(TypeInfo.String, "a", "b"));
        final ObjectLocation<String> firstElement = BoundSequences.element(NOT_LAZY, stringSequence, IntVariable.make(0));

        ObjectVariable.make(false, new ObjectBindingExpression<Foo>() {
            private SequenceLocation<String> xform$attr;

            @Override
            protected DependencySource[] getStaticDependents() {
                return new Location[]{xform$attr};
            }

            public Foo computeValue() {
                final Foo foo = fooFactory.make();
                foo.get$x().setAsSequenceFromLiteral(xform$attr.getAsSequence());
                foo.initialize$();
                return foo;
            }

            {
                BoundSequenceBuilder<String> jfx$$1sb = new BoundSequenceBuilder<String>(NOT_LAZY, TypeInfo.String);
                jfx$$1sb.add(firstElement);
                xform$attr = jfx$$1sb.toSequence();
                // @@@ Sequence loc is built in initially-lazy mode !
            }
        });

        assertEquals("[a]", list.toString());
    }

    public void testJfxc1067() {
        final List<String> list = new ArrayList<String>();

        final FXObjectFactory<Foo> fooFactory = new FXObjectFactory<Foo>(Foo.class, new String[]{"x"}) {
            @Override
            public void postInit(Foo receiver) {
                list.add(receiver.get$x().getAsSequence().get(0));
            }
        };

        SequenceVariable<Foo> w = SequenceVariable.make(TypeInfo.<Foo>getTypeInfo());
        SequenceLocation<String> stringSequence = SequenceVariable.make(TypeInfo.String, Sequences.make(TypeInfo.String, "a", "b"));
        w.bind(false, new AbstractBoundComprehension<String, ObjectLocation<String>, Foo>(NOT_LAZY, TypeInfo.<Foo>getTypeInfo(), TypeInfo.String, stringSequence, false) {
            protected SequenceLocation<Foo> computeElements$(final ObjectLocation<String> textStr, final IntLocation $indexof$textStr) {
                return BoundSequences.singleton(NOT_LAZY, TypeInfo.<Foo>getTypeInfo(),
                                                ObjectVariable.<Foo>make(false, new ObjectBindingExpression<Foo>() {
                                                    private SequenceLocation<String> xform$attr;

                                                    @Override
                                                    protected DependencySource[] getStaticDependents() {
                                                        return new Location[]{xform$attr};
                                                    }

                                                    public Foo computeValue() {
                                                        final Foo foo = fooFactory.make();
                                                        foo.get$x().setAsSequenceFromLiteral(xform$attr.getAsSequence());
                                                        foo.initialize$();
                                                        return foo;
                                                    }

                                                    {
                                                        BoundSequenceBuilder<String> jfx$$1sb = new BoundSequenceBuilder<String>(NOT_LAZY, TypeInfo.String);
                                                        jfx$$1sb.add(textStr);
                                                        xform$attr = jfx$$1sb.toSequence();
                                                    }
                                                }));
            }
        });
        assertEquals(2, w.getAsSequence().size());
        assertEquals("[a, b]", list.toString());
    }

    public enum Operator {
        PLUS, MINUS, TIMES, DIVIDE, MODULO, NEGATE,
        CMP_EQ, CMP_NE,
        CMP_LT, CMP_LE, CMP_GT, CMP_GE,
        AND, OR, NOT
    }

    private static final int CASE_OP_INT = 0;
    private static final int CASE_OP_DOUBLE = 1;
    private static final int CASE_OP_FLOAT = 2;
    private static final int CASE_OP_LONG = 3;
    private static final int CASE_CMP_INT = 4;
    private static final int CASE_CMP_DOUBLE = 5;
    private static final int CASE_CMP_FLOAT = 6;
    private static final int CASE_CMP_LONG = 7;

    private static class NumericBindingExpression extends AbstractBindingExpression {
        private final Operator op;
        private final int arm;
        private final NumericLocation a, b;

        private NumericBindingExpression(Operator op, int arm, NumericLocation a, NumericLocation b) {
            this.op = op;
            this.arm = arm;
            this.a = a;
            this.b = b;
        }

        public void compute() {
            switch (arm) {
                case CASE_OP_INT:
                    switch (op) {
                        case PLUS: pushValue(a.getAsInt() + b.getAsInt()); break;
                        case MINUS: pushValue(a.getAsInt() - b.getAsInt()); break;
                        case TIMES: pushValue(a.getAsInt() * b.getAsInt()); break;
                        case DIVIDE: pushValue(a.getAsInt() / b.getAsInt()); break;
                        case MODULO: pushValue(a.getAsInt() % b.getAsInt()); break;
                        case NEGATE: pushValue(-a.getAsInt()); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                    break;

                case CASE_OP_DOUBLE:
                    switch (op) {
                        case PLUS: pushValue(a.getAsDouble() + b.getAsDouble()); break;
                        case MINUS:  pushValue(a.getAsDouble() - b.getAsDouble()); break;
                        case TIMES: pushValue(a.getAsDouble() * b.getAsDouble()); break;
                        case DIVIDE: pushValue(a.getAsDouble() / b.getAsDouble()); break;
                        case MODULO: pushValue(a.getAsDouble() % b.getAsDouble()); break;
                        case NEGATE: pushValue(-a.getAsDouble()); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                    break;

                case CASE_OP_FLOAT:
                    switch (op) {
                        case PLUS: pushValue((a.getAsFloat() + b.getAsFloat())); break;
                        case MINUS: pushValue((a.getAsFloat() - b.getAsFloat())); break;
                        case TIMES: pushValue((a.getAsFloat() * b.getAsFloat())); break;
                        case DIVIDE: pushValue((a.getAsFloat() / b.getAsFloat())); break;
                        case MODULO: pushValue((a.getAsFloat() % b.getAsFloat())); break;
                        case NEGATE: pushValue((-a.getAsFloat())); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                    break;

                case CASE_OP_LONG:
                    switch (op) {
                        case PLUS: pushValue((a.getAsLong() + b.getAsLong())); break;
                        case MINUS: pushValue((a.getAsLong() - b.getAsLong())); break;
                        case TIMES: pushValue((a.getAsLong() * b.getAsLong())); break;
                        case DIVIDE: pushValue((a.getAsLong() / b.getAsLong())); break;
                        case MODULO: pushValue((a.getAsLong() % b.getAsLong())); break;
                        case NEGATE: pushValue((-a.getAsLong())); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                    break;

                case CASE_CMP_INT:
                {
                    int left = a.getAsInt();
                    int right = b.getAsInt();
                    switch (op) {
                        case CMP_EQ: pushValue((left == right)); break;
                        case CMP_NE: pushValue((left != right)); break;
                        case CMP_LT: pushValue((left < right)); break;
                        case CMP_LE: pushValue((left <= right)); break;
                        case CMP_GT: pushValue((left > right)); break;
                        case CMP_GE: pushValue((left >= right)); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                }
                break;

                case CASE_CMP_DOUBLE:
                {
                    double left = a.getAsDouble();
                    double right = b.getAsDouble();
                    switch (op) {
                        case CMP_EQ: pushValue((left == right)); break;
                        case CMP_NE: pushValue((left != right)); break;
                        case CMP_LT: pushValue((left < right)); break;
                        case CMP_LE: pushValue((left <= right)); break;
                        case CMP_GT: pushValue((left > right)); break;
                        case CMP_GE: pushValue((left >= right)); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                }
                break;

                case CASE_CMP_FLOAT:
                {
                    float left = a.getAsFloat();
                    float right = b.getAsFloat();
                    switch (op) {
                        case CMP_EQ: pushValue((left == right)); break;
                        case CMP_NE: pushValue((left != right)); break;
                        case CMP_LT: pushValue((left < right)); break;
                        case CMP_LE: pushValue((left <= right)); break;
                        case CMP_GT: pushValue((left > right)); break;
                        case CMP_GE: pushValue((left >= right)); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                }
                break;

                case CASE_CMP_LONG:
                {
                    long left = a.getAsLong();
                    long right = b.getAsLong();
                    switch (op) {
                        case CMP_EQ: pushValue((left == right)); break;
                        case CMP_NE: pushValue((left != right)); break;
                        case CMP_LT: pushValue((left < right)); break;
                        case CMP_LE: pushValue((left <= right)); break;
                        case CMP_GT: pushValue((left > right)); break;
                        case CMP_GE: pushValue((left >= right)); break;
                        default: throw new UnsupportedOperationException(op.toString());
                    }
                }
                break;
            }
        }
    }

    public static IntLocation op_int(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return IntVariable.make(lazy, new NumericBindingExpression(op, CASE_OP_INT, a, b), a, b);
    }

    public static DoubleLocation op_double(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return DoubleVariable.make(lazy, new NumericBindingExpression(op, CASE_OP_DOUBLE, a, b), a, b);
    }

    public static FloatLocation op_float(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return FloatVariable.make(lazy, new NumericBindingExpression(op, CASE_OP_FLOAT, a, b), a, b);
    }

    public static LongLocation op_long(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return LongVariable.make(lazy, new NumericBindingExpression(op, CASE_OP_LONG, a, b), a, b);
    }

    public static BooleanLocation cmp_int(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new NumericBindingExpression(op, CASE_CMP_INT, a, b), a, b);
    }

    public static BooleanLocation cmp_long(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new NumericBindingExpression(op, CASE_CMP_LONG, a, b), a, b);
    }

    public static BooleanLocation cmp_float(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new NumericBindingExpression(op, CASE_CMP_FLOAT, a, b), a, b);
    }

    public static BooleanLocation cmp_double(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new NumericBindingExpression(op, CASE_CMP_DOUBLE, a, b), a, b);
    }


    public static BooleanLocation op_boolean(final boolean lazy, final BooleanLocation a, final BooleanLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new AbstractBindingExpression() {
            public void compute() {
                switch (op) {
                    case AND: pushValue((a.getAsBoolean() && b.getAsBoolean())); break;
                    case OR: pushValue((a.getAsBoolean() || b.getAsBoolean())); break;
                    case CMP_EQ: pushValue((a.getAsBoolean() == b.getAsBoolean())); break;
                    case CMP_NE: pushValue((a.getAsBoolean() != b.getAsBoolean())); break;
                    case NOT: pushValue((!a.getAsBoolean())); break;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static<T, V> BooleanLocation cmp_other(final boolean lazy, final ObjectLocation<T> a, final ObjectLocation<V> b, final Operator op) {
        return BooleanVariable.make(lazy, new AbstractBindingExpression() {
            public void compute() {
                T aVal = a.get();
                V bVal = b.get();
                switch (op) {
                    case CMP_EQ: pushValue((aVal == null ? bVal == null : aVal.equals(bVal))); break;
                    case CMP_NE: pushValue((aVal == null ? bVal != null : !aVal.equals(bVal))); break;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static<T, V> BooleanLocation cmp_other(final boolean lazy, final ObjectLocation<T> a, final SequenceLocation<V> b, final Operator op) {
        return BooleanVariable.make(lazy, new AbstractBindingExpression() {
            public void compute() {
                T aVal = a.get();
                Sequence<V> bVal = b.getAsSequence();
                switch (op) {
                    case CMP_EQ:
                        pushValue((aVal == null ? (Sequences.size(bVal) == 0) : bVal.size() == 1 && bVal.get(0).equals(aVal))); break;

                    case CMP_NE:
                        pushValue((aVal == null ? (Sequences.size(bVal) != 0) : bVal.size() != 1 || !bVal.get(0).equals(aVal))); break;

                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static<T, V> BooleanLocation cmp_other(final boolean lazy, final SequenceLocation<T> a, final ObjectLocation<V> b, final Operator op) {
        switch (op) {
            case CMP_EQ:
            case CMP_NE:
                return cmp_other(lazy, b, a, op);
            default: throw new UnsupportedOperationException(op.toString());
        }
    }

    public static<T, V> BooleanLocation cmp_other(final boolean lazy, final SequenceLocation<T> a, final SequenceLocation<V> b, final Operator op) {
        return BooleanVariable.make(lazy, new AbstractBindingExpression() {
            public void compute() {
                Sequence<T> aVal = a.getAsSequence();
                Sequence<V> bVal = b.getAsSequence();
                switch (op) {
                    case CMP_EQ: pushValue((aVal == null ? (Sequences.size(bVal) == 0) : aVal.equals(bVal))); break;
                    case CMP_NE: pushValue((aVal == null ? (Sequences.size(bVal) != 0) : !aVal.equals(bVal))); break;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }


}
