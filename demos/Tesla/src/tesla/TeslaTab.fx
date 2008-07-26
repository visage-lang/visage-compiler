/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.scene.*;
import java.lang.System;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.ext.swing.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.input.*;

public class TeslaTab extends CustomNode {
    private attribute tabBackground: Group;
    protected attribute text: Text;
    protected attribute rect: Node;
    public attribute font: Font;
    public attribute title: String;
    public attribute selected: Boolean;
    public attribute content: Node[];
    private attribute tab2ndImage:Image = Image {url: "{__DIR__}Image/2ndnav_tab_bg.gif"};
    
    
    
    function create():Node {
        Group  {
            content:
            [ImageView {
                visible: bind selected
                image: Image {url: "http://teslamotors.com/images/nav/2ndnav_r1_c2.gif"}
            },
            tabBackground = Group {
                var textWidth = bind text.getWidth()+2;
                visible: bind selected
                transform: Transform.translate(18, 0)
                content: bind [
                    Rectangle {
                        height: 34
                        width: textWidth
                        stroke: Color.TRANSPARENT
                    },
                    for(i in [0.0..textWidth]) {
                        ImageView {
                            translateX: i
                            image: tab2ndImage 
                        }
                    }
                ]
            },
            /****
            Rectangle {
                visible: bind selected
                transform: Transform.translate(18, 0)
                fill: Pattern {
                    content: Group {
                        content: ImageView {image: Image {url: "{__DIR__}Image/2ndnav_tab_bg.gif"}}
                    }
                }
                height: 34
                width: bind text.getWidth()+2
            },
                ***/
            ImageView {
                visible: bind selected
                transform: bind Transform.translate(tabBackground.getX()+tabBackground.getWidth(), 0)
                image: Image {url: "http://teslamotors.com/images/nav/2ndnav_r1_c4.gif"}
            },                
            text = Text {
                transform: Transform.translate(13, 12)
//                attribute: text
                textOrigin: TextOrigin.TOP
                font: Font.font("Arial", FontStyle.BOLD, 10)
                content: bind title
                fill: bind (if (not selected and rect.isMouseOver()) then Color.color(0.7, 0.7, 0.7, 1) else Color.WHITE)
            },

            rect = Rectangle {
//                attribute: rect
                transform: Transform.translate(19, 10)
                cursor: Cursor.HAND
                height: 25
                width: bind text.getWidth()
                onMouseReleased: function(e:MouseEvent) {
                    System.out.println("tab selected...");
                    selected = true;
                }
                fill: Color.TRANSPARENT
            }]
        };
    }
}
