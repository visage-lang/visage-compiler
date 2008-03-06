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
import java.lang.System;

public class TeslaTab extends CompositeNode {
    private attribute tabBackground: Rect;
    protected attribute text: Text;
    protected attribute rect: Node;
    public attribute font: Font;
    public attribute title: String;
    public attribute selected: Boolean;
    public attribute content: Node[];
    
    
    
    function composeNode():Node {
        Group  {
            content:
            [ImageView {
                visible: bind selected
                image: Image {url: "http://teslamotors.com/images/nav/2ndnav_r1_c2.gif"}
            },
            tabBackground = Rect {
                visible: bind selected
                transform: Transform.translate(18, 0)
                fill: Pattern {
                    content: Group {
                        content: ImageView {image: Image {url: "{__DIR__}Image/2ndnav_tab_bg.gif"}}
                    }
                }
                height: 34
                width: bind text.currentWidth+2
            },
            text = Text {
                transform: Transform.translate(19, 12)
//                attribute: text
                font: Font.Font("Arial", ["BOLD"], 12)
                content: bind title
                fill: bind (if (not selected and rect.hover) then Color.color(0.7, 0.7, 0.7, 1) else Color.WHITE) as Paint
            },
            ImageView {
                visible: bind selected
                transform: bind Transform.translate(tabBackground.currentX+tabBackground.currentWidth, 0)
                image: Image {url: "http://teslamotors.com/images/nav/2ndnav_r1_c4.gif"}
            },
            rect = Rect {
//                attribute: rect
                transform: Transform.translate(19, 10)
                cursor: Cursor.HAND
                height: 25
                width: bind text.currentWidth
                onMouseReleased: function(e:CanvasMouseEvent) {
                    System.out.println("tab selected...");
                    this.selected = true;
                }
                selectable: true
                fill: Color.rgba(0, 0, 0, 0) as Paint
            }]
        };
    }
}
