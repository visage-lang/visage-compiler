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

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

import java.util.Iterator;

/**
 * SequenceExpression represents an integer-value bound expression.  Associated with an SequenceExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  SequenceExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class SequenceExpression<T> extends AbstractLocation implements SequenceLocation<T> {

    private final SequenceBindingExpression<T> expression;
    private Sequence<T> value, previousValue;

    /** Create an SequenceExpression with the specified expression and dependencies. */
    public static<T> SequenceLocation<T> make(SequenceBindingExpression<T> exp, Location... dependencies) {
        SequenceExpression<T> loc = new SequenceExpression<T>(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    /** Create a lazy SequenceExpression with the specified expression and dependencies. */
    public static<T> SequenceLocation<T> makeLazy(SequenceBindingExpression<T> exp, Location... dependencies) {
        SequenceExpression<T> loc = new SequenceExpression<T>(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    public void addDependencies(Location... dependencies) {
        for (Location dep : dependencies)
            dep.addChangeListener(getWeakChangeListener());
    }

    private SequenceExpression(boolean lazy, SequenceBindingExpression<T> expression) {
        super(false, lazy);
        this.expression = expression;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
        return value.toString();
    }

    @Override
    public Iterator<T> iterator() {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
        return value.iterator();
    }

    @Override
    public T get(int position) {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
        return value.get(position);
    }
    
    @Override
    public Sequence<T> get() {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
        return value;
    }

    public Sequence<T> getPreviousValue() {
        return previousValue;
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = expression.get();
            previousValue = null;
            setValid();
        }
    }

    @Override
    public void invalidate() {
        if (isValid())
            previousValue = value;
        super.invalidate();
    }

    @Override
    public Sequence<T> set(Sequence<T> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteValue(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Sequence<T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(Sequence<T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }
}
