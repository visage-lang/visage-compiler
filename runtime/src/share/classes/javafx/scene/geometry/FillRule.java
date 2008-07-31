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

import java.awt.geom.GeneralPath;

// PENDING_DOC_REVIEW
/**
 * The fill rule for a path.
 *
 * @profile common
 * @needsreview
 */  
public enum FillRule {

    // PENDING_DOC_REVIEW
    /**
     * Defines a non-zero filling rule for determining the interior of a path. 
     *
     * @profile common
     */      
    NON_ZERO(GeneralPath.WIND_NON_ZERO),

    // PENDING_DOC_REVIEW
    /**
     * Defines an even-odd filling rule for determining the interior of a path.
     *
     * @profile common
     */      
    EVEN_ODD(GeneralPath.WIND_EVEN_ODD);

    final int toolkitValue;
    
    private FillRule(int toolkitValue) {
        this.toolkitValue = toolkitValue;
    }

}
