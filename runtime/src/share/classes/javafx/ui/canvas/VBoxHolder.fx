package javafx.ui.canvas; 

import javafx.ui.*;
import com.sun.scenario.scenegraph.*;

public class VBoxHolder extends Node {
    attribute vbox:VBox;
    attribute content: Node;
    attribute y:Number = bind content.currentY on replace { vbox.doLayout(); }
    attribute h:Number = bind content.currentHeight on replace { vbox.doLayout(); };
    init {}
    protected function createNode():SGNode {
        content.parentCanvasElement = this;
        return content.getNode();
    }
}
