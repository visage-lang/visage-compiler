/*
 * Light.fx
 *
 * Created on Dec 20, 2007, 4:02:22 PM
 */

package lightsout;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
/**
 * @author jclarke
 */

public class Light extends Group {
    var gx: Number;
    var gy: Number;
    var selected: Boolean;
    var row: Row;
    function setSelected(t:Boolean) {
        selected = t;
        if(selected) { 
            view.fill = Color.WHITE;
        } else {
            view.fill = ColorConstants.blueGrad; 
        }
    }
    function triggerAdjacent() {
        if(sizeof row.lights > gx+1) {
            row.lights[gx.intValue()+1].flip();
        }
        if(0<= gx-1) {
            row.lights[gx.intValue()-1].flip();
        }
        var model = row.model;
        if(sizeof model.rows > gy+1) {
            model.rows[gy.intValue()+1].lights[gx.intValue()].flip();
        }
        if(0 <= gy-1) {
            model.rows[gy.intValue()-1].lights[gx.intValue()].flip();
        }
    }
    function toggle() {
        setSelected(not selected);
        triggerAdjacent();
        model.moveCount++;
        model.checkFinished();
    }
    function flip() {
        setSelected(not selected);
    }
    var view: Rect;
    var x:Number;
    var y:Number;
    var model:LightsOutModel;
    public var transform: Transform[] = 
                    bind [ Transform.translate( gx*55+15+x, gy*55+15+y)];
    var onMousePressed:function(:CanvasMouseEvent):Void  = function(e:CanvasMouseEvent):Void { toggle(); };
    var strokeAlpha = 0.0;
    init {
        var size = 50;
        //var strokeAlpha = 0.0;
        var shad = Rect {fill: Color.rgba(0x0,0x0,0x0,0x80), x: 2, y:2, width:size, height: size, arcWidth:10, arcHeight:10,  };
        view = Rect {fill:Color.WHITE, x: 0, y:0, width:size, height: size, arcWidth:10, arcHeight:10,
            stroke: Color{red:0.9, green:0.9,blue:0.9, opacity:bind strokeAlpha}, strokeWidth: 3};
        content = [shad, view];
        view.onMouseEntered = function(e:CanvasMouseEvent):Void  { 
                //strokeAlpha = [0.0..1.0 step 0.1] dur 250 while hover; 
                var anime:Animation = Animation{
                    duration:250, 
                    sequence:[0.3..1.0 step 0.1]
                    instance: this
                    property: "strokeAlpha"
                    stopProperty: "hover"
                };
                anime.start();              
        };
        view.onMouseExited  = function(e:CanvasMouseEvent):Void  { 
            //strokeAlpha = 0.0;}; [1.0..0.0 step 0.1] dur 250; 
                var anime:Animation = Animation{
                    duration:250, 
                    sequence:[1.0..0.0 step -0.1]
                    instance: this
                    property: "strokeAlpha"
                };
                anime.start();              
        };
        view.onMousePressed = function(e:CanvasMouseEvent):Void  { };
        view.onMouseReleased = function(e:CanvasMouseEvent):Void  { toggle(); };
    }
    
}
