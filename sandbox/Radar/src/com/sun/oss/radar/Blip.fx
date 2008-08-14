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
import java.lang.Math;
import javafx.ui.animation.*;
import java.lang.System;

/**
 * @author jclarke
 */

public class Blip extends CompositeNode {
    
    public attribute image:Image = Image {url: "{__DIR__}images/blip.png"};
    public attribute text:String;
    public attribute foreground:Color = Color.YELLOW;
    public attribute location:Location on replace old {
        insert old before tail[0];
    };
    attribute tail:Location[];
    public attribute course:Number; // course in degrees
    attribute imageView:ImageView;
    attribute textNode:Text;
    attribute x:Number = bind location.x;
    attribute y:Number = bind location.y;
    
    // speed in pixels / ms
    attribute velocity:Number = 0.001;
    
    public function nextLocation(time:Number) {
        var tdelta = velocity * time;
        var radians = Math.toRadians((course - 270) * -1);
        var dx = Math.cos(radians) * tdelta;
        var dy = Math.sin(radians) * tdelta;
        location = Location{x: location.x - dx, y: location.y + dy};
    }
    
    attribute movement = Timeline {
        keyFrames:[ 
            KeyFrame { 
                keyTime: 0s, 
            },        
            KeyFrame { 
                keyTime: 1s, 
                action: function() {
                    nextLocation(1000);
                }          
            }
        ]
        repeatCount: java.lang.Double.POSITIVE_INFINITY 
    };
    
    public function composeNode(): Node {
        Group {
            transform: bind Transform.translate(x, y)
            onMouseClicked: function(e:CanvasMouseEvent):Void {
                //System.out.println("Clicked");
            }
            content: [
                Line {
                    x1: bind textNode.currentX + textNode.currentWidth/2
                    y1: bind textNode.currentY + textNode.currentHeight/2
                    x2: bind imageView.currentX + imageView.currentWidth/2
                    y2: bind imageView.currentY + imageView.currentHeight/2
                    stroke: Color.YELLOW
                },
                imageView = ImageView {
                    image: bind image
                    transform: bind Rotate{ angle: course }
                },
                textNode = Text {
                    fill: bind foreground as Paint
                    font: Font.Font("Courier", ["BOLD"], 12);
                    content: bind text
                    x: bind imageView.currentX -10
                    y: bind imageView.currentY -12
                },
                Text {
                        visible:bind hover
                        fill: Color.WHITE
                        font: Font.Font("Courier", ["BOLD"], 14);
                        content: bind "x: {x}, y: {y}, course: {course} velocity: {velocity}"
                        x: bind imageView.currentX +50
                        y: bind imageView.currentY +50
                 }                
            ]
        };
    }
    
    postinit {
        movement.start();
    }
    
    

}
