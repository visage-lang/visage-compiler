package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

public class MotoBottomPanel extends Intro {
    attribute panelWidth: Number;
    attribute panelHeight: Number;
    attribute panelMargin: Number;
    attribute panels: Intro*;
    attribute promotions: Intro;
    attribute musicStuff: Intro;
    attribute insideMusic: Intro;
    attribute guitarPicks: Intro;
    private attribute introAnim: KeyFrameAnimation;
}

attribute MotoBottomPanel.panels = bind [promotions, musicStuff, insideMusic, guitarPicks];

attribute MotoBottomPanel.introAnim = bind select KeyFrameAnimation {
    keyFrames:
    [at (0s) {
        foreach (p in panels) p.opacity => 0;
	trigger { println(" panels = {sizeof panels}"); }
    },
    foreach (p in panels) 
    after (200ms) {
        p.opacity => 1;
        trigger { p.doIntro(); println("doing intro..."); }
    }]
} from size in sizeof panels;

operation MotoBottomPanel.doIntro() {
    introAnim.start();
}

function MotoBottomPanel.composeNode() =
Group {
    content:
    HBox {
        content:
        [MotoPanel {
            attribute: promotions
            height: bind panelHeight
            width: bind panelWidth
            title: Text {content: "Promotions", fill: white, font: new Font("VERDANA", "PLAIN", 14)}
            content: 
            PromotionsPanel
        },
        Group {
            var selection = bind 0
            content: 
            
            [MotoPanel {
                
                attribute: musicStuff
                transform: bind translate(panelMargin, 0)
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
                transform: translate(220, 6)
                content:
                foreach (i in [1, 2, 3])
                Group {
                    onMouseClicked: operation(e) {selection = indexof i;}
                    var: num
                    cursor: HAND
                    transform: translate(5, 0)
                    var fillColor:Color = bind if selection == indexof i then white else yellow
                    content:
                    [Rect {height: 15, width: 12, fill: new Color(0, 0, 0, 0), selectable: true},
                    Text {content: "{i}", fill: bind fillColor, font: new Font("ARIAL", "BOLD", 11)},
                    Line {x1: -2, x2: 7, y1: 12, y2: 12, stroke: bind fillColor, visible: bind num.hover}]
                }
            }]
        },
        InsideMusicPane {height: 180, width: 250, attribute: insideMusic},
        GuitarPicks {
            attribute: guitarPicks
            transform: bind translate(panelMargin, 0)
            label1: "<html><div style='font-size:12;color:#dfd010;font-weight:bold'>register</div><div style='font-size:9;color:white'>free downloads<br>and Motorola<br>exclusives.<br><br><br></div></html>"
            label2: "<html><span style='font-size:12;color:#dfd010;font-weight:bold'>WAP</span><div style='font-size:9;color:white'><br>Coming Soon.<br></div></html>"
        }]
    }
};

Canvas {
    background: black
    
    var p = MotoBottomPanel {panelHeight: 200, panelWidth: 250, panelMargin: 15}
    content: [Rect {height: 300, width: 900, selectable:true fill: black, onMouseClicked: operation(e) {p.doIntro();}}, p]
    
}
