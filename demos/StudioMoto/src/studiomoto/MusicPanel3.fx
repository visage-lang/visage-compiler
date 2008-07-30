package studiomoto;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;


public class MusicPanel3 extends CustomNode {
    override function create():Node {
        Group {
            clip: Rectangle {height: 160, width: 220}
            content:
            Group {
                transform: Transform.translate(-60, 0)
                content:
                [ImageView {
                },
                ImageView {visible: false
                    transform: Transform.translate(230, 145)
                    verticalAlignment: VerticalAlignment.BOTTOM, 
                    horizontalAlignment: HorizontalAlignment.TRAILING
                    image: Image{url: "{__DIR__}Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(80, 0)
                    image: Image{url: "{__DIR__}Image/86.png"}
                },
                VBox {
                    transform: Transform.translate(150, 5)
                    content:
                    [ComponentView {
                        component:
                        Label {
                            text: "<html><div style='color:yellow;font-size:9pt;width:135;font-face:arial;'>Motorola brought the SLVR to the House of Blues Chicago with the Fray</div></html>"
                        }
                    },
                    MoreInfoButton {
                        transform: Transform.translate(0, 8)
                    } as Node]
                }]
            }
        };
 
    }
}


MusicPanel3 {}


