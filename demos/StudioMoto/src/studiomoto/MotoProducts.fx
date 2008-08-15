package studiomoto;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

import javafx.ext.swing.*;
import javafx.animation.*;
import java.lang.System;

public class Product {
    public var title: String;
    public var url: String;
}

public class MotoProducts extends MotoPanel {
    var products: Product[];
    var consoleY: Number  = -200;
    var phoneY: Number = -200;
    var flipPhoneY: Number = -200;
    var pdaY: Number = -200;
    var headphonesY: Number = -200;
    
    var consoleShadowY: Number  = -200;
    var flipPhoneShadowY: Number = -200;
    var pdaShadowY: Number = -200;
    var headphonesShadowY: Number = -200;
    var phoneShadowY: Number = -200;
    
    var consoleImage: Image = getImage(5);
    var phoneImage: Image = getImage(6);
    var flipPhoneImage: Image = getImage(7);
    var pdaImage: Image = getImage(8);
    var headphonesImage: Image = getImage(9);
    
    var shadow1: Image = getImage(1);
    var shadow2: Image = getImage(2);
    var shadow3: Image = getImage(3);
    var shadow4: Image = getImage(4);
    var introCount: Number;
    var productVisible: Boolean;
    
    
    function getImage(n:Integer): Image {
        Image {url: "{__DIR__}products/Image/{n}.png"};
    }
    function makeDropStoryBoard(target:Number, start:Number, end:Number, bounce:Boolean): Timeline {
        Timeline {
             keyFrames: [
                    KeyFrame {
                        time: 0s
                        values: target => start
                    },
                    //TODO What is different about bounce?
                    if(bounce) {
                        KeyFrame {
                            time: 1s
                            values: target => end tween Interpolator.EASEBOTH
                        };
                    }else {
                        KeyFrame {
                            time: 1s
                            values: target => end tween Interpolator.EASEBOTH
                        };
                    }
                ]
            };
    }
    
    
                  
    var introAnim: Timeline = bind Timeline {
         keyFrames:
              [KeyFrame {
                  time: 0s
                  values: [
                      consoleShadowY => -200,
                      consoleY => -200,
                      phoneShadowY => -200,
                      phoneY => -200,
                      headphonesShadowY => -200,
                      headphonesY => -200,
                      pdaShadowY => -200,
                      pdaY => -200,
                      flipPhoneShadowY => -200,
                      flipPhoneY => -200,
                  ]
              },
              KeyFrame {
                  time: 100ms
                  values: [
                      consoleShadowY => -200,
                  ]
              },
              KeyFrame {
                  time: 200ms
                  values: [
                      consoleY => -200,
                  ]
              },  
              KeyFrame {
                  time: 300ms
                  values: [
                      phoneShadowY => -200,
                      
                  ]
              },   
              KeyFrame {
                  time: 500ms
                  values: [
                      phoneY => -200,
                      headphonesShadowY => -200,
                  ]
              },  
              KeyFrame {
                  time: 700ms
                  values: [
                      pdaShadowY => -200,
                  ]
              },               
              KeyFrame {
                  time: 800ms
                  values: [
                      headphonesY => -200,
                  ]
              },  
              KeyFrame {
                  time: 900ms
                  values: [
                      pdaY => -200,
                      flipPhoneShadowY => -200,
                      consoleShadowY => 85 tween Interpolator.EASEBOTH
                  ]
              },               
              KeyFrame {
                  time: 1000ms
                  values: [
                      consoleY => 65 tween Interpolator.EASEBOTH
                  ]
              },
              KeyFrame {
                  time: 1100ms
                  values: [
                      phoneShadowY => 120 tween Interpolator.EASEBOTH
                  ]
              },   
              KeyFrame {
                  time: 1300ms
                  values: [
                      phoneY => 53 tween Interpolator.EASEBOTH,
                      headphonesShadowY => 70 tween Interpolator.EASEBOTH,
                  ]
              },   
              KeyFrame {
                  time: 1500ms
                  values: [
                      pdaShadowY => 70 tween Interpolator.EASEBOTH,
                  ]
              },              
              KeyFrame {
                  time: 1600ms
                  values: [
                      headphonesY => 18 tween Interpolator.EASEBOTH,
                  ]
              },  
              KeyFrame {
                  time: 1700ms
                  values: [
                      pdaY => -5 tween Interpolator.EASEBOTH,
                      flipPhoneShadowY => 117 tween Interpolator.EASEBOTH
                  ]
              },
              KeyFrame {
                  time: 1900ms
                  values: [
                      flipPhoneY => 50 tween Interpolator.EASEBOTH
                  ]
              },              
              ]
    };

