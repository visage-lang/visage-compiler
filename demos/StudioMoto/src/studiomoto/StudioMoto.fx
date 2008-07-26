package studiomoto;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.transform.*;
import javafx.scene.geometry.*;
import javafx.scene.text.*;
import javafx.ext.swing.*; 
import java.lang.System;
import studiomoto.MotoMenuButton;
import javafx.animation.*;
import javafx.lang.Duration;


var frame:SwingFrame;
var canvas:Canvas;

// Workaround for lack of local var trigger
class HomeModel {

    attribute ys = [[0..-18 step -1],[-18..-12]];
    attribute homeY:Number = 0;
    attribute interval = 300/sizeof ys;
    attribute homeSequence: Timeline = Timeline {
        var t = 0;
        keyFrames: for(s in reverse ys) {
            t += interval;
            KeyFrame {
                time: Duration.valueOf(t)
                values: homeY => s
            }
        }
     };
    attribute homeSequenceR:Timeline = Timeline {
        var t = 0;
        keyFrames: for(s in  ys) {
            t+= interval;
            KeyFrame {
                time: Duration.valueOf(t)
                values: homeY => s
            }
        }
    };
    attribute homeButton: HomeButton;
    attribute selection: Integer on replace {
        if (selection > 0) {
            if(homeButton.isMouseOver()) {
                homeSequence.start();
            } else {
                homeSequenceR.start();
            }
        }
    }
};


