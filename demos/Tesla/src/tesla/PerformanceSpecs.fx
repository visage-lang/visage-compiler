/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;

class PerformanceSpecs extends CompositeNode {
    function composeNode():Node {
        return Group {
            content:
            [ImageView {
                // 165x319
                transform: Transform.translate(10, 10)
                image: Image {url: "http://teslamotors.com/images/content/rear8.jpg"}
            },
            View {
                transform: Transform.translate(165+25,10)
                content: Label {
                    foreground: Color.WHITE
                    font: Font.Font("Arial", ["PLAIN"], 10)
                    text:
                    "<html>
<head>
<style>
* \{ border: 0; outline: 0; -moz-outline: none; }

body \{

	font-family: Arial, Helvetica, sans-serif;
	font-size: 10px;
	color: #cccccc;
	}

/* links in bullet lists have no underline */
li a \{
	font-weight: normal;
	color: #cccccc;
}

.errMsg \{
	color: #900;
	font-weight: bold;
}

/* see http://forum.textdrive.com/viewtopic.php?id=4304 */
embed, object \{ display: block; }


p \{
	margin-bottom: 12px;
	line-height: 140%;
}

ol \{ list-style: disc; }

a, li a \{
	font-weight: bold;
	color: white;
	text-decoration: none;
}

a:hover \{
	text-decoration: underline;
}

.leftcolcontent \{
	margin-top: 10px;
	width: 165px;
}


#contact table, #contact tr, #contact td \{
	background-color: #2d3942;
}

#contact a \{
	text-decoration: underline;
}



#contactform tr,
#contactform2 tr \{
	vertical-align: top;
}

#contactform input,
#contactform textarea \{
	background: black !important; color: white !important;
}

#contactform .bdr \{
	border: solid 1px #cccccc;
	background: black !important; color: white !important;
}

#contactform2 td \{
	padding: 7px 0px;
}

#subtbl tr \{
	vertical-align: top;
}

#pressreleases tr \{
	vertical-align: top;
}

#pressreleases .prdate \{
	margin-right: 15px;
	width: 120px;
}

#pressreleases .prtitle \{
	margin-bottom: 10px;
}

/* downloads */
#banners \{
	margin-bottom: 40px;
}

#banners .linkarea \{
	margin-top: 10px;
	width: 491px;
	background-color: #2c2c2c;
	border: solid 1px #4e4e4e;
	color: #cccccc;
	overflow: hidden;
	font-size: 12px;
}

#wallpaper \{
	margin-bottom: 10px;
}

#wallpaper .button_cell a \{
	text-decoration: none;
}

#wallpaper table \{
	width: 475px;
}

#wallpaper tr \{
	vertical-align: bottom;
}

#wallpaper .img_cell \{
	width: 300px;
}

#wallpaper .button_cell \{
	padding-left: 10px;
}

#wallpaper .button_cell img \{
	margin-right: 5px;
}



/* formatting for question in FAQ popup */
#faqans \{
	display: none;
	position: absolute;
	background: #2d3941;
	color: white;
	top: 250px;
	left: 310px;
	width: 400px;
	padding: 50px 20px 20px 20px;
	border: solid 1px #999;
	background: url(/images/nav/teslashield.gif);
	background-repeat: no-repeat;
}

#faqans .quest, 
#faqans a,
#faqans a:link \{
	font-weight: bold;
	color: white;
	font-size: 120%;
}

.faqQ, .faqQ a \{
	font-weight: bold;
	color: white;
	margin-top: 25px;
}

/* -fx version is used when we are doing moofx popup window */
.faqQ-fx, .faqQ-fx a \{
	font-weight: normal;
	color: #cccccc;
}

/* do you know? captions under photos */
#dyk \{
	font-weight: bold;
	color: white;
}

/* formatting for answer in FAQ popup */
#faqans .ans \{
	margin-top: 5px;
          line-height: 140%;
}

#faqans .closerDiv \{
	position: absolute;
	top: 8px;
	left: 395px;
}

#faqans .closerRule \{
	position: absolute;
	width: 440px;
	height: 1px;
	top: 35px;
	left: 0px;
	border-bottom: solid 1px #999;
}

