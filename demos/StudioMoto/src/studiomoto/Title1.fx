package studiomoto;
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
    attribute height: Number;
    attribute width: Number;
    attribute power: Node;
    attribute a: Timeline = Timeline {
        var pf: PointerFactory = PointerFactory{};
        var _poweredByY = pf.make(poweredByY).unwrap();
        var _motorolaY = pf.make(motorolaY).unwrap();
        var _powerY = pf.make(powerY).unwrap();
        toggle: true
        keyFrames:
        [ KeyFrame {
            keyTime: 0s
            keyValues:  [
                NumberValue {
                    target: _poweredByY
                    value: 0  
                },
               NumberValue {
                    target: _motorolaY
                    value: 0 
                },
               NumberValue {
                    target: _powerY
                    value: 0
                }
            ]
        },
        KeyFrame {
            keyTime: 400ms
            keyValues:  [
                NumberValue {
                    target: _poweredByY
                    value: power.currentHeight  
                    interpolate: NumberValue.EASEBOTH
                },
               NumberValue {
                    target: _motorolaY
                    value: -power.currentHeight/2
                    interpolate: NumberValue.EASEBOTH
                },
               NumberValue {
                    target: _powerY
                    value: power.currentHeight
                    interpolate: NumberValue.EASEBOTH
                }
            ]
        }]
    };
    attribute motorolaY: Number;
    attribute poweredByY: Number;
    attribute powerY: Number;
    
    function composeNode():Node {
        power = View {
            content: Label {
                font: Font.Font("Arial", ["BOLD"], 10)
                text: bind label3 //"power"
                foreground: Color.YELLOW
            }
            transform: bind Transform.translate(1, -power.currentHeight + powerY)
        };        
        Group {
            cursor: Cursor.HAND
            var mainGroup = this
            content:
            [Group {
                transform: Transform.translate(logoGroup.currentWidth/2, 0)
                content: logoGroup = Group {
                    content: [HBox {
                        content: 
                        [Group {
                            var poweredBy = View {
                                content: Label {
                                    font: Font.Font("Arial", ["BOLD"], 10)
                                    text: bind label1 //"powered by", 
                                    foreground: Color.rgba(1, 1, 1, .5)
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
                                transform: Transform.translate(0, poweredBy.currentHeight + 2 + motorola.currentHeight/2 + 2)
                                shape: Rect {height: bind power.currentHeight+2, width: bind power.currentWidth+2}
                                content: power
                                halign: HorizontalAlignment.CENTER
                            },
                            Clip {
                                transform: Transform.translate(0, poweredBy.currentHeight + 2)
                                halign: HorizontalAlignment.CENTER
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
                cursor: Cursor.HAND
                height: bind height
                width:  bind width
                //stroke: Color.BLACK
                fill: Color.rgba(0, 0, 0,  0)
                selectable: true
                var rect = this
                // TODO Trigger
                /***************
                trigger on (newValue = rect.hover) {
                    a.start();
                }
                 * ***********/
            }]
        };

    }       
    
}
