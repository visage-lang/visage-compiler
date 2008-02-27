package casual.ui;

import casual.theme.*;

import javafx.ui.Canvas;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Text;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.CanvasMouseEvent;

class ClickRect extends Rect
{
    attribute pressedHover: Boolean;
}

public class Button extends CompositeNode
{
    public attribute x: Integer;
    public attribute y: Integer;
    public attribute width: Integer;
    public attribute height: Integer;
    public attribute text: String;
    public attribute font: Font;
    
    public attribute onClick: operation();
    public operation doClick();
    attribute clickTarget: ClickRect;
    attribute clickTrigger: Boolean;
}

attribute Button.isSelectionRoot = true;

trigger on Button.clickTrigger = value
{
    if (value == true)
    {
        hover = true;
        
        var event:CanvasMouseEvent = null;
        (clickTarget.onMousePressed)(event);
    }
    else
    {
        var event:CanvasMouseEvent = null;
        (clickTarget.onMouseReleased)(event);
        
        hover = false;
    }
}

operation Button.doClick()
{
    clickTrigger = [true, false] dur 150;
}

function Button.composeNode() = Group
{
    content:
    [
        ClickRect
        {
            attribute: clickTarget
            
            var: self
            var pressed = bind false
            var strokeWidth = bind theme:ThemeManager.uiStrokeWidth
            
            selectable: true
            pressedHover: bind (pressed and hover)

            onMousePressed: operation(e:CanvasMouseEvent)
            {
                pressed = true;
            }
            onMouseDragged: operation(e:CanvasMouseEvent)
            {
            }
            onMouseReleased: operation(e:CanvasMouseEvent)
            {   
                if ((self.pressedHover == true) and (onClick <> null))
                {
                    pressed = false;
                    
                    do later
                    {
                        (onClick)();
                    }
                }
                else
                {
                    pressed = false;
                }
            }
            
            x: bind x
            y: bind y
            width: bind width-1
            height: bind height-1
            fill: bind if (self.pressedHover==true) then theme:ThemeManager.uiBackground.darker() else theme:ThemeManager.uiBackground
            stroke: bind theme:ThemeManager.uiBorderColor
            strokeWidth: bind strokeWidth
        },
        Text
        {
            selectable: true
            editable: false
            x: bind (x+width/2)
            y: bind (y+height/2)
            valign: MIDDLE
            halign: CENTER	
            fill: bind theme:ThemeManager.uiForeground
            content: bind text
            font: bind font
        }
    ]
};




