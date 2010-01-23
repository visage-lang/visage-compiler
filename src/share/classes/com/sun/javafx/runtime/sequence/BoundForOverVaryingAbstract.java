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

public abstract class BoundForOverVaryingAbstract<T, PT> extends BoundFor<T, PT> {

    protected FXForPart<PT>[] parts;
    protected int[] cumulatedLengths;
    protected boolean areCumulatedLengthsValid = false;
    private int cacheIndex;
    private int cachePart;

    public BoundForOverVaryingAbstract(FXObject container, int forVarNum, int inductionSeqVarNum, boolean dependsOnIndex) {
        super(container, forVarNum, inductionSeqVarNum, dependsOnIndex);
        resetCache();
    }


    /** Get the size of part ipart. */
    protected abstract int size(int ipart);

    /** Get the j'th item of part ipart. */
    protected abstract T get(int ipart, int j);
    
    public int size() {
        initializeIfNeeded();
        return cumLength(numParts);
    }

    protected int cumLength(int ipart) {
        if (ipart <= 0) {
            return 0;
        }
        if (!areCumulatedLengthsValid) {
            if (cumulatedLengths == null) {
                cumulatedLengths = new int[numParts];
            }

            // We have invalid lengths, recompute them all
            int sum = 0;
            for (int ips = 0; ips < numParts; ++ips) {
                sum += size(ips);
                cumulatedLengths[ips] = sum;
            }
            areCumulatedLengthsValid = true;
        }
        return cumulatedLengths[ipart-1];
    }

    protected FXForPart<PT> getPart(int ipart) {
        return parts[ipart];
    }

    protected void blanketInvalidationOfBoundFor() {
        container.invalidate$(forVarNum, 0, SequencesBase.UNDEFINED_MARKER_INT, SequencesBase.UNDEFINED_MARKER_INT, FXObject.PHASE_TRANS$CASCADE_INVALIDATE);
    }

    protected void syncInductionVar(int ipart) {
        FXForPart part = getPart(ipart);
        part.setInductionVar$(container.elem$(inductionSeqVarNum, ipart));
    }

    protected void buildParts(int ipFrom, int ipTo) {
        for (int ips = ipFrom; ips < ipTo; ++ips) {
            FXForPart part = makeForPart$(ips);
            parts[ips] = part;
            syncInductionVar(ips);
            addDependent$(part, partResultVarNum, this);
        }
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
}

