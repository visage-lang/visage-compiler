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


package javafx.ui;

import com.sun.javafx.runtime.awt.RadialGradientPaint;
import com.sun.javafx.api.ui.GradientFactory;

public class RadialGradient extends Gradient {
    public attribute radius:Number on replace {
        createGradient();
    };

    public attribute cx:Number on replace {
        createGradient();
    };
    public attribute cy:Number on replace {
        createGradient();
    };
    public attribute focusX: Number = -1 on replace {
        createGradient();
    };
    public attribute focusY: Number = -1 on replace {
        createGradient();
    };

    public function createGradient():Void {
        var fractions = getFractions();
        var colors = getColors();
        var fx = if(focusX == -1) then cx else focusX;
        var fy = if(focusY == -1) then cy else focusY;
        var center = new java.awt.geom.Point2D.Double(cx, cy);
        var focus = new java.awt.geom.Point2D.Double(fx, fy);
        //TODO JXFC-305
        //gradient = new RadialGradientPaint(center, radius.floatValue(), focus,
        //        fractions, colors, spreadMethod.id, colorSpace.id, affineTransform);
        gradient = GradientFactory.createRadialGradientPaint(center, radius.floatValue(), focus,
                fractions, colors, spreadMethod.id, colorSpace.id, affineTransform);
    }
}