package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class PromotionsPanel extends CompositeNode {
}

function PromotionsPanel.composeNode() =
Group {
    content:
    [VBox {
        content:
        [Text {
            font: new Font("ARIAL", "BOLD", 11)
            content: "Motorola Talk and Tunes HT820Bundle"
            fill: bind if hover then yellow else white
        },
        Text {
            font: new Font("ARIAL", "PLAIN", 11)
            
            fill: bind if hover then white else yellow
            transform: translate(0, 5)
            content: "Listen to music wherever you go"
        },
        ImageView {
            image: {url: "{__DIR__}/Image/78.png"}
        },
        Group {
            transform: translate(0, -20)
            cursor: HAND
            var: button
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
            }
            ]
        }
        ]
    }]
};

Canvas {
    background: black
    content: PromotionsPanel {}
}


