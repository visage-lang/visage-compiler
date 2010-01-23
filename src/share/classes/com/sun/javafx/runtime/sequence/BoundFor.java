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
import com.sun.javafx.runtime.FXBase;
import com.sun.javafx.runtime.FXObject;

/**
 *
 * @param <T> Result element type
 * @param <PT> Induction type
 */
public abstract class BoundFor<T, PT> extends FXBase {

    /** 
     * The bfElem class in the design document implements this interface.
     */
    public static interface FXForPart<PT> extends FXObject {
        /**
         * Get the indexof variable
         */
        public int getIndex$();

        /**
         * Adjust the indexof variable
         * May cause re-calculation.
         */
        public void adjustIndex$(int value$);

        /**
         * Set the induction variable
         * May cause re-calculation.
         */
        public void setInductionVar$(PT value$);
    };

    protected final FXObject container;
    protected final int forVarNum;
    protected final int inductionSeqVarNum;
    protected final boolean dependsOnIndex;

    public int partResultVarNum; // This gets magically assigned when a part is created

    protected int numParts;
    protected boolean uninitialized = true;

    private int cacheIndex;
    private int cachePart;

    public BoundFor(FXObject container, int forVarNum, int inductionSeqVarNum, boolean dependsOnIndex) {
        this.container = container;
        this.forVarNum = forVarNum;
        this.inductionSeqVarNum = inductionSeqVarNum;
        this.dependsOnIndex = dependsOnIndex;
        resetCache();
    }

    // Required public interface

    public abstract FXForPart makeForPart$(int index);

    // Called by invalidate when the input sequence changes.
    public abstract void replaceParts(int startPart, int endPart, int insertedParts, int phase);


    // Shared implementation interface (optional)

    // Cumulative length, exclusive
    protected abstract int cumLength(int ipart);

    protected abstract FXForPart<PT> getPart(int part);

    protected void initializeIfNeeded() {
        if (uninitialized) {
            // Init the induction sequence
            int sz = container.size$(inductionSeqVarNum);

            uninitialized = false;
            replaceParts(0, 0, sz, FXObject.PHASE_TRANS$CASCADE_TRIGGER);
        }
    }

    public int size() {
        initializeIfNeeded();
        return cumLength(numParts);
    }

    public T get(int index) {
        initializeIfNeeded();

        if (index < 0)
            return null;
        
        // FIXME - should use binary search if not in cache.
        int i, cumPrev;
        if (index >= cacheIndex) {
            i = cachePart;
            cumPrev = cumLength(i);
        } else {
            i = 0;
            cumPrev = 0;
        }
        for (;; i++) {
            if (i >= numParts)
                return null;
            int cum = cumLength(i+1);
            if (index < cum) {
                cachePart = i;
                cacheIndex = cumPrev;
                return get(i, index-cumPrev);
            }
            cumPrev = cum;
        }
    }

    protected void resetCache() {
        cachePart = 0;
    }

    /** Get the size of part ipart. */
    protected int size(int ipart) {
        FXForPart part = getPart(ipart);
        return part.size$(partResultVarNum); // sequence version
    }

    /** Get the j'th item of part ipart. */
    protected T get(int ipart, int j) {
        FXForPart part = getPart(ipart);
        return (T) part.elem$(partResultVarNum, j);
    }
}

