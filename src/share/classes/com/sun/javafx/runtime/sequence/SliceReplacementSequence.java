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

package com.sun.javafx.runtime.sequence;

/**
 * SliceReplacementSequence
 *
 * @author Michael Heinrichs
 */
class SliceReplacementSequence<T> extends DerivedSequence<T> implements Sequence<T> {
    
    private final int gapPos;
    private final int gapSize;
    private final Sequence<? extends T> replacementSequence;

    SliceReplacementSequence(Sequence<T> sequence, int gapStartPos, int gapEndPos, Sequence<? extends T> replacementSequence) {
        super(sequence.getElementType(), sequence, Sequences.size(sequence) + Sequences.size(replacementSequence) - (gapEndPos - gapStartPos), Math.max(sequence.getDepth(), replacementSequence.getDepth()) + 1);
        this.gapPos = gapStartPos;
        this.gapSize = gapEndPos - gapStartPos;
        this.replacementSequence = replacementSequence;
    }

    public T get(int position) {
        if (position < gapPos) {
            return sequence.get(position);
        }
        final int replacementSize = Sequences.size(replacementSequence);
        final int pos = position - gapPos;
        if (pos < replacementSize) {
            return replacementSequence.get(pos);
        } else {
            return sequence.get(position + gapSize - replacementSize);
        }
    }

    @Override
    public void toArray(int sourceOffset, int length, Object[] dest, int destOffset) {
        if (sourceOffset < 0 || (length > 0 && sourceOffset + length > size))
            throw new ArrayIndexOutOfBoundsException();
        if (sourceOffset < gapPos) {
            int lengthFirstSeq = Math.min(gapPos-sourceOffset, length);
            sequence.toArray(sourceOffset, lengthFirstSeq, dest, destOffset);
            destOffset += lengthFirstSeq;
            length -= lengthFirstSeq;
            sourceOffset += lengthFirstSeq;
        }
        final int replacementSize = Sequences.size(replacementSequence);
        int lengthReplacement = 0;
        int startReplacement = sourceOffset-gapPos;
        if (startReplacement >= 0 && startReplacement < replacementSize) {
            lengthReplacement = Math.min(replacementSize-startReplacement, length);
            replacementSequence.toArray(startReplacement, lengthReplacement, dest, destOffset);
            destOffset += lengthReplacement;
            length -= lengthReplacement;
        }

        if (length > 0) {
            int startSecondSeq = gapSize + Math.max(gapPos, sourceOffset - replacementSize);
            sequence.toArray(startSecondSeq, length, dest, destOffset);
        }
    }
}
