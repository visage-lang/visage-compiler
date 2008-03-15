/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;


class CockpitDetailView extends CompositeNode {
    attribute imageUrl: String;
    attribute text: String;
    
    function composeNode():Node {
        return Group {
            // Image: 374x259
            content:
            [Rect {
                arcHeight: 10
                arcWidth: 10
                height: 400
                width: 400
                fill: Color.rgba(.4, .4, .5, .7) as Paint
                onMouseClicked: function(e:CanvasMouseEvent) {
                    visible = false;
                }
            },
            Text {
                transform: Transform.translate(384-15, 4)
                fill: Color.rgba(.7, .7, .7, 1) as Paint
                content: "close"
                halign: HorizontalAlignment.TRAILING
                font: Font.Font("Verdana", ["PLAIN"], 8)
            },
            Text {
                transform: Transform.translate(384, 6)
                fill: Color.rgba(.7, .7, .7, 1) as Paint
                content: "X"
                halign: HorizontalAlignment.TRAILING
                font: Font.Font("Verdana", ["BOLD"], 8)
            },
            ImageView {
                transform: Transform.translate(12.5, 20)
            //image: bind select Image {url: u} from u in imageUrl
            },
            Text {
                transform: Transform.translate(15, 259 + 20 + 10)
                font: Font.Font("Verdana", ["BOLD"], 11)
                content: bind text
                fill: Color.WHITE as Paint
            }]
        }
    }
}

class Hotspot extends CompositeNode {
    attribute text: String;
    attribute action: function();
    attribute scaleValue: Number;
    //attribute Hotspot.scaleValue = bind if text == null then 0.4 else if hover then [0.4,0.6] dur 300 motion EASEBOTH else [0.6, 0.4] dur 300 motion EASEBOTH;
    
    attribute g: Group;
    attribute whiteCircle: Circle;
    
    function composeNode():Node {
        return Group {
            var x = bind (scaleValue-0.4) * -15
            var y = bind (scaleValue-0.4) * -15
            onMouseClicked: function(e) {
                /*
                do later {hover = false;}
                (this.action)();*/
            }
            cursor: Cursor.HAND
            var blue = Color.rgba(.5, .5, .8, 1)
            content: [g = Group {
                //                attribute: g
                //transform: bind [Transform.translate(x, y), Transform.scale(scaleValue, scaleValue)]
                transform: [Transform.translate(x, y), Transform.scale(scaleValue, scaleValue)]
                content:
                [Rect {
                    selectable: true
                    height: 30
                    width: 30
                    fill: Color.rgba(0, 0, 0, 0) as Paint
                },
                Circle {
                    cx: 15
                    cy: 5
                    radius: 5
                    fill: Color.BLUE as Paint
                },
                Circle {
                    cx: 5
                    cy: 15
                    radius: 5
                    fill: Color.BLUE as Paint
                },
                Circle {
                    cx: 15
                    cy: 25
                    radius: 5
                    fill: Color.BLUE as Paint
                },
                whiteCircle = Circle {
                    cx: 25
                    cy: 15
                    radius: 5
                    //attribute: whiteCircle
                    fill: bind (if (hover) then Color.rgba(0, 0, 0, 0) else Color.WHITE)  as Paint
                }]
            },
            Text {
                selectable: true
                //transform: bind translate(x, y)
                x: 20/2
                y: 15/2
                valign: VerticalAlignment.CENTER
                content: bind text
                fill: Color.WHITE  as Paint
                font: Font.Font("Verdana", ["BOLD"], 12)
                visible: bind hover
            }]
        }
    }
}

class Cockpit extends CompositeNode {
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
    
    
    function composeNode():Node {
        return Group {
            content:
            [View {
                transform: Transform.translate(10, 10)
                content: Label {
                    foreground: Color.WHITE
                    font: Font.Font("Arial", ["PLAIN"], 12)
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
                        detailText = "The Vehicle Display System (VDS) screen provides real-time\ninformation about the performance, range, and efficiency of\nyour Tesla Roadster.";
                        detailImageUrl = "{__DIR__}Image/6.jpg";
                        detailVisible = true;
                    }
                },
                Hotspot {
                    transform: Transform.translate(10, 375)
                },
                Text {
                    fill: Color.WHITE as Paint
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
