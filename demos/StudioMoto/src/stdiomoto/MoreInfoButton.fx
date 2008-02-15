package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MoreInfoButton extends CompositeNode {
    attribute action: function()?;
}

function MoreInfoButton.composeNode() =
Group {
    cursor: HAND
    onMouseClicked: operation(e) {(this.action)();}
    content:
    [ImageView {
        // 115x37
        image: {url: "{__DIR__}/Image/80.png"}
        
    },
    ImageView {
        visible: bind hover
        image: {url: "{__DIR__}/Image/81.png"}
        
    },
    Text {
        font: new Font("ARIAL", "BOLD", 11)
        content: "MORE INFO"
        transform: translate(115/2, 37/2)
        valign: MIDDLE, halign: CENTER
        fill: bind if hover then black else white
    }]
};

MoreInfoButton {}
