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

    private static class NumericBindingExpression extends BindingExpression {
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
        return BooleanVariable.make(lazy, new BindingExpression() {
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
        return BooleanVariable.make(lazy, new BindingExpression() {
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
        return BooleanVariable.make(lazy, new BindingExpression() {
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
        return BooleanVariable.make(lazy, new BindingExpression() {
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

    // @@@ Missing Byte, Short, Long for if and select

    private static BindingExpression wrap(final Function0<? extends Location> fun) {
        return new BindingExpression() {
            public void compute() {
                pushValue(fun.invoke());
            }
        };
    }

    private static<T> BindingExpression wrap(final Function1<? extends Location, T> fun, final ObjectLocation<T> receiver) {
        return new BindingExpression() {
            public void compute() {
                pushValue(fun.invoke(receiver.get()));
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static<T, L extends ObjectLocation<T>> L makeBoundIf(TypeInfo<T, L> typeInfo,
                                                                boolean lazy,
                                                                final BooleanLocation conditional,
                                                                final BindingExpression thenBranch,
                                                                final BindingExpression elseBranch) {
        final L loc = typeInfo.makeLocation();
        final ObjectVariable<L> helper = IndirectLocationHelper.makeIndirectHelper(lazy, loc, new BindingExpression() {
            public void compute() {
                if (thenBranch.location == null) {
                    // First-time setup
                    thenBranch.setLocation(location);
                    elseBranch.setLocation(location);
                }

                if (conditional.get())
                    thenBranch.compute();
                else
                    elseBranch.compute();
            }
        }, conditional);
        ((BindableLocation<T, ?>) loc).bind(lazy, IndirectLocationHelper.makeBindingExpression(typeInfo, helper));
        return loc;
    }

    public static IntLocation makeBoundIf(boolean lazy,
                                          final BooleanLocation conditional,
                                          final Function0<IntLocation> thenBranch,
                                          final Function0<IntLocation> elseBranch) {
        return makeBoundIf(TypeInfo.Integer, lazy, conditional, wrap(thenBranch), wrap(elseBranch));
    }

    public static FloatLocation makeBoundIf(boolean lazy,
                                            final BooleanLocation conditional,
                                            final Function0<FloatLocation> thenBranch,
                                            final Function0<FloatLocation> elseBranch) {
        return makeBoundIf(TypeInfo.Float, lazy, conditional, wrap(thenBranch), wrap(elseBranch));
    }

    public static DoubleLocation makeBoundIf(boolean lazy,
                                             final BooleanLocation conditional,
                                             final Function0<DoubleLocation> thenBranch,
                                             final Function0<DoubleLocation> elseBranch) {
        return makeBoundIf(TypeInfo.Double, lazy, conditional, wrap(thenBranch), wrap(elseBranch));
    }

    public static BooleanLocation makeBoundIf(boolean lazy,
                                              final BooleanLocation conditional,
                                              final Function0<BooleanLocation> thenBranch,
                                              final Function0<BooleanLocation> elseBranch) {
        return makeBoundIf(TypeInfo.Boolean, lazy, conditional, wrap(thenBranch), wrap(elseBranch));
    }

    public static<T> ObjectLocation<T> makeBoundIf(boolean lazy,
                                                   final BooleanLocation conditional,
                                                   final Function0<ObjectLocation<T>> thenBranch,
                                                   final Function0<ObjectLocation<T>> elseBranch) {
        return makeBoundIf(TypeInfo.<T>getTypeInfo(), lazy, conditional, wrap(thenBranch), wrap(elseBranch));
    }

    public static<T> SequenceLocation<T> makeBoundIf(TypeInfo<T, ?> typeInfo,
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


    @SuppressWarnings("unchecked")
    public static<T, L extends ObjectLocation<T>> L makeBoundSelect(final TypeInfo<T, L> typeInfo,
                                                                    boolean lazy,
                                                                    final ObjectLocation<?> receiver,
                                                                    final BindingExpression selector) {
        final L loc = typeInfo.makeLocation();
        final ObjectVariable<L> helper = IndirectLocationHelper.makeIndirectHelper(lazy, loc, new BindingExpression() {
            public void compute() {
                if (selector.location == null) {
                    // First-time setup
                    selector.setLocation(location);
                }

                if (receiver.get() != null)
                    selector.compute();
                else
                    pushValue(typeInfo.makeDefaultConstant());
            }
        }, receiver);
        ((BindableLocation<T, ?>) loc).bind(lazy, IndirectLocationHelper.makeBindingExpression(typeInfo, helper));
        return loc;
    }

    public static<X> IntLocation makeBoundSelect(boolean lazy,
                                                 final ObjectLocation<X> receiver,
                                                 final Function1<IntLocation, X> selector) {
        return makeBoundSelect(TypeInfo.Integer, lazy, receiver, wrap(selector, receiver));
    }

    public static<T> DoubleLocation makeBoundSelect(boolean lazy,
                                                    final ObjectLocation<T> receiver,
                                                    final Function1<DoubleLocation, T> selector) {
        return makeBoundSelect(TypeInfo.Double, lazy, receiver, wrap(selector, receiver));
    }

    public static<T> FloatLocation makeBoundSelect(boolean lazy,
                                                   final ObjectLocation<T> receiver,
                                                   final Function1<FloatLocation, T> selector) {
        return makeBoundSelect(TypeInfo.Float, lazy, receiver, wrap(selector, receiver));
    }

    public static<T> BooleanLocation makeBoundSelect(boolean lazy,
                                                     final ObjectLocation<T> receiver,
                                                     final Function1<BooleanLocation, T> selector) {
        return makeBoundSelect(TypeInfo.Boolean, lazy, receiver, wrap(selector, receiver));
    }

    public static<T, U> ObjectLocation<U> makeBoundSelect(final TypeInfo<U, ?> typeInfo,
                                                          boolean lazy,
                                                          final ObjectLocation<T> receiver,
                                                          final Function1<ObjectLocation<U>, T> selector) {
        return makeBoundSelect(TypeInfo.<U>getTypeInfo(), lazy, receiver, wrap(selector, receiver));
    }

    public static<T, U> SequenceLocation<U> makeBoundSelect(final TypeInfo<U, ?> typeInfo,
                                                            boolean lazy,
                                                            final ObjectLocation<T> receiver,
                                                            final Function1<SequenceLocation<U>, T> selector) {

        final SequenceLocation<U> defaultValue = SequenceConstant.<U>make(typeInfo, typeInfo.emptySequence);

        return new IndirectSequenceExpression<U>(typeInfo, lazy, receiver) {

            public SequenceLocation<U> computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? defaultValue : selector.invoke(selectorValue);
            }
        };
    }

}
