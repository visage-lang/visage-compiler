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


class StylingPage extends TeslaPage {
    
    override attribute menuImageUrl = 
    "http://teslamotors.com/images/nav/nav_styling.gif";
       
    override attribute content =
    [Group {
        content:
        [TeslaTabPane {
            transform: Transform.translate(0, 10)
            width: 750
            tabs:
            [TeslaTab {
                title: "photo gallery"
                selected: true
                content: TeslaTabPane {
                    width: 750
                    drawBorder: false
                    tabs:
                    [TeslaTab2 {
                        title: "body"
                        content: BodyStyling {}    
                        selected: true
                    },
                    TeslaTab2 {
                        title: "cockpit"
                        content: CockpitStyling {}     
                    }]
                }
            },
            TeslaTab {
                title: "the process in pictures"
                content: ProcessInPictures {}
            },
            TeslaTab {
                title: "available colors"
                content: ColorSelector {}
            },
            TeslaTab {
                title: "wallpaper"
            }]
        }]
    } as Node];
    
    /*
trigger on new StylingPage {
        menuImageUrl = 
        "http://teslamotors.com/images/nav/nav_styling.gif";
}*/
}
