/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.javafx.runtime.FXObject;

/**
 * Helper class used for implementing bound for-comprehensions.
 *
 * We compile:
 * def y = bind for (iv in st) {
 *   def vv = zorp(indexof iv);
 *   foo(iv, ext, vv)
 * }
 * to:
 *
 * final BoundForHelper bfhelper$y = BoundForHelper.make(this, VOFF$y, true);
 * public ForPart<T> makeForPart$y(final int index) {
 *     return new ForPart<T>() {
 *         // as bfElem in the design document:
 *         def outer;
 *         var iv;
 *         var indexof;
 *         def vv = bind zorp(indexof);
 *         def result = bind foo(iv, outer.ext);
 *         invalidate$result(int begin, int end, int newLen, int phase) {
 *             updateForPart(indexof, begin, end, newLen, phase);
 *         }
 *         public Sequence<T> getResult() { return result; }
 *         public T get(int index) { return result.get(index); }
 *         public int size() { return result.size(); }
 *         public void adjustIndex(int delta) {
 *             indexof += delta;
 *         }
 *     };
 * }
 * public T get$y(int i) { return bfhelper$y,get(i); }
 * public int size$y() { return bfhelper$y.size(); }
 * public ForPart<?> makeForPart$(int varNum, int indexOf) {
 *     switch (varNum) {
 *         case VOFF$y: retrun makeForPart$y(indexOf);
 *     }
 * }
 * void invalidate$st(int begin, int end, int newLen, int phase) {
 *    if (phase == ???)
 *        bfhelper$y.replaceParts(begin, end, newLen);
 * }
 *
 * @author Per Bothner
 */
public abstract class BoundForHelper<T> {

    /** The bfElem class in the design document implements this interface. */
    public static interface ForPart<T> extends FXObject {
        /** Get the result variable as a Sequence. */
        public Sequence<T> getResult();
        /** Equivalent to getResult().get(index) but may be more efficient. */
        public T get(int index);
        /** Equivalent to getResult().size() but may be more efficient. */
        public int size();
        /** Adjust the indexof variable by delta.
         * May cause re-calculation.
         * This may cause size() to change - what then?  FIXME.
         */
        public void adjustIndex(int delta);
    };

    // cumulatedLengths[i] = size(i)+cumulatedLengths[i-1];
    // Inariant: cumulatedLengths.length == elements.length;
    int[] cumulatedLengths;
    ForPart<T>[] elements;
    int numParts;
    FXObject container;
    int varNum;

    // Invariant: numParts == elements.length - (gapEnd - gapStart).
    int gapStart, gapEnd;
    // Amount to add to cumulatedLengths[i] for i>=gapEnd.
    int postGapExtra;

    int cacheIndex;
    int cachePart;
    public boolean dependsOnIndex = true;

    public BoundForHelper(FXObject container, int varNum, boolean dependsOnIndex) {
        this.container = container;
        this.varNum = varNum;
        this.dependsOnIndex = dependsOnIndex;
    }

    public int size() {
        // cumLength handles the part==-1 case.
        return cumLength(numParts-1);
    }

    /** cumLength(i) is the cached summed size of parts 0..i (inclusive). */
    int cumLength(int part) {
        if (part < gapStart)
            return part < 0 ? 0 : cumulatedLengths[part];
        part += gapEnd - gapStart;
        return cumulatedLengths[part] + postGapExtra;
    }

    protected ForPart<T> getPart(int part) {
        if (part < gapStart)
            return elements[part];
        part += gapEnd - gapStart;
        return elements[part];
    }

    /** Get the size of part ipart. */
    protected int size(int ipart) {
        return getPart(ipart).size();
    }

    /** Get the j'th item of part ipart. */
    protected T get(int ipart, int j) {
        return getPart(ipart).get(j);
    }

    // Called by invalidate when the result of part[ipart] changes.
    public void updateForPart(int ipart, int begin, int end, int newLen, int phase) {
        if (phase != FXObject.VFLGS$IS_INVALID) // FIXME - is this right?
            return;
        int len = cumulatedLengths.length;
        int delta = newLen - begin + end;
        if (ipart > gapStart)
            ipart += gapEnd - gapStart;
        for (int i = 0;  i < len;  i++) {
            if (i == gapStart) {
                postGapExtra += delta;
                return;
            }
            cumulatedLengths[i] += delta;
        }
    }

