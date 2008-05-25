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

import java.awt.geom.GeneralPath;

// PENDING_DOC_REVIEW
/**
 * The {@code PathElement} class represents an abstract element 
 * of the {@link Path} that can represent any geometric objects 
 * like straight lines, quadratic or cubic curves etc...
 * 
 * @profile common
 */
public abstract class PathElement {

    // PENDING_DOC_REVIEW
    /**
     * Defines the {@code Path} for this path element.
     *
     * @profile common
     */
    attribute path:Path;

    // PENDING_DOC_REVIEW
    /**
     * Performs the action specified for this particular {@code PathElement}   
     *
     * @profile common
     */
    abstract function addTo(path2D:GeneralPath):Void;

    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */
    public attribute absolute: Boolean = true on replace {
        if (path <> null) { path.updatePath2D(); }
    }

}
