package javafx.util;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

import java.lang.System;
import javafx.animation.*;

public class Timer {
    
    public attribute TIMEOUT = 600;
    public attribute tasks: TimerTask[];
    
    private attribute timeline: Timeline = Timeline {
            keyFrames:  KeyFrame { time: 0.6s,  action: function() { run() }} 
            repeatCount: java.lang.Double.POSITIVE_INFINITY
        };
        
    public function addTask(action: function() ) {
        addTask("Task", action);
    }
    
    public function addTask (name: String, action: function() ) {
        
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
    } 
    
    private function run(){
        var time = System.currentTimeMillis();
        for(keyTimer in tasks){
            if ( ( keyTimer.time + TIMEOUT ) < time.doubleValue() ){
                delete keyTimer from tasks;
                keyTimer.action();
            }
        }
    }

    init{
            timeline.start();
    }
}