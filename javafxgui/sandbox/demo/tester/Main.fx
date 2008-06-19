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

package demo.tester;

import javafx.gui.*;
import javafx.gui.swing.*;
import java.lang.System;
import java.awt.BorderLayout;

var frame = Frame {
    title: "JavaFX GUI Toolkit Demo"
    menus: [Menu{text: "File",
                 items: [MenuItem{text: "Exit", action: function() {System.exit(0);}}]},
            Menu{text: "Do",
                 items: [MenuItem{text: "Show List",
                                  action: function() {
                                      showList();
                                  }
                                  }]},
            Menu{text: "Help",
                 items: [MenuItem{text: "About"
                                  action: function() {
                                      showAbout();
                                  }
                         },
                         CheckBoxMenuItem{text: "Check" selected: true},
                         RadioButtonMenuItem{text: "Radio" action: function() {System.out.println("Called");}}
                         ]}]
}

function showList() {
    var doButton = Button {text: "DO"};
    var okButton = Button {text: "OK"};
    var flow = FlowPanel{content: [doButton, okButton]};

    var people = Person.createPersonSequence();

    var list = List{
        items: bind for (person in people) ListItem {text: bind "{person.firstName} {person.lastName}"}
    }
    
    var dialog = Dialog {
        title: "About"
        content: BorderPanel{bottom: flow, center: list};
    }

    doButton.action = function() {
        list.scrollable = not list.scrollable;
    }

    okButton.action = function() {
        dialog.getJDialog().dispose();
    }
    
    dialog.getJDialog().setResizable(false);
    dialog.getJDialog().pack();
    dialog.visible = true;
}

function showAbout() {
    var okButton = Button {text: "OK"};
    
    var canvas = Canvas {
        content: [ImageView {x: 0 y: 0 image: Image {width: 300 height: 150 url: "{__DIR__}/sun_logo.png"}}]
    }

    var dialog = Dialog {
        title: "About"
        content: BorderPanel {bottom: okButton, center: canvas}
    }

    okButton.action = function() {
        dialog.getJDialog().dispose();
    }
    
    dialog.getJDialog().setResizable(false);
    dialog.getJDialog().pack();
    dialog.visible = true;
}

var hSlider = Slider{minimum: 20 maximum: 180 value: 20};
var vSlider = Slider{minimum: 0 maximum: 160 value: 160 orientation: Orientation.VERTICAL};

function makeColor1(hVal: Integer, vVal: Integer): Color {
    return Color{red: hVal/180.0 green: 0.5 blue: (180 - vVal)/180.0};
}

function OLDmakeColor2(hVal: Integer, vVal: Integer): Color {
    return Color{red: 0.5 green: (180 - hVal)/180.0 blue: (180 - vVal)/180.0};
}

function makeColor2(hVal: Integer, vVal: Integer): Paint {
    return LinearGradient { 
        startX: 0.0 
        startY: 0.0
        endX: 1.0
        endY: 0.0
        stops: [
           Stop { offset: 0.0 color: Color{red: 0 green: 0 blue: 0} },
           Stop { offset: 1.0 color: Color{red: 0.5 green: (180-hVal)/180.0 blue: (180-vVal)/180.0} }]
    };
}

/*
var canvas = Canvas{
        content: [Circle {centerX: 150 centerY: 150 radius: bind (hSlider.value + vSlider.value) / 2
                          fill: bind makeColor2(hSlider.value, vSlider.value)},
                  Rectangle {x: bind hSlider.value y: bind 180 - vSlider.value width: bind hSlider.value height: bind 180 - vSlider.value
                             fill: bind makeColor1(hSlider.value, vSlider.value)},
                  Rectangle {x: bind hSlider.value + 50 y: bind 180 - vSlider.value + 50 width: bind hSlider.value height: bind 180 - vSlider.value
                             fill: bind makeColor1(hSlider.value, vSlider.value)},
                  Rectangle {x: bind hSlider.value + 100 y: bind 180 - vSlider.value + 100 width: bind hSlider.value height: bind 180 - vSlider.value
                             fill: bind makeColor1(hSlider.value, vSlider.value)},
                  Text {font: Font{size: 30} x: 50 y: 400 content: "Play with the sliders..." fill: Color{red: 1.0 green: 0 blue: 0}}]
                  
        background: Color{red: 1.0 green: 1.0 blue: 1.0}
}
*/

