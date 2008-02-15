package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;


public class MusicPanel3 extends CompositeNode {
}


function MusicPanel3.composeNode() =
Clip {
    shape: Rect {height: 160, width: 220}
    content:
    Group {
        transform: translate(-60, 0)
        content:
        [ImageView {
        },
        ImageView {visible: false
            transform: translate(230, 145)
            valign: BOTTOM, halign: TRAILING
            image: {url: "{__DIR__}/Image/83.png"}
        },
        ImageView {
            transform: translate(80, 0)
            image: {url: "{__DIR__}/Image/86.png"}
        },
        ,
        VBox {
            transform: translate(150, 5)
            content:
            [View {
                content:
                Label {
                    text: "<html><div style='color:yellow;font-size:9pt;width:135;font-face:arial;'>Motorola brought the SLVR to the House of Blues Chicago with the Fray</div></html>"
                }
            },
            MoreInfoButton {
                transform: translate(0, 8)
            }]
        }]
    }
};

MusicPanel3 {}

