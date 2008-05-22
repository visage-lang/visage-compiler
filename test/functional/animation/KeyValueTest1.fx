/**
  * Checking KeyValue class with a Pointer type target value.
  * 
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import com.sun.javafx.runtime.PointerFactory;

class CLS { 
   public attribute pf : PointerFactory = PointerFactory {}; 
   public attribute a : Number = 5 on replace {       
       System.out.println("a: {a}");
       Thread.sleep (1000); 
   }; 
   public attribute bpa = bind pf.make(a); 
   public attribute pa = bpa.unwrap(); 
} 

var cls = CLS {};

var t : Timeline = Timeline {
   repeatCount: 1
   keyFrames : [ 
       KeyFrame { 
           time : 0.0s 
           values: KeyValue {
               target: cls.pa 
               value: 1.0 
               interpolate: Interpolator.LINEAR 
           } 
           action : function () { 
               System.out.println ("timeline tick - {cls.a}"); 
           } 
       }, 
       KeyFrame { 
           time : 0.9s 
           values: KeyValue {
               target: cls.pa 
               value: 2.0 
               //interpolate: Interpolator.LINEAR 
           } 
           action : function () { 
               java.lang.System.out.println ("timeline finished - {cls.a}"); 
           } 
       } 
   ] 
}; 

t.start ();
