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
package demo.listtest2;

import javafx.gui.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.lang.System;

var list1 = List {
    items: for (i in [0..9]) ListItem{text: "Item {i}"};
    selectedIndex: 0;
};

var list2 = List {
};

var add = Button{text: ">>"};
add.getJButton().setRequestFocusEnabled(false);
var remove = Button{text: "<<"};
remove.getJButton().setRequestFocusEnabled(false);

var listPanel = GridPanel{rows: 1 columns: 2 content: [list1, list2]};
var buttonPanel = FlowPanel{content: [add, remove]};

add.action = function() {
    var index = list1.selectedIndex - 1;
    insert list1.selectedItem into list2.items;
    list1.selectedIndex = if (index < 0) 0 else index;
}

remove.action = function() {
    insert list2.selectedItem into list1.items;
}


Frame {
    title: "List Test 2"
    content: BorderPanel {center: listPanel bottom: buttonPanel}
    width: 400
    height: 400
    visible: true
};
