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
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * CountingSequenceListener
 *
 * @author Brian Goetz
 */
class CountingSequenceListener implements SequenceReplaceListener<Integer> {
    int changeCount, insertCount, deleteCount, replaceCount;
    Sequence<Integer> inserted = Sequences.emptySequence(Integer.class);
    Sequence<Integer> deleted = Sequences.emptySequence(Integer.class);

    public void onReplace(int startPos, int endPos, Sequence<? extends Integer> newElements, Sequence<Integer> oldValue, Sequence<Integer> newValue) {
        if (endPos == startPos && Sequences.size(newElements) == 1) {
            ++replaceCount;
            ++changeCount;
        }
        else {
            for (int i=endPos; i >= startPos; i--) {
                ++deleteCount;
                ++changeCount;
                deleted = deleted.insert(oldValue.get(i));
            }
            for (Integer t : newElements) {
                ++insertCount;
                ++changeCount;
                inserted = inserted.insert(t);
            }
        }
    }
}
