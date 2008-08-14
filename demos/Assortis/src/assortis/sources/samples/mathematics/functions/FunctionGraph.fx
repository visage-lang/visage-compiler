package assortis.sources.samples.mathematics.functions;

import javafx.ext.swing.*;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import java.lang.Math;
import java.lang.System;


function f1(x:Number):Number{
    return 3 * Math.exp( -(x * x ) / 2 );
};

function f2(x:Number):Number{
    var n = 10; 

    var sum = 0.0;
    var sign = 1.0;
    

    for(i in [1..n]){

        sign = -sign;
        sum += ((1 - sign) * Math.sin(i * x)) / i; 
    }

    return sum;
};


public class FunctionGraph extends CustomNode{
    
    var scale:Number = 1;
    var xMin: Number;
    var xMax: Number;
    var dx: Number = 1;
    var color: Color;
    
    var func: function(a: Number):Number;

    override function create():Node{

      var n = (xMax - xMin) / dx;
    
      var polyline = Polyline{ stroke: color };
    
      for(i in [0..n]){
        var x = xMin + i * dx;
        var y =  (this.func)(x);
        
        insert(   x * scale ) into polyline.points;
        insert( - y * scale ) into polyline.points;   
      }
    
      return Group{ content: [ polyline] };
    }
}


public class Coordinats extends CustomNode{

    var xMin: Number;
    var xMax: Number;
    var scale: Number;
    var color: Color = Color.GREEN;

    override function create():Node{
      var min = xMin * scale;
      var max = xMax * scale;
      var h = 7;
    
      return Group{
        content:[
        Line{ startX: min endX: max stroke: color },
        Line{ startX: max endX: max - h endY: h stroke: color },
        Line{ startX: max endX: max - h endY: -h stroke: color },
        Line{ startY: min endY: max stroke: color },
        Line{ startY: min  endX: -h endY: min + h stroke: color },
        Line{ startY: min  endX:  h endY: min + h stroke: color },
        Group{
            content: [
            Text{ x: min y: -3 content: "{xMin}" },
            Text{ x: max y: -3 content: "{xMax}"}
            //Text{ x: min font: Font{faceName: "Arial", size: 18} content: "{xMin}" },
            //Text{ x: max font: Font{faceName: "Arial", size: 18} content: "{xMax}"}
            ] }
        ]
        
      };
    }
}

function run() {
    var w = 300;
    var h = 300;

    var xMin = -4;
    var xMax = 4;

    var dx = 0.1;
    var scale = 20;


    SwingFrame{
        width:  300
        height: 300
        title: "Function Graph"

        content: Canvas{
    	 content: [ Group{

                transform: Translate.translate(w/2, h/2)
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
                    func: f1
                },FunctionGraph {
                    xMin: xMin
                    xMax: xMax
                    scale: scale
                    dx: dx
                    color: Color.BLUE
                    func: f2                
            }]            
        }]
       }
    }
}
