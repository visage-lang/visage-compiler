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
 * Indirect locations are used to express bindings that respond nonuniformly with respect to dependencies. For example,
 * bind a.b will change when a changes or when a.b changes, but the response to changes is different. If a.b changes but
 * a doesn't, the response is simply to update the value; if a changes, the response also includes updating the
 * dependency set as well, removing the old a.b and adding the new one.
 * <p/>
 * Indirect location instances are created with a set of static dependencies; in the example above, a would be a static
 * dependency.  Instances override computeLocation(), which returns a Location with its secondary dependencies set up;
 * in the example above, calling computeLocation() would evaluate a, and return a location corresponding to a.b.
 *
 * @author Brian Goetz
 */
public abstract class IndirectSequenceExpression<T> extends SequenceVariable<T> implements IndirectLocation<SequenceLocation<T>> {

    protected final ObjectLocation<SequenceLocation<T>> helper;

    public IndirectSequenceExpression(TypeInfo<T, ?> typeInfo, boolean lazy, Location... dependencies) {
        super(typeInfo);
        helper = IndirectLocationHelper.make(this, lazy, dependencies);
        bind(lazy, helper.get());
        // @@@ Downside of this approach: we get two change events, one when the dependencies change, and another when
        // the rebinding happens.  
        helper.addChangeListener(new ObjectChangeListener<SequenceLocation<T>>() {
            public void onChange(SequenceLocation<T> oldValue, SequenceLocation<T> newValue) {
                rebind(newValue);
            }
        });
    }

    public abstract SequenceLocation<T> computeLocation();

    public SequenceLocation<T> getUnderlyingLocation() {
        return computeLocation();
    }
}
