package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;

public class MusicPanel2 extends CompositeNode {
    attribute base: java.net.URL; // work around for __DIR__
    attribute softY: Number;
    function composeNode():Node {
        Clip {
            shape: Rect {height: 160, width: 220}
            content:
            Group {
                transform: Transform.translate(0, 0)
                content:
                [ImageView {
                    transform: Transform.translate(-80, 150) 
                    valign: VerticalAlignment.BOTTOM
                    image: Image {url: "{base}/Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(260, 150)
                    valign: VerticalAlignment.BOTTOM, halign: HorizontalAlignment.TRAILING
                    image: Image {url: "{base}/Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(0, 50)
                    image: Image {url: "{base}/Image/84.png"}
                },
                ImageView {
                    transform: Transform.translate(75, 30+softY)
                    image: Image {url: "{base}/Image/85.png"}
                },

                HBox {
                    transform: Transform.translate(0, 0)
                    content:
                    [View {
                        transform: Transform.translate(0, 8)
                        content:
                        Label {
                            text: "<html><div style='color:yellow;font-size:9pt;width:120;font-face:arial;'>Click here for more information on Soft!</div></html>"
                        }
                    },
                    MoreInfoButton {
                        transform: Transform.translate(-20, 0)
                    } as Node]
                }]
            }
        };

    }
}

//TODO Trigger
/*************
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
 * **********/

MusicPanel2 {}

