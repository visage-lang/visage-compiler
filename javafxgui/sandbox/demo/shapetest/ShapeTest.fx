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

package demo.shapetest;

import javafx.gui.*;
import javafx.gui.swing.*;

var doUnion = function():Void {
    resultText = "A + B";
    resultShape = ShapeSubtract { a:[shapeA, shapeB] fill:Color.LIGHTGREY };
};

var doSubtract = function():Void {
    resultText = "A - B";
    resultShape = ShapeSubtract { a:[shapeA] b:[shapeB] fill:Color.LIGHTGREY };
};

var doIntersect = function():Void {
    resultText = "A X B";
    resultShape = ShapeIntersect { a:[shapeA] b:[shapeB] fill:Color.LIGHTGREY };
};

var doXOR = function():Void {
    resultText = "A XOR B";
    var union = ShapeSubtract { a:[shapeA, shapeB] };
    var intersection = ShapeIntersect { a:[shapeA] b:[shapeB] };
    resultShape = ShapeSubtract { a:[union] b:[intersection] fill:Color.LIGHTGREY };
};

var menuItems = [
    MenuItem{ text:"Union" action:doUnion },
    MenuItem{ text:"Subtract" action:doSubtract },
    MenuItem{ text:"Intersect" action:doIntersect },
    MenuItem{ text:"XOR" action:doXOR },
    MenuItem{ text:"Exit" action:function(){java.lang.System.exit(0)} }
];

var shapeA = Polygon {
    points: [0,0, 100,0, 0,100]
      fill: Color.CRIMSON
};

var shapeB = Polygon {
    points: [0,0, 100,0, 100,100]
      fill: Color.INDIGO
};

var resultShape:Shape = ShapeSubtract { 
       a: [shapeA, shapeB] 
    fill: Color.LIGHTGREY
};

var resultText = "A + B";

class LabelText extends Text {
    init {
        verticalAlignment = VerticalAlignment.CENTER;
        horizontalAlignment = HorizontalAlignment.CENTER;
        x = 50;
        y = 50;
        font = Font{size: 30}
    }
}

class BoundsBox extends Rectangle {
    init {
        x = 0;
        y = 0;
        width = 100;
        height = 100;
        strokeWidth = 1;
        stroke = Color.GREEN;
        strokeDashArray = [2,2];
    }
}

var scene = [
    Group { 
        translateX: 20
        translateY: 20
        content: [shapeA, BoundsBox{}, LabelText { content:"A" }]
    },
    Group { 
        translateX: 140
        translateY: 20
        content: [shapeB, BoundsBox{}, LabelText { content:"B" }]
    },
    Group { 
        translateX: 260
        translateY: 20
        content: bind [resultShape, BoundsBox{}, LabelText { content: bind resultText }]
    }
];

var frame = Frame {
         title: bind "Test Shape Operations"
         width: 500
        height: 500
    background: Color.WHITE
         menus: [Menu{ text:"Actions" items:menuItems }]
       content: Canvas { content:[scene] }
       visible: true
}



