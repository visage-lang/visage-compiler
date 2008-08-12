/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxpad;

import java.lang.System;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.ext.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * @author jclarke
 */

class CloseButton extends CustomNode {
    var pressed: Boolean;
    public var action: function():Void;
    var group:Group;
    var pressHover: Boolean = bind pressed and group.isMouseOver();
    var gfill:Paint = bind if(group.isMouseOver()) then Color.GRAY else Color.color(.7, .7, .7, 1.0);
    var gstroke:Paint = bind if (pressHover) then Color.color(.8, .8, .8, 1) else Color.WHITE;
    override function create(): Node {
        group = Group {
            var r = 6;
            content: [
                Circle {
                    onMousePressed: function(e) {pressed = true;}
                    onMouseReleased: function(e) {
                            if (group.isMouseOver() and action != null) {
                                action();
                            } 
                            pressed = false;
                    }
                    //TODO selectable: true
                    //blocksMouse: true
                    centerX: r
                    centerY: r
                    radius: r
                    fill: bind gfill
                    stroke: bind gfill
                },
                Line {
                    startX: r/2
                    startY: r/2
                    endX: r + r/2
                    endY: r + r/2
                    stroke: bind gstroke
                },
                Line {
                    startX: r/2
                    startY: r + r/2
                    endX: r + r/2
                    endY: r/2
                    stroke: bind gstroke
                }            
            ]
        }
    }    
}

class SearchField  extends CustomNode {
    var columns: Integer = 13;
    public var fSearchValue: String;
    var action: function():Void;
    var cancel: function():Void;
    var textField: SwingTextField;
    var baseline: Number;
    var view:ComponentView;
    var dodgerBlue80:Color = Color {
            red: Color.DODGERBLUE.red 
            green: Color.DODGERBLUE.green
            blue: Color.DODGERBLUE.blue
            opacity: 0.8 };
    
    override function create(): Node {
       var self = this;
       var group =  Group {
            content:
            [Rectangle {
                height: bind view.getHeight() + 4
                width: bind view.getWidth() + 20
                arcHeight: 20
                arcWidth: 20
                stroke: bind if (textField.getJTextField().hasFocus()) then dodgerBlue80 else Color.GRAY
                strokeWidth: 2
                fill: Color.WHITE
            },
            Group {
                transform: Transform.translate(10, 2)
                content: view = ComponentView {
                    component: textField = SwingTextField {
                            font: Font.font("VERDANA", FontStyle.PLAIN, 12)
                            //TODO focused: true
                            columns: bind self.columns
                            //TODO border: EmptyBorder{}
                            background: Color.color(0, 0, 0, 0)
                            action: bind self.action
                            text: bind fSearchValue with inverse
                            //TODO - done in Swing below
                            /*****************
                            onKeyDown: function(e:KeyEvent):Void {
                                if (e.keyStroke == KeyStroke.ESCAPE) {
                                    if(cancel <> null) 
                                        cancel();
                                }
                            }
                            ***********/
                        }
                    //TODO baseline: self.baseline
                };
            }]
         };
         // WorkAround for onKeyDown function
         var tf = textField.getJTextField();
         tf.addKeyListener(KeyAdapter {
             override function keyPressed(e:KeyEvent):Void {
                   if (e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
                        if(cancel != null) 
                            cancel();
                    }
             }
         });
         // END WorkAround for onKeyDown function
         group;
      }
}
    
class SearchButton extends CustomNode {
    var icon: Image;
    var text: String;
    var height: Number;
    var baseline: Number;
    var font: Font =  Font.font("VERDANA", FontStyle.BOLD, 11);
    var action: function():Void;
    var pressed: Boolean;   
    var r:Rectangle;
    var grad = LinearGradient {
                    endX: 0
                    stops:
                    [Stop {
                        offset: 0
                        color: Color.color(.6, .6, .6, 1)
                    },
                    Stop {
                        offset: 1
                        color: Color.color(.75, .75, .75, 1)
                    }]
                };    
    
