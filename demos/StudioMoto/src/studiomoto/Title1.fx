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
    public var label:Node;
    override function create():Node {
        label;
    }
}
public class Title1 extends CustomNode {
    var label1: Node;
    var label2: Node;
    var label3: Node;
    var logoGroup: Node;
    public var height:Integer;
    public var width:Integer;
    var power: Node;
    var motorolaY: Number;
    var poweredByY: Number;
    var powerY: Number;    
    var poweredHeight =  bind power.getHeight();
    var poweredHeight2 = bind -(power.getHeight()/2);    
    var timelineEnter: Timeline = bind Timeline {
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
    var timelineExit: Timeline = bind Timeline {
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

    var group:Group;
    var hover:Boolean = bind group.isMouseOver() on replace {
        if(hover) {
            timelineEnter.start();
        }else {
            timelineExit.start();
        }
    };

    var title = this;
    override function create():Node {
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

