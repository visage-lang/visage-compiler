package studiomoto;
import java.lang.System;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.ext.swing.*;
import javafx.animation.*;

public class MotoBottomPanel extends Intro {
    var panelWidth: Number;
    var panelHeight: Number;
    var panelMargin: Number;
    var panels: Intro[] on replace { createTimeline(); }
    var promotions: Intro;
    var musicStuff: Intro;
    var insideMusic: Intro;
    var guitarPicks: Intro;
    var introAnim: Timeline;
    function createTimeline():Void {
        introAnim  = Timeline {
        keyFrames: for (p in panels)
            [KeyFrame {
                time: 0s
                values: p.opacity => 0
            },
            KeyFrame {
                time: 400ms * indexof p
                values: p.opacity => 1
                action: function() {
                     p.doIntro(); 
                }
            }]
        
    }
}
    
    override function doIntro():Void {
       introAnim.start();
    }  
    
    override function create():Node {  
        var group = Group {
            content:
            HBox {
                content:
                [promotions = MotoPanel {
                    height: bind panelHeight
                    width: bind panelWidth
                    title: Text {
                        content: "Promotions", 
                        textOrigin: TextOrigin.TOP
                        fill: Color.WHITE, 
                        font: Font.font("VERDANA", FontStyle.PLAIN, 14)
                    }
                    content: PromotionsPanel{} as Node
                },
                Group {
                    var selection = /*bind*/ 0;
                    content: 

                    [musicStuff = MotoPanel {
                        transform: bind Transform.translate(panelMargin, 0)
                        height: bind panelHeight
                        width: bind panelWidth
                        title: ComponentView {
                            component: Label {
                                text: "<html><div style='font-size:14pt;'><span style='color:white;'>Music</span><span style='color:yellow;'>Stuff</span></div></html>"
                            }
                        }  
                        content: MusicPanels {
                            selection: bind selection
                        } as Node
                    },
                    HBox {
                        var nums:Group[] = [];
                        var hovers:Boolean[] = bind [for (i in nums) i.isMouseOver(), false, false, false];
                        opacity: bind musicStuff.opacity
                        transform: Transform.translate(220, 6)
                        // Major hack to work around IndexOutOfBoundsExceptions from sequence implementation
                        content: nums = for (i in [1, 2, 3])
                        Group {
                            var fillColor:Color = bind if (selection == indexof i) Color.WHITE else Color.YELLOW;
                            onMouseClicked: function(e) {selection = indexof i;}
                            cursor: Cursor.HAND
                            transform: Transform.translate(5, 0)
                            content:
                            [Rectangle {height: 15, width: 12, fill: Color.TRANSPARENT},
                            Text {
                                content: "{i}", 
                                textOrigin: TextOrigin.TOP
                                fill: bind fillColor, 
                                font: Font.font("ARIAL", FontStyle.BOLD, 11)
                            },
                            Line {startX: -2, endX: 7, startY: 12, endY: 12, stroke: bind fillColor, visible: bind hovers[i-1]}]
                        }
                    }]
                },
                insideMusic = InsideMusicPane {height: 180, width: 250},
                guitarPicks = GuitarPicks {
                    transform: bind Transform.translate(panelMargin, 0)
                    label1: "<html><div style='font-size:12;color:#dfd010;font-weight:bold'>register</div><div style='font-size:9;color:white'>free downloads<br>and Motorola<br>exclusives.<br><br><br></div></html>"
                    label2: "<html><span style='font-size:12;color:#dfd010;font-weight:bold'>WAP</span><div style='font-size:9;color:white'><br>Coming Soon.<br></div></html>"
                }]
            }
        };
        panels = [promotions, musicStuff, insideMusic, guitarPicks];
        group;
    }
}


function run() {
    Canvas {
        background: Color.BLACK;

        var p = MotoBottomPanel {panelHeight: 200, panelWidth: 250, panelMargin: 15};
        content: [Rectangle {height: 300, width: 900,  fill: Color.BLACK, onMouseClicked: function(e) {p.doIntro();}}, p]
    }
}

