/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import java.lang.System;
import javafx.ext.swing.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.input.*;
import javafx.animation.*;

class TeslaPage extends CustomNode {
    public attribute menuImageUrl: String;
    public attribute content: Node[];
    public attribute menuSelect: function(i:Integer):Void;
    public attribute buySelect: function();
    attribute opacityValue: Number = 1.0;
    attribute fadein:Timeline = Timeline {
        keyFrames: [
            KeyFrame {
                time: 0s
                values: opacityValue => 0
            },
            KeyFrame {
                time: 1s
                values: opacityValue => 1.0 tween Interpolator.EASEBOTH
            }
        ]
    };  
    attribute fadeout:Timeline = Timeline {
        keyFrames: [
            KeyFrame {
                time: 0s
                values: opacityValue => 1.0
            },
            KeyFrame {
                time: 1s
                values: opacityValue => 0.0 tween Interpolator.EASEBOTH
            }
        ]
    };     
    
    override attribute visible on replace  {
        if (visible) {
            fadeout.stop();
            fadein.start();
        }else {
            fadein.stop();
            fadeout.start();
        }
    }
    
    override function create():Node {
        return Group {
            content: 
            [ImageView {
            // 777x33
            //image: bind select Image {url: u} from u in menuImageUrl
                image: Image {url: bind menuImageUrl }
            },
            Rectangle {
                cursor: Cursor.HAND
                transform: Transform.translate(180, 0)
                height: 40
                width: 380
                fill: Color.TRANSPARENT;
                var offsets = reverse [0, 60, 155, 250, 310, 365];
                onMouseReleased: function(e:MouseEvent) {
                    var x = e.getX();
                    for (i in offsets) {
                        if (x > i) {
                             menuSelect(5- indexof i);
                             break;
                        }
                    }
                }
            },
            Rectangle {
                cursor: Cursor.HAND
                transform: Transform.translate(560, 0)
                height: 33
                width: 60
                fill: Color.TRANSPARENT
                onMouseClicked: function(e:MouseEvent) {
                    //             println("buy...");
                    if (buySelect != null) {
                        buySelect();
                    }
                }
            },
            Group {
                transform: Transform.translate(40, 33)
                opacity: bind opacityValue
                content: bind content
            }]
        };
        
    }
    
}

Canvas {
    content: TeslaPage{}
}
