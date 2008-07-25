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

package bounce;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import javafx.ext.swing.SwingFrame;
import javafx.ext.swing.Canvas;
import javafx.ext.swing.SwingSlider;
import javafx.ext.swing.Label;
import javafx.scene.geometry.Rectangle;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import java.lang.System;

SwingFrame {
    closeAction: function() {System.exit(0);}
    title: "Ball";
    visible: true
    content:
    Canvas {
        content:
        Group {
            
            // model variables
            
            var x = 0.0;
            var y = 0.0;
            var sx = 1.0;
            var sy = 1.0;
            var r = 50.0;
            var color1 = Color.SILVER
            var color2 = Color.MAROON
            
            content:
            [Rectangle {height: 460, width: 800, fill: Color.BLACK},
            Group {
                transform: bind [Transform.translate(x, y), 
                                 Transform.translate(50.0, 50.0),
                                 Transform.scale(sx, sy),
                                 Transform.translate(-50.0, -50.0)]
                content: 
                [Rectangle {
                    stroke: Color.BLACK
                    x: 0
                    y: 0
                    arcHeight: 100, arcWidth: 100
                    width: 100
                    height: 100
                    fill: RadialGradient {
                        var d = 2*r
                        proportional: false
                        centerX: r
                        centerY: r
                        focusX: 0.75*d
                        focusY: 0.25*d
                        radius: r
                        
                        stops:
                        [Stop {
                            offset: 0
                            color: color1
                        },
                        Stop {
                            offset: 0.85,
                            color: color2
                        },
                        Stop {
                            offset: 1
                            color: color2
                        }]
                        
                    }
                    
                    var ax = Timeline {
                        // x
                        repeatCount: Timeline.INDEFINITE
                        autoReverse: true
                        keyFrames:
                        [KeyFrame {
                            time: 0s
                            values: x => 0.0
                        },
                        KeyFrame {
                            time: 10s
                            values: x => 700.0 tween Interpolator.LINEAR
                        }]
                    }
                    
                    var ay = Timeline {
                        // y
                        repeatCount: Timeline.INDEFINITE
                        keyFrames:
                        [KeyFrame {
                            time: 0s
                            values: y => 0.0
                        },
                        KeyFrame {
                            time: 2.2s
                            values: y => 375.0 tween (Interpolator.SPLINE(0, 0, 0.5, 0))
                        },
                        KeyFrame {
                            time: 2.25s
                            values: y => 375.0
                        },
                        KeyFrame {
                            time: 4.5s
                            values: y => 0.0 tween (Interpolator.SPLINE(0, 0, 0, 0.5))
                        }]
                    }
                    
                    var sxy =  Timeline {
                        // scale x y
                        repeatCount: Timeline.INDEFINITE
                        keyFrames:
                        [KeyFrame {
                            time: 2s
                            values: [
                                sx => 1.0,
                                sy => 1.0
                            ]
                        },
                        KeyFrame {
                            time: 2.25s
                            values: [
                                sx => 1.2 tween Interpolator.LINEAR,
                                sy => 0.7 tween Interpolator.LINEAR
                            ]
                        },
                        KeyFrame {
                            time: 2.5s
                            values: [
                                sx => 1.0 tween Interpolator.LINEAR,
                                sy => 1.0 tween Interpolator.LINEAR
                            ]
                        },
                        KeyFrame {
                            time: 4.5s
                            values: [
                                sx => 1.0,
                                sy => 1.0
                            ]
                        }]
                    }
                    
                    var t = Timeline {
                        repeatCount: Timeline.INDEFINITE
                        keyFrames: 
                        KeyFrame {
                            time: 0s
                            timelines: [ax, ay, sxy]
                        }
                    }
                    onMouseClicked: function(e) {
                        t.start();
                    }                    
                }]
            }]
        }
    }
}
