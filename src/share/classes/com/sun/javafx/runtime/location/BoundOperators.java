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

import com.sun.javafx.functions.Function0;
import com.sun.javafx.functions.Function1;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

/**
 * Factories for bound operator expressions.  Factories for most operators (plus, ==, !) are generated and live in
 * GeneratedBoundOperators; these are the hand-written ones.
 *
 * @author Brian Goetz
 */
public class BoundOperators {

    // non-instantiable
    private BoundOperators() { }

    // @@@ Currently there are two different schemes here; the old scheme (GeneratedBoundOperators, plus everything below
    // the @@@---@@@ line in this file), and a newer, more compact scheme.   The more compact scheme is not complete, but
    // it currently handles all the XxxLocations for primitive types, plus an object-to-NumericLocation wrapper, for all
    // the binary arithmetic ops (plus, minus, times, divide, modulo)

    public enum NumericArithmeticOperator {
        PLUS {
            public int operate(int left, int right) { return left + right; }
            public long operate(long left, long right) { return left + right; }
            public float operate(float left, float right) { return left + right; }
            public double operate(double left, double right) { return left + right; }
        },
        MINUS {
            public int operate(int left, int right) { return left - right; }
            public long operate(long left, long right) { return left - right; }
            public float operate(float left, float right) { return left - right; }
            public double operate(double left, double right) { return left - right; }
        },
        TIMES {
            public int operate(int left, int right) { return left * right; }
            public long operate(long left, long right) { return left * right; }
            public float operate(float left, float right) { return left * right; }
            public double operate(double left, double right) { return left * right; }
        },
        DIVIDE {
            public int operate(int left, int right) { return left / right; }
            public long operate(long left, long right) { return left / right; }
            public float operate(float left, float right) { return left / right; }
            public double operate(double left, double right) { return left / right; }
        },
        MODULO {
            public int operate(int left, int right) { return left % right; }
            public long operate(long left, long right) { return left % right; }
            public float operate(float left, float right) { return left % right; }
            public double operate(double left, double right) { return left % right; }
        };

        public abstract int operate(int left, int right);
        public abstract long operate(long left, long right);
        public abstract float operate(float left, float right);
        public abstract double operate(double left, double right);
    }

    public enum NumericUnaryOperator {
        NEGATE {
            public int operate(int left) { return -left ; }
            public long operate(long left) { return -left ; }
            public float operate(float left) { return -left ; }
            public double operate(double left) { return -left ; }
        };
        public abstract int operate(int left);
        public abstract long operate(long left);
        public abstract float operate(float left);
        public abstract double operate(double left);
    }

    public enum NumericComparisonOperator {
        CMP_EQ {
            public boolean operate(int left, int right) { return left == right; }
            public boolean operate(long left, long right) { return left == right; }
            public boolean operate(float left, float right) { return left == right; }
            public boolean operate(double left, double right) { return left == right; }
        },
        CMP_NE {
            public boolean operate(int left, int right) { return left != right; }
            public boolean operate(long left, long right) { return left != right; }
            public boolean operate(float left, float right) { return left != right; }
            public boolean operate(double left, double right) { return left != right; }
        },
        CMP_LT {
            public boolean operate(int left, int right) { return left < right; }
            public boolean operate(long left, long right) { return left< right; }
            public boolean operate(float left, float right) { return left < right; }
            public boolean operate(double left, double right) { return left < right; }
        },
        CMP_LE {
            public boolean operate(int left, int right) { return left <= right; }
            public boolean operate(long left, long right) { return left <= right; }
            public boolean operate(float left, float right) { return left <= right; }
            public boolean operate(double left, double right) { return left <= right; }
        },
        CMP_GT {
            public boolean operate(int left, int right) { return left > right; }
            public boolean operate(long left, long right) { return left > right; }
            public boolean operate(float left, float right) { return left > right; }
            public boolean operate(double left, double right) { return left > right; }
        },
        CMP_GE {
            public boolean operate(int left, int right) { return left >= right; }
            public boolean operate(long left, long right) { return left >= right; }
            public boolean operate(float left, float right) { return left >= right; }
            public boolean operate(double left, double right) { return left >= right; }
        };

        public abstract boolean operate(int left, int right);
        public abstract boolean operate(long left, long right);
        public abstract boolean operate(float left, float right);
        public abstract boolean operate(double left, double right);
    }

