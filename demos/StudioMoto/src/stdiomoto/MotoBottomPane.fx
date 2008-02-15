package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

public class MotoBottomPane extends Intro {
    attribute panels: Intro*;
    attribute selection: Integer;
    attribute selectedPanel: Intro?;
    attribute introAnim: KeyFrameAnimation;
}

attribute MotoBottomPane.introAnim = KeyFrameAnimation {
    keyFrames:
    after (.5s) { trigger { doIntro(); } }
};

attribute MotoBottomPane.selectedPanel = bind panels[selection];

trigger on MotoBottomPane.selectedPanel[oldValue] = newValue {
    oldValue.opacity = 0;
    newValue.opacity = 0;
    introAnim.start();
}

operation MotoBottomPane.doIntro() {
    selectedPanel.opacity = 1;
    println("CALL INTRO { selection }");
    selectedPanel.doIntro();
}

function MotoBottomPane.composeNode() =
Group {
    content: bind selectedPanel
};
