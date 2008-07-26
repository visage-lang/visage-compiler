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
import javafx.animation.*;
import javafx.ext.swing.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.input.*;
import java.lang.System;

public class BodyStyling extends CustomNode {
    
    attribute imageBaseUrl: String = "http://www.teslamotors.com/design/images-body";
    attribute thumbBaseUrl: String = "http://www.teslamotors.com/design/thumbs-body";
    
    attribute images: Integer[] = 
    [10, 
        30,
        40,
        70,
        50,
        20,
        80,
        60,
        90,
        94,
        95,
        96,
        97,
        103,
        105,
        191,
        192,
        193,
        401,
        402,
        404,
        408,
        500,
        501,
        502,
        554,
        99,
        555,
        600,
        700,
        100,
        101,
        102];
    
    attribute panTimeline = Timeline {
        toggle: true
        keyFrames: [
            KeyFrame {
                time: 0s
                values: thumbPaneX => 0
            },
            KeyFrame {
                time: 1s
                values: thumbPaneX => -54 * 3 tween Interpolator.EASEOUT
            }
    ]
};

attribute selectedImage: Integer;
attribute thumbPaneX: Number;
function panLeft():Void {
    panned = false;
    panTimeline.start();
}

function panRight():Void {
    panned = true;
    panTimeline.start();
}

attribute panned: Boolean;

attribute selectedImageOpacity: Number = 1.0;

function create():Node {
    var size = sizeof images;
    System.out.println("image size = {size}");
    
    Group {
        transform: Translate.translate(15, 30)
        content:
        [Group {
                clip: Rectangle {height: 54*6, width: 44 * 3 + 22}
                content: Group {
                    transform: bind Transform.translate(thumbPaneX, 0)
                    content:
                    for (row in [0..5]) {
                        for(col in [0..5]) {
                            if  (col*6 + row >= size)
                            {System.out.println("skipping {col*6 + row}"); null}
                            else { 
                                var dummy = {System.out.println("not skipping {col*6 + row} of {sizeof images}"); 1;}
                                var g:Group; 
                                g = Group {
                                    id: "g"
                                    //var: g
                                    transform: Transform.translate(col * 54, row * 54)
                                    content:
                                    [ImageView {
                                            image: Image {url: "{thumbBaseUrl}/{images[col*6 + row]}.jpg"}
                                        },
                                        Rectangle {
                                            cursor: Cursor.HAND
                                            height: 44
                                            width: 44
                                            stroke: Color.color(.7, .7, .7, 1)
                                            fill: Color.color(0, 0, 0, 0)
                                            onMouseClicked: function(e) {
                                                selectedImage = col*6 + row;
                                            }
                                            },{
                                            var r:Rectangle;
                                            var rectFlash:Timeline = bind Timeline {
                                                keyFrames: [
                                                    KeyFrame {
                                                        time: 0s
                                                        values: r.opacity => 0.0
                                                    },
                                                    KeyFrame {
                                                        time: 250ms
                                                        values: r.opacity => 0.8 tween Interpolator.EASEOUT
                                                    },
                                                    KeyFrame {
                                                        time: 500ms
                                                        values: r.opacity => 0.0 tween Interpolator.EASEOUT
                                                    }                                        
                                            ]
                                    };     
                                    r = Rectangle {
                                        id: "rect"
                                        height: 44,
                                        width: 44,
                                        fill: Color.WHITE
                                        opacity: 0.0
                                        onMouseEntered: function(e:MouseEvent):Void {
                                            rectFlash.start();
                                        }
                                        }},
                                Group {
                                    content:
                                    bind if (selectedImage == col*6 + row)
                                    then [Group {
                                            var fillColor = Color.color(.7, .7, .7, 1.0);
                                            transform: Transform.translate(1.5, 1.5)
                                            content:
                                            [Rectangle {
                                                    height: 45
                                                    width: 45
                                                    strokeWidth: 3
                                                    stroke: fillColor
                                                    strokeLineJoin: StrokeLineJoin.BEVEL
                                                    strokeLineCap: StrokeLineCap.SQUARE
                                                },
                                                Polygon {
                                                    transform: Transform.translate(45, 0)
                                                    horizontalAlignment: HorizontalAlignment.TRAILING
                                                    points: [0.0, 0.0, 12.5, 0.0, 12.5, 12.5]
                                                    fill: fillColor
                                                    stroke: fillColor as  Paint
                                                    strokeWidth: 3
                                                    strokeLineJoin: StrokeLineJoin.BEVEL
                                                    }]
                                            }]
                                    else null
                                    }]
                        }  
                }
        }
}
}
},
Group {
    transform: Transform.translate(10, 54 * 6+10)
    content:
    [Group {
            visible: bind panned
            content:
            [Arrow {
                    transform: Transform.translate(15, 15)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER
                    left: true
                },
                Rectangle {
                    height: 44, 
                    width: 44, 
                    stroke:Color.TRANSPARENT, 
                    fill: Color.TRANSPARENT
                    onMouseClicked: function(e:MouseEvent) {
                        if (e.getClickCount() == 1) {
                            this.panLeft();
                        }
                }
                cursor: Cursor.HAND
                }]
    },
    Group {
        visible: bind not panned and sizeof images > 18
        transform: Transform.translate(44*2 + 20, 0)
        content:
        [Arrow {
                transform: Transform.translate(22, 22)
                verticalAlignment: VerticalAlignment.CENTER
                horizontalAlignment: HorizontalAlignment.CENTER
                left: false
            },
            Rectangle {height: 44, width: 44, 
                fill: Color.TRANSPARENT
                stroke: Color.TRANSPARENT
                onMouseClicked: function(e:MouseEvent) {
                    if (e.getClickCount() == 1) {
                        this.panRight();
                    }
            }
            cursor: Cursor.HAND
            }]
    }]
},
Group {
    var makeImage = function(imageBaseUrl:String, selectedImage:Integer): Image {
        Image {
            url:  "{imageBaseUrl}/{images[selectedImage]}.jpg"
        }
    };
    transform: [Transform.translate(54*3+15, 0), Transform.scale(0.67, 0.67)]
    opacity: bind selectedImageOpacity
content:
[ImageView {
        image: bind makeImage(imageBaseUrl, selectedImage)
    },
    Rectangle {
        height: 600
        width: 800
        stroke: Color.color(.7, .7, .7, 1)
        }]
},
Group {
    
    transform: [Transform.translate(54*3+15, 0)]
    content:
    [
        Group {
            transform: Transform.translate(0, .67*600-44)
            content: 
            [Rectangle {
                    cursor: Cursor.HAND
                    height: 44
                    width: 44
                    fill: Color.TRANSPARENT
                    stroke: Color.TRANSPARENT
                    onMouseClicked: function(e) {
                        if (selectedImage == 0) {
                            selectedImage = sizeof images -1;
                            } else {
                            selectedImage--;
                        }
                }
        },
        Group {
            content: [
                Rectangle {
                    height: 44
                    width: 44
                    stroke: Color.TRANSPARENT
                },
                Arrow{
                    transform: Transform.translate(19,17)
                    //color: Color.BLACK
                    horizontalAlignment: HorizontalAlignment.CENTER
                    verticalAlignment: VerticalAlignment.CENTER
                    left: true
                }
                //TODO ShapeSubtract not seeming to work right
                /*****
                ShapeSubtract {
                a: Rectangle {
                height: 44
                width: 44
            }
                var arrow = Arrow{}
                b: ShapeIntersect {
                transform: [Transform.translate(20, 12), Transform.rotate(180, 12.5, 10)]
                a: bind arrow.arrowShape
                b: bind arrow.arrowShape
            }
                fill: Color.color(.7, .7, .7, 1)
                }*********/
            ]
    }
]
},
Group {
    transform: Transform.translate(.67*800-44, .67*600-44)
    content:
    [
        Rectangle {
            height: 44
            width: 44
            fill: Color.TRANSPARENT
            stroke: Color.TRANSPARENT
            cursor: Cursor.HAND
            onMouseClicked: function(e) {
                if (selectedImage +1 == sizeof images) {
                    selectedImage = 0;
                    } else {
                    selectedImage++;
                }
        }
},
Group {
    content: [
        Rectangle {
            height: 44
            width: 44
            stroke: Color.TRANSPARENT
        },
        Arrow {
            transform: Transform.translate(25,25)
            //color: Color.BLACK
            horizontalAlignment: HorizontalAlignment.CENTER
            verticalAlignment: VerticalAlignment.CENTER
            left: false
        }
        // TODO ShapeSubtract does not appear to work right
        /****************************
        ShapeSubtract {
        a: Rectangle {
        height: 44
        width: 44
    }
        var arrow = Arrow{  }
        b: ShapeIntersect {
        transform: Transform.translate(10, 12)
        a: bind arrow.arrowShape
        b: bind arrow.arrowShape
    }
        fill: Color.color(.7, .7, .7, 1)
    }
        ********************/
    ]
}
]
}
]


}
] 
}
}
}

Canvas{
content:
BodyStyling{}
}
