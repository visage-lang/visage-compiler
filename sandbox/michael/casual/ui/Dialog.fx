package casual.ui;

import casual.theme.*;

import javafx.ui.Canvas;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;
import javafx.ui.Cursor;
import javafx.ui.KeyEvent;
import javafx.ui.KeyStroke;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Text;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

public class DialogType
{
    public attribute id: String;
}

ERROR:DialogType = DialogType
{
    id: "ERROR"
};

WARNING:DialogType = DialogType
{
    id: "WARNING"
};

INFO:DialogType = DialogType
{
    id: "INFO"
};

public class Dialog extends CompositeNode
{
    public attribute frame: Frame;
    
    public attribute x: Integer;
    public attribute y: Integer;
    public attribute width: Integer;
    public attribute height: Integer;
    public attribute buttonText: String;
    
    attribute active: Boolean;
    attribute interactive: Boolean;
    attribute type: DialogType;
    attribute text: String;
    attribute headline: String?;
    
    operation getHeadline(type: DialogType): String;
    operation getFontSmall(type: DialogType): Font;
    operation getFontBig(type: DialogType): Font;
    operation getForeground(type: DialogType): Color;
    operation getBackgroundInside(type: DialogType): AbstractColor;
    operation getBackgroundOutside(type: DialogType): AbstractColor;
    operation getBorderColor(type: DialogType): AbstractColor;
}

attribute Dialog.isSelectionRoot = true;
attribute Dialog.buttonText = "OK";

function Dialog.getFontSmall(type: DialogType): Font
{
    return theme:ThemeManager.windowFont.bold();
}

function Dialog.getFontBig(type: DialogType): Font
{
    return getFontSmall(type).bigger().bigger();
}

function Dialog.getForeground(type: DialogType): Color
{
    return if (type == ERROR:DialogType)
        then theme:ThemeManager.errorForeground
    else if (type == WARNING:DialogType)
        then theme:ThemeManager.warningForeground
    else
        theme:ThemeManager.infoForeground;
}

function Dialog.getBackgroundInside(type: DialogType): AbstractColor
{
    return if (type == ERROR:DialogType)
        then theme:ThemeManager.errorBackgroundInside
    else if (type == WARNING:DialogType)
        then theme:ThemeManager.warningBackgroundInside
    else
        theme:ThemeManager.infoBackgroundInside;
}

function Dialog.getBackgroundOutside(type: DialogType): AbstractColor
{
    return if (type == ERROR:DialogType)
        then theme:ThemeManager.errorBackgroundOutside
    else if (type == WARNING:DialogType)
        then theme:ThemeManager.warningBackgroundOutside
    else
        theme:ThemeManager.infoBackgroundOutside;
}

function Dialog.getBorderColor(type: DialogType): AbstractColor
{
    return if (type == ERROR:DialogType)
        then theme:ThemeManager.errorBorderColor
    else if (type == WARNING:DialogType)
        then theme:ThemeManager.warningBorderColor
    else
        theme:ThemeManager.infoBorderColor;
}

function Dialog.composeNode() = Group
{
    var strokeWidth = 2
    var dialogWidth = bind if (width<>0) then width else (frame.width - 100)
    var dialogHeight = bind if (height<>0) then height else (0.4 * frame.height)
    var dialogX = bind if (x<>0) then x else ((frame.width/2) - (dialogWidth/2))
    var dialogY = bind if (y<>0) then y else ((frame.height/2) - (dialogHeight/2))
    var margin = 10
    
    visible: bind active
    cursor: DEFAULT:Cursor
    
    content:
    [
        Rect
        {
            x: bind 0
            y: bind 0
            width: bind frame.width
            height: bind frame.height
            fill: bind getBackgroundOutside(type)
            onMousePressed: operation(e)
            {
                e.source.consume();
            }
            onMouseDragged: operation(e)
            {
                e.source.consume();
            }
            onMouseReleased: operation(e)
            {
                e.source.consume();
            }
        },
        Rect
        {
            x: bind dialogX
            y: bind dialogY
            width: bind dialogWidth
            height: bind dialogHeight
            fill: bind getBackgroundInside(type)
            strokeWidth: bind strokeWidth
            stroke: bind getBorderColor(type)
        },
        Text
        {
            var: self
            
            visible: bind (headline<>null)
            x: bind (dialogX + margin)
            y: bind (dialogY + 2*margin)
            valign: MIDDLE
            halign: LEADING	
            fill: bind getForeground(type)
            content: bind headline
            font: bind getFontBig(type)
        },
        Text
        {
            var: self
            
            var offset = if (headline<>null) then 0 else margin
            
            x: bind (dialogX + (dialogWidth/2))
            y: bind (dialogY + (dialogHeight/2) - offset)
            valign: MIDDLE
            halign: CENTER
            fill: bind getForeground(type)
            content: bind text
            font: bind getFontSmall(type)
        },
        Button
        {
            var: self
            var w = 60
            var h = 20
            
            visible: bind (interactive==true)
            focused: bind active
            focusable: true
            text: bind buttonText
            x: bind (dialogX + dialogWidth - w - margin)
            y: bind (dialogY + dialogHeight - h - margin)
            width: bind w
            height: bind h
            font: bind getFontSmall(type)
            onClick: operation()
            {
                active = false;
                do later
                {
                    frame.requestFocus();
                }
            }
            onKeyDown: operation(e:KeyEvent)
            {
                if (e.keyStroke == ENTER:KeyStroke)
                {
                    e.source.consume();
                    
                    self.doClick();
                }
            }
        },
        IndeterminateProgressBar
        {
            var size = 10
            
            halign: CENTER
            valign: CENTER
            active: bind ((active==true) and (interactive==false))
            x: bind (dialogX + dialogWidth - size - 2*margin)
            y: bind (dialogY + dialogHeight - size - 2*margin)
            size: bind size
            fill: bind getForeground(type)
        },
    ]
};



