package javafx.ui.scenario;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.awt.geom.RoundRectangle2D;
import com.sun.scenario.scenegraph.SGShape;
import java.lang.System;

public class FXSGRect extends FXSGShape {
    private attribute awtRect: RoundRectangle2D.Double =
        bind lazy new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight)
        on replace {
            System.out.println("setting shape...");
            shape.setShape(awtRect);
        };
    attribute arcHeight: Number;
    attribute arcWidth: Number;
    attribute x: Number;
    attribute y: Number;
    attribute width: Number;
    attribute height: Number;
    //protected function createShape(): SGShape;
    protected attribute shape: SGShape on replace {
        System.out.println("setting shape...");
        shape.setShape(awtRect);
        //TODO the following method is not found in SGShape
        // but repaint(true) is called form setShape so the following
        // is probably redundant anyway.
        //shape.repaint();
    };
}


