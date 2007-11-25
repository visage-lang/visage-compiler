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

package javafx.ui.canvas;

import javafx.ui.Alignment;
import javafx.ui.Color;
import javafx.ui.Font;
import javafx.ui.Paint;
import javafx.ui.canvas.VisualNode;
import com.sun.scenario.scenegraph.SGAbstractShape;
import com.sun.scenario.scenegraph.SGText;
import com.sun.scenario.scenegraph.SGText.VAlign;

/**
 * A canvas node that displays text defined by a location (x, y),
 * a string of characters (content), and a font. If stroking and filling
 * and/or shape functions aren't required, it's often more useful to 
 * use a View with a Label or SimpleLabel to represent text (for example,
 * in that case you can use HTML markup).
 */
public class Text extends VisualNode {
    // private:
    private attribute sgtext: SGText;
    private attribute awtFont: java.awt.Font = bind font.getFont() on replace {
        sgtext.setFont(awtFont);
    };

    // public
    /** The character content of this text. */
    public attribute content: String on replace {
        sgtext.setText(content);
    };
    
    /** The x coordinate of the location of this text. */
    public attribute x: Number on replace {
        updateLocation();
    };
    
    /** The y coordinate of the location of this text. */
    public attribute y: Number on replace {
        updateLocation();
    };
    
    /** The font used to render the characters of this text. */
    public attribute font: Font;

    public attribute verticalAlignment: Alignment = Alignment.LEADING on replace {
        updateVerticalAlignment();
    };

    public attribute fill: Paint = Color.BLACK;
    public attribute stroke: Paint = null;

    private function updateLocation() {
        if (sgtext <> null) {
            sgtext.setLocation(new java.awt.geom.Point2D.Double(x, y));
        }
    };

    private function updateVerticalAlignment() {
        if (sgtext <> null) {
            if (verticalAlignment == Alignment.BASELINE) {
                sgtext.setVerticalAlignment(VAlign.BASELINE);
            } else if (verticalAlignment == Alignment.LEADING) {
                sgtext.setVerticalAlignment(VAlign.TOP);
            } else {
                sgtext.setVerticalAlignment(VAlign.BOTTOM);
            }
        }
    };


    public function createVisualNode(): SGAbstractShape {
        sgtext = new SGText();
        sgtext.setAntialiasingHint(java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        sgtext.setText(content);
        sgtext.setFont(awtFont);
        updateLocation();
        updateVerticalAlignment();
        return sgtext;
    }
}
