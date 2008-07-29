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

class ChargingAndBatteries extends CustomNode {
    
    function create():Node {
        return Group {
            content:
            [ImageView {
                // 165x319
                transform: Transform.translate(10, 10)
                image: Image {url: "http://teslamotors.com/images/content/charging.jpg"}
            },
            ComponentView {
                transform: Transform.translate(165+25,10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font{name:"Arial", size:12}
                    text:
                    "<html><div width='500'><h1>Charging & Batteries
</h1>

<h3 style='margin-top: 10px;'>250 Miles Between Charges
</h3>

<p>
Before now, electric vehicles typically capped off around 60 miles per charge, 
relegating them to the status of commuter cars. 
The Tesla Roadster changes all that. Plug it in at night when you 
pull into the garage, and you can drive about 250 miles on that charge the next day. 
</p>
	
<p>
Just like the fuel gauge in your existing car, the instruments inside 
the Tesla Roadster indicate how many more miles can be driven before you 
need to think about recharging. So unlike the old days of electric vehicles, 
now you can &ldquo;drive electric&rdquo; without the anxiety you&rsquo;ll run 
out of charge and be left stranded. 

</p>

<h3>How Long Does Your Cell Phone Take to Charge?
</h3>
	
<p>
If you're like most people, you can&rsquo;t answer this question. 
You plug in your phone every night, and it&rsquo;s fully charged in the morning. 
You never think about how long it took so long as the charge lasts 
throughout the next day. Once you get into the habit, that&rsquo;s the way 
you&rsquo;ll feel about your Tesla Roadster; every morning you&rsquo;ll wake up 
to a fully charged car, ready for whatever is on the day&rsquo;s agenda. 
</p>
	
<h3>Charge At Home or On the Road
</h3>

	
<p>
Plug your Tesla Roadster into the at-home charging unit, and you&rsquo;ll 
be fully charged in under four hours. But we consider this a &ldquo;worst case&rdquo; 
for someone starting with a completely dead battery. 
Even after a 100-mile trip, you can be completely charged in under two hours.* 
And should you need to charge on the road, packed away in the trunk is an 
optional mobile-charging kit that lets you charge from standard electrical 
outlets while away from home. 
</p>


<h3>A Bonus: Regenerative Braking
</h3>

<p>
Regenerative braking &mdash; which recovers and stores the energy usually 
lost when you slow down &mdash; extends your charge even further, 
delivering higher miles-per-charge on in-town driving. 
Think of it like engine braking with a bonus. Whenever you slow 
down, you send a charge back into the battery. It&rsquo;s a much-needed silver 
lining to red lights, traffic jams, and other slowdowns. 

</p>

	
<h3>A Better Battery
</h3>

<p>
Unlike the lead-acid or nickel metal hydride batteries in most 
electric cars &mdash; which are heavy and difficult to dispose of &mdash; we 
use a proprietary Lithium ion battery pack. Nickel cadmium batteries were notorious 
for memory problems, and nickel metal hydride batteries reduced this problem. 
The lithium ion batteries we use in the Tesla Roadster eliminate the problem. 
So there is no need to worry about waiting until the battery pack is fully 
discharged before recharging it: feel free to charge your car whenever 
you have access to power. 
</p>
	
	
<p>
Even with the demands of charging and discharging the battery pack on a 
daily basis, the batteries in the Tesla Roadster will give you more than 
100,000 miles of peak performance driving. After that point, the battery will 
see only gradual drops in performance over time. 
</p>



<h3>Battery Recycling
</h3>

<p>
Unlike other batteries that came before them, Lithium ion batteries are 
classified by the federal government as non-hazardous waste and are safe for 
disposal in the normal municipal waste stream. However, dumping these batteries 
in the trash would be throwing money away. Even a completely dead battery 
pack contains valuable, recoverable materials that can be sold back to 
recycling companies for cash.
</p>

<p>
But reuse is such a key part of our philosophy, we&rsquo;re doing our best to 
arrange to have our car batteries safely recycled &mdash; even before we&rsquo;ve 
sold our first car. Our goal is to include the cost of recycling 
in the purchase price of each car.
</p>


<h3>Electrical Cost to Charge the Batteries
</h3>

<p>
With your electrical company&rsquo;s incentive pricing factored in, 
it will cost you roughly 1 cent per mile to drive the Tesla Roadster.** 
But the incentives don&rsquo;t stop there. Depending on where you live, 
other bonuses may include: 
</p>

<ul>
<li>Single-occupancy access to all carpool lanes </li>
<li>Income tax credit (awaiting new legislation) </li>
<li>A luxury car that's fully exempt from the luxury car tax </li>

<li>Free parking at charging stations at LAX </li>
<li>No parking meter fees in an increasing number of major metropolitan areas</li>
</ul>


<p>
The information on this page should not be viewed as an official or legally 
binding document. For more information about electric vehicle incentives, 
visit <a href=\"http://www.fueleconomy.gov\" target=\"_blank\">www.fueleconomy.gov</a> 
or consult an IRS tax representative. 
</p>

<h3>Safety
</h3>

<p>
Your personal safety is a priority for us. 
From the battery technologies we&rsquo;ve chosen, to the unique design of 
our battery pack, our testing shows just how safe the Tesla Roadster is, 
even in extreme worst-case scenarios. Learn more <a href=\"/engineering/safety.php\">here</a>. 
</p>

<br><br>
<p>
How far can this ground-breaking
battery take you on a single charge? 
Significantly farther than any other EV (electric vehicle) to date.
</p>

<div>
</div>
<p>
	This map gives an indication of the expected range. Your mileage may vary.
</p>

<p style='font-size:80%;'>

	* With an EVSE system installed operating at 70 amps.
</p>

<p style='font-size:80%;'>
	** Since most car owners recharge at night, this calculation uses off-peak charging
	rates with a time-of-use meter. To calculate your exact cost-per-mile, contact
	your local electricity provider. 
</p>

	</td>		<td style='width: 7px;'><img src='/images/nav/spacer.gif' width=1 height=1 /></td>
	</tr></table>
</div>		<div style='margin-top: 30px;'></div> 		</td>

	</tr>
	<tr>
		<td style=\"background: url('/images/nav/article_r4_c1.jpg') repeat-y; height: 100%; width: 42px;\"></td>
		<td style=\"background: url('/images/nav/article_r4_c5.jpg') repeat-y; height: 100%; width: 212px;\"></td>
	</tr>
	<tr>
		<td colspan=\"3\" style=\"background: #666; width: 100%; height: 1px;\"><img src=\"/images/nav/spacer.gif\" width=\"1\" height=\"1\" alt=\"\" /></td>
		<td rowspan=\"2\" style=\"background: #000;\"><img src=\"/images/nav/spacer.gif\" width=\"182\" height=\"1\" alt=\"\" /></td>
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

Canvas{
content:
ChargingAndBatteries{}
}
