package casual.ui;

import casual.theme.*;

import javafx.ui.SimpleLabel;
import javafx.ui.EmptyBorder;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Line;
import javafx.ui.canvas.CanvasMouseEvent;

public class TitleBar extends CompositeNode
{
    public attribute frame: Frame;
    
    public attribute width: Integer;
    public attribute foreground: Color;
    public attribute background: AbstractColor;
    
    public attribute title: String?;
    public attribute showCloseIcon: Boolean;
    public attribute showMinimizeIcon: Boolean;
    
    public attribute onClose: operation();    
    
    private attribute thickness: Integer;
}

attribute TitleBar.showCloseIcon = true;
attribute TitleBar.showMinimizeIcon = true;
attribute TitleBar.thickness = 21;

function TitleBar.composeNode() = Group
{
    selectable: true
    isSelectionRoot: true
    
    var strokeWidth = bind theme:ThemeManager.uiStrokeWidth
    var iconOffset = 0.45
    var iconGap = 1
    //var xbase = (width - thickness - strokeWidth)
    var xbase = bind (width - thickness - thickness - strokeWidth)
    var ybase = bind (-strokeWidth)
    
    var origx = 0
    var origy = 0
    var startx = 0
    var starty = 0 
    onMousePressed: operation(e:CanvasMouseEvent)
    {
        origx = frame.screenx;
        origy = frame.screeny;
        startx = e.screenx;
        starty = e.screeny;
        
        if (e.clickCount >= 2)
        {
            frame.iconified = true;
        }
    }
    onMouseDragged: operation(e:CanvasMouseEvent)
    {
        frame.setLocation(origx+(e.screenx-startx), origy+(e.screeny-starty));
    }
    onMouseReleased: operation(e:CanvasMouseEvent)
    {
        frame.requestFocus();
    }
    
    var label = SimpleLabel
    {
        border: bind theme:ThemeManager.titleBorder
        foreground: bind if (frame.active==true) then foreground else foreground.moreTransparent()
        background: bind background
        font: bind theme:ThemeManager.titleBarFont
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
        },
        
        // title
        View
        {
            content: label
        },
        
        // close button
        Group
        {
            var: closeButton
            var pressed = bind false
            var pressedHover = bind (pressed and closeButton.hover)
            
            isSelectionRoot: true
            visible: bind showCloseIcon
            
            onMousePressed: operation(e:CanvasMouseEvent)
            {
                pressed = true;
            }
            onMouseDragged: operation(e:CanvasMouseEvent)
            {
                
            }
            onMouseReleased: operation(e:CanvasMouseEvent)
            {
                pressed = false;
                
                if (closeButton.hover)
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
                    var foreground = bind if (frame.active==true) then theme:ThemeManager.titleBarCloseIconColor else theme:ThemeManager.titleBarCloseIconColor.moreTransparent()
                    
                    selectable: true
                    x: bind xbase
                    y: ybase
                    width: bind thickness
                    height: bind (iconOffset*label.height)
                    fill: bind if pressedHover then foreground.darker() else foreground
                    strokeWidth: bind strokeWidth
                    stroke: bind theme:ThemeManager.uiBorderColor
                },
                Line
                {
                    selectable: true
                    x1: bind x1
                    y1: bind (ybase + strokeWidth + yoffset)
                    x2: bind x2
                    y2: bind (ybase + iconOffset*label.height - strokeWidth - yoffset)
                    stroke: bind if pressedHover then theme:ThemeManager.uiForeground.darker() else theme:ThemeManager.uiForeground
                    strokeWidth: 2
                },
                Line
                {
                    selectable: true
                    x1: bind x1
                    y2: bind (ybase + strokeWidth + yoffset)
                    x2: bind x2
                    y1: bind (ybase + iconOffset*label.height - strokeWidth - yoffset)
                    stroke: bind if pressedHover then theme:ThemeManager.uiForeground.darker() else theme:ThemeManager.uiForeground
                    strokeWidth: 2
                },
            ]    
        },
        
        // minimize button
        Group
        {
            var: minimizeButton
            var pressed = bind false
            var pressedHover = bind (pressed and minimizeButton.hover)
            
            visible: bind showMinimizeIcon
            isSelectionRoot: true
            
            onMousePressed: operation(e:CanvasMouseEvent)
            {
                pressed = true;
            }
            onMouseDragged: operation(e:CanvasMouseEvent)
            {
                
            }
            onMouseReleased: operation(e:CanvasMouseEvent)
            {
                pressed = false;
                
                if (minimizeButton.hover)
                {
                    frame.iconified = true;
                }
                
                frame.requestFocus();
            }
            
            content:
            [
                Rect
                {
                    var foreground = bind if (frame.active==true) then theme:ThemeManager.titleBarMinimizeIconColor else theme:ThemeManager.titleBarMinimizeIconColor.moreTransparent()
                    
                    selectable: true
                    x: bind (xbase - iconGap - thickness - strokeWidth)
                    y: ybase
                    width: bind thickness
                    height: bind (iconOffset*label.height)
                    fill: bind if pressedHover then foreground.darker() else foreground
                    strokeWidth: bind strokeWidth
                    stroke: bind theme:ThemeManager.uiBorderColor
                },
                Rect
                {
                    selectable: true
                    x: bind (xbase - iconGap - thickness - strokeWidth + thickness/3)
                    y: bind (ybase + iconOffset*label.height - 3*strokeWidth)
                    width: bind (thickness/3)
                    height: 2
                    fill: bind if pressedHover then theme:ThemeManager.uiForeground.darker() else theme:ThemeManager.uiForeground
                },
            ]    
        },
    ]
};

