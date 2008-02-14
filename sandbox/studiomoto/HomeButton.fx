package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;

public class HomeButton extends CompositeNode {
    public attribute action: function();
}

function HomeButton.composeNode() =
Group {
    cursor: HAND
    content: ImageView {
        onMouseClicked: operation(e) {(this.action)();}
        image: {url: "{__DIR__}/Image/94.png"}
    }
};

HomeButton {}
