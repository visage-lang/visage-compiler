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

package demo.pano;

import javafx.gui.*;
import javafx.gui.component.*;
import java.lang.*;

//var width = 800;
//var height = 600;

// load list of available panos and create combo
var panos:ComboBoxItem[];
var doc:javafx.xml.Document = javafx.xml.DocumentBuilder{}.parseURI("http://jasperpotts.com/panos/panos.php");
for (node in doc.documentElement.children){
    if (node.type == javafx.xml.NodeType.ELEMENT){
        var element:javafx.xml.Element = node as javafx.xml.Element;
        insert ComboBoxItem{text: element.getAttribute("name") value: element.getAttribute("url")} into panos;
        System.out.println("name="+element.getAttribute("name"));
    }
}
var panoCombo:ComboBox = ComboBox{items:panos};
panoCombo.selectedItem = panos[0];

// create canvas and pano viewer
var canvas:Canvas = Canvas {background: Color.BLACK};
var panoNode:PanoNode = PanoNode{
    width: bind canvas.width
    height: bind canvas.height
    url: bind panoCombo.selectedItem.value as String
};
insert panoNode into canvas.content;

var app = Application {
    content:  BorderPanel {
        center: canvas
        bottom: FlowPanel {
            content: [
                Button {
                    text: "fit"
                    action: function() {
                        panoNode.zoomFit();
                    }
                },
                Button {
                    text: "+"
                    action: function() {
                        panoNode.zoomIn();
                    }
                },
                Button {
                    text: "-"
                    action: function() {
                        panoNode.zoomOut();
                    }
                },
                panoCombo,
            ]
        };
    }
}

var frame = Frame{
    content: app.content;
//    width: width;
//    height: height;
    width: 800;
    height: 600;
    visible: true;
}