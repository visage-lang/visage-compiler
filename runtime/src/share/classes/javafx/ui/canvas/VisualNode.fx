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

/**
 * Abstract base class for visual elements that appear in the canvas.
 */
public abstract class VisualNode extends Node, AbstractVisualNode {
    private attribute sgvisualnode: SGAbstractShape;
    private attribute awtStroke: java.awt.Paint //TODO JXFC-211  = bind stroke.getPaint()
        on replace  {
            if (sgvisualnode <> null and awtStroke <> null) {
                sgvisualnode.setDrawPaint(awtStroke);
            }
            updateMode();
        };
    private attribute awtFill: java.awt.Paint //TODO JXFC-211= bind fill.getPaint() 
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
            //TODO JXFC-211
            /*******************
            sgvisualnode.setDrawStroke(new BasicStroke(strokeWidth,
                                                       strokeLineCap.id,
                                                       strokeLineJoin.id,
                                                       strokeMiterLimit,
                                                       strokeDashArray,
                                                       strokeDashOffset));
            **************************/
            updateMode();
        }
    }

    protected function createNode(): SGNode {
        sgvisualnode = this.createVisualNode();

        if (awtFill <> null) {
            sgvisualnode.setFillPaint(awtFill);
        }
        if (awtStroke <> null) {
            //TODO JXFC-211  sgvisualnode.setDrawPaint(stroke.getPaint());
        }
        updateStroke(); // will also call updateMode()...

        return sgvisualnode;
    }

    init {
        //TODO JXFC-211
        //strokeWidth = 1.0;
        //strokeLineCap = StrokeLineCap.SQUARE;
        //strokeLineJoin = StrokeLineJoin.MITER;
        //strokeMiterLimit = 10.0;
        //strokeDashArray = [];
        //strokeDashOffset = 0.0;
    }
}


