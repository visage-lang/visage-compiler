/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.Iterator;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * SequenceExpression represents an integer-value bound expression.  Associated with an SequenceExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  SequenceExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public abstract class SequenceExpression<T> extends AbstractSequenceLocation<T>
        implements SequenceLocation<T>, BindableLocation<SequenceBindingExpression<T>>, SequenceBindingExpression<T> {

    protected SequenceBindingExpression<T> binding;
    protected boolean isLazy;

    public SequenceExpression(Class<T> clazz, boolean lazy, Location... dependencies) {
        super(clazz, false);
        bind(this, lazy);
        addDependencies(dependencies);
    }

    /** Calculate the current value of the expression */
    public abstract Sequence<? extends T> computeValue();

    @Override
    public void update() {
        if (!isValid()) 
            replaceValue(Sequences.upcast(clazz, computeValue()));
    }

    private void ensureValid() {
        if (!isValid())
            update();
    }

    @Override
    public String toString() {
        ensureValid();
        return super.toString();
    }

    @Override
    public Iterator<T> iterator() {
        ensureValid();
        return super.iterator();
    }

    @Override
    public T get(int position) {
        ensureValid();
        return super.get(position);
    }

    @Override
    public Sequence<T> getAsSequence() {
        ensureValid();
        return super.getAsSequence();
    }

    public void bind(SequenceBindingExpression<T> binding, boolean lazy) {
        if (isBound())
            throw new IllegalStateException("Cannot rebind variable");
        this.binding = binding;
        isLazy = lazy;
    }

    public boolean isBound() {
        return binding != null;
    }

    public boolean isLazy() {
        return isLazy;
    }

}