    public enum BooleanOperator {
        AND {
            public boolean operate(boolean left, boolean right) {
                return left && right;
            }
        },
        OR {
            public boolean operate(boolean left, boolean right) {
                return left || right;
            }
        },
        EQ {
            public boolean operate(boolean left, boolean right) {
                return left == right;
            }
        },
        NE {
            public boolean operate(boolean left, boolean right) {
                return left != right;
            }
        };
        public abstract boolean operate(boolean left, boolean right);
    }

    public enum BooleanUnaryOperator {
        NOT {
            public boolean operate(boolean operand) {
                return !operand;
            }
        };
        public abstract boolean operate(boolean operand);
    }


    public static IntLocation op_int(final NumericLocation a, final NumericLocation b, final NumericArithmeticOperator op) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return op.operate(a.getAsInt(), b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation op_double(final NumericLocation a, final NumericLocation b, final NumericArithmeticOperator op) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return op.operate(a.getAsDouble(), b.getAsDouble());
            }
        }, a, b);
    }

    public static FloatLocation op_float(final NumericLocation a, final NumericLocation b, final NumericArithmeticOperator op) {
        return FloatVariable.make(new FloatBindingExpression() {
            public float computeValue() {
                return op.operate(a.getAsFloat(), b.getAsFloat());
            }
        }, a, b);
    }

    public static LongLocation op_long(final NumericLocation a, final NumericLocation b, final NumericArithmeticOperator op) {
        return LongVariable.make(new LongBindingExpression() {
            public long computeValue() {
                return op.operate(a.getAsLong(), b.getAsLong());
            }
        }, a, b);
    }


    public static IntLocation op_int(final NumericLocation a, final NumericUnaryOperator op) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return op.operate(a.getAsInt());
            }
        }, a);
    }

    public static DoubleLocation op_double(final NumericLocation a, final NumericUnaryOperator op) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return op.operate(a.getAsDouble());
            }
        }, a);
    }

    public static FloatLocation op_float(final NumericLocation a, final NumericUnaryOperator op) {
        return FloatVariable.make(new FloatBindingExpression() {
            public float computeValue() {
                return op.operate(a.getAsFloat());
            }
        }, a);
    }

    public static LongLocation op_long(final NumericLocation a, final NumericUnaryOperator op) {
        return LongVariable.make(new LongBindingExpression() {
            public long computeValue() {
                return op.operate(a.getAsLong());
            }
        }, a);
    }


    public static BooleanLocation cmp_int(final NumericLocation a, final NumericLocation b, final NumericComparisonOperator op) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return op.operate(a.getAsInt(), b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation cmp_long(final NumericLocation a, final NumericLocation b, final NumericComparisonOperator op) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return op.operate(a.getAsLong(), b.getAsLong());
            }
        }, a, b);
    }

    public static BooleanLocation cmp_float(final NumericLocation a, final NumericLocation b, final NumericComparisonOperator op) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return op.operate(a.getAsFloat(), b.getAsFloat());
            }
        }, a, b);
    }

    public static BooleanLocation cmp_double(final NumericLocation a, final NumericLocation b, final NumericComparisonOperator op) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return op.operate(a.getAsDouble(), b.getAsDouble());
            }
        }, a, b);
    }


    public static BooleanLocation op_boolean(final BooleanLocation a, final BooleanLocation b, final BooleanOperator op) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return op.operate(a.getAsBoolean(), b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation op_boolean(final BooleanLocation a, final BooleanUnaryOperator op) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return op.operate(a.getAsBoolean());
            }
        }, a);
    }

    // @@@---@@@ Below here is the old scheme

    public static <T, V> BooleanLocation eq_oo(final ObjectLocation<T> a, final ObjectLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                T aVal = a.get();
                V bVal = b.get();
                if (aVal == null)
                    return bVal == null;
                else
                    return aVal.equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation ne_oo(final ObjectLocation<T> a, final ObjectLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                T aVal = a.get();
                V bVal = b.get();
                if (aVal == null)
                    return bVal != null;
                else
                    return !aVal.equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation eq_ss(final SequenceLocation<T> a, final SequenceLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                Sequence<T> aVal = a.getAsSequence();
                Sequence<V> bVal = b.getAsSequence();
                if (aVal == null)
                    return bVal == null || bVal.isEmpty();
                else
                    return aVal.equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation eq_so(final SequenceLocation<T> a, final ObjectLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                Sequence<T> aVal = a.getAsSequence();
                V bVal = b.get();
                if (bVal == null)
                    return aVal == null || aVal.isEmpty();
                else
                    return aVal.size()==1 && aVal.get(0).equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation eq_os(final ObjectLocation<T> a, final SequenceLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                T aVal = a.get();
                Sequence<V> bVal = b.getAsSequence();
                if (aVal == null)
                    return bVal == null || bVal.isEmpty();
                else
                    return bVal.size()==1 && bVal.get(0).equals(aVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation ne_ss(final SequenceLocation<T> a, final SequenceLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                Sequence<T> aVal = a.getAsSequence();
                Sequence<V> bVal = b.getAsSequence();
                if (aVal == null)
                    return bVal != null && !bVal.isEmpty();
                else
                    return !aVal.equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation ne_so(final SequenceLocation<T> a, final ObjectLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                Sequence<T> aVal = a.getAsSequence();
                V bVal = b.get();
                if (bVal == null)
                    return aVal != null && !aVal.isEmpty();
                else
                   return aVal.size()!=1 || !aVal.get(0).equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation ne_os(final ObjectLocation<T> a, final SequenceLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                T aVal = a.get();
                Sequence<V> bVal = b.getAsSequence();
                if (aVal == null)
                    return bVal != null && !bVal.isEmpty();
                else
                    return bVal.size()!=1 || !bVal.get(0).equals(aVal);
            }
        }, a, b);
    }


    public static BooleanLocation eq_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) == ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation eq_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) == ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) == ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation eq_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) == ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) == ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation eq_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) == ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation eq_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) == ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) == ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation eq_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) == ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) == ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation ne_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) != ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ne_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) != ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) != ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation ne_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) != ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) != ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation ne_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) != ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ne_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) != ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) != ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ne_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) != ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) != ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation eq_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) == ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation eq_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) == ((boolean) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) == ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation eq_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) == ((boolean) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation ne_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) != ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation ne_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) != ((boolean) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) != ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation ne_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) != ((boolean) b.get());
            }
        }, a, b);
    }


    public static IntLocation makeBoundIf(boolean lazy,
                                          final BooleanLocation conditional,
                                          final Function0<IntLocation> thenBranch,
                                          final Function0<IntLocation> elseBranch) {
        return new IndirectIntExpression(lazy, conditional) {
            public IntLocation computeLocation() {
                return (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
            }
        };
    }

    public static DoubleLocation makeBoundIf(boolean lazy,
                                             final BooleanLocation conditional,
                                             final Function0<DoubleLocation> thenBranch,
                                             final Function0<DoubleLocation> elseBranch) {
        return new IndirectDoubleExpression(lazy, conditional) {
            public DoubleLocation computeLocation() {
                return (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
            }
        };
    }

    public static BooleanLocation makeBoundIf(boolean lazy,
                                              final BooleanLocation conditional,
                                              final Function0<BooleanLocation> thenBranch,
                                              final Function0<BooleanLocation> elseBranch) {
        return new IndirectBooleanExpression(lazy, conditional) {
            public BooleanLocation computeLocation() {
                return (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
            }
        };
    }

    public static<T> ObjectLocation<T> makeBoundIf(boolean lazy,
                                                   final BooleanLocation conditional,
                                                   final Function0<ObjectLocation<T>> thenBranch,
                                                   final Function0<ObjectLocation<T>> elseBranch) {
        return new IndirectObjectExpression<T>(lazy, conditional) {
            public ObjectLocation<T> computeLocation() {
                return (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
            }
        };
    }

    public static<T> SequenceLocation<T> makeBoundIf(TypeInfo<T> typeInfo,
                                                     boolean lazy,
                                                     final BooleanLocation conditional,
                                                     final Function0<SequenceLocation<T>> thenBranch,
                                                     final Function0<SequenceLocation<T>> elseBranch) {
        return new IndirectSequenceExpression<T>(typeInfo, lazy, conditional) {
            public SequenceLocation<T> computeLocation() {
                return (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
            }
        };
    }


    public static<T> IntLocation makeBoundSelect(boolean lazy,
                                                 final ObjectLocation<T> receiver,
                                                 final Function1<IntLocation, T> selector) {
        return new IndirectIntExpression(lazy, receiver) {
            public IntLocation computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? IntConstant.make(DEFAULT) : selector.invoke(selectorValue);
            }

            public int setAsInt(int value) {
                // @@@ Shouldn't mutate unconditionally -- only if bound bidirectionally
                return helper.get().setAsInt(value);
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public Integer set(Integer value) {
                return helper.get().set(value);
            }
        };
    }

    public static<T> DoubleLocation makeBoundSelect(boolean lazy,
                                                    final ObjectLocation<T> receiver,
                                                    final Function1<DoubleLocation, T> selector) {
        return new IndirectDoubleExpression(lazy, receiver) {
            public DoubleLocation computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? DoubleConstant.make(DEFAULT) : selector.invoke(selectorValue);
            }

            public double setAsDouble(double value) {
                // @@@ Shouldn't mutate unconditionally -- only if bound bidirectionally
                return helper.get().setAsDouble(value);
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public Double set(Double value) {
                return helper.get().set(value);
            }
        };
    }

    public static<T> BooleanLocation makeBoundSelect(boolean lazy,
                                                     final ObjectLocation<T> receiver,
                                                     final Function1<BooleanLocation, T> selector) {
        return new IndirectBooleanExpression(lazy, receiver) {
            public BooleanLocation computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? BooleanConstant.make(DEFAULT) : selector.invoke(selectorValue);
            }

            public boolean setAsBoolean(boolean value) {
                // @@@ Shouldn't mutate unconditionally -- only if bound bidirectionally
                return helper.get().setAsBoolean(value);
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public Boolean set(Boolean value) {
                return helper.get().set(value);
            }
        };
    }

    public static<T, U> ObjectLocation<U> makeBoundSelect(final TypeInfo<U> typeInfo,
                                                          boolean lazy,
                                                          final ObjectLocation<T> receiver,
                                                          final Function1<ObjectLocation<U>, T> selector) {
        return new IndirectObjectExpression<U>(lazy, receiver) {

            public ObjectLocation<U> computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? ObjectConstant.make(typeInfo.defaultValue) : selector.invoke(selectorValue);
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public U set(U value) {
                // @@@ Shouldn't mutate unconditionally -- only if bound bidirectionally
                return helper.get().set(value);
            }
        };
    }

    public static<T, U> SequenceLocation<U> makeBoundSelect(final TypeInfo<U> typeInfo,
                                                            boolean lazy,
                                                            final ObjectLocation<T> receiver,
                                                            final Function1<SequenceLocation<U>, T> selector) {

        final SequenceLocation<U> defaultValue = SequenceConstant.<U>make(typeInfo, typeInfo.emptySequence);

        return new IndirectSequenceExpression<U>(typeInfo, lazy, receiver) {

            public SequenceLocation<U> computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? defaultValue : selector.invoke(selectorValue);
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public Sequence<U> setAsSequence(Sequence<? extends U> value) {
                // @@@ Shouldn't mutate unconditionally -- only if bound bidirectionally
                return helper.get().setAsSequence(value);
            }

            public Sequence<U> set(Sequence<U> value) {
                return helper.get().set(value);
            }

            public U set(int position, U newValue) {
                return helper.get().set(position, newValue);
            }

            public Sequence<? extends U> replaceSlice(int startPos, int endPos, Sequence<? extends U> newValues) {
                return helper.get().replaceSlice(startPos, endPos, newValues);
            }

            public void delete(int position) {
                helper.get().delete(position);
            }

            public void deleteSlice(int startPos, int endPos) {
                helper.get().deleteSlice(startPos, endPos);
            }

            public void delete(SequencePredicate<U> tSequencePredicate) {
                helper.get().delete(tSequencePredicate);
            }

            public void deleteAll() {
                helper.get().deleteAll();
            }

            public void deleteValue(U targetValue) {
                helper.get().deleteValue(targetValue);
            }

            public void insert(U value) {
                helper.get().insert(value);
            }

            public void insert(Sequence<? extends U> values) {
                helper.get().insert(values);
            }

            public void insertFirst(U value) {
                helper.get().insertFirst(value);
            }

            public void insertFirst(Sequence<? extends U> values) {
                helper.get().insertFirst(values);
            }

            public void insertBefore(U value, int position) {
                helper.get().insertBefore(value, position);
            }

            public void insertBefore(U value, SequencePredicate<U> tSequencePredicate) {
                helper.get().insertBefore(value, tSequencePredicate);
            }

            public void insertBefore(Sequence<? extends U> values, int position) {
                helper.get().insertBefore(values, position);
            }

            public void insertBefore(Sequence<? extends U> values, SequencePredicate<U> tSequencePredicate) {
                helper.get().insertBefore(values, tSequencePredicate);
            }

            public void insertAfter(U value, int position) {
                helper.get().insertAfter(value, position);
            }

            public void insertAfter(U value, SequencePredicate<U> tSequencePredicate) {
                helper.get().insertAfter(value, tSequencePredicate);
            }

            public void insertAfter(Sequence<? extends U> values, int position) {
                helper.get().insertAfter(values, position);
            }

            public void insertAfter(Sequence<? extends U> values, SequencePredicate<U> tSequencePredicate) {
                helper.get().insertAfter(values, tSequencePredicate);
            }
        };
    }

}
