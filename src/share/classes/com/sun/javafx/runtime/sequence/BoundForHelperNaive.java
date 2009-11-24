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


public abstract class BoundForHelperNaive<T, PT> extends BoundForHelper<T, PT> {

    private FXForPart<PT>[] elements;
    private int[] cumulatedLengths;
    private boolean areCumulatedLengthsValid = false;

    public BoundForHelperNaive(FXObject container, int forVarNum, int inductionSeqVarNum, boolean dependsOnIndex) {
        super(container, forVarNum, inductionSeqVarNum, dependsOnIndex);
    }

    protected int cumLength(int ipart) {
        if (ipart < 0) {
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
        return cumulatedLengths[ipart];
    }

    protected FXForPart<PT> getPart(int ipart) {
        return elements[ipart];
    }

    // Called by invalidate when the result of part[ipart] changes.
    public void updateForPart(int ipart, int begin, int end, int newLen, int phase) {
        if (uninitialized)
            return;
        if (phase != FXObject.VFLGS$NEEDS_TRIGGER)
            return;
        areCumulatedLengthsValid = false;
        //TODO: add invalidation
    }

    private void syncInductionVar(int ipart) {
        FXForPart part = getPart(ipart);
        part.setInductionVar$(container.elem$(inductionSeqVarNum, ipart));
    }

    // Called by invalidate when the input sequence changes.
    public void replaceParts(int startPart, int endPart, int insertedParts, int phase) {
        if (uninitialized)
            return;
        if (phase != FXObject.VFLGS$NEEDS_TRIGGER)
            return;
        int removedParts = endPart - startPart;
        int deltaParts = insertedParts - removedParts;
        int newNumParts = numParts + deltaParts;

        if (elements == null) {
            // First time, create the elements
            elements = new FXForPart[newNumParts];
        } else if (deltaParts != 0) {
            // Changing size.  Copy the existing parts
            int toCopy = deltaParts > 0? numParts : newNumParts;
            FXForPart<PT>[] tmpP = (FXForPart<PT>[]) new FXForPart[newNumParts];
            System.arraycopy(elements, 0, tmpP, 0, toCopy);
            elements = tmpP;

            // invalid, new one created lazily
            cumulatedLengths = null;
            areCumulatedLengthsValid = false;

            // Conservative: resync the induction var for on copied parts past the change point
            for (int ips = startPart; ips < toCopy; ++ips) {
                syncInductionVar(ips);
            }
        } else {
            // In-place modification.  Update induction var
            for (int ips = startPart; ips < endPart; ++ips) {
                syncInductionVar(ips);
            }
        }

        // Build new parts.
        for (int i = 0; i < deltaParts; i++) {
            int ipart = numParts + i;
            elements[ipart] = makeForPart$(ipart);
            syncInductionVar(ipart);
        }
        
        numParts = newNumParts;
    }
}

