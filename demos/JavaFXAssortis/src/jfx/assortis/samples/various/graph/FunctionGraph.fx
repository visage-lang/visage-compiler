/*
 * FunctionGraph.fx
 *
 * Created on Mar 27, 2008, 1:31:56 PM
 */

package jfx.assortis.samples.various.graph;

import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;


import java.lang.System;


var sqr =  function(x:Number):Number{
    return x * x;
};


var myFunction =  function(x:Number):Number{
    return 3 * Math.exp(-sqr(x)/2);
};

var approximation= function(x:Number):Number{
    var n = 10; 

    var sum = 0.0;
    var sign = 1.0;
    

    for(i in [1..n]){

        sign = -sign;
        sum += ((1 - sign) * Math.sin(i * x)) / i; 
    }

    return sum;
};





public class Reflection extends Transform{
   public static function reflect():Transform{
      return Transform.matrix(1,0,0,-1,0,0);
   }	
} 


public class FunctionGraph extends CompositeNode{
    
    attribute scale:Number = 1;
    attribute xMin: Number;
    attribute xMax: Number;
    attribute dx: Number = 1;
    attribute color: Color;
    
    attribute func: function(a: Number):Number;

    public function composeNode():Node{

      var n = (xMax - xMin) / dx;
    
      var polyline = Polyline{ stroke: color };
    
      for(i in [0..n]){
        var x = xMin + i * dx;
        var y =  (this.func)(x);
        
        insert( x * scale ) into polyline.points;
        insert( y * scale ) into polyline.points;   
      }
    
      return Group{ content: [ polyline] };
    }
}


public class Coordinats extends CompositeNode{

    attribute xMin: Number;
    attribute xMax: Number;
    attribute scale: Number;
    attribute color: Color = Color.GREEN;

    public function composeNode():Node{
      var min = xMin * scale;
      var max = xMax * scale;
      var h = 7;
    
      return Group{
        content:[
        Line{ x1: min x2: max stroke: color },
        Line{ x1: max x2: max - h y2: h stroke: color },
        Line{ x1: max x2: max - h y2: -h stroke: color },
        Line{ y1: min y2: max stroke: color },
        Line{ y1: max  x2: -h y2: max - h stroke: color },
        Line{ y1: max  x2:  h y2: max - h stroke: color },
        Group{
            transform: [ Reflection.reflect() ]
            content: [
            Text{ x: min font: Font{faceName: "Arial", size: 18} content: "{xMin}" },
            Text{ x: max font: Font{faceName: "Arial", size: 18} content: "{xMax}"}
            ] }
        ]
        
      };
    }
}


var w = 300;
var h = 300;

var xMin = -4;
var xMax = 4;
var scale = 20;
var dx = 0.1;




Frame{
    width:  300
    height: 300
    title: "Function Graphic"
    

    content: Canvas{
	 content: [ Group{

            transform: [ Translate.translate(w/2, h/2), Reflection.reflect() ]
            content: [ Coordinats{
                xMin: xMin
                xMax: xMax
                scale: scale
            }, FunctionGraph {
                xMin: xMin
                xMax: xMax
                scale: scale
                dx: dx
                color: Color.DARKORANGE
                func: approximation
            },FunctionGraph {
                xMin: xMin
                xMax: xMax
                scale: scale
                dx: dx
                color: Color.BLUE
                func: myFunction                
            }]
      
          
        }]
    }
    visible: true
}
