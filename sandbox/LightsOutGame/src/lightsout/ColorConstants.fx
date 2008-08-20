/*
 * This file is created with Netbeans 6 Plug-In for JavaFX
 */
package lightsout;

import javafx.ui.canvas.*;
import javafx.ui.*;

public class ColorConstants {
    public static var slate:Paint= LinearGradient{ startX:0, startY:0, endX:0, endY:1, 
         stops: [Stop { offset:0, color:Color.rgb(0x34,0x34,0x34)}, 
                    Stop{offset:0.5,color:Color.rgb(0x55,0x55,0x55)},
                    Stop{offset:1.0,color:Color.rgb(0x43,0x43,0x43)}]};
    public static var blueGrad:Paint= LinearGradient{ startX:0, startY:0, endX:0, endY:1, 
         stops: [Stop { offset:0, color:Color.rgb(0,0x33,0xff)}, 
                    Stop{offset:0.5,color:Color.rgb(0,0x55,0xff)},
                    Stop{offset:1.0,color:Color.BLUE}]};
}

