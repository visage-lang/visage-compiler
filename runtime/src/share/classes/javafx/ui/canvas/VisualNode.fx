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


import java.awt.BasicStroke;
import javafx.ui.Paint;
import com.sun.scenario.scenegraph.SGAbstractShape;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.tools.javafx.ui.SequenceUtil;

/**
 * Abstract base class for visual elements that appear in the canvas.
 */
public abstract class VisualNode extends Node, AbstractVisualNode {
    private attribute sgvisualnode: SGAbstractShape;
    public attribute stroke: Paint on replace {
        if(stroke <> null) {
            awtStroke = stroke.getPaint();
        }
    };
    public attribute fill: Paint on replace {
        if(fill <> null) {
            awtFill = fill.getPaint();
        }
    };
    private attribute awtStroke: java.awt.Paint  //TODO:JFXC-329 = bind if (stroke <> null) then stroke.getPaint() else null
        on replace  {
            if (sgvisualnode <> null and awtStroke <> null) {
                sgvisualnode.setDrawPaint(awtStroke);
            }
            updateMode();
        };
    private attribute awtFill: java.awt.Paint //TODO:JFXC-329 = bind if (fill <> null) then fill.getPaint() else null
        on replace {
            if (sgvisualnode <> null and awtFill <> null) {
                sgvisualnode.setFillPaint(awtFill);
            }
            updateMode();
        };

    public abstract function createVisualNode(): SGAbstractShape;
    public function getVisualNode(): SGAbstractShape{
        if (sgvisualnode == null) {
            this.createNode();
        }
        return sgvisualnode;
    }

   private  function updateMode() {
        if (sgvisualnode <> null) {
            if (awtFill <> null and awtStroke <> null) {
                sgvisualnode.setMode(SGAbstractShape.Mode.STROKE_FILL);
            } else if (awtFill <> null) {
                sgvisualnode.setMode(SGAbstractShape.Mode.FILL);
            } else {
                sgvisualnode.setMode(SGAbstractShape.Mode.STROKE);
            }
        }
    }

    public function updateStroke():Void {
        if (sgvisualnode <> null) {
            var basic = new BasicStroke(strokeWidth.floatValue(),
                       strokeLineCap.id.intValue(),
                       strokeLineJoin.id.intValue(),
                       strokeMiterLimit.floatValue(),
                       SequenceUtil.sequenceOfDouble2floatArray(strokeDashArray),
                       strokeDashOffset.floatValue());
            sgvisualnode.setDrawStroke(basic);
            updateMode();
            
        }
    }

    public function createNode(): SGNode {
        sgvisualnode = this.createVisualNode();
        if(awtFill == null and fill <> null) {
            awtFill = fill.getPaint();
        }
        if (awtFill <> null) {
            sgvisualnode.setFillPaint(awtFill);
        }
        if(awtStroke == null and stroke <> null) {
            awtStroke = stroke.getPaint();
        }
        if (awtStroke <> null) {
            sgvisualnode.setDrawPaint(stroke.getPaint());
        }
        updateStroke(); // will also call updateMode()...

        return sgvisualnode;
    }
    
    public attribute strokeWidth:Number = 1.0;
    public attribute strokeLineJoin:StrokeLineJoin = StrokeLineJoin.MITER;
    public attribute strokeLineCap:StrokeLineCap = StrokeLineCap.SQUARE;
    public attribute strokeMiterLimit:Number = 10.0;
    public attribute strokeDashArray:Number[] = [1.0];
    public attribute strokeDashOffset:Number = 0.0;
}


