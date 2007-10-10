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

package com.sun.javafx.runtime.sequence;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an empty sequence of a specific type.  Empty sequences are cached using the "Flyweight" pattern.
 * To construct an empty sequence, use the Sequences.emptySequence() factory, not the EmptySequence constructor.
 * O(1) space and time construction costs.  
 *
 * @author Brian Goetz
 */
class EmptySequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private static final Map<Class<?>, Sequence<?>> map = new HashMap<Class<?>, Sequence<?>>();

    public EmptySequence(Class<T> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    public static<T> Sequence<T> get(Class<T> clazz) {
        Sequence<?> e = map.get(clazz);
        if (e == null) {
            e = new EmptySequence<T>(clazz);
            map.put(clazz, e);
        }
        return (Sequence<T>) e;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public T get(int position) {
        return nullValue;
    }
}
