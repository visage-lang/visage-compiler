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

import com.sun.javafx.runtime.TypeInfo;

/**
 * Helper classes for indirect locations; maintains separate dependency paths for the static dependencies (passed into
 * the constructor) and the dynamic dependencies (embodied in the returned location from computeLocation()).  All
 * subclasses need to do is provide the computeLocation() method.
 *
 * @author Brian Goetz
 */
public class IndirectLocationHelper {
    public static<T, L extends ObjectLocation<T>>
    ObjectVariable<L> makeIndirectHelper(boolean lazy, final L helpedLocation, BindingExpression binding, L defaultLocationValue, Location... dependencies) {
        final ObjectVariable<L> helper = ObjectVariable.make(defaultLocationValue, lazy, binding, dependencies);
        helpedLocation.addDependency(helper);
        if (!lazy) {
            L initialValue = helper.get();
            helpedLocation.addDynamicDependency(initialValue);
            ((AbstractLocation) helpedLocation).setUnderlyingLocation(initialValue);
        }
        helper.addChangeListener(new ObjectChangeListener<L>() {
            public void onChange(L oldLoc, L newLoc) {
                helpedLocation.clearDynamicDependencies();
                helpedLocation.addDynamicDependency(newLoc);
                ((AbstractLocation) helpedLocation).setUnderlyingLocation(newLoc);
            }
        });
        return helper;
    }

    public static<V, T extends ObjectLocation<V>> BindingExpression makeBindingExpression(final TypeInfo<V, ?> ti, final ObjectLocation<T> helper) {
        return new BindingExpression() {
            public void compute() {
                pushFrom(ti, helper.get());
            }
        };
    }

    public static<T> SequenceLocation<T> makeIndirectSequenceLocation(final TypeInfo<T, ?> typeInfo,
                                                                      final boolean lazy,
                                                                      final BindingExpression binding,
                                                                      final Location... dependencies) {
        // The approach for sequences is different because we need to use actual binding, not just triggers, otherwise
        // the sequences triggers won't flow through the intermediate nodes correctly.
        return new SequenceVariable<T>(typeInfo) {
            ObjectLocation<SequenceLocation<T>> helper;
            {
                helper = makeIndirectHelper(lazy, this, binding, new SequenceConstant<T>(typeInfo, typeInfo.emptySequence), dependencies);
                bind(lazy, helper.get());
                // @@@ Downside of this approach: we get two change events, one when the dependencies change, and another when
                // the rebinding happens.
                helper.addChangeListener(new ObjectChangeListener<SequenceLocation<T>>() {
                    public void onChange(SequenceLocation<T> oldValue, SequenceLocation<T> newValue) {
                        rebind(newValue);
                    }
                });
            }
        };
    }
}
