package studiomoto;
import javafx.ui.UIElement;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class Product {
    public attribute title: String;
    public attribute url: String;
}

public class MotoProducts extends MotoPanel {
    attribute products: Product[];
    attribute consoleY: Number  = -200;
    attribute phoneY: Number = -200;
    attribute flipPhoneY: Number = -200;
    attribute pdaY: Number = -200;
    attribute headphonesY: Number = -200;
    
    attribute consoleShadowY: Number  = -200;
    attribute flipPhoneShadowY: Number = -200;
    attribute pdaShadowY: Number = -200;
    attribute headphonesShadowY: Number = -200;
    attribute phoneShadowY: Number = -200;
    
    attribute consoleImage: Image = getImage(5);
    attribute phoneImage: Image = getImage(6);
    attribute flipPhoneImage: Image = getImage(7);
    attribute pdaImage: Image = getImage(8);
    attribute headphonesImage: Image = getImage(9);
    
    attribute shadow1: Image = getImage(1);
    attribute shadow2: Image = getImage(2);
    attribute shadow3: Image = getImage(3);
    attribute shadow4: Image = getImage(4);
    attribute introCount: Number;
    attribute productVisible: Boolean;
    
    
    function getImage(n:Integer): Image {
        Image {url: "{base}/products/Image/{n}.png"};
    }
    function makeDropStoryBoard(target:Pointer, start:Number, end:Number, bounce:Boolean): Timeline {
        Timeline {
             keyFrames: [
                    KeyFrame {
                        keyTime: 0s
                        keyValues:  NumberValue {
                            target: target;
                            value: start
                        }
                    },
                    //TODO What is different about bounce?
                    if(bounce) {
                        KeyFrame {
                            keyTime: 1s
                            keyValues:  NumberValue {
                                target: target;
                                value: end
                                interpolate: NumberValue.EASEBOTH
                            }
                        };
                    }else {
                        KeyFrame {
                            keyTime: 1s
                            keyValues:  NumberValue {
                                target: target;
                                value: end
                                interpolate: NumberValue.EASEBOTH
                            }
                        };
                    }
                ]
            };
    }
    
    
    private attribute __consoleShadowY = bind pf.make(consoleShadowY);
    private attribute _consoleShadowY = __consoleShadowY.unwrap();
    private attribute __consoleY = bind pf.make(consoleY);
    private attribute _consoleY = __consoleY.unwrap();
    private attribute __phoneShadowY = bind pf.make(phoneShadowY);
    private attribute _phoneShadowY = __phoneShadowY.unwrap();
    private attribute __phoneY = bind pf.make(phoneY);
    private attribute _phoneY = __phoneY.unwrap();
    private attribute __headphonesShadowY = bind pf.make(headphonesShadowY);
    private attribute _headphonesShadowY = __headphonesShadowY.unwrap();
    private attribute __headphonesY = bind pf.make(headphonesY);
    private attribute _headphonesY = __headphonesY.unwrap();
    private attribute __pdaShadowY = bind pf.make(pdaShadowY);
    private attribute _pdaShadowY = __pdaShadowY.unwrap();
    private attribute __pdaY = bind pf.make(pdaY);
    private attribute _pdaY = __pdaY.unwrap();
    private attribute __flipPhoneShadowY = bind pf.make(flipPhoneShadowY);
    private attribute _flipPhoneShadowY = __flipPhoneShadowY.unwrap();
    private attribute __flipPhoneY = bind pf.make(flipPhoneY);
    private attribute _flipPhoneY = __flipPhoneY.unwrap();
                  
                  
                  
    attribute introAnim: Timeline = Timeline {
         keyFrames:
              [KeyFrame {
                  keyTime: 0s
                  timelines: makeDropStoryBoard(_consoleShadowY, -200, 85, false);
              },
              KeyFrame {
                  keyTime: 100ms
                  timelines: makeDropStoryBoard(_consoleY, -200, 65, true);
              },
              KeyFrame {
                  keyTime: 500ms
                  timelines: makeDropStoryBoard(_phoneShadowY, -200,  120, false);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_phoneY, -200, 53, true);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_headphonesShadowY, -200, 70, false);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_headphonesY, -200, 18, true);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_pdaShadowY, -200, 70, false);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_pdaY, -200, -5, true);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_flipPhoneShadowY, -200, 117, false);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  timelines: makeDropStoryBoard(_flipPhoneY, -200, 50, true);
              },
              KeyFrame {
                  keyTime: 100ms
                  relative: true
                  action: function() {
                      productVisible = true;
                  }
              },
              KeyFrame {
                  keyTime: 5s
                  relative: true 
              }
              ]
    };

    function doIntro(){
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
        introAnim.start();
    }
    

    // NOTE: these were all var's in the original code, 
    // but they don't work that way in the interpreter, bug??
    private attribute open:Boolean = false;
    private attribute margin:Integer = 3;
    private attribute transparentFill = Color.rgba(0, 0, 0, 0);
    private attribute row:Group;
    private attribute group:Group;
    private attribute lhover:Boolean = bind group.hover on replace { 
        if(not lhover) {
            open = false;
        }
    };
    
    // From MotoPanel
    init {
        //TODO override
     title = View {
        content: Label {
            text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Moto</span><span style='color:yellow;'>Products</span></div></html>"
        }
    };
        
     content = Group {
        
        ///var shadowImage = Image {url: "{base}/Image/
        content: HBox {
            transform: Transform.translate(0, 10)
            content:
            [VBox {
                content:
                [Text {
                    font: Font{face: FontFace.ARIAL, size: 11}
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
                            image: Image{url: "{base}/Image/97.png"}
                        },
                        VBox {
                            visible: bind open
                            content:
                            [ImageView {              
                                cursor: Cursor.DEFAULT
                                image: Image{url: "{base}/Image/99.png"}
                            },
                            Group {
                                transform: Transform.translate(11, -7.5)

                                content:
                                [Rect {
                                    selectable: true
                                    height: 300
                                    width: 225
                                    fill: Color.rgba(0, 0, 0, .8)
                                },
                                VBox {
                                    
                                    transform: Transform.translate(5, 5)
                                    content: bind for (p in products) {
                                        row = Group {
                                            transform: Transform.translate(0, margin)
                                            content:
                                            [Rect {
                                                cursor: Cursor.HAND
                                                selectable: true
                                                height: 12//bind titleText.currentHeight
                                                width: 200
                                                fill: transparentFill
                                            },
                                            Text {
                                                content: bind p.title
                                                font: Font {face: FontFace.ARIAL, size: 11}
                                                fill: bind if (row.hover) then Color.YELLOW else Color.rgba(.8, .8, .8, 1)
                                            }]
                                        };
                                    }
                                }]
                            }]
                        },
                        Text {
                            content: "CHOOSE A PRODUCT"
                            transform: Transform.translate(20, 37/2)
                            valign: VerticalAlignment.MIDDLE
                            fill: bind if (open) then Color.BLACK else Color.WHITE
                            font: Font{face: FontFace.VERDANA, size: 11, style: FontStyle.BOLD}
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
    };
    
    }


}


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


