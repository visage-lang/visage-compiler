package casual;

import javafx.ui.animation.*;
import java.util.Calendar;
import java.text.DateFormat;

class Timer
{
    attribute tick: Timeline = 
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

    attribute running: Boolean
        on replace {
            if (running) {
                tick.start();
            } else {
                tick.stop();
            }
        };
    
    attribute current: Calendar;
    attribute formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    
    public attribute hours: Number = bind current.get(Calendar.HOUR_OF_DAY);
    public attribute minutes: Number = bind current.get(Calendar.MINUTE);
    public attribute seconds: Number = bind current.get(Calendar.SECOND);
    
    public function toString(): String {
        return formatter.format(current.getTime());
    };
}

