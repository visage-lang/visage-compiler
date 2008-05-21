/**
  * Checks the value change of keyFrames. 4 supported data types are checked.
  * 
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

var strValue: String = "cat";
var intValue: Integer = -1;
var numValue: Number = -1.0;
var booValue: Boolean = false;

var t: Timeline = Timeline {
    keyFrames: [
        KeyFrame {
            time: 0ms
            values: [ strValue => "dog", intValue => 0, numValue => 0.0, booValue => true ]
            action: function() {
                if(strValue <> "dog" or intValue <> 0 or numValue <> 0.0 or booValue <> true) {
                    System.out.println("fail");
                }
            }
        },
        KeyFrame {
            time: 300ms
            values: [ strValue => "", intValue => Integer.MAX_VALUE, numValue => Double.POSITIVE_INFINITY, booValue => false ]
            action: function() {        
                if(strValue <> '' or intValue <> 2147483647 or numValue <> Double.POSITIVE_INFINITY/*Double.longBitsToDouble(0x7ff0000000000000L)*/ or booValue <> false) {
                    System.out.println("fail");
                } else {
                    System.out.println("pass");
                }
            }
        }
    ]
} // Double.longBitsToDouble(0x7ff0000000000000L)

t.start();
