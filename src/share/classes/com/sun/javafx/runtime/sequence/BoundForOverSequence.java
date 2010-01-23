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

public abstract class BoundForOverSequence<T, PT> extends BoundForOverVaryingAbstract<T, PT> {

    private boolean inWholesaleUpdate = true; // ignore initial individual updates

    public BoundForOverSequence(FXObject container, int forVarNum, int inductionSeqVarNum, boolean dependsOnIndex) {
        super(container, forVarNum, inductionSeqVarNum, dependsOnIndex);
    }

    // Called by invalidate when the result of a part changes.
    @Override
    public void update$(FXObject src, final int varNum, int startPos, int endPos, int newLength, final int phase) {
        if (uninitialized || inWholesaleUpdate)
            return;
        if ((phase & PHASE_TRANS$PHASE) == PHASE$INVALIDATE) {
            blanketInvalidationOfBoundFor();
            return;
        }
        //System.err.println("updateForPart src: " + ((FXForPart)src).getIndex$() + ", newLength: " + newLength);

        // Do invalidation

        int ipart = ((FXForPart) src).getIndex$();
        int oldStartPos = cumLength(ipart);
        int oldEndPos = cumLength(ipart + 1);

        // Set-up to lazily update lengths
        cumulatedLengths = null;
        areCumulatedLengthsValid = false;

        // Calculate the inserted length (in the new parts)
        int newStartPos = oldStartPos;
        int newEndPos = cumLength(ipart + 1);
        int insertedLength = newEndPos - newStartPos;

        resetCache();

        // Send invalidation
        //System.out.println("ipart: " + ipart + ", oldStart: " + oldStartPos + ", oldEndPos: " + oldEndPos + ", newEndPos: " + newEndPos + ", insertedLength: " + insertedLength);
        container.invalidate$(forVarNum, oldStartPos, oldEndPos, insertedLength, phase);
    }

    // Called by invalidate when the input sequence changes.
    public void replaceParts(int startPart, int endPart, int insertedParts, int phase) {
        if (uninitialized)
            return;
        if ((phase & PHASE_TRANS$PHASE) == PHASE$INVALIDATE) {
            blanketInvalidationOfBoundFor();
            return;
        }
        //System.err.println("startPart: " + startPart + ", endPart: " + endPart + ", insertedParts: " + insertedParts);
        int removedParts = endPart - startPart;
        int deltaParts = insertedParts - removedParts;
        int newNumParts = numParts + deltaParts;

        if (parts == null || deltaParts != 0) {
            // Changing size or first time.

            int oldStartPos;
            int oldEndPos;
            int trailingLength;

            // Allocate the new elements
            FXForPart<PT>[] newParts = (FXForPart<PT>[]) new FXForPart[newNumParts];

            if (parts == null) {
                assert startPart == 0;
                assert endPart == 0;
                oldStartPos = 0;
                oldEndPos = 0;
                trailingLength = 0;
            } else {
                // Remember old positions (for invalidate)
                oldStartPos = cumLength(startPart);
                oldEndPos = cumLength(endPart);
                trailingLength = numParts - endPart;

                // Copy the existing parts
                System.arraycopy(parts, 0, newParts, 0, startPart);
                //System.err.println("parts.len: " + parts.length + ", start: " + startPart +  ", end: " + endPart + ", newParts.len: " + newParts.length + ", s+i: " + (startPart + insertedParts) + ", trail: " + trailingLength);
                System.arraycopy(parts, endPart, newParts, startPart + insertedParts, trailingLength);

                for (int ips = startPart; ips < endPart; ++ips) {
                    removeDependent$(parts[ips], partResultVarNum, this);
                }
            }

            // Install new parts
            parts = newParts;
            numParts = newNumParts;

            // Don't generate individual updates
            inWholesaleUpdate = true;

            // Fill in the new parts
            buildParts(startPart, startPart + insertedParts);

            // Set-up to lazily update lengths
            cumulatedLengths = null;
            areCumulatedLengthsValid = false;

            // Calculate the inserted length (in the new parts)
            int newStartPos = oldStartPos;
            int newEndPos = cumLength(startPart + insertedParts);
            int insertedLength = newEndPos - newStartPos;

            // Send wholesale invalidation
            container.invalidate$(forVarNum, oldStartPos, oldEndPos, insertedLength, phase);
            inWholesaleUpdate = false;

            // Adjust the index of trailing parts
            for (int ips = startPart + insertedParts; ips < startPart + insertedParts + trailingLength; ++ips) {
                getPart(ips).adjustIndex$(deltaParts);
            }
        } else {
            // In-place modification.  Update induction var.  Invalidation from parts.
            for (int ips = startPart; ips < endPart; ++ips) {
                syncInductionVar(ips);
            }
            areCumulatedLengthsValid = false;
        }
        resetCache();
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

