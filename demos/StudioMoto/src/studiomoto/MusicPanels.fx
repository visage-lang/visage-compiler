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
    var selection: Integer on replace {
        if(selectionClip != null) {
            selectionClip.stop();
            selectionClip.start();
        }
    };
    var panels: Node[] = [MusicPanel1{}, MusicPanel2{} , MusicPanel3{} ];
    var selectedPanel: Node = bind panels[selection];
    
    var level:Number;
    var glow:Glow = Glow{level:bind level};
    var glowAnimation = Timeline {
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
    
    var alpha: Number = 1 on replace {
        if(alpha == 1) {
            glowAnimation.start();
        }
    }
    var selectionClip:Timeline = Timeline {
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
 
    
    
    override function create():Node {
        Group {
            effect: bind glow
            opacity: bind alpha
            content: bind selectedPanel

        };  
    }
}


MusicPanels {}

