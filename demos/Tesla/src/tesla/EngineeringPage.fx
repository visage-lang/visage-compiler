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

class EngineeringPage extends TeslaPage {

        override attribute menuImageUrl = "http://teslamotors.com/images/nav/nav_engineering.gif";
    
        override attribute content =
        [Group {
            content:
            [TeslaTabPane {
                transform: Transform.translate(0, 10)
                width: 750
                tabs:
                [TeslaTab {
                    title: "how it works"
                    selected: true
                    content: HowItWorks{}
                },
                TeslaTab {
                    title: "cockpit"
                    content: Cockpit{}
                },
                TeslaTab {
                    title: "under the skin"
                    content: UnderTheSkin{}
                },
                TeslaTab {
                    title: "safety"
                },
                TeslaTab {
                    title: "security"
                },
                TeslaTab {
                    title: "technical specs"
                }]
            }]
        } as Node];
}
