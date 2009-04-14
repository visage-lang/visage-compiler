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
import com.sun.javafx.runtime.TypeInfo;

/**
 * @author Brian Goetz
 */
public class BoundOperators {

    // non-instantiable
    private BoundOperators() { }

    @SuppressWarnings("unchecked")
    public static<T, L extends ObjectLocation<T>> L makeBoundIf(final TypeInfo<T, L> typeInfo,
                                                                boolean lazy,
                                                                final BooleanLocation conditional,
                                                                final ObjectLocation<T> thenLoc,
                                                                final ObjectLocation<T> elseLoc) {
        final L loc = typeInfo.makeLocation();
        ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
            @Override
            public void onChange(boolean oldValue, boolean newValue) {
                loc.clearDynamicDependencies();
                ObjectLocation<T> newLoc = conditional.getAsBoolean() ? thenLoc : elseLoc;
                loc.addDynamicDependency(newLoc);
                ((AbstractLocation) loc).setUnderlyingLocation(newLoc);
            }
        };
        conditional.addChangeListener(listener);
        listener.onChange(!conditional.getAsBoolean(), conditional.getAsBoolean());

        BindingExpression bindingExpression = new AbstractBindingExpression() {
            public void compute() {
                pushFrom(typeInfo, conditional.getAsBoolean() ? thenLoc : elseLoc);
            }
        };
        ((BindableLocation<T, ?>) loc).bind(lazy, bindingExpression, conditional);
        return loc;
    }

    // @@@ This can go away once we switch to the makeBoundIf(TypeInfo, ...) version vvv
    private static<T> BindingExpression wrap(final Function0<SequenceLocation<T>> fun) {
        return new AbstractBindingExpression() {
            @Override
            public void compute() {
                pushValue(fun.invoke());
            }
        };
    }

    public static<T> SequenceLocation<T> makeBoundIf(TypeInfo<T, ?> typeInfo,
                                                     boolean lazy,
                                                     final BooleanLocation conditional,
                                                     final Function0<SequenceLocation<T>> thenBranch,
                                                     final Function0<SequenceLocation<T>> elseBranch) {
        return makeBoundSequenceIf(typeInfo, lazy, conditional, wrap(thenBranch), wrap(elseBranch));
    }

    // @@@ This can go away once we switch to the makeBoundIf(TypeInfo, ...) version ^^^
    public static<T> SequenceLocation<T> makeBoundSequenceIf(TypeInfo<T, ?> typeInfo,
                                                             boolean lazy,
                                                             final BooleanLocation conditional,
                                                             final BindingExpression thenBranch,
                                                             final BindingExpression elseBranch) {
        BindingExpression bindingExpression = new AbstractBindingExpression() {
            @Override
            public void compute() {
                if (thenBranch.getLocation() == null) {
                    // First-time setup
                    thenBranch.setLocation(location);
                    elseBranch.setLocation(location);
                }

                if (conditional.getAsBoolean())
                    thenBranch.compute();
                else
                    elseBranch.compute();
            }
        };
        return makeIndirectSequenceLocation(typeInfo, lazy, bindingExpression, conditional);
    }

    private static <T, L extends ObjectLocation<T>> BindingExpression makeSelectBindingExpression(final ObjectLocation<?> receiver, final BindingExpression selector, final L defaultConstant) {
        return new AbstractBindingExpression() {
            public void compute() {
                if (selector.getLocation() == null) {
                    // First-time setup
                    selector.setLocation(location);
                }

                if (receiver.get() != null)
                    selector.compute();
                else {
                    pushValue(defaultConstant);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static<T, L extends ObjectLocation<T>> L makeBoundSelect(final TypeInfo<T, L> typeInfo,
                                                                    boolean lazy,
                                                                    final SBECL selector) {
        final ObjectLocation<?> receiver = (ObjectLocation<?>) selector.arg0();
        final L loc = typeInfo.makeLocation();
        final L defaultConstant = typeInfo.makeDefaultConstant();
        final ObjectVariable<L> lastADotB = makeIndirectHelper(lazy, loc, makeSelectBindingExpression(receiver, selector, defaultConstant),
                                                               defaultConstant, receiver);
        ((BindableLocation<T, ?>) loc).bind(lazy, makeBindingExpression(typeInfo, lastADotB));
        return loc;
    }

    public static<T, U> SequenceLocation<U> makeBoundSequenceSelect(final TypeInfo<U, ?> typeInfo,
                                                                    boolean lazy,
                                                                    final SBECL selector) {
        final ObjectLocation<?> receiver = (ObjectLocation<?>) selector.arg0();
        SequenceLocation<U> defaultValue = SequenceConstant.<U>make(typeInfo, typeInfo.emptySequence);
        return makeIndirectSequenceLocation(typeInfo, lazy, makeSelectBindingExpression(receiver, selector, defaultValue), receiver);
    }

    /**
     * Helper methods for indirect locations; maintains separate dependency paths for the static dependencies (passed into
     * the constructor) and the dynamic dependencies (embodied in the returned location from computeLocation()).  All
     * subclasses need to do is provide the computeLocation() method.
     */

    private static<T, L extends ObjectLocation<T>>
    ObjectVariable<L> makeIndirectHelper(boolean lazy, final L helpedLocation, BindingExpression binding, L defaultLocationValue, Location... dependencies) {
        final ObjectVariable<L> helper = ObjectVariable.make(defaultLocationValue, lazy, binding, dependencies);
        helpedLocation.addDependency(helper);
        if (!lazy) {
            L initialValue = helper.get();
            helpedLocation.addDynamicDependency(initialValue);
            ((AbstractLocation) helpedLocation).setUnderlyingLocation(initialValue);
        }
        helper.addChangeListener(new ChangeListener<L>() {
            @Override
            public void onChange(L oldLoc, L newLoc) {
                helpedLocation.clearDynamicDependencies();
                helpedLocation.addDynamicDependency(newLoc);
                ((AbstractLocation) helpedLocation).setUnderlyingLocation(newLoc);
            }
        });
        return helper;
    }

    private static<V, T extends ObjectLocation<V>> BindingExpression makeBindingExpression(final TypeInfo<V, ?> ti, final ObjectLocation<T> helper) {
        return new AbstractBindingExpression() {
            public void compute() {
                pushFrom(ti, helper.get());
            }
        };
    }

    private static<T> SequenceLocation<T> makeIndirectSequenceLocation(final TypeInfo<T, ?> typeInfo,
                                                                      final boolean lazy,
                                                                      final BindingExpression binding,
                                                                      final Location... dependencies) {
        // The approach for sequences is different because we need to use actual binding, not just triggers, otherwise
        // the sequences triggers won't flow through the intermediate nodes correctly.
        return new SequenceVariable<T>(typeInfo) {
            ObjectLocation<SequenceLocation<T>> helper;
            {
                // Helper is deliberately not lazy -- because we need to get change events
                helper = makeIndirectHelper(false, this, binding, new SequenceConstant<T>(typeInfo, typeInfo.emptySequence), dependencies);
                bind(lazy, helper.get());
                // @@@ Downside of this approach: we get two change events, one when the dependencies change, and another when
                // the rebinding happens.
                helper.addChangeListener(new ChangeListener<SequenceLocation<T>>() {
                    @Override
                    public void onChange(SequenceLocation<T> oldValue, SequenceLocation<T> newValue) {
                        rebind(lazy, newValue);
                    }
                });
            }
        };
    }
}
