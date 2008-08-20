package casual;

import javafx.ui.animation.*;
import java.util.Calendar;
import java.text.DateFormat;

class Timer
{
    var tick: Timeline = 
        Timeline {
            keyFrames:
                KeyFrame { 
                    keyTime: 1s, 
                    action: function() {
                        current = Calendar.getInstance();
                    }
                }
            repeatCount: java.lang.Double.POSITIVE_INFINITY
        };

    var running: Boolean
        on replace {
            if (running) {
                tick.start();
            } else {
                tick.stop();
            }
        };
    
    var current: Calendar;
    var formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    
    public var hours: Number = bind current.get(Calendar.HOUR_OF_DAY);
    public var minutes: Number = bind current.get(Calendar.MINUTE);
    public var seconds: Number = bind current.get(Calendar.SECOND);
    
    public function toString(): String {
        return formatter.format(current.getTime());
    };
}

