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
    static var UNSET:Integer = java.lang.Integer.MIN_VALUE;
    
    public var timingTarget:FXTimingTarget on replace {
        changed = true;
    };
    public var interpolator:FXInterpolator on replace {
        changed = true;
    };
    public var delay:Integer = 0 on replace {
        changed = true;
    };
    public var sequence:Object[] on replace {
        changed = true;
    };
    public var property:String on replace {
        changed = true;
    };
    public var instance:Object on replace {
        changed = true;
    };
    public var stopProperty:String on replace {
        changed = true;
    };
    public var stopInstance:Object on replace {
        changed = true;
    }; 
    public var stopValue:Object = null on replace {
        changed = true;
    };
    public var interpolation:String on replace {
        changed = true;
    };
    public var repeatCount:Number = 1.0 on replace {
        changed = true;
    };
    public var duration:Integer = Clip.INDEFINITE on replace {
        changed = true;
    };
    public var resolution:Integer = UNSET on replace {
        changed = true;
    };
    public var direction:Clip.Direction on replace {
        changed = true;
    };
    public var repeatBehavior:Clip.RepeatBehavior on replace {
        changed = true;
    };
    public var endBehavior:Clip.EndBehavior on replace {
        changed = true;
    };
    
    var animation:FXAnimation;
    var changed:Boolean;
    
    public function start():Void {
        if((not isRunning() and changed) or animation == null) {
            changed = false;
            animation = if(property != null and property.length() > 0) then 
                    new FXAnimation (timingTarget, duration, instance, property, stopInstance, stopProperty, stopValue, sequence)
                else 
                    new FXAnimation (timingTarget, interpolator, sequence, duration, resolution, 
                        repeatCount, direction, repeatBehavior, endBehavior, interpolation);
            animation.setDelay(delay);
        }
        animation.start();
    }
    
    public function isRunning():Boolean{
        return if(animation != null) then animation.isRunning() else false;
    }
    
    public function stop():Void {
        if(animation != null) {
            animation.stop();
        }
    }
    public function cancel():Void {
        if(animation != null) {
            animation.cancel();
        }      
    }
    public function pause():Void {
        if(animation != null) {
            animation.pause();
        }
    }
    
    public function resume():Void {
        if(animation != null) {
            animation.resume();
        }
    }    
    
    public function waitUntilDone(): Void {
        if(animation != null) {
            animation.waitUntilDone();
        }
    }
    
    public function startAndWait(): Void {
        start();
        waitUntilDone();
    }    

}
