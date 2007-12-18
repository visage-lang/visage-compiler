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
//TODO BATIK
//TODO BATIK import net.java.javafx.ui.batik.PathLength;

/**
 * The <code>Shape</code> interface provides definitions for objects 
 * that represent some form of geometric shape.
 */
public abstract class Shape extends VisualNode, AbstractPathElement {
    protected attribute sgshape: SGShape;
    protected abstract function createShape(): SGShape; // TODO: could return Shape here instead?
    public function getShape(): SGShape{
        if (sgshape == null) {
            sgshape = this.createShape();
            sgshape.setAntialiasingHint(<<java.awt.RenderingHints>>.VALUE_ANTIALIAS_ON);
        }
        return sgshape;
    }
    public function getTransformedShape(): java.awt.Shape{
        return if (awtTransformedShape == null) then getAWTShape() else awtTransformedShape;
    }
    public attribute outline: Boolean = false;
    public function length(): Number {
        var shape = this.getTransformedShape();
        if (shape <> null) {
            System.out.println("Shape.length() //TODO BATIK");
            //TODO BATIK return pathLength.lengthOfPath();
        }
        return java.lang.Double.NaN;
    }
    public function pointAt(length: Number): java.awt.geom.Point2D {
        var shape = this.getTransformedShape();
        if (shape <> null) {
            System.out.println("Shape.pointAt() //TODO BATIK");
            //TODO BATIK return pathLength.pointAtLength(length);
        }
        return null;
    }
    public function angleAt(length: Number): Number {
        var shape = this.getTransformedShape();
        if (shape <> null) {
            System.out.println("Shape.angleAt() //TODO BATIK");
            //TODO BATIK var angle = pathLength.angleAtLength(length);
            //TODO BATIK return Math.toDegrees(angle);
        }
        return java.lang.Double.NaN;
    }
    public function transformAt(length: Number): Transform[] {
        var pt = pointAt(length);
        var angle = angleAt(length);
        //TODO JXFC-339
        System.out.println("Shape.transformAt() //TODO JFXC-339");
        return [/*****Transform.CompositeTransform {
            transforms: bind [
                Translate.translate(pt.getX(), pt.getY()) as Transform, 
                Rotate.rotate(angle, 0, 0) as Transform] 
        } as Transform *****/];
       
    }

    public function toPath(): Path{
        var path = Path {};
        System.out.println("Shape.toPath() //TODO BATIK");
       //TODO BATIK 
       /****************
        var p = new PathLength(getTransformedShape());
       var segs = p.getSegments();
       
       for (seg in segs) {
           if (seg.getSegType() == PathIterator.SEG_MOVETO) {
               insert MoveTo {x: seg.getX(), y: seg.getY()} into path.d;
           } else if (seg.getSegType() == PathIterator.SEG_LINETO) {
               insert LineTo {x: seg.getX(), y: seg.getY()} into path.d;
           } else {
               // close
               insert ClosePath into path.d;
           }
       }
       *******************/
       return path;
    }
    public attribute fillRule: FillRule;
    //TODO BATIK  private attribute pathLength: PathLength = bind lazy new PathLength(awtTransformedShape);
    private attribute awtTransformedShape: java.awt.Shape  = 
                bind lazy getAWTTransformedShape(awtTransform, awtShape);
    private attribute awtShape: java.awt.Shape;
    private attribute awtTransform: AffineTransform;
    //private attribute transformListener: ZTransformListener; // TODO: implement?
    private function getAWTTransformedShape(t:AffineTransform, s:java.awt.Shape):java.awt.Shape{
        return t.createTransformedShape(s);
    }
    // TODO: implement properly
    private function getAWTShape(): java.awt.Shape{
        getNode();
    /*
        if (transformListener == null) {
            var self = this;
            zshape.addChangeListener(new ChangeListener() {
                    function stateChanged(e) {
                        self.awtShape = null;
                        self.awtShape = self.zshape.getShape();
                    }
            });
            transformListener = new ZTransformListener() {
                    function transformChanged(e) {
                        self.awtTransform = self.tg.getTransform();
                    }
                };
            tg.addTransformListener(transformListener);
        }
    */
        awtTransform = new AffineTransform();
        transformFilter.getTransform(awtTransform);
        awtShape = getShape().getShape();
        return awtTransformedShape;
    }

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

    public attribute selectable: Boolean = false;
}





