/*
 * This file is created with Netbeans 6 Plug-In for JavaFX
 */
package lightsout;

import lightsout.ColorConstants;
import javafx.ui.canvas.*;
import javafx.ui.*;
import java.lang.System;

import javafx.ui.animation.*;

public class BlueButton extends Group {
    attribute text : String;
    attribute strokeAlpha : Number = 0.3;
    attribute font : Font=  Font.Font("Arial",["BOLD"],12);
    attribute width : Number = 80;
    attribute height : Number = 30;
    attribute buttonAction: function(e:CanvasMouseEvent):Void;
    
    
    public attribute content: Node[] = [
        Rect { fill: Color.rgb(0x30,0x30,0x30), x:2, y:2, width: bind width, height: bind height, arcWidth:6, arcHeight: 6 },
        Rect {
            fill: ColorConstants.blueGrad, 
            x:0, y:0, width: bind width, height: bind height,
            arcWidth:6, arcHeight:6,
            //TODO JXFC-469 stroke: Color{red:1,green:1,blue:1,opacity: bind strokeAlpha}
            onMouseEntered : function(e:CanvasMouseEvent):Void { 
                //strokeAlpha = [0.3..1.0 step 0.1] dur 150 while hover; 
                var anime:Animation = Animation{
                    duration:150, 
                    sequence:[0.3..1.0 step 0.1]
                    instance: this
                    property: "strokeAlpha"
                    stopProperty: "hover"
                };
                anime.start();  
            }
            onMouseExited : function(e:CanvasMouseEvent):Void {
                //strokeAlpha = [1.0..0.3 step 0.1] dur 150 while not hover; 
                var anime:Animation = Animation{
                    //timingTarget:timingTarget;
                    duration:150, 
                    sequence:[1.0..0.3 step -0.1]
                    instance: this
                    property: "strokeAlpha"
                    stopProperty: "hover"    
                    stopValue: false
                };
                anime.start();   
            }
            //TODO JFXC-466 onMousePressed : buttonAction
        },
        Text{ content: bind text, 
            x: bind width/2, halign: HorizontalAlignment.CENTER, 
            y: bind height/2, valign: VerticalAlignment.CENTER, 
            fill: Color.WHITE, font : bind font }
        ];
        
}


