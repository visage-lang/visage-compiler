package javafx.ui.scenario;
import javafx.ui.*;
import javafx.ui.canvas.*;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGNode;
import java.awt.geom.RoundRectangle2D.Double;
import java.lang.System;
//import com.sun.scenario.scenegraph.SGShape.Mode;

public class FXSGShape extends FXSGNode, AbstractVisualNode {
    private attribute awtStroke: java.awt.Paint = bind lazy stroke.getPaint()
        on replace {
            shape.setDrawPaint(awtStroke);
        };    
    private attribute awtFill: java.awt.Paint = bind lazy fill.getPaint()
        on replace {
            shape.setFillPaint(awtFill);
        };
    protected attribute shape: SGShape on replace {
        System.out.println("setting fill {awtFill} and stroke {awtStroke}");
        shape.setFillPaint(awtFill);
        shape.setDrawPaint(awtStroke);
        shape.setMode(SGShape.Mode.STROKE_FILL);
    };
    protected function createShape(): SGShape {
        return getShape();
    }
    public function getShape(): SGShape{
        if (shape == null) {
            shape = new SGShape();
        }
        return shape;
    }
    public function createNode(): SGNode {
        return getShape();
    }
    
    public function updateStroke():Void {
        if(shape <> null)
            shape.setDrawPaint(awtStroke);
    }
    public function getFill(): java.awt.Paint {
        return awtFill;
    };
    public function getStroke():java.awt.Paint {
        return awtStroke;
    }
    //public function getPaint(): java.awt.Paint;
}



