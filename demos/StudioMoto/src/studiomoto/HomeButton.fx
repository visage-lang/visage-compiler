package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;

public class HomeButton extends CompositeNode {
    attribute base: java.net.URL; // work around for __DIR__
    public attribute action: function();
    function composeNode() : Node {
        Group {
        cursor: Cursor.HAND
        content: ImageView {
            onMouseClicked: function(e) {(this.action)();}
            image: Image {url: "{base}/Image/94.png"}
        }}
    }
};

HomeButton {}

