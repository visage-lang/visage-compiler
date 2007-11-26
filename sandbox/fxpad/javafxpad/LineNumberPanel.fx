package javafxpad;
import javafx.ui.*;

public class LineNumberPanel extends Widget {
    private attribute panel: <<net.java.javafx.ui.LineNumberPanel>>;
    public attribute lineCount: Number;
    public operation getCellBounds(line:Integer):<<java.awt.Rectangle>>;
}

operation LineNumberPanel.getCellBounds(line:Integer) {
    return panel.getCellBounds(line);
}

trigger on LineNumberPanel.lineCount = value {
    panel.setLineCount(value);
}

operation LineNumberPanel.createComponent() {
    panel = new <<net.java.javafx.ui.LineNumberPanel>>;
    panel.setOpaque(false);
    panel.setLineCount(lineCount);
    return panel;
}
