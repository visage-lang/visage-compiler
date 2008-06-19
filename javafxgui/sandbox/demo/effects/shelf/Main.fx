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

package demo.effects.shelf;

import javafx.animation.*;
import javafx.gui.*;
import javafx.gui.swing.*;
import javafx.gui.effect.*;
import javafx.gui.effect.light.*;
import java.lang.System;
import java.lang.Math;

var hSlider = Slider {minimum: 0 maximum: 100 value: 50};

class TiltedNode extends CustomNode {
    
    public attribute centerX:Number = 0.0 on replace {
        updateTx();
    }
    public attribute centerY:Number = 0.0 on replace {
        updateTx();
    }
    public attribute angle:Number = 0.0 on replace {
        updateTx();
    }
    public attribute scale:Number = 1.0 on replace {
        updateTx();
    }
    public attribute item:Node on replace {
        updateTx();
    }

    private attribute tx:Effect;

    private function getPerspectiveTransform(rotationRadians:Number,
                                             x:Number, y:Number,
                                             w:Number, h:Number):PerspectiveTransform
    {
        // Hack: account for reflection
        h *= 1.75;
        // First make a camera looking towards Z=+inf
        // from the center of the screen:
        var camera = Surface3D {};
        camera.translate(x+(w/2.0),y+(h/2.0), 0);
        camera.concat(1, 0, 0, 0,
                      0, 1, 0, 0,
                      0, 0, 0, 0,
                      0, 0, 1, 0);
        // Constructor creates a unit square x=0->1, y=0->1, z=0
        // Note that z=0 is right on top of the camera
        // z=0 makes things "infinitely big"
        // z=1 makes things "life size"
        // z=2 makes things "half life size", etc.
        var scene = Surface3D {};
        // Scale it for "life size" being WxH
        scene.scale(2*w, 2*h, 1.0/scale);
        // Move center to origin, and move it back to z=+2
        // (thus the object will be W/2 x H/2 and centered
        // around the eye of the camera at this point)
        scene.translate(-0.5, -0.5, 2);
        // Rotates around the vertical axis positioned at
        // 0.5 Z coordinates behind the vertical center of
        // the rectangular object
        scene.rotateXZaroundY(0.5, 0.0, rotationRadians);
        // Return Effect
        getPerspectiveTransform(scene, camera);
    }

    private function getPerspectiveTransform(scene:Surface3D, camera:Surface3D):PerspectiveTransform {
        var ul:Surface3D.Point3D = new Surface3D.Point3D(0, 0, 0);
        var ur:Surface3D.Point3D = new Surface3D.Point3D(1, 0, 0);
        var ll:Surface3D.Point3D = new Surface3D.Point3D(0, 1, 0);
        var lr:Surface3D.Point3D = new Surface3D.Point3D(1, 1, 0);
        scene.transform(ul);
        scene.transform(ur);
        scene.transform(ll);
        scene.transform(lr);
        camera.transform(ul);
        camera.transform(ur);
        camera.transform(ll);
        camera.transform(lr);
        PerspectiveTransform {
            input: Reflection {}
            ulx: ul.getX() uly:ul.getY()
            urx: ur.getX() ury:ur.getY()
            lrx: lr.getX() lry:lr.getY()
            llx: ll.getX() lly:ll.getY()
        }
    }

    private function updateTx() {
        var x = centerX - (item.getWidth() / 2);
        var y = centerY - (item.getHeight() / 2);
        var w = item.getWidth();
        var h = item.getHeight();
        tx = getPerspectiveTransform(Math.toRadians(angle), x, y, w, h);
    }

    protected function create():Node {
        return Group {
            effect: bind tx
            content: Group {
                //effect: bind if (item.isMouseOver()) Glow {} else null
                content: bind item
            }
        }
    }
}

public class DisplayShelf extends CustomNode {

    private attribute maxWidth:Number;

    public attribute items:Node[] on replace {
        delete tnodes;
        maxWidth = 0;
        for (item in items) {
            var iw = item.getWidth();
            if (iw > maxWidth) maxWidth = iw;
            insert TiltedNode { item: item } into tnodes;
        }
        updateNodes();
    }

    public attribute selected:Integer = 0 on replace {
        updateNodes();
    }

    public attribute centerX:Number = 0.0 on replace {
        updateNodes();
    }

    public attribute centerY:Number = 0.0 on replace {
        updateNodes();
    }

    public attribute minScale:Number = 0.6 on replace {
        updateNodes();
    }

    public attribute maxScale:Number = 1.5 on replace {
        updateNodes();
    }

    private attribute tnodes:TiltedNode[];
    
    private attribute position:Number = 0.0 on replace {
        updateNodePositions();
    }
    private attribute timeline:Timeline;

    private function updateNodes() {
        var max = tnodes.size()-1;
        var active = (position * max) as Integer;
        if (selected <> active) {
            var target = (selected as Number) / max;
            if (timeline <> null) timeline.stop();
            timeline = Timeline {
                keyFrames: [
                    KeyFrame {
                        time: 300ms
                        values: position => target tween Interpolator.EASEOUT
                    }
                ]
            };
            timeline.start();
        }
    }
    
    private function updateNodePositions() {
        var max = tnodes.size()-1;
        var active = (position * max) as Integer;
        var maxAngle = 60;
        var spacing = maxWidth;
        var totalw = max * spacing;
        var interval = 1.0 / max;
        for (i in [0..<tnodes.size()]) {
            var node = tnodes[i];
            node.centerX = centerX - (position * totalw) + (i * spacing);
            node.centerY = centerY;
            if (i == active) {
                var pct = 1.0 - ((position - (active*interval)) / interval);
                node.angle = (1.0 - pct) * -maxAngle;
                node.scale = minScale + (pct * (maxScale-minScale));
            } else if (i == active+1) {
                var pct = ((position - (active*interval)) / interval);
                node.angle = (1.0 - pct) * maxAngle;
                node.scale = minScale + (pct * (maxScale-minScale));
            } else if (i > active+1) {
                node.angle = maxAngle;
                node.scale = minScale;
            } else if (i < active) {
                node.angle = -maxAngle;
                node.scale = minScale;
            }
        }
    }

    protected function create():Node {
        return Group {
            content: bind tnodes
        }
    }
}

var canvas = Canvas {
    background: Color.BLACK
    content: DisplayShelf {
        selected: bind ((hSlider.value / 100.0) * 4) as Integer
        centerX: 350
        centerY: 250
        items: [
            Circle {
                fill: Color.ORANGE
                radius: 50
            },
            Rectangle {
                fill: Color.BLUE
                width: 120
                height: 100
            },
            Circle {
                fill: Color.RED
                radius: 50
            },
            Rectangle {
                fill: Color.YELLOW
                width: 120
                height: 100
            },
            Circle {
                fill: Color.GREEN
                radius: 50
            },
        ]
    }
};

Frame {
    title: "JavaFX Shelf Demo"
    width: 700
    height: 500
    content: BorderPanel {
        bottom: hSlider
        center: canvas
    }
    visible: true
}
