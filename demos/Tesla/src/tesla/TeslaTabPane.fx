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

class TeslaTabHolder extends CompositeNode {
    attribute tabPane: TeslaTabPane;
    attribute tab: TeslaTab;
    attribute selected: Boolean = bind tab.selected with inverse on replace (value) {
        System.out.println("tab holder selected { selected }");
        if (selected) {
            tabPane.selectedTab = tab;
        }
    }
    
    function composeNode():Node {return Group {content: bind tab}};
}

public class TeslaTabPane extends CompositeNode {
    public attribute width: Number;
    public attribute tabs: TeslaTab[];
    attribute selectedTab: TeslaTab on replace oldValue = newValue {
        oldValue.selected = false;
    //        opacityValue = [0, 1] dur 500 motion EASEBOTH while selectedTab == newValue;
    }
   /// private attribute holders: TeslaTabHolder[];
    // = 
    //bind if (sizeof tabs == 0) then null else
    //for (t in tabs) 
    //TeslaTabHolder {
    //    tabPane: this
   //     tab: bind t
        //transform: bind Transform.translate(if (this.holders[indexof t -1] == null) then 0 else
        //this.holders[indexof t -1].currentX + this.holders[indexof t -1].currentWidth + 10, 0)
    //;};    
    
    private attribute holderGroup: Node;
    attribute selectedContent: Node[] = bind selectedTab.content;;
    attribute opacityValue: Number;
    attribute lineStroke: Paint  = Color.GRAY as Paint;
    attribute drawBorder: Boolean = true;;
    
    
    function composeNode():Node {
        return Group {
            content: 
            [Line {
                visible: bind drawBorder
                x1: 0, y1: 33, x2: bind width, y2: 33, stroke: bind lineStroke
            },
            Group {
                transform: bind Transform.translate((width-holderGroup.currentWidth)/2, 0)
//                content: Group {
//                    attribute: holderGroup
//                    content: bind holders
//                }
                content: holderGroup = HBox {
                      content: bind for (t in tabs) 
                      TeslaTabHolder {
                          tabPane: this
                          tab: t
                      }
                }
            },
            Group {
                visible: bind drawBorder
                transform: Transform.translate(0, 44)
                var red = Color.rgba(0.7, 0, 0, 1)
                content:
                [Line {x2: bind width, stroke: Color.RED as Paint, strokeWidth: 0.5},
                Line {y1: 2 y2: 2, x2: bind width, stroke: Color.RED as Paint, strokeWidth: 0.5}]
            },
            Group {
                transform: Transform.translate(0, 70 - (if (drawBorder) then 0 else 44))
                opacity: bind opacityValue
                content: bind selectedContent
            }]
        };
    }
}
