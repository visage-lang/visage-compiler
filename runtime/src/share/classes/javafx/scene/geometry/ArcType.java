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

import java.awt.geom.Arc2D;

// PENDING_DOC_REVIEW
/**
 * {@code ArcType} specifies the closure type for {@link Arc} objects.
 * 
 * @profile common
 */ 
public enum ArcType {

    // PENDING_DOC_REVIEW
    /**
     * The closure type for an open arc with no path segments connecting 
     * the two ends of the arc segment.
     * 
     * @profile common
     */     
    OPEN(Arc2D.OPEN),

    // PENDING_DOC_REVIEW
    /**
     * The closure type for an arc closed by drawing a straight line segment 
     * from the start of the arc segment to the end of the arc segment.
     * 
     * @profile common
     */     
    CHORD(Arc2D.CHORD),

    // PENDING_DOC_REVIEW
    /**
     * The closure type for an arc closed by drawing straight line segments 
     * from the start of the arc segment to the center of the full ellipse 
     * and from that point to the end of the arc segment.
     * 
     * @profile common
     */     
    ROUND(Arc2D.PIE);

    final int toolkitValue;
    
    private ArcType(int toolkitValue) {
        this.toolkitValue = toolkitValue;
    }

}
