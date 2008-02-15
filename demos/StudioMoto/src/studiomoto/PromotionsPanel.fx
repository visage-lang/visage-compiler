package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class PromotionsPanel extends CompositeNode {
    attribute base: java.net.URL; // work around for __DIR__
    function composeNode():Node {
        Group {
            content:
            [VBox {
                content:
                [Text {
                    font: Font.Font("ARIAL", ["BOLD"], 11)
                    content: "Motorola Talk and Tunes HT820Bundle"
                    fill: bind if (hover) then Color.YELLOW else Color.WHITE
                },
                Text {
                    font: Font.Font("ARIAL", ["PLAIN"], 11)

                    fill: bind if (hover) then Color.WHITE else Color.YELLOW
                    transform: Transform.translate(0, 5)
                    content: "Listen to music wherever you go"
                },
                ImageView {
                    image: Image {url: "{base}/Image/78.png"}
                },
                Group {
                    transform: Transform.translate(0, -20)
                    cursor: Cursor.HAND
                    var button = this
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
                        valign: VerticalAlignment.MIDDLE, halign: HorizontalAlignment.CENTER
                        fill: bind if (hover) Color.BLACK else Color.WHITE
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



