/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.javafx.runtime.animation;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.scenario.animation.*;
import com.sun.tools.javafx.ui.FXBean;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author jclarke
 */
public class FXAnimation implements TimingTarget, Interpolator {
    private static final int UNSET = Integer.MIN_VALUE;
    FXTimingTarget timingTarget;
    FXInterpolator interpolator;
    Clip clip;
    int counter;
    int delay = 0;
    Sequence sequence;
    FXBean bean;
    String property;
    Object instance;
    String stopProperty;
    Object stopInstance;
    Object stopValue= Boolean.TRUE;
    FXBean stopBean;
    
    public FXAnimation(int duration, Object instance, String property, Sequence sequence) {
        this(duration, instance, property, null, null, null, sequence);
    }
    public FXAnimation(int duration, Object instance, String property, 
            Object stopInstance, String stopProperty, Object stopValue, Sequence sequence) {
        try {
            this.sequence =  sequence;
            this.bean = new FXBean(instance.getClass());
            this.instance = instance;
            this.property = property;
            this.stopInstance = stopInstance;
            this.stopProperty = stopProperty;
            if(stopValue != null) {
                this.stopValue = stopValue;
            }
            if(this.stopProperty != null && this.stopProperty.length() > 0) {
                if(this.stopInstance == null) {
                    this.stopInstance = this.instance;
                    this.stopBean = this.bean;
                }else {
                    if(this.stopInstance.getClass() == this.instance.getClass()) {
                        this.stopBean = this.bean;
                    }else {
                        this.stopBean = new FXBean(this.stopInstance.getClass());
                    }
                }
            }
            clip = Clip.create(duration, this);
        } catch (IntrospectionException ex) {
            Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public FXAnimation(FXTimingTarget timingTarget,  int duration) {
        this(timingTarget, null, null, duration, UNSET, UNSET, null,
                null, null, null);
    }  
    public FXAnimation(FXTimingTarget timingTarget,  int duration,  Sequence sequence, String builtinInterpolator) {
        this(timingTarget, null, sequence, duration, duration/sequence.size(), UNSET, null,
                null, null, builtinInterpolator);
    }     
    public FXAnimation(FXTimingTarget timingTarget,  int duration,  String builtinInterpolator) {
        this(timingTarget, null, null, duration, UNSET, UNSET, null,
                null, null, builtinInterpolator);
    }    
    public FXAnimation(FXTimingTarget timingTarget,  int duration, int resolution) {
        this(timingTarget, null, null, duration, resolution, UNSET, null,
                null, null, null);
    }  
    public FXAnimation(FXTimingTarget timingTarget,  int duration, int resolution,  String builtinInterpolator) {
        this(timingTarget, null, null, duration, resolution, UNSET, null,
                null, null, builtinInterpolator);
    }     
    public FXAnimation(FXTimingTarget timingTarget,  int duration, int resolution, int repeatCount) {
        this(timingTarget, null, null, duration, resolution, repeatCount, null,
                null, null, null);
    }  
    
    public FXAnimation(FXTimingTarget timingTarget,  int duration, int resolution, int repeatCount, String builtinInterpolator ) {
        this(timingTarget, null,  null, duration, resolution, repeatCount, null,
                null, null, builtinInterpolator);
    }
    public FXAnimation(FXTimingTarget timingTarget,  
                int duration, int resolution, double repeatCount, Clip.Direction direction, 
                Clip.RepeatBehavior repeatBehavior, Clip.EndBehavior endBehavior, 
                String builtinInterpolator) {
        this(timingTarget, null,  null, duration, resolution, repeatCount, direction,
                repeatBehavior, endBehavior, builtinInterpolator);
    }
    public FXAnimation(FXTimingTarget timingTarget, FXInterpolator interpolator, 
            Sequence sequence,
                int duration, int resolution, double repeatCount, Clip.Direction direction, 
                Clip.RepeatBehavior repeatBehavior, Clip.EndBehavior endBehavior, 
                String builtinInterpolator) {
        this.sequence = sequence;
        this.timingTarget = timingTarget;
        this.interpolator = interpolator;
        clip = Clip.create(duration, this);
        if(repeatCount != UNSET) {
            clip.setRepeatCount(repeatCount);
        }
        if(resolution != UNSET) {
            clip.setResolution(resolution);
        }
        if(direction != null)
            clip.setDirection(direction);
        if(interpolator != null) {
            clip.setInterpolator(this);
        }else if(builtinInterpolator != null) {
            if(builtinInterpolator.startsWith("Linear")){
                clip.setInterpolator(Interpolators.getLinearInstance());
            }else if(builtinInterpolator.startsWith("Discrete")){
                clip.setInterpolator(Interpolators.getDiscreteInstance());
            }else if(builtinInterpolator.startsWith("Spline")){
                // Spline:x1:y1:x2:y2 e.g Easing:100:100:50:50
                String[] args = builtinInterpolator.split(":");
                float x1 = Float.parseFloat(args[1]);
                float y1 = Float.parseFloat(args[2]);
                float x2 = Float.parseFloat(args[3]);
                float y2 = Float.parseFloat(args[4]);
                clip.setInterpolator(Interpolators.getSplineInstance(x1, y1, x2, y2));
            }else if(builtinInterpolator.equals("Easing")){
                clip.setInterpolator(Interpolators.getEasingInstance());
            }else if(builtinInterpolator.startsWith("Easing:")){
                // Easing:acceleration:deceleration e.g Easing:.1:.1 
                String[] args = builtinInterpolator.split(":");
                float acceleration = Float.parseFloat(args[1]);
                float deceleration = Float.parseFloat(args[1]);
                clip.setInterpolator(Interpolators.getEasingInstance(acceleration, deceleration));
            }
            
        }
        
        if(repeatBehavior != null) {
            clip.setRepeatBehavior(repeatBehavior);
        }
        if(endBehavior != null) {
            clip.setEndBehavior(endBehavior);
        }
        
    }
    
    public void setDuration(int duration) {
            clip.setDuration(duration);
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public synchronized void waitUntilDone() {
        while(isRunning()) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                //Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean isRunning(){
        return clip.isRunning();
    }
    
    public void stop() {
        clip.stop();
    }
    public void start() {
        counter = 0;
        if(delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        clip.start();
    }
    
    public void cancel() {
         clip.cancel();
        synchronized (this) {
            this.notifyAll();
        }         
    }
    public void pause() {
        clip.pause();

    }
    
    public void resume() {
        clip.resume();
    }

    public void timingEvent(float fraction) {
        if(timingTarget != null) {
            if(sequence != null) {
                
                if(sequence.size() == 0) {
                    timingTarget.timingEvent(0.0);
                }else {
                    double value = 0.0;
                    int ndx = (int)(fraction * (sequence.size()-1));
                    Object obj = sequence.get(ndx);
                    if(obj instanceof Number) {
                        value = ((Number)obj).doubleValue();
                    }else {
                        try {
                            value = Double.valueOf(obj.toString()).doubleValue();
                        }catch(NumberFormatException ex) {
                            Logger.getLogger(FXAnimation.class.getName()).log(Level.WARNING, null, ex);
                            value = 0.0;
                        }
                    }
                    timingTarget.timingEvent(value);
                }
            }else {
                timingTarget.timingEvent(fraction);
            }
        } else if (sequence != null && bean != null) {
            if(stopBean != null) {
                try {
                    Object check = stopBean.getObject(this.stopInstance, this.stopProperty);
                    if(!check.equals(stopValue)) {
                        clip.stop();
                        return;
                    }
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Double value = null;
                if(sequence.size() == 0) {
                    value = new Double(0.0);
                }else {                
                    int ndx = (int)(fraction * (sequence.size()-1));
                    Object obj = sequence.get(ndx);
                    if(obj instanceof Number) {
                        if(obj instanceof Double) {
                            value = (Double)obj;
                        }else {
                            value = new Double(((Number)obj).doubleValue());
                        }
                    }else {
                        try {
                            value = Double.valueOf(obj.toString());
                        }catch(NumberFormatException ex) {
                            Logger.getLogger(FXAnimation.class.getName()).log(Level.WARNING, null, ex);
                            value = new Double(0.0);
                        }
                    }
                    bean.setObject(instance, property, value);
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex) {
                Logger.getLogger(FXAnimation.class.getName()).log(Level.SEVERE, null, ex);
            }
                            
        }
    }

    public void begin() {
        if(timingTarget != null) {
            timingTarget.begin();
        }
    }

    public void end() {
        synchronized (this) {
            this.notifyAll();
        }  
        if(timingTarget != null) {
            timingTarget.end();
        }
    }

    public float interpolate(float fraction) {
        if(interpolator != null)
            return (float)interpolator.interpolate(fraction);
        else 
            return fraction;
    }

}
