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

package com.sun.oss.radar;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;
import java.awt.Point;

/**
 * @author jclarke
 */

var canvas:Canvas;

Frame {
    onClose: function() { System.exit(0); }
    visible: true
    title: "JavaFX Radar"
    height: 1000
    width: 1000
    content: canvas = Canvas {
        background: Color.BLACK
         content: RadarScreen {
             height: bind canvas.height
             width : bind canvas.width
             blips: [
                 Airplane{
                     location: Location{x:500,y:500}
                     text: "0"
                     course: 0
                 },
                 Airplane{
                     location: Location{x:500,y:500}
                     text: "90"
                     course: 90
                 },
                 Airplane{
                     location: Location{x:500,y:500}
                     text: "180"
                     course: 180
                 },
                 Airplane{
                     location: Location{x:500,y:500}
                     text: "270"
                     course: 270
                 },
                 Airplane{
                     location: Location{x:500,y:500}
                     text: "45"
                     course: 45
                 }                 
             ]
         }
    }
}
