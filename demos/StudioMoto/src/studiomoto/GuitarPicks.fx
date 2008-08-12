package studiomoto;
import javafx.scene.*;
import javafx.ext.swing.*;
import javafx.scene.transform.*;
import javafx.scene.image.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
import javafx.animation.*;


public class GuitarPicks extends Intro {
    /** HTML label for the top pick */
    public attribute label1: String;
    /** action when you click the top pick */
    public attribute action1: function();
    
    /** HTML label for the bottom pick */
    public attribute label2: String;
    /** action when you click the bottom pick */
    public attribute action2: function();
    
    attribute rot: Number; 
    attribute y1: Number;
    attribute y2: Number;
    
    attribute pick1: Node = Group {
        blocksMouse: true
        cursor: Cursor.HAND 
        
        transform: bind [Transform.translate(0, y1), Transform.rotate(rot, 30, 80)]
        
        onMouseClicked: function(e) { if (action1 != null) action1();}
        
        content:
        [ImageView {
            image: Image {url: "{__DIR__}Image/90.png"}
        },
        ComponentView {
            verticalAlignment: VerticalAlignment.CENTER, 
            horizontalAlignment: HorizontalAlignment.CENTER
            transform: Transform.translate(52, 50)
            component: Label {
                //TODO cursor: Cursor.HAND
                text: bind label1
            }
        }]
    };
    attribute pick2: Node =
        Group {
            blocksMouse: true
            cursor: Cursor.HAND
            onMouseClicked: function(e) { if(action2 != null) action2();}
            
            transform: bind [Transform.translate(0, 70+y2), Transform.rotate(360-rot, 30, 80)]
            
            content:
            [ImageView {
                image: Image {url: "{__DIR__}Image/91.png"}
            },
            ComponentView {
                verticalAlignment: VerticalAlignment.CENTER, 
                horizontalAlignment: HorizontalAlignment.CENTER
                transform: Transform.translate(52, 50)
                component: Label {
                    //TODO focusable: false
                    //TODO cursor: Cursor.HAND
                    text: bind label2
                }
            }]
    };
    
    //function doHover(pick:Node);
    
    attribute pick1Hover: Boolean = bind pick1.isMouseOver()
    on replace {
        if (pick1Hover)
            hoverAnim.start();
    };
    attribute pick2Hover: Boolean = bind pick2.isMouseOver()
    on replace {
        if (pick2Hover)
            hoverAnim.start();
    };
    
    attribute hoverAnim: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                time: 0s
                values: [ y1 => 0, y2 => 0 ]
             },
             KeyFrame {
                time: 250ms
                values: [y1 => -12 tween Interpolator.LINEAR, y2 => 12 tween Interpolator.LINEAR]
                action: function() {
                    //TODO this does not seem to work right, both picks end up behind the 
                    // background image.
                    //if (pick1Hover) pick1.toFront() else if (pick2Hover) pick2.toFront();
                    if (pick1Hover) pick1.toBack() else if (pick2Hover) pick2.toBack();
                }
             },
             KeyFrame {
                time: 500ms
                values: [y1 => 0 tween Interpolator.LINEAR, y2 => 0 tween Interpolator.LINEAR]
             }             
        ]
    };
    attribute introAnim: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                time: 0s
                values:  rot => 90
             },
             KeyFrame {
               time: 500ms
               values:  rot => 0 tween Interpolator.EASEBOTH
             }             
        ]
    };

    override function create() : Node {
        Group {
            content:  
            [ImageView {
                transform: Transform.translate(0, 40)
                image: Image {url: "{__DIR__}Image/89.png"}
            },
            pick2, pick1]
        };
    }

    override function doIntro() {
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
    Rectangle {
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
    ComponentView {
        component: SwingButton {
            icon: CanvasIcon {
                content: picks()
            }
        }
    };
}

// test it
picks();
