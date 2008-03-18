package casual.ui;

import casual.theme.*;

import javafx.ui.SimpleLabel;
import javafx.ui.EmptyBorder;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Line;
import javafx.ui.canvas.CanvasMouseEvent;

public class TitleBar extends CompositeNode
{
    public attribute frame: CasualFrame;
    
    public attribute width: Integer;
    public attribute foreground: Color;
    public attribute background: Color;
    
    public attribute title: String;
    public attribute showCloseIcon: Boolean = true;
    public attribute showMinimizeIcon: Boolean = true;
    
    public attribute onClose: function();    
    
    private attribute thickness: Integer = 21;

    function composeNode() {
        Group {
            selectable: true
            isSelectionRoot: true

            var strokeWidth = bind ThemeManager.getInstance().uiStrokeWidth
            var iconOffset = 0.45
            var iconGap = 1
            //var xbase = (width - thickness - strokeWidth)
            var xbase = bind (width - thickness - thickness - strokeWidth)
            var ybase = bind (-strokeWidth)

            var origx = 0
            var origy = 0
            var startx = 0
            var starty = 0 
            onMousePressed: function(e:CanvasMouseEvent)
            {
                origx = frame.screenx.intValue();
                origy = frame.screeny.intValue();
                startx = e.screenx.intValue();
                starty = e.screeny.intValue();

                if (e.clickCount >= 2)
                {
                    frame.iconified = true;
                }
            }
            onMouseDragged: function(e:CanvasMouseEvent)
            {
                frame.setLocation(origx+(e.screenx-startx), origy+(e.screeny-starty));
            }
            onMouseReleased: function(e:CanvasMouseEvent)
            {
                frame.requestFocus();
            }

            var label = SimpleLabel
            {
                border: bind ThemeManager.getInstance().titleBorder
                foreground: bind if (frame.active==true) then foreground else foreground.moreTransparent()
                background: bind background
                font: bind ThemeManager.getInstance().titleBarFont
                text: bind if (title == null) then "Untitled" else title
            }

            content:
            [
                // background
                Rect
                {
                    selectable: true
                    width: bind width
                    height: bind label.height
                    fill: bind background
                } as Node,

                // title
                View
                {
                    content: label
                },

                // close button
                Group
                {
                    var pressed = bind false
                    var pressedHover = bind (pressed and hover)

                    isSelectionRoot: true
                    visible: bind showCloseIcon

                    onMousePressed: function(e:CanvasMouseEvent)
                    {
                        pressed = true;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent)
                    {

                    }
                    onMouseReleased: function(e:CanvasMouseEvent)
                    {
                        pressed = false;

                        if (hover)
                        {
                            frame.close();
                            if (onClose <> null)
                            {
                                (onClose)();
                            }
                        }

                        frame.requestFocus();
                    }

                    var xoffset = 9
                    var yoffset = 2
                    var x1 = (xbase + xoffset)
                    var x2 = (xbase + thickness - xoffset)

                    content:
                    [
                        Rect
                        {
                            var foreground = bind if (frame.active==true) then ThemeManager.getInstance().titleBarCloseIconColor else ThemeManager.getInstance().titleBarCloseIconColor.moreTransparent()

                            selectable: true
                            x: bind xbase
                            y: ybase
                            width: bind thickness
                            height: bind (iconOffset*label.height)
                            fill: bind if (pressedHover) then foreground.darker() else foreground
                            strokeWidth: bind strokeWidth
                            stroke: bind ThemeManager.getInstance().uiBorderColor
                        } as Node,
                        Line
                        {
                            selectable: true
                            x1: bind x1
                            y1: bind (ybase + strokeWidth + yoffset)
                            x2: bind x2
                            y2: bind (ybase + iconOffset*label.height - strokeWidth - yoffset)
                            stroke: bind if (pressedHover) then ThemeManager.getInstance().uiForeground.darker() else ThemeManager.getInstance().uiForeground
                            strokeWidth: 2
                        } as Node,
                        Line
                        {
                            selectable: true
                            x1: bind x1
                            y2: bind (ybase + strokeWidth + yoffset)
                            x2: bind x2
                            y1: bind (ybase + iconOffset*label.height - strokeWidth - yoffset)
                            stroke: bind if (pressedHover) then ThemeManager.getInstance().uiForeground.darker() else ThemeManager.getInstance().uiForeground
                            strokeWidth: 2
                        } as Node,
                    ]    
                },

                // minimize button
                Group
                {
                    var pressed = bind false
                    var pressedHover = bind (pressed and hover)

                    visible: bind showMinimizeIcon
                    isSelectionRoot: true

                    onMousePressed: function(e:CanvasMouseEvent)
                    {
                        pressed = true;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent)
                    {

                    }
                    onMouseReleased: function(e:CanvasMouseEvent)
                    {
                        pressed = false;

                        if (hover)
                        {
                            frame.iconified = true;
                        }

                        frame.requestFocus();
                    }

                    content:
                    [
                        Rect
                        {
                            var foreground = bind if (frame.active==true) then ThemeManager.getInstance().titleBarMinimizeIconColor else ThemeManager.getInstance().titleBarMinimizeIconColor.moreTransparent()

                            selectable: true
                            x: bind (xbase - iconGap - thickness - strokeWidth)
                            y: ybase
                            width: bind thickness
                            height: bind (iconOffset*label.height)
                            fill: bind if (pressedHover) then foreground.darker() else foreground
                            strokeWidth: bind strokeWidth
                            stroke: bind ThemeManager.getInstance().uiBorderColor
                        } as Node,
                        Rect
                        {
                            selectable: true
                            x: bind (xbase - iconGap - thickness - strokeWidth + thickness/3)
                            y: bind (ybase + iconOffset*label.height - 3*strokeWidth)
                            width: bind (thickness/3)
                            height: 2
                            fill: bind if (pressedHover) then ThemeManager.getInstance().uiForeground.darker() else ThemeManager.getInstance().uiForeground
                        } as Node,
                    ]    
                },
            ]
        }
    };
}
