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
import com.sun.javafx.runtime.sequence.Sequences;

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

    public enum Operator { PLUS, MINUS, TIMES, DIVIDE, MODULO, NEGATE,
        CMP_EQ, CMP_NE,
        CMP_LT, CMP_LE, CMP_GT, CMP_GE,
        AND, OR, NOT }

    public static IntLocation op_int(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return IntVariable.make(lazy, new IntBindingExpression() {
            public int computeValue() {
                switch (op) {
                    case PLUS: return a.getAsInt() + b.getAsInt();
                    case MINUS: return a.getAsInt() - b.getAsInt();
                    case TIMES: return a.getAsInt() * b.getAsInt();
                    case DIVIDE: return a.getAsInt() / b.getAsInt();
                    case MODULO: return a.getAsInt() % b.getAsInt();
                    case NEGATE: return -a.getAsInt();
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static DoubleLocation op_double(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return DoubleVariable.make(lazy, new DoubleBindingExpression() {
            public double computeValue() {
                switch (op) {
                    case PLUS: return a.getAsDouble() + b.getAsDouble();
                    case MINUS: return a.getAsDouble() - b.getAsDouble();
                    case TIMES: return a.getAsDouble() * b.getAsDouble();
                    case DIVIDE: return a.getAsDouble() / b.getAsDouble();
                    case MODULO: return a.getAsDouble() % b.getAsDouble();
                    case NEGATE: return -a.getAsDouble();
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static FloatLocation op_float(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return FloatVariable.make(lazy, new FloatBindingExpression() {
            public float computeValue() {
                switch (op) {
                    case PLUS: return a.getAsFloat() + b.getAsFloat();
                    case MINUS: return a.getAsFloat() - b.getAsFloat();
                    case TIMES: return a.getAsFloat() * b.getAsFloat();
                    case DIVIDE: return a.getAsFloat() / b.getAsFloat();
                    case MODULO: return a.getAsFloat() % b.getAsFloat();
                    case NEGATE: return -a.getAsFloat();
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static LongLocation op_long(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return LongVariable.make(lazy, new LongBindingExpression() {
            public long computeValue() {
                switch (op) {
                    case PLUS: return a.getAsLong() + b.getAsLong();
                    case MINUS: return a.getAsLong() - b.getAsLong();
                    case TIMES: return a.getAsLong() * b.getAsLong();
                    case DIVIDE: return a.getAsLong() / b.getAsLong();
                    case MODULO: return a.getAsLong() % b.getAsLong();
                    case NEGATE: return -a.getAsLong();
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static BooleanLocation cmp_int(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                int left = a.getAsInt();
                int right = b.getAsInt();
                switch (op) {
                    case CMP_EQ: return left == right;
                    case CMP_NE: return left != right;
                    case CMP_LT: return left < right;
                    case CMP_LE: return left <= right;
                    case CMP_GT: return left > right;
                    case CMP_GE: return left >= right;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static BooleanLocation cmp_long(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                long left = a.getAsLong();
                long right = b.getAsLong();
                switch (op) {
                    case CMP_EQ: return left == right;
                    case CMP_NE: return left != right;
                    case CMP_LT: return left < right;
                    case CMP_LE: return left <= right;
                    case CMP_GT: return left > right;
                    case CMP_GE: return left >= right;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static BooleanLocation cmp_float(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                float left = a.getAsFloat();
                float right = b.getAsFloat();
                switch (op) {
                    case CMP_EQ: return left == right;
                    case CMP_NE: return left != right;
                    case CMP_LT: return left < right;
                    case CMP_LE: return left <= right;
                    case CMP_GT: return left > right;
                    case CMP_GE: return left >= right;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static BooleanLocation cmp_double(final boolean lazy, final NumericLocation a, final NumericLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                double left = a.getAsDouble();
                double right = b.getAsDouble();
                switch (op) {
                    case CMP_EQ: return left == right;
                    case CMP_NE: return left != right;
                    case CMP_LT: return left < right;
                    case CMP_LE: return left <= right;
                    case CMP_GT: return left > right;
                    case CMP_GE: return left >= right;
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }


    public static BooleanLocation op_boolean(final boolean lazy, final BooleanLocation a, final BooleanLocation b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                switch (op) {
                    case AND: return a.getAsBoolean() && b.getAsBoolean();
                    case OR: return a.getAsBoolean() || b.getAsBoolean();
                    case CMP_EQ: return a.getAsBoolean() == b.getAsBoolean();
                    case CMP_NE: return a.getAsBoolean() != b.getAsBoolean();
                    case NOT: return !a.getAsBoolean();
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static<T, V> BooleanLocation cmp_other(final boolean lazy, final ObjectLocation<T> a, final ObjectLocation<V> b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                T aVal = a.get();
                V bVal = b.get();
                switch (op) {
                    case CMP_EQ: return aVal == null ? bVal == null : aVal.equals(bVal);
                    case CMP_NE: return aVal == null ? bVal != null : !aVal.equals(bVal);
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    public static<T, V> BooleanLocation cmp_other(final boolean lazy, final ObjectLocation<T> a, final SequenceLocation<V> b, final Operator op) {
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                T aVal = a.get();
                Sequence<V> bVal = b.getAsSequence();
                switch (op) {
                    case CMP_EQ:
                        return aVal == null ? (Sequences.size(bVal) == 0) : bVal.size() == 1 && bVal.get(0).equals(aVal);

                    case CMP_NE:
                        return aVal == null ? (Sequences.size(bVal) != 0) : bVal.size() != 1 || !bVal.get(0).equals(aVal);

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
        return BooleanVariable.make(lazy, new BooleanBindingExpression() {
            public boolean computeValue() {
                Sequence<T> aVal = a.getAsSequence();
                Sequence<V> bVal = b.getAsSequence();
                switch (op) {
                    case CMP_EQ: return aVal == null ? (Sequences.size(bVal) == 0) : aVal.equals(bVal);
                    case CMP_NE: return aVal == null ? (Sequences.size(bVal) != 0) : !aVal.equals(bVal);
                    default: throw new UnsupportedOperationException(op.toString());
                }
            }
        }, a, b);
    }

    // @@@ Missing Byte, Short, Long for if and select

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

    public static FloatLocation makeBoundIf(boolean lazy,
                                            final BooleanLocation conditional,
                                            final Function0<FloatLocation> thenBranch,
                                            final Function0<FloatLocation> elseBranch) {
        return new IndirectFloatExpression(lazy, conditional) {
            public FloatLocation computeLocation() {
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

    public static<T> FloatLocation makeBoundSelect(boolean lazy,
                                                   final ObjectLocation<T> receiver,
                                                   final Function1<FloatLocation, T> selector) {
        return new IndirectFloatExpression(lazy, receiver) {
            public FloatLocation computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? FloatConstant.make(DEFAULT) : selector.invoke(selectorValue);
            }

            public float setAsFloat(float value) {
                // @@@ Shouldn't mutate unconditionally -- only if bound bidirectionally
                return helper.get().setAsFloat(value);
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public Float set(Float value) {
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
