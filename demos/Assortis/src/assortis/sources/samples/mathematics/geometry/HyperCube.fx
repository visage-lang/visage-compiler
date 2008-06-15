package assortis.sources.samples.mathematics.geometry;

import assortis.library.mathematics.multidim.*;

import javafx.gui.*;
import javafx.animation.*;


import java.lang.System;

var dim = 4;
var angle:Number =  0.0;

var delta = 1.0;

var timeline = Timeline {
    keyFrames :  KeyFrame {
            time : 0.05s
            action: function() { angle += delta}    
        }
    repeatCount: Timeline.INDEFINITE       
}

timeline.start();

Frame{
    width:  300
    height: 300
    closeAction: function  () { System.exit(0); } 
    title: "Hyper Cube"
    
    content: Canvas{
        content: MDUniverse{
            transform: Transform.translate(150,140);
            dimension: dim
            projection: MDMatrix{
                dimN: 2
                dimM: dim
                elems: [ 
                        [1, 0, 0, 0],
                        [0, 1, 0, 0]
                    ]
            }
            transforms: bind [ 
                  MDTransform.rotate(angle, 0, 1, dim),
                  MDTransform.rotate(angle, 0, 2, dim),
                  MDTransform.rotate(angle, 0, 3, dim),
                  MDTransform.rotate(angle, 1, 2, dim),
            ]
            
            shapes: [ MDCube{ dim: dim  side: 50} ]
        }
    }
}