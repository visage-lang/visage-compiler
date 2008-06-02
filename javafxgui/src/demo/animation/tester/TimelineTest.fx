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
package demo.animation.tester;

import javafx.gui.*;
import javafx.animation.*;
import java.lang.System;
import java.lang.Thread;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.Timer;

class BouncingNode {
    attribute x: Integer;
    attribute y: Integer;
    attribute w: Integer;
    attribute h: Integer;
    attribute rx: Integer;
    attribute ry: Integer;
    private attribute tx: Integer;
    private attribute ty: Integer;
    attribute forwardX: Boolean = true;
    attribute forwardY: Boolean = true;
    attribute speed: Integer = 5;
    attribute node: Node;
    function update() {
        var dx = 0;
        if (forwardX) {
            dx = tx + speed;
            if (w < dx + x + rx) {
                dx = tx - speed;
                forwardX = false;
            }
        } else {
            dx = tx - speed;
            if (dx + x - rx < 0) {
                dx = tx + speed;
                forwardX = true;
            }
        }

        var dy = 0;
        if (forwardY) {
            dy = ty + speed;
            if (h < dy + y + ry) {
                dy = ty - speed;
                forwardY = false;
            }
        } else {
            dy = ty - speed;
            if (dy + y - ry < 0) {
                dy = ty + speed;
                forwardY = true;
            }
        }
        tx = dx;
        ty = dy;
    }
    init {
        rx = 20;
        ry = 20;
        tx = 0;
        ty = 0;
        node = Ellipse {
            centerX: x
            centerY: y
            radiusX: rx
            radiusY: ry
            translateX: bind tx
            translateY: bind ty
            fill: Color.RED
        };
    }
}

var w = 400;
var h = 600;
var balls: BouncingNode[] = [];
var random = new java.util.Random();
for (i in [0..3000]) {
    insert BouncingNode {
        x: 150 + i * 300
        y: 30 + i * 300
        w: w
        h: h
        speed: random.nextInt(5)
        forwardX: (i % 2 == 0)
        forwardY: (i % 2 <> 0)
    } into balls;
}
Frame {
    content: Canvas {
        content: for (ball in balls) ball.node
    }
    width: w
    height: h
    closeAction: function() {
        System.exit(0);
    }
    visible: true
};

var task = ActionListener {
    public function actionPerformed(event: ActionEvent) {
        for (ball in balls)
            ball.update();
    }
};

var timer = new Timer(16, task);
timer.setRepeats(true);

var timeline = Timeline {
    repeatCount: java.lang.Double.POSITIVE_INFINITY
    keyFrames: KeyFrame {
        time: 16ms
        action: function() { for (ball in balls) ball.update(); }
        canSkip: true
    }
};
timeline.start();
//timer.start();
