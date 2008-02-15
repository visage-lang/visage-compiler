package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

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
                action: funtion() {
                    doIntro();
                }
        }
    };
    function doIntro() {
        selectedPanel.opacity = 1;
        println("CALL INTRO { selection }");
        selectedPanel.doIntro();
    }

    function composeNode():Node {
        Group {
            content: bind selectedPanel
        };
    }
}

