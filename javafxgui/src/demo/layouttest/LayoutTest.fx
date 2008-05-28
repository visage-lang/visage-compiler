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

package demo.layouttest;

import javafx.gui.*;

class Disk extends CustomNode {

    attribute color:Color;
    attribute name:String;

    protected function create():Node {
        VBox { content: [
            Circle {
             onMousePressed: function(e):Void { toBack(); }
                     radius: bind circleRadius
                    centerX: bind circleRadius
                    centerY: bind circleRadius
                strokeWidth: 3
                     stroke: Color.LIGHTGREY;
                       fill: bind color
                     smooth: true
                    },
            Text { 
                    content:name 
                          x: bind circleRadius
                 textOrigin: TextOrigin.TOP
                       fill: Color.DARKGREY
        horizontalAlignment: HorizontalAlignment.CENTER
            } ]
        }
    }

    public function toString():String { "Disk-{name}" }
}


var circleRadius:Number = 20;
var circleSpacing:Number = 10;

var growCircleRadius = function():Void {
    circleRadius += 5;
}


var growCircleSpacing = function():Void {
    circleSpacing += 10;
}

var vDisks = VBox {
    content: [
        Disk { color:Color.INDIGO name:"vbIndigo"}, 
        Disk { color:Color.CRIMSON name:"vbCrimson"}, 
        Disk { color:Color.BLACK name:"vbBlack"}
    ]
    spacing: bind circleSpacing
};

var hDisks = HBox {
      content: [
        Disk { color:Color.INDIGO name:"hbIndigo"}, 
        Disk { color:Color.CRIMSON name:"hbCrimson" visible:false}, 
        Disk { color:Color.BLACK name:"hbBlack" }
      ]
      spacing: bind circleSpacing
   translateX: 100
     rotation: 25
};

var hRectangle = Rectangle {
           x: bind hDisks.getBoundsX() - 5
           y: bind hDisks.getBoundsY() - 5
       width: bind hDisks.getBoundsWidth() + 10
      height: bind hDisks.getBoundsHeight() + 10
 strokeWidth: 2
      stroke: Color.INDIGO
};

var scene = [
    Group { 
          content: [vDisks, hDisks, hRectangle]
       translateX: 50
       translateY: 50
    }
];


var menuItems = [
    MenuItem{ text:"Grow Circle Spacing" action:growCircleSpacing },
    MenuItem{ text:"Grow Circle Radius" action:growCircleRadius },
    MenuItem{ text:"Exit" action:function(){java.lang.System.exit(0)} }
];

var frame = Frame {
         title: bind "Test VBox, HBox"
         width: 500
        height: 500
    background: Color.WHITE
         menus: [Menu{ text:"Actions" items:menuItems }]
       content: Canvas { content:[scene] }
       visible: true
}



