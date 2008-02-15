package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MusicPanel2 extends CompositeNode {
    attribute softY: Number;
}

trigger on MusicPanel2.hover = newValue {
    var d = 500;
    if (newValue) {
        var ys = [[10..-2],[-2..0]]; 
        softY = reverse ys animation {dur: d*1ms  condition: bind hover == newValue, interpolate: EASEBOTH:Number};
    } else {
        var ys = [[0..15],[15..10]];
        softY = ys animation {dur: d*1ms,  condition: bind hover == newValue, interpolate: EASEBOTH:Number};
    }
}

function MusicPanel2.composeNode() =
Clip {
    shape: Rect {height: 160, width: 220}
    content:
    Group {
        transform: translate(0, 0)
        content:
        [ImageView {
            transform: translate(-80, 150) 
            valign: BOTTOM
            image: {url: "{__DIR__}/Image/83.png"}
        },
        ImageView {
            transform: translate(260, 150)
            valign: BOTTOM, halign: TRAILING
            image: {url: "{__DIR__}/Image/83.png"}
        },
        ImageView {
            transform: translate(0, 50)
            image: {url: "{__DIR__}/Image/84.png"}
        },
        ImageView {
            transform: translate(75, 30+softY)
            image: {url: "{__DIR__}/Image/85.png"}
        },
        
        HBox {
            transform: translate(0, 0)
            content:
            [View {
                transform: translate(0, 8)
                content:
                Label {
                    text: "<html><div style='color:yellow;font-size:9pt;width:120;font-face:arial;'>Click here for more information on Soft!</div></html>"
                }
            },
            MoreInfoButton {
                transform: translate(-20, 0)
            }]
        }]
    }
};

MusicPanel2 {}
