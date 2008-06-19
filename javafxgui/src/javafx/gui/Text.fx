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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGText;

// PENDING_DOC_REVIEW
/**
 * The {@code Text} class defines a node that displays a single line of text.
 *
 * @profile common
 */
public class Text extends Shape {

    function createSGNode():SGNode { new SGText() }

    function getSGText():SGText { getSGNode() as SGText }

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

    // PENDING_DOC_REVIEW
    /**
     * Defines if this {@code Text} is editable or not.
     *
     * @profile common
     */    
    public attribute editable:Boolean = false;

    // PENDING_DOC_REVIEW
    /**
     * Defines if this {@code Text} should select the text when gets focused.
     * 
     * @profile common
     */    
    public attribute selectOnFocus: Boolean = true;

    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */    
    public attribute action: function(): Void;

    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */    
    public attribute verify: function(newValue: String): Boolean;

    // PENDING_DOC_REVIEW
    /**
     * Gets the start position of the selected text in this {@code Text}.
     *
     * @profile common
     */    
    public bound function getSelectionStart(): Integer { 0 }

    // PENDING_DOC_REVIEW
    /**
     * Gets the end position of the selected text in this {@code Text}.
     * 
     * @profile common
     */    
    public bound function getSelectionEnd():Integer { 0 }

    // PENDING_DOC_REVIEW
    /**
     * Selects the text between the specified start and end positions.
     *
     * @param start the zero-based index of the first character to be selected  
     * @param end the zero-based end position of the text to be selected                  
     * 
     * @profile common
     */    
    public function select(start:Integer, end:Integer):Void { }

    // PENDING_DOC_REVIEW
    /**
     * Selects all the text in this text {@code Text}.
     *
     * @profile common
     */    
    public function selectAll():Void { }

}
