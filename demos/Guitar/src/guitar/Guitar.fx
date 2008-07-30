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
package guitar;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.input.*;
import javafx.ext.swing.*;
import java.lang.System;



public class Guitar extends CustomNode {
    private attribute startX = 0.0;
    private attribute startY = 0.0;
    attribute loadingSound: Number;
    function play(string:GuitarString){
        string.audioClip.stop();
        string.audioClip.play();
    }
    override function create(): Node {
        var self = this;
        Group {
            onMouseDragged: function(e:MouseEvent):Void {
                translateX = e.getDragX() - startX;
                translateY = e.getDragY() - startY;
            }
            onMousePressed: function(e:MouseEvent):Void {
                startX = e.getDragX() - translateX;
                startY = e.getDragY() - translateY;
              
            }
            content:
                [ImageView { 
                    transform:[Transform.translate(0, 49)]
                    image: Image{
                        backgroundLoading: true, 
                        url: "{__DIR__}Resources/Pickup.png"
                    }
                },
                GuitarString {
                    theGuitar: self
                    transform: [Transform.translate(58, 42)]
                    note: "E"
                },
                GuitarString {
                    theGuitar: self
                    transform: [Transform.translate(90, 42)]
                    note: "A"
                },
                GuitarString {
                    theGuitar: self
                    transform: [Transform.translate(122, 42)]
                    note: "D"
                },
                GuitarString {
                    wound: false
                    theGuitar: self
                    transform: [Transform.translate(155, 42)]
                    note: "G"
                },
                GuitarString {
                    theGuitar: self
                    transform: [Transform.translate(187, 42)]
                    wound: false
                    note: "B"
                },
                GuitarString {
                    theGuitar: self
                    transform: [Transform.translate(219, 42)]
                    wound: false
                    note: "EE"
                },
                ImageView { 
                    transform: [Transform.translate(9, 0)]
                    image: Image{
                        backgroundLoading: true, 
                        url: "{__DIR__}Resources/EADGBE.png"
                    }
                },
                ImageView { 
                    visible: false
                    transform: [Transform.translate(9, 0)]
                    image: Image{
                        backgroundLoading: true, 
                        url: "{__DIR__}Resources/DADGBE.png"
                    }
                },
                ImageView { 
                    transform: [Transform.translate(9, 201)]
                    image: Image{
                        backgroundLoading: true, 
                        url: "{__DIR__}Resources/Standard.png"
                    }
                },
                ImageView { 
                    visible: false
                    transform: [Transform.translate(9, 201)]
                    image: Image{
                        backgroundLoading: true, 
                        url: "{__DIR__}Resources/DropD.png"
                    }
                },
                Text {
                    font: Font{name:"Verdana", style:FontStyle.BOLD, size:10}
                    content: "Loading Sound..."
                    fill: bind if (loadingSound > 0) then Color.WHITE else Color.TRANSPARENT
                    transform: [Transform.translate(131, -30)]
                    horizontalAlignment: HorizontalAlignment.CENTER
                }]
            };   
    }
}





SwingFrame {
    title: "Guitar Tuner"
    height: 500
    width: 600
    closeAction: function() {System.exit(0);}
    visible: true
    background: Color.BLACK
    content: Canvas {
        
        background: Color.BLACK
        content:
        [ImageView {
            cursor: Cursor.DEFAULT
            image: Image{url: "http://dontipton.com/myPictures/guitar-lights-neck-circle.jpg"}
        },
        ComponentView {
            cursor: Cursor.DEFAULT
            opacity: 0.6
            transform: [Transform.translate(20, 20), Transform.scale(0.5, 0.5)]
            component: Label {
                font: Font{name:"Tahoma", style:FontStyle.BOLD, size:20}
                foreground: Color.WHITE
                text:
                "<html>
                   <body>
                   <table>
                   <tr>
                   <td>
                   <img src='http://l.yimg.com/static.widgets.yahoo.com/133/images/minichit_missing.png'></img>
                   </td> 
                   <td width='150'>
                   JavaFX Clone of Yahoo Widget Engine Guitar Tuner Widget
                   </td>
                   </tr>
                </table>"
            }
        },
        Guitar {
            cursor: Cursor.DEFAULT
            transform: bind [Transform.translate(300.0, 200.0) ]
            verticalAlignment: VerticalAlignment.CENTER
            horizontalAlignment: HorizontalAlignment.CENTER
        }]
    }
}
