/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;


class Picture {
    attribute imageUrl: String;
    attribute text: String;
}

// 454x275

public class ProcessInPictures extends CompositeNode {
    
    
    attribute selectedPictureIndex: Integer
    on replace oldValue = newValue {
        var c = ++counter;
        /*
        trigger on (i = [false, true] dur 12000 while c == counter) {
            if (i) {
                if (newValue + 1 < sizeof pictures) {
                    selectedPictureIndex++;
                } else {
                    selectedPictureIndex = 0;
                    ++counter;
                    opacityValue = 1.0;
                }
            }
        }*/
        lastSelectedPicture = if (oldValue >= 0 and oldValue < sizeof pictures) pictures[oldValue] else null;
    //        opacityValue = [0, 1] dur 2000 motion EASEOUT while c == counter;
    }
    
    attribute selectedPicture: Picture = bind pictures[selectedPictureIndex];
    
    
    attribute lastSelectedPicture: Picture;
    attribute opacityValue: Number = 1.0;
    attribute counter: Number;
    function forward() {
        ++selectedPictureIndex;
    }
    
    function back() {
        --selectedPictureIndex;
    }
    
    attribute pictures: Picture[] = 
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
    
    
    function composeNode():Node {
        return Group {
            content:
            [Group {
                content:
                View {
                    transform: Transform.translate(15, 0)
                    content:
                    Label {
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
                var blue = Color.rgba(.2, .2, .2, 1)
                var darkBlue = Color.rgba(.1, .1, .1, .8)
                transform: Transform.translate(210, 0)
                content:
                [Rect {
                    arcHeight: 10
                    arcWidth: 10
                    height: 420
                    width: 520
                    fill: Color.BLUE as Paint
                },
                Group {
                    transform: Transform.translate(35, 25)
                    content: 
                    [ImageView {
                        opacity: bind opacityValue
                        var f = function(url:String) {
                             Image {url: url};
                        }
                        image: bind f(selectedPicture.imageUrl) 
                    },
                    ImageView {
                        opacity: bind 1-opacityValue
                    //image: bind select Image {url: u} from u in lastSelectedPicture.imageUrl
                    },
                    Rect {
                        stroke: Color.WHITE as Paint
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
                            [{var r: Rect;
                                r = Rect {                                   
                                    selectable: true
                                    cursor: Cursor.HAND
                                    arcHeight: 20
                                    arcWidth: 20
                                    height: 20
                                    width: 100
                                    fill: bind (if (r.hover) then Color.BLACK else Color.DARKBLUE) as Paint
                                    onMouseClicked: function(e) {
                                        this.back();
                                    }
                                }},
                            HBox {
                                transform: Transform.translate(50, 10)
                                valign: VerticalAlignment.CENTER
                                halign: HorizontalAlignment.CENTER
                                content:
                                [Polygon {
                                    fill: Color.WHITE as Paint
                                    points: [0, 5, 10, 0, 10, 10]
                                },
                                Text {
                                    transform: Transform.translate(5, 0)
                                    content: "Back"
                                    fill: Color.rgba(1, 1, 1, .8) as Paint
                                }]
                            }]
                        },
                        Group {
                            visible: bind selectedPictureIndex + 1 < sizeof pictures
                            transform: Transform.translate(454-10, 0)
                            halign: HorizontalAlignment.TRAILING
                            content:
                            [{ var r:Rect;
                                r = Rect {                                    
                                    selectable: true
                                    cursor: Cursor.HAND
                                    arcHeight: 20
                                    arcWidth: 20
                                    height: 20
                                    width: 100
                                    fill: bind (if (r.hover) then Color.BLACK else Color.DARKBLUE) as Paint
                                    onMouseClicked: function(e) {
                                        this.forward();
                                    }
                                }},
                            HBox {
                                transform: Transform.translate(50, 10)
                                valign: VerticalAlignment.CENTER
                                halign: HorizontalAlignment.CENTER
                                content:
                                [Text {
                                    fill: Color.color(1, 1, 1, .8) as Paint
                                    content: "Forward"
                                },
                                Polygon {
                                    transform: Transform.translate(5, 0)
                                    fill: Color.WHITE as Paint
                                    points: [0, 0, 10, 5, 0, 10]
                                }]
                            }]
                        }]
                    },
                    Group {
                        transform: Transform.translate(0, 288)
                        content: Text {
                            opacity: bind opacityValue
                            fill: Color.WHITE as Paint
                            font: Font.Font("Verdana", ["BOLD"], 11)
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