    override function doIntro(){
        productVisible = false;

        consoleY = -200;
        phoneY = -200;
        flipPhoneY = -200;
        pdaY = -200;
        headphonesY = -200;

        consoleShadowY = -200;
        flipPhoneShadowY = -200;
        pdaShadowY = -200;
        headphonesShadowY = -200;
        phoneShadowY = -200;
        super.doIntro();
        productVisible = true;
        introAnim.start();
    }
    

    // NOTE: these were all var's in the original code, 
    // but they don't work that way in the interpreter, bug??
    var open:Boolean = false;
    var margin:Integer = 3;
    var transparentFill = Color.TRANSPARENT;
    var group:Group;
    var lhover:Boolean = bind group.isMouseOver() on replace { 
        if(not lhover) {
            open = false;
        }
    };
    
    // From MotoPanel
     override var title = ComponentView {
        component: Label {
            text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Moto</span><span style='color:yellow;'>Products</span></div></html>"
        }
    };
        
      override var content = Group {
        content: HBox {
            transform: Transform.translate(0, 10)
            content:
            [VBox {
                content:
                [Text {
                    textOrigin: TextOrigin.TOP
                    font: Font{name: "ARIAL", size: 11}
                    content: "Get information on the latest Motorola products here."
                    fill: Color.WHITE
                },
                Group {
                    content:
                    [group = Group {
                        content:
                        [ImageView {
                            //TODO
                            visible: bind not open
                            cursor: Cursor.HAND
                            onMouseClicked: function(e) {open = true;}
                            image: Image{url: "{__DIR__}Image/97.png"}
                        },
                        VBox {
                            visible: bind open
                            content:
                            [ImageView {              
                                cursor: Cursor.DEFAULT
                                onMouseClicked: function(e) {open = false;}
                                image: Image{url: "{__DIR__}Image/99.png"}
                            },
                            Group {
                                transform: Transform.translate(11, -7.5)

                                content:
                                [Rectangle {
                                    height: 300
                                    width: 225
                                    fill: Color.color(0, 0, 0, .8)
                                },
                                VBox {
                                    transform: Transform.translate(5, 5)
                                    content: bind for (p in products) {
                                        var row:Group;
                                        row = Group {
                                            transform: Transform.translate(0, margin)
                                            content:
                                            [Rectangle {
                                                cursor: Cursor.HAND
                                                height: 12//bind titleText.currentHeight
                                                width: 200
                                                fill: transparentFill
                                            },
                                            Text {
                                                textOrigin: TextOrigin.TOP
                                                content: bind p.title
                                                font: Font {name: "ARIAL", size: 11}
                                                fill: bind if (row.isMouseOver()) then Color.YELLOW else Color.color(.8, .8, .8, 1)
                                            }]
                                        };
                                    }
                                }]
                            }]
                        },
                        Text {
                            textOrigin: TextOrigin.TOP
                            content: "CHOOSE A PRODUCT"
                            transform: Transform.translate(20, 37/2)
                            verticalAlignment: VerticalAlignment.CENTER
                            fill: bind if (open) then Color.BLACK else Color.WHITE
                            font: Font{name: "VERDANA", size: 11, style: FontStyle.BOLD}
                        }]
                    }]
                }]
            },
            Group {
                opacity: bind if (productVisible) then 1 else 0

                content:
                [
                ImageView {
                    transform: bind Transform.translate(-15, flipPhoneShadowY)
                    image: bind shadow4
                },
                ImageView {
                    transform: bind Transform.translate(0, flipPhoneY)
                    image: bind flipPhoneImage
                },

                ImageView {
                    transform: bind Transform.translate(5, consoleShadowY)
                    image: bind shadow1
                },
                ImageView {
                    transform: bind Transform.translate(30, consoleY)
                    image: bind consoleImage
                },            

                ImageView {
                    transform: bind Transform.translate(40, pdaShadowY)
                    image: bind shadow3
                },
                ImageView {
                    transform: bind Transform.translate(55, pdaY)
                    image: bind pdaImage
                },

                ImageView {
                    transform: bind Transform.translate(140, headphonesShadowY)
                    image: bind shadow2
                },
                ImageView {
                    transform: bind Transform.translate(140, headphonesY)
                    image: bind headphonesImage
                },
                ImageView {
                    transform: bind Transform.translate(240, phoneY)
                    image: bind phoneImage
                },
                ImageView {
                    transform: bind Transform.translate(225, phoneShadowY)
                    image: bind shadow4
                }]


            }]
        }
    }
}


function run( ) {
    Canvas {
        background: Color.RED
        content: MotoProducts {
            height: 220
            width: 1000
            products:
            [Product {
                title: "Product 1"
            },
            Product {
                title: "Product 2"
            }]
        }
    }
}


