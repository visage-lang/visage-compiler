package studiomoto;
import java.lang.System;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class MotoBottomPanel extends Intro {
    attribute panelWidth: Number;
    attribute panelHeight: Number;
    attribute panelMargin: Number;
    attribute panels: Intro[] on replace { createTimeline(); }
    attribute promotions: Intro;
    attribute musicStuff: Intro;
    attribute insideMusic: Intro;
    attribute guitarPicks: Intro;
    attribute pf: PointerFactory = PointerFactory{};
    private attribute introAnim: Timeline;
    function createTimeline():Void {
        System.out.println("creating timeline: {sizeof panels}");
        introAnim  = Timeline {
        keyFrames: for (p in panels)
            [KeyFrame {
                keyTime: 0s
                keyValues: 
                    NumberValue {
                        target: bind pf.make(p.opacity).unwrap();
                        value: 0
                    }
            },
            KeyFrame {
                keyTime: 400ms * indexof p
                keyValues:
                    NumberValue {
                        target: bind pf.make(p.opacity).unwrap()
                        value: 1
                    }
                action: function() {
                     p.doIntro(); System.out.println("doing intro...");
                }
            }]
        
    }
}
    
    function doIntro():Void {
       introAnim.start();
    }  
    
    function composeNode():Node {  
        var group = Group {
            content:
            HBox {
                content:
                [promotions = MotoPanel {
                    height: bind panelHeight
                    width: bind panelWidth
                    title: Text {content: "Promotions", fill: Color.WHITE, font: Font.Font("VERDANA", ["PLAIN"], 14)}
                    content: PromotionsPanel{} as Node
                },
                Group {
                    var selection = /*bind*/ 0
                    content: 

                    [musicStuff = MotoPanel {
                        transform: bind Transform.translate(panelMargin, 0)
                        height: bind panelHeight
                        width: bind panelWidth
                        title: View {
                            content: Label {
                                text: "<html><div style='font-size:14pt;'><span style='color:white;'>Music</span><span style='color:yellow;'>Stuff</span></div></html>"
                            }
                        }  
                        content: MusicPanels {
                            selection: bind selection
                        } as Node
                    },
                    HBox {
                        opacity: bind musicStuff.opacity
                        transform: Transform.translate(220, 6)
                        // Major hack to work around IndexOutOfBoundsExceptions from sequence implementation
                        var nums:Group[] = []
                        var hovers:Boolean[] = bind [for (i in nums) i.hover, false, false, false]
                        content: nums = for (i in [1, 2, 3])
                        Group {
                            onMouseClicked: function(e) {selection = indexof i;}
                            cursor: Cursor.HAND
                            transform: Transform.translate(5, 0)
                            var fillColor:Color = bind if (selection == indexof i) Color.WHITE else Color.YELLOW
                            content:
                            [Rect {height: 15, width: 12, fill: Color.color(0, 0, 0, 0), selectable: true},
                            Text {content: "{i}", fill: bind fillColor, font: Font.Font("ARIAL", ["BOLD"], 11)},
                            Line {x1: -2, x2: 7, y1: 12, y2: 12, stroke: bind fillColor, visible: bind hovers[i-1]}]
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


Canvas {
    background: Color.BLACK
    
    var p = MotoBottomPanel {panelHeight: 200, panelWidth: 250, panelMargin: 15}
    content: [Rect {height: 300, width: 900, selectable:true fill: Color.BLACK, onMouseClicked: function(e) {p.doIntro();}}, p]
    
}

