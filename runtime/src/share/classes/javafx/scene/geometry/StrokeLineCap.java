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

package javafx.scene.geometry;

import java.awt.BasicStroke;

// PENDING_DOC_REVIEW
/**
 * Defines the end cap style of a {@code Shape}.
 *
 * @profile common
 */
public enum StrokeLineCap {

    // PENDING_DOC_REVIEW
    /**
     * Ends unclosed subpaths and dash segments with a square projection 
     * that extends beyond the end of the segment to a distance 
     * equal to half of the line width.
     *
     * @profile common
     */
    SQUARE(BasicStroke.CAP_SQUARE),

    // PENDING_DOC_REVIEW
    /**
     * Ends unclosed subpaths and dash segments with no added decoration.
     *
     * @profile common
     */
    BUTT(BasicStroke.CAP_BUTT),

    // PENDING_DOC_REVIEW
    /**
     * Ends unclosed subpaths and dash segments with a round decoration 
     * that has a radius equal to half of the width of the pen.
     *
     * @profile common
     */
    ROUND(BasicStroke.CAP_ROUND);

    final int toolkitValue;

    private StrokeLineCap(int toolkitValue) {
        this.toolkitValue = toolkitValue;
    }

}
