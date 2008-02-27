package studiomoto;
import java.lang.System;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class MotoMenuButton extends CompositeNode {
    public attribute anim: MotoMenuAnimation;
    public attribute label1: String;
    public attribute label2: String;
    public attribute action: function();
    attribute mouseOver: Boolean = bind rect.hover on replace {
        a.start();
        if (mouseOver) {
            anim.start();
        } else {
            anim.stop();
        }        
    };
    private attribute pf: PointerFactory = PointerFactory{};
    private attribute _y = bind pf.make(y).unwrap();
    attribute a: Timeline = Timeline {
        
        toggle: true
        keyFrames: [
            KeyFrame {
                keyTime: 0s
                keyValues:  NumberValue {
                    target: _y;
                    value: 0
                }
            },
            KeyFrame {
                keyTime: 200ms
                keyValues:  NumberValue {
                    target: _y;
                    value: -12
                    interpolate: NumberValue.EASEBOTH
                } 
            },
            KeyFrame {
                keyTime: 400ms
                keyValues:  NumberValue {
                    target: _y;
                    value: -10
                    interpolate: NumberValue.EASEBOTH
                }
            }
        ]
    };

    attribute y: Number;
    private attribute group:Group;
    private attribute rect:Rect;
    
    function composeNode():Node {
        Group {
            content:
            [group = Group {
                cursor: Cursor.HAND
                var w = 110
                var h = 60
                transform: bind Transform.translate(0, y)
                content: /** bind **/
                [rect = Rect {
                    selectable: true
                    width: w
                    height: h
                    fill: Color.BLACK
                    arcHeight: 20
                    arcWidth: 20
                    onMouseClicked: function(e) {if(action <> null) action();}
                },
                Rect {
                    clip: Clip{shape: Rect {height: h*.25, width: w}}
                    width: w
                    height: h*.8
                    fill: LinearGradient {
                        endX: 0, endY: h*.8
                        stops:
                        [Stop {
                            offset: 0
                            color: Color.color(.7, .7, .7, 1)
                        },
                        Stop {
                            offset: 0.05
                            color: Color.color(.2, .2, .2, 1)
                        },
                        Stop {
                            offset: 0.5
                            color: Color.color(.1, .1, .1, 1)
                        }]
                    }
                    arcHeight: 20
                    arcWidth: 20
                },
                Group {
                    visible: bind not mouseOver    
                    transform: Transform.translate(30, h*.3)
                    valign: VerticalAlignment.MIDDLE
                    content:
                    VBox {
                        var textColor = Color.ORANGERED
                        var font1 = Font {face: FontFace.ARIAL, style: [FontStyle.PLAIN] size: 12}
                        var font = Font {face: FontFace.ARIAL, style: [FontStyle.BOLD] size: 12}
                        content:
                        [Text {
                            font: font1
                            fill: Color.ORANGE
                            content: bind label1
                        },
                        Text {
                            transform: Transform.translate(0, 3)
                            fill: Color.WHITE
                            font: font
                            content: bind label2
                        }]
                    }
                },
                Group {
                    var empty:Node[] = [];
                    visible: bind mouseOver
                    content: bind 
                    [Group {
                        transform: Transform.translate(w -5, h*.4)
                        valign: VerticalAlignment.MIDDLE, 
                        halign: HorizontalAlignment.TRAILING
                        content: bind if (mouseOver) then [anim as Node] else empty
                    },
                    HBox {
                        transform: Transform.translate(w-5, h*.7)
                        valign: VerticalAlignment.CENTER, halign: HorizontalAlignment.TRAILING
                        var textColor = Color.ORANGERED
                        var font = Font {face: FontFace.ARIAL, style: [FontStyle.BOLD] size: 8}
                        content:
                        [Text {
                            font: font
                            fill: Color.ORANGE
                            content: bind label1.toUpperCase()
                        },
                        Text {
                            transform: Transform.translate(2, 0)
                            fill: Color.WHITE
                            font: font
                            content: bind label2.toUpperCase()
                        }]
                    }]
                },
                Group {
                    transform: Transform.translate(10, 5)
                    content:
                    [
                    ImageView {
                        image: Image {url: "{__DIR__}Image/7.png"}
                    },
                    ImageView {
                        opacity: bind if (mouseOver) then 0.5 else 0
                        transform: Transform.translate(-3, -3)
                        image: Image {url: bind "{__DIR__}Image/8.png"}
                    }]
                }]
            }]
    
        }
    }
}

MotoMenuButton { 
    label1: "Test", 
    label2: "Test 2" , 
    transform: Transform.translate(100, 100)
    anim: MotoMenuAnimation {active: true}
};

