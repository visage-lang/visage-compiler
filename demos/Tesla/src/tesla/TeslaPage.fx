/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;

class TeslaPage extends CompositeNode {
    public attribute menuImageUrl: String;
    public attribute content: Node[];
    public attribute menuSelect: function(i:Integer):Void;
    public attribute buySelect: function();
    attribute opacityValue: Number;
    
    /*
    attribute visible:Boolean on replace (value) {
        if (value) {
        //   opacityValue = [0, 100] dur 1000 motion EASEBOTH while visible == value;
        }
    }
  */
    
    function composeNode():Node {
        return Group {
            content: 
            [ImageView {
            // 777x33
            //image: bind select Image {url: u} from u in menuImageUrl
                image: Image {url: bind menuImageUrl }
            },
            Rect {
                selectable: true
                cursor: Cursor.HAND
                transform: Transform.translate(180, 0)
                height: 40
                width: 380
                fill: Color.rgba(0, 0, 0, 0) as Paint
                var offsets = [180, 230, 335, 430, 490, 540] 
                onMouseReleased: function(e:CanvasMouseEvent) {
                    var x = e.localX;
                    System.out.println("localX={e.localX} e.x={e.x}");
                    //var i = (reverse (select indexof o from o in offsets where x > o))[0];
                    //             println("i={i}");
                    System.out.println("x = {x}");
                    //var is = reverse (for (o in offsets where x > 0) indexof o);
                    var is = reverse offsets;
                    var j = 0;
                    for (i in is) {
                        if (x > i) {
                             System.out.println("x > {i}");
                             menuSelect(5-j);
                             break;
                        }
                        j++;
                    }
                }
            },
            Rect {
                selectable: true
                cursor: Cursor.HAND
                transform: Transform.translate(560, 0)
                height: 33
                width: 60
                fill: Color.rgba(0, 0, 0, 0) as Paint
                onMouseClicked: function(e:CanvasMouseEvent) {
                    //             println("buy...");
                    if (buySelect <> null) {
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
    //trigger on TeslaPage.menuImageUrl = value {
    //println("menu image url = {value}");
    //}
    
    /*
trigger on TeslaPage.visible = value {
    if (value) {
        opacityValue = [0, 100] dur 1000 motion EASEBOTH while visible == value;
    }
}
*/
    
}

Canvas {
    content: TeslaPage{}
}