    // Called by invalidate when the input sequence changes.
    public void replaceParts(int startPart, int endPart, int insertedParts) {
        int removedParts = endPart - startPart;
        int deltaParts = insertedParts - removedParts;
        int gapDelta = startPart - gapStart; // distance we need to move gap

        // Make sure the cumulatedLengths and elements arrays are big enough.
        if (cumulatedLengths == null) {
            int newLength = insertedParts+10;
            cumulatedLengths = new int[newLength];
            elements = new ForPart[newLength];
            gapStart = 0;
            gapEnd = cumulatedLengths.length;
        } else if (numParts + deltaParts > cumulatedLengths.length) {
            // We could be smarter in reducing copying when gapDelta!= 0,
            // but it's probably more complicated than worth doing.
            // See gapReserve in ArraySequence for what is needed.
            int oldLength = cumulatedLengths.length;
            int newLength = numParts + deltaParts + oldLength;
            int[] tmpL = new int[newLength];
            ForPart<T>[] tmpP = (ForPart<T>[]) new ForPart<?>[newLength];
            int postGap = oldLength - gapEnd;
            System.arraycopy(cumulatedLengths, 0, tmpL, 0, gapStart);
            System.arraycopy(cumulatedLengths, oldLength-postGap, tmpL, newLength-postGap, postGap);
            System.arraycopy(cumulatedLengths, 0, tmpP, 0, gapStart);
            System.arraycopy(cumulatedLengths, oldLength-postGap, tmpP, newLength-postGap, postGap);
            cumulatedLengths = tmpL;
            elements = tmpP;
            gapEnd = newLength - postGap;
        }

        // Adjust the gap so gapStart==startPart.
        if (gapDelta > 0) {
            for (int i = 0;  i < gapDelta;  i++) {
                cumulatedLengths[i+gapStart] = cumulatedLengths[i+gapEnd] + postGapExtra;
                elements[i+gapStart] = elements[i+gapEnd];
                elements[i+gapEnd] = null; // to avoid a memory leak.
            }
        } else if (gapDelta < 0) {
            for (int i = -gapDelta;  --i >= 0;) {
                cumulatedLengths[gapEnd+i+gapDelta] = cumulatedLengths[i+startPart] - postGapExtra;
                elements[gapEnd+i+gapDelta] = elements[i+startPart];
                elements[i+startPart] = null; // to avoid a memory leak.
            }
        }
        gapStart += gapDelta;
        gapEnd += gapDelta;

        // Evaluate and insert new parts.
        int itemsBeforeChange = cumLength(startPart-1);
        int removedItems = cumLength(endPart-1) - itemsBeforeChange;
        int cumulate = itemsBeforeChange;
        for (int i = 0;  i < insertedParts;  i++) {
            int index = startPart+i;
            elements[index] = makeForPart$(index);
            cumulate += size(index);
            cumulatedLengths[index] = cumulate;
        }
        int insertedItems = cumulate - itemsBeforeChange;
        gapStart += insertedParts;
        gapEnd -= removedParts;
        postGapExtra += insertedItems - removedItems;
        numParts += deltaParts;
        // If need by adjust indexof for trailing parts.
        if (dependsOnIndex && deltaParts != 0) {
            int end = elements.length;
            for (int i = gapEnd;  i < end;  i++) {
                // Note the following might cause the part.result to be
                // re-calculated, hence re-sized, hence modifying cumulatedLengths.
                elements[i].adjustIndex(deltaParts);
            }
        }
    }

    public abstract ForPart<T> makeForPart$(int index);
    /*{
        return (ForPart<T>) container.makeForPart$(varNum, index);
    }*/

    public T get(int index) {
        // FIXME - should use binary search if not in cache.
        int i = index >= cacheIndex ? cachePart : 0;
        int cumPrev = 0;
        for (;;) {
            if (i >= numParts)
                return null;
            int cum = cumLength(i);
            if (index < cum) {
                cachePart = i;
                cacheIndex = cumPrev;
                return get(i, index-cumPrev);
            }
            cumPrev = cum;
        }
    }

}

