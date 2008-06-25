package assortis.sources.language.javafx.events;

import javafx.input.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;

var x = 50.0;
var y = 30.0;

Circle{
    centerX: bind x
    centerY: bind y
    radius: 25
    fill: Color.ORANGE
    stroke: Color.GREEN
    
    onMouseDragged: function(e:MouseEvent){
        x = e.getX();
        y = e.getY();
    }
    
}
