package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class InsideMusicPane extends MotoPanel {
}

attribute InsideMusicPane.title = 
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

attribute InsideMusicPane.content = 
Group {
    content:
    [Group {
    // items
    },
    ImageView {
        transform: bind translate(0, 120)
        image: {url: "{__DIR__}/Image/95.png"}
    },
    ImageView {
        halign: TRAILING
        valign: BOTTOM
        transform: bind translate(width -30, height -35)
        image: {url: "{__DIR__}/Image/88.png"}
    }]
};

Canvas {
    background: black
    content:
    InsideMusic {height: 180, width: 250}
}
