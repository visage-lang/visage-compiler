package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.animation.*;

public class MotoBottomPanel extends Intro {
    attribute panelWidth: Number;
    attribute panelHeight: Number;
    attribute panelMargin: Number;
    attribute panels: Intro[] = bind [promotions, musicStuff, insideMusic, guitarPicks];
    attribute promotions: Intro;
    attribute musicStuff: Intro;
    attribute insideMusic: Intro;
    attribute guitarPicks: Intro;
    attribute pf: PointerFactory = PointerFactory{};
    private attribute introAnim: Timeline = bind TimeLine {
        var size = sizeof panels;
        keyFrames: [
            KeyFrame {
                keyTime: 0s
                keyValues: for (p in panels) {
                    NumberValue {
                        target: pf.make(p.opacity).unwrap()
                        value: 0
                    }
                }
                action: function() {
                    println(" panels = {sizeof panels}");
                }
            }
            KeyFrame {
                keyTime: 200ms
                keyValues: for(p in panels) {
                    NumberValue {
                        target: pf.make(p.opacity).unwrap()
                        value: 1
                    }
                    action: function() {
                         p.doIntro(); println("doing intro...");
                    }
                }
            }
        ]
    };
    
    function doIntro():Void {
       introAnim.start();
    }  
    
    function composeNode():Node {  
        Group {
            content:
            HBox {
                content:
                [MotoPanel {
                    attribute: promotions
                    height: bind panelHeight
                    width: bind panelWidth
                    title: Text {content: "Promotions", fill: Color.WHITE, font: Font.Font("VERDANA", "PLAIN", 14)}
                    content: 
                    PromotionsPanel
                },
                Group {
                    var selection = bind 0
                    content: 

                    [MotoPanel {

                        attribute: musicStuff
                        transform: bind Transform.translate(panelMargin, 0)
                        height: bind panelHeight
                        width: bind panelWidth
                        title: View {
                            content: Label {
                                text: "<html><div style='font-size:14pt;'><span style='color:white;'>Music</span><span style='color:yellow;'>Stuff</span></div></html>"
                            }
                        }  
                        content: 
                        MusicPanels {

                            selection: bind selection

                        }
                    },
                    HBox {
                        opacity: bind musicStuff.opacity
                        transform: Transform.translate(220, 6)
                        content:
                        for (i in [1, 2, 3])
                        Group {
                            onMouseClicked: operation(e) {selection = indexof i;}
                            var num = this
                            cursor: Cursor.HAND
                            transform: Transform.translate(5, 0)
                            var fillColor:Color = bind if selection == indexof i then Color.WHITE else Color.YELLOW
                            content:
                            [Rect {height: 15, width: 12, fill: Color.rgba(0, 0, 0, 0), selectable: true},
                            Text {content: "{i}", fill: bind fillColor, font: Fonrt.Font("ARIAL", "BOLD", 11)},
                            Line {x1: -2, x2: 7, y1: 12, y2: 12, stroke: bind fillColor, visible: bind num.hover}]
                        }
                    }]
                },
                InsideMusicPane {height: 180, width: 250, attribute: insideMusic},
                GuitarPicks {
                    attribute: guitarPicks
                    transform: bind Transform.translate(panelMargin, 0)
                    label1: "<html><div style='font-size:12;color:#dfd010;font-weight:bold'>register</div><div style='font-size:9;color:white'>free downloads<br>and Motorola<br>exclusives.<br><br><br></div></html>"
                    label2: "<html><span style='font-size:12;color:#dfd010;font-weight:bold'>WAP</span><div style='font-size:9;color:white'><br>Coming Soon.<br></div></html>"
                }]
            }
        };

    }  
}



Canvas {
    background: Color.BLACK
    
    var p = MotoBottomPanel {panelHeight: 200, panelWidth: 250, panelMargin: 15}
    content: [Rect {height: 300, width: 900, selectable:true fill: Color.BLACK, onMouseClicked: function(e) {p.doIntro();}}, p]
    
}
