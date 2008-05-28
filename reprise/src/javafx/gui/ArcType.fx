/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.awt.geom.Arc2D;

// PENDING_DOC_REVIEW
/**
 * The {@code ArcType} specifies the following closure type for 
 * the {@link Arc} objects.
 * <p>
 * It contains three predefined static values 
 * ({@link #OPEN}, {@link #CHORD} and {@link #ROUND}).
 * 
 * @profile common
 */ 
public class ArcType {

    private attribute toolkitValue: Integer = Arc2D.OPEN;

    private attribute name: String = "OPEN";

    // PENDING_DOC_REVIEW
    /**
     * The closure type for an open arc with no path segments connecting 
     * the two ends of the arc segment.
     * 
     * @profile common
     */     
    public static attribute OPEN = ArcType { }

    // PENDING_DOC_REVIEW
    /**
     * The closure type for an arc closed by drawing a straight line segment 
     * from the start of the arc segment to the end of the arc segment.
     * 
     * @profile common
     */     
    public static attribute CHORD = ArcType { 
        toolkitValue: Arc2D.CHORD
        name: "CHORD" 
    }

    // PENDING_DOC_REVIEW
    /**
     * The closure type for an arc closed by drawing straight line segments 
     * from the start of the arc segment to the center of the full ellipse 
     * and from that point to the end of the arc segment.
     * 
     * @profile common
     */     
    public static attribute ROUND = ArcType {
        toolkitValue: Arc2D.PIE
        name: "PIE" 
    }

    function getToolkitValue(): Integer { toolkitValue }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns a {@code String} that represents the value of this {@code ArcType}.
     *
     * @profile common
     */     
    public function toString(): String { name }

}
