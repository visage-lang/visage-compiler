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
package demo.listtest;

import javafx.gui.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.lang.System;

var items = for (i in [0..9]) ListItem{text: "Item {i}"};
items[0].selected = true;
items[1].selected = true;
items[2].selected = true;

var list = List {
    items: bind items
    selectedIndex: 5;
};

var b1 = Button {text: "B1"}
var b2 = Button {text: "B2"}
var b3 = Button {text: "B3"}
var b4 = Button {text: "B4"}
var b5 = Button {text: "B5"}
var b6 = Button {text: "B6"}

var flow = FlowPanel {content: [b1, b2, b3, b4, b5, b6]};
for (c in flow.content) {
    (c as Button).getJButton().setRequestFocusEnabled(false);
}

b1.action = function() {
    System.out.println("{list.selectedIndex} ==> {list.selectedItem}");
}

b2.action = function() {
    var index = 0;
    for (item in list.items) {
        //System.out.println("{index++}, {item.listIndex}, {item.text}, {item.selected}");
        System.out.println("{index++}, {item.text}, {item.selected}");
    }
}

b3.action = function() {
    list.selectedItem = ListItem{text: "nap"};
}

b4.action = function() {
    list.selectedIndex = -20;
}

b5.action = function() {
    delete items[2..4];
}

b6.action = function() {
    items[2..4] = [ListItem{text: "nap"}, ListItem{text: "nup"}];
}

Frame {
    title: "List Test"
    content: BorderPanel {center: list bottom: flow}
    width: 400
    height: 400
    visible: true
};
