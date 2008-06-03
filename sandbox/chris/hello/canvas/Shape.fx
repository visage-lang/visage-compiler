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


import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.lang.Math;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGAbstractShape;


/**
 * The <code>Shape</code> interface provides definitions for objects 
 * that represent some form of geometric shape.
 */
public abstract class Shape extends VisualNode {
    protected attribute sgshape: SGShape;
    protected abstract function createShape(): SGShape; 
    public function getShape(): SGShape{
        if (sgshape == null) {
            sgshape = this.createShape();
            sgshape.setAntialiasingHint(<<java.awt.RenderingHints>>.VALUE_ANTIALIAS_ON);
        }
        return sgshape;
    }
    public attribute fillRule: FillRule;
    private attribute awtShape: java.awt.Shape;
    private attribute awtTransform: AffineTransform;
    public attribute outline: Boolean = false;

    // TODO: implement properly
    private function getAWTShape(): java.awt.Shape{
        getNode();
        awtTransform = new AffineTransform();
        transformFilter.getTransform(awtTransform);
        awtShape = getShape().getShape();
        return awtShape;
    }

    protected function addTo(gp:GeneralPath):Void {
        var tshape = this.getShape().getShape(); //this.getTransformedShape();
        if (outline) {
            var s = new BasicStroke(strokeWidth.floatValue(),
                                    strokeLineCap.id.intValue(),
                                    strokeLineJoin.id.intValue(),
                                    strokeMiterLimit.floatValue(),
                                    null, //for (i in strokeDashArray) i.floatValue(),
                                    strokeDashOffset.floatValue());
            gp.append(s.createStrokedShape(tshape), false);
        } else {
            gp.append(tshape, false);
        }
    }

    public function createVisualNode(): SGAbstractShape {
        return this.getShape();
    }

}





