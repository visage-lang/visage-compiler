package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;


public class GuitarPicks extends Intro {
    attribute base: java.net.URL; // work around for __DIR__
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
        cursor: Cursor.HAND 
        
        transform: bind [Transform.translate(0, y1), Transform.rotate(rot, 30, 80)]
        
        onMouseClicked: function(e) {(this.action1)();}
        
        content:
        [ImageView {
            image: Image {url: "{base}/Image/90.png"}
        },
        View {
            valign: VerticalAlignment.MIDDLE, halign: HorizontalAlignment.CENTER
            transform: Transform.translate(52, 50)
            content: SimpleLabel {
                cursor: Cursor.HAND
                text: bind label1
            }
        }]
    };
    private attribute pick2: Node =
        Group {
            isSelectionRoot: true
            cursor: Cursor.HAND
            onMouseClicked: function(e) {(this.action2)();}
            
            transform: bind [Transform.translate(0, 70+y2), Transform.rotate(360-rot, 30, 80)]
            
            content:
            [ImageView {
                image: Image {url: "{base}/Image/91.png"}
            },
            View {
                valign: VerticalAlignment.MIDDLE, halign: HorizontalAlignment.CENTER
                transform: Transform.translate(52, 50)
                content: SimpleLabel {
                    focusable: false
                    cursor: Cursor.HAND
                    text: bind label2
                }
            }]
    };
    
    //private function doHover(pick:Node);
    
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
    
    private attribute pf: PointerFactory = PointerFactory{};
    private attribute __y1 = bind pf.make(y1);
    private attribute _y1 = __y1.unwrap();
    private attribute __y2 = bind pf.make(y2);
    private attribute _y2 = __y2.unwrap();    
    attribute hoverAnim: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _y1
                        value: 0
                    },
                    NumberValue {
                        target: _y2
                        value: 0
                    }
                ]
             },
             KeyFrame {
                keyTime: 250ms
                keyValues:  [
                    NumberValue {
                        target: _y1
                        value: -12
                        interpolate: NumberValue.LINEAR
                    },
                    NumberValue {
                        target: _y2
                        value: 12
                        interpolate: NumberValue.LINEAR
                    }
                ]
                action: function() {
                    if (pick1.hover) pick1.toFront()else if (pick2.hover) pick2.toFront();
                }
             },
             KeyFrame {
                keyTime: 500ms
               keyValues:  [
                    NumberValue {
                        target: _y1
                        value: 0
                        interpolate: NumberValue.LINEAR
                    },
                    NumberValue {
                        target: _y2
                        value: 0
                        interpolate: NumberValue.LINEAR
                    }
                ]
             }             
        ]
    };
    private attribute __rot = bind pf.make(rot);
    private attribute _rot = __rot.unwrap();
    attribute introAnim: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _rot
                        value: 90
                    }
                ]
             },
             KeyFrame {
               keyTime: 500ms
               keyValues:  [
                    NumberValue {
                        target: _rot
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    }
                ]
             }             
        ]
    };

    function composeNode() : Node {
        Group {
            content:  
            [ImageView {
                transform: Transform.translate(0, 40)
                image: Image {url: "{base}/Image/89.png"}
            },
            pick2, pick1]
        };
    }

    function doIntro() {
        introAnim.start();
    }
}

// Example code

function picks():GuitarPicks {
    var gp:GuitarPicks;
    gp = GuitarPicks {   
        action1: function() { gp.doIntro(); }
        label1: "<html><div style='font-size:12;color:#dfd010;font-weight:bold'>register</div><div style='font-size:9;color:white'>free downloads<br>and Motorola<br>exclusives.<br><br><br></div></html>"
        label2: "<html><span style='font-size:12;color:#dfd010;font-weight:bold'>WAP</span><div style='font-size:9;color:white'><br>Coming Soon.<br></div></html>"
    };
}



function pickPattern(){
    Rect {
        arcHeight: 30
        arcWidth: 30
        stroke: Color.BLACK
        strokeWidth: 3
        transform: Transform.translate(10, 0)
        height: 300
        width: 330
        fill: Pattern {
            content: picks()
        }
    };
}


function pickIcon() {
    View {
        content: Button {
            icon: CanvasIcon {
                content: picks()
            }
        }
    };
}

// test it
picks();
