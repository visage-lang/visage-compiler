/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;


class PerformancePage extends TeslaPage {
    override attribute menuImageUrl = 
    "http://www.teslamotors.com/images/nav/nav_performance.gif";
    override attribute content =
   [ Group {
        content:
        [TeslaTabPane {
            transform: Transform.translate(0, 10)
            width: 750
            tabs:
            [TeslaTab {
                title: "efficiency & performance"
                content: EfficiencyAndPerformance{}
                selected: true
            },
            TeslaTab {
                title: "electric power"
                content: ElectricPower{}
            },
            TeslaTab {
                title: "charging and batteries"
                content: ChargingAndBatteries{}
            },
            TeslaTab {
                title: "performance specs"
                content: PerformanceSpecs{}
            }]
        }]
    } as Node];
}
