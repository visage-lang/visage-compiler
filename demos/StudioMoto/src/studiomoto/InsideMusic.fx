package studiomoto;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.ext.swing.*;

public class InsideMusic extends MotoPanel {
    override var title = Group {
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
    
    override  var content = Group {
        content:
        [ComponentView {
            component: Label {
                text:
                "<html><div style='width:200;color:white;font-size:10pt;font-face:arial;'>To the right we have today's top music news feeds provided by 
    <a href='' style='color:yellow;'>ROLLING STONE</a>
    </div>
    </html>"
            }
        }]
    };
    
}

Canvas {
    background: Color.BLACK
    content:
    InsideMusic {height: 180, width: 250}
}

