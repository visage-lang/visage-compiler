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

package javafxpad;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.awt.Dimension;

class ViewOutline extends CompositeNode {
    attribute outline: Node; 
    attribute viewTransform: Translate = Translate {x: 0, y: 0};
    attribute sizing: Rect on replace {
         if (sizing == null) {
            viewTransform.x = outline.currentX + 4;
            viewTransform.y = outline.currentY + 4;
            viewHolder.size = new Dimension(rectWidth, rectHeight);
         } 
    };
    public attribute selected: Boolean;
    public attribute rectHeight: Number on replace {
        if (sizing == null) {
            viewHolder.size = new Dimension(rectWidth, rectHeight);
        }  
    };
    public attribute rectWidth: Number on replace {
        if (sizing == null) {
            viewHolder.size = new Dimension(rectWidth, rectHeight);
        }  
    };
    attribute northWest: Rect;
    attribute north: Rect;
    attribute northEast: Rect;
    attribute east: Rect;
    attribute southEast: Rect;
    attribute south: Rect;
    attribute southWest: Rect;
    attribute west: Rect;
    attribute outlineWidth: Number = 1.5;
    attribute view: Widget;
    attribute viewHolder: View;
    
    public function composeNode(): Node {
        var transparentFill = Color.color(0, 0, 0, 0);
        var strokeColor = Color.color(0.0, 0.0, 1.0, 0.5);
        var outlineDash:Number[] = [];
        var handleSize = 8;
        var arcStroke = Color.color(1.0, 0.0, 0.0, 0.5);
        var r1:Rect;
        return Group {
            content:
            [viewHolder = View {
                transform: bind viewTransform
                //filter: Identity
                size: new Dimension(rectWidth, rectHeight)
                content: bind view
            },
            outline = Group {
            var tx = Translate {x: 0, y: 0}
            transform: tx
            content:
            [r1 = Rect {
                var thisRect = bind r1;
                selectable: true
                width: bind rectWidth
                height: bind rectHeight
                stroke: bind if (sizing <> null) then strokeColor else transparentFill
                strokeWidth: 1
                     //strokeDashArray: outlineDash
                fill: bind if (sizing <> null) then Color.color(1, 1, 1, 0.5) else null
                var mouseX:Number = 0
                var mouseY:Number = 0
                onMousePressed: function(e:CanvasMouseEvent):Void {
                    mouseX = e.x;
                    mouseY = e.y;
                    thisRect.cursor = Cursor.MOVE;
                }
                onMouseDragged: function(e:CanvasMouseEvent):Void {
                    var dx = e.x - mouseX;
                    var dy = e.y - mouseY;
                    mouseX = e.x;
                    mouseY = e.y;
                    tx.x += dx;
                    tx.y += dy;
                }
                onMouseClicked: function(e:CanvasMouseEvent):Void {
                     if (e.clickCount == 2) {
                         selected = not selected;
                     }
                }
                onMouseReleased: function(e:CanvasMouseEvent):Void {
                     thisRect.cursor = thisRect.getCanvas().cursor;
                }
            },
            Group {
                visible: bind selected
                content:
                [northWest = Rect {
                    selectable: true
                    cursor: Cursor.NW_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    fill: transparentFill
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = northWest;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void  {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
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
                north = Rect {
                    selectable: true
                    cursor: Cursor.N_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth/2, 0)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = north;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
                        rectHeight -= dy;
                        tx.y += dy;
                    }
                    onMouseReleased: function(e):Void {
                        sizing = null;
                    }
                },
                northEast = Rect {
                    selectable: true
                    cursor: Cursor.NE_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    fill: transparentFill
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    transform: bind Transform.translate(rectWidth, 0)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = northEast;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
                        var w = rectWidth + dx;
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
                east = Rect {
                    selectable: true
                    cursor: Cursor.E_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth, rectHeight/2)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = east;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
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
                southEast = Rect {
                    selectable: true
                    cursor: Cursor.SE_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth, rectHeight)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = southEast;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
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
                south = Rect {
                    selectable: true
                    cursor: Cursor.S_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(rectWidth/2, rectHeight)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    var oldValue = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = south;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        if (e.button == 1) {
                            var dx = e.x - mouseX;
                            var dy = e.y - mouseY;
                            mouseX = e.x;
                            mouseY = e.y;
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
                southWest = Rect {
                    selectable: true
                    cursor: Cursor.SW_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(0, rectHeight)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY :Number= 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = southWest;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
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
                west = Rect {
                    selectable: true
                    cursor: Cursor.W_RESIZE
                    height: handleSize
                    width: handleSize
                    stroke: strokeColor
                    strokeWidth: bind outlineWidth
                    strokeDashArray: outlineDash
                    fill: transparentFill
                    transform: bind Transform.translate(0, rectHeight/2)
                    valign: VerticalAlignment.CENTER
                    halign: HorizontalAlignment.CENTER
                    var mouseX:Number = 0
                    var mouseY:Number = 0
                    onMousePressed: function(e:CanvasMouseEvent):Void {
                        mouseX = e.x;
                        mouseY = e.y;
                        sizing = west;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent):Void {
                        var dx = e.x - mouseX;
                        var dy = e.y - mouseY;
                        mouseX = e.x;
                        mouseY = e.y;
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
                    visible: bind sizing <> null
                    content: bind "{rectWidth} X {rectHeight}"
                    transform: bind Transform.translate(sizing.currentX + sizing.currentWidth + 5, sizing.currentY + sizing.currentHeight + 5)   
                }]
           }]
         }]
        };
    }
}