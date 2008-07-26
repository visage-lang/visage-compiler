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
import javafx.input.MouseEvent;
import javafx.scene.text.Text;

class CockpitDetailView extends CustomNode {
    attribute imageUrl: String;
    attribute text: String;
    
    function create():Node {
        return Group {
            // Image: 374x259
            content:
            [Rectangle {
                arcHeight: 10
                arcWidth: 10
                height: 400
                width: 400
                fill: Color.color(.4, .4, .5, .7)
                onMouseClicked: function(e:MouseEvent) {
                    visible = false;
                }
            },
            Text {
                transform: Transform.translate(384-15, 4)
                fill: Color.color(.7, .7, .7, 1)
                content: "close"
                horizontalAlignment: HorizontalAlignment.TRAILING
                font: Font.font("Verdana", FontStyle.PLAIN, 8)
            },
            Text {
                transform: Transform.translate(384, 6)
                fill: Color.color(.7, .7, .7, 1)
                content: "X"
                horizontalAlignment: HorizontalAlignment.TRAILING
                font: Font.font("Verdana", FontStyle.BOLD, 8)
            },
            ImageView {
                transform: Transform.translate(12.5, 20)
            //image: bind select Image {url: u} from u in imageUrl
            },
            Text {
                transform: Transform.translate(15, 259 + 20 + 10)
                font: Font.font("Verdana", FontStyle.BOLD, 11)
                content: bind text
                fill: Color.WHITE
            }]
        }
    }
}

class Hotspot extends CustomNode {
    attribute text: String;
    attribute action: function();
    attribute scaleValue: Number;
    //attribute Hotspot.scaleValue = bind if text == null then 0.4 else if hover then [0.4,0.6] dur 300 motion EASEBOTH else [0.6, 0.4] dur 300 motion EASEBOTH;
    
    attribute g: Group;
    attribute whiteCircle: Circle;
    
    function create():Node {
        return Group {
            var x = bind (scaleValue-0.4) * -15;
            var y = bind (scaleValue-0.4) * -15;
            var txt:Text;
            var blue = Color.color(.5, .5, .8, 1);
            onMouseClicked: function(e) {
                /*
                do later {hover = false;}
                (this.action)();*/
            }
            cursor: Cursor.HAND
            content: [g = Group {
                //                attribute: g
                //transform: bind [Transform.translate(x, y), Transform.scale(scaleValue, scaleValue)]
                transform: [Transform.translate(x, y), Transform.scale(scaleValue, scaleValue)]
                content:
                [Rectangle {
                    height: 30
                    width: 30
                    fill: Color.color(0, 0, 0, 0)
                },
                Circle {
                    centerX: 15
                    centerY: 5
                    radius: 5
                    fill: Color.BLUE
                },
                Circle {
                    centerX: 5
                    centerY: 15
                    radius: 5
                    fill: Color.BLUE
                },
                Circle {
                    centerX: 15
                    centerY: 25
                    radius: 5
                    fill: Color.BLUE
                },
                whiteCircle = Circle {
                    centerX: 25
                    centerY: 15
                    radius: 5
                    //attribute: whiteCircle
                    fill: bind (if (whiteCircle.isMouseOver()) then Color.TRANSPARENT else Color.WHITE) 
                }]
            },
            txt = Text {
                //transform: bind translate(x, y)
                x: 20/2
                y: 15/2
                verticalAlignment: VerticalAlignment.CENTER
                content: bind text
                fill: Color.WHITE
                font: Font.font("Verdana", FontStyle.BOLD, 12)
                visible: bind txt.isMouseOver()
            }]
        }
    }
}

class Cockpit extends CustomNode {
    //var __DOCBASE__:String = "";
    attribute detailImageUrl: String;
    attribute detailText: String;
    attribute detailVisible: Boolean
    	on replace oldValue {
        	if (oldValue) {
        		//detailOpacity = [0, 100] dur 500 motion EASEBOTH while oldValue == detailVisible;
        	}
    	}
    attribute detailOpacity: Number;
    
    
    function create():Node {
        return Group {
            content:
            [ComponentView {
                transform: Transform.translate(10, 10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
                    text:
                    "<html><div width='200'><h1>Cockpit
</h1>

<p>
	Sit inside the Tesla Roadster, and you notice the road before you  
	more than the accoutrements around you. Built close to the ground  
	with a low center of gravity, the car brings you closer to the road  
	in every sense. After just one drive, you will find the view from  
	larger vehicles to feel disappointingly removed from the driving  
	experience. In between accelerations, a quick glance at the dash  
	tells you the time (synchronized to the correct time zone), your  
	remaining charge, your tire pressure, and even your fastest  
	acceleration of the day.
</p>

<p>
	<a href='' style='text-decoration:none'><img border='0' src='http://www.teslamotors.com/images/nav/button_seemoreingallery.gif'
										style='border: 0px; width: 140px; height: 19px;'></a>
</p></div></html>"
                }
            },
            Group {
                transform: Transform.translate(210, 0)
                content:
                [ImageView {
                    image: Image{url: "{__DIR__}Image/1.jpg"}
                },
                Hotspot {
                    visible: bind not detailVisible
                    transform: Transform.translate(320, 60)
                    text: "Sound System and Vents"
                    action: function() {
                        detailText = "The state-of-the-art sound system from Blaupunkt provides\nMP3 suppport and even has an interface for your IPod.";
                        detailImageUrl = "{__DIR__}Image/5.jpg";
                        detailVisible = true;
                    }
                },
                Hotspot {
                    visible: bind not detailVisible
                    transform: Transform.translate(278, 185)
                    text: "Center Console"
                    action: function() {
                        detailText = "The center console controls the seat heaters and the instant-\non cabin heating and cooling system.";
                        detailImageUrl = "{__DIR__}Image/3.jpg";
                        detailVisible = true;
                    }
                },
                Hotspot {
                    visible: bind not detailVisible
                    transform: Transform.translate(95, 120)
                    text: "Door Switches"
                    action: function() {
                        detailText = "The fully powered door switches control central locking,\n power windows, and trunk release.";
                        detailImageUrl = "{__DIR__}Image/4.jpg";
                        detailVisible = true;
                    }
                },
                Hotspot {
                    visible: bind not detailVisible
                    transform: Transform.translate(225, 115)
                    text: "Steering Wheel"
                    action: function() {
                        detailText = "The leather-trimmed, sports steering wheel also contains\n the driver's airbag.";
                        detailImageUrl = "{__DIR__}Image/2.jpg";
                        detailVisible = true;
                    }
                },
                Hotspot {
                    visible: bind not detailVisible
                    transform: Transform.translate(130, 140)
                    text: "VDS Screen"
                    action: function() {
                        detailText = "The Vehicle Display System (VDS) screen provides real-time\ninformation about the performance, range, and efficiencenterY of\nyour Tesla Roadster.";
                        detailImageUrl = "{__DIR__}Image/6.jpg";
                        detailVisible = true;
                    }
                },
                Hotspot {
                    transform: Transform.translate(10, 375)
                },
                Text {
                    fill: Color.WHITE
                    transform: Transform.translate(25, 378)
                    content: "Click on hot spots to view more."
                },
                CockpitDetailView {
                    opacity: bind detailOpacity
                    transform: Transform.translate(100, 30)
                    text: bind detailText
                    imageUrl: bind detailImageUrl
                    visible: bind detailVisible
                }]
            }]
        }
    }
}

Canvas {
content: CockpitDetailView{}
}
