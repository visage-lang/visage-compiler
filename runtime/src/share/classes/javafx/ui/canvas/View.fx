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


import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import javafx.ui.Widget;
import javafx.ui.canvas.Node;
import javafx.ui.canvas.SizeableCanvasElement;
import com.sun.scenario.scenegraph.SGComponent;
import com.sun.scenario.scenegraph.SGNode;
import org.jdesktop.layout.Baseline;
import java.awt.geom.Rectangle2D;

/**
 * A Canvas node that contains a Widget (Swing component). The contained widget 
 * will be sized to its preferred size.
 */
public class View extends Node, SizeableCanvasElement {
    // TODO MARK AS FINAL
    private attribute sgcomponent: SGComponent;

    /** If true, the graphics context used to paint the widget will be anti-aliased. Defaults to false */
    public attribute antialias: Boolean = false; 
    /** If true, the graphics context used to paint the widget will use anti-aliasing for text. Defaults to false.*/
    public attribute antialiasText: Boolean = false; 
    /** If true, the graphics context used to paint the widget will use fractional metrics, otherwise integer metrics will be used. Defaults to false.*/
    public attribute fractionalMetrics: Boolean;
    /** The widget (Swing component) contained in this node */
    public attribute content: Widget on replace {
       sgcomponent.setComponent(content.getComponent());
    }
    public attribute size: Dimension on replace {
        sgcomponent.setSize(size);
    }
    public attribute baseline: Number;

    private function updateBaseline(){
        if (sgcomponent != null) {
            var c = sgcomponent.getComponent();
            if (c instanceof JComponent) {
                baseline = Baseline.getBaseline(c as JComponent);
            }
        }
    };

    override attribute bounds on replace {
        updateBaseline();
    }
    
    public function createNode(): SGNode {
        sgcomponent = new SGComponent();
        sgcomponent.setComponent(content.getComponent());
        // TODO: implement?
        //sgcomponent.setAntialias(antialias);
        //sgcomponent.setAntialiasText(antialiasText);
        //sgcomponent.setFractionalMetrics(fractionalMetrics);
        if (size != null) {
            sgcomponent.setSize(size);
        }
        updateBaseline();
        return sgcomponent;
    }    
    
    public function setSize(width: Number, height: Number):Void  {
        sgcomponent.setSize(width.intValue(), height.intValue());
    }
    public function onSizeToFitCanvas(value:Boolean):Void {
        // empty
    }
}


//TODO: are any of the following required?
/*
trigger on View.antialias = value {
   zswing.setAntialias(value);
}

trigger on View.antialiasText = value {
   zswing.setAntialiasText(value);
}

trigger on View.fractionalMetrics = value {
   zswing.setFractionalMetrics(value);
}
*/

