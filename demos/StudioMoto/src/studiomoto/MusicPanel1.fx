package studiomoto;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;

public class MusicPanel1 extends CustomNode {
    override function create():Node {
        Group {
            clip: Rectangle {height: 160, width: 220}
            content:
            Group {
                transform: Transform.translate(-50, 0)
                content:
                [ImageView {
                },
                ImageView {
                    transform: Transform.translate(230, 145)
                    verticalAlignment: VerticalAlignment.BOTTOM, 
                    horizontalAlignment: HorizontalAlignment.TRAILING
                    image: Image{url: "{__DIR__}Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(0, 0)
                    image: Image {url: "{__DIR__}Image/82.png"}
                },
                VBox {
                    transform: Transform.translate(150, 0)
                    content:
                    [ComponentView {
                        component:
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

function run() {
    MusicPanel1 {}
}

