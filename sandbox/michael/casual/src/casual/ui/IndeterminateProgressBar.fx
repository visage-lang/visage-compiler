package casual.ui;

import javafx.ui.AbstractColor;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Circle;
import javafx.ui.canvas.Rect;

class IndeterminateProgressBar extends CompositeNode
{
    public attribute active: Boolean;
    public attribute size: Integer;
    public attribute x: Integer;
    public attribute y: Integer;
    public attribute fill: AbstractColor;
}

function IndeterminateProgressBar.composeNode() = Group
{
    var strokeWidth = bind (size/8)
    
    visible: bind active
    transform: translate(x, y)
    content:
    [
        Circle
        {
            stroke: fill
            strokeWidth: bind strokeWidth
            cx: bind size
            cy: bind size
            radius: bind size
        },
        Rect
        {
            fill: fill
            height: bind (2*strokeWidth)
            width: bind (size - (2*strokeWidth))
            arcHeight: bind (2*strokeWidth)
            arcWidth: bind (2*strokeWidth)
            transform: bind [translate(size-strokeWidth, size-strokeWidth), rotate(if active then ([0..360] dur 1000 linear while visible continue if true) else 0, strokeWidth, strokeWidth)]
        },
        Rect
        {
            fill: fill
            height: bind (2*strokeWidth)
            width: bind (size - (3*strokeWidth))
            arcHeight: bind (2*strokeWidth)
            arcWidth: bind (2*strokeWidth)
            transform: bind [translate(size-strokeWidth, size-strokeWidth), rotate(if active then ([0..360] dur 12000 linear while visible continue if true) else 0, strokeWidth, strokeWidth)]
        }
    ]
};

