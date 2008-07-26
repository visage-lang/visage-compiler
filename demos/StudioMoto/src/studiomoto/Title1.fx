package studiomoto;

import java.lang.System;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.layout.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.ext.swing.*;
import javafx.animation.*;

class LabelWrapper extends CustomNode {
    public attribute label:Node;
    public function create():Node {
        label;
    }
}
public class Title1 extends CustomNode {
    attribute label1: Node;
    attribute label2: Node;
    attribute label3: Node;
    attribute logoGroup: Node;
    public attribute height:Integer;
    public attribute width:Integer;
    attribute power: Node;
    attribute motorolaY: Number;
    attribute poweredByY: Number;
    attribute powerY: Number;    
    attribute poweredHeight =  bind power.getHeight();
    attribute poweredHeight2 = bind -(power.getHeight()/2);    
    attribute timelineEnter: Timeline = bind Timeline {
        toggle: false // true
        keyFrames:
        [ KeyFrame {
            time: 0s
            values: [
                poweredByY => 0,
                motorolaY =>  0,
                powerY => 0
            ]
        },
        KeyFrame {
            time: 400ms
            values: [
                poweredByY => poweredHeight tween Interpolator.EASEBOTH,
                motorolaY =>  poweredHeight2 tween Interpolator.EASEBOTH,
                powerY => poweredHeight tween Interpolator.EASEBOTH
            ]            
        }]
    };
    attribute timelineExit: Timeline = bind Timeline {
        toggle: false // true
        keyFrames:
        [ KeyFrame {
            time: 0s
            values: [
                poweredByY => poweredHeight,
                motorolaY =>  poweredHeight2,
                powerY => poweredHeight,         

            ]
        },
        KeyFrame {
            time: 400ms
            values: [
                poweredByY => 0 tween Interpolator.EASEBOTH,
                motorolaY =>  0 tween Interpolator.EASEBOTH,
                powerY => 0 tween Interpolator.EASEBOTH
            ]            
        }]
    };    

    private attribute group:Group;
    private attribute hover:Boolean = bind group.isMouseOver() on replace {
        if(hover) {
            timelineEnter.start();
        }else {
            timelineExit.start();
        }
    };

    attribute title = this;
    function create():Node {
        power = LabelWrapper { 
            label:label3
            transform: bind Transform.translate(1, -power.getHeight() + powerY)
        }

        
        group = Group {
            var mainGroup = this;
            blocksMouse: true
            cursor: Cursor.HAND
            content:
            [Group {
                
                content: logoGroup = Group {
                    content: [HBox {
                        content: 
                        [Group {
                            var poweredBy = LabelWrapper {
                                label: label1
                                transform: bind Transform.translate(1, poweredByY)
                            };

                            var motorola = LabelWrapper {
                                label: label2;
                                transform: bind Transform.translate(1, motorolaY)
                            };

                            content: 
                            [
                            Group {
                                content: poweredBy
                                clip: Rectangle {height: bind poweredBy.getHeight()+2, width: bind poweredBy.getWidth()+2}
                                horizontalAlignment: HorizontalAlignment.CENTER
                            }, 
                            Group {
                                transform: bind Transform.translate(0, poweredBy.getHeight() + 2 + motorola.getHeight()/2 + 2)
                                clip: Rectangle {height: bind power.getHeight()+2, width: bind power.getWidth()+2}
                                content: power
                                horizontalAlignment: HorizontalAlignment.CENTER
                            },
                            Group {
                                transform: bind Transform.translate(0, poweredBy.getHeight() + 2)
                                horizontalAlignment: HorizontalAlignment.CENTER
                                content: 
                                [motorola]
                            //shape: Rectangle {height: bind motorola.getHeight()+2, width: bind motorola.getWidth()+2}
                            }]
                        }]

                    }]
                }
                transform: bind Transform.translate(logoGroup.getWidth()/2, 0)
            },
            Rectangle {
                cursor: Cursor.HAND
                height: bind title.height
                width:  bind title.width
                fill: Color.TRANSPARENT
            }]
        };

    }       
    
}

