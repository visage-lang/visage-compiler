/*
 * Animation.fx
 *
 * Created on Dec 21, 2007, 1:26:04 PM
 */

package javafx.ui.animation;

import java.lang.Object;
import java.lang.System;
import com.sun.scenario.animation.Clip;
import com.sun.scenario.animation.Clip.Direction;
import com.sun.scenario.animation.Clip.RepeatBehavior;
import com.sun.scenario.animation.Clip.EndBehavior;
import com.sun.javafx.runtime.animation.*;

/**
 * @author jclarke
 */
public class Animation {
    private static attribute UNSET:Integer = java.lang.Integer.MIN_VALUE;
    
    public attribute timingTarget:FXTimingTarget;
    public attribute interpolator:FXInterpolator;
    public attribute delay:Integer = 0;
    public attribute sequence:Object[];
    public attribute property:String;
    public attribute instance:Object;
    public attribute stopProperty:String;
    public attribute stopInstance:Object;   
    public attribute stopValue:Object = null;
    public attribute interpolation:String;
    public attribute repeatCount:Number = 1.0;
    public attribute duration:Integer = Clip.INDEFINITE;
    public attribute resolution:Integer = UNSET;
    public attribute direction:Clip.Direction;
    public attribute repeatBehavior:Clip.RepeatBehavior;
    public attribute endBehavior:Clip.EndBehavior;
    
    private attribute animation:FXAnimation;
    
    public function start():Void {
        if(not isRunning()) {
            animation = if(property <> null and property.length() > 0) then 
                    new FXAnimation (duration, instance, property, stopInstance, stopProperty, stopValue, sequence)
                else 
                    new FXAnimation (timingTarget, interpolator, sequence, duration, resolution, 
                        repeatCount, direction, repeatBehavior, endBehavior, interpolation);
            animation.setDelay(delay);
        }
        animation.start();
    }
    
    public function isRunning():Boolean{
        return if(animation <> null) then animation.isRunning() else false;
    }
    
    public function stop():Void {
        if(animation <> null) {
            animation.stop();
        }
    }
    public function cancel():Void {
        if(animation <> null) {
            animation.cancel();
        }      
    }
    public function pause():Void {
        if(animation <> null) {
            animation.pause();
        }
    }
    
    public function resume():Void {
        if(animation <> null) {
            animation.resume();
        }
    }    
    
    public function waitUntilDone(): Void {
        if(animation <> null) {
            animation.waitUntilDone();
        }
    }
    
    public function startAndWait(): Void {
        start();
        waitUntilDone();
    }    

}