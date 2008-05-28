/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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


import java.awt.Composite;
import java.awt.geom.AffineTransform;
import javafx.ui.Canvas;
import javafx.ui.Paint;
import com.sun.scenario.scenegraph.SGNode;

//TODO Needs LENS Implementation
/**
 * Provides a view onto some other part of the scene graph. The node 
 * assigned to the <code>view</code> attribute will also be rendered
 * within the bounds of this object and this object's transformations
 * and/or filters, opacity, etc will be applied to it.
 */
public class Lens extends Node {
    private attribute lgx: Number;
    private attribute lgy: Number;
    //TODO: implement
    /*
    private attribute nodeListener:ZNodeListener;
    private attribute zcamera: ZCamera;
    private attribute lg: ZLayerGroup;
    private attribute viewBounds: ZBounds;
    */
    private function setView():Void {
        //TODO: implement
    }
    public attribute composite: Composite;
    public attribute viewComposite: Composite;
    public attribute fill: Paint;
    public attribute mask: Paint;
    // public
    /** the object that will be displayed in this lens */
    public attribute view: Node;
    
    public function createNode(): SGNode {
        //TODO: implement

        return null;
    }    
}

/*
attribute Lens.viewBounds = bind view.bounds;

trigger on Lens.composite = value {
    zcamera.setComposite(value);
}

trigger on Lens.viewComposite = value {
    zcamera.setComposite(value);
}

trigger on Lens.viewBounds = value {
    if (value <> null) {
        this.setView();
    }
}

function Lens.setView() {
    if (zcamera.getDrawingSurface() == null) {
         var zcanvas = this.getCanvas().zcanvas;
         if (zcanvas <> null) {
              zcamera.setDrawingSurface(zcanvas.getDrawingSurface());	 
         }
    }
}

trigger on Lens.view = value {
    if (value.parentCanvasElement == null) {
        value.parentCanvasElement = this;
    }
    setView();
    if (zcamera <> null) {
        if (lg <> null) {
            if (value.getNode() <> null) {
                if (lg.getNumChildren() > 0) {
                    lg.removeChild(0);
                }
                lg.addChild(value.getNode());
                zcamera.globalBoundsChanged(lg, null, lg.getBounds());
            }
        } else {
            lg = new ZLayerGroup(value.getNode());
            var self = this;
            lg.addNodeListener(new ZNodeListener() {
                    function boundsChanged(e:ZNodeEvent) {
                        self.zcamera.globalBoundsChanged(self.lg, null, self.lg.getBounds());
                    }
                });
            zcamera.addLayer(lg);
        }
    }
}
*/


