/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafxpad;
import javafx.ui.*;
import javafx.ui.canvas.*;
class ViewOutline extends CompositeNode {
    attribute outline: Node?; 
    attribute viewTransform: Translate;
    attribute sizing: Rect?;
    public attribute selected: Boolean;
    public attribute rectHeight: Number;
    public attribute rectWidth: Number;
    attribute northWest: Rect;
    attribute north: Rect;
    attribute northEast: Rect;
    attribute east: Rect;
    attribute southEast: Rect;
    attribute south: Rect;
    attribute southWest: Rect;
    attribute west: Rect;
    attribute outlineWidth: Number;
    attribute view: Widget;
    attribute viewHolder: View;
}

attribute ViewOutline.viewTransform = Translate {x: 0, y: 0};

trigger on ViewOutline.view = value {
    //view.e = {height: rectHeight, width: rectWidth};
}

trigger on ViewOutline.rectHeight = value {
    if (sizing == null) {
        viewHolder.size = {height: rectHeight, width: rectWidth};
    }
}

trigger on ViewOutline.sizing = value {
     if (value == null) {
        viewTransform.x = outline.currentX + 4;
        viewTransform.y = outline.currentY + 4;
        viewHolder.size = {height: rectHeight, width: rectWidth};
     }
}
trigger on ViewOutline.rectWidth = value {
    if (sizing == null) {
        viewHolder.size = {height: rectHeight, width: rectWidth};
    }
}

attribute ViewOutline.outlineWidth = 1.5;

operation ViewOutline.composeNode() {
    var transparentFill = new Color(0, 0, 0, 0);
    var strokeColor = new Color(0.0, 0.0, 1.0, 0.5);
    var outlineDash = [];
    var handleSize = 8;
    var arcStroke = new Color(1.0, 0.0, 0.0, 0.5);
    return Group {
        content:
        [View {
            transform: bind viewTransform
            //filter: Identity
            size: {height: rectHeight, width: rectWidth}
            attribute: viewHolder
            content: bind view
        },
        Group {
        attribute: outline
        var tx = Translate {x: 0, y: 0}
        transform: tx
        content:
        [Rect {
            var: self
            selectable: true
            width: bind rectWidth
            height: bind rectHeight
            stroke: bind if sizing <> null then strokeColor else transparentFill
            strokeWidth: 1
                 //strokeDashArray: outlineDash
            fill: bind if sizing <> null then new Color(1, 1, 1, 0.5) else null
            var mouseX = 0
            var mouseY = 0
            onMousePressed: operation(e:CanvasMouseEvent) {
                mouseX = e.x;
                mouseY = e.y;
                self.cursor = MOVE;
            }
            onMouseDragged: operation(e:CanvasMouseEvent) {
                var dx = e.x - mouseX;
                var dy = e.y - mouseY;
                mouseX = e.x;
                mouseY = e.y;
                tx.x += dx;
                tx.y += dy;
            }
            onMouseClicked: operation(e:CanvasMouseEvent) {
                 if (e.clickCount == 2) {
                     selected = not selected;
                 }
            }
            onMouseReleased: operation(e:CanvasMouseEvent) {
                 self.cursor = self.getCanvas().cursor;
            }
        },
        Group {
            visible: bind selected
            content:
            [Rect {
                var: self
                selectable: true
                cursor: NW_RESIZE
                attribute: northWest
                height: handleSize
                width: handleSize
                stroke: strokeColor
                fill: transparentFill
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                attribute: north
                selectable: true
                cursor: N_RESIZE
                height: handleSize
                width: handleSize
                stroke: strokeColor
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                fill: transparentFill
                transform: bind translate(rectWidth/2, 0)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
                    var dx = e.x - mouseX;
                    var dy = e.y - mouseY;
                    mouseX = e.x;
                    mouseY = e.y;
                    rectHeight -= dy;
                    tx.y += dy;
                }
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                attribute: northEast
                selectable: true
                cursor: NE_RESIZE
                height: handleSize
                width: handleSize
                stroke: strokeColor
                fill: transparentFill
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                transform: bind translate(rectWidth, 0)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                selectable: true
                cursor: E_RESIZE
                attribute: east
                height: handleSize
                width: handleSize
                stroke: strokeColor
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                fill: transparentFill
                transform: bind translate(rectWidth, rectHeight/2)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                attribute: southEast
                selectable: true
                cursor: SE_RESIZE
                height: handleSize
                width: handleSize
                stroke: strokeColor
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                fill: transparentFill
                transform: bind translate(rectWidth, rectHeight)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                attribute: south
                selectable: true
                cursor: S_RESIZE
                height: handleSize
                width: handleSize
                stroke: strokeColor
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                fill: transparentFill
                transform: bind translate(rectWidth/2, rectHeight)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                var oldValue = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                attribute: southWest
                selectable: true
                cursor: SW_RESIZE
                height: handleSize
                width: handleSize
                stroke: strokeColor
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                fill: transparentFill
                transform: bind translate(0, rectHeight)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Rect {
                var: self
                attribute: west
                selectable: true
                cursor: W_RESIZE
                height: handleSize
                width: handleSize
                stroke: strokeColor
                strokeWidth: bind outlineWidth
                strokeDashArray: outlineDash
                fill: transparentFill
                transform: bind translate(0, rectHeight/2)
                valign: CENTER
                halign: CENTER
                var mouseX = 0
                var mouseY = 0
                onMousePressed: operation(e:CanvasMouseEvent) {
                    mouseX = e.x;
                    mouseY = e.y;
                    sizing = self;
                }
                onMouseDragged: operation(e:CanvasMouseEvent) {
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
                onMouseReleased: operation(e) {
                    sizing = null;
                }
            },
            Text {
                visible: bind sizing <> null
                content: bind "{rectWidth} X {rectHeight}"
                transform: bind translate(sizing.currentX + sizing.currentWidth + 5, sizing.currentY + sizing.currentHeight + 5)   
            }]
       }]
     }]
    };
}
