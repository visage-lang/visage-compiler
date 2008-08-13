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
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.input.*;
import javafx.animation.*;

class UnderTheSkinView {
    var title: String;
    var text: String;
    var imageUrl: String;
}

class UnderTheSkin extends CustomNode {
    var views: UnderTheSkinView[] =
    [UnderTheSkinView {
        title: "Overview"
        text: "Our minimalist design philosophy encourages multiple uses for each\ncomponent and fanatical attention to weight, which helps to maximize\n energy use, efficiency, and performance."
        imageUrl: "{__DIR__}Image/skin/1.jpg"
    },
    UnderTheSkinView {
        title: "ESS"
        text: 
        "The Energy Storage System (ESS) - comprised of several thousand consumer-grade
lithium-ion cells - is the heart of the Tesla Roadster. Battery conditions are
continuously monitored and fed to the Vehicle Management System (VMS),
allowing for precise tracking of battery history, performance, and available energy."
        imageUrl: "{__DIR__}Image/skin/2.jpg"
    },
    UnderTheSkinView {
        title: "Chassis"
        text: 
        "The Tesla chassis uses extruded aluminum to create a lightweight, stiff
structure. Front and rear crumple zones are integral parts of the design."
        imageUrl: "{__DIR__}Image/skin/3.jpg"
    },
    UnderTheSkinView {
        title: "Motor"
        text: 
        "The Tesla motor weighs less than 70 pounds yet produces horsepower equivalent to
a much heavier internal combustion engine. And unlike a gasoline engine, Tesla's
electric motor doesn't sacrifice mileage for performance."
        imageUrl: "{__DIR__}Image/skin/4.jpg"
    },
    UnderTheSkinView {
        title: "PEM"
        text: 
        "The Power Electronics Module (PEM) contains high voltage electronics that
control the motor and allow for integrated battery charging. The motor and
PEM have been designed as a tightly integrated system that delivers up to
185 kW of motor output."
        imageUrl: "{__DIR__}Image/skin/5.jpg"
    }]
    on replace oldValues[i..j] = newValues {
        if (selectedView == null) {
            if (sizeof newValues > 0) {
                selectedView = newValues[0];
            }
        }   
    }
    var fadein:Timeline = Timeline {
        keyFrames: [
            KeyFrame {
                time: 0s
                values: opacityValue => 0
            },
            KeyFrame {
                time: 500ms
                values: opacityValue => 1.0 tween Interpolator.LINEAR
            }
        ]
    };     
    
    var selectedView: UnderTheSkinView
    on replace {
        fadein.stop();
        fadein.start();
    }
    var opacityValue: Number = 1.0;
    
    
    
    override function create():Node {
        return Group {
            content:
            [ComponentView {
                transform: Transform.translate(10, 10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
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
                var blue = Color.color(.2, .2, .2, 1);
                var darkBlue = Color.color(.1, .1, .1, 1);
                transform: Translate.translate(210, 0)
                content:
                [Rectangle {
                    arcHeight: 10
                    arcWidth: 10
                    height: 420
                    width: 520
                    fill: Color.BLUE
                },
                Group {
                    transform: Translate.translate(40, 20)
                    content: bind for (v in views)
                    Group {
                        transform: bind Translate.translate(indexof v * (453/5), 0)
                        content: 
                        [Rectangle {
                            cursor: Cursor.HAND
                            arcHeight: 20
                            arcWidth: 20
                            height: 18
                            width: 400/5
                            fill: bind (if (selectedView == v) then Color.WHITE else Color.MIDNIGHTBLUE)
                            onMouseClicked: function(e:MouseEvent) {
                                selectedView = v;
                            }
                        },
                        Group {
                            transform: Translate.translate(40, 12)
                            content:
                            [Rectangle {
                                y: 2
                                height: 5
                                width: 5
                                fill: bind (if (selectedView == v) then Color.BLACK else Color.WHITE)
                            },
                            Text {
                                textOrigin: TextOrigin.TOP
                                x: 10
                                fill: bind (if (selectedView == v) then Color.BLACK else Color.WHITE)
                                content: bind v.title
                                font: Font.font("Helvetica", FontStyle.BOLD, 12)
                            }]
                            verticalAlignment: VerticalAlignment.CENTER
                            horizontalAlignment: HorizontalAlignment.CENTER
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
                    Rectangle {
                        width: 440
                        height: 275
                        stroke: Color.WHITE
                        strokeWidth: 2
                    }]
                },
                //TODO: Need to wrap this text
                Text {
                    opacity: bind opacityValue
                    fill: Color.WHITE
                    font: Font.font("Helvetica", FontStyle.BOLD, 12)
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
