package casual.ui;

import casual.theme.*;

import javafx.ui.AbstractColor;
import javafx.ui.Color;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Line;
import javafx.ui.canvas.CanvasMouseEvent;

public class ResizeIcon extends CompositeNode
{
    public attribute frame: Frame;
    
    public attribute width: Integer;
    public attribute height: Integer;
}

function ResizeIcon.composeNode() = Group
{
    content:
    [
        Rect
        {
            var origx = 0
            var origy = 0
            var origw = 0
            var origh = 0
            
            isSelectionRoot: true
            selectable: true
            
            width: bind width
            height: bind height
            fill: bind theme:ThemeManager.chatFrameBackground
            
            onMousePressed: operation(e:CanvasMouseEvent)
            {
                origx = e.x;
                origy = e.y;
                origw = frame.width;
                origh = frame.height;
            }
            onMouseDragged: operation(e:CanvasMouseEvent)
            {
                frame.inLiveResize = true;
                frame.setSize(origw+(e.x-origx), origh+(e.y-origy));
            }
            onMouseReleased: operation(e:CanvasMouseEvent)
            {
                frame.inLiveResize = false;
                frame.requestFocus();
            }
        },
        Line
        {
            x1: bind (width-2)
            y1: bind (height-1)
            x2: bind (width-1)
            y2: bind (height-2)
            stroke: bind theme:ThemeManager.defaultBackground
        },
        Line
        {
            x1: bind (width-6)
            y1: bind (height-1)
            x2: bind (width-1)
            y2: bind (height-6)
            stroke: bind theme:ThemeManager.defaultBackground
        },
        Line
        {
            x1: bind (width-10)
            y1: bind (height-1)
            x2: bind (width-1)
            y2: bind (height-10)
            stroke: bind theme:ThemeManager.defaultBackground
        },
        Line
        {
            x1: bind (width-3)
            y1: bind (height-1)
            x2: bind (width-1)
            y2: bind (height-3)
            stroke: bind theme:ThemeManager.uiForeground
        },
        Line
        {
            x1: bind (width-7)
            y1: bind (height-1)
            x2: bind (width-1)
            y2: bind (height-7)
            stroke: bind theme:ThemeManager.uiForeground
        },
        Line
        {
            x1: bind (width-11)
            y1: bind (height-1)
            x2: bind (width-1)
            y2: bind (height-11)
            stroke: bind theme:ThemeManager.uiForeground
        },
    ]
};



