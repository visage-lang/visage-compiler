package studiomoto;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.ext.swing.*;

public class InsideMusicPane extends MotoPanel {
    
    override attribute title = Group {
        content:
        [ComponentView {
            component: Label {
                text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Inside</span><span style='color:yellow;'>Music</span></div></html>"
            }
        },
        Text {content: "COURTESY OF ROLLING STONE", fill: Color.WHITE,
            textOrigin: TextOrigin.TOP
            font: Font.font("Arial", FontStyle.PLAIN, 8),
            horizontalAlignment: HorizontalAlignment.TRAILING
            transform: bind Transform.translate(width-30, 8)
        }]
    };
    override attribute content = Group {
        content:
        [Group {
        // items
        },
        ImageView {
            transform: bind Transform.translate(0, 120)
            image: Image {url: "{__DIR__}Image/95.png"}
        },
        ImageView {
            horizontalAlignment: HorizontalAlignment.TRAILING
            verticalAlignment: VerticalAlignment.BOTTOM
            transform: bind Transform.translate(width -30, height -35)
            image: Image {url: "{__DIR__}Image/88.png"}
        }]
    };
    
}



Canvas {
    background: Color.BLACK
    content:
    InsideMusic {height: 180, width: 250}
}

