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

import com.sun.javafx.api.ui.GradientFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LinearGradient extends Gradient {
    public attribute startX:Number on replace {
        createGradient();
    };
    public attribute startY:Number on replace {
        createGradient();
    };
    public attribute endX:Number on replace {
        createGradient();
    };
    public attribute endY:Number on replace {
        createGradient();
    };

    public function createGradient():Void {
        var fractions = getFractions();
        if ((endX <> startX or endY <> startY) or sizeof fractions >= 2) {
            
            var colors = getColors();
            var start = new java.awt.geom.Point2D.Double(startX, startY);
            var end = new java.awt.geom.Point2D.Double(endX, endY);
            if(start <> end) {
                gradient = GradientFactory.createLinearGradientPaint(start, end,
                    fractions, colors, spreadMethod.id, colorSpace.id, affineTransform);
            } else {
                Logger.getLogger(this.getClass().getName()).warning("The start point must be different from the end point, check attributes startX,startY and endX,endY");
            }
        }
    }
}