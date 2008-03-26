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
import java.lang.System;
import com.sun.scenario.scenegraph.SGAbstractShape;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;
import com.sun.scenario.scenegraph.SGNode;

/**
 * Abstract base class for visual elements that appear in the canvas.

 */
public abstract class VisualNode extends Node {
    
    // TODO MARK AS FINAL
    private attribute sgvisualnode: SGAbstractShape;
    
    public attribute stroke: Paint on replace {
        if(stroke <> null) {
            awtStroke = stroke.getPaint();
        } else {
            awtStroke = null;
        }
        updateStroke();
        updateMode();
    };
    public attribute fill: Paint on replace {
        if(fill <> null) {
            awtFill = fill.getPaint();
        } else {
            awtFill = null;
        }
        updateMode();
    };
    private attribute awtStroke: java.awt.Paint  //TODO:JFXC-329 = bind if (stroke <> null) then stroke.getPaint() else null
        on replace  {
            if (sgvisualnode <> null and awtStroke <> null) {
                sgvisualnode.setDrawPaint(awtStroke);
            }
            updateStroke();
        };
    private attribute awtFill: java.awt.Paint //TODO:JFXC-329 = bind if (fill <> null) then fill.getPaint() else null
        on replace {
            if (sgvisualnode <> null and awtFill <> null) {
                sgvisualnode.setFillPaint(awtFill);
            }
        };

    public abstract function createVisualNode(): SGAbstractShape;
    public function getVisualNode(): SGAbstractShape{
        if (sgvisualnode == null) {
            this.getNode();
        }
        return sgvisualnode;
    }

   private function updateMode() {
        if (sgvisualnode <> null) {
            if (awtFill <> null and awtStroke <> null) {
                sgvisualnode.setMode(SGAbstractShape.Mode.STROKE_FILL);
            } else if (awtStroke <> null) {
                 sgvisualnode.setMode(SGAbstractShape.Mode.STROKE);
            } else {
                // should have Mode.NONE
                // however there's not so use FILL to avoid
                // drawing a bogus stroke
                sgvisualnode.setMode(SGAbstractShape.Mode.FILL);
            }
        }
    }

    static private attribute TRANSPARENT_FILL:java.awt.Color = 
       new java.awt.Color(0, 0, 0, 0);

    static private attribute NO_STROKE = new BasicStroke(0.0.floatValue());

    public function updateStroke():Void {
        if (sgvisualnode <> null) {
            if (awtStroke <> null) {
                var basic = new BasicStroke(strokeWidth.floatValue(),
                                            strokeLineCap.id.intValue(),
                                            strokeLineJoin.id.intValue(),
                                            strokeMiterLimit.floatValue(),
                                            strokeDashArray,
                                            strokeDashOffset.floatValue());
                sgvisualnode.setDrawStroke(basic);
            } else {
                sgvisualnode.setDrawStroke(NO_STROKE);
            }
        }
    }

    public function createNode(): SGNode {
        sgvisualnode = this.createVisualNode();
        if(fill <> null) {
            awtFill = fill.getPaint();
        }
        if (awtFill <> null) {
            sgvisualnode.setFillPaint(awtFill);
        }
        if(stroke <> null) {
            awtStroke = stroke.getPaint();
        }
        if (awtStroke <> null) {
            sgvisualnode.setDrawPaint(awtStroke);
        }
        updateStroke(); 
        updateMode();
        return sgvisualnode;
    }
    
    public attribute strokeWidth:Number = 1.0 on replace {
        updateStroke();
    };
    public attribute strokeLineJoin:StrokeLineJoin = StrokeLineJoin.MITER 
    on replace {
        updateStroke();
    };
    public attribute strokeLineCap:StrokeLineCap = StrokeLineCap.SQUARE 
    on replace {
        updateStroke();
    };
    public attribute strokeMiterLimit:Number = 10.0 on replace {
        updateStroke();
    };
    public attribute strokeDashArray:Number[] = [1.0] on replace [a..b] = slice {
        updateStroke();
    };
    public attribute strokeDashOffset:Number = 0.0 on replace {
        updateStroke();
    };

}
 


