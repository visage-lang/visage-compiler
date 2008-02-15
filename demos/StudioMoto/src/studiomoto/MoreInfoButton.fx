package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MoreInfoButton extends CompositeNode {
    attribute base: java.net.URL; // work around for __DIR__
    attribute action: function();

    function composeNode():Node {
        Group {
            cursor: Cursor.HAND
            onMouseClicked: function(e) {(this.action)();}
            content:
            [ImageView {
                // 115x37
                image: Image {url: "{base}/Image/80.png"}
                
            },
            ImageView {
                visible: bind hover
                image: Image {url: "{base}/Image/81.png"}
                
            },
            Text {
                font: Font.Font("ARIAL", ["BOLD"], 11)
                content: "MORE INFO"
                transform: Transform.translate(115/2, 37/2)
                valign: VerticalAlignment.MIDDLE
                halign:  HorizontalAlignment.CENTER
                fill: bind if (hover) Color.BLACK else Color.WHITE
        }]}
    }
}

MoreInfoButton {}

