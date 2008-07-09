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

package javafx.application;

import javafx.scene.*;
import javafx.scene.paint.*;

/**
 * The JavaFX {@code Stage} class is the root area for all scene content.
 *
 * @profile common
 */
public class Stage {

    // PENDING_DOC_REVIEW
    /**
     * The width of this {@code Stage}
     *
     * @profile common
     * @readonly
     */
    public attribute /*read-only*/  width:Integer;

    // PENDING_DOC_REVIEW
    /**
     * The height of this {@code Stage}
     *
     * @profile common
     * @readonly
     */
    public attribute /*read-only*/  height:Integer;

    // PENDING_DOC_REVIEW
    /**
     * Defines the background fill of this {@code Stage}. Both a {@code null} value meaning paint no background and a
     * Paint with transparency are supported, but what is painted behind it will depend on the platform.
     *
     * @profile common
     */
    public attribute fill: Paint = Color.WHITE;

    // PENDING_DOC_REVIEW
    /**
     * The array of {@link Node}s to be rendered on this {@code Stage}.
     *
     * @profile common
     */
    public attribute content: Node[];
    
}