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
import javafx.scene.image.*;
import javafx.scene.paint.*;


class ElectricPower extends CustomNode {
    attribute textContent: Node;
    
    function create():Node {
        return Group {
            content:
            [ImageView {
                // 165x319
                transform: Transform.translate(10, 10)
                image: Image{url: "http://teslamotors.com/images/content/rear2.jpg"}
            },
            textContent = ComponentView {
//                attribute: textContent
                transform: Transform.translate(165+25,10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
                    text:
                    "<html><div width='400px'>
<h1>Electric Power</h1>

<h3 style='margin-top: 10px;'>Drive Quickly, Tread Lightly</h3>

<p>
Most electric vehicles operate under the assumption that driving is merely a necessary evil if you need to get someplace you can?t reach on foot or bike. The result has been cars that are designed, built, and marketed in ways that refuse to glorify driving. 
</p>

<p>
We respectfully disagree. We believe driving is exhilarating. Just watch any child on a go-cart and the joy is plain to see. And when you can soar along at top speed, knowing the only oil in the car is in the transmission, the only emissions are the songs from the radio, the ride becomes more enjoyable still.
</p>

<h3>The Ultimate Multi-Fuel Vehicle</h3>

<p>
Electric cars equal freedom. Not simply from oil reliance, but from dependence on any specific power source. Electric power can be generated from natural gas, coal, solar, wind, hydro, and nuclear sources ? or a combination of all of them ? without changing the design of the car. No matter how or when the world changes, the car adapts, making it immune from obsolescence.
</p>

<p>
We foresee a day when all cars run on electric power and when people will struggle to remember a time when a love of driving came with a side order of guilt.

</p>

<h3>No More Tradeoffs</h3>

<p>
Up until now, if you wanted a car with amazing gas mileage, you?d pick something like the leading hybrid; but when you pressed down the gas pedal to zip up a freeway on-ramp, you'd likely be a little disappointed ? it takes over 10 seconds to reach 60 miles per hour. On the other hand, if you demanded the 0-to-60 times of a $300,000 supercar, you'd wind up with an embarrassing 9 miles to the gallon in the city.
</p>

<p>
The graph below shows the Tesla Roadster (upper right) in a class by itself with better acceleration than a Lamborghini Murcielago and twice the mile-per-gallon equivalent of popular hybrids. The highly efficient Tesla Roadster gets the equivalent of 135 miles per gallon with an enviable 0-to-60 time of around four seconds.
</p>
</div></html>"
                }
            },
            ImageView {
                transform: bind Transform.translate(textContent.getX(), textContent.getY() + textContent.getHeight() + 5)
                image: Image{url: "http://teslamotors.com/display_data.php?data_name=effic_v_perf"}
            }]
            
        };
    }
}

Canvas{
content:ElectricPower{}
}
