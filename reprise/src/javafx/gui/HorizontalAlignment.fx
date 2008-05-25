/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * The {@code HorizontalAlignment} class represents the horizontal alignment
 * in an area.
 *
 * @profile common
 */
public class HorizontalAlignment {

    private attribute toolkitValue: Integer = javax.swing.SwingConstants.LEADING;

    private attribute name: String = "LEADING";
    
    // PENDING_DOC_REVIEW
    /**
     * Represents the leading position.
     *
     * @profile common
     */          
    public static attribute LEADING = HorizontalAlignment { 
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the trailing position.
     *
     * @profile common
     */          
    public static attribute TRAILING = HorizontalAlignment {
        toolkitValue: javax.swing.SwingConstants.TRAILING
        name: "TRAILING"
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the left position.
     *
     * @profile common
     */          
    public static attribute LEFT = HorizontalAlignment {
        toolkitValue: javax.swing.SwingConstants.LEFT
        name: "LEFT"
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the central position.
     *
     * @profile common
     */          
    public static attribute CENTER = HorizontalAlignment {
        toolkitValue: javax.swing.SwingConstants.CENTER
        name: "CENTER"
    }

    // PENDING_DOC_REVIEW
    /**
     * Represents the right position.
     *
     * @profile common
     */          
    public static attribute RIGHT = HorizontalAlignment {
        toolkitValue: javax.swing.SwingConstants.RIGHT
        name: "RIGHT"
    }

    static function fromToolkitValue(value: Integer): HorizontalAlignment {
         if (value == LEADING.toolkitValue) LEADING
         else if (value == TRAILING.toolkitValue) TRAILING
         else if (value == LEFT.toolkitValue) LEFT
         else if (value == CENTER.toolkitValue) CENTER
         else RIGHT
    }

    function getToolkitValue(): Integer { toolkitValue }

    public function toString(): String { name }

}
