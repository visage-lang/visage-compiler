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
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGText.VAlign;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.font.*;
import java.text.*;
import java.lang.System;

/**
 * A canvas node that displays text defined by a location (x, y),
 * a string of characters (content), and a font. If stroking and filling
 * and/or shape functions aren't required, it's often more useful to 
 * use a View with a Label or SimpleLabel to represent text (for example,
 * in that case you can use HTML markup).
 */
public class Text extends Shape {

    private attribute textShape:SGShape;

    private attribute awtFont: java.awt.Font = bind font.getFont() on replace {
        updateText();
    };

    // public
    /** The character content of this text. */
    public attribute content: String on replace {
        updateText();
    };
    
    /** The x coordinate of the location of this text. */
    public attribute x: Number on replace {
        updateText();
    };
    
    /** The y coordinate of the location of this text. */
    public attribute y: Number on replace {
        updateText();
    };
    
    /** The font used to render the characters of this text. */
    public attribute font: Font = Font {size:16};

    public attribute verticalAlignment: Alignment = Alignment.LEADING on replace {
        updateText();
    };

    private function updateText():Void {
        if (textShape <> null and content <> null) {
            var layout = new TextLayout(content, awtFont, FRC);
            var b = layout.getBounds();
            var tx = x - b.getX();
            var ty = y - b.getY();
            if (verticalAlignment == Alignment.BASELINE) {
                ty += b.getY();
            } else if (verticalAlignment == Alignment.TRAILING) {
                ty -= layout.getAscent() + layout.getDescent();
            }
            var t = AffineTransform.getTranslateInstance(tx, ty);
            var tshape = layout.getOutline(t);
            textShape.setShape(tshape);
        }
    }

    private static attribute FRC = new FontRenderContext(null, true, true);


    protected function createShape(): SGShape {
        textShape = new SGShape();
        updateText();
        return textShape;
    }

    init {
        // override defaults in superclass
	//TODO: should be protected by "not isInitialized"
        if(fill == null)
            fill = Color.BLACK;
    }
}
