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
package demo.borderpaneltest;

import javafx.gui.*;
import java.lang.System;
import javax.swing.JPanel;
import javax.swing.JComponent;

function printIt() {
    System.out.println("---------------------");
    var p1Child = p1.getJComponent().getComponents();
    var p1Children = for (child in p1Child) (child as JComponent).getName();
    System.out.println("Panel 1 content ==>");
    printBP(p1);
    System.out.println("Panel 1 children ==> {p1Children.toString()}");
//    var p2Child = p2.getJComponent().getComponents();
//    var p2Children = for (child in p2Child) (child as JComponent).getName();
//    System.out.println("Panel 2 content ==>");
//    printBP(p2);
//    System.out.println("Panel 2 children ==> {p2Children.toString()}");
    System.out.println("b1 parent ==> {b1.getParent().getName()}");
    System.out.println("b2 parent ==> {b2.getParent().getName()}");
    System.out.println("b3 parent ==> {b3.getParent().getName()}");
    System.out.println("b4 parent ==> {b4.getParent().getName()}");
    System.out.println("b5 parent ==> {b5.getParent().getName()}");
}

function printBP(bp: BorderPanel) {
    System.out.println("    top       = {bp.top.getName()}");
    System.out.println("    bottom    = {bp.bottom.getName()}");
    System.out.println("    left      = {bp.left.getName()}");
    System.out.println("    right     = {bp.right.getName()}");
    System.out.println("    center    = {bp.center.getName()}");
    System.out.println("    pageStart = {bp.pageStart.getName()}");
    System.out.println("    pageEnd   = {bp.pageEnd.getName()}");
    System.out.println("    lineStart = {bp.lineStart.getName()}");
    System.out.println("    lineEnd   = {bp.lineEnd.getName()}");
}

var b1 = Button{name: "Button 1" text: "Button 1"};
var b2 = Button{name: "Button 2" text: "Button 2"};
var b3 = Button{name: "Button 3" text: "Button 3"};
var b4 = Button{name: "Button 4" text: "Button 4"};
var b5 = Button{name: "Button 5" text: "Button 5"};

//printIt();

var p1 = BorderPanel{name: "Panel 1"
                     top: b1
                     center: b3
                     //left: b3
                     //right: b4

                     //center: b5

                     //pageStart: b1
                     //pageEnd: b2
                     //lineStart: b3
                     //lineEnd: b4
                    };

var p2 = BorderPanel{name: "Panel 2"};

//printIt();

var f1 = Frame {
    name: "Frame 1"
    content: p1
    width: 400
    height: 400
    visible: true
}

b1.action = function() {
}

b2.action = function() {
}

b3.action = function() {
    if (p1.bottom == null) {
        p1.bottom = p1.top;
    } else {
        p1.top = p1.bottom;
    }
    printIt();
}

b4.action = function() {
}

b5.action = function() {
}
