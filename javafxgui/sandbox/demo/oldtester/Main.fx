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

package demo.oldtester;

import javafx.gui.*;
import javafx.gui.component.*;
import java.lang.System;

var icon = Icon {
    image: Image {url: "http://media-images.nscpcdn.com/media/contrib/avatar/8/a/user_387254.gif" }
};

var tg = ToggleGroup{}

var font = Font {name: "Courier New", style: FontStyle.BOLD_ITALIC, size: 20}

var checkbox = CheckBox {
    name: 'Check'
    text: 'Check'
    font: font
    foreground: Color {green : 1}
};

var toggle = ToggleButton {
    name: 'Toggle'
    text: 'Toggle'
    font: font
    foreground: Color {green : 1}
    selected: bind checkbox.selected with inverse
};

var radio1 = RadioButton {
    name: 'Radio1'
    text: 'Radio1'
    font: font
    foreground: Color {green : 1}
    selected: true
    toggleGroup: tg
};

var radio2 = RadioButton {
    name: 'Radio2'
    text: 'Radio2'
    font: font
    foreground: Color {green : 1}
    selected: true
    toggleGroup: tg
};

var printButton = Button {
    name: 'Print'
    icon: Icon { image: Image {url:'http://www.hscripts.com/freeimages/icons/clipart/globe/globe-clipart-picture1.gif'} }
    pressedIcon: Icon {image: Image{url:'http://www.hscripts.com/freeimages/icons/clipart/globe/globe-clipart-picture5.gif'} }
    text: 'Print Selection'
    font: Font {size: 20}
};

var clearButton = Button {
    name: 'Clear'
    text: 'Clear Selection'
    font: font
    foreground: Color {red : 1}
//    oldbackground: Color {blue : 1}
    action: function() {
        tg.clearSelection();
    }
};

var label = Label {
    text: 'Buttons!'
    icon: icon
    font: Font {name: "Courier New", style: FontStyle.BOLD_ITALIC, size: 20}
    horizontalTextPosition: HorizontalAlignment.CENTER
    verticalTextPosition: VerticalAlignment.BOTTOM
};

var verifyText  = function(newValue:String): Boolean {
    java.lang.System.out.println("newValue is " + newValue + " should be \"stooge\"");
    newValue.equals("stooge")
};

var textfield = TextField { 
    text: "Hello World" 
    selectOnFocus: true 
    verify: verifyText 
};

var panel = FlowPanel{content: [
                            label,
                            textfield,
                            checkbox,
                            toggle,
                            radio1,
                            radio2,
                            printButton,
                            clearButton]};

var frame = Frame {
    content: panel
    icon: icon
    title: "Stooges"
    visible: true
    menus: [Menu{text: "File"}, Menu{text: "Edit"}, Menu{text: "Help"}]
};

frame.menus[0].items = [MenuItem{text: "Exit" action: function() {System.exit(0);}}];

var slider = Slider {
    value: 75
};


var black = Color {red:0 green:0 blue:0};
var white = Color {red:1 green:1 blue:1};

var canvas = Canvas {
    content: [ 
        Rectangle { x:0 y:0 width:300 height:300 fill:black },
        Group {
            content: [
                Rectangle { x:10 y:10 width:200 height:200 arcWidth:10 arcHeight:10 fill: Color {red:1} },
                Ellipse { centerX:50 centerY:50 radiusX:25 radiusY:15 fill: Color {blue:1} },
                Circle { centerX:150 centerY:150 radius:20 stroke: black strokeWidth:5 },
                ImageView { x:25 y:100 image:Image{url:"https://duke.dev.java.net/images/wave/Wave.jpg" size:64} }
            ]
        },
        Text { content: "Hello World" x:40 y:40 font:font fill:white}
    ]
    background: Color { red:1 blue:1 green:1 }
};

var url = "http://gary2idaho.files.wordpress.com/2007/04/three-stooges.jpg";
var image1 = Image{ url:url size: 128};
var image2 = Image{ url:url width: 64};
var image3 = Image{ url:url height: 64};
var image4 = Image { size:256 url: "http://media-images.nscpcdn.com/media/contrib/avatar/8/a/user_387254.gif"}


var dialogPanel = FlowPanel {content: [
                                canvas,
                                Label { icon: Icon { image:image1  } },
                                Label { icon: Icon { image:image2  } },
                                Label { icon: Icon { image:image3  } },
                                Label { icon: Icon { image:image4  } }]};

Dialog {
    content: dialogPanel
    visible: true
    owner: frame        
}

printButton.action = function() {
    //var selection = tg.getSelection();
    //var name = if (selection == null) "nothing" else selection.name;
    //System.out.println("{name} is selected");
    insert MenuItem{text: "HI"} into frame.menus[0].items;
}
