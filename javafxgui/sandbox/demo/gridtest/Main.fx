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

package demo.gridtest;

import javafx.animation.*;
import javafx.gui.*;
import javafx.gui.swing.*;
import java.lang.System;

/**
 * Recreates http://www.bit-101.com/java/Grid2.html
 */

class Cell {
    attribute grid: Grid;
    attribute row: Integer;
    attribute col: Integer;
    attribute x: Number;
    attribute y: Number;
    attribute xv: Number;
    attribute yv: Number;
    attribute cx: Number;
    attribute cy: Number;
    attribute left:   Cell;
    attribute right:  Cell;
    attribute top:    Cell;
    attribute bottom: Cell;
    init {
        x = col * grid.gSize;
        y = row * grid.gSize;
        cx = x;
        cy = y;
    }

    // called after the whole grid.cells sequence has been initialized/filled
    function initAdjacencies() {
        left   = if (col > 0)             grid.cells[row * grid.width + col-1] else null;
        right  = if (col < grid.width-1)  grid.cells[row * grid.width + col+1] else null;
        top    = if (row > 0)             grid.cells[(row-1) * grid.width + col] else null;
        bottom = if (row < grid.height-1) grid.cells[(row+1) * grid.width + col] else null;
    }
}

public class Grid extends Group {
    attribute xmouse: Number;
    attribute ymouse: Number;
    attribute k1: Number = 0.01;
    attribute k2: Number = 0.20000000000000001;
    attribute damp: Number  = 0.72999999999999998;
    attribute drag: Integer = -1;
    attribute dragging: Boolean;
    attribute width: Integer = 40;
    attribute height: Integer = 40;
    attribute gSize: Number = 15;
    attribute cells: Cell[] = for (row in [0..height-1], col in [0..width-1]) Cell { grid: this, row: row, col: col };
    function update():Void {
        if (dragging) {
            cells[drag.intValue()].x = xmouse.intValue();
            cells[drag.intValue()].y = ymouse.intValue();
        }
        var i = 0;
        for (cell in cells) {
            if (drag <> i) {
                cell.xv += (cell.cx - cell.x)*k1;
                cell.yv += (cell.cy - cell.y)*k1;
                if (cell.left <> null) {
                    cell.xv += (cell.left.x + gSize - cell.x) *k2;
                    cell.yv += (cell.left.y - cell.y) *k2;
                }
                if (cell.right <> null) {
                    cell.xv += (cell.right.x - gSize - cell.x) *k2;
                    cell.yv += (cell.right.y - cell.y) *k2;
                }
                if (cell.top <> null) {
                    cell.xv += (cell.top.x - cell.x) *k2;
                    cell.yv += (cell.top.y + gSize - cell.y) *k2;
                }
                if (cell.bottom <> null) {
                    cell.xv += (cell.bottom.x - cell.x) *k2;
                    cell.yv += (cell.bottom.y - gSize - cell.y) *k2;
                }
                cell.xv *= damp;
                cell.yv *= damp;
                cell.x += cell.xv.intValue();
                cell.y += cell.yv.intValue();
            }
            i++;
        }
    }

    init {
        for (cell in cells) {
            cell.initAdjacencies();
        }

        this.content = [
        Group {
            content: 
            [Rectangle {
                width: bind width * gSize;
                height: bind height * gSize;
                fill: Color.BLACK;
                onMousePressed: function(mouseEvent:MouseEvent):Void {
                    var row = (mouseEvent.getCanvasY() / gSize).intValue();
                    var col = (mouseEvent.getCanvasX() / gSize).intValue();
                    if (row >= 0 and row < height and col >= 0 and col < width) {
                        xmouse = mouseEvent.getCanvasX();
                        ymouse = mouseEvent.getCanvasY();
                        drag = row * width + col;
                    }
                    dragging = true;
                }
                onMouseDragged: function(mouseEvent:MouseEvent):Void {
                    xmouse = mouseEvent.getCanvasX();
                    ymouse = mouseEvent.getCanvasY();
                }
                onMouseReleased: function(mouseEvent:MouseEvent):Void {
                    dragging = false;
                    drag = -1;
                }
            },
            Group {
                content:
                for (cell in cells) 
                  Group {
                    content: 
                    [if (cell.right == null) null else Line {
                        stroke: Color.WHITE;
                        startX: bind cell.x;
                        startY: bind cell.y;
                        endX: bind cell.right.x;
                        endY: bind cell.right.y;
                    },
                    if (cell.bottom == null) null else Line {
                         stroke: Color.WHITE;
                         startX: bind cell.x;
                         startY: bind cell.y;
                         endX: bind cell.bottom.x;
                         endY: bind cell.bottom.y;
                    }]
                  }
            }]
        }];
    }
}

var grid = new Grid;

var t = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
    KeyFrame {
        time: 1ms
        action: function() { grid.update(); }
        canSkip: true
    }
    ]
};

Frame {
    closeAction: function() {System.exit(0);}
    width: 500
    height: 500
    visible: true
    content: Canvas {
        content: grid;
    }
}

t.start();
