package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*; 
import javafx.ui.filter.*;
import java.lang.System;
import studiomoto.MotoMenuButton;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;
import javafx.lang.Time;


var frame:Frame;
var canvas:Canvas;
var home:HomeButton;
var pf: PointerFactory = PointerFactory{};
var base: java.net.URL = __DIR__;
frame = Frame {
    centerOnScreen: true
    onClose: function() {System.exit(0);}
    title: "JavaFX - Motorola Music"
    height: 700
    width: 1100
    visible: true 
    var selection = bind 0
    var splash = StudioMotoSplash {
        onDone: function() {selection = 0;}
    }    
    private attribute tshowing = bind showing on replace {
            selection = -1;
            splash.doSplash();
    };
    

    
    background: Color.BLACK
    content:
    canvas = Canvas {
        background: Color.BLACK
        var w = 1300
        var h = 700
        content:
        [Group {
            content:
            [HBox {
                content: 
                [ImageView {
                    image: Image{url: "{base}/Image/1.jpg"}
                },
                ImageView {
                    image: Image{url: "{base}/Image/1.jpg"}
                }]
                onMouseClicked: function(e) {splash.doSplash();}
            },
            Group {
                isSelectionRoot: true
                transform: Transform.translate(60, 70)
                content:
                [ImageView {
                    //982x527
                    //transform: Transform.translate(527/2, 40)
                    
                    //halign: HorizontalAlignment.CENTER
                    image: Image{url: "{base}/Image/4.png"}
                },
                Group {
                    transform: Transform.translate(30, 20)
                    content:
                    [Title1 {
                        height: 50
                        width: 150
                        label1: "<html><span style='font-size:12;color:#dddddd'>welcome to</span></html>"
                        label2: "<html><div style='font-size:28;'><span style='color:white'>studio</span><span style='color:yellow;font-weight:bold;'>moto</span></div></html>"
                        label3: "<html><span style='font-size:12;color:white'>welcome to</span></html>"
                    } as Node],
                },
                Group {
                    //transform: bind Transform.translate(canvas.width/2, h/2)
                    transform: Transform.translate(17, 50)
                    //halign: HorizontalAlignment.CENTER
                    //valign: MIDDLE, halign: HorizontalAlignment.CENTER
                    content: //if true then null else
                    [Group {
                        transform: Transform.translate(700, 0)
                        halign: HorizontalAlignment.CENTER
                        content: HBox {
                            clip: Clip{shape: Rect {y: -50, height: 100, width: w}}
                            var labels1 = ["about", "inside", "music", "moto", "site"]
                            var labels2 = ["studiomoto", "music", "playtime", "products", "support"]
                            var a = MotoMenuAnimation{}
                            var dummy = a.getNode()
                            content: 
                            [Group {
                                
                                isSelectionRoot: true
                                content:
                                [Rect {height: 30+68, width: 139, selectable: true, fill: Color.rgba(0, 0, 0, 0), visible: bind selection > 0},
                                (home = HomeButton {
                                    var ys = [[0..-18 step -1],[-18..-12]];
                                    var homeY:Number = 0;
                                    var tmp:Number = if(selection > 0) {
                                        var _homeY:Pointer = bind pf.make(homeY).unwrap(); 
                                        if(home.hover) {
                                            var seq = ys;
                                            var interval = 300/sizeof seq;
                                            var clip = Timeline {
                                                keyFrames: for(s in seq) {
                                                    KeyFrame {
                                                        keyTime: Time {millis: interval}
                                                        relative: true
                                                        keyValues: NumberValue {
                                                            target: _homeY
                                                            value: s
                                                        }
                                                    }
                                                }
                                             };
                                             clip.start();
                                        }else {
                                            var seq = reverse ys;
                                            var interval = 300/sizeof seq;
                                            var clip = Timeline {
                                                keyFrames: for(s in seq) {
                                                    KeyFrame {
                                                        keyTime: Time {millis: interval}
                                                        relative: true
                                                        keyValues: NumberValue {
                                                            target: _homeY
                                                            value: s
                                                        }
                                                    }
                                                }
                                             };
                                             clip.start();
                                        }
                                        0;
                                    } else {
                                        30;
                                    }
                                    transform: bind Transform.translate(-5, -10 + homeY)
                                    action: function() {selection = 0;}
                                }) as Node ]
                            },
                            
                            for (i in [0..sizeof labels1-1]) 
                            MotoMenuButton {
                                anim: a
                                action: function() {selection = indexof i+1;}
                                label1: labels1[i]
                                label2: labels2[i]
                                transform: Transform.translate(5, 0)
                            } as Node]
                        }
                    },            
                    Group {
                        transform: Transform.translate(0, 28)
                        content:
                        [Group {
                            content:
                            [ImageView {
                                image: Image{url: "{base}/Image/71.png"}
                            },
                            ImageView {
                                transform: Transform.translate(22, 17)
                                image: Image{url: "{base}/Image/72.png"}
                            },
                            MotoCenterPanel {
                                transform: Transform.translate(957/2+15, 20)
                                halign: HorizontalAlignment.CENTER
                                height: 220
                                width: 400
                            } as Node]
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
                    label1: "<html>powered by</html>"
                    label2: "<html>Motorola</html>"
                    label3: "<html>power</html>"
                } as Node,
                ImageView {
                    transform: Transform.translate(80, 0)
                    // 42x42
                    image: Image{url: "{base}/Image/5.png"}
                }]
            },
            MotoBottomPane {
                transform: Transform.translate(100, 400)
                selection: bind selection
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