#faqans .closerRuleIE \{
	position: absolute;
	width: 440px;
	height: 1px;
	top: 25px;
	left: 0px;
	border-bottom: solid 1px #999;
}


/* for buy col */
.closerDiv \{
	align: center;
	text-align: center;
	
}


/* bullets under photo */

#bullet \{
	color: #999;
}

#bullet a,
#bullet a.active \{
	color: #999;
	text-decoration: none;
}

#bullet a:hover \{
	text-decoration: underline;
}

#helpnav a \{
	text-indent: -3000px;
	position: absolute;
	display: block;
}

#helpnav a:hover \{
	border: 1px #666 solid;
}

#helpnav a.active \{
	border: 1px white solid;
}

#helpnav a#helpnav-cms-toggle\{
	top: 10px;
	left: 424px;
	width: 98px;
	height: 23px;
	border: 0px;
}

#helpnav a#helpnav-cms\{
	top: 10px;
	left: 375px;
	width: 37px;
	height: 23px;
	border: 0px;
}

#helpnav a#helpnav-discuss \{
	top: 10px;
	left: 533px;
	width: 55px;
	height: 23px;
}

#helpnav a#helpnav-media \{
	top: 10px;
	left: 597px;
	width: 63px;
	height: 23px;
}

#helpnav a#helpnav-customercare \{
	top: 10px;
	left: 670px;
	width: 100px;
	height: 23px;
}

.footer \{
	padding-top: 5px;
}

/* padding around graphics, flash movies */
.gfx \{
	margin: 10px 0px 30px 0px;
}

#mainnav a \{
	position: absolute;
	display: block;
	height: 35px;
	top: 90px;
	z-index: 1000;
	text-indent: -3000px;
	}

#mainnav a#tab1 \{
	left: 180px;
	width: 53px;
	}

#mainnav a#tab2 \{
	left: 233px;
	width: 103px;
	}

#mainnav a#tab3 \{
	left: 336px;
	width: 92px;
	}

#mainnav a#tab4 \{
	left: 428px;
	width: 62px;
	}

#mainnav a#tab5 \{
	left: 490px;
	width: 54px;
	}

#mainnav a#tabBuy \{
	left: 570px;
	width: 54px;
	}


#subnav \{
	font: normal 12px Arial, Helvetica, Verdana, sans-serif;
	background: url('/images/nav/2ndnav_r1_c1.jpg') repeat-x;
	width: 100%;
}

#subnav td \{
	vertical-align: middle;
	text-align: center;
}

#subnav td a \{
	text-decoration: none;
	color: #cccccc;
	font-weight: normal;
	white-space: nowrap;
}

#subnav td a:hover \{
	color: #999;
}

#subnav td.active a \{
}

#subnav td.active .left \{
	background: url('/images/nav/2ndnav_r1_c2.gif') no-repeat;
	width: 19px;
	height: 34px;
}

#subnav td.active .right \{
	background: url('/images/nav/2ndnav_r1_c4.gif') no-repeat;
	width: 19px;
	height: 34px;
}

#subnav td.active .center \{
	background: url('/images/nav/2ndnav_tab_bg.gif') repeat-x;
	padding: 0px 0px;
}
	

#thirdnav \{
	border-bottom: 1px #990000 double;
}

#thirdnav td \{
	padding: 6px 0;
	white-space: nowrap;
}

#thirdnav a \{
	display: block;
	padding: 5px 5px;
	margin-left: 10px;
	margin-right: 10px;
	text-align: center;
	color: #cccccc;
	border: 1px #000 solid;
	text-decoration: none;
	font-weight: normal;
}

#thirdnav td.current a \{
	color: #cccccc;
	border: 1px #cccccc solid;
}

#thirdnav a:hover \{
	color: #999;
	border: solid 1px black;
}


/* see http://www.greywyvern.com/code/min-height-hack.html */
.prop \{
	height: 480px;
	float: right;
	width: 1px;
}

.propclear \{
	clear: both;
	height: 1px;
	overflow: hidden;
}

#maincontent,
#maincontent td \{
	}

