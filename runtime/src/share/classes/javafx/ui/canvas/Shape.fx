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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.lang.System;
import javafx.ui.XY;
import javafx.ui.canvas.AbstractPathElement;
import javafx.ui.canvas.VisualNode;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGAbstractShape;
import javafx.ui.canvas.Transform.CompositeTransform;
import com.sun.javafx.api.ui.path.ext.awt.geom.PathLength;
import com.sun.javafx.api.ui.path.ext.awt.geom.PathLength.PathSegment;

/**
 * The <code>Shape</code> interface provides definitions for objects 
 * that represent some form of geometric shape.
 */
public abstract class Shape extends VisualNode, AbstractPathElement {
    override attribute selectable = false;

    protected attribute sgshape: SGShape;
    protected abstract function createShape(): SGShape; // TODO: could return Shape here instead?
    public function getShape(): SGShape {
        if (sgshape == null) {
            sgshape = this.createShape();
            // fix  me: this fails to handle modifications to shape
            awtTransformedShape = affineTransformHACK.createTransformedShape(sgshape.getShape());
            sgshape.setAntialiasingHint(java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        }
        return sgshape;
    }
//TODO: Hack until JFXC-675 is addressed
    protected attribute affineTransformHACK :AffineTransform on replace {
            // fix  me: this fails to handle modifications to shape
           awtTransformedShape = 
               affineTransformHACK.createTransformedShape(getShape().getShape());
    }
    public function getTransformedShape(): java.awt.Shape {
        getNode(); // hack
        if (awtTransformedShape == null) {
            var t = affineTransformHACK ;
            var s = getShape().getShape();
            if (t <> null) {
                awtTransformedShape = t.createTransformedShape(s);
            } else {
                awtTransformedShape = s;
            }
        }
        return awtTransformedShape;
    }
    public attribute outline: Boolean = false;
    public function length(): Number {
        var shape = this.getTransformedShape();
        if (shape <> null) {
            return pathLength.lengthOfPath();
        }
        return java.lang.Double.NaN;
    }
    public function pointAt(length: Number): java.awt.geom.Point2D {
        var shape = this.getTransformedShape();
        if (shape <> null) {
            return pathLength.pointAtLength(length.floatValue());
        }
        return null;
    }
    public function angleAt(length: Number): Number {
        var shape = this.getTransformedShape();
        if (shape <> null) {
            var angle = pathLength.angleAtLength(length.floatValue());
            return Math.toDegrees(angle);
        }
        return java.lang.Double.NaN;
    }
    public function transformAt(length: Number): Transform[] {
        var pt = pointAt(length);
        var angle = angleAt(length);
        return [Transform.CompositeTransform {
            transforms: bind [
                Transform.translate(pt.getX(), pt.getY()), 
                Transform.rotate(angle, 0, 0)] 
        } ];
    }

    public function toPath(): Path{
       var path = Path {};
       var p = new PathLength(getTransformedShape());
       //var segs:PathLength.PathSegment[] = p.getSegments();
       var numSegs = p.getNumberOfSegments();
       //for (seg in segs) {
       for (i in [0..<numSegs]) {
           var seg = p.getSegment(i);
           if (seg.getSegType() == PathIterator.SEG_MOVETO) {
               insert MoveTo {x: seg.getX(), y: seg.getY()} into path.d;
           } else if (seg.getSegType() == PathIterator.SEG_LINETO) {
               insert LineTo {x: seg.getX(), y: seg.getY()} into path.d;
           } else {
               // close
               insert ClosePath{} into path.d;
           }
       }
       return path;
    }
    public attribute fillRule: FillRule;
    private attribute pathLength: PathLength = bind lazy new PathLength(awtTransformedShape);
    private attribute awtTransformedShape: java.awt.Shape;
    
    /*
    function Shape.transformAtUnitInterval(ui: Number) {
        return transformAt(ui*length());
    }
    */

    protected function addTo(gp:GeneralPath):Void {
        var tshape = this.getTransformedShape();
        if (outline) {
            var s = new BasicStroke(strokeWidth.floatValue(),
                       strokeLineCap.id.intValue(),
                       strokeLineJoin.id.intValue(),
                       strokeMiterLimit.floatValue(),
                       strokeDashArray,
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





