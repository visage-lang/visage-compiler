/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
package javafx.gui;

import com.sun.scenario.animation.ShapeEvaluator;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGAbstractShape;
import com.sun.scenario.scenegraph.SGAbstractGeometry;
import com.sun.scenario.scenegraph.SGText;
import java.awt.BasicStroke;
import java.lang.Object;
import javafx.animation.Interpolatable;


// PENDING_DOC_REVIEW
/**
 * The {@code Shape} class provides definitions for common shapes objects that
 * represent some form of geometric shapes giving their stroke and fill style.
 * 
 * @profile common
 */              
public abstract class Shape extends Node, Interpolatable {

    function getSGAbstractShape():SGAbstractShape { 
        getSGNode() as SGAbstractShape 
    }

    private attribute initializing:Boolean = true;

    init { initializing = false; updateStroke(); }

    private function updateStroke():Void {  // update stroke
        if (initializing) { return; }
        getSGAbstractShape().setDrawStroke(if (sizeof strokeDashArray == 0)
            new BasicStroke(
                strokeWidth.floatValue(),
                strokeLineCap.getToolkitValue(),
                strokeLineJoin.getToolkitValue(),
                strokeMiterLimit.floatValue()) 
            else new BasicStroke(
                strokeWidth.floatValue(),
                strokeLineCap.getToolkitValue(),
                strokeLineJoin.getToolkitValue(),
                strokeMiterLimit.floatValue(),
                strokeDashArray,
                strokeDashOffset.floatValue()));
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines a square pen line width, the default value is 1.0.
     *
     * @profile common 
     */ 
    public attribute strokeWidth:Number = 1.0 on replace { updateStroke(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the decoration applied where path segments meet. 
     * The value must have one of the following values:
     * {@code StrokeLineJoin.BEVEL}, {@code StrokeLineJoin.MITER}, 
     * and {@code StrokeLineJoin.Round}.
     * The default value is {@code StrokeLineJoin.MITER}.
     *
     * @profile common
     */     
    public attribute strokeLineJoin:StrokeLineJoin = StrokeLineJoin.MITER on replace { updateStroke(); }
    
    // PENDING_DOC_REVIEW
    /**
     * The end cap style of this {@code Shape} as one of the following 
     * values that define possible end cap styles: 
     * {@code StrokeLineCap.BUTT}, {@code StrokeLineCap.ROUND}, 
     * and  {@code StrokeLineCap.SQUARE}.
     *
     * @profile common
     */     
    public attribute strokeLineCap:StrokeLineCap = StrokeLineCap.SQUARE on replace { updateStroke(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the limit for the {@code StrokeLineJoin.MITER} line join style. 
     * The default value is {@code 10.0}.
     *
     * @profile common
     */     
    public attribute strokeMiterLimit:Number = 10.0 on replace { updateStroke(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines a distance specified in user coordinates that represents 
     * an offset into the dashing pattern. In other words, the dash phase 
     * defines the point in the dashing pattern that will correspond 
     * to the beginning of the stroke. The default value is {@code 0.0}
     *
     * @profile common
     */     
    public attribute strokeDashOffset:Number = 0.0 on replace { updateStroke(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the array representing the lengths of the dash segments. 
     * Alternate entries in the array represent the user space lengths 
     * of the opaque and transparent segments of the dashes. 
     * As the pen moves along the outline of the {@code Shape} to be stroked, 
     * the user space distance that the pen travels is accumulated. 
     * The distance value is used to index into the dash array. 
     * The pen is opaque when its current cumulative distance maps 
     * to an even element of the dash array and transparent otherwise. 
     * The default value is {@code [1.0]}.
     *
     * @profile common
     */     
    public attribute strokeDashArray:Number[] = null on replace oldS[a..b] = newS { updateStroke(); }
   
     private function updateMode():Void {
        var mode = 
            if (fill <> null and stroke <> null) 
                { SGAbstractShape.Mode.STROKE_FILL }
            else if (fill <> null) 
                { SGAbstractShape.Mode.FILL }
            else 
                { SGAbstractShape.Mode.STROKE }
        getSGAbstractShape().setMode(mode);
    }

    // PENDING_DOC_REVIEW
    /**
    * Defines parameters to fill the interior of an {@code Shape} 
    * using the settings of the {@code Paint} context. 
    * The default value is {@code null}.
    *
    * @profile common
    */     
    public attribute fill:Paint on replace {
        if (fill <> null) {
            getSGAbstractShape().setFillPaint(fill.getAWTPaint());
        }
        updateMode();
    }

    // PENDING_DOC_REVIEW
    /**
    * Defines parameters of an {@code AbstractShape} stroke 
    * using the settings of the {@code Paint context}. 
    * The default value is {@code null}.
    *
    * @profile common
    */     
    public attribute stroke:Paint on replace {
        if (stroke <> null) {
            getSGAbstractShape().setDrawPaint(stroke.getAWTPaint());
        }
        updateMode();
    }

    // PENDING_DOC_REVIEW
    /**
    * Defines whether antialiasing hints are used or not for this {@code Shape}. 
    * If the value equals true the rendering hints are applied.
    * The default value is {@code true}.
    */
    public attribute smooth:Boolean = true on replace {
        var node = getSGAbstractShape();
        if (node instanceof SGAbstractGeometry) {
            var hint = if (smooth) 
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON else 
                java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
            (node as SGAbstractGeometry).setAntialiasingHint(hint);
        }
        else if (node instanceof SGText) {
            (node as SGText).setAntialiased(smooth);
        }
    }

    private attribute morphEvaluator:ShapeEvaluator;
    public function ofTheWay(endVal:Object, t:Number): Object {
        if (morphEvaluator == null) {
            morphEvaluator = ShapeEvaluator {};
        }
        var v1 = getSGAbstractShape().getShape();
        var v2 = (endVal as Shape).getSGAbstractShape().getShape();
        var s = DelegateShape {};
        s.getSGShape().setShape(morphEvaluator.evaluate(v1, v2, t.floatValue()));
        s
    }
}
