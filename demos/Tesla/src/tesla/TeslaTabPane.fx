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
import javafx.scene.layout.*;
import javafx.input.*;
import javafx.animation.*;
import java.lang.System;

class TeslaTabHolder extends CustomNode {
    var tabPane: TeslaTabPane;
    var tab: TeslaTab;
    var selected: Boolean = bind tab.selected with inverse on replace {
        System.out.println("tab holder selected { selected }");
        if (selected) {
            tabPane.opacityValue = 0.0;
            tabPane.selectedTab = tab;
        }
    }
    
    override function create():Node {return Group {content: bind tab}};
}

public class TeslaTabPane extends CustomNode {
    var self = this;
    public var width: Number;
    public var tabs: TeslaTab[];
    var fadein:Timeline = Timeline {
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
    var selectedTab: TeslaTab on replace oldValue = newValue {
        if(oldValue != null) {
            oldValue.selected = false;
        }
        fadein.start();
    }
    
    var holderGroup: Node;
    var selectedContent: Node[] = bind selectedTab.content;
    var opacityValue: Number = 1.0;
    var lineStroke: Paint  = Color.GRAY;
    var drawBorder: Boolean = true;;
    
    
    override function create():Node {
        return Group {
            content: 
            [Line {
                visible: bind drawBorder
                startX: 0, startY: 33, endX: bind width, endY: 33, stroke: bind lineStroke
            },
            Group {
                transform: bind Transform.translate((width-holderGroup.getWidth())/2, 0)
                content: holderGroup = HBox {
                      content: bind for (t in tabs) 
                      TeslaTabHolder {
                          tabPane: this
                          tab: t
                      }
                }
            },
            Group {
                var red = Color.color(0.7, 0, 0, 1);
                visible: bind drawBorder
                transform: Transform.translate(0, 44)
                content:
                [Line {endX: bind width, stroke: Color.RED, strokeWidth: 0.5},
                Line {startY: 2 endY: 2, endX: bind width, stroke: Color.RED, strokeWidth: 0.5}]
            },
            Group {
                transform: Transform.translate(0, 70 - (if (drawBorder) then 0 else 44))
                opacity: bind opacityValue
                content: bind selectedContent
            }]
        };
    }
}
