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
import javafx.scene.text.*;


class HowItWorks extends CustomNode {
    
    override function create():Node {
        return Group {
            content:
            [ImageView {
                // 165x210
                transform: Transform.translate(10, 10)
                image: Image {url: "http://teslamotors.com/images/content/airintake.jpg"}
            },
            ImageView {
                // 165x319
                transform: Transform.translate(10, 185)
                image: Image {url: "http://teslamotors.com/images/content/under-the-skin-thumbnail-co.gif"}
            },
            ComponentView {
                transform: Transform.translate(165+25,10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
                    text:
                    "<html><div width='500'>
<h1>
	How It Works
</h1>

<p>
	The Tesla Roadster powertrain consists of four main parts: the battery pack (which we call the Energy 
	Storage System or ESS),
	motor, transmission, and the PEM (Power Electronics Module), none of which are
	&ldquo;off-the-shelf&rdquo; components. Rather, each includes innovations, both small
	and large, to support our mission of a high-performance car that&rsquo;s gentle on
	the environment. Together, these four components form one of the most instrumented cars ever
	made.
</p>

<h3>
	Energy Storage System (ESS)
</h3>

<p>
	When you set out to build a high-performance electric car, the biggest challenge is
	obvious from the start: the battery. Its complexities are clear: it&rsquo;s heavy,
	expensive, and offers limited power and range. Yet it has one quality that eclipses
	these disadvantages, and motivates you to keep working tirelessly: it&rsquo;s clean.
</p>

<p>
	The Tesla Roadster&rsquo;s &ldquo;fuel tank&rdquo; weighs in at about 1,000 pounds and delivers
	four to five times the energy-density stores of other batteries. Safe, light,
	durable, and recyclable, it represents the biggest innovation in the Tesla Roadster
	and is one of the largest and most advanced lithium-ion battery packs in the world.
	Why this matters to you, the driver, is that it frees you to drive farther than ever
	before in an EV, while enjoying the power of world-class performance. The design
	ensures optimum operating conditions which maximize the life of the cells, while
	offering high levels of safety.

</p>

<p>
	The architecture provides excellent redundancy and tolerance against cell-to-cell
	manufacturing variations. The system uses commodity lithium ion cells which, thanks
	to high demand by the consumer electronics industry, has spurred development that
	drives costs down and performance up. Finally, recharge time is impressively quick,
	enabled by an onboard, high-power charging system.
</p>

<p>
	The system addresses thermal balancing with a liquid cooling circuit. Multiple
	passive and active safety devices ensure safe operation over the wide range of
	driving environments and scenarios. An array of sensors and a dozen microcontrollers
	communicate with the vehicle to allow efficient use and management of the battery
	pack. Finally, the entire assembly is housed in a rugged enclosure, which protects
	the system from the harsh road environment while supporting the internal components.
</p>

<p>
	The ESS changes
	the very personality of electric cars &mdash; making the Tesla Roadster the first car
	that delivers the power of a sports car, the driving range of a gasoline-powered car,
	and the greenness of a bicycle.

</p>

<h3>
	Motor
</h3>

<p>
	The motor for a high-performance electric car requires a device that is
	simultaneously light, compact, and high in efficiency. The Tesla Roadster EV motor is just
	that. We accomplish this by starting with a well-optimized electromagnetic design and
	then using the lowest loss conductors and the highest quality magnetic steel
	possible.
</p>

<p>
	The power of the motor is not only limited by how much power you put into it, but
	also by how fast it can be cooled, how hot it can operate, and how efficiently it
	runs. We addressed each of these in innovative ways. Our motor can operate
	continuously around 120&deg;C, thanks to the array of air-cooling fins on our aluminum
	housing.
</p>

<p>
	Without proper efficiency, our motor would convert electrical energy into heat
	instead of rotational energy. That&rsquo;s why we constructed it with specially
	designed, high-quality lamination steel that has very low eddy current losses,
	particularly at high rpm. The rotor is made with a proprietary process that produces
	a low resistance &ldquo;squirrel cage&rdquo; with large end rings using oxygen free
	copper. This allows the rotor to develop high current flows, and torque, with low
	resistance losses. The use of a small air gap allows tight inductive coupling which,
	combined with low loss magnetic materials, enables the development of high torque at
	high rpm. Together, these factors allow us to induce large currents, even at high
	rpm, producing much flatter power and efficiency curves from approximately 2,000 rpm
	to 12,000 rpm.
</p>

<p>
	The sum of all these features is a single motor with efficiencies of 85 to 95
	percent, power output of up to 185 kW, and a small footprint that measures just 250
	mm (diameter) by 350 mm (length).
</p>

<h3>
	Transmission

</h3>

<p>
	Inside the box, our transmission couples the simplicity and efficiency of a manual
	with the smarts and sophistication of an automatic. The Tesla Roadster has only two
	forward gears and either one will work for most of your driving. Unlike a manual
	transmission, the car will not stall if you have it in the wrong gear. Plus it puts
	you in control of the shifting to fine-tune your driving experience or to achieve the
	upper limits of acceleration and top speed. And because there is no clutch, you can
	quickly and easily start from a stop or shift gears on the freeway.
</p>

<h3>
	Power Electronics Module (PEM)
</h3>

<p>
	You&rsquo;ll see this very important part of the car every time you pop the trunk. It
	controls over 200 kW of electrical power during peak acceleration, enough power to
	illuminate 2,000 incandescent lightbulbs (or 10,000 compact fluorescents). The PEM
	performs several critical functions in the Tesla Roadster, including motor torque
	control, regenerative braking control, and charging.
</p>

<p>
	When you shift gears or accelerate in the Tesla Roadster, the PEM translates your
	commands into precisely timed voltages that tell the propulsion motor to respond with
	the proper speed and direction of rotation.
</p>

<p>
	Circuits within the PEM monitor temperatures, voltages, and currents for maximum or
	minimum limits by using a combination of hardware- and firmware-settable values.
	These circuits prevent damage to the PEM, ESS, or motor when a variance from nominal
	operating conditions is detected.
</p>

<p>
	During normal operation, the PEM monitors things like the voltage delivered by the
	ESS, the speed of rotation of the propulsion motor, and temperatures of the motor and
	power electronics. Should you like to know these things yourself, simply glance at
	the on-board Vehicle Display System.
</p>


	</td>		<td style='width: 7px;'><img src='http://www.teslamotors.com/images/nav/spacer.gif' width=1 height=1 /></td>
	</tr></table>
</div>		<div style='margin-top: 30px;'></div> 		</td>
	</tr>
	<tr>
		<td style=\"background: url('http://www.teslamotors.com/images/nav/article_r4_c1.jpg') repeat-y; height: 100%; width: 42px;\"></td>
		<td style=\"background: url('http://www.teslamotors.com/images/nav/article_r4_c5.jpg') repeat-y; height: 100%; width: 212px;\"></td>
	</tr>

	<tr>
		<td colspan=\"3\" style=\"background: #666; width: 100%; height: 1px;\"><img src=\"http://www.teslamotors.com/images/nav/spacer.gif\" width=\"1\" height=\"1\" alt=\"\" /></td>
		<td rowspan=\"2\" style=\"background: #000;\"><img src=\"http://www.teslamotors.com/images/nav/spacer.gif\" width=\"182\" height=\"1\" alt=\"\" /></td>
	</tr>
</table>

<div class='footer'>
	
<div id='nav'>

	<a href='/about/terms.php'>terms of use</a>

		
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;

	<a href='/about/privacy.php'>privacy policy</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;

	<a href='/about/employment.php'>employment</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;

	<a href='/about/company.php'>company</a>

	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;

	<a href='/customer_care/contact_us.php'>contact us</a>

	<br />
	
	<a href='/'>home</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;
	
	<a href='/performance/performance.php'>performance</a>

	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;
	
	<a href='/engineering/how_it_works.php'>engineering</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;
	
	<a href='/styling/body.php'>styling</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;
	
	<a href='/learn_more/foreign_oil.php'>more</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;

	
	<a href='/blog1/'>blog</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;
	
	<a href='/customer_care/contact_us.php'>customer care</a>
	
	&nbsp;&nbsp;&bull;&nbsp;&nbsp;
	
	<a href='/media/media_coverage.php'>media</a>
	
	<br /><br />
	
	 
&copy; 2006 Tesla Motors, Inc. All rights reserved. Tesla Motors is a registered trademark. (version 1-1-0061)		

</div></html>"
                }
            }]
        };
    }
}

Canvas {
    content: HowItWorks{}
}
