package javafx.ui.scenario;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.awt.geom.AffineTransform;
import com.sun.scenario.scenegraph.SGNode;

public abstract class FXSGNode extends Transformable {
    private attribute node: SGNode;
    public function getNode(): SGNode {
        if (node == null) {
            node = createNode();
            //TODO - no such method in SGNode
            //node.setTransform(affineTransform);
            //node.setOpacity(opacity);
        }
        return node;
    }
    protected abstract function createNode(): SGNode;

    public attribute opacity: Number = 1 on replace {
        //TODO - no such method in SGNode
        //node.setOpacity(opacity);
    };
    public attribute id: String;
    
    public function onTransformChanged(t:AffineTransform):Void {
        //TODO - no such method in SGNode
        //node.setTransform(t);
    }
}

