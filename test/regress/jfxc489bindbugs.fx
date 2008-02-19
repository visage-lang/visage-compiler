/* Regression test: parse error 'for' vs 'foreach'
 * @test
 */
import javafx.ui.*;

public class Model {
    attribute names: String[] = ["Chris", "Tom", "Per", "Robert", "Lubo", "Brian"];
    attribute selection: Integer = 0;
}
var model = Model {};
Frame {
    visible: true
    content:
    BorderPanel {
        center: ListBox {
            cells: bind for (name in model.names) ListCell { text: name }
            selection: bind model.selection with inverse        
            
        }
        bottom:
        FlowPanel {
            content:
            [Button {
                text: "Add"
                action: function() { insert "Name" into model.names ; }
            },
            Button {
                text: "Delete"
                action: function() { delete model.names[model.selection.intValue()]; }
            }]
        }
        
    }
}

