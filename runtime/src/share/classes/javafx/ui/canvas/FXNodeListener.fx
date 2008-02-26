
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
import com.sun.scenario.scenegraph.event.*;
import java.awt.geom.Rectangle2D;

public class FXNodeListener extends SGNodeListener {

    public function boundsChanged(e:SGNodeEvent):Void {
        if (e.isConsumed()) {
            return;
        }
        var n = e.getNode();
        if (n == null) {
            return;
        }
        var b = n.getBounds();
        readonly var node = n.getAttribute("FX") as Node;
        if (node <> null) {
            // really shouldn't invoke later here, but scenario
            // gets confused if you modify a parent node
            // from the event call back, which we may do below.
            // remove invokeLater once scenario's fixed as you'll
            // see noticeable flashing the way it is.
            /*******
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
             * ***********/
                        var oldBounds = new Rectangle2D.Double(node.currentX,
                                                               node.currentY,
                                                               node.currentWidth,
                                                               node.currentHeight);
                        if (not oldBounds.equals(b)) {
                            node.currentX = b.getX();
                            node.currentY = b.getY();
                            node.currentWidth = b.getWidth();
                            node.currentHeight = b.getHeight();
                            //node.realign();
                        }
              /*****************
                    }
                });
              ****************/
        }
    }
}