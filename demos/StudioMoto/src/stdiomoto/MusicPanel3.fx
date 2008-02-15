package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;


public class MusicPanel3 extends CompositeNode {
    function composeNode():Node {
        Clip {
            shape: Rect {height: 160, width: 220}
            content:
            Group {
                transform: Transform.translate(-60, 0)
                content:
                [ImageView {
                },
                ImageView {visible: false
                    transform: Transform.translate(230, 145)
                    valign: VerticalAlignment.BOTTOM, halign: HorizontalAlignment.TRAILING
                    image: Image{url: "{__DIR__}/Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(80, 0)
                    image: Image{url: "{__DIR__}/Image/86.png"}
                },
                ,
                VBox {
                    transform: Transform.translate(150, 5)
                    content:
                    [View {
                        content:
                        Label {
                            text: "<html><div style='color:yellow;font-size:9pt;width:135;font-face:arial;'>Motorola brought the SLVR to the House of Blues Chicago with the Fray</div></html>"
                        }
                    },
                    MoreInfoButton {
                        transform: Transform.translate(0, 8)
                    }]
                }]
            }
        };
 
    }
}


MusicPanel3 {}

