/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
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
package demo.containertest;

import javafx.gui.*;
import java.lang.System;
import javax.swing.JComponent;

function printIt() {
    System.out.println("---------------------");
    var p1Content = for (comp in p1.content) comp.getName();
    var p1Child = p1.getJComponent().getComponents();
    var p1Children = for (child in p1Child) (child as JComponent).getName();
    System.out.println("Panel 1 content ==> {p1Content.toString()}");
    System.out.println("Panel 1 children ==> {p1Children.toString()}");
    var p2Content = for (comp in p2.content) comp.getName();
    var p2Child = p2.getJComponent().getComponents();
    var p2Children = for (child in p2Child) (child as JComponent).getName();
    System.out.println("Panel 2 content ==> {p2Content.toString()}");
    System.out.println("Panel 2 children ==> {p2Children.toString()}");
    System.out.println("b1 parent ==> {b1.getParent().getName()}");
    System.out.println("b2 parent ==> {b2.getParent().getName()}");
    System.out.println("b3 parent ==> {b3.getParent().getName()}");
    System.out.println("b4 parent ==> {b4.getParent().getName()}");
    System.out.println("m1 parent ==> {m1.getParent().getName()}");
    System.out.println("m2 parent ==> {m2.getParent().getName()}");
    System.out.println("m3 parent ==> {m3.getParent().getName()}");
    System.out.println("m4 parent ==> {m4.getParent().getName()}");
}

var b1 = Button{name: "Button 1" text: "Button 1"};
var b2 = Button{name: "Button 2" text: "Button 2"};
var b3 = Button{name: "Button 3" text: "Button 3"};
var b4 = Button{name: "Button 4" text: "Button 4"};
var l1 = List{name: "List 1" items: [ListItem{text: "ListItem 1"}]};

b1.action = function() {
    insert b2 into p1.content;
}

b2.action = function() {
    insert m4 into f.menus;
    printIt();
}

b3.action = function() {
    delete f.menus;
    printIt();
}

b4.action = function() {
    var f = Frame{name: "HooHah"};
    f.menus = [m1];
    printIt();
}

b1.x = 0;
b1.y = 0;
b1.width = 100;
b1.height = 100;

b2.x = 110;
b2.y = 0;
b2.width = 100;
b2.height = 100;

var lg = LinearGradient { 
        startX: 0.0 
        startY: 0.0
        endX: 1.0
        endY: 0.0
        stops: [
           Stop { offset: 0.0 color: Color{red: 0 green: 0 blue: 0} },
           Stop { offset: 1.0 color: Color{red: 0.5} }]};

var p1 = FlowPanel{name: "Panel 1", content: [l1, b2, b3, b4, b1], background: lg};
var p2 = Panel{name: "Panel 2"};

printIt();

var m1 = Menu{name: "Menu 1", text: "Menu 1"};
var m2 = Menu{name: "Menu 2", text: "Menu 2"};
var m3 = Menu{name: "Menu 3", text: "Menu 3"};
var m4 = Menu{name: "Menu 4", text: "Menu 4"};

var f = Frame {
    name: "Frame 1"
    menus: [m1, m2, m3, m1];
    content: p1
    width: 400
    height: 400
    visible: true
}

printIt();

var hslider = Slider{minimum: 10, maximum: 200, value: 10};
var vslider = Slider{minimum: 0, maximum: 100, value: 100, orientation: Orientation.VERTICAL}

var list = List{items: [ListItem{text: "Hello"}]
                    x: bind hslider.value
                    y: bind hslider.value
                    width: bind 200 - vslider.value
                    height: bind 200 - vslider.value};

var panel = Panel{content: list};

Frame {
    title: "ContainerTest"
    visible: true
    width: 400
    height: 400
    content: BorderPanel{bottom: hslider right: vslider center: panel}
}
