/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * SequenceHistoryListener
 *
 * @author Brian Goetz
 */
public class HistorySequenceListener<T> implements SequenceChangeListener<T> {
    private List<String> elements = new ArrayList<String>();

    public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
        if (endPos == startPos && Sequences.size(newElements) == 1) {
            elements.add(String.format("r-%d-%s-%s", startPos, oldValue.get(startPos).toString(), newValue.get(startPos).toString()));
        }
        else {
            for (int i=endPos; i >= startPos; i--) {
                elements.add(String.format("d-%d-%s", i, oldValue.get(i).toString()));
            }
            int index = startPos;
            for (T t : newElements) {
                elements.add(String.format("i-%d-%s", index, t.toString()));
                index++;
            }
        }
    }

    public List<String> get() { return elements; }

    public void clear() { elements.clear(); }
}
