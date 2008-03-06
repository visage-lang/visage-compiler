/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;

class ColorButton extends CompositeNode {
    attribute color: BodyColor;
    attribute selector: ColorSelector;
    attribute selected: Boolean = bind selector.selectedColor == color;
    
    function composeNode():Node {
        return Clip {
            shape: Rect {height: 31, width: 33}
            content:
            [ImageView {
                image: Image{url: bind color.imageUrl}
            },
            Rect {
                width: 32
                height: 23
                stroke: Color.WHITE as Paint
                fill: Color.rgba(0, 0, 0, 0) as Paint
                cursor: Cursor.HAND
                onMouseEntered: function(e) {
                    selector.selectedColor = color;
                }
            },
            Rect {
                transform: Transform.translate(0, 26)
                height: 4
                width: 33
                fill: bind (if (selected) then Color.WHITE else Color.rgba(0, 0, 0, 0)) as Paint
            }]
        }
    }
}

class BodyColor {
    attribute name: String;
    attribute imageUrl: String;
    attribute carUrl: String;
    attribute group: String;
}

public class ColorSelector extends CompositeNode {
    //var __DOCBASE__ = "";
    attribute fusionRed: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/6.jpg"
        carUrl: "{__DIR__}Image/colors/15.jpg"
        name: "Fusion Red"
        group: "solid"
    };
    
    attribute racingGreen: BodyColor  = BodyColor {
        imageUrl: "{__DIR__}Image/colors/4.jpg"
        carUrl: "{__DIR__}Image/colors/18.jpg"
        name: "Racing Green"
        group: "solid"
    };
    
    attribute brilliantYellow: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/11.jpg"
        carUrl: "{__DIR__}Image/colors/24.jpg"
        name: "Brilliant Yellow"
        group: "metallic"
    };
    
    attribute radiantRed: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/1.jpg"
        carUrl: "{__DIR__}Image/colors/13.jpg"
        name: "Radiant Red"
        group: "metallic"
    };
    
    attribute sterlingSilver: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/5.jpg"
        carUrl: "{__DIR__}Image/colors/21.jpg"
        name: "Sterling Silver"
        group: "metallic"
    };
    
    attribute glacierBlue: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/9.jpg"
        carUrl: "{__DIR__}Image/colors/23.jpg"
        name: "Glacier Blue"
        group: "metallic"
    };
    
    attribute obsidianBlack: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/12.jpg"
        carUrl: "{__DIR__}Image/colors/25.jpg"
        name: "Obsidian Black"
        group: "metallic"
    };
    
    attribute veryOrange: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/7.jpg"
        carUrl: "{__DIR__}Image/colors/20.jpg"
        name: "Very Orange"
        group: "premium"
    };
    attribute electricBlue: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/10.jpg"
        carUrl: "{__DIR__}Image/colors/22.jpg"
        name: "Electric Blue"
        group: "premium"
    };
    attribute arcticWhite: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/2.jpg"
        carUrl: "{__DIR__}Image/colors/16.jpg"
        name: "Arctic White"
        group: "premium"
    };
    
    attribute thunderGray: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/5.jpg"
        carUrl: "{__DIR__}Image/colors/21.jpg"
        name: "Thunder Gray"
        group: "premium"
    };
    
    attribute jetBlack: BodyColor = BodyColor {
        imageUrl: "{__DIR__}Image/colors/3.jpg"
        carUrl: "{__DIR__}Image/colors/17.jpg"
        name: "Jet Black"
        group: "premium"
    };
    
    attribute solidColors: BodyColor[] = bind [fusionRed, racingGreen] ;
    attribute metallicColors: BodyColor[] = bind
    [brilliantYellow, radiantRed, sterlingSilver, glacierBlue, obsidianBlack];
    attribute premiumColors: BodyColor[] = bind
    [veryOrange, electricBlue,arcticWhite, thunderGray, jetBlack];
    
    attribute colors: BodyColor[] = bind 
    [solidColors, metallicColors, premiumColors];
    
    attribute colorIndex: Number;
    attribute selectedColor: BodyColor;
    
    attribute solidGroup: Node;
    attribute metallicGroup: Node;
    attribute premiumGroup: Node;
    
    
    
    function composeNode():Node {
        return Group {
            content:
            [View {
                transform: Transform.translate(10, 10)
                content: Label {
                    foreground: Color.WHITE
                    font: Font.Font("Arial", ["PLAIN"], 12)
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
                //                    image: bind select Image{url: u} from u in selectedColor.carUrl
                },
                Group {
                    content:
                    [Rect {
                        height: 200
                        width: 500
                        arcHeight: 10
                        arcWidth: 10
                        fill: Color.rgba(.2, .2, .25, 1) as Paint
                    },
                    VBox {
                        transform: Transform.translate(20, 20)
                        content:
                        [Clip {
                            shape: Rect {height: 20, width: 400}
                            content:
                            HBox {
                                content:
                                [Text {
                                    content: "Exterior Color:"
                                    fill: Color.rgba(.8, .8, .8, 1) as Paint
                                },
                                Text {
                                    transform: bind Transform.translate(5, 0)
                                    content: bind "{selectedColor.name}"
                                    fill: Color.WHITE as Paint
                                },
                                Text {
                                    transform: bind Transform.translate(5, 0)
                                    content: bind "({selectedColor.group})"
                                    fill: Color.GRAY as Paint
                                }]
                            }
                        },
                        HBox {
                            transform: Transform.translate(0, 10)
                            content:
                            [VBox {
                                content:
                                [solidGroup = HBox {
                                    //                                    attribute: solidGroup
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
                                    stroke: Color.GRAY as Paint
                                    x1: 0
                                    x2: bind solidGroup.currentWidth
                                },
                                Text {
                                    font: Font.Font("Arial", ["PLAIN"], 9)
                                    fill: Color.GRAY as Paint
                                    transform: Transform.translate(0, 5)
                                    content: "Solid Colors"
                                }]
                            },
                            VBox {
                                transform: Transform.translate(10, 0)
                                content:
                                [metallicGroup = HBox {
                                    //                                    attribute: metallicGroup
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
                                    stroke: Color.GRAY as Paint
                                    x1: 0
                                    x2: bind metallicGroup.currentWidth
                                },
                                Text {
                                    font: Font.Font("Arial", ["PLAIN"], 9)
                                    fill: Color.GRAY as Paint
                                    transform: Transform.translate(0, 5)
                                    content: "Metallic Colors"
                                }]
                            },
                            VBox {
                                transform: Transform.translate(10, 0)
                                content:
                                [premiumGroup = HBox {
                                    //                                    attribute: premiumGroup
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
                                    stroke: Color.GRAY as Paint
                                    x1: 0
                                    x2: bind premiumGroup.currentWidth
                                },
                                Text {
                                    font: Font.Font("Arial", ["PLAIN"], 9)
                                    fill: Color.GRAY as Paint
                                    transform: Transform.translate(0, 5)
                                    content: "Premium Colors"
                                }]
                            }]
                        },
                        Text {
                            transform: Transform.translate(0, 15)
                            font: Font.Font("Verdana", ["BOLD"], 10)
                            content: 
                            "Which of our 12 different colors is your favorite? Bold or subtle? Metallic or non-
metallic? No matter what color you choose, you'll be proud of how green your
Tesla Roadster really is."
                            fill: Color.rgba(.8, .8, .8, 1) as Paint
                            
                        },
                        Text {
                            font: Font.Font("Arial", ["PLAIN"], 9)
                            transform: Transform.translate(0, 15)
                            content: 
                            "Please note that these colors are intended as a guide only and are not an exact match to the actual 
cars' colors."
                            fill: Color.GRAY as Paint
                        }]
                    }]
                }]
            }]
        }
    }
}

Canvas{
content: ColorSelector{}
}
