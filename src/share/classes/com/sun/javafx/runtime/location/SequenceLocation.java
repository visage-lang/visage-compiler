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

/**
 * A sequence-valued Location.  Exposes analogues of the mutative methods from Sequence, which modify the sequence
 * value and notify the appropriate change listeners.  If the change listeners implement the SequenceChangeListener
 * interface, the additional methods in SequenceChangeListener for insert, delete, and update will be called.
 *
 * @author Brian Goetz
 */
public interface SequenceLocation<T> extends Location, Iterable<T> {
    
    T get(int position);

    Sequence<T> get();

    /** Retrieve the previous value of this location; only defined while change listeners are being notified */
    public Sequence<T> getPreviousValue();

    public void addChangeListener(SequenceChangeListener<? super T> listener);

    Sequence<T> set(Sequence<? extends T> value);

    public T set(int position, T value);

    public void delete(int position);

    public void deleteAll();

    public void deleteValue(T value);

    public void delete(SequencePredicate<T> predicate);

    public void insert(T value);

    public void insert(Sequence<? extends T> values);

    public void insertFirst(T value);

    public void insertFirst(Sequence<? extends T> values);

    public void insertBefore(T value, int position);

    public void insertBefore(T value, SequencePredicate<T> predicate);

    public void insertBefore(Sequence<? extends T> values, int position);

    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> predicate);

    public void insertAfter(T value, int position);

    public void insertAfter(T value, SequencePredicate<T> predicate);

    public void insertAfter(Sequence<? extends T> values, int position);

    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> predicate);
}
