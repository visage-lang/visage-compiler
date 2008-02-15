package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MusicPanel1 extends CompositeNode {
}

function MusicPanel1.composeNode() =
Clip {
    shape: Rect {height: 160, width: 220}
    content:
    Group {
        transform: translate(-50, 0)
        content:
        [ImageView {
        },
        ImageView {
            transform: translate(230, 145)
            valign: BOTTOM, halign: TRAILING
            image: {url: "{__DIR__}/Image/83.png"}
        },
        ImageView {
            transform: translate(0, 0)
            image: {url: "{__DIR__}/Image/82.png"}
        },
        ,
        VBox {
            transform: translate(150, 0)
            content:
            [View {
                content:
                Label {
                    text: "<html><div style='color:yellow;font-size:9pt;width:120;font-face:arial;'>Welcome to the world of songwriter Geoff Byrd</div></html>"
                }
            },
            MoreInfoButton {
            }]
        }]
    }
};

MusicPanel1 {}
