package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.Glow;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class MusicPanels extends CompositeNode {
    attribute pf: PointerFactory = PointerFactory{};
    attribute selection: Integer on replace {
        if(selectionClip != null) {
            selectionClip.stop();
            selectionClip.start();
        }
    };
    attribute panels: Node[] = [MusicPanel1{} as Node, MusicPanel2{}  as Node, MusicPanel3{}  as Node];
    attribute selectedPanel: Node = bind panels[selection];
    
    attribute level:Number;
    attribute glow:Glow = Glow{level:bind level};
    private attribute __level = bind pf.make(level);
    private attribute _level = __level.unwrap();
    attribute glowAnimation = Timeline {
        keyFrames:
        [KeyFrame {
            keyTime: 0s
            /******
            keyValues: NumberValue {
                target: _level;
                value: .8
            } 
             * ****/
            action: function() {
                glow = Glow{};
            }              
        },
        KeyFrame {
            keyTime: 300ms
            /******
            keyValues: NumberValue {
                target: _level;
                value: 0
            } 
             * ****/
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
    private attribute __alpha = bind pf.make(alpha);
    private attribute _alpha:Pointer = __alpha.unwrap();
    private attribute selectionClip:Timeline = Timeline {
            keyFrames: [
                KeyFrame {
                    keyTime: 0s
                    keyValues: NumberValue {
                            target: _alpha
                            value: 0
                        }
                },
                KeyFrame {
                    keyTime: 300ms
                    keyValues: NumberValue {
                            target: _alpha
                            value: 1
                        } 
                }
            ]
        };    
 
    
    
    function composeNode():Node {
        Group {
            //todo GLOW filter: bind if (alpha == 1) then select Glow[i] from i in [0, 1] animation {dur: 300ms} else null
            filter: bind glow
            opacity: bind alpha
            content: bind selectedPanel

        };  
    }
}


MusicPanels {}

