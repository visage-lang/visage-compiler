/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class Arrow extends CompositeNode {
    public attribute color: Paint = Color.color(.75, .75, .75, 1) as Paint;
    public attribute left: Boolean;
    public attribute arrowShape: Shape = Path {
        d: Path {  
            outline: true
            d:
            [MoveTo {
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
            }]
            stroke: bind color;
            strokeWidth: 5
        //            strokeLineJoin: BEVEL
        }
        fill: bind color
    };
    
    
    function composeNode():Node {
        return Group {
            transform: bind if (left) then Rotate.rotate(180, arrowShape.currentWidth/2, arrowShape.currentHeight/2) else null
            content: bind arrowShape
        };
    }
}    


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
