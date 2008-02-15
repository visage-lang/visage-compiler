package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
//TODO GLOW import javafx.ui.filter.Glow;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class MusicPanels extends CompositeNode {
    attribute pf: PointerFactory = PointerFactory{};
    attribute selection: Integer on replace {
        var newValue = selection;
        var selectionClip:Timeline = Timeline {
            keyFrames: [
                KeyFrame {
                    keyTime: 0s
                    keyValues: NumberValue {
                            target: _alpha
                            value: 0
                        }
                    action: function() {
                        if(selection <> newValue) {
                            selectionClip.stop();
                        }
                    }
                },
                KeyFrame {
                    keyTime: 300ms
                    keyValues: NumberValue {
                            target: _alpha
                            value: 1
                        } 
                    action: function() {
                        if(selection <> newValue) {
                            selectionClip.stop();
                        }
                    }                    
                }
            ]
        };
        selectionClip.start();
    };
    attribute panels: Node[] = [MusicPanel1{} as Node, MusicPanel2{}  as Node, MusicPanel3{}  as Node];
    attribute selectedPanel: Node = bind panels[selection];
    attribute alpha: Number = 1;
    attribute _alpha:Pointer = pf.make(alpha).unwrap();
    
    function composeNode():Node {
        Group {
            //todo GLOW filter: bind if (alpha == 1) then select Glow[i] from i in [0, 1] animation {dur: 300ms} else null
            opacity: bind alpha
            content: bind selectedPanel

        };  
    }
}


MusicPanels {}

