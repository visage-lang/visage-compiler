package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;


public class MotoPanel extends Intro {
    attribute base: java.net.URL; // work around for __DIR__
    attribute width: Number;
    attribute height: Number;
    attribute titleX: Number;
    attribute contentY: Number;
    attribute title: Node;
    attribute content: Node;
    attribute alpha1: Number;
    
    private attribute pf: PointerFactory = PointerFactory{};
    private attribute _titleXp = bind pf.make(titleX);
    private attribute _titleX = _titleXp.unwrap();
    private attribute _contentYp = bind pf.make(contentY);
    private attribute _contentY = _contentYp.unwrap();
    private attribute _alpha1p = bind pf.make(alpha1);    
    private attribute _alpha1 = _alpha1p.unwrap();    
    
    attribute intro: Timeline = Timeline {

        keyFrames:
           [ KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _titleX
                        value: width
                    },
                    NumberValue {
                        target: _contentY
                        value: height
                    },
                    NumberValue {
                        target: _alpha1
                        value: 0
                    }
                ]
            },
            KeyFrame {
                keyTime: 250ms
                keyValues:  NumberValue {
                        target: _alpha1
                        value: 1
                        interpolate: NumberValue.LINEAR
                    }
            },
            KeyFrame {
                keyTime: 1s
                keyValues:  [
                    NumberValue {
                        target: _titleX
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    },
                    NumberValue {
                        target: _contentY
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    },
                    NumberValue {
                        target: _alpha1
                        value: 0
                        interpolate: NumberValue.LINEAR
                    }
                ]
            }
         ]
    };
    function doIntro():Void {
       intro.start();
    }

    function composeNode():Node {
        Clip {
            //TODO GLOW animaton
            //filter: bind if (hover) select Glow[i] from i in [0, 1] animation {dur: 300ms}  else null
            shape: Rect {height: bind height, width: bind width}
            onMouseClicked: function(e) {doIntro();}
            content:
            [ImageView {
                transform: Transform.translate(0, 2)
                image: Image {url: "{base}/Image/77.png"}
            },
            Circle {
                opacity: bind alpha1
                cx: 10.5
                cy: 12.5
                                                                                                radius: 10
                fill: Color.WHITE
                fill:RadialGradient {
                    cx: 10
                    cy: 10
                    radius: 12
                    stops:
                    [Stop {
                        offset: 0
                        color: Color.WHITE
                    },
                    Stop {
                        offset: 1
                        color: Color.rgba(1, 1, 1, 0)
                    }]
                }
            },
            Clip {
                shape: Rect {height: bind title.currentHeight, width: bind width}
                transform: bind Transform.translate(25+titleX, 18)
                                                                                                content: bind title
                valign: VerticalAlignment.BOTTOM
            },
            Clip {
                transform: Transform.translate(20, 20)
                shape: Rect {x: -50, height: bind height-20, width: bind width+50}
                content:
                Group {
                    transform: bind Transform.translate(0, contentY)
                    content:
                    [Group {

                        content:
                        ImageView {
                            clip: bind Clip{shape: Rect {height: 5, width: bind width}}
                            image: Image {url: "{base}/Image/95.png"}
                        }
                    },
                    Group {
                        transform: Transform.translate(5, 10)
                        content: bind content
                    }]
                }
            }]
        };
    };
};

Canvas {
    background: Color.BLACK
    content:
    MotoPanel {
        width: 200
        height: 200
        title: Text {content: "Promotions", fill: Color.WHITE, font: Font{face: FontFace.ARIAL, size: 14}}
        content: Text {content: "Promotions", fill: Color.WHITE, font: Font{face: FontFace.ARIAL, size: 14}}
        
    }
}




