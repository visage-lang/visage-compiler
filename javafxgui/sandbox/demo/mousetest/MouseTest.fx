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

package demo.mousetest;

import javafx.gui.*;
import javafx.gui.swing.*;

var menuItems = [
    MenuItem{ text:"Exit" action:function(){java.lang.System.exit(0)} }
];


/* 6683803 - Node.blocksMouse attribute does not work for Groups
 * 
 * Verify that the blocksMouse attribute set on the Group, prevents mouse events
 * that occur on one of the red foreground rectangles from being dispatched to the
 * grey background rectangle.  In other words: if you click on a red rectangle, 
 * nothing should happen.
 */
        
var scene = [
    Rectangle {
        x:5 y:5 width:190 height:190 fill:Color.LIGHTGREY
        onMousePressed: function(e) { java.lang.System.out.println("Background") }
        onMouseWheelMoved: function(e:MouseEvent) { java.lang.System.out.println("Wheel {e.getWheelRotation()}") }
    },
    Group { 
        blocksMouse: true
        content: [
            Rectangle { x:10 y:10 width:50 height:50 fill:Color.CRIMSON },
            Rectangle { x:75 y:75 width:50 height:50 fill:Color.CRIMSON },
            Rectangle { x:140 y:140 width:50 height:50 fill:Color.CRIMSON },
        ]
    }
];

var frame = Frame {
         title: bind "Test Mouse Operations"
         width: 500
        height: 500
    background: Color.WHITE
         menus: [Menu{ text:"Actions" items:menuItems }]
       content: Canvas { content:[scene] }
       visible: true
}



