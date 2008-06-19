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

package demo.mediatest;

import javafx.gui.*;
import javafx.gui.swing.*;
import java.lang.System;
import javafx.animation.*;


Frame {
    closeAction: function() {System.exit(0);}  
    width: 800, height: 540 
    
    title: "Media Bounce Demo";
    visible: true    
    
    var c=Circle{radius:50, centerX:85, centerY:60};
    
    // variables for animation
    var x = 0.0;
    var y = 0.0;
    var sx = 1.0;
    var sy = 1.0;      
    var r = 50.0;
    var rotation = 0.0;
    
    content:       
    Canvas {
        background: Color.color(0.3, 0.3, 0.3)        
      
        
        var video = MxMedia{fileName:"ducks"};
        
          
        var player = javafx.gui.MediaPlayer {
            media: video,
            autoPlay: true;
            repeatCount: MediaPlayer.REPEAT_FOREVER
            mute:true; volume:0;
            onError: function(e:MediaError) {
                System.out.println("Got an error" + e );
            }                            
        };
        
    content:          
    MediaView{ mediaPlayer:player, 
        onMouseClicked: function(e) {
            if (player.paused) {
                player.play();
                } else {
                player.pause();
            }
    }    
    
    clip: c;
    
    onMouseExited: function(e:MouseEvent) { e.node.opacity = 1.0 }
    onMouseEntered: function(e:MouseEvent) { e.node.opacity = 1.0 }
    
    //Animation 
            transform: bind [Transform.translate(x, y), 
                                 Transform.translate(50.0, 50.0),
                                 Transform.scale(sx, sy),
                                 Transform.translate(-50.0, -50.0)]
                
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
                    var rt = Timeline {
                        keyFrames: [
                            KeyFrame {
                                time:0s
                                values: [rotation => 0]
                            }
                        ]

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
                }
                      
}
}
  



