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

package javafx.scene.text;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGText;
import javafx.scene.paint.Color;
import javafx.scene.geometry.Shape;

// PENDING_DOC_REVIEW
/**
 * The {@code Text} class defines a node that displays a single line of text.
 *
 * @profile common
 */
public class Text extends Shape {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode():SGNode { new SGText() }

    function getSGText():SGText { impl_getSGNode() as SGText }

    override attribute fill = Color.BLACK;

    // PENDING_DOC_REVIEW
    /**
     * Defines text string that is to be displayed.
     *
     * @profile common
     */
    public attribute content: String on replace { 
        getSGText().setText(content); 
    }
    
    private function ul():Void {
        getSGText().setLocation(new java.awt.geom.Point2D.Double(x, y));
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of text origin.
     *
     * @profile common
     */   
    public attribute x: Number on replace { ul(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of text origin.
     *
     * @profile common
     */    
    public attribute y: Number on replace { ul(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the font of text.
     *
     * @profile common
     */    
    public attribute font: Font = Font{} on replace {
        getSGText().setFont(font.getAWTFont());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the origin of text coordinate system in local coordinates.
     *
     * @profile common
     */
    public attribute textOrigin: TextOrigin = TextOrigin.BASELINE on replace {
        getSGText().setVerticalAlignment(textOrigin.getToolkitValue());
    }

}
