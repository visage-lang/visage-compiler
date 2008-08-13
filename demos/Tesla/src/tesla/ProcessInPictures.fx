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
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.animation.*;
import java.lang.System;

class Picture {
    var imageUrl: String;
    var text: String;
}

// 454x275

public class ProcessInPictures extends CustomNode {
    
    
    var selectedPictureIndex: Integer
    on replace oldValue = newValue {
        fadeIn.start();
    }
    var fadeIn = Timeline {
        keyFrames: [
            KeyFrame {
                time: 0s
                values: opacityValue => 0
            },
            KeyFrame {
                time: 2s
                values: opacityValue => 1.0 tween Interpolator.EASEOUT
            }
        ] 
   };
    
    var selectedPicture: Picture = bind pictures[selectedPictureIndex];
    
    
    var opacityValue: Number = 1.0;
    function forward() {
        ++selectedPictureIndex;
    }
    
    function back() {
        --selectedPictureIndex;
    }
    
    var pictures: Picture[] = 
    [Picture {
        imageUrl: "{__DIR__}Image/process/1.jpg"
        text: "Initial Rendering of the Tesla Roadster."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/2.jpg"
        text: 
        "Quarter-scale Clay Model (Early Stage)
The early clay model has a peak on the front bumper that was smoothed
down in the full-size clay model."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/3.jpg"
        text: 
        "Quarter-scale Clay Model (Intermediate Stage)
This model has individual headlight apertures that were later enclosed
on the full-size clay model."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/4.jpg"
        text:
        "Quarter-scale Clay Model (Signed Off)
This model has a black graphic on the front that was later removed for a
cleaner line in the full-size clay model."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/5.jpg"
        text: "Early Interior Sketch of the Tesla Roadster."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/6.jpg"
        text:  "Developed Rendering of the Interior."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/7.jpg"
        text:
        "Full-size Clay Model (Early Stage)
The air intake feature on the front bumper was increased in this model
for a more aggresive look"
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/8.jpg"
        text:
        "Full-size Clay Model (Intermediate Stage)
The headlight apertures are fitted for the first time with an enclosure."
    },
    Picture {
        imageUrl: "{__DIR__}Image/process/9.jpg"
        text:
        "Full-size Clay Model (Signed Off)
Although this looks like a real car, it is in fact a clay model finished
with painted film, rear wheels, mirrors, wiper, windscreen, and headlights."
    }];
    
    
    override function create():Node {
        return Group {
            content:
            [Group {
                content:
                ComponentView {
                    transform: Transform.translate(15, 0)
                    component: Label {
                        foreground: Color.WHITE
                        text:
                        "<html>
<div width='190'>
<h1>

    The Process in Pictures
</h1>

<p class='leftcolcontent'>
    Lots of great ideas start as sketches on napkins. Tesla is one of them.
    Long before its body took shape in carbon fiber, its every detail was rendered
    in pencil, clay, and computerized graphics. Want to see how it evolved along
    the way? Come along for the ride.
</p>
</div>
</html>"                    
                    }
                }
            },
            Group {
                var blue = Color.color(.2, .2, .2, 1);
                var darkBlue = Color.color(.1, .1, .1, .8);
                transform: Transform.translate(210, 0)
                content:
                [Rectangle {
                    arcHeight: 10
                    arcWidth: 10
                    height: 420
                    width: 520
                    fill: Color.BLUE
                },
                Group {
                    transform: Transform.translate(35, 25)
                    content: 
                    [ImageView {
                        var f = function(url:String) {
                             Image {url: url};
                        };
                        opacity: bind opacityValue
                        image: bind Image{ url: selectedPicture.imageUrl}
                    },
                    //ImageView {
                    //    opacity: bind 1-opacityValue
                    //image: bind select Image {url: u} from u in lastSelectedPicture.imageUrl
                    //},
                    Rectangle {
                        stroke: Color.WHITE
                        height: 275
                        width: 454
                    },
                    Group {
                        transform: Transform.translate(0, 250)
                        content:
                        [Group {
                            visible: bind selectedPictureIndex > 0
                            transform: Transform.translate(10, 0)
                            content:
                            [{var r: Rectangle;
                                r = Rectangle {                                   
                                    cursor: Cursor.HAND
                                    arcHeight: 20
                                    arcWidth: 20
                                    height: 20
                                    width: 100
                                    fill: bind (if (r.isMouseOver()) then Color.BLACK else Color.DARKBLUE)
                                    onMouseClicked: function(e) {
                                        this.back();
                                    }
                                }},
                            HBox {
                                transform: Transform.translate(50, 12)
                                verticalAlignment: VerticalAlignment.CENTER
                                horizontalAlignment: HorizontalAlignment.CENTER
                                content:
                                [Polygon {
                                    fill: Color.WHITE
                                    points: [0, 5, 10, 0, 10, 10]
                                },
                                Text {
                                    textOrigin: TextOrigin.TOP
                                    transform: Transform.translate(5, 0)
                                    content: "Back"
                                    fill: Color.color(1, 1, 1, .8)
                                }]
                            }]
                        },
                        Group {
                            visible: bind selectedPictureIndex + 1 < sizeof pictures
                            transform: Transform.translate(454-10, 0)
                            horizontalAlignment: HorizontalAlignment.TRAILING
                            content:
                            [{ var r:Rectangle;
                                r = Rectangle {                                    
                                    cursor: Cursor.HAND
                                    arcHeight: 20
                                    arcWidth: 20
                                    height: 20
                                    width: 100
                                    fill: bind (if (r.isMouseOver()) then Color.BLACK else Color.DARKBLUE)
                                    onMouseClicked: function(e) {
                                        this.forward();
                                    }
                                }},
                            HBox {
                                transform: Transform.translate(50, 12)
                                verticalAlignment: VerticalAlignment.CENTER
                                horizontalAlignment: HorizontalAlignment.CENTER
                                content:
                                [Text {
                                    textOrigin: TextOrigin.TOP
                                    fill: Color.color(1, 1, 1, .8)
                                    content: "Forward"
                                },
                                Polygon {
                                    transform: Transform.translate(5, 0)
                                    fill: Color.WHITE
                                    points: [0, 0, 10, 5, 0, 10]
                                }]
                            }]
                        }]
                    },
                    Group {
                        transform: Transform.translate(0, 288)
                        content: Text {
                            opacity: bind opacityValue
                            fill: Color.WHITE
                            font: Font.font("Verdana", FontStyle.BOLD, 11)
                            content: bind selectedPicture.text
                        }
                    }]
                }]
            }]
        }
        ;
    }
}


Canvas {
content: ProcessInPictures{}
}
