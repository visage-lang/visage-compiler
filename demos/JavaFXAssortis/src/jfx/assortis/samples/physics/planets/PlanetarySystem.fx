
package jfx.assortis.samples.physics.planets;


import javafx.ui.*;
import java.lang.Math;
import java.lang.Thread;
import java.lang.System;
import java.lang.Runnable;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


function square(x: Number): Number{
    return x * x;
}


public class PlanetarySystem {

    attribute dimension: Integer = 2;
    attribute dt: Number = 60* 60 * 24 * 2;
    attribute G: Number = 6.67e-11;
    attribute planets:  Planet[];
    attribute ticks: Integer;

  public function start(){
    var timerListener:ActionListener = ActionListener {
        public function actionPerformed(evt:ActionEvent): Void {
            run();
        }
    }
    var timer:Timer = new Timer(20, timerListener);
    timer.start(); 
  }  

  public function run():Void{
    
    for(p1 in planets){
        
        for(i in [0..dimension-1]){
            p1.acceleration[i] = 0.0;
	}
        
        for(p2 in planets[p| p<>p1]){
            
            var k = G * p2.mass;
            
            var deltaR:Number[] = [0.0,0.0];

            for(i in [0..dimension-1]){
                deltaR[i] = p2.coordinate[i] - p1.coordinate[i];
            }
            
            var squareR = 0.0;
            for(i in [0..dimension-1]){
                squareR += square(deltaR[i]);
            }
            
            for(i in [0..dimension-1]){
                p1.acceleration[i] += k * deltaR[i] / (squareR * Math.sqrt(squareR));
            }

        }

    }
        
    for(p in planets){
        for(i in [0..dimension-1]){
            p.velocity[i] += p.acceleration[i] * dt;
            p.coordinate[i] += p.velocity[i] * dt;
        }

    }
  }     

}


