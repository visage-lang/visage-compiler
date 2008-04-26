/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package guitar;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Node;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Translate;
import javafx.ui.canvas.ImageView;
import javafx.ui.canvas.CanvasMouseEvent;
import javafx.ui.*;
import java.lang.Math;
import java.lang.System;
import java.net.URL;
import java.applet.AudioClip;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

/**
 * @author jclarke
 */


public class GuitarString extends CompositeNode {
    attribute audioClip: AudioClip;
    attribute theGuitar: Guitar;
    attribute wound: Boolean = true;
    attribute note: String;
    attribute pf: PointerFactory = PointerFactory{};
    attribute soundUrl: URL = bind if (note == null) then null else this.getClass().getResource("Resources/sound/{note}.au")
        on replace {
           //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      public function run():Void {
                            theGuitar.loadingSound++;
                            //var clip;
                            //TODO DO  - this is a work around until a more permanent solution is provided
                            //javax.swing.SwingUtilities.invokeAndWait(java.lang.Runnable {
                            //    public function run():Void {
                                    audioClip = java.applet.Applet.newAudioClip(soundUrl); 
                            //    }
                           // });
                            //audioClip = clip;
                            theGuitar.loadingSound--;
                      }
            });
            //System.out.println("soundUrl={soundUrl}");
    }
    attribute imageUrl: String = bind this.getClass().getResource('Resources/{if (wound) then "Wound" else "Plain"}.png').toString();
    public function composeNode(): Node {
        var self = this;
        Group {
            var x = 0;
            var bpx = bind pf.make(x)
            var px = bpx.unwrap();

            var clip = Timeline {
                keyFrames: [
                    KeyFrame {
                        keyTime: 0s
                        keyValues:  NumberValue {
                            target: px;
                            value: 1.0
                        }
                    },
                    KeyFrame {
                        keyTime: 41ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    },      
                    KeyFrame {
                        keyTime: 81ms
                        keyValues:  NumberValue {
                            target: px;
                            value: -1;
                        }
                    },
                    KeyFrame {
                        keyTime: 121ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    },                            
                    KeyFrame {
                        keyTime: 161ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 1.0
                        }
                    }, 
                    KeyFrame {
                        keyTime: 181ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    },
                    KeyFrame {
                        keyTime: 221ms
                        keyValues:  NumberValue {
                            target: px;
                            value: -1.0
                        }
                    },
                    KeyFrame {
                        keyTime: 261ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    },
                    KeyFrame {
                        keyTime: 301ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 1.0
                        }
                    },
                    KeyFrame {
                        keyTime: 341ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    },
                    KeyFrame {
                        keyTime: 361ms
                        keyValues:  NumberValue {
                            target: px;
                            value: -1.0
                        }
                    },   
                    KeyFrame {
                        keyTime: 361ms
                        keyValues:  NumberValue {
                            target: px;
                            value: -1.0
                        }
                    },
                    KeyFrame {
                        keyTime: 401ms
                        keyValues:  NumberValue {
                            target: px;
                            value: -1.0
                        }
                    },
                    KeyFrame {
                        keyTime: 441ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    }, 
                    KeyFrame {
                        keyTime: 500ms
                        keyValues:  NumberValue {
                            target: px;
                            value: 0.0
                        }
                    }                            
                ]

            };
            
            content: [
            ImageView { 
                cursor: Cursor.HAND
                transform: bind [Translate {x: x, y: 0}]
                image: Image {
                    url: bind imageUrl
                }
                var playing = false
                onMouseMoved: function(e:CanvasMouseEvent):Void {
                    if (playing) {return;}

                    theGuitar.play(self);
                    clip.start();
                }
            }]
        }
    }
}