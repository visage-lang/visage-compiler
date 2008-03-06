/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;


public class TeslaTab2 extends TeslaTab {
    
    function composeNode():Node {
        return Group {
            content:
            [rect = Rect {
//                attribute: rect
                height: bind text.currentHeight + 15
                width: bind text.currentWidth + 10
                stroke: bind (if (selected) then Color.WHITE else Color.rgba(0, 0, 0, 0)) as Paint
                fill: Color.rgba(0, 0, 0, 0) as Paint
                onMouseReleased: function(e:CanvasMouseEvent) {
                    this.selected = true;
                }
                selectable: true
                cursor: Cursor.HAND
            },
            Group {
                transform: Transform.translate(5, 7.5)
                content:
                text = Text {
//                    attribute: text
                    font: Font.Font("Arial", ["BOLD"], 12)
                    content: bind title
                    var grayColor = Color.color(0.7, 0.7, 0.7, 1)
                    var cond = bind not selected and rect.hover
                    fill: bind (if (cond) then grayColor else Color.WHITE) as Paint
                }
            }]
        }
        ;
    }
}
