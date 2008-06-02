/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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
 
package javafx.ui.canvas; 


import javafx.ui.Canvas;
import javafx.ui.Image;
import javafx.ui.UIElement;

public class CanvasImage extends Image {
    private attribute canvasIcon: javax.swing.Icon; 
    private attribute canvas: Canvas;
    private attribute imageUrl: String = bind url on replace {
        if (image == null) {
            //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      public function run():Void {
                           getImage();
                      }
            });
        } else {
            UIElement.context.defineImage(imageUrl, image);
        }
    };
    
    public attribute content: Node on replace  {
        if (canvas <> null) {
            canvas.content = [ content ];
        }
    }

    public bound function getImage():java.awt.Image {
        retrieveImage(canvasIcon, canvas, url)
    }

    /* hack to make bound getImage dependent on canvas, icon, and url
     */
    public function retrieveImage(dummyCanvasIcon: javax.swing.Icon, dummyCanvas: Canvas, dummyURL : String):java.awt.Image {
        if (canvas == null) {
            canvas = Canvas { content: [this.content] };
        }
        if (canvasIcon <> canvas.jsgpanel.toIcon()) {
            canvas.getComponent();
            canvasIcon = canvas.jsgpanel.toIcon();
            image = canvas.jsgpanel.getIconImage();        
            if (url <> null) {
                UIElement.context.defineImage(url, image);
            }
        }
        return image;
    }
}



