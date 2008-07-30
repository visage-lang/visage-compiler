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
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.ext.swing.*;
import javafx.scene.text.*;


class EfficiencyAndPerformance extends CustomNode {
    attribute textContent: Node;
    
    override function create():Node {
        Group {
            content:
            [ImageView {
                // 165x319
                transform: Transform.translate(10, 10)
                image: Image {url: "http://teslamotors.com/images/content/fullfromabove.jpg"}
            },
            textContent = ComponentView {
//                attribute: textContent
                transform: Transform.translate(165+25,10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
                    text:
                    "<html><div width='400px'><h1>Efficiency & Performance
</h1>

<h3 style='margin-top: 10px;'>Efficiency. Performance. Pick Two. 
</h3>

<p>
	Gone are the days when sports cars demanded a choice between efficiency
	and performance. Now it&rsquo;s no longer a zero-sum game. Instead, you get peak
	performance at every moment, with little mileage penalty.  
</p>

<h3>Instant Freedom
</h3>

<p>
	The first time you drive the Tesla Roadster, prepare to be surprised. You&rsquo;re
	at freeway speed in seconds without even thinking about it. There&rsquo;s no clutch
	to contend with and no race-car driving techniques to perform. Just the touch of
	your foot and you&rsquo;re off, without any of the sluggishness of an automatic.  
</p>

<p>
	How powerful is the acceleration? A quick story to illustrate. A favorite trick here
	at Tesla Motors is to invite a passenger along and ask him to turn on the radio. At the
	precise moment we ask, we accelerate. Our passenger simply can&rsquo;t sit forward
	enough to reach the dials. But who needs music when you&rsquo;re experiencing such
	a symphony of motion. 
</p>

<p>
	Rest assured that this responsiveness works at all speeds, as noticeable when
	you&rsquo;re inching your way through parking lots as when flying along freeways. 
</p>

<h3>100% Torque, 100% of the Time
</h3>

<p>
	The Tesla Roadster delivers full availability of performance every moment you are in the car,
	even while at a stoplight. Its peak torque begins at zero rpm and stays powerful
	beyond 13,000 rpm. 
</p>

<p>
	This is the precise opposite of what you experience with a gasoline engine,
	which has very little torque at a low rpm and only reaches peak torque in a narrow
	rpm range. This forces you to make frequent gear changes to maintain optimal torque.
	With the Tesla Roadster, you get great acceleration and the highest energy efficiency at the same
	time. All while requiring no special driving skills to enjoy it. This makes the
	Tesla Roadster six times as efficient as the best sports cars while producing
	one-tenth of the pollution.
</p>
</div></html>"
                }
            },
            ImageView {
                transform: bind Transform.translate(textContent.getX(), textContent.getY() + textContent.getHeight() + 5)
                image: Image{url: "http://www.teslamotors.com/images/content/motor_torque_curve.gif"}
            }]
        };
}
}

Canvas{
content:EfficiencyAndPerformance{}
}
