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

    private FXForPart<PT>[] parts;
    private int[] cumulatedLengths;
    private boolean areCumulatedLengthsValid = false;
    private boolean inWholesaleUpdate = true; // ignore initial individual updates

    public BoundForHelperNaive(FXObject container, int forVarNum, int inductionSeqVarNum, boolean dependsOnIndex) {
        super(container, forVarNum, inductionSeqVarNum, dependsOnIndex);
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

    private void blanketInvalidationOfBoundFor() {
        container.invalidate$(forVarNum, 0, SequencesBase.UNDEFINED_MARKER_INT, SequencesBase.UNDEFINED_MARKER_INT, FXObject.PHASE_TRANS$CASCADE_INVALIDATE);
    }

    // Called by invalidate when the result of a part changes.
    @Override
    public void update$(FXObject src, final int varNum, int startPos, int endPos, int newLength, final int phase) {
        update$(src, varNum, phase);
    }

    // Called by invalidate when the result of a part changes.
    @Override
    public void update$(FXObject src, final int varNum, final int phase) {
        if (uninitialized || inWholesaleUpdate)
            return;
        if (phase != FXObject.VFLGS$NEEDS_TRIGGER) {
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

    private void syncInductionVar(int ipart) {
        FXForPart part = getPart(ipart);
        part.setInductionVar$(container.elem$(inductionSeqVarNum, ipart));
    }

    private void buildParts(int ipFrom, int ipTo) {
        for (int ips = ipFrom; ips < ipTo; ++ips) {
            FXForPart part = makeForPart$(ips);
            parts[ips] = part;
            syncInductionVar(ips);
            addDependent$(part, partResultVarNum, this);
        }
    }

    // Called by invalidate when the input sequence changes.
    public void replaceParts(int startPart, int endPart, int insertedParts, int phase) {
        if (uninitialized)
            return;
        if (phase != FXObject.VFLGS$NEEDS_TRIGGER) {
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
}

