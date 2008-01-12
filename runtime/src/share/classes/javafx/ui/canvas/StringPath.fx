package javafx.ui.canvas;
import com.sun.scenario.scenegraph.SGShape;
import java.awt.geom.GeneralPath;
import com.sun.javafx.api.ui.path.parser.AWTPathProducer;
import java.io.StringReader;

/*
 * Shape which constructs its content by parsing SVG path data from a String
 * TBD: think of a better name for it
 */

public class StringPath extends Shape {

    private attribute path2d: GeneralPath;
    private attribute pathShape: SGShape;

    public attribute content: String on replace {
        if (pathShape <> null) {
            parseContent();
        }
    }
    
    function parseContent():Void {
        path2d = AWTPathProducer.createPath(new StringReader(content), fillRule.id);
        pathShape.setShape(path2d);
    }

    protected function createShape():SGShape {
        pathShape = new SGShape();
        parseContent();
        return pathShape;
    }
}
