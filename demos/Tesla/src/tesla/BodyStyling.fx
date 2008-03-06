/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.*;
import java.lang.System;

public class BodyStyling extends CompositeNode {

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
    
    attribute pf = PointerFactory {};
    attribute pThumbX = bind pf.make(thumbPaneX).unwrap();
    
    attribute panTimeline = Timeline {
        toggle: true
        keyFrames:
        [KeyFrame {
            keyTime: 0s
            keyValues: 
            NumberValue {
                target: pThumbX
                value: 0
            }
        },
        KeyFrame {
            keyTime: 1s
            keyValues:
            NumberValue {
                target: pThumbX
                value: -54 *3
                interpolate: NumberValue.QuintEase.EASEOUT;
            }
        }]
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
    
    function composeNode():Node {
        var size = sizeof images;
        System.out.println("image size = {size}");
        Group {
            transform: Translate.translate(15, 30)
            content:
            [Clip {
                shape: Rect {height: 54*6, width: 44 * 3 + 22}
                content:
                Group {
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
                                Rect {
                                    cursor: Cursor.HAND
                                    height: 44
                                    width: 44
                                    stroke: Color.color(.7, .7, .7, 1) as Paint
                                    fill: Color.color(0, 0, 0, 0) as Paint
                                    onMouseClicked: function(e) {
                                        selectedImage = col*6 + row;
                                    }
                                },{
                                var r:Rect;
                                r = Rect {
                                    id: "rect"
                                    selectable: false
                                    height: 44,
                                    width: 44,
                                    fill: Color.WHITE as Paint
                                    //opacity: bind if (g.hover) then [[0,0.8,0]] dur 500 motion EASEBOTH else 0 
                                    visible: false
                                }},
                                Group {
                                    content:
                                    bind if (selectedImage == col*6 + row)
                                    then Group {
                                        var fillColor = Color.rgba(.7, .7, .7, 1)
                                        transform: Transform.translate(1.5, 1.5)
                                        content:
                                        [Rect {
                                            height: 45
                                            width: 45
                                            strokeWidth: 3
                                            stroke: fillColor as Paint
                                            strokeLineJoin: StrokeLineJoin.BEVEL
                                            strokeLineCap: StrokeLineCap.SQUARE
                                        },
                                        Polygon {
                                            transform: Transform.translate(45, 0)
                                            halign: HorizontalAlignment.TRAILING
                                            points: [0, 0, 12.5, 0, 12.5, 12.5]
                                            fill: fillColor as Paint
                                            stroke: fillColor as  Paint
                                            strokeWidth: 3
                                            strokeLineJoin: StrokeLineJoin.BEVEL
                                        }]
                                    }
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
                        valign: VerticalAlignment.CENTER
                        halign: HorizontalAlignment.CENTER
                        left: true
                    },
                    Rect {height: 44, width: 44, fill: Color.rgba(0, 0, 0, 0) as Paint
                        selectable: true
                        onMouseClicked: function(e:CanvasMouseEvent) {
                            if (e.clickCount == 1) {
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
                        valign: VerticalAlignment.MIDDLE
                        halign: HorizontalAlignment.CENTER
                        left: false
                    },
                    Rect {height: 44, width: 44, fill: Color.rgba(0, 0, 0, 0) as Paint
                        selectable: true
                        onMouseClicked: function(e:CanvasMouseEvent) {
                            if (e.clickCount == 1) {
                                this.panRight();
                            }
                        }
                        cursor: Cursor.HAND
                    }]
                }]
            },
            Group {
                transform: [Transform.translate(54*3+15, 0), Transform.scale(0.67, 0.67)]
                opacity: bind selectedImageOpacity
                var makeImage = function(imageBaseUrl:String, selectedImage:Integer): Image {
                    Image {
                        url:  "{imageBaseUrl}/{images[selectedImage]}.jpg"
                    }
                }
                content:
                [ImageView {
                    image: bind makeImage(imageBaseUrl, selectedImage)
                },
                Rect {
                    height: 600
                    width: 800
                    stroke: Color.rgba(.7, .7, .7, 1) as Paint
                }]
            },
            Group {
                transform: [Transform.translate(54*3+15, 0)]
                content:
                [Group {
                    transform: Transform.translate(0, .67*600-44)
                    content: 
                    [Rect {
                        cursor: Cursor.HAND
                        height: 44
                        width: 44
                        selectable: true
                        fill: Color.rgba(0, 0, 0, 0) as Paint
                        onMouseClicked: function(e) {
                            if (selectedImage == 0) {
                                selectedImage = sizeof images -1;
                            } else {
                                selectedImage--;
                            }
                        }
                    },
                    Subtract {
                        shape1: Rect {
                            height: 44
                            width: 44
                        }
                        //var arrow = Arrow
                        shape2: Intersect {
                            transform: [Transform.translate(10, 12), Transform.rotate(180, 12.5, 10)]
                            /*shape1: bind arrow.arrowShape
                            shape2: bind arrow.arrowShape*/
                        }
                        fill: Color.rgba(.7, .7, .7, 1) as Paint
                    }]
                },
                Group {
                    transform: Transform.translate(.67*800-44, .67*600-44)
                    content:
                    [Rect {
                        height: 44
                        width: 44
                        fill: Color.rgba(0, 0, 0, 0) as Paint
                        cursor: Cursor.HAND
                        onMouseClicked: function(e) {
                            if (selectedImage +1 == sizeof images) {
                                selectedImage = 0;
                            } else {
                                selectedImage++;
                            }
                        }
                    },
                    Subtract {
                        shape1: Rect {
                            height: 44
                            width: 44
                        }
                        //var arrow = Arrow
                        shape2: Intersect {
                            transform: [Transform.translate(10, 12)]
                            /*shape1: bind arrow.arrowShape
                            shape2: bind arrow.arrowShape*/
                        }
                        fill: Color.rgba(.7, .7, .7, 1) as Paint
                    }]
                }]
            }]
            
            
        }
        
    }
}

Canvas{
content:
BodyStyling{}
}
