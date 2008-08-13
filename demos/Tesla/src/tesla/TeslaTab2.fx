/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.geometry.*;
import javafx.ext.swing.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.input.*;

public class TeslaTab2 extends TeslaTab {
    
    override function create():Node {
        return Group {
            content:
            [rect = Rectangle {
//                var: rect
                height: bind text.getHeight() + 15
                width: bind text.getWidth() + 10
                stroke: bind (if (selected) then Color.WHITE else Color.TRANSPARENT)
                fill: Color.TRANSPARENT
                onMouseReleased: function(e:MouseEvent) {
                    this.selected = true;
                }
                cursor: Cursor.HAND
            },
            Group {
                transform: Transform.translate(5, 7.5)
                content:
                text = Text {
                    var grayColor = Color.color(0.7, 0.7, 0.7, 1);
                    var cond = bind not selected and rect.isMouseOver();
                    textOrigin: TextOrigin.TOP
                    font: Font.font("Arial", FontStyle.BOLD, 12)
                    content: bind title
                    fill: bind (if (cond) then grayColor else Color.WHITE)
                }
            }]
        }
        ;
    }
}
