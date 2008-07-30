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

package guitar;

import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.image.*;
import javafx.input.*;
import javafx.ext.swing.*;
import java.lang.Math;
import java.lang.System;
import java.net.URL;
import java.applet.AudioClip;
import javafx.animation.*;

/**
 * @author jclarke
 */


public class GuitarString extends CustomNode {
    attribute audioClip: AudioClip;
    attribute theGuitar: Guitar;
    attribute wound: Boolean = true;
    attribute note: String;
    attribute soundUrl: URL = bind if (note == null) then null else this.getClass().getResource("Resources/sound/{note}.au")
        on replace {
           //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      override function run():Void {
                            theGuitar.loadingSound++;
                            audioClip = java.applet.Applet.newAudioClip(soundUrl); 
                            theGuitar.loadingSound--;
                      }
            });
            //System.out.println("soundUrl={soundUrl}");
    }
    attribute imageUrl: String = bind this.getClass().getResource('Resources/{if (wound) then "Wound" else "Plain"}.png').toString();
    override function create(): Node {
        var self = this;
        Group {
            var x = 0.0;

            var clip = Timeline {
                keyFrames: [
                    KeyFrame { time: 0s,    values: x => 1.0 },
                    KeyFrame { time: 41ms,  values: x => 0.0 },      
                    KeyFrame { time: 81ms,  values: x => -1.0 },
                    KeyFrame { time: 121ms, values: x => 0.0 },
                    KeyFrame { time: 161ms, values: x => 1.0 },
                    KeyFrame { time: 181ms, values: x => 0.0 },
                    KeyFrame { time: 221ms, values: x => -1.0 },
                    KeyFrame { time: 261ms, values: x => 0.0 },
                    KeyFrame { time: 301ms, values: x => 1.0 },
                    KeyFrame { time: 341ms, values: x => 0.0 },
                    KeyFrame { time: 371ms, values: x => -1.0 },
                    KeyFrame { time: 401ms, values: x => 0.0 },
                    KeyFrame { time: 441ms, values: x => 1.0 },
                    KeyFrame { time: 500ms, values: x => 0.0 },
                ]

            };
            
            content: [
            ImageView { 
                var playing = false;
                cursor: Cursor.HAND
                transform: bind [Translate {x: x, y: 0}]
                image: Image {
                    url: bind imageUrl
                }
                onMouseMoved: function(e:MouseEvent):Void {
                    if (playing) {return;}

                    theGuitar.play(self);
                    clip.start();
                }
            }]
        }
    }
}
