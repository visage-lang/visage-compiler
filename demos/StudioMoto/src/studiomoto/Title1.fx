package studiomoto;

import java.lang.System;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class Title1 extends CompositeNode {
    attribute label1: String;
    attribute label2: String;
    attribute label3: String;
    attribute logoGroup: Node;
    attribute height: Number = bind currentHeight;
    attribute width: Number = bind currentWidth;
    attribute power: Node;
    attribute motorolaY: Number;
    attribute poweredByY: Number;
    attribute powerY: Number;    
    private attribute pf: PointerFactory = PointerFactory{};
    private attribute _poweredByYp = bind pf.make(poweredByY);
    private attribute _poweredByY = _poweredByYp.unwrap();
    private attribute _motorolaYp = bind pf.make(motorolaY);
    private attribute _motorolaY = _motorolaYp.unwrap();
    private attribute _powerYp = bind pf.make(powerY);
    private attribute _powerY = _powerYp.unwrap();
    attribute a: Timeline = Timeline {

        toggle: false // true
        keyFrames:
        [ KeyFrame {
            keyTime: 0s
            keyValues:  [
                NumberValue {
                    target: _poweredByY
                    value: bind if(lhover) 0 else power.currentHeight
                },
               NumberValue {
                    target: _motorolaY
                    value: bind if(lhover) 0 else -(power.currentHeight/2)
                },
               NumberValue {
                    target: _powerY
                    value: bind if(lhover) 0 else power.currentHeight
                }
            ]
        },
        KeyFrame {
            keyTime: 400ms
            keyValues:  [
                NumberValue {
                    target: _poweredByY
                    value: bind if(lhover)  power.currentHeight else 0
                    interpolate: NumberValue.EASEBOTH
                },
               NumberValue {
                    target: _motorolaY
                    value: bind if(lhover)  -(power.currentHeight/2) else 0
                    interpolate: NumberValue.EASEBOTH
                },
               NumberValue {
                    target: _powerY
                    value: bind if(lhover)  power.currentHeight else 0
                    interpolate: NumberValue.EASEBOTH
                }
            ]
        }]
    };

    private attribute rect:Rect;
    private attribute lhover:Boolean = bind rect.hover on replace {
        a.start();
    };
    function composeNode():Node {
        power = View {
            content: Label {
                font: Font.Font("Arial", ["BOLD"], 10)
                text: bind label3 //"power"
                foreground: Color.YELLOW
            }
            transform: bind Transform.translate(1, -power.currentHeight + powerY)
        };  
        var self = this;
        Group {
            cursor: Cursor.HAND
            var mainGroup = this
            content:
            [Group {
                
                content: logoGroup = Group {
                    content: [HBox {
                        content: 
                        [Group {
                            var poweredBy = View {
                                content: Label {
                                    font: Font.Font("Arial", ["BOLD"], 10)
                                    text: bind label1 //"powered by", 
                                    foreground: Color.color(1, 1, 1, .5)
                                }
                                transform: bind Transform.translate(1, poweredByY)
                            },
                            var motorola = View {
                                content: Label {
                                    font: Font.Font("Arial", ["PLAIN"], 18)
                                    text: bind label2 //"Motorola", 
                                    foreground: Color.WHITE 
                                }
                                transform: bind Transform.translate(1, motorolaY)

                            };
                            

                            content: 
                            [
                            Clip {
                                content: poweredBy
                                shape: Rect {height: bind poweredBy.currentHeight+2, width: bind poweredBy.currentWidth+2}
                                halign: HorizontalAlignment.CENTER
                            }, 
                            Clip {
                                transform: bind Transform.translate(0, poweredBy.currentHeight + 2 + motorola.currentHeight/2 + 2)
                                shape: Rect {height: bind power.currentHeight+2, width: bind power.currentWidth+2}
                                content: power
                                halign: HorizontalAlignment.CENTER
                            },
                            Clip {
                                transform: bind Transform.translate(0, poweredBy.currentHeight + 2)
                                halign: HorizontalAlignment.CENTER
                                content: 
                                [motorola]
                            //shape: Rect {height: bind motorola.currentHeight+2, width: bind motorola.currentWidth+2}
                            }]
                        }]

                    }]
                }
                transform: bind Transform.translate(logoGroup.currentWidth/2, 0)
            },
            rect = Rect {
                isSelectionRoot: true
                cursor: Cursor.HAND
                height: bind self.height
                width:  bind self.width
                //stroke: Color.BLACK
                fill: Color.color(0, 0, 0,  0)
                selectable: true

            }]
        };

    }       
    
}

