/*
 * Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.runtime.sequence.*;

/**
 * SequenceHistoryListener
 *
 * @author Brian Goetz
 */
public class HistorySequenceListener<T> extends ChangeListener<T> {
    private List<String> elements = new ArrayList<String>();

    public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
        int newElementsSize = Sequences.sizeOfNewElements(buffer, startPos, newElements);
        if (endPos == startPos + 1 && newElementsSize == 1) {
            T oldElement = Sequences.getFromOldValue(buffer, oldValue, startPos, endPos, startPos);
            T newElement = Sequences.getFromNewElements(buffer, startPos, newElements, 0);
            if (oldElement == null) System.err.println("oldElement is null");
            if (newElement == null) System.err.println("newElement is null");
            elements.add(String.format("r-%d-%s-%s", startPos, oldElement.toString(), newElement.toString()));
        }
        else {
            for (int i=endPos; --i >= startPos; ) {
                T oldElement = Sequences.getFromOldValue(buffer, oldValue, startPos, endPos, i);
                elements.add(String.format("d-%d-%s", i, oldElement.toString()));
            }
            for (int i = 0;  i < newElementsSize;  i++) {
                T newElement = Sequences.getFromNewElements(buffer, startPos, newElements, i);
                elements.add(String.format("i-%d-%s", i+startPos, newElement.toString()));
            }
        }
    }

    public List<String> get() { return elements; }

    public void clear() { elements.clear(); }
}
