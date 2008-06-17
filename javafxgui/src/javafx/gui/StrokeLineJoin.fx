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
package javafx.gui;

import java.awt.BasicStroke;

// PENDING_DOC_REVIEW
/**
 * Defines the line join style of the {@code Shape} and {@code AbstractShape} 
 * as one of the static values that define possible line join styles.
 *
 * @profile common
 */
public class StrokeLineJoin {

    private attribute toolkitValue: Integer = BasicStroke.JOIN_MITER;

    private attribute name: String = "JOIN_MITER";

    // PENDING_DOC_REVIEW
    /**
     * Joins path segments by extending their outside edges until they meet.
     *
     * @profile common 
     */
    public static attribute MITER = StrokeLineJoin { }

    // PENDING_DOC_REVIEW
    /**
     * Joins path segments by connecting the outer corners 
     * of their wide outlines with a straight segment.
     *
     * @profile common 
     */
    public static attribute BEVEL = StrokeLineJoin { 
            toolkitValue: BasicStroke.JOIN_BEVEL
            name: "JOIN_BEVEL" 
    }

    // PENDING_DOC_REVIEW
    /**
     * Joins path segments by rounding off the corner 
     * at a radius of half the line width.
     *
     * @profile common 
     */
    public static attribute ROUND = StrokeLineJoin {
        toolkitValue: BasicStroke.JOIN_ROUND
        name: "JOIN_ROUND" 
    }

    function getToolkitValue(): Integer { toolkitValue }

    public function toString(): String { name }

}
