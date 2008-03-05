package casual.ui;

import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Clip;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Node;

import java.lang.Math;

public class ScrollPane extends CompositeNode
{
    public attribute view: Node;
    public attribute height: Number;
    public attribute width: Number;
    public attribute scrollFactor: Number;
    
    private attribute scrollOffset: Number = bind Math.max((view.currentHeight - height)*scrollFactor, 0);

    function composeNode() {
        Clip {
            shape: Rect
            {
                height: bind height
                width: bind width
            }

            content: Group
            {
                transform: bind translate(0, -scrollOffset)
                content: bind view
            }
        }
    };

}
