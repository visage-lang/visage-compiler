package studiomoto;
import javafx.scene.*;
import javafx.scene.image.*;

public class HomeButton extends CustomNode {
    public var action: function();
    override function create() : Node {
        Group {
        cursor: Cursor.HAND
        content: ImageView {
            onMouseClicked: function(e) {(this.action)();}
            image: Image {url: "{__DIR__}Image/94.png"}
        }}
    }
};

HomeButton {}

