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
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.sequence.*;

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

    public static<T> SequenceLocation<T> makeBoundIf(Class<T> clazz,
                                                     boolean lazy,
                                                     final BooleanLocation conditional,
                                                     final Function0<SequenceLocation<T>> thenBranch,
                                                     final Function0<SequenceLocation<T>> elseBranch) {
        return new IndirectSequenceExpression<T>(clazz, lazy, conditional) {
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

    public static<T, U> ObjectLocation<U> makeBoundSelect(final Class clazz,
                                                          boolean lazy,
                                                          final ObjectLocation<T> receiver,
                                                          final Function1<ObjectLocation<U>, T> selector) {
        return new IndirectObjectExpression<U>(lazy, receiver) {

            public ObjectLocation<U> computeLocation() {
                T selectorValue = receiver.get();
                return selectorValue == null ? ObjectConstant.make(Util.<U>defaultValue(clazz)) : selector.invoke(selectorValue);
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

    public static<T, U> SequenceLocation<U> makeBoundSelect(final Class<U> clazz,
                                                            boolean lazy,
                                                            final ObjectLocation<T> receiver,
                                                            final Function1<SequenceLocation<U>, T> selector) {

        final SequenceLocation<U> defaultValue = SequenceConstant.<U>make(clazz, Sequences.emptySequence(clazz));

        return new IndirectSequenceExpression<U>(clazz, lazy, receiver) {

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
