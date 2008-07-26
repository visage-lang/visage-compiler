package studiomoto;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.ext.swing.*;

public class PromotionsPanel extends CustomNode {
    function create():Node {
        Group {
            content:
            [VBox {
                content:
                [Text {
                    textOrigin: TextOrigin.TOP
                    font: Font.font("ARIAL", FontStyle.BOLD, 11)
                    content: "Motorola Talk and Tunes HT820Bundle"
                    fill: bind if (isMouseOver()) then Color.YELLOW else Color.WHITE
                },
                Text {
                    textOrigin: TextOrigin.TOP
                    font: Font.font("ARIAL", FontStyle.PLAIN, 11)
                    fill: bind if (isMouseOver()) then Color.WHITE else Color.YELLOW
                    transform: Transform.translate(0, 5)
                    content: "Listen to music wherever you go"
                },
                ImageView {
                    image: Image {url: "{__DIR__}Image/78.png"}
                },
                Group {
                    var button = this;
                    transform: Transform.translate(0, -20)
                    cursor: Cursor.HAND
                    content:
                    [ImageView {
                        // 115x37
                        image: Image {url: "{__DIR__}Image/80.png"}

                    },
                    ImageView {
                        visible: bind isMouseOver()
                        image: Image {url: "{__DIR__}Image/81.png"}

                    },
                    Text {
                        textOrigin: TextOrigin.TOP
                        font: Font.font("ARIAL", FontStyle.BOLD, 11)
                        content: "MORE INFO"
                        transform: Transform.translate(115/2, 37/2)
                        verticalAlignment: VerticalAlignment.CENTER, 
                        horizontalAlignment: HorizontalAlignment.CENTER
                        fill: bind if (isMouseOver()) Color.BLACK else Color.WHITE
                    }
                    ]
                }
                ]
            }]
        };
    }
}

Canvas {
    background: Color.BLACK
    content: PromotionsPanel {} as Node
}



