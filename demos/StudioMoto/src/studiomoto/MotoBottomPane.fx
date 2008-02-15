package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.animation.*;
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
                keyTime: 500ms
                action: function() {
                    doIntro();
                }
        }
    };
    function doIntro() {
        selectedPanel.opacity = 1;
        System.out.println("CALL INTRO { selection }");
        selectedPanel.doIntro();
    }

    function composeNode():Node {
        Group {
            content: bind selectedPanel
        };
    }
}

