/*
 * This file is created with Netbeans 6 Plug-In for JavaFX
 */
package lightsout;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import lightsout.math.*;
import java.lang.System;
import javafx.ui.animation.Animation;
import com.sun.javafx.runtime.animation.FXTimingTarget;

public class LightsOutSplash extends CompositeNode {
    public static attribute stdFont:Font = Font.Font("Arial", ["BOLD"], 55);        
    private attribute text_lights: Text= Text {
        //content:"Lights", x:125,y:70, font:stdFont, fill: Color.WHITE
        content:"Lights", x:-300.0,y:70, font:stdFont, fill: Color.WHITE
    };
    private attribute text_out: Text = Text {
        //content:"Out", x:155,y:130, font:stdFont, fill: Color.WHITE
        content:"Out", x:500.0,y:130, font:stdFont, fill: Color.WHITE
    };
    private attribute start_button : BlueButton = 
        BlueButton { text: "Start", font: Font.Font("Arial",["BOLD"],20)
            //transform : [Transform.translate(152,230)], width: 100, height: 30,
            transform : [Transform.translate(152,500)], width: 100, height: 30,
            //transition to the next screen
            buttonAction : function(e:CanvasMouseEvent):Void { 
                System.out.println("Pressed");
                //opacity = 0.0;  [1.00..0 step 0.01] dur 1000 delay ;
                var anime:Animation = Animation{
                    duration:1000, 
                    instance:this, 
                    property:"opacity",
                    sequence:[1.0..0.0 step -0.01]
                };
                anime.start();                
                //locan.opacity = 1.0; [0.00..1.0 step 0.01] dur 1000 delay 500;
                anime = Animation{
                    duration:1000, 
                    instance:locan, 
                    property:"opacity",
                    sequence:[0.0..1.0 step 0.01]
                };
                anime.start();                
            }
        };

    // a reference to main so we can do screen transitions
    attribute locan: LightsOutCanvas;
    public function transitionIn(){
        //fade in
        //opacity = [0.0..1.0 step 0.01] dur 1000 delay 0;
        var anime:Animation = Animation{
            duration:1000, 
            instance:this, 
            property:"opacity",
            sequence:[0.0..1.0 step 0.01]
        };
        anime.start();
        //then move in the text after 1.5s. notice the delay of 1500 
        //text_lights.x = [-300..125] dur 500 delay 1500;
        anime = Animation{
            duration:1500, 
            instance:text_lights, 
            property:"x",
            sequence:[-300..125]
            delay:1500
        };        
        anime.start();
        
        //text_out.x = [500..155] dur 500 delay 1500;
        anime = Animation{
            duration:1500, 
            instance:text_out, 
            property:"x",
            sequence:[500..155 step -1]
            delay:1500
        };        
        anime.start();        
        var sby = 500;
        var transform:Translate = Transform.translate(155,500);
        // then move in the button.  
        // TODO need to bind sby
        start_button.transform = [transform];
        //sby = [500..200] dur 500 delay 1800 SPLINE([0, 1], [0, .75, .5, 1]);
        //sby = [500..200] dur 1000 motion SPLINE([0, 1], [0, .75, .5, 1]);
        //sby = [350..200] dur 750 motion SPRING delay 2000;
        
        anime = Animation{
            instance:transform
            property:"y"
            duration:1750, 
            sequence:[350..200 step -1]
            delay:2000
            interpolation:"Spline:0:.75:.5:1"
        };  
        anime.start(); 
    }

    
    public function composeNode(): Node {
         Group { content:[
            Rect{fill: ColorConstants.slate, width:400, height:300},
            text_lights, text_out, start_button
            ]};
    }
    
}

/****************************************************

//var x = [20, 0, 300, 0] dur {seconds: 3,  motion: SPLINE([0, 1], [0, .75, .5, 1])};
function SPRING(vals:Number[], t:Number) {
    var amplitude = 1.0;
    var friction = 0.3;
    var mass = 0.1;//058;
    var ridigity = 30.0;
    var phase = 0;
    var bouncer:Equation = new BouncerEquation(amplitude, friction, mass, ridigity, phase);
    //System.out.println("blah = {bouncer.compute(t)}");
    var t2 = bouncer.compute(t);
    //System.out.println("t = {t} t2 = {t2}");
    var foo = sizeof vals;
    var firstv = vals[0];
    var lastv = vals[(sizeof vals)-1];
    //System.out.println("first = {firstv} and last = {lastv}");
    return firstv + (lastv-firstv)*(1-t2); //1-t2 makes it reverse
}

function SPLINE(keyTimes:Number[], keySplines:Number[]): function(Number[], Number):Number {
    return function(input:Number[], t:Number): Number {
        //assert sizeof input > 1;
        //assert sizeof input == sizeof keyTimes;
        //assert sizeof keySplines == (sizeof keyTimes-1)*4;
        var i1;
        for( i in [sizeof keyTimes-1..0]) {
            if (keyTimes[i] <= t) {
                i1 = sizeof keyTimes -1 - i;
                break;
            }
        }
        var i2 = i1 + 1;
        var off = i1 * 4;
        var x1 = keySplines[off];
        var y1 = keySplines[off+1];
        var x2 = keySplines[off+2];
        var y2 = keySplines[off+3];
        var v1 = input[i1];
        var v2 = input[i2];
        function getY(t: Number):Number {
            var invT = 1 - t;
            var b1 = 3 * t * (invT * invT);
            var b2 = 3 * (t * t) * invT;
            var b3 = t * t * t;
            var y = (b1 * y1) + (b2 * y2) + b3;
            return y;
        }
        var st = (t-keyTimes[i1])/(keyTimes[i2]-keyTimes[i1]);
        var y = getY(st);
        return v1 + (v2-v1)*y;

    };
}
****************************************************/





