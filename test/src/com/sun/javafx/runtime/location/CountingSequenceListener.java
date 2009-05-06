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

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.*;

/**
 * CountingSequenceListener
 *
 * @author Brian Goetz
 */
class CountingSequenceListener extends ChangeListener<Integer> {
    int changeCount, insertCount, deleteCount, replaceCount;
    Sequence<? extends Integer> inserted = TypeInfo.Integer.emptySequence;
    Sequence<? extends Integer> deleted = TypeInfo.Integer.emptySequence;

    public void onChange(ArraySequence<Integer> buffer, Sequence<? extends Integer> oldValue, int startPos, int endPos, Sequence<? extends Integer> newElements) {
        int newElementsSize = Sequences.sizeOfNewElements(buffer, startPos, newElements);
        if (endPos == startPos + 1 && newElementsSize == 1) {
            ++replaceCount;
            ++changeCount;
        }
        else {
            for (int i=endPos; --i >= startPos; ) {
                ++deleteCount;
                ++changeCount;
                Integer old = Sequences.getFromOldValue(buffer, oldValue, startPos, endPos, i);
                deleted = Sequences.insert(TypeInfo.Integer, deleted, old);
            }
            for (int i = 0;  i < newElementsSize;  i++) {
                Integer newElement = Sequences.getFromNewElements(buffer, startPos, newElements, i);
                ++insertCount;
                ++changeCount;
                inserted = Sequences.insert(TypeInfo.Integer, inserted, newElement);
            }
        }
    }
}
