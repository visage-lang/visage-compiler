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
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.ext.swing.*;

class ColorButton extends CustomNode {
    var color: BodyColor;
    var selector: ColorSelector;
    var selected: Boolean = bind selector.selectedColor == color;
    
    override function create():Node {
        return Group {
            content: [
                Rectangle {height: 31, width: 33, stroke: Color.TRANSPARENT},
                ImageView {
                    image: Image{url: bind color.imageUrl}
                },
                Rectangle {
                    width: 33
                    height: 23
                    stroke: Color.WHITE
                    fill: Color.TRANSPARENT
                    cursor: Cursor.HAND
                    onMouseEntered: function(e) {
                        selector.selectedColor = color;
                    }
                },
                Rectangle {
                    transform: Transform.translate(0, 26)
                    height: 4
                    width: 33
                    stroke: Color.TRANSPARENT
                    fill: bind (if (selected) then Color.WHITE else Color.TRANSPARENT)
                }
            ]
        }
    }
}

class BodyColor {
    var name: String;
    var imageUrl: String;
    var carUrl: String;
    var group: String;
}

public class ColorSelector extends CustomNode {
    var fusionRed: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/6.jpg"
        carUrl: "{__DIR__}Image/colors/15.jpg"
        name: "Fusion Red"
        group: "solid"
    };
    
    var racingGreen: BodyColor  = BodyColor {
        imageUrl: "{__DIR__}Image/colors/4.jpg"
        carUrl: "{__DIR__}Image/colors/18.jpg"
        name: "Racing Green"
        group: "solid"
    };
    
    var brilliantYellow: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/11.jpg"
        carUrl: "{__DIR__}Image/colors/24.jpg"
        name: "Brilliant Yellow"
        group: "metallic"
    };
    
    var radiantRed: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/1.jpg"
        carUrl: "{__DIR__}Image/colors/13.jpg"
        name: "Radiant Red"
        group: "metallic"
    };
    
    var sterlingSilver: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/5.jpg"
        carUrl: "{__DIR__}Image/colors/21.jpg"
        name: "Sterling Silver"
        group: "metallic"
    };
    
    var glacierBlue: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/9.jpg"
        carUrl: "{__DIR__}Image/colors/23.jpg"
        name: "Glacier Blue"
        group: "metallic"
    };
    
    var obsidianBlack: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/12.jpg"
        carUrl: "{__DIR__}Image/colors/25.jpg"
        name: "Obsidian Black"
        group: "metallic"
    };
    
    var veryOrange: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/7.jpg"
        carUrl: "{__DIR__}Image/colors/20.jpg"
        name: "Very Orange"
        group: "premium"
    };
    var electricBlue: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/10.jpg"
        carUrl: "{__DIR__}Image/colors/22.jpg"
        name: "Electric Blue"
        group: "premium"
    };
    var arcticWhite: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/2.jpg"
        carUrl: "{__DIR__}Image/colors/16.jpg"
        name: "Arctic White"
        group: "premium"
    };
    
    var thunderGray: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/5.jpg"
        carUrl: "{__DIR__}Image/colors/21.jpg"
        name: "Thunder Gray"
        group: "premium"
    };
    
    var jetBlack: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/3.jpg"
        carUrl: "{__DIR__}Image/colors/17.jpg"
        name: "Jet Black"
        group: "premium"
    };
    
    var solidColors: BodyColor[] = bind [fusionRed, racingGreen] ;
    var metallicColors: BodyColor[] = bind
    [brilliantYellow, radiantRed, sterlingSilver, glacierBlue, obsidianBlack];
    var premiumColors: BodyColor[] = bind
    [veryOrange, electricBlue,arcticWhite, thunderGray, jetBlack];
    
    var colors: BodyColor[] = bind 
    [solidColors, metallicColors, premiumColors];
    
    var colorIndex: Number;
    var selectedColor: BodyColor = fusionRed;
    
    var solidGroup: Node;
    var metallicGroup: Node;
    var premiumGroup: Node;
    
    
    
    override function create():Node {
        return Group {
            content:
            [ComponentView {
                transform: Transform.translate(10, 10)
                component: Label {
                    foreground: Color.WHITE
                    font: Font.font("Arial", FontStyle.PLAIN, 12)
                    text:
                    "<html><div width='200'><h1>Available Colors
</h1>

<p style='color:#cccccc'>
The Tesla Roadster is available in metallic and non-metallic colors.
</p>
</div></html>"
                }
            },
            VBox {
                transform: Transform.translate(210, 0)
                content:
                [ImageView {
                // 486x225
                    image: bind if(selectedColor != null) { Image{url: selectedColor.carUrl} } else null
                //                    image: bind select Image{url: u} from u in selectedColor.carUrl
                },
                Group {
                    content:
                    [Rectangle {
                        height: 200
                        width: 500
                        arcHeight: 10
                        arcWidth: 10
                        fill: Color.color(.2, .2, .25, 1)
                    },
                    VBox {
                        transform: Transform.translate(20, 20)
                        content:
                        [Group { 
                            //clip: Rectangle {height: 20, width: 400}
                            content:
                            HBox {
                                content:
                                [Text {
                                    content: "Exterior Color:"
                                    fill: Color.color(.8, .8, .8, 1)
                                },
                                Text {
                                    transform: bind Transform.translate(5, 0)
                                    content: bind "{selectedColor.name}"
                                    fill: Color.WHITE
                                },
                                Text {
                                    transform: bind Transform.translate(5, 0)
                                    content: bind "({selectedColor.group})"
                                    fill: Color.GRAY
                                }]
                            }
                        },
                        HBox {
                            transform: Transform.translate(0, 10)
                            content:
                            [VBox {
                                content:
                                [solidGroup = HBox {
                                    //                                    var: solidGroup
                                    content:
                                    [ColorButton {
                                        selector: this
                                        color: fusionRed
                                    },
                                    ColorButton {
                                        selector: this
                                        transform: Transform.translate(5, 0)
                                        color: racingGreen
                                    }]
                                },
                                Line {
                                    transform: Transform.translate(0, 5)
                                    stroke: Color.GRAY
                                    startX: 0
                                    endX: bind solidGroup.getWidth()
                                },
                                Text {
                                    textOrigin: TextOrigin.TOP
                                    font: Font.font("Arial", FontStyle.PLAIN, 9)
                                    fill: Color.GRAY
                                    transform: Transform.translate(0, 5)
                                    content: "Solid Colors"
                                }]
                            },
                            VBox {
                                transform: Transform.translate(10, 0)
                                content:
                                [metallicGroup = HBox {
                                    //                                    var: metallicGroup
                                    content:
                                    [ColorButton {
                                        selector: this
                                        color: brilliantYellow
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: radiantRed
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: sterlingSilver
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: glacierBlue
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: obsidianBlack
                                        selector: this
                                    }]
                                },
                                Line {
                                    transform: Transform.translate(0, 5)
                                    stroke: Color.GRAY
                                    startX: 0
                                    endX: bind metallicGroup.getWidth()
                                },
                                Text {
                                    textOrigin: TextOrigin.TOP
                                    font: Font.font("Arial", FontStyle.PLAIN, 9)
                                    fill: Color.GRAY
                                    transform: Transform.translate(0, 5)
                                    content: "Metallic Colors"
                                }]
                            },
                            VBox {
                                transform: Transform.translate(10, 0)
                                content:
                                [premiumGroup = HBox {
                                    //                                    var: premiumGroup
                                    content:
                                    [ColorButton {
                                        color: veryOrange
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: electricBlue
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: arcticWhite
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: thunderGray
                                        selector: this
                                    },
                                    ColorButton {
                                        transform: Transform.translate(5, 0)
                                        color: jetBlack
                                        selector: this
                                    }]
                                },
                                Line {
                                    transform: Transform.translate(0, 5)
                                    stroke: Color.GRAY
                                    startX: 0
                                    endX: bind premiumGroup.getWidth()
                                },
                                Text {
                                    textOrigin: TextOrigin.TOP
                                    font: Font.font("Arial", FontStyle.PLAIN, 9)
                                    fill: Color.GRAY
                                    transform: Transform.translate(0, 5)
                                    content: "Premium Colors"
                                }]
                            }]
                        },
                        Text {
                            textOrigin: TextOrigin.TOP
                            transform: Transform.translate(0, 15)
                            font: Font.font("Verdana", FontStyle.BOLD, 10)
                            content: 
                            "Which of our 12 different colors is your favorite? Bold or subtle? Metallic or non-
metallic? No matter what color you choose, you'll be proud of how green your
Tesla Roadster really is."
                            fill: Color.color(.8, .8, .8, 1)
                            
                        },
                        Text {
                            textOrigin: TextOrigin.TOP
                            font: Font.font("Arial", FontStyle.PLAIN, 9)
                            transform: Transform.translate(0, 15)
                            content: 
                            "Please note that these colors are intended as a guide only and are not an exact match to the actual 
cars' colors."
                            fill: Color.GRAY
                        }]
                    }]
                }]
            }]
        }
    }
}

// test
function run( ) {
    Canvas{
        content: ColorSelector{}
    }
}