#maincontent #jobs  \{
	margin-bottom: 40px;
}	

#maincontent #jobs hr \{
	border-top: solid 1px white;
	height: 1px;
	margin: 20px 0px;
	
}

#maincontent .discuss,
#maincontent td .discuss \{
	padding: 0px;
}

#maincontent #onecol \{
	padding: 15px 15px;
	vertical-align: top;
	}

#maincontent #left \{
	width: 165px;
	padding-top: 15px;
	padding-left: 15px;
	padding-right: 15px;
	vertical-align: top;
}

#maincontent #right \{
	padding-top: 15px;
	padding-left: 0px;
	vertical-align: top;
}


#maincontent #team h3 \{
	margin-top: 40px;
}

#maincontent #team .photo \{
	margin-top: 40px;
 	width: 165px;
	height: 125px;
	border: solid 1px #cccccc;
}
 
#maincontent #team .content \{
	margin-left: 15px;
}

#maincontent h1 \{
	font-size: 22px;
	font-weight: normal;
	color: white;
	margin-bottom: 5px;
}

#maincontent h3 \{ /* byline */
	font-size: 16px;
	color: white;
	font-weight: normal;
	margin-top: 20px;
}

#maincontent .quote \{
}

#maincontent #left img \{
	border: 1px #999 solid;
}

#maincontent #left p.caption \{
	padding-top: 4px;
}

#maincontent #left ul,
#maincontent ul \{
	list-style: disc;
	margin-left: 1.5em;
	margin-bottom: 15px;
}

/* controls height of buy col copy */
#buyunder .content \{
	margin: 175px 12px 0px 20px;
	width: 150px;
}


/* specs charts */

.specsFinePrint \{
	padding-top: 3px;
	font-size: 90%;
	color: #666;
	line-height: 130%;
}

.subrow \{
	height: 20px;
}

table.specs \{

	border-top: solid 1px #333;
	border-bottom: solid 1px #333;
	margin-right: 20px;
}

table.specs td, table.specs tr \{
	vertical-align: top;
	height: 50px;
	padding: 10px;
}

/* wow! for sub-rows, we can shrink height */
table.specs td table td,
table.specs td table tr \{
	height: 10px;
	padding: 0px 0px 5px 0px;
}

table.specs tr.r0 td \{
	background-color: black;
	vertical-align: top;
}

table.specs tr.r1 td \{
	background-color: #2d3942;
	vertical-align: top;
}


/* buy col */
/* why two cursor declrs? see http://www.quirksmode.org/css/cursor.html */

.buycoltbl td  \{
	padding-bottom: 10px;
}

.buycoltbl td \{
	vertical-align: bottom;
}

.buycolcontactme \{
	width: 78px;
	height: 19px;
	cursor: pointer;
	cursor: hand;
}


/* gray buy layer */

#buycover \{
	background: #656565;
	position: absolute;
	top: 87px;
	left: 0;
	display: none;
	width: 100%;
	z-index: 5000;
	color: white;
}

#buycover #buytabsplash	\{
	padding-top: 11px;
}

#buycover #buycontent td \{
	padding-right: 20px;
}

#buycover .closebtn \{
	margin-left: 130px;
}

.cont2 \{
	margin-top: 60px;
	padding-left: 16px;
	padding-right: 10px;
}

#buycloser \{
	position: absolute;
	width: 99%;
	height: 99%;
	top: 0;
	bottom: 0;
	z-index: 51;
}

#buycontent \{
	text-align: left;
}

.homesection \{
	width: 25%;
	vertical-align: top;
}

.homesection .padder \{
	margin: 5px 9px;
}

.homesection .padder a.iconheader,
.homesection .padder span.iconheader \{
	display: block;
	margin-top: 30px;
	margin-bottom: 10px;
	border: 1px #600 solid;
	padding: 0;
	text-align: center;
	height: 55px;
	background: black;
}

.homesection .padder a.iconheader img,
.homesection .padder span.iconheader img
 \{
	position: relative;
	top: -21px;
	background: black;
}

.padder li \{
	margin-bottom: 8px;
}
.homesection .padder .section \{
	margin-top: 30px;
	border: 1px #990000 solid;
	padding: 10px;
	text-align: center;
}


