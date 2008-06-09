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
 

package demo.mediatest;

import javafx.gui.*;
import javafx.gui.effect.*;

import java.lang.System;


var media = MxMedia {fileName:"ducks"}
var mediaPlayer = MediaPlayer { media:media, autoPlay:true }
var currentEffect:Effect = Reflection{}
var mediaView = MediaView {mediaPlayer:mediaPlayer
    effect: bind currentEffect
}
Frame {content:Canvas{content:mediaView, background:Color.GRAY} 
        visible:true
        title:bind media.source,
        closeAction: function() {System.exit(0);}
        menus : [Menu{text: "Effects", items:[
            MenuItem{text:"Sepia"
                action: function():Void {currentEffect = SepiaTone{}}
            },
            MenuItem{text:"Reflection"
                action: function():Void {currentEffect = Reflection{}}
            },
            MenuItem{text:"Perspective"
                action: function():Void {
                    currentEffect = myPerspective}     
            }
        ]}]
}

var myPerspective = PerspectiveTransform {
    ulx:0
    uly:0
    urx:570
    ury:50
    llx:30
    lly:286
    lrx:500
    lry:237
};


