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
 * The {@code VerticalAlignment} class represents the vertical alignment
 * in an area.
 *
 * @profile common
 */
public class VerticalAlignment {

    // PENDING(shannonh) - need private constructor to prevent bogus instance

    private attribute name: String;

    // PENDING_DOC_REVIEW
    /**
     * Represents the central position.
     *
     * @profile common
     */
    public static attribute CENTER = VerticalAlignment { name: "CENTER" }

    // PENDING_DOC_REVIEW
    /**
     * Represents the top position.
     *
     * @profile common
     */
    public static attribute TOP = VerticalAlignment { name: "TOP" }

    // PENDING_DOC_REVIEW
    /**
     * Represents the bottom position.
     *
     * @profile common
     */
    public static attribute BOTTOM = VerticalAlignment { name: "BOTTOM" }

    override function toString(): String { name }

}
