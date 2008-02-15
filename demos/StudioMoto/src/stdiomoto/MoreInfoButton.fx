package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MoreInfoButton extends CompositeNode {
    attribute action: function();

    function composeNode() {
        Group {
            cursor: HAND
            onMouseClicked: function(e) {(this.action)();}
            content:
            [ImageView {
                // 115x37
                image: Image {url: "{__DIR__}/Image/80.png"}
                
            },
            ImageView {
                visible: bind hover
                image: Image {url: "{__DIR__}/Image/81.png"}
                
            },
            Text {
                font: new Font("ARIAL", "BOLD", 11)
                content: "MORE INFO"
                transform: translate(115/2, 37/2)
                valign: VerticalAlignment.MIDDLE
                halign:  HorizontalAlignment.CENTER
                fill: bind if (hover) Color.BLACK else Color.WHITE
        }]}
    }
}

MoreInfoButton {}
