package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

public class MotoPanel extends Intro {
    attribute width: Number;
    attribute height: Number;
    attribute titleX: Number;
    attribute contentY: Number;
    attribute title: Node?;
    attribute content: Node?;
    operation doIntro();
    attribute alpha1: Number;
    attribute intro: KeyFrameAnimation;
}

attribute MotoPanel.intro = KeyFrameAnimation {
   keyFrames:
   [at (0s) {
       titleX => width;
       contentY => height;
       alpha1 => 0;
   },
   at (.25s) {
       alpha1 => 1 tween LINEAR;
   },
   at (1s) {
       titleX => 0 tween EASEBOTH;
       alpha1 => 0 tween LINEAR;
       contentY => 0 tween EASEBOTH;
   }]
};

operation MotoPanel.doIntro() {
   intro.start();
}

function MotoPanel.composeNode() =

Clip {
    filter: bind if hover then select Glow[i] from i in [0, 1] animation {dur: 300ms}  else null
    shape: Rect {height: bind height, width: bind width}
    onMouseClicked: operation(e) {doIntro();}
    content:
    [ImageView {
        transform: translate(0, 2)
        image: {url: "{__DIR__}/Image/77.png"}
    },
    Circle {
        opacity: bind alpha1
        cx: 10.5
        cy: 12.5
        radius: 10
        fill: white
        fill:RadialGradient {
            cx: 10
            cy: 10
            radius: 12
            stops:
            [Stop {
                offset: 0
                color: white
            },
            Stop {
                offset: 1
                color: new Color(1, 1, 1, 0)
            }]
        }
    },
    Clip {
        shape: Rect {height: bind title.currentHeight, width: bind width}
        transform: bind translate(25+titleX, 18)
        content: bind title
        valign: BOTTOM
    },
    Clip {
        transform: translate(20, 20)
        shape: Rect {x: -50, height: bind height-20, width: bind width+50}
        content:
        Group {
            transform: bind translate(0, contentY)
            content:
            [Group {
                
                content:
                ImageView {
                    clip: bind select {shape: Rect {height: 5, width: w}} from w in width
                    image: {url: "{__DIR__}/Image/95.png"}
                }
            },
            Group {
                transform: translate(5, 10)
                content: bind content
            }]
        }
    }]
};

Canvas {
    background: black
    content:
    MotoPanel {
        width: 200
        height: 200
        title: Text {content: "Promotions", fill: white, font: Font{face: ARIAL, size: 14}}
        content: Text {content: "Promotions", fill: white, font: Font{face: ARIAL, size: 14}}
        
    }
}



