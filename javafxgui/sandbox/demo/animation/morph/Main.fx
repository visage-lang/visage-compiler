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
package demo.animation.morph;

import javafx.animation.*;
import javafx.gui.*;
import java.lang.System;

var shape1 = Rectangle { x: 10 y: 10 width: 200 height: 100 };
var shape2 = Circle { centerX: 150 centerY: 150 radius: 75 };
var shape3 = Path {
    elements: [
        MoveTo { x: 200 y: 200 },
        LineTo { x: 280 y: 200 },
        LineTo { x: 240 y: 280 },
        ClosePath {}
    ]
};
var shape4 = Text {
    font: Font { size: 100 style: FontStyle.BOLD }
    x: 25
    y: 150
    content: "Hello"
};

var color = Color.BLUE;
var geom:Shape = shape1;
var strokeW = 10;
var stroke = Color.WHITE;

var t = Timeline {
    repeatCount: Timeline.INDEFINITE
    autoReverse: true
    keyFrames: [
        KeyFrame {
            time: 2s
            values: [
                geom => shape2 tween Interpolator.EASEIN,
                color => Color.RED tween Interpolator.LINEAR,
                strokeW => 10
            ]
        },
        KeyFrame {
            time: 4s
            values: [
                geom => shape3 tween Interpolator.LINEAR,
                color => Color.YELLOW tween Interpolator.LINEAR,
                strokeW => 0 tween Interpolator.LINEAR
            ]
            action: function() {
                stroke = if (stroke == null) Color.WHITE else null;
            }
        },
        KeyFrame {
            time: 6s
            values: [
                geom => shape4 tween Interpolator.EASEOUT,
                color => Color.WHITE tween Interpolator.LINEAR
            ]
        },
    ]
};

Frame {
    closeAction: function() {System.exit(0);}
    title: "Morph Demo";
    visible: true
    content:
    Canvas {
        preferredSize: [300,300]
        background: Color.BLACK
        content:
        DelegateShape {
            shape: bind geom
            fill: bind color
            stroke: bind stroke
            strokeWidth: bind strokeW
        }
    }
}

t.start();
