package javafx.ui.scenario;
import javafx.ui.*;
import javafx.ui.canvas.*;
import com.sun.scenario.scenegraph.JSGPanel;

public class FXSGCanvas extends Widget {
    private attribute jsg: JSGPanel;
    private attribute root: FXSGGroup = FXSGGroup {
        content: bind content
    };
    public attribute content: FXSGNode[];
    public function createComponent():javax.swing.JComponent{
        jsg = new JSGPanel(); 
        jsg.setScene(root.getNode());
        return jsg;
    }
}

