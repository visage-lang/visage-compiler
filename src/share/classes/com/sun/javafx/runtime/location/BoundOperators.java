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

import com.sun.javafx.runtime.sequence.Sequence;

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

    public static IntLocation ifExpression(final BooleanLocation condition,
                                           IntBindingExpression thenExpr,
                                           IntBindingExpression elseExpr) {
        final IntVariable thenVar = IntVariable.make(true, thenExpr);
        final IntVariable elseVar = IntVariable.make(true, elseExpr);
        return new IndirectIntExpression(false, condition) {
            protected IntLocation computeLocation() {
                clearDynamicDependencies();
                if (condition.get()) {
                    addDynamicDependency(thenVar);
                    return thenVar;
                }
                else {
                    addDynamicDependency(elseVar);
                    return elseVar;
                }
            }
        };
    }

    public static DoubleLocation ifExpression(final BooleanLocation condition,
                                              DoubleBindingExpression thenExpr,
                                              DoubleBindingExpression elseExpr) {
        final DoubleVariable thenVar = DoubleVariable.make(true, thenExpr);
        final DoubleVariable elseVar = DoubleVariable.make(true, elseExpr);
        return new IndirectDoubleExpression(false, condition) {
            protected DoubleLocation computeLocation() {
                clearDynamicDependencies();
                if (condition.get()) {
                    addDynamicDependency(thenVar);
                    return thenVar;
                }
                else {
                    addDynamicDependency(elseVar);
                    return elseVar;
                }
            }
        };
    }

    public static BooleanLocation ifExpression(final BooleanLocation condition,
                                               BooleanBindingExpression thenExpr,
                                               BooleanBindingExpression elseExpr) {
        final BooleanVariable thenVar = BooleanVariable.make(true, thenExpr);
        final BooleanVariable elseVar = BooleanVariable.make(true, elseExpr);
        return new IndirectBooleanExpression(false, condition) {
            protected BooleanLocation computeLocation() {
                clearDynamicDependencies();
                if (condition.get()) {
                    addDynamicDependency(thenVar);
                    return thenVar;
                }
                else {
                    addDynamicDependency(elseVar);
                    return elseVar;
                }
            }
        };
    }

    public static <T> ObjectLocation<T> ifExpression(final BooleanLocation condition,
                                                     ObjectBindingExpression<T> thenExpr,
                                                     ObjectBindingExpression<T> elseExpr) {
        final ObjectVariable<T> thenVar = ObjectVariable.make(true, thenExpr);
        final ObjectVariable<T> elseVar = ObjectVariable.make(true, elseExpr);
        return new IndirectObjectExpression<T>(false, condition) {
            protected ObjectLocation<T> computeLocation() {
                clearDynamicDependencies();
                if (condition.get()) {
                    addDynamicDependency(thenVar);
                    return thenVar;
                }
                else {
                    addDynamicDependency(elseVar);
                    return elseVar;
                }
            }
        };
    }

    public static <T> SequenceLocation<T> ifExpression(Class<T> clazz,
                                                       final BooleanLocation condition,
                                                       SequenceBindingExpression<T> thenExpr,
                                                       SequenceBindingExpression<T> elseExpr) {
        final SequenceVariable<T> thenVar = SequenceVariable.make(clazz, true, thenExpr);
        final SequenceVariable<T> elseVar = SequenceVariable.make(clazz, true, elseExpr);
        return new IndirectSequenceExpression<T>(clazz, false, condition) {
            protected SequenceLocation<T> computeLocation() {
                clearDynamicDependencies();
                if (condition.get()) {
                    addDynamicDependency(thenVar);
                    return thenVar;
                }
                else {
                    addDynamicDependency(elseVar);
                    return elseVar;
                }
            }
        };
    }

}
