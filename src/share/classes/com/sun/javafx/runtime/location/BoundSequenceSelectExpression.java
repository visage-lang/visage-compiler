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

import  com.sun.javafx.runtime.sequence.Sequences;

/**
 * BoundSequenceSelectExpression
 *
 * @author Robert Field
 */
public abstract class BoundSequenceSelectExpression<T, U> extends IndirectSequenceExpression<T> implements SequenceLocation<T> {
    private final ObjectLocation<U> selector;
    private final SequenceLocation<T> defaultValue;

    public BoundSequenceSelectExpression(Class<T> clazz, ObjectLocation<U> selector) {
        super(clazz, false/*lazy*/, selector);
        this.selector = selector;
        this.defaultValue = SequenceConstant.<T>make(Sequences.emptySequence(clazz));
    }

    protected abstract SequenceLocation<T> computeSelect(U selectorValue);

    protected SequenceLocation<T> computeLocation() {
        clearDynamicDependencies();
        U selectorValue = selector.get();
        if (selectorValue == null) {
            return defaultValue;
        } else {
            SequenceLocation<T> result = computeSelect(selectorValue);
            addDynamicDependency(result);
            return result;
        }
    }
}
