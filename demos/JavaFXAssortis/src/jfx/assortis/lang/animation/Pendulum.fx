package jfx.assortis.lang.animation;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.animation.*;

import java.lang.Math;
import java.lang.System;


var g = 10;
var mass = 10;
var radius = 10;

var t = 0.0;
var dt = 0.1;

var w = 0.5;


var angle = bind Math.sqrt( radius / g ) * Math.sin( w * t);


var cx = bind 10 * radius * Math.sin( angle );
var cy = bind 10 * radius * Math.cos( angle );


var timeline = Timeline {
    keyFrames:  KeyFrame { time: 0.01s,  action: function() { t += dt; } } 
    repeatCount: java.lang.Double.POSITIVE_INFINITY
} 

timeline.start();

Frame{
    title: "Pendulum"
    
    width: 220
    height: 220
    
    content: BorderPanel{
        center: Canvas{
            content: Group{
                transform: Transform.translate(110, 30);
                content: [
                    Line{ x2: bind cx, y2: bind cy, stroke: Color.ORANGE },
                    Circle{ cx: 0, cy: 0,  radius: 5 fill: Color.GRAY },
                    Circle{ cx: bind cx, cy: bind cy, radius: 15 fill: Color.ORANGE stroke: Color.ORANGE }

                ]
        }
        
    }
}

visible: true
}
