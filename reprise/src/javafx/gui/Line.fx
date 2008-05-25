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
import com.sun.scenario.scenegraph.SGLine;


// PENDING_DOC_REVIEW
/**
 * This {@code Line} represents a line segment in {@code (x,y)}
 * coordinate space. 
 */
public class Line extends Shape {

    function createSGNode():SGNode { new SGLine() }

    function getSGLine():SGLine { getSGNode() as SGLine }
    
    // PENDING_DOC_REVIEW
    /**
     * The X coordinate of the start point of the line segment.
     */
    public attribute x1:Number on replace { 
        getSGLine().setX1(x1.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * The Y coordinate of the start point of the line segment.
     */
    public attribute y1:Number on replace { 
        getSGLine().setY1(y1.floatValue());        
    }
    
    // PENDING_DOC_REVIEW
    /**
     * The X coordinate of the end point of the line segment.
     */
    public attribute x2:Number on replace { 
        getSGLine().setX2(x2.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * The Y coordinate of the end point of the line segment.
     */
    public attribute y2:Number on replace { 
        getSGLine().setY2(y2.floatValue());
    }

}
