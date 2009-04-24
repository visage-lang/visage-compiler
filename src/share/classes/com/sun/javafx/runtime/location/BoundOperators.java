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

        class ArmInvalidationListener extends InvalidationListener {
            private final ObjectLocation<T> branch;

            ArmInvalidationListener(ObjectLocation<T> branch) {
                this.branch = branch;
            }

            @Override
            public boolean onChange() {
                if (conditional.getAsBoolean()) {
                    if (branch == thenLoc)
                        loc.invalidate();
                }
                else {
                    if (branch == elseLoc)
                        loc.invalidate();
                }
                return true;
            }
        }

        BindingExpression bindingExpression = new AbstractBindingExpression() {
            public void compute() {
                pushFrom(typeInfo, conditional.getAsBoolean() ? thenLoc : elseLoc);
            }
        };
        ((BindableLocation<T, ?>) loc).bind(lazy, bindingExpression, conditional);

        elseLoc.addInvalidationListener(new ArmInvalidationListener(elseLoc));
        thenLoc.addInvalidationListener(new ArmInvalidationListener(thenLoc));

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
        final ObjectVariable<L> lastAdotBLoc = makeIndirectHelper(lazy, loc, makeSelectBindingExpression(receiver, selector, defaultConstant),
                                                                  defaultConstant);
        ((BindableLocation<T, ?>) loc).bind(lazy, new AbstractBindingExpression() {
            public void compute() {
                pushFrom(typeInfo, lastAdotBLoc.get());
            }
        });
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

        class IndirectInvalidationListener extends InvalidationListener {
            private final L attachedTo;

            IndirectInvalidationListener(L attachedTo) {
                this.attachedTo = attachedTo;
            }

            @Override
            public boolean onChange() {
                if (helper.get() == attachedTo)
                    helpedLocation.invalidate();
                return true;
            }
        }

        ChangeListener<L> changeListener = new ChangeListener<L>() {
            IndirectInvalidationListener listener;

            @Override
            public void onChange(L oldLoc, L newLoc) {
                if (listener != null)
                    listener.attachedTo.removeInvalidationListener(listener);
                listener = new IndirectInvalidationListener(newLoc);
                newLoc.addInvalidationListener(listener);
                ((AbstractLocation) helpedLocation).setUnderlyingLocation(newLoc);
            }
        };
        helper.addChangeListener(changeListener);
        if (!lazy)
            changeListener.onChange(null, helper.get());
        return helper;
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
