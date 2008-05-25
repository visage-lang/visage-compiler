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
 * Creates an addition to the path by moving to the specified 
 * coordinates specified in double precision.
 *
 * @profile common
 */     
public class MoveTo extends PathElement {

    private function u():Void { 
        if (path <> null) { path.updatePath2D(); }
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the specified X coordinate.
     *
     * @profile common
     */     
    public attribute x:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the specified Y coordinate.
     *
     * @profile common
     */     
    public attribute y:Number on replace { u(); }

    function addTo(path2D:GeneralPath):Void {
        if (absolute) {
            path2D.moveTo(x.floatValue(), y.floatValue());
        }
        else {
            var cp = path2D.getCurrentPoint();
            path2D.moveTo((cp.getX() + x).floatValue(), (cp.getY() + y).floatValue());
        }
    }

}
