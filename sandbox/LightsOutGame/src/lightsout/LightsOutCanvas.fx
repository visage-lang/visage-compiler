/*
 * This file is created with Netbeans 6 Plug-In for JavaFX
 */

package lightsout;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import java.lang.System;
import lightsout.ColorConstants;


public class LightsOutCanvas extends Group { 
    var model: LightsOutModel;
    init {
        model = LightsOutModel{};
        model.randomize();

        //background and grid
        insert Rect {width:400,height:300,fill:ColorConstants.slate } into content;
        insert model into content;

        //reset button
        insert BlueButton {
            text : "Reset"
            font: Font.Font("Arial",["BOLD"],20)
            transform: [Transform.translate(305,15)]
            width: 80, height: 30
            onMousePressed : function(e:CanvasMouseEvent):Void { model.randomize(); }
            } into content;

        //score
        insert Text {content:"Moves", x:310, y:90, fill:Color.WHITE, font: Font.Font("Arial",["BOLD"],20) } into content;
        insert Text {content: bind model.moveCount.intValue().toString(),
            x: 343, y: 120, fill:Color.WHITE, font: Font.Font("Arial",["BOLD"],60),  halign: HorizontalAlignment.CENTER  } into content;

        //winning text
        insert Group {
            content : [
                Rect {x:0, y:0, width: 250, height: 70, fill: Color.rgba(0,0,0,200), arcWidth:30, arcHeight:30 },
                Text {content: "You Won!",  x:20, y:20, fill:Color.WHITE, font: Font.Font("Arial",["BOLD"],45) }
                ]
            transform : [Transform.translate(25,100)]
            visible: bind model.finished,
        } into content;

        //quit button
        insert BlueButton { text: "Quit", font: Font.Font("Arial",["BOLD"],20), transform: [Transform.translate(305,255)], width: 80, height: 30,
            onMousePressed: function(e:CanvasMouseEvent):Void  { System.exit(0); }
        } into content;
    }

}

var canvas = LightsOutCanvas{};
Canvas { content: [canvas] }

