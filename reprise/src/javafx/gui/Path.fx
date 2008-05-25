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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;
import java.awt.geom.GeneralPath;

// PENDING_DOC_REVIEW
/**
 * The {@code Path} class represents a simple shape 
 * and provides facilities required for basic construction 
 * and management of a geometric path. 
 *
 * @profile common
 */
public class Path extends Shape {

    function createSGNode():SGNode { new SGShape() }

    function getSGShape():SGShape { getSGNode() as SGShape }

    function updatePath2D() {
        var path2D = new GeneralPath(fillRule.getToolkitValue(), sizeof elements);
        for (elt in elements) {
            elt.addTo(path2D);
        }
        getSGShape().setShape(path2D);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the filling rule constant for determining the interior of the path. 
     * The value must be one of the following constants: 
     * {@code FillRile.EVEN_ODD} or {@code FillRule.NON_ZERO}. 
     * The default value is {@code FillRule.NON_ZERO}.
     *
     * @profile common
     */
    public attribute fillRule:FillRule = FillRule.NON_ZERO on replace { 
        updatePath2D();
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the array of path elements of this path.
     *
     * @profile common
     */
    public attribute elements:PathElement[] on replace oldElts[a..b] = newElts { 
        for (elt in newElts) { elt.path = this; }
        updatePath2D();
    }

}
