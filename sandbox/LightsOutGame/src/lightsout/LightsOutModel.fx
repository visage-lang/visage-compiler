/*
 * This file is created with Netbeans 6 Plug-In for JavaFX
 */
package lightsout;
import javafx.ui.*;
import javafx.ui.canvas.*;
import lightsout.*;
import lightsout.ColorConstants;

import com.sun.scenario.scenegraph.SGNode;


// class definitions
public class LightsOutModel extends Group {
    var rows: Row[];
    function randomize():Void{
        if(sizeof rows > 0) {
            //clear first
            for(row in rows) {
                for(light in row.lights]) {
                    light.setSelected(false);
                }
            }
            if(sizeof rows > 3) {
            //set two values
                this.rows[2].lights[2].toggle();
                this.rows[3].lights[1].toggle();
            }
            moveCount = 0;
            finished = false;
            //this.rows[1].lights[2].toggle();
        }
    }

    function checkFinished(){
        var won = true;
        for(row in rows) {
            for(light in row.lights]) {
                if(light.selected) {
                    won = false;
                }
            }
        }
        finished = won;
    }
    var moveCount: Number;
    var finished: Boolean = false;
    public var content: Node[] = bind rows;
    public function createNode(): SGNode {
        for(i in [0..4]) {
            var self = this;
            var row = Row {model:self};
            for(j in [0..4]) {
                var x = j;
                var y = i;
                var light = Light{gx:x, gy:y, row:row, model:self};
                insert light into row.lights;
            }
            insert row into rows;
        }
        return super.createNode();
    }
    
}







