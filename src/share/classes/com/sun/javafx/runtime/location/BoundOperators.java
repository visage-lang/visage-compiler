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

import com.sun.javafx.functions.Function0;
import com.sun.javafx.functions.Function1;
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * Factories for bound operator expressions.  Factories for most operators (plus, ==, !) are generated and live in
 * GeneratedBoundOperators; these are the hand-written ones.
 *
 * @author Brian Goetz
 */
public class BoundOperators extends GeneratedBoundOperators {
    // non-instantiable
    private BoundOperators() { }

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
                    return bVal == null;
                else
                    return aVal.equals(bVal);
            }
        }, a, b);
    }

    public static <T, V> BooleanLocation ne_ss(final SequenceLocation<T> a, final SequenceLocation<V> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                Sequence<T> aVal = a.getAsSequence();
                Sequence<V> bVal = b.getAsSequence();
                if (aVal == null)
                    return bVal != null;
                else
                    return !aVal.equals(bVal);
            }
        }, a, b);
    }


    public static IntLocation makeBoundIf(boolean lazy,
                                          final BooleanLocation conditional,
                                          final Function0<IntLocation> thenBranch,
                                          final Function0<IntLocation> elseBranch) {
        return new IndirectIntExpression(lazy, conditional) {
            protected IntLocation computeLocation() {
                clearDynamicDependencies();
                IntLocation result = (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
                addDynamicDependency(result);
                return result;
            }
        };
    }

    public static DoubleLocation makeBoundIf(boolean lazy,
                                             final BooleanLocation conditional,
                                             final Function0<DoubleLocation> thenBranch,
                                             final Function0<DoubleLocation> elseBranch) {
        return new IndirectDoubleExpression(lazy, conditional) {
            protected DoubleLocation computeLocation() {
                clearDynamicDependencies();
                DoubleLocation result = (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
                addDynamicDependency(result);
                return result;
            }
        };
    }

    public static BooleanLocation makeBoundIf(boolean lazy,
                                              final BooleanLocation conditional,
                                              final Function0<BooleanLocation> thenBranch,
                                              final Function0<BooleanLocation> elseBranch) {
        return new IndirectBooleanExpression(lazy, conditional) {
            protected BooleanLocation computeLocation() {
                clearDynamicDependencies();
                BooleanLocation result = (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
                addDynamicDependency(result);
                return result;
            }
        };
    }

    public static<T> ObjectLocation<T> makeBoundIf(boolean lazy,
                                                   final BooleanLocation conditional,
                                                   final Function0<ObjectLocation<T>> thenBranch,
                                                   final Function0<ObjectLocation<T>> elseBranch) {
        return new IndirectObjectExpression<T>(lazy, conditional) {
            protected ObjectLocation<T> computeLocation() {
                clearDynamicDependencies();
                ObjectLocation<T> result = (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
                addDynamicDependency(result);
                return result;
            }
        };
    }

    public static<T> SequenceLocation<T> makeBoundIf(Class<T> clazz,
                                                     boolean lazy,
                                                     final BooleanLocation conditional,
                                                     final Function0<SequenceLocation<T>> thenBranch,
                                                     final Function0<SequenceLocation<T>> elseBranch) {
        return new IndirectSequenceExpression<T>(clazz, lazy, conditional) {
            protected SequenceLocation<T> computeLocation() {
                clearDynamicDependencies();
                SequenceLocation<T> result = (conditional.get()) ? thenBranch.invoke() : elseBranch.invoke();
                addDynamicDependency(result);
                return result;
            }
        };
    }


    public static<T> IntLocation makeBoundSelect(boolean lazy, 
                                                 final ObjectLocation<T> receiver,
                                                 final Function1<IntLocation, T> selector) {
        return new IndirectIntExpression(lazy, receiver) {
            protected IntLocation computeLocation() {
                clearDynamicDependencies();
                T selectorValue = receiver.get();
                if (selectorValue == null) {
                    return IntConstant.make(DEFAULT);
                } else {
                    IntLocation result = selector.invoke(selectorValue);
                    addDynamicDependency(result);
                    return result;
                }
            }

            public int setAsInt(int value) {
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
            protected DoubleLocation computeLocation() {
                clearDynamicDependencies();
                T selectorValue = receiver.get();
                if (selectorValue == null) {
                    return DoubleConstant.make(DEFAULT);
                } else {
                    DoubleLocation result = selector.invoke(selectorValue);
                    addDynamicDependency(result);
                    return result;
                }
            }

            public double setAsInt(double value) {
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
            protected BooleanLocation computeLocation() {
                clearDynamicDependencies();
                T selectorValue = receiver.get();
                if (selectorValue == null) {
                    return BooleanConstant.make(DEFAULT);
                } else {
                    BooleanLocation result = selector.invoke(selectorValue);
                    addDynamicDependency(result);
                    return result;
                }
            }

            public boolean setAsBoolean(boolean value) {
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

    public static<T, U> ObjectLocation<U> makeBoundSelect(final Class clazz,
                                                          boolean lazy,
                                                          final ObjectLocation<T> receiver,
                                                          final Function1<ObjectLocation<U>, T> selector) {
        return new IndirectObjectExpression<U>(lazy, receiver) {

            protected ObjectLocation<U> computeLocation() {
                clearDynamicDependencies();
                T selectorValue = receiver.get();
                if (selectorValue == null) {
                    return ObjectConstant.make(Util.<U>defaultValue(clazz));
                } else {
                    ObjectLocation<U> result = selector.invoke(selectorValue);
                    addDynamicDependency(result);
                    return result;
                }
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public U set(U value) {
                return helper.get().set(value);
            }
        };
    }

    public static<T, U> SequenceLocation<U> makeBoundSelect(final Class<U> clazz,
                                                            boolean lazy,
                                                            final ObjectLocation<T> receiver,
                                                            final Function1<SequenceLocation<U>, T> selector) {

        final SequenceLocation<U> defaultValue = SequenceConstant.<U>make(Sequences.emptySequence(clazz));

        return new IndirectSequenceExpression<U>(clazz, lazy, receiver) {
            // @@@ Sequence triggers are not going to be right.  Need to add a trigger to each "generation" of underlying
            // sequence location, and propagate sequence triggers to outer Location wrapper

            protected SequenceLocation<U> computeLocation() {
                clearDynamicDependencies();
                T selectorValue = receiver.get();
                if (selectorValue == null) {
                    return defaultValue;
                } else {
                    SequenceLocation<U> result = selector.invoke(selectorValue);
                    addDynamicDependency(result);
                    return result;
                }
            }

            public void setDefault() {
                helper.get().setDefault();
            }

            public Sequence<U> setAsSequence(Sequence<? extends U> value) {
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
