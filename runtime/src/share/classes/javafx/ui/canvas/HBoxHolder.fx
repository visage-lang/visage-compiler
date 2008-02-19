package javafx.ui.canvas; 

import javafx.ui.*;
import com.sun.scenario.scenegraph.*;

public class HBoxHolder extends Node {
    attribute hbox:HBox;
    attribute content: Node;
    attribute x:Number = bind content.currentX on replace { hbox.doLayout(); }
    attribute w:Number = bind content.currentWidth on replace { hbox.doLayout(); };
    protected function createNode():SGNode {
        content.parentCanvasElement = this;
        return content.getNode();
    }
}
