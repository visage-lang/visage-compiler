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
 
package hello.canvas;

import hello.VerticalAlignment;
import hello.HorizontalAlignment;
import java.lang.Exception;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.Math;
import java.lang.System;
import javax.swing.SwingUtilities;

import com.sun.scenario.scenegraph.SGAlignment;
import com.sun.scenario.scenegraph.SGComposite;
import com.sun.scenario.scenegraph.SGClip;
import com.sun.scenario.scenegraph.SGEffect;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGLeaf;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGTransform.Affine;
import com.sun.scenario.scenegraph.event.SGMouseListener;



/**
 * Common base class for all objects that appear in a Canvas.
 */
public abstract class Node extends CanvasElement {


    private function clamp(n:Number, min:Number, max:Number):Number{
       return Math.max(Math.min(n, max), min);
    }

    attribute transformFilter: SGTransform.Affine;
    private attribute alignmentFilter: SGAlignment;
    private attribute compositeFilter: SGComposite;
    private attribute clipFilter: SGClip;
    private attribute antialiasClip: Boolean = false;
    private attribute effectFilter: SGEffect;
    private attribute contentNode: SGNode;

    protected attribute bounds: Rectangle2D;
    private attribute focusListener: FocusListener;
    

    private function getScreenLocation(component:java.awt.Component): java.awt.Point{
        var comp = component;
        while (comp <> null and not (comp instanceof java.awt.Frame) and 
               not (comp instanceof java.awt.Window) and
               not (comp instanceof java.applet.Applet)) {
            comp = comp.getParent();
        }
        return comp.getLocationOnScreen();
    }


    public function getNode(): SGNode {
        if (alignmentFilter == null) {
            var canvas = this.getCanvas();
            contentNode = this.createNode();
            effectFilter = new SGEffect();
            effectFilter.setChild(contentNode);
            compositeFilter = new SGComposite();
            compositeFilter.setChild(effectFilter);
            clipFilter = new SGClip();
            clipFilter.setChild(compositeFilter);
            transformFilter = SGTransform.createAffine(new AffineTransform(), clipFilter);
            transformFilter.putAttribute("fx", this);
            //transformFilter.addNodeListener(Node.LISTENER);
            alignmentFilter = new SGAlignment();
            alignmentFilter.setChild(transformFilter);

            if (halign <> null) {
                alignmentFilter.setHorizontalAlignment(halign.id.intValue());
            }
            if (valign <> null) {
                alignmentFilter.setVerticalAlignment(valign.id.intValue());
            }
            alignmentFilter.setVisible(visible);
            if (opacity <> 1.0) {
                compositeFilter.setOpacity(clamp(opacity, 0, 1).floatValue());
            }
            //if (filter <> null) {
                //JXFC-XXX com.sun.javafx.runtime.sequence.Sequence<java.awt.image.BufferedImageOp>
                //effectFilter.setImageOps(for(i in filter)i.getFilter());
            //}
            if (toolTipText <> null) {
                //alignmentFilter.setToolTipText(toolTipText); // TODO: hmm
            }
            if (id <> null) {
                alignmentFilter.putAttribute("id", id); // TODO: use ID
            }
            var b = alignmentFilter.getBounds();
            currentX = b.getX();
            currentY = b.getY();
            currentWidth = b.getWidth();
            currentHeight = b.getHeight();
        }
        return alignmentFilter;
    }

    protected abstract function createNode(): SGNode;
    

    //TODO: implement properly...
    public attribute toolTipText: String on replace  {
    /*
        if (alignmentFilter <> null) {
            alignmentFilter.setToolTipText(value);
        }
    */
    };


    /** Determines whether this node responds to mouse events, or other picking functions. */
    public attribute selectable: Boolean  = true on replace  {
        // TODO: are these needed
        /*
        sg.setPickable(value);
        tg.setPickable(value);
        ag.setPickable(value);
        */
    };

    /**
     * An optional list of Filters that will be applied to this node. 
     * If present whenever the content of this node changes a new buffered 
     * image will be created consisting of the result of applying the list of 
     * filters (in order) to the new content. The generated image will then 
     * be used to paint the node.
    public attribute filter: Filter[]
        on insert [ndx] (f) {
            if (effectFilter <> null) {
                //JXFC-XXX com.sun.javafx.runtime.sequence.Sequence<java.awt.image.BufferedImageOp>
                //effectFilter.setImageOps(for(i in filter) i.getFilter());
                    
            }
        }
        on delete [ndx] (f)  {
            if (effectFilter <> null) {
                //JXFC-XXX com.sun.javafx.runtime.sequence.Sequence<java.awt.image.BufferedImageOp>
                //effectFilter.setImageOps(for(i in filter) i.getFilter());
            }
        }

        on replace [ndx] (f)  {
            if (effectFilter <> null) {
                //JXFC-XXX com.sun.javafx.runtime.sequence.Sequence<java.awt.image.BufferedImageOp>
                //effectFilter.setImageOps(for(i in filter) i.getFilter());
            }
        };
     */
    /** A number between 0 and 1, 0 being transparent and 1 opaque. */
    public attribute opacity: Number = 1.0 on replace {
        if (compositeFilter <> null) {
            compositeFilter.setOpacity(clamp(opacity, 0, 1).floatValue());
        }
    }

    /** Determines the horizontal alignment of this node relative to its origin. */
    attribute halign: HorizontalAlignment on replace  {
        if (halign == null) {
            //halign = HorizontalAlignment.LEADING;
        }
	if (alignmentFilter <> null and halign <> null) {
	    alignmentFilter.setHorizontalAlignment(halign.id.intValue());
	}
    }

    /** Determines the vertical alignment of this node relative to its origin. */
	public attribute valign: VerticalAlignment on replace {
        if (valign == null) {
            //valign = VerticalAlignment.TOP;
        }
	if (alignmentFilter <> null and valign <> null) {
	    alignmentFilter.setVerticalAlignment(valign.id.intValue());
	}
    };

    /** 
     * Determines whether this node is visible in the canvas. If set to 
     * false, this node and anything it contains will not be painted and
     * will not receive events.
     */
    public attribute visible: Boolean = true on replace {
	    if (alignmentFilter <> null) {
		alignmentFilter.setVisible(visible);
	    }
    };

    /** Read-only attribute returning the current x coordinate of this node relative to its parent. */
    public attribute currentX: Number;
    /** Read-only attribute returning the current y coordinate of this node relative to its parent. */
    public attribute currentY: Number;
    /** Read-only attribute returning the current width of this node. */
    public attribute currentWidth: Number;
    /** Read-only attribute returning the current height of this node. */
    public attribute currentHeight: Number;

    public attribute hover: Boolean;

    public attribute id: String on replace {
	if (contentNode <> null) {
	    contentNode.setID(id);   
	}
    };

    public function getCanvas(): Canvas {
        var n:CanvasElement = parentCanvasElement;
        while (n <> null) {
            if ((n as java.lang.Object) instanceof Canvas) {
	        return (n as java.lang.Object) as Canvas;
            }
            n = n.parentCanvasElement;
        }
        return null;
    }

}

