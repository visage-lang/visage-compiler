package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class InsideMusic extends MotoPanel {
}

attribute InsideMusic.title = 
Group {
    content:
    [View {
        content: Label {
            text: "<html><div style='font-face:Arial;font-size:14pt'><span style='color:white;'>Inside</span><span style='color:yellow;'>Music</span></div></html>"
        }
    },
    Text {content: "COURTESY OF ROLLING STONE", fill: white,
        font: new Font("Arial", "PLAIN", 8),
        halign: TRAILING
        transform: bind translate(width-30, 8)
    }]
};

attribute InsideMusic.content = 
Group {
    content:
    [View {
        content: Label {
            text:
            "<html><div style='width:200;color:white;font-size:10pt;font-face:arial;'>To the right we have today's top music news feeds provided by 
<a href='' style='color:yellow;'>ROLLING STONE</a>
</div>
</html>"
        }
    }]
};

Canvas {
    background: black
    content:
    InsideMusic {height: 180, width: 250}
}