    override function create(): Node {
        var self = this;
        Group {
            var content = HBox {
                content:
                [ImageView {
                    image: bind icon
                    transform: bind Transform.translate(0, height/2)
                    verticalAlignment: VerticalAlignment.CENTER
                },
                Text {
                    font: bind font
                    //valign: VerticalAlignment.MIDDLE
                    verticalAlignment: VerticalAlignment.CENTER
                    transform: bind Transform.translate(12, baseline+5)
                    content: bind text
                    fill: bind if (mouseOver) then Color.WHITE else Color.BLACK
                    x: bind r.getX()
                }]
            }
            content:
            [r = Rectangle {
                onMousePressed: function(e) {pressed = true;}
                onMouseReleased: function(e) {
                    if (mouseOver and action != null) {
                        action();
                    }
                    pressed = false;
                }

                height: bind self.height
                width: bind content.getWidth() + 20
                arcHeight: 20
                arcWidth: 20
                //TODO selectable: true
                //blocksMouse: true
                fill: bind if (mouseOver) then grad as Paint else Color.TRANSPARENT as Paint
                stroke: bind if (pressed) then Color.GRAY else Color.TRANSPARENT
            },
            Group {
                transform: Transform.translate(5, 15)
                content: content
            }]
        };        
    }    
}

public class SearchPanel extends CustomNode {
    var closeAction: function();
    var searchNextAction: function();
    var searchPrevAction: function();
    var highlightAllAction: function();
    public var pSearchValue: String;
    public var matchCase: Boolean on replace {
        bmatchCase = matchCase;
    };
    public var bmatchCase: Boolean on replace {
        matchCase = bmatchCase;
    }
    public var searchField:SearchField;
    var open: Boolean on replace {
        if(open and searchField != null) {
            searchField.requestFocus();
        }
    }
    override function create(): Node {
       
        var self = this;
        searchField = SearchField {
            transform: Transform.translate(10, 0)
            fSearchValue: bind self.pSearchValue with inverse
            cancel: bind closeAction
        };   
        HBox {

            content:
            [CloseButton {
                transform: Transform.translate(5, 24)
                action: bind closeAction
                verticalAlignment: VerticalAlignment.CENTER
            },
            Text {
                transform: bind Transform.translate(10, 20-searchField.baseline)
                verticalAlignment: VerticalAlignment.CENTER
                font: Font.font("VERDANA", FontStyle.BOLD, 11)
                content: "Find:"
            },
            searchField,
            SearchButton {
                height: 18
                baseline: bind searchField.baseline
                transform: Transform.translate(5, 2)
                text: "Next"
                action: bind searchNextAction
            },
            SearchButton {
                height: 18
                baseline: bind searchField.baseline
                transform: Transform.translate(5, 2)
                text: "Previous"
                action: bind searchPrevAction
            },
            SearchButton {
                height: 18
                baseline: bind searchField.baseline
                transform: Transform.translate(5, 2)
                text: "Highlight all"
                action: bind highlightAllAction
            },
            ComponentView {
                transform: Transform.translate(0, -1)
                component: SwingCheckBox {
                    text: "Match Case",
                    font: Font.font("VERDANA", FontStyle.BOLD, 11),
                    //TODO focusable: false
                    selected: bind bmatchCase with inverse
                }
            },
            Text {
                visible: false
                transform: bind Transform.translate(0, 2+searchField.baseline)
                verticalAlignment: VerticalAlignment.CENTER
                font: Font.font("VERDANA", FontStyle.BOLD, 11)
                content: "Match case"
            },
            SearchButton {
                visible: false
                height: 18
                transform: Transform.translate(5, 2)
                text: "Replace"
            },
            SearchButton {
                visible: false
                height: 18
                transform: Transform.translate(5, 2)
                text: "Replace All"
            }]
        };    
        
    }
}
