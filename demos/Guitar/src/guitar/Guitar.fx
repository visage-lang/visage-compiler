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
package guitar;

import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;



public class Guitar extends CompositeNode {
    attribute loadingSound: Number;
    function play(string:GuitarString){
        string.audioClip.stop();
        string.audioClip.play();
    }
    public function composeNode(): Node {
        var self = this;
        Group {
            var tx = Translate {x: 0, y: 0}
            transform: [tx]
            onMouseDragged: function(e:CanvasMouseEvent):Void {
                tx.x += e.localDragTranslation.x;
                tx.y += e.localDragTranslation.y;
            }
            content:
                [ImageView { 
                    transform:[Transform.translate(0, 49)]
                    image: Image{url: this.getClass().getResource("Resources/Pickup.png").toString()}
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
                    image: Image{url: this.getClass().getResource("Resources/EADGBE.png").toString()}
                },
                ImageView { 
                    visible: false
                    transform: [Transform.translate(9, 0)]
                    image: Image{url: this.getClass().getResource("Resources/DADGBE.png").toString()}
                },
                ImageView { 
                    transform: [Transform.translate(9, 201)]
                    image: Image{url: this.getClass().getResource("Resources/Standard.png").toString()}
                },
                ImageView { 
                    visible: false
                    transform: [Transform.translate(9, 201)]
                    image: Image{url: this.getClass().getResource("Resources/DropD.png").toString()}
                },
                Text {
                    font: Font{faceName:"Verdana", style:[FontStyle.BOLD], size:10}
                    content: "Loading Sound..."
                    fill: bind if (loadingSound > 0) then Color.WHITE else Color.BLACK
                    transform: [Transform.translate(131, -30)]
                    halign: HorizontalAlignment.CENTER
                }]
            };   
    }
}





Frame {
    title: "Guitar Tuner"
    height: 500
    width: 600
    onClose: function() {System.exit(0);}
    visible: true
    background: Color.BLACK
    content: Canvas {
        cursor: Cursor.DEFAULT
        background: Color.BLACK
        content:
        [ImageView {
            preload: true
            image: Image{url: "http://dontipton.com/myPictures/guitar-lights-neck-circle.jpg"}
        },
        View {
            opacity: 0.6
            transform: [Transform.translate(20, 20), Transform.scale(0.5, 0.5)]
            content: Label {
                font: Font{faceName:"Tahoma", style:[FontStyle.BOLD], size:20}
                foreground: Color.WHITE
                text:
                "<html>
                   <body>
                   <table>
                   <tr>
                   <td>
                   <img src='http://widgets.yahoo.com/images/home/maintile/icon.gif'></img>
                   </td> 
                   <td width='150'>
                   JavaFX Clone of Yahoo Widget Engine Guitar Tuner Widget
                   </td>
                   </tr>
                </table>"
            }
        },
        Guitar {
            transform: bind [Transform.translate(300.0, 200.0) ]
            valign: VerticalAlignment.CENTER
            halign: HorizontalAlignment.CENTER
        }]
    }
}
