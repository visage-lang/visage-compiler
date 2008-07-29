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


class HomeSectionView extends CustomNode {
    attribute imageUrl: String;
    attribute text: String;
    
    function create():Node {
        return Group {
            content:
            [Rectangle {
                transform: Transform.translate(0, 20)
                height: 56
                width: 165
                stroke: Color.color(.5, 0, 0, 1)
            },
            ImageView {
                transform: Transform.translate(10, 0)
                cursor: Cursor.HAND
                image: Image {url: bind imageUrl }
            },    
            ComponentView {
                transform: Transform.translate(0, 85)
                component: Label {
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
                    foreground: Color.WHITE
                    text: bind text
                }
            }]
        }
        ;
    }
}

class HomePage extends TeslaPage {
        override attribute menuImageUrl = "http://www.teslamotors.com/images/nav/nav_home.gif";
        override attribute content = Group {
            content:
            [Group {
                var red = Color.color(0.7, 0, 0, 1);
                transform: Transform.translate(0, 20)
                content:
                [Line {endX: 750 stroke: Color.RED, strokeWidth: 0.5},
                Line {startY: 2 endY: 2, endX: 750, stroke: Color.RED, strokeWidth: 0.5}]
            },
            ImageView {
                transform: Transform.translate(2, 40)
                image: Image {
                    url: "http://www.teslamotors.com/images/nav/home_r1_c1.jpg"
                }
            },
            ImageView {
                transform: Transform.translate(540, 300)
                cursor: Cursor.HAND
                image: Image {
                    url: "http://www.teslamotors.com/images/nav/button_seemore.gif"
                }
            },
            Group {
                var gray = Color.color(0.7, 0.7, 0.7, 1);
                transform: Transform.translate(0, 336)
                content:
                [Line {endX: 750 stroke: Color.GRAY, strokeWidth: 0.5},
                Line {startY: 2 endY: 2, endX: 750, stroke: Color.GRAY, strokeWidth: 0.5}]
            },
            ImageView {
                transform: Transform.translate(120, 360)
                image: Image{url: "http://www.teslamotors.com/images/nav/home_content_bg.gif"}
            },
            Group {
                transform: Transform.translate(15, 360)
                content:
                [HomeSectionView {
                    transform: Transform.translate(185*0, 0)
                    imageUrl: "{__DIR__}Image/downloads.gif"
                    text: "<html><div width='165' style='color:#cccccc'>Until you get a Tesla Roadster of your own, why not download one
                        of our four free wallpaper images to use as your computer desktop?<p>We also have a nifty
                        set of buttons and banners you can download to put in a blog or on your MySpace page to help
                        us get the word out. <a style='color:white;font-weight:bold;' href=''>downloads</a><img src='http://www.teslamotors.com/images/nav/inlinearrow.gif'></img></div></html>"
                },
                HomeSectionView {
                    transform: Transform.translate(185*1, 0)
                    imageUrl: "{__DIR__}Image/read_press.gif"
                    text: "<html><head><style> a \{color:white;font-weight:bold;text-decoration:none;} a.hover \{text-decoration:underline;}</style></head></body><div width='165' style='color:#cccccc'>						<div class=\"padder\">

                                                            <p>  		Tesla Motors is making headlines in places like: 	</p>  	<ul style=\"margin-bottom: 12px; margin-left: 15px; list-style-type: disc; list-style-image: none; list-style-position: outside\">  		<li><a href=\"http://www.innovateforum.com/innovate/article/articleDetail.jsp?id=368093\" target=\"_new\">Innovate Forum - Innovative Auto Packs a Dual Punch</a></li><li><a href=\"http://www.pasadenastarnews.com/opinions/ci_4233760\" target=\"_new\">Pasadena Star News - Carmaker pulls plug as another charges ahead</a>       </li><li><a href=\"http://www.forbes.com/2006/08/22/unsolicited-advice-advertising-meb_0823tesla.html\" target=\"_new\">Forbes - If pigs had wings</a>       </li>  		<li>        <a href=\"http://environment.guardian.co.uk/travel/story/0,,1855609,00.html\" target=\"_new\">Guardian - Batteries included</a>       </li>  		<li>       <a href=\"http://news.com.com/Automaker+aims+to+bring+clean+cars+to+the+masses/2100-11389_3-6107369.html\" target=\"_new\">CNET - Automaker aims to bring clean cars to the masses</a>       </li>  		  		  	</ul>  	<p style=\"text-align: right\"> 		<a href=\"../../media/media_coverage.php?PHPSESSID=486e9c7df4d4a8f1509456dfe1582420\">more headlines <img border='0' style=\"border: 0px none ; width: 10px; height: 8px\" src=\"http://www.teslamotors.com/images/nav/inlinearrow.gif\" alt=\"\" /></a>  	</p>

                                                    </div></div></body></html>"
                },
                HomeSectionView {
                    transform: Transform.translate(185*2, 0)
                    imageUrl: "{__DIR__}Image/faq.gif"
                    text: "<html><head><style>a \{color:white;font-weight:bold;text-decoration:none;} a.hover \{text-decoration:underline;}</style></head><body><div width='175' style='color:#cccccc'><p>  	 		How long does it take to charge? How fast is it? And most importantly, 		how do I buy one? 	</p> 	 	<p> 		These questions and more answered in our 		<a href=\"../../learn_more/faqs.php?PHPSESSID=486e9c7df4d4a8f1509456dfe1582420\">Frequently Asked Questions.&nbsp;<img border='0' style=\"border: 0px none ; width: 10px; height: 8px\" src=\"http://www.teslamotors.com/images/nav/inlinearrow.gif\" alt=\"\" /></a>  	</p></div></body></html>"
                },
                HomeSectionView {
                    transform: Transform.translate(185*3, 0)
                    imageUrl: "{__DIR__}Image/know_us.gif"
                    text: "<html><head><style>a \{color:white;font-weight:bold;text-decoration:none;} a.hover \{text-decoration:underline;}</style></head><body><div width='175' style='color:#cccccc'><p>Our CTO is speaking about High-Performance Electric Vehicles in September. <a href=\"../../learn_more/events.php\">read more <img border='0' style=\"border: 0px none ; width: 10px; height: 8px\" src=\"http://www.teslamotors.com/images/nav/inlinearrow.gif\" alt=\"\" /></a></p><p>Come see us at the LA Auto Show in December. <a href=\"../../learn_more/events.php\">read more <img border='0' style=\"border: 0px none ; width: 10px; height: 8px\" src=\"http://www.teslamotors.com/images/nav/inlinearrow.gif\" alt=\"\" /></a></p><p>How exactly does someone get the idea to build a high-performance electric car 		and, more importantly, convince others that he&lsquo;s onto something? 		<a href=\"../../about/company.php?PHPSESSID=486e9c7df4d4a8f1509456dfe1582420\">read more <img border='0' style=\"border: 0px none ; width: 10px; height: 8px\" src=\"http://www.teslamotors.com/images/nav/inlinearrow.gif\" alt=\"\" /></a> 	</p> 	 	<p> 		Check out the Tesla Motors blog. We had some fun at the Concours d&rsquo;Elegance in Pebble Beach. 		<a href=\"../../blog1/?PHPSESSID=486e9c7df4d4a8f1509456dfe1582420\">read more <img border='0' style=\"border: 0px none ; width: 10px; height: 8px\" src=\"http://teslamotors.com/images/nav/inlinearrow.gif\" alt=\"\" /></a>  	</p></div></body></html>"
                }]
            }]

        };

}
