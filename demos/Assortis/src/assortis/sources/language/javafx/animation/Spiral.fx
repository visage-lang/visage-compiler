package assortis.sources.language.javafx.animation;

import javafx.gui.*;
import javafx.gui.swing.Frame;
import javafx.animation.*;

import java.lang.Math;

var N = 500;

var r = 0.1;
var w = 0.05;

var angle =  0.0;
var delta = 10.0;

var timeline = Timeline {
    keyFrames :  KeyFrame {
            time : 0.05s
            action: function() { angle += delta}    
        }
    repeatCount: Timeline.INDEFINITE       
}

timeline.start();

Frame {
    title: "Spiral"
    width: 200
    height: 200
    content: Canvas {
        content: Path {
            transform: bind [Transform.translate(100,85),Transform.rotate(angle, 0,0)]
            elements: [ MoveTo{x: 0, y: 0} ,
                for(n in [0..N])  
                    LineTo{ 
                        x: r * n * Math.cos( w * n ),
                        y: r * n * Math.sin( w * n )
                    }
                ]
               stroke: Color.GREEN
            }

    }
}