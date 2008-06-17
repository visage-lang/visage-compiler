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

import java.awt.geom.GeneralPath;

// PENDING_DOC_REVIEW
/**
 * <p>Creates a line path element by drawing a straight line 
 * from the current coordinateS to the new coordinates.</p>
 * <pre><code>Path {
    stroke:Color.RED
    elements: [
        MoveTo { x: 0   y: 0 },
        LineTo { x: 100 y: 100},
    ]
}</code></pre>
 * <p><img src="doc-files/LineTo01.png"/></p>
 *
 * @profile common
 * @needsreview josh
 */ 
public class LineTo extends PathElement {

    private function u():Void { 
        if (path <> null) { path.updatePath2D(); }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate.
     *
     * @profile common
     */ 
    public attribute x:Number on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate. 
     *
     * @profile common
     */ 
    public attribute y:Number on replace { u(); }

    function addTo(path2D:GeneralPath):Void {
        if (absolute) {
            path2D.lineTo(x, y);
        }
        else {
            var cp = path2D.getCurrentPoint();
            path2D.lineTo(cp.getX() + x, cp.getY() + y);
        }
    }

}
