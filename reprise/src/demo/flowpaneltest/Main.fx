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
package demo.flowpaneltest;

import javafx.gui.*;
import java.lang.System;

var b1 = Button{name: "Button 1" text: "Button 1"};
var b2 = Button{name: "Button 2" text: "Button 2"};
var b3 = Button{name: "Button 3" text: "Button 3"};
var b4 = Button{name: "Button 4" text: "Button 4"};
var b5 = Button{name: "Button 5" text: "Button 5"};

var p1 = FlowPanel{content: [b1, b2, b3, b4, b5]
                   hgap: 10
                   vgap: 30
                   alignment: HorizontalAlignment.LEADING
              }

var f1 = Frame {
    name: "Frame 1"
    content: p1
    width: 400
    height: 400
    visible: true
}

b1.action = function() {
    p1.alignment = HorizontalAlignment.TRAILING;
}

b2.action = function() {
    p1.vgap = 50;
}

b3.action = function() {
    p1.hgap = 20;
}

b4.action = function() {
}

b5.action = function() {
}
