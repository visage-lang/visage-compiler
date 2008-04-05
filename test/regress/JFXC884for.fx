/**
 * Regression test JFXC-884 : Cannot find constructor SimpleBoundComprehension
 *
 * @test
 */

import javafx.ui.*;
import javafx.ui.canvas.*;

Group {
   var font = Font.Font("Tahoma", ["PLAIN"], 8);
  content: bind for (x in [0..100 step 5] )
       Group {
           content: [Line {
               stroke: Color.BLACK
               x1: x
               x2: x
               y1: if (x % 100 == 0)
                   then 0
                   else if ( x % 10 == 0)
                   then 9
                   else 12
               y2: 15
           },
           if (x % 100 == 0) then Text {content: "{x}" x: x+2, font: font} else Text{}
           ]
       }
}; 
