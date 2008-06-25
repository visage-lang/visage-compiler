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

package javafx.scene.geometry;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGLine;


// PENDING_DOC_REVIEW
/**
 * <p>This Line represents a line segment in {@code (x,y)}
 * coordinate space. Example:</p>
 * <pre><code>Line {
    startX:   0  startY:   0
      endX: 100    endY: 100
    stroke:Color.RED
}</code></pre>
 <p><img src="doc-files/Line01.png"/></p>
 * @profile common
 * @needsreview josh
 */
public class Line extends Shape {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode():SGNode { new SGLine() }

    function getSGLine():SGLine { impl_getSGNode() as SGLine }
    
    // PENDING_DOC_REVIEW
    /**
     * The X coordinate of the start point of the line segment.
     */
    public attribute startX:Number on replace { 
        getSGLine().setX1(startX.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * The Y coordinate of the start point of the line segment.
     */
    public attribute startY:Number on replace { 
        getSGLine().setY1(startY.floatValue());        
    }
    
    // PENDING_DOC_REVIEW
    /**
     * The X coordinate of the end point of the line segment.
     */
    public attribute endX:Number on replace { 
        getSGLine().setX2(endX.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * The Y coordinate of the end point of the line segment.
     */
    public attribute endY:Number on replace { 
        getSGLine().setY2(endY.floatValue());
    }

}
