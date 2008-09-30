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
class SliceReplacementSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    
    protected final Sequence<T> sequence;
    protected final int size, depth;

    private final int gapPos;
    private final int gapSize;
    private final Sequence<? extends T> replacementSequence;
    private final int replacementSize;

    public SliceReplacementSequence(Sequence<T> sequence, int gapStartPos, int gapEndPos, Sequence<? extends T> replacementSequence) {
        super(sequence.getElementType());
        this.sequence = sequence;
        final int seqSize = sequence.size();
        gapStartPos = Math.min (Math.max (0, gapStartPos), seqSize);          // 0 <= gapStartPos <= size
        gapEndPos = Math.min (Math.max (gapStartPos, gapEndPos), seqSize);    // gapStartPos <= gapEndPos <= size
        this.gapPos = gapStartPos;
        this.gapSize = Math.min (sequence.size(), gapEndPos - gapStartPos);
        this.replacementSequence = replacementSequence;
        this.replacementSize = replacementSequence.size();
        this.size = sequence.size() + replacementSize - gapSize;
        this.depth = sequence.getDepth() + 1;
    }

    public int size() {
        return size;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    public T get(int position) {
        if (position < gapPos) {
            return sequence.get(position);
        }
        int pos = position - gapPos;
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

        int lengthFirstSeq = 0;
        if (sourceOffset < gapPos) {
            lengthFirstSeq = Math.min(gapPos-sourceOffset, length);
            sequence.toArray(sourceOffset, lengthFirstSeq, dest, destOffset);
        }
        int lengthReplacement = 0;
        if (replacementSize > 0 && sourceOffset < gapPos + replacementSize && sourceOffset + length > gapPos) {
            int startReplacement = Math.max(0, sourceOffset-gapPos);
            lengthReplacement = Math.min(replacementSize-startReplacement, length-lengthFirstSeq);
            replacementSequence.toArray(startReplacement, lengthReplacement, dest, destOffset + lengthFirstSeq);
        }
        if (sourceOffset+length > gapPos+replacementSize) {
            int startSecondSeq = gapPos + gapSize + Math.max(0, sourceOffset - gapPos - replacementSize);
            int lengthSecondSeq = length - lengthFirstSeq - lengthReplacement;
            sequence.toArray(startSecondSeq, lengthSecondSeq, dest, destOffset + lengthFirstSeq + lengthReplacement);
        }
    }
}
