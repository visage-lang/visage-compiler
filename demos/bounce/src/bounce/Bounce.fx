package bounce;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;
import java.lang.Object;
import java.lang.Double; // hack

var x = 0.0;
var pf = PointerFactory {};
var bxp = bind pf.make(x);
var xp = bxp.unwrap();
var LINEAR = Interpolator {
    public function interpolate(oldValue:Object,
                                newValue:Object,
                                t:Number):Object 
    {
        var x1 = oldValue as Double; // hack
        var x2 = newValue as Double; // hack
        return x1 + (x2 - x1)*t;
    }
};


var timeline = Timeline {
        toggle: true
        keyFrames:
        [KeyFrame {
             keyTime: 0s
             keyValues:
             KeyValue {
                 target: xp;
                 value: 0.0;
             }
        },
        KeyFrame {
            keyTime: 1s
            keyValues:
            KeyValue {
                target: xp;
                value: 500.0;
                interpolate: LINEAR
            }
        }]
}

Frame {
    height: 500, width: 800;
    visible: true;
    content: Canvas {
        content: 
        [Rect {
            height: 500;
            width: 800;
            fill: Color.WHITE
        },
        Rect {
            height: 50, width: 50;
            fill: Color.GREEN;
            stroke: Color.YELLOW;
            x: bind x with inverse;
            onMouseClicked: function(e):Void {
                timeline.start();
            }
        }]
    }
}


