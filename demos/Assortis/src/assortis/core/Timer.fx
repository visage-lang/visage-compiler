/*
* Timer.fx
*
* Created on May 8, 2008, 3:28:27 PM
*/

package assortis.core;

/**
* @author Alexandr Scherbatiy
*/

import java.lang.System;
import java.lang.Thread;
import java.lang.Runnable;

import javafx.animation.*;

public class Timer {
    
    public attribute tasks: TimerTask[];
    public attribute TIMEOUT = 600;

    public function addTask (name: String, action: function() ) {
        
        //System.out.println("[timer] add task: {name}");
        
        var task = tasks[t | t.name == name ];
        
        if (0 < sizeof task ){
            var t = task[0];
            t.time = System.currentTimeMillis();
            t.action = action;
        }  else{
            insert TimerTask{
                name: name
                time: System.currentTimeMillis()
                action: action
            } into tasks;
        }
        
        //System.out.println("[timer] tasks: {tasks}");
    } 
    
    public function start () {  
        //System.out.println("[timer] start");
        
        var timeline = Timeline {
            keyFrames:  KeyFrame { time: 0.6s,  action: function() { run() }} 
            repeatCount: java.lang.Double.POSITIVE_INFINITY
        };
        timeline.start(); 

//    var runnable = Runnable {
//        public function run(){
//            while(true) {
//                Thread.sleep(TIMEOUT/2);
//                
//                var time = System.currentTimeMillis();
//                for(task in tasks){
//                    if ( ( task.time + TIMEOUT ) < time.doubleValue() ){
//                        delete task from tasks;
//                        task.action();
//                    }
//                }
//            }
//        }
//    };
//
//    (new Thread(runnable)).start();

    } 
    
    function run(){
        //System.out.println("timer: run");
        var time = System.currentTimeMillis();
        for(keyTimer in tasks){
            if ( ( keyTimer.time + TIMEOUT ) < time.doubleValue() ){
                delete keyTimer from tasks;
                keyTimer.action();
            }
        }
    
    }
}