frame = SwingFrame {

    /** various bugs encounted with this attempt - which is itself a workaround for lack of local variable trigger: having these varibles at the top level gets undefined symbol $receiver
var homeY:Number = 0;
var selection = 0;
var home: HomeButton;

var homeModel = HomeModel {
    homeY: bind homeY with inverse;
    selection: bind selection with inverse;
    homeButton: bind home with inverse
};
*/

    // final workaround: change references to local var x to homeModel.x

    var homeModel = HomeModel {}

    // TODO centerOnScreen: true
    title: "JavaFX - Motorola Music"
    height: 700
    width: 1100
    visible: true;
    var splash = StudioMotoSplash {
        onDone: function() {homeModel.selection = 0;}
    }
    override attribute visible on replace {
        if(visible) {
            homeModel.selection = -1;
            splash.doSplash();
            homeModel.selection = 0;
        }
    };


    background: Color.BLACK
    content:
    canvas = Canvas {
        var w = 1300;
        var h = 700;
        background: Color.BLACK
        content:
        [Group {
            content:
            [HBox {
                content: 
                [ImageView {
                    image: Image{url: "{__DIR__}Image/1.jpg"}
                },
                ImageView {
                    image: Image{url: "{__DIR__}Image/1.jpg"}
                }]
                onMouseClicked: function(e) {splash.doSplash();}
            },
            Group {
                blocksMouse: true
                transform: Transform.translate(60, 70)
                content:
                [ImageView {
                    //982x527
                    //transform: Transform.translate(527/2, 40)
                    
                    //horizontalAlignment: HorizontalAlignment.CENTER
                    image: Image{url: "{__DIR__}Image/4.png"}
                },
                Group {
                    transform: Transform.translate(30, 20)
                    content:
                    [Title1 {
                        height: 50
                        width: 150
                        label1: Text {
                            textOrigin: TextOrigin.TOP
                            font: Font.font("Arial", FontStyle.BOLD, 12)
                            fill: Color.web("#DDDDDD", 1.0);
                            content: "welcome to"
                        }
                        label2: HBox {
                            content: [ Text {
                                    textOrigin: TextOrigin.TOP
                                    font: Font.font("Arial", FontStyle.PLAIN, 28)
                                    fill: Color.WHITE
                                    content: "studio"
                                },
                                Text {
                                    textOrigin: TextOrigin.TOP
                                    font: Font.font("Arial", FontStyle.BOLD, 28)
                                    fill: Color.YELLOW
                                    content: "moto"
                                }
                            ]
                        }
                        label3: Text {
                            textOrigin: TextOrigin.TOP
                            font: Font.font("Arial", FontStyle.BOLD, 12)
                            fill: Color.WHITE
                            content: "welcome to"
                        }
                    }],
                },
                Group {
                    //transform: bind Transform.translate(canvas.width/2, h/2)
                    transform: Transform.translate(17, 50)
                    //horizontalAlignment: HorizontalAlignment.CENTER
                    //verticalAlignment: MIDDLE, horizontalAlignment: HorizontalAlignment.CENTER
                    content: //if true then null else
                    [Group {
                        //transform: Transform.translate(700, 0) // original 
                        transform: Transform.translate(410, 0) 
                        horizontalAlignment: HorizontalAlignment.CENTER
                        content: HBox {
                            var labels1 = ["about", "inside", "music", "moto", "site"];
                            var labels2 = ["studiomoto", "music", "playtime", "products", "support"];
                            var a = MotoMenuAnimation{};
                            //var dummy = a.getNode();
                            clip: Rectangle {y: -50, height: 100, width: w}
                            content: 
                            [Group {
                                blocksMouse: true
                                content: 
                                [Rectangle {height: 30+68, width: 139, fill: Color.color(0, 0, 1, 0), visible: bind homeModel.selection > 0},
                                (homeModel.homeButton = HomeButton {
                                    // TODO moved var tmp up to Frame at the top. See note there.

                                    transform: bind Transform.translate(-5, -10  + (if (homeModel.selection == 0) 30 else homeModel.homeY))
                                    action: function() {homeModel.selection = 0;}
                                }) ]
                            },
                            
                            for (i in [0..<sizeof labels1]) 
                            MotoMenuButton {
                                anim: a
                                action: function() {homeModel.selection = indexof i+1;}
                                label1: labels1[i]
                                label2: labels2[i]
                                transform: Transform.translate(5, 0)
                            }]
                        }
                    },            
                    Group {
                        transform: Transform.translate(0, 28)
                        content:
                        [Group {
                            content:
                            [ImageView {
                                image: Image{url: "{__DIR__}Image/71.png"}
                            },
                            ImageView {
                                transform: Transform.translate(22, 17)
                                image: Image{url: "{__DIR__}Image/72.png"}
                            },
                            MotoCenterPanel {
                                transform: Transform.translate(957/2+15, 20)
                                horizontalAlignment: HorizontalAlignment.CENTER
                                height: 220
                                width: 400
                            }]
                        }]                    
                    }]
                }]
            },
            Group {
                transform: Transform.translate(875, 100)
                content:
                [Title1 {
                    height: 50
                    width: 130
                    label1: Text {
                        textOrigin: TextOrigin.TOP
                        font: Font.font("Arial", FontStyle.BOLD, 10)
                        fill: Color.color(1, 1, 1, .5)
                        content: "powered by"
                    }
                    label2: Text {
                        textOrigin: TextOrigin.TOP
                        font: Font.font("Arial", FontStyle.BOLD, 18)
                        fill: Color.WHITE
                        content: "Motorola"
                    }
                    label3: Text {
                        textOrigin: TextOrigin.TOP
                        font: Font.font("Arial", FontStyle.BOLD, 10)
                        fill: Color.YELLOW
                        content: "power"
                    }                    
                } ,
                ImageView {
                    transform: Transform.translate(80, 0)
                    // 42x42
                    image: Image{url: "{__DIR__}Image/5.png"}
                }]
            },
            MotoBottomPane {
                transform: Transform.translate(100, 400)
                selection: bind homeModel.selection
                panels:
                [MotoBottomPanel {
                    panelHeight: 170
                    panelWidth: 240
                    panelMargin: 15
                },
                About {
                    height: 180
                    width: 880
                },
                InsideMusic {
                    height: 180
                    width: 300
                },
                MusicPlaytime {
                    height: 180
                    width: 880
                },
                MotoProducts {
                    height: 180
                    width: 880
                },
                SiteSupport {
                    height: 180,
                    width: 250
                }]
            }]
        },
        Group {
            visible: bind splash.backgroundAlpha > .01
            content: splash as Node
        }]
    }
}


