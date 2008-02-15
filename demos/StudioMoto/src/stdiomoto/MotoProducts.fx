package studiomoto;
import javafx.ui.UIElement;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class Product {
    public attribute title: String;
    public attribute url: String;
}

public class MotoProducts extends MotoPanel {
    attribute products: Product*;
    attribute consoleY: Number;
    attribute phoneY: Number;
    attribute flipPhoneY: Number;
    attribute pdaY: Number;
    attribute headphonesY: Number;
    
    attribute consoleShadowY: Number;
    attribute flipPhoneShadowY: Number;
    attribute pdaShadowY: Number;
    attribute headphonesShadowY: Number;
    attribute phoneShadowY: Number;
    
    attribute consoleImage: Image;
    attribute phoneImage: Image;
    attribute flipPhoneImage: Image;
    attribute pdaImage: Image;
    attribute headphonesImage: Image;
    
    attribute shadow1: Image;
    attribute shadow2: Image;
    attribute shadow3: Image;
    attribute shadow4: Image;
    attribute introCount: Number;
    attribute productVisible: Boolean;
    function getImage(n:Integer): Image;
    operation makeDropStoryBoard(target:&Number, yStart:Number, yEnd:Number, bounce:Boolean): StoryBoard;
    attribute introAnim: KeyFrameAnimation;
}

function MotoProducts.getImage(n) = Image {url: "{__DIR__}/products/Image/{n}.png"};

attribute MotoProducts.shadow1 = getImage(1);
attribute MotoProducts.shadow2 = getImage(2);
attribute MotoProducts.shadow3 = getImage(3);
attribute MotoProducts.shadow4 = getImage(4);
attribute MotoProducts.consoleImage = getImage(5);
attribute MotoProducts.phoneImage = getImage(6);
attribute MotoProducts.flipPhoneImage = getImage(7);
attribute MotoProducts.pdaImage = getImage(8);
attribute MotoProducts.headphonesImage = getImage(9);
/*
attribute MotoProducts.consoleY  = 65;
attribute MotoProducts.phoneY = 50;
attribute MotoProducts.headphonesY = 20;
attribute MotoProducts.flipPhoneY = 50;
*/
attribute MotoProducts.consoleY  = -200;
attribute MotoProducts.phoneY = -200;
attribute MotoProducts.headphonesY = -200;
attribute MotoProducts.flipPhoneY = -200;
attribute MotoProducts.pdaY = -200;

attribute MotoProducts.consoleShadowY  = -200;
attribute MotoProducts.phoneShadowY = -200;
attribute MotoProducts.headphonesShadowY = -200;
attribute MotoProducts.flipPhoneShadowY = -200;
attribute MotoProducts.pdaShadowY = -200;

function MotoProducts.makeDropStoryBoard(target:&Number, start:Number, end:Number, bounce:Boolean) = StoryBoard {
         keyFrames:
	 [at (0s) {
	    	 *target => start;
	 },
	 if (bounce) 
	 then
	 after (1s) {
	    	 *target => end tween EASEBOTH;
	 }
         else 
	 after (1s) {
	    	 *target => end tween EASEBOTH;
	 }]
};

attribute MotoProducts.introAnim = KeyFrameAnimation {
	  keyFrames:
	  [at (0s) {
	      makeDropStoryBoard(& consoleShadowY, -200, 85, false);
	  },
	  at (.1s) {
	      makeDropStoryBoard(& consoleY, -200, 65, true);
	  },
	  at (.5s) {
	      makeDropStoryBoard(& phoneShadowY, -200,  120, false);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& phoneY, -200, 53, true);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& headphonesShadowY, -200, 70, false);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& headphonesY, -200, 18, true);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& pdaShadowY, -200, 70, false);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& pdaY, -200, -5, true);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& flipPhoneShadowY, -200, 117, false);
	  },
	  after (.1s) {
	      makeDropStoryBoard(& flipPhoneY, -200, 50, true);
	  },
	  at (.4s) {
	      productVisible => true;
	  },
	  after (5s) {}]
};

operation MotoProducts.doIntro() {
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

attribute MotoProducts.title = View {
    content: Label {
        text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Moto</span><span style='color:yellow;'>Products</span></div></html>"
    }
};

trigger on MotoProducts.productVisible = newValue {
//    println("product visible = {newValue}");
}

attribute MotoProducts.content = Group {
    ///var shadowImage = Image {url: "{__DIR__}/Image/
    content: HBox {
        transform: translate(0, 10)
        content:
        [VBox {
            content:
            [Text {
                font: {face: ARIAL, size: 11}
                content: "Get information on the latest Motorola products here."
                fill: white
            },
            Group {
                
                var open = bind false
                content:
                [Group {
                    trigger on (newValue = hover) {
                        if (not newValue) {
                            open = false;
                        }
                    }
                    content:
                    [ImageView {
                        visible: bind not open
                        cursor: HAND
                        onMouseClicked: operation(e) {open = true;}
                        image: {url: "{__DIR__}/Image/97.png"}
                    },
                    VBox {
                        visible: bind open
                        content:
                        [ImageView {              
                            cursor: DEFAULT
                            image: {url: "{__DIR__}/Image/99.png"}
                        },
                        Group {
                            transform: translate(11, -7.5)
                            
                            content:
                            [Rect {
                                selectable: true
                                height: 300
                                width: 225
                                fill: new Color(0, 0, 0, .8)
                            },
                            VBox {
                                transform: translate(5, 5)
                                var transparentFill = new Color(0, 0, 0, 0)
                                var margin = 3
                                var textColor = new Color(.8, .8, .8, 1)
                                var hoverTextColor:Color = yellow
                                var textFont = Font {face: ARIAL, size: 11}
                                content: bind foreach (p in products)
                                Group {
                                    var: row
                                    transform: translate(0, margin)
                                    var titleText = Text {
                                        content: bind p.title
                                        font: textFont
                                        fill: bind if row.hover then hoverTextColor else textColor
                                    }
                                    content:
                                    [Rect {
                                        cursor: HAND
                                        selectable: true
                                        height: 12//bind titleText.currentHeight
                                        width: 200
                                        fill: transparentFill
                                    },
                                    titleText]
                                }
                            }]
                        }]
                    },
                    Text {
                        content: "CHOOSE A PRODUCT"
                        transform: translate(20, 37/2)
                        valign: MIDDLE
                        fill: bind if open then black else white
                        font: {face: VERDANA, size: 11, style: BOLD}
                    }]
                }]
            }]
        },
        Group {
            opacity: bind if productVisible then 1 else 0
            
            content:
            [
            ImageView {
                transform: bind translate(-15, flipPhoneShadowY)
                image: bind shadow4
            },
            ImageView {
                transform: bind translate(0, flipPhoneY)
                image: bind flipPhoneImage
            },
            
            ImageView {
                transform: bind translate(5, consoleShadowY)
                image: bind shadow1
            },
            ImageView {
                transform: bind translate(30, consoleY)
                image: bind consoleImage
            },            
            
            ImageView {
                transform: bind translate(40, pdaShadowY)
                image: bind shadow3
            },
            ImageView {
                transform: bind translate(55, pdaY)
                image: bind pdaImage
            },
            
            ImageView {
                transform: bind translate(140, headphonesShadowY)
                image: bind shadow2
            },
            ImageView {
                transform: bind translate(140, headphonesY)
                image: bind headphonesImage
            },
            ImageView {
                transform: bind translate(240, phoneY)
                image: bind phoneImage
            },
            ImageView {
                transform: bind translate(225, phoneShadowY)
                image: bind shadow4
            }]
            
            
        }]
    }
};

Canvas {
    background: red
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

