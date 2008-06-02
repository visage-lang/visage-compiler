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
package javafx.gui;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;

/**
 * The {@code DelegateShape} class is a variant of {@code Shape} that
 * inherits its geometry from another {@code Shape} instance.  It is
 * primarily useful as the scene graph representation for morphing shapes;
 * as in the following example:
 * <pre>
 *     var shape1 = Rectangle { x: 0 y: 0 width: 100 height: 50 };
 *     var shape2 = Circle { centerX: 30 centerY: 30 radius: 20 };
 *     var geom = shape1;
 *     Canvas {
 *         content:
 *         DelegateShape {
 *             shape: bind geom
 *             fill: Color.BLUE
 *         }
 *     }
 *     var t = Timeline {
 *         keyFrames: [
 *             KeyFrame { time: 0s values: geom => shape1 },
 *             KeyFrame { time: 2s values: geom => shape2 tween Interpolator.LINEAR },
 *         ]
 *     };
 *     t.start();
 * </pre>
 *
 * @profile common
 */              
public class DelegateShape extends Shape {

    function createSGNode():SGNode { new SGShape() }

    function getSGShape():SGShape { getSGNode() as SGShape }

    /**
     * Defines the {@code Shape} that provides the geometry for
     * this {@code DelegateShape}.  The initial value is null.
     *
     * @profile common 
     */ 
    public attribute shape:Shape on replace {
        getSGShape().setShape(shape.getSGAbstractShape().getShape());
    }
}
