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
        after (.5s) { trigger { doIntro(); } }
    };
    function doIntro() {
        selectedPanel.opacity = 1;
        println("CALL INTRO { selection }");
        selectedPanel.doIntro();
    }

}

function MotoBottomPane.composeNode() =
Group {
    content: bind selectedPanel
};