var people = Person.createPersonSequence();
    
var list = List{
    items: bind for (person in people) ListItem {text: bind "{person.firstName} {person.lastName}"}
}

class BlueDisk extends CustomNode {
    protected function create():Node {
        Circle {
                 radius: bind 20.0 + (vSlider.value / 10.0);
            strokeWidth: 3
                 stroke: Color.LIGHTGREY;
                   fill: Color.INDIGO;
                 smooth: true;
        }
    }
}

var canvas = Canvas {
    background: Color{red: 1.0 green: 1.0 blue: 1.0}
    preferredSize: [620, 460]
    content: [
        Circle {
                      id: "Circle"
           onMouseExited: function(e:MouseEvent) { e.node.opacity = 1.0 }
          onMouseEntered: function(e:MouseEvent) { e.node.opacity = 0.5 }
                 centerX: 150 
                 centerY: 150 
                  radius: bind (hSlider.value + vSlider.value) / 2 
                    fill: bind makeColor2(hSlider.value, vSlider.value)
        },
        /*
        Path {
            elements: [
                     MoveTo { x:10 y:10 },
                     HLineTo { x:110, absolute:true },
                     VLineTo { y:110, absolute:true },
                     HLineTo { x:-100, absolute:false },
                     VLineTo { y:-100, absolute:false }
            ]
            fill: Color.LIGHTGREY
            strokeWidth: 3
            stroke: Color.CRIMSON
        }
        */
        VBox {
               spacing: bind 15.0 + (hSlider.value / 5.0)
            translateX: 500
            translateY: 75
               content: [BlueDisk{}, BlueDisk{}, BlueDisk{}]
        },
        Group { 
                 opacity: 0.5
                  cursor: Cursor.HAND
                      id: "Rectangle Group"
                 content: [
                    Rectangle {
                      translateX: bind hSlider.value 
                      translateY: bind 180 - vSlider.value 
                           width: bind hSlider.value 
                          height: bind 180 - vSlider.value
                            fill: bind makeColor1(hSlider.value, vSlider.value)},
                    Rectangle {
                               x: bind hSlider.value + 50 
                               y: bind 180 - vSlider.value + 50
                           width: bind hSlider.value 
                          height: bind 180 - vSlider.value
                            fill: bind makeColor1(hSlider.value, vSlider.value)},
                    Rectangle {
                  onMousePressed: function(e:MouseEvent) { e.node.requestFocus(); }
                    onKeyPressed: function(e:KeyEvent) { System.out.println(e.getKeyText()); }
                              id: "Rectangle3"
                               x: bind hSlider.value + 100 
                               y: bind 180 - vSlider.value + 100 
                     blocksMouse: true
                           width: bind hSlider.value 
                          height: bind 180 - vSlider.value
                 strokeDashArray: [2.0, 3.0]
                          stroke: Color.DARKRED
                            fill: bind makeColor1(hSlider.value, vSlider.value)}]},
        ComponentView {
              translateX: 200
              translateY: 250
               component: TextField { 
                            text: "This is actually a text field component" 
                            borderless: true
                            background: bind makeColor2(hSlider.value, vSlider.value)
                            background: null
                          }},
        ComponentView {
              translateX: bind hSlider.value/3.0 + 50.0
              translateY: bind -vSlider.value/3.0 + 340
              blocksMouse: true
               component: Button { 
                   preferredSize: bind [hSlider.value + 100, 30]
                            text: "Button Node"
             horizontalAlignment: HorizontalAlignment.CENTER
                        action: function():Void { list.visible = not list.visible }}},
        ComponentView {
              translateX: bind vSlider.value/3.0 + 200.0
              translateY: bind -hSlider.value/3.0 + 80
             blocksMouse: true
               component: list},
        Text {
                    font: Font{size: 30} 
                       x: 50 
                       y: 400 
                 content: "Play with the sliders..." 
                    fill: Color{red: 1.0 green: 0 blue: 0}
                   cache: true }]
};

frame.content = BorderPanel{bottom: hSlider, right: vSlider, center: canvas};
frame.visible = true;

