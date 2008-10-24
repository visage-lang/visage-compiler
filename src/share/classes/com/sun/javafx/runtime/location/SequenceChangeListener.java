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

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.util.Linkable;

/**
 * Sequence-specific notification methods 
 *
 * @author Brian Goetz
 */
public abstract class SequenceChangeListener<T> implements Linkable<SequenceChangeListener<T>, AbstractVariable> {
    private SequenceChangeListener<T> next;
    private AbstractVariable host;

    public SequenceChangeListener<T> getNext() {
        return next;
    }

    public void setNext(SequenceChangeListener<T> next) {
        this.next = next;
    }

    public AbstractVariable getHost() {
        return host;
    }

    public void setHost(AbstractVariable host) {
        this.host = host;
    }

    public abstract void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue);
}
