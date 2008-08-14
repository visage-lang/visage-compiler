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
import javafx.ext.swing.*;
import javafx.scene.paint.*;

public class Arrow extends CustomNode {
    public var color: Paint = Color.color(.75, .75, .75, 1);
    public var left: Boolean;
    public var arrowShape: Shape = Path {
            //outline: true
            elements: [
                MoveTo {
                    x: 0
                    y: 10
                },
                LineTo {
                    x: 25
                    y: 10
                },
                LineTo {
                    x: 15
                    y: 0
                },
                MoveTo {    
                    x: 25
                    y: 10
                },
                LineTo {
                    x: 15
                    y: 20
                }
            ]
            stroke: bind color;
            strokeWidth: 5
            strokeLineJoin: StrokeLineJoin.BEVEL
    };
    
    
    override function create():Node {
        return Group {
            transform: if (left) then Transform.rotate(180, arrowShape.getWidth()/2, arrowShape.getHeight()/2) else null
            content: bind arrowShape
        };
    }
}    


// test
function run() {
    Canvas {
       var arrow = Arrow{}
        content:
        arrow,
    /*    Subtract {
            transform: Translate.translate(100, 100)
            shape1: Rect {
                height: 44
                width: 44
            }
            //        var arrow = 
            shape2: Union {
                transform: [Translate.translate(10, 12), Rotate.rotate(180, 12.5, 10)]
                content: Arrow{}.arrowShape 
            }
            fill: Color.rgba(.7, .7, .7, 1)
        }*/
    }
}