#nav \{
	margin-left: 44px;
	margin-top: 3px;
	color: #666;
	padding-bottom: 30px;
	font-weight: normal;
}

#nav p \{
	color: #666;
}

#nav a \{
	color: #666;
	text-decoration: none;
	font-weight: normal;
}

#nav a:hover \{
	color: #666;
	text-decoration: underline;
}


/* confirmation popup for buy layer */

#bademail,  #good,
#bademail1, #good1  \{
	display: none;
	padding: 20px;
	color: white;
	font-weight: normal;
	position: absolute;
	background: #2d3941;
	top: 290px;
	width: 110px;
	border: solid 1px #999;
	align: right;
}

#loading, #loading1 \{
	display: none;
	padding: 10px 0px;
	margin: 0px;
}

</style>
</head>
<body>
<div id='maincontent' width='420px'>
<h1>Performance Specs
</h1>

<p>
	The Tesla Roadster&rsquo;s specs illustrate what it does (0 to 60 in about 4 seconds) &mdash;
	as well as what it doesn&rsquo;t (zero emissions, zero motor oil). With one moving part in the
	motor, no clutch, and two gears, it&rsquo;s not only a joy to drive, but to own as well.
	There is no motor oil to change; no filters, belts, or spark plugs to replace; no
	oxygen sensors to mistrust before an emissions test &mdash; in fact, no emissions
	test required ever. Other than inspection, the only service we recommend for the
	first 100,000 miles is brake and tire service.
</p>

<h3>Tesla Roadster Specifications*
</h3>

<div style='height: 10px; width: 10px;'></div>
<table cellpadding=0 cellspacing=0 border=0 class='specs'>
	<tr class='r0'>
		<td id='left'>
			Style
		</td>
		<td id='right'>
			2-seat, open-top, rear-drive roadster
		</td>

	</tr>
	<tr class='r1'>
		<td id='left'>
			Drivetrain
		</td>
		<td id='right'>
			Electric motor with 2-speed electric-shift manual
			transmission with integral differential
		</td>
	</tr>
	<tr class='r0'>

		<td id='left'>
			Motor
		</td>
		<td id='right'>
			3-phase, 4-pole electric motor, 248hp peak (185kW),
			redline 13,500 rpm, regenerative \"engine braking\"
		</td>
	</tr>
	<tr class='r1'>
		<td id='left'>
			Chassis
		</td>

		<td id='right'>
			Bonded extruded aluminum with 4-wheel wishbone suspension
		</td>
	</tr>
	<tr class='r0'>
		<td id='left'>
			Brakes
		</td>
		<td id='right'>
			4-wheel disc brakes with ABS
		</td>

	</tr>
	<tr class='r1'>
		<td id='left'>
			Acceleration
		</td>
		<td id='right'>
			0 to 60 in about 4 seconds
		</td>
	</tr>
	<tr class='r0'>

		<td id='left'>
			Top Speed
		</td>
		<td id='right'>
			Over 130 mph
		</td>
	</tr>
	<tr class='r1'>
		<td id='left'>
			Range
		</td>

		<td id='right'>
			250 miles EPA highway
		</td>
	</tr>
	<tr class='r0'>
		<td id='left'>
			Battery Life
		</td>
		<td id='right'>
			Useful battery life in excess of 100,000 miles
		</td>

	</tr>
	<tr class='r1'>
		<td id='left'>
			Energy Storage System
		</td>
		<td id='right'>
			Custom microprocessor-controlled lithium-ion battery pack
		</td>
	</tr>
	<tr class='r0'>

		<td id='left'>
			Full Charge
		</td>
		<td id='right'>
			As short as 3.5 hours
		</td>
	</tr>
</table>

<p class='specsFinePrint' style='width: 370px;'>
	* We are currently in the midst of important and time-consuming safety and
	durability testing for the Tesla Roadster. While we are confident of our
	numbers, this testing may require design changes that affect the final
	specifications.

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

</div></body></html>"
                }
            }]
        };
}
}

Canvas {
content: PerformanceSpecs{}
}
