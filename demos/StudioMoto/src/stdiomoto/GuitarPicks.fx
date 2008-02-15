package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

public class GuitarPicks extends Intro {
    /** HTML label for the top pick */
    public attribute label1: String;
    /** action when you click the top pick */
    public attribute action1: function();
    
    /** HTML label for the bottom pick */
    public attribute label2: String;
    /** action when you click the bottom pick */
    public attribute action2: function();
    
    private attribute rot: Number; 
    private attribute y1: Number;
    private attribute y2: Number;
    
    private attribute pick1: Node = Group {
        isSelectionRoot: true
        cursor: HAND 
        
        transform: bind [translate(0, y1), rotate(rot, 30, 80)]
        
        onMouseClicked: function(e) {(this.action1)();}
        
        content:
        [ImageView {
            image: Image {url: "{__DIR__}/Image/90.png"}
        },
        View {
            valign: MIDDLE, halign: CENTER
            transform: translate(52, 50)
            content: SimpleLabel {
                cursor: HAND
                text: bind label1
            }
        }]
    };
    private attribute pick2: Node =
        Group {
            isSelectionRoot: true
            cursor: HAND
            onMouseClicked: function(e) {(this.action2)();}
            
            transform: bind [translate(0, 70+y2), rotate(360-rot, 30, 80)]
            
            content:
            [ImageView {
                image: Image {url: "{__DIR__}/Image/91.png"}
            },
            View {
                valign: MIDDLE, halign: CENTER
                transform: translate(52, 50)
                content: SimpleLabel {
                    focusable: false
                    cursor: HAND
                    text: bind label2
                }
            }]
    };
    
    private function doHover(pick:Node);
    
    private attribute pick1Hover: Boolean = bind pick1.hover
    on replace {
        if (pick1Hover)
            hoverAnim.start();
    };
    private attribute pick2Hover: Boolean = bind pick2.hover
    on replace {
        if (pick2Hover)
            hoverAnim.start();
    };
    
    attribute hoverAnim: KeyFrameAnimation = KeyFrameAnimation {
        keyFrames:
        [at (0s) {
            y2 => 0;
            y1 => 0;
        },
        at (.25s) {
            y2 => 12 tween LINEAR;
            y1 => -12 tween LINEAR;
            trigger { if (pick1.hover) pick1.toFront()else if (pick2.hover) pick2.toFront() }
        },
        at (.5s) {
            y2 => 0 tween LINEAR;
            y1 => 0 tween LINEAR;
            
        }]
    };
    attribute introAnim: KeyFrameAnimation = KeyFrameAnimation {
        keyFrames:
        [at (0s) {
            rot => 90;
        },
        at (.5s) {
            rot => 0 tween EASEBOTH;
        }]
    };

    function composeNode() : Node {
        Group {
            content:  
            [ImageView {
                transform: translate(0, 40)
                image: Image {url: "{__DIR__}/Image/89.png"}
            },
            pick2, pick1]
    };

    function doIntro() {
        introAnim.start();
    }
}

// Example code

function picks() =
GuitarPicks {   
    var: self
    action1: operation() { self.doIntro(); }
    label1: "<html><div style='font-size:12;color:#dfd010;font-weight:bold'>register</div><div style='font-size:9;color:white'>free downloads<br>and Motorola<br>exclusives.<br><br><br></div></html>"
    label2: "<html><span style='font-size:12;color:#dfd010;font-weight:bold'>WAP</span><div style='font-size:9;color:white'><br>Coming Soon.<br></div></html>"
};



function pickPattern() = 

Rect {
    arcHeight: 30
    arcWidth: 30
    stroke: black
    strokeWidth: 3
    transform: translate(10, 0)
    height: 300
    width: 330
    fill: Pattern {
        content: picks()
    }
};


function pickIcon() =

View {
    content: Button {
        icon: CanvasIcon {
            content: picks()
        }
    }
};

// test it
picks();