/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.TypeInfo;
import java.lang.ref.WeakReference;

/**
 * @author Brian Goetz
 */
public class Locations {

    // non-instantiable
    private Locations() { }

    @SuppressWarnings("unchecked")
    public static<T, L extends ObjectLocation<T>> L makeBoundIf(final TypeInfo<T, L> typeInfo,
                                                                boolean lazy,
                                                                final BooleanLocation conditional,
                                                                final ObjectLocation<T> thenLoc,
                                                                final ObjectLocation<T> elseLoc) {
        final L loc = typeInfo.makeLocation();
        ((BindableLocation<T, ?>) loc).bind(lazy, makeBoundIfBE(typeInfo, lazy, conditional, thenLoc, elseLoc));
        return loc;
    }

    public static<T, L extends ObjectLocation<T>> BindingExpression makeBoundIfBE(
            final TypeInfo<T, L> typeInfo, 
            final boolean lazy, final BooleanLocation conditional,
            final ObjectLocation<T> thenLoc, final ObjectLocation<T> elseLoc) {
        return new AbstractBindingExpression() {
            StaticDependentLocation weakMe;
            ObjectLocation<T> lastArm;

            @Override
            public void setLocation(Location location) {
                super.setLocation(location);
                addStaticDependent(conditional);
                weakMe = new StaticDependentLocation(location);
            }

            @Override
            public void compute() {
                boolean c = conditional.getAsBoolean();
                ObjectLocation<T> thisArm = c ? thenLoc : elseLoc;
                if (thisArm != lastArm) {
                    if (lastArm != null && lastArm instanceof AbstractLocation) {
                        ((AbstractLocation) lastArm).removeChild(weakMe);
                    }
                    if (thisArm instanceof AbstractLocation)
                        ((AbstractLocation) thisArm).addChild(weakMe);
                    lastArm = thisArm;
                }
                pushFrom(typeInfo, thisArm);
            }
        };
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

   public static<T, L extends ObjectLocation<T>> L makeBoundSelect(final TypeInfo<T, L> typeInfo,
                                                                                   final ObjectLocation<? extends FXObject> receiver,
                                                                                   final int varNum) {
       final L loc = typeInfo.makeLocation();
       ((BindableLocation<T, ?>) loc).bind(false, makeBoundSelectBE(typeInfo, receiver, varNum));
       return loc;
    }

   public static<T, L extends ObjectLocation<T>, R extends FXObject> BindingExpression makeBoundSelectBE(final TypeInfo<T, L> typeInfo,
                                                                                   final ObjectLocation<R> receiver,
                                                                                   final int varNum) {
       return new AbstractBindingExpression() {
           L lastL = null;
           StaticDependentLocation weakMe;

           @Override
           public void setLocation(Location location) {
               super.setLocation(location);
               addStaticDependent(receiver);
               weakMe = new StaticDependentLocation(location);
           }

           public void compute() {
               R rcvr = receiver.get();
               L thisL = rcvr==null? 
                   typeInfo.makeDefaultConstant() : 
                   (L) rcvr.loc$(varNum);
               if (thisL != lastL) {
                   if (lastL != null && lastL instanceof AbstractLocation)
                       ((AbstractLocation) lastL).removeChild(weakMe);
                   if (thisL instanceof AbstractLocation)
                       ((AbstractLocation) thisL).addChild(weakMe);
                   lastL = thisL;
               }
               pushFrom(typeInfo, thisL);
           }
       };
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
        helpedLocation.addDependency(dependencies);
        if (!lazy) {
            L initialValue = helper.get();
            helpedLocation.addDynamicDependency(initialValue);
        }
        helper.addChangeListener(new IndirectChangeListener<T, L>(helpedLocation));
        return helper;
    }

    private static class IndirectChangeListener<T, L extends ObjectLocation<T>> extends ChangeListener<L> {
        private final WeakReference<L> helpedLocationHolder;

        public IndirectChangeListener(L helpedLocation) {
            this.helpedLocationHolder = new WeakReference<L>(helpedLocation);
        }

        @Override
        public void onChange(L oldLoc, L newLoc) {
            L helpedLocation = helpedLocationHolder.get();
            if (helpedLocation != null) {
                helpedLocation.clearDynamicDependencies();
                helpedLocation.addDynamicDependency(newLoc);
            }
        }
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

    /**
     * Return a wrapping location that mirrors the underlying Location but upcasts its type
     * @param clazz  Really Class<V> but because of generics limitations this cannot be declared
     * @param loc Location to upcast
     * @return
     */
    @SuppressWarnings("unchecked")
    public static<T, V extends T> ObjectLocation<T> upcast(TypeInfo<V, ?> typeInfo, ObjectLocation<V> loc) {
        return (ObjectLocation<T>)(loc);
    }

    // Used outside compiler - in GUI code. Once we design
    // an alternative for them, we can remove this method.
    public static boolean hasDependencies(Location loc) {
	 return ((AbstractVariable) loc).hasDependencies();
    }
}
