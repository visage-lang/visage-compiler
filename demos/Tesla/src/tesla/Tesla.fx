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
import javafx.scene.image.*;

import java.lang.System;

class Tesla extends CustomNode {
    
    attribute selectedPage: TeslaPage;
    
    function menuSelect(i:Integer):Void  {
        if (i >= 0 and i < sizeof pages) {
            selectedPage = pages[i];
        }
        System.out.println("menu select {i} selectedPage = {selectedPage} of {sizeof pages}");
    }
    
    attribute contentGroup: Group;
    
    attribute pages: TeslaPage[] = 
    [{ var p:HomePage;
        p = HomePage {
            visible: bind selectedPage == p or selectedPage == null
            menuSelect: function(i:Integer):Void {this.menuSelect(i);}
       }
    },
    { var p:PerformancePage;
       p = PerformancePage {
            visible: bind selectedPage == p 
            menuSelect: function(i:Integer):Void {this.menuSelect(i);}
        }
    },
    {
        var p:EngineeringPage;
        p = EngineeringPage {
            visible: bind selectedPage == p 
            menuSelect: function(i:Integer):Void {this.menuSelect(i);}
        }
    },
    {
        var p:StylingPage;
        p = StylingPage {
            visible: bind selectedPage == p 
            menuSelect: function(i:Integer):Void {this.menuSelect(i);}
        }
    }
    ];
    
    
    function create():Node {
        return Group {
            var gray = Color.color(0.4, 0.4, 0.4, 1)
            content: 
            [ImageView {
                image: Image {
                    url: "http://www.teslamotors.com/images/nav/header_logoandnav.gif"
                }
            },
            Rectangle {
                x: 30
                y: 116
                height: 500
                width: 940
                arcHeight: 20
                arcWidth: 20
                fill: Color.GRAY
            },
            Rectangle {
                x: 40
                y: 92 + 33-1
                height: bind contentGroup.getHeight() + 10
                width: 750
                arcHeight: 20
                arcWidth: 20
                fill: Color.BLACK
                stroke: Color.GRAY
            },
            contentGroup = Group {
                transform: Transform.translate(0, 92)
//                attribute: contentGroup
                content: bind pages
            }]
            
        };
    }
}
        
Frame {
    //centerOnScreen: true
    visible: true
    height: 800
    width: 1000
    title: "Tesla"
    //onClose: function() {System.exit(0);}
    content: ScrollPane {
        //background: Color.BLACK
        view: Canvas {
            background: Color.BLACK
            //cursor: Cursor.DEFAULT
            content: Tesla{}
        }
    }
}
