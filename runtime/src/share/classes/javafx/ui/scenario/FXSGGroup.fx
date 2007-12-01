package javafx.ui.scenario;
import javafx.ui.*;
import javafx.ui.canvas.*;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;

public class FXSGGroup extends FXSGNode {
    private attribute group: SGGroup;
    public attribute content: FXSGNode[]
        on insert [ndx] (n) {
            if (group <> null) {
                group.add(ndx, n.getNode());
            }        
        }
        on replace [ndx] (oldValue) {
            if (group <> null) {
                group.remove(oldValue.getNode());
                group.add(ndx, content[ndx].getNode());
            }
        }
        on delete [ndx] (n) {
            if (group <> null) {
                group.remove(n.getNode());
            } 
        };
        
    public function createNode(): SGNode {
        group = new SGGroup();
        foreach( i in [0..sizeof content exclusive]) {
            group.add(i, content[i].getNode());
        }
        return group as SGNode;
    }
}


