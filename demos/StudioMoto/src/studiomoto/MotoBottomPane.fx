package studiomoto;
import javafx.scene.*;
import javafx.animation.*;
import java.lang.System;

public class MotoBottomPane extends Intro {
    attribute panels: Intro[];
    attribute selection: Integer;
    attribute selectedPanel: Intro = bind panels[selection]
    on replace oldValue = newValue {
        oldValue.opacity = 0;
        newValue.opacity = 0;
        introAnim.start();
    }
    attribute introAnim: Timeline = Timeline {
        keyFrames:
            KeyFrame {
                time: 500ms
                action: function() {
                    doIntro();
                }
        }
    };
    override function doIntro() {
        selectedPanel.opacity = 1;
        //System.out.println("CALL INTRO { selection }");
        selectedPanel.doIntro();
    }

    override function create():Node {
        Group {
            content: bind selectedPanel
        };
    }
}


