package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MusicPanel1 extends CompositeNode {
    attribute base: java.net.URL; // work around for __DIR__
    function composeNode():Node {
        Clip {
            shape: Rect {height: 160, width: 220}
            content:
            Group {
                transform: Transform.translate(-50, 0)
                content:
                [ImageView {
                },
                ImageView {
                    transform: Transform.translate(230, 145)
                    valign: VerticalAlignment.BOTTOM, halign: HorizontalAlignment.TRAILING
                    image: Image{url: "{base}/Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(0, 0)
                    image: Image {url: "{base}/Image/82.png"}
                },
                VBox {
                    transform: Transform.translate(150, 0)
                    content:
                    [View {
                        content:
                        Label {
                            text: "<html><div style='color:yellow;font-size:9pt;width:120;font-face:arial;'>Welcome to the world of songwriter Geoff Byrd</div></html>"
                        }
                    },
                    MoreInfoButton {
                    }as Node]
                }]
            }
        }; 
    }
}



MusicPanel1 {}

