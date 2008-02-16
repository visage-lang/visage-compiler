package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;
import javafx.lang.Time;

public class MusicPanel2 extends CompositeNode {
    attribute base: java.net.URL;
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
    private attribute hoverClip:Timeline;
    private attribute pf: PointerFactory = PointerFactory{}; 
    private attribute __softY = bind  pf.make(softY); 
    private attribute _softY:Pointer = __softY.unwrap(); 
    // From javafx.ui.Node        
    public attribute hover: Boolean on replace {
        if(hoverClip <> null) {
            // stop old clip, this assumes if old clip is already stopped
            // then nothing bad will happen.
            hoverClip.stop();
        }
        var d = 500;
        
        if (hover) {
            var ys = [[10..-2],[-2..0]]; 
            var interval = (d*1)/sizeof ys;
            hoverClip = Timeline {
                keyFrames: for(s in reverse ys) {
                    KeyFrame {
                        keyTime: Time {millis: interval}
                        relative: true
                        keyValues: NumberValue {
                            target: _softY
                            value: s
                            interpolate: NumberValue.EASEBOTH
                        }
                    }
                }
             };
        } else {
            var ys = [[0..15],[15..10]];
            var interval = (d*1)/sizeof ys;
            hoverClip = Timeline {
                keyFrames: for(s in ys) {
                    KeyFrame {
                        keyTime: Time {millis: interval}
                        relative: true
                        keyValues: NumberValue {
                            target: _softY
                            value: s
                            interpolate: NumberValue.EASEBOTH
                        }
                    }
                }
             };            
        }
        hoverClip.start();
    };
}

MusicPanel2 {}

