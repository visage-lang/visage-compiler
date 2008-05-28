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
package demo.scratchpad;


import javafx.gui.*;
import java.lang.System;

var name = "";
var font = Font{name: "Arial"};
var foreground = Color.BLACK;
var background = Color.BLACK;
var enabled = false;
var text = "";
var icon = Icon{};
var pressedIcon = Icon{};
var horizontalAlignment = HorizontalAlignment.RIGHT;
var verticalAlignment = VerticalAlignment.BOTTOM;
var horizontalTextPosition = HorizontalAlignment.RIGHT;
var verticalTextPosition = VerticalAlignment.BOTTOM;
var selected = false;

var panel = Panel {
    background: bind background
}

var button = CheckBox {
    name: bind name
    font: bind font
    foreground: bind foreground
    enabled: bind enabled
    text: bind text
    icon: bind icon
    pressedIcon: bind pressedIcon
    horizontalAlignment: bind horizontalAlignment
    verticalAlignment: bind verticalAlignment
    horizontalTextPosition: bind horizontalTextPosition
    verticalTextPosition: bind verticalTextPosition
    selected: bind selected
};

name = "Foo";
font = Font{};
foreground = Color.WHITE;
background = Color.WHITE;
enabled = true;
text = "Foo";
icon = Icon{};
pressedIcon = Icon{};
horizontalAlignment = HorizontalAlignment.LEFT;
verticalAlignment = VerticalAlignment.TOP;
horizontalTextPosition = HorizontalAlignment.LEFT;
verticalTextPosition = VerticalAlignment.TOP;
selected = true;

var label = Label {
    text: bind "Foo"
    icon: bind Icon{}
    horizontalAlignment: bind HorizontalAlignment.LEFT
    verticalAlignment: bind VerticalAlignment.TOP
    horizontalTextPosition: bind HorizontalAlignment.LEFT
    verticalTextPosition: bind VerticalAlignment.TOP
    labelFor: bind button
}

var slider = Slider {
    minimum: bind 10
    maximum: bind 90
    value: bind 30
    orientation: bind Orientation.VERTICAL
}

var textField = TextField {
    text: bind "Foo"
    columns: bind 50
    editable: bind false
    horizontalAlignment: bind HorizontalAlignment.LEFT
}

var f = Frame {
    name: bind "name"
    resizable: bind true
    title: bind "title"
    background: bind Color.GREEN
    //foreground: bind Color.WHITE
    //font: bind Font{}
    visible: true
}

var d = Dialog {
    owner: f
    name: bind "name"
    resizable: bind true
    title: bind "title"
    //background: bind Color.GREEN
    //foreground: bind Color.WHITE
    //font: bind Font{}
    visible: true
}
