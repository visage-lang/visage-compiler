package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;

public class HomeButton extends CompositeNode {
    public attribute action: function();
    function composeNode() : Node {
        Group {
        cursor: Cursor.HAND
        content: ImageView {
            onMouseClicked: function(e) {(this.action)();}
            image: Image {url: "{__DIR__}/Image/94.png"}
        }}
    }
};

HomeButton {}
