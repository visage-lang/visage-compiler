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

package javafx.scene;

// PENDING_DOC_REVIEW
/**
 * The {@code HorizontalAlignment} class represents the horizontal alignment
 * in an area.
 *
 * @profile common
 */
public class HorizontalAlignment {

    // PENDING(shannonh) - need private constructor to prevent bogus instance

    private attribute name: String;

    // PENDING_DOC_REVIEW
    /**
     * Represents the leading position.
     *
     * @profile common
     */
    public static attribute LEADING = HorizontalAlignment { name: "LEADING" }

    // PENDING_DOC_REVIEW
    /**
     * Represents the trailing position.
     *
     * @profile common
     */
    public static attribute TRAILING = HorizontalAlignment { name: "TRAILING" }

    // PENDING_DOC_REVIEW
    /**
     * Represents the left position.
     *
     * @profile common
     */
    public static attribute LEFT = HorizontalAlignment { name: "LEFT" }

    // PENDING_DOC_REVIEW
    /**
     * Represents the central position.
     *
     * @profile common
     */
    public static attribute CENTER = HorizontalAlignment { name: "CENTER" }

    // PENDING_DOC_REVIEW
    /**
     * Represents the right position.
     *
     * @profile common
     */
    public static attribute RIGHT = HorizontalAlignment { name: "RIGHT" }

    override function toString(): String { name }

}
