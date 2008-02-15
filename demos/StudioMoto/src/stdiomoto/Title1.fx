package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;


public class Title1 extends CompositeNode {
    attribute label1: String;
    attribute label2: String;
    attribute label3: String;
    attribute logoGroup: Node?;
    attribute height: Number;
    attribute width: Number;
    attribute power: Node;
    attribute a: KeyFrameAnimation;
    attribute motorolaY: Number;
    attribute poweredByY: Number;
    attribute powerY: Number;
    
}

attribute Title1.a = KeyFrameAnimation {
    toggle: true
    keyFrames:
    [at (0s) {
        poweredByY => 0;
        motorolaY => 0;
        powerY => 0;
    },
    at (400ms) {
        poweredByY => power.currentHeight tween EASEBOTH;
        motorolaY => -power.currentHeight/2 tween EASEBOTH;
        powerY => power.currentHeight tween EASEBOTH;
    }]
};

function Title1.composeNode() =
Group {
    cursor: HAND
    var: mainGroup
    content:
    [Group {
        transform: translate(logoGroup.currentWidth/2, 0)
        content: Group {
            attribute: logoGroup
            
            content:
            [HBox {
                
                content: 
                [Group {
                    
                    var poweredBy = View {
                        content: Label {
                            font: new Font("Arial", "BOLD", 10)
                            text: bind label1 //"powered by", 
                            foreground: new Color(1, 1, 1, .5)
                        }
                        transform: bind translate(1, poweredByY)
                    },
                    var motorola = View {
                        content: Label {
                            font: new Font("Arial", "PLAIN", 18)
                            text: bind label2 //"Motorola", 
                            foreground: white 
                        }
                        transform: bind translate(1, motorolaY)
                        
                    },
                    var pow = View {
                        attribute: power
                        content: Label {
                            font: new Font("Arial", "BOLD", 10)
                            
                            text: bind label3 //"power"
                            foreground: yellow
                        }
                        transform: bind translate(1, -power.currentHeight + powerY)
                    }
                    content: 
                    [
                    Clip {
                        content: poweredBy
                        shape: Rect {height: bind poweredBy.currentHeight+2, width: bind poweredBy.currentWidth+2}
                        halign: CENTER
                    }, 
                    Clip {
                        transform: translate(0, poweredBy.currentHeight + 2 + motorola.currentHeight/2 + 2)
                        shape: Rect {height: bind power.currentHeight+2, width: bind power.currentWidth+2}
                        content: power
                        halign: CENTER
                    },
                    Clip {
                        transform: translate(0, poweredBy.currentHeight + 2)
                        halign: CENTER
                        content: 
                        [motorola]
                    //shape: Rect {height: bind motorola.currentHeight+2, width: bind motorola.currentWidth+2}
                    }]
                }]
                
            }]
        }
    },
    Rect {
        isSelectionRoot: true
        cursor: HAND
        height: bind height
        width:  bind width
        //stroke: black
        fill: new Color(0, 0, 0,  0)
        selectable: true
        var: rect
        trigger on (newValue = rect.hover) {
            a.start();
        }
    }]
};
