/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package tesla;

import javafx.ui.*;
import javafx.ui.canvas.*;


class UnderTheSkinView {
    attribute title: String;
    attribute text: String;
    attribute imageUrl: String;
}

class UnderTheSkin extends CompositeNode {
    attribute views: UnderTheSkinView[] =
    [UnderTheSkinView {
        title: "Overview"
        text: "Our minimalist design philosophy encourages multiple uses for each\ncomponent and fanatical attention to weight, which helps to maximize\n energy use, efficiency, and performance."
        imageUrl: "/tesla/Image/skin/1.jpg"
    },
    UnderTheSkinView {
        title: "ESS"
        text: 
        "The Energy Storage System (ESS) - comprised of several thousand consumer-grade
lithium-ion cells - is the heart of the Tesla Roadster. Battery conditions are
continuously monitored and fed to the Vehicle Management System (VMS),
allowing for precise tracking of battery history, performance, and available energy."
        imageUrl: "/tesla/Image/skin/2.jpg"
    },
    UnderTheSkinView {
        title: "Chassis"
        text: 
        "The Tesla chassis uses extruded aluminum to create a lightweight, stiff
structure. Front and rear crumple zones are integral parts of the design."
        imageUrl: "/tesla/Image/skin/3.jpg"
    },
    UnderTheSkinView {
        title: "Motor"
        text: 
        "The Tesla motor weighs less than 70 pounds yet produces horsepower equivalent to
a much heavier internal combustion engine. And unlike a gasoline engine, Tesla's
electric motor doesn't sacrifice mileage for performance."
        imageUrl: "/tesla/Image/skin/4.jpg"
    },
    UnderTheSkinView {
        title: "PEM"
        text: 
        "The Power Electronics Module (PEM) contains high voltage electronics that
control the motor and allow for integrated battery charging. The motor and
PEM have been designed as a tightly integrated system that delivers up to
185 kW of motor output."
        imageUrl: "/tesla/Image/skin/5.jpg"
    }]
    on replace oldValues[i..j] = newValues {
        if (selectedView == null) {
            if (sizeof newValues > 0) {
                selectedView = newValues[0];
            }
        }   
    }
    
    attribute selectedView: UnderTheSkinView
    on replace {
/*
        for (i in [0..100]) dur 500 while v == selectedView {
            opacityValue = i/100;
        }
*/
    }
    attribute opacityValue: Number;
    
    
    
    function composeNode():Node {
        return Group {
            content:
            [View {
                transform: Transform.translate(10, 10)
                content: Label {
                    foreground: Color.WHITE
                    font: Font.Font("Arial", ["PLAIN"], 12)
                    text:
                    "<html><div width='190'><h1>Under the Skin
</h1>

<p>
	The highly purposeful design of the Tesla Roadster shows up in its many 
	multi-use components. There's a battery box that's both functional and 
	structural, air ducts that double as energy-absorbing zones, and a 
	high-performance AC motor that can run forwards and backwards. The PEM 
	(Power Electronics Module) performs several critical functions, 
	including motor torque control, regenerative braking control, and 
	charging. Of course the most notable thing inside the car is the battery 
	itself: our ESS (Energy Storage System) is safe, lightweight, and able 
	to take you 250 miles between charges.
</p></div></html>"
                }
            },
            Group {
                var blue = Color.color(.2, .2, .2, 1)
                var darkBlue = Color.color(.1, .1, .1, 1)
                transform: Translate.translate(210, 0)
                content:
                [Rect {
                    arcHeight: 10
                    arcWidth: 10
                    height: 420
                    width: 520
                    fill: Color.BLUE as Paint
                },
                Group {
                    transform: Translate.translate(40, 20)
                    content: bind for (v in views)
                    Group {
                        transform: bind Translate.translate(indexof v * (453/5), 0)
                        content: 
                        [Rect {
                            cursor: Cursor.HAND
                            arcHeight: 20
                            arcWidth: 20
                            height: 18
                            width: 400/5
                            fill: bind (if (selectedView == v) then Color.WHITE else Color.MIDNIGHTBLUE) as Paint
                            onMouseClicked: function(e:CanvasMouseEvent) {
                                selectedView = v;
                            }
                        },
                        Group {
                            transform: Translate.translate(40, 9)
                            content:
                            [Rect {
                                y: 2
                                height: 5
                                width: 5
                                fill: bind (if (selectedView == v) then Color.BLACK else Color.WHITE) as Paint
                            },
                            Text {
                                x: 10
                                fill: bind (if (selectedView == v) then Color.BLACK else Color.WHITE) as Paint
                                content: bind v.title
                                font: Font.Font("Helvetica", ["BOLD"], 12)
                            }]
                            valign: VerticalAlignment.CENTER
                            halign: HorizontalAlignment.CENTER
                        }]
                    }
                },
                Group {
                    transform: Translate.translate(40, 50)
                    content:
                    [ImageView {
                        //                        image: bind select Image {url: u} from u in selectedView.imageUrl
                        var f = function(url:String) {
                             Image {url: url};
                        }
                        image: bind f(selectedView.imageUrl) 
                    },
                    Rect {
                        width: 440
                        height: 275
                        stroke: Color.WHITE as Paint
                        strokeWidth: 2
                    }]
                },
                Text {
                    opacity: bind opacityValue
                    fill: Color.WHITE as Paint
                    font: Font.Font("Helvetica", ["BOLD"], 12)
                    transform: Transform.translate(35, 345)
                    content: bind selectedView.text
                }]
            }]
        }
    }
};

Canvas{
    content:UnderTheSkin{}
}
