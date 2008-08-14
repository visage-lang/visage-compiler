package casual.ui;

import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Clip;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Node;
import javafx.ui.canvas.VisualNode;
import javafx.ui.canvas.Translate;

import java.lang.Math;

public class ScrollPane extends CompositeNode
{
    public attribute view: Node;
    public attribute height: Number;
    public attribute width: Number;
    public attribute scrollFactor: Number;
    
    attribute scrollOffset: Number = bind Math.max((view.currentHeight - height)*scrollFactor, 0);

    function composeNode() {
        Clip {
            shape: Rect
            {
                height: bind height
                width: bind width
            } as VisualNode

            content: Group
            {
                transform: bind Translate {x: 0, y: -scrollOffset}
                content: bind view
            }
        }
    };

}
