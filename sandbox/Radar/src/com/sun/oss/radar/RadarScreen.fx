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

package com.sun.oss.radar;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;
import java.awt.Point;
import java.awt.geom.Point2D.Double;
import java.util.Date;

/**
 * @author jclarke
 */

public class RadarScreen extends CompositeNode {
    public var height:Number;
    public var width:Number;
    public var foreground:Color = Color.rgba(0, 204, 0, 205);
    public var blips:Blip[];
    
    
    var cx:Number = bind width/2;
    var cy:Number = bind width/2; // on purpose to keep aspect the same
    var radius:Number = bind width/2;
    var extent = bind java.lang.Math.sqrt(2*radius*radius);
    
    var angle:Number;
    var pf: PointerFactory = PointerFactory{};
    var bpAngle:Pointer = bind pf.make(angle);
    var pAngle:Pointer = bpAngle.unwrap();
    var sweepLine:Group = Group {
        transform: bind [Transform.translate(cx,cy),Rotate{angle: angle}]
        content: [
            Polygon {
                points: bind [
                    0,0,
                    0, radius*1.5,
                    -40, radius*1.5
                ]
                fill: LinearGradient {
                    startX: 0
                    startY: 0
                    endX: -40
                    endY: 0
                    stops: for(x in [0.1..0.9 step 0.1])
                        Stop {offset: x+.1; color: Color.color(0, .8, 0, x) }
                }
            }

        ]
    };
    var sweep = Timeline {
        keyFrames:[ 
            KeyFrame { 
                keyTime: 0s, 
                keyValues:  NumberValue {
                    target: pAngle;
                    value: 0
                }            
            },
            KeyFrame { 
                keyTime: 2s, 
                keyValues:  NumberValue {
                    target: pAngle;
                    value: 90
                    interpolate: NumberValue.LINEAR
                }            
            }, 
            KeyFrame { 
                keyTime: 4s, 
                keyValues:  NumberValue {
                    target: pAngle;
                    value: 180
                    interpolate: NumberValue.LINEAR
                }            
            },
            KeyFrame { 
                keyTime: 6s, 
                keyValues:  NumberValue {
                    target: pAngle;
                    value: 270
                    interpolate: NumberValue.LINEAR
                }            
            },   
            KeyFrame { 
                keyTime: 8s, 
                keyValues:  NumberValue {
                    target: pAngle;
                    value: 360
                    interpolate: NumberValue.LINEAR
                }            
            }             
        ]
        repeatCount: java.lang.Double.POSITIVE_INFINITY 
    }
    var timeStr:String = "{new Date()}";
    var clock:Timeline = Timeline {
        keyFrames:[ 
            KeyFrame { 
                keyTime: 1s, 
                action: function() {
                    timeStr = "{new Date()}";
                }
            },
        ]
        repeatCount: java.lang.Double.POSITIVE_INFINITY
    };
    
    public function composeNode(): Node {
        
        var self = this;
        Group {
            content: [
                // Cross hairs
                Line { 
                    x1: bind cx
                    y1: bind cy - radius/4 + radius/8
                    x2: bind cx
                    y2: bind cy + radius/4 - radius/8
                    stroke: bind foreground as Paint
                },
                Line {
                    x1: bind cx - radius/4 + radius/8
                    y1: bind cy
                    x2: bind cx + radius/4 - radius/8
                    y2: bind cy
                    stroke: bind foreground as Paint
                },
                // horizontal lines
                Line {
                    x1: bind cx - 3 * radius/4 - radius/8
                    y1: bind cy
                    x2: bind cx - radius/2 + radius/8
                    y2: bind cy
                    stroke: bind foreground as Paint
                },
                Line {
                    x1: bind cx +  3 * radius/4 + radius/8
                    y1: bind cy
                    x2: bind cx + radius/2 - radius/8
                    y2: bind cy
                    stroke: bind foreground as Paint
                },
                // vertical lines
                Line {
                    x1: bind cx
                    y1: bind cy - 3 * radius/4 - radius/8
                    x2: bind cx
                    y2: bind cy - radius/2 + radius/8
                    stroke: bind foreground as Paint
                },
                Line {
                    x1: bind cx
                    y1: bind cy + 3 * radius/4 + radius/8
                    x2: bind cx
                    y2: bind cy + radius/2 - radius/8
                    stroke: bind foreground as Paint
                },
                // 45 degree lines
                Group {
                    transform: Rotate{angle:45, cx:bind cx, cy: bind cy}
                    content: [
                        // horizontal lines
                        Line {
                            x1: bind cx - 3 * radius/4 - radius/8
                            y1: bind cy
                            x2: bind cx - radius/2 + radius/8
                            y2: bind cy
                            stroke: bind foreground as Paint
                        },
                        Line {
                            x1: bind cx +  3 * radius/4 + radius/8
                            y1: bind cy
                            x2: bind cx + radius/2 - radius/8
                            y2: bind cy
                            stroke: bind foreground as Paint
                        },
                        // vertical lines
                        Line {
                            x1: bind cx
                            y1: bind cy - 3 * radius/4 - radius/8
                            x2: bind cx
                            y2: bind cy - radius/2 + radius/8
                            stroke: bind foreground as Paint
                        },
                        Line {
                            x1: bind cx
                            y1: bind cy + 3 * radius/4 + radius/8
                            x2: bind cx
                            y2: bind cy + radius/2 - radius/8
                            stroke: bind foreground as Paint
                        }
                    ]
                },
                // Inner Circle
                Circle {
                    cx: bind cx
                    cy: bind cy
                    radius: bind radius/4
                    fill: Color.rgba(0,0,0,0) as Paint
                    strokeWidth: 4.0
                    stroke: bind foreground as Paint
                },
                // Middle Circle
                Circle {
                    cx: bind cx
                    cy: bind cy
                    radius: bind radius/2
                    fill: Color.rgba(0,0,0,0) as Paint
                    strokeWidth: 4.0
                    stroke: bind foreground as Paint
                },
                // Outer Circle
                Circle {
                    cx: bind cx
                    cy: bind cy
                    radius: bind 3 * radius/4
                    fill: Color.rgba(0,0,0,0) as Paint
                    strokeWidth: 4.0
                    stroke: bind foreground as Paint
                },
                Text {
                    font: Font.Font("Courier", ["BOLD"], 14);
                    fill: bind foreground as Paint
                    content: bind timeStr
                    x: 5
                    y: bind height - 30
                },
                sweepLine,
                blips
            ]
        };
    }
    
    postinit {
        sweep.start();
        clock.start();
    }

}
