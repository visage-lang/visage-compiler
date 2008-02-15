package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;

public class MotoMenuButton extends CompositeNode {
    public attribute anim: MotoMenuAnimation;
    public attribute label1: String;
    public attribute label2: String;
    public attribute action: function();
    attribute mouseOver: Boolean;
    attribute a: Timeline = Timeline {
        toggle: true
        keyFrames:
        [at (0s) {
            y => 0;
        },
        at (200ms) {
            y => -12 tween EASEBOTH;
        },
        at (400ms) {
            y => -10 tween EASEBOTH;
        }]
    };

    attribute y: Number;

    function composeNode() {
        Group {
            content:
            [Group {
                cursor: HAND
                trigger on (h = mouseOver) {
                    a.start();
                }
                var w = 110
                var h = 60
                transform: bind translate(0, y)
                content: bind
                [Rect {
                    selectable: true
                    width: w
                    height: h
                    fill: black
                    arcHeight: 20
                    arcWidth: 20
                    var: rect
                    trigger on (newValue = rect.hover) {
                        mouseOver = newValue;
                    }
                    onMouseClicked: function(e) {(this.action)();}
                },
                Rect {
                    clip: {shape: Rect {height: h*.25, width: w}}
                    width: w
                    height: h*.8
                    fill: LinearGradient {
                        x2: 0, y2: 1
                        stops:
                        [Stop {
                            offset: 0
                            color: new Color(.7, .7, .7, 1)
                        },
                        Stop {
                            offset: 0.05
                            color: new Color(.2, .2, .2, 1)
                        },
                        Stop {
                            offset: 0.5
                            color: new Color(.1, .1, .1, 1)
                        }]
                    }
                    arcHeight: 20
                    arcWidth: 20
                },
                Group {
                    visible: bind not mouseOver    
                    transform: translate(30, h*.3)
                    valign: MIDDLE
                    content:
                    VBox {
                        var textColor = orangered: Color
                        var font1 = Font {face: ARIAL, style: PLAIN size: 12}
                        var font = Font {face: ARIAL, style: BOLD size: 12}
                        content:
                        [Text {
                            font: font1
                            fill: orange
                            content: bind label1
                        },
                        Text {
                            transform: translate(0, 3)
                            fill: white
                            font: font
                            content: bind label2
                        }]
                    }
                },
                Group {
                    visible: bind mouseOver
                    content: bind 
                    [Group {
                        transform: translate(w -5, h*.4)
                        valign: MIDDLE, halign: TRAILING
                        content: bind if mouseOver then anim else null
                        var active = bind mouseOver
                        trigger on (a = active) {
                            if (a) {
                                anim.start();
                            } else {
                                println("STOP");
                                anim.stop();
                            }
                        }
                    },
                    HBox {
                        transform: translate(w-5, h*.7)
                        valign: CENTER, halign: TRAILING
                        var textColor = orangered: Color
                        var font = Font {face: ARIAL, style: BOLD size: 8}
                        content:
                        [Text {
                            font: font
                            fill: orange
                            content: bind label1.toUpperCase()
                        },
                        Text {
                            transform: translate(2, 0)
                            fill: white
                            font: font
                            content: bind label2.toUpperCase()
                        }]
                    }]
                },
                Group {
                    transform: translate(10, 5)
                    content:
                    [ImageView {
                        visible: false //bind mouseOver
                        transform: translate(-3, -3)
                        image: {url: bind "{__DIR__}/Image/8.png"}
                    },
                    ImageView {
                        //clip: {shape: Rect {width: 10, height: 24}}            
                        //var u = bind if mouseOver then unitinterval in dur 1000 fps 5 while mouseOver continue if true  else 0
                        //var i = bind if (false and mouseOver) (11 + u * 55).intValue() else 7
                        //image: Image {url: bind "{__DIR__}/Image/{i}.png"}
                        image: Image {url: "{__DIR__}/Image/7.png"}
                    },
                    ImageView {
                        opacity: bind if mouseOver then 0.5 else 0
                        transform: translate(-3, -3)
                        image: Image {url: bind "{__DIR__}/Image/8.png"}
                    }]
                }]
            }]
    
        }
    }
}

MotoMenuButton { 
    label1: "Test", 
    label2: "Test 2" , 
    transform: translate(100, 100)
    anim: MotoMenuAnimation {active: true}
};
