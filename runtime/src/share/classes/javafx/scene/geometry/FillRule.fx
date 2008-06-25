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
 * Sets the filling rule for the path to the following values: 
 * {@code NON_ZERO} or {@code EVEN_ODD}. 
 * The default value is {@code NON_ZERO}.
 *
 * @profile common
 * @needsreview
 */  
public class FillRule {

    private attribute toolkitValue: Integer = GeneralPath.WIND_NON_ZERO;

    private attribute name: String = "NON_ZERO";
    
    // PENDING_DOC_REVIEW
    /**
     * Defines a non-zero filling rule for determining the interior of a path. 
     *
     * @profile common
     */      
    public static attribute NON_ZERO = FillRule {
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines an even-odd filling rule for determining the interior of a path.
     *
     * @profile common
     */      
    public static attribute EVEN_ODD = FillRule {
        toolkitValue: GeneralPath.WIND_EVEN_ODD
        name: "EVEN_ODD"
    }

    function getToolkitValue(): Integer { toolkitValue }

    public function toString(): String { name }

}
