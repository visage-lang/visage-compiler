package studiomoto;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;
import javafx.scene.effect.Glow;
import javafx.animation.*;

public class MusicPanels extends CustomNode {
    attribute selection: Integer on replace {
        if(selectionClip != null) {
            selectionClip.stop();
            selectionClip.start();
        }
    };
    attribute panels: Node[] = [MusicPanel1{}, MusicPanel2{} , MusicPanel3{} ];
    attribute selectedPanel: Node = bind panels[selection];
    
    attribute level:Number;
    attribute glow:Glow = Glow{level:bind level};
    attribute glowAnimation = Timeline {
        keyFrames:
        [KeyFrame {
            time: 0s
            action: function() {
                glow = Glow{};
            }              
        },
        KeyFrame {
            time: 300ms
            action: function() {
                glow = null;
            }              
        }]
    };
    
    attribute alpha: Number = 1 on replace {
        if(alpha == 1) {
            glowAnimation.start();
        }
    }
    private attribute selectionClip:Timeline = Timeline {
            keyFrames: [
                KeyFrame {
                    time: 0s
                    values: alpha => 0
                },
                KeyFrame {
                    time: 300ms
                    values: alpha => 1
                }
            ]
        };    
 
    
    
    function create():Node {
        Group {
            effect: bind glow
            opacity: bind alpha
            content: bind selectedPanel

        };  
    }
}


MusicPanels {}

