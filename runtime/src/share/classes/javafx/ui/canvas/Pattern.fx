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


import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import javafx.ui.Canvas;
import javafx.ui.Paint;

/**
 * A Pattern is used to fill or stroke an object using a pre-defined graphic 
 * object which can be replicated ("tiled") at fixed intervals in x and y to 
 * cover the areas to be painted. 
 * <p>
 * Attributes x, y, width, height define a reference rectangle somewhere on 
 * the infinite canvas. The reference rectangle has its top/left at (x,y) and 
 * its bottom/right at (x+width,y+height). The tiling theoretically extends a 
 * series of such rectangles to infinity in X and Y (positive and negative), 
 * with rectangles starting at (x + m*width, y + n*height) for each possible 
 * integer value for m and n.
 */
public class Pattern extends Paint {
    private attribute UNSET: Integer = java.lang.Integer.MIN_VALUE;
    private attribute canvas: Canvas;
    private attribute texturePaint: TexturePaint;
    /** The x coordinate of the start point of the tile. Defaults to zero. */
    public attribute x: Number;
    /** The y coordinate of the start point of the tile. Defaults to zero */
    public attribute y: Number;
    /** The width the tile. Defaults to the preferred size of this pattern's content. */
    public attribute width: Number;
    /** The height of the tile. Defaults to the preferred size of this pattern's content. */
    public attribute height: Number;
    /** The graphic objects that will be used to create the tile that this pattern will paint.*/
    public attribute content: Node[];
    
    public function getPaint():java.awt.Paint{
        makeTexture();
        if (texturePaint == null) {
            return Color.BLACK;
        }
        return texturePaint;
    }    
    
    public function makeTexture():Void {
        if (canvas == null) {
            canvas = Canvas {content: bind content};
        }
        if (texturePaint == null) {
            canvas.getComponent();
            var im ;
            try {
                im  = canvas.jsgpanel.getIconImage() as BufferedImage;
            } catch (e) {
                return;            
            }
            var w:Number = im.getWidth();
            var h:Number = im.getHeight();
            if (height <> UNSET) {
                h = height;
            }
            if (width <> UNSET) {
                w = width;
            }
            if (im <> null) {
                var rect = new Rectangle2D.Double(x, y, w, h);
                texturePaint = new TexturePaint(im, rect);
            }
        }
    }    
}




