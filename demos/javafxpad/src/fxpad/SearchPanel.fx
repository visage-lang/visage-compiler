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

package fxpad;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;

/**
 * @author jclarke
 */

class CloseButton extends CompositeNode {
    private attribute pressed: Boolean;
    public attribute action: function():Void;
    attribute group:Group;
    attribute pressHover: Boolean = bind pressed and group.hover;
    attribute gfill:Paint = bind if(group.hover) then Color.GRAY else Color.color(.7, .7, .7, 1.0);
    attribute gstroke:Paint = bind if (pressHover) then Color.color(.8, .8, .8, 1) else Color.WHITE;
    public function composeNode(): Node {
        group = Group {
            var r = 6;
            content: [
                Circle {
                    onMousePressed: function(e) {pressed = true;}
                    onMouseReleased: function(e) {
                            if (group.hover and action <> null) {
                                action();
                            } 
                            pressed = false;
                    }
                    selectable: true
                    cx: r
                    cy: r
                    radius: r
                    fill: bind gfill
                    stroke: bind gfill
                },
                Line {
                    x1: r/2
                    y1: r/2
                    x2: r + r/2
                    y2: r + r/2
                    stroke: bind gstroke
                },
                Line {
                    x1: r/2
                    y1: r + r/2
                    x2: r + r/2
                    y2: r/2
                    stroke: bind gstroke
                }            
            ]
        }
    }    
}

class SearchField  extends CompositeNode {
    attribute columns: Integer = 13;
    public attribute fSearchValue: String;
    attribute action: function():Void;
    attribute cancel: function():Void;
    attribute textField: TextField;
    attribute baseline: Number;
    attribute view:View;
    
    public function composeNode(): Node {
       var self = this;
        Group {
            content:
            [Rect {
                height: bind view.currentHeight + 4
                width: bind view.currentWidth + 20
                arcHeight: 20
                arcWidth: 20
                stroke: bind if (textField.focused) then Color.transparent(Color.DODGERBLUE, 0.8) else Color.GRAY
                strokeWidth: 2
                fill: Color.WHITE
            },
            Group {
                transform: Transform.translate(10, 2)
                content: view = View {
                    content: textField = TextField {
                            font: Font.Font("VERDANA", ["PLAIN"], 12)
                            focused: true
                            columns: bind self.columns
                            border: EmptyBorder{}
                            background: Color.color(0, 0, 0, 0)
                            action: bind self.action
                            text: bind fSearchValue with inverse
                            onKeyDown: function(e:KeyEvent):Void {
                                if (e.keyStroke == KeyStroke.ESCAPE) {
                                    if(cancel <> null) 
                                        cancel();
                                }
                            }
                        }
                    baseline: self.baseline
                };
            }]
         };
      }
}
    
class SearchButton extends CompositeNode {
    attribute icon: Image;
    attribute text: String;
    attribute height: Number;
    attribute baseline: Number;
    attribute font: Font =  Font.Font("VERDANA", ["BOLD"], 11);
    attribute action: function():Void;
    private attribute pressed: Boolean;   
    attribute r:Rect;
    attribute grad = LinearGradient {
                    startX: bind r.currentX
                    startY:  bind r.currentY
                    endX: bind r.currentX
                    endY: bind r.currentY + r.height
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
    
    public function composeNode(): Node {
        var self = this;
        Group {
            var content = HBox {
                content:
                [ImageView {
                    image: bind icon
                    transform: bind Transform.translate(0, height/2)
                    valign: VerticalAlignment.MIDDLE
                },
                Text {
                    font: bind font
                    //valign: VerticalAlignment.MIDDLE
                    verticalAlignment: Alignment.BASELINE
                    transform: bind Transform.translate(5, baseline)
                    content: bind text
                    fill: bind if (hover) then Color.WHITE else Color.BLACK
                    x: bind r.currentX
                }]
            }
            content:
            [r = Rect {
                onMousePressed: function(e) {pressed = true;}
                onMouseReleased: function(e) {
                    if (hover and action <> null) {
                        action();
                    }
                    pressed = false;
                }

                height: bind self.height
                width: bind content.currentWidth + 20
                arcHeight: 20
                arcWidth: 20
                selectable: true
                fill: bind if (hover) then grad as Paint else Color.color(0, 0, 0, 0) as Paint
                stroke: bind if (pressed) then Color.GRAY else Color.color(0, 0, 0, 0)
            },
            Group {
                transform: Transform.translate(5, 15)
                content: content
            }]
        };        
    }    
}

public class SearchPanel extends CompositeNode {
    attribute closeAction: function();
    attribute searchNextAction: function();
    attribute searchPrevAction: function();
    attribute highlightAllAction: function();
    public attribute pSearchValue: String;
    public attribute matchCase: Boolean on replace {
        bmatchCase = matchCase;
    };
    // bridges the binds from JavaFXPad to the Checkbox.
    public attribute bmatchCase: Boolean on replace {
        matchCase = bmatchCase;
    }
    public attribute searchField:SearchField;
    attribute open: Boolean on replace {
        if(open and searchField<> null) {
            searchField.requestFocus();
        }
    }
    public function composeNode(): Node {
       
        var self = this;
        searchField = SearchField {
            transform: Transform.translate(10, 0)
            fSearchValue: bind self.pSearchValue with inverse
            cancel: bind closeAction
        };   
        HBox {

            content:
            [CloseButton {
                transform: Transform.translate(5, 24/2)
                action: bind closeAction
                valign: VerticalAlignment.MIDDLE
            },
            Text {
                transform: bind Transform.translate(10, 18-(4+searchField.baseline))
                verticalAlignment: Alignment.BASELINE
                font: Font.Font("VERDANA", ["BOLD"], 11)
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
            View {
                transform: Transform.translate(0, -1)
                content: CheckBox {
                    text: "Match Case",
                    font: Font.Font("VERDANA", ["BOLD"], 11),
                    focusable: false
                    selected: bind bmatchCase with inverse
                }
            },
            Text {
                visible: false
                transform: bind Transform.translate(0, 2+searchField.baseline)
                verticalAlignment: Alignment.BASELINE
                font: Font.Font("VERDANA", ["BOLD"], 11)
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
