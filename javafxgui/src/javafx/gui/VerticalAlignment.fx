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

// PENDING_DOC_REVIEW
/**
 * The {@code VerticalAlignment} class represents the vertical alignment
 * in an area.
 *
 * @profile common
 */
public class VerticalAlignment {

    private attribute toolkitValue: Integer = javax.swing.SwingConstants.CENTER;

    private attribute name: String = "CENTER";

    // PENDING_DOC_REVIEW
    /**
     * Represents the central position.
     *
     * @profile common
     */
    public static attribute CENTER = VerticalAlignment {
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the top position.
     *
     * @profile common
     */
    public static attribute TOP = VerticalAlignment {
        toolkitValue: javax.swing.SwingConstants.TOP
        name: "TOP"
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the bottom position.
     *
     * @profile common
     */
    public static attribute BOTTOM = VerticalAlignment {
        toolkitValue: javax.swing.SwingConstants.BOTTOM
        name: "BOTTOM"
    }

    static function fromToolkitValue(value: Integer): VerticalAlignment {
         if (value == CENTER.toolkitValue) CENTER
         else if (value == TOP.toolkitValue) TOP
         else BOTTOM
    }

    function getToolkitValue(): Integer { toolkitValue }

    public function toString(): String { name }

}
