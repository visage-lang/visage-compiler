package assortis.sources.language.javafx.animation;

import javafx.ext.swing.*;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
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

SwingFrame{
    title: "Pendulum"
    
    width: 220
    height: 220
    
    content: BorderPanel{
        center: Canvas{
            content: Group{
                transform: Transform.translate(110, 30);
                content: [
                    Line{ endX: bind cx, endY: bind cy, stroke: Color.ORANGE },
                    Circle{ centerX: 0, centerY: 0,  radius: 5 fill: Color.GRAY },
                    Circle{ centerX: bind cx, centerY: bind cy, radius: 15 fill: Color.ORANGE stroke: Color.ORANGE }

                ]
        }
        
      }
   }
}
