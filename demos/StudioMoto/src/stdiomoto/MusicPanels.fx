package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.Glow;

public class MusicPanels extends CompositeNode {
    attribute selection: Integer;
    attribute panels: Node*;
    attribute selectedPanel: Node?;
    attribute alpha: Number;
}
attribute MusicPanels.panels =
[MusicPanel1, MusicPanel2, MusicPanel3];
attribute MusicPanels.selectedPanel = bind panels[selection];
attribute MusicPanels.alpha = 1;
trigger on MusicPanels.selection = newValue {
    alpha = [0, 1] animation {dur: 300ms, interpolate: LINEAR:Number, condition: bind selection == newValue};
}

function MusicPanels.composeNode() =
Group {
    filter: bind if alpha == 1 then select Glow[i] from i in [0, 1] animation {dur: 300ms} else null
    opacity: bind alpha
    content: bind selectedPanel
    
};

MusicPanels {}
