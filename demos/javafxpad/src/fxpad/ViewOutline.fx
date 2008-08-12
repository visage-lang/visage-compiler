/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package fxpad;

import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.ext.swing.*;
import javafx.input.*;
import java.awt.Dimension;

class ViewOutline extends CustomNode {
    var outline: Node; 
    var viewTransform: Translate = Translate {x: 0, y: 0};
    
    //TODO sizing setBounds
    var sizing: Rectangle on replace {
        //var bounds =viewHolder.getBounds();
         if (sizing == null) {
            viewTransform.x = outline.getX() + 4;
            viewTransform.y = outline.getY() + 4;
            //bounds.setRect(bounds.getX(), bounds.getY(), rectWidth, rectHeight);
         } else {
             //bounds.setRect(sizing.x, sizing.y, sizing.width, sizing.height);
         }
    };
    public var selected: Boolean;
    public var rectHeight: Number on replace {
        if (sizing == null) {
            //var bounds = viewHolder.getBounds();
            //bounds.setRect(bounds.getX(), bounds.getY(), rectWidth, rectHeight);            
        }  
    };
    public var rectWidth: Number on replace {
        if (sizing == null) {
            //var bounds =viewHolder.getBounds();
            //bounds.setRect(bounds.getX(), bounds.getY(), rectWidth, rectHeight);             
        }  
    };
    var northWest: Rectangle;
    var north: Rectangle;
    var northEast: Rectangle;
    var east: Rectangle;
    var southEast: Rectangle;
    var south: Rectangle;
    var southWest: Rectangle;
    var west: Rectangle;
    var outlineWidth: Number = 1.5;
    var view: Component;
    var viewHolder: ComponentView;
    
    override function create(): Node {
        var transparentFill = Color.TRANSPARENT;
        var strokeColor = Color.color(0.0, 0.0, 1.0, 0.5);
        var outlineDash:Number[] = [];
        var handleSize = 8;
        var arcStroke = Color.color(1.0, 0.0, 0.0, 0.5);
        var r1:Rectangle;
        return Group {
            content:
            [viewHolder = ComponentView {
                transform: bind viewTransform
                //effect: Identity
                //TODO - done elsewhere size: new Dimension(rectWidth, rectHeight)
                component: bind view
            },
            outline = Group {
            var tx = Translate {x: 0, y: 0}
            transform: tx
            content:
            [r1 = Rectangle {
                //TODO selectable: true
                blocksMouse: true
                width: bind rectWidth
                height: bind rectHeight
                stroke: bind if (sizing != null) then strokeColor else transparentFill
                strokeWidth: 1
                     //strokeDashArray: outlineDash
                fill: bind if (sizing != null) then Color.color(1, 1, 1, 0.5) else null;
                var mouseX:Number = 0;
                var mouseY:Number = 0;
                var saveCursor:Cursor = null;
                onMousePressed: function(e:MouseEvent):Void {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    saveCursor = r1.cursor;
                    r1.cursor = Cursor.MOVE;
                }
                onMouseDragged: function(e:MouseEvent):Void {
                    var dx = e.getX() - mouseX;
                    var dy = e.getY() - mouseY;
                    mouseX = e.getX();
                    mouseY = e.getY();
                    tx.x += dx;
                    tx.y += dy;
                }
                onMouseClicked: function(e:MouseEvent):Void {
                     if (e.getClickCount() == 2) {
                         selected = not selected;
                     }
                }
                onMouseReleased: function(e:MouseEvent):Void {
                     r1.cursor = saveCursor;
                }
            },
            Group {
                visible: bind selected
                content:
                [northWest = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.NW_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    fill: transparentFill
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = northWest;
                    }
                    onMouseDragged: function(e:MouseEvent):Void  {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        var w = rectWidth - dx;
                        if (w < 0) {
                            w = 0;
                        }
                        var h = rectHeight - dy;
                        if (h < 0) {
                            h = 0;
                        }
                        rectWidth = w;
                        rectHeight = h;
                        tx.x += dx;
                        tx.y += dy;
                    }
                    onMouseReleased: function(e):Void  {
                        sizing = null;
                    }
                },
                north = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.N_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth/2, 0)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = north;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        rectHeight -= dy;
                        tx.y += dy;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                northEast = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.NE_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    fill: transparentFill
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    transform: bind Transform.translate(rectWidth, 0)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = northEast;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        var w = rectWidth + dx;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        if (w < 0) {
                            w = 0;
                        }
                        var h = rectHeight - dy;
                        if (h < 0) {
                            h = 0;
                        }
                        rectWidth = w;
                        rectHeight = h;
                        tx.y += dy;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                east = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.E_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth, rectHeight/2)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = east;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        var w = rectWidth + dx;
                        if (w < 0) {
                            w = 0;
                        }
                        rectWidth = w;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                southEast = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.SE_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth, rectHeight)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = southEast;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        var w = rectWidth + dx;
                        var h = rectHeight + dy;
                        if (w < 0) {
                            w = 0;
                        }
                        if (h < 0) {
                            h = 0;
                        }
                        rectHeight = h;
                        rectWidth = w;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                south = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.S_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth/2, rectHeight)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    var oldValue = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = south;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        if (e.getButton() == 1) {
                            var dx = e.getX() - mouseX;
                            var dy = e.getY() - mouseY;
                            mouseX = e.getX();
                            mouseY = e.getY();
                            var h = rectHeight + dy;
                            if (h < 0) {
                                h = 0;
                            }
                            rectHeight = h;
                        }
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                southWest = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.SW_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(0, rectHeight)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY :Number= 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = southWest;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        var w = rectWidth - dx;
                        var h = rectHeight + dy;
                        if (w < 0) {
                            w = 0;
                        }
                        if (h < 0) {
                            h = 0;
                        }
                        rectHeight = h;
                        rectWidth = w;
                        tx.x += dx;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                west = Rectangle {
                    //TODO ?? selectable: true
                    blocksMouse: true
                    cursor: Cursor.W_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(0, rectHeight/2)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER;
                    var mouseX:Number = 0;
                    var mouseY:Number = 0;
                    onMousePressed: function(e:MouseEvent):Void {
                        mouseX = e.getX();
                        mouseY = e.getY();
                        sizing = west;
                    }
                    onMouseDragged: function(e:MouseEvent):Void {
                        var dx = e.getX() - mouseX;
                        var dy = e.getY() - mouseY;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        var w = rectWidth - dx;
                        if (w < 0) {
                            w = 0;
                        }
                        rectWidth = w;
                        tx.x += dx;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                Text {
                    visible: bind sizing != null
                    content: bind "{rectWidth} X {rectHeight}"
                    transform: bind Transform.translate(sizing.x + sizing.width + 5, sizing.y + sizing.height + 5)   
                }]
           }]
         }]
        };
    }
}
