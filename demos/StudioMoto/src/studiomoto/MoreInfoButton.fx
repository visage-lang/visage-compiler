package studiomoto;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.ext.swing.*;

public class MoreInfoButton extends CustomNode {
    attribute action: function();

    override function create():Node {
        Group {
            cursor: Cursor.HAND
            onMouseClicked: function(e) {if(action != null) action();}
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
                verticalAlignment: VerticalAlignment.CENTER
                horizontalAlignment:  HorizontalAlignment.CENTER
                fill: bind if (isMouseOver()) Color.BLACK else Color.WHITE
        }]}
    }
}

MoreInfoButton {}

