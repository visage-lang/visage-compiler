package casual.ui;

import casual.theme.*;

import javafx.ui.Label;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.EmptyBorder;
import javafx.ui.Font;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;
import net.java.javafx.ui.UIContext; // hack

public class Message extends CompositeNode
{
    public attribute type: MessageType;
    public attribute message: String;
    attribute width: Number;
    attribute height: Number;
    
    private operation getAlign(type: MessageType): String;
    private operation getForeground(type: MessageType): Color;
    private operation getBackground(type: MessageType): AbstractColor;
    private operation getFont(type: MessageType): Font;
    private operation getBorder(type: MessageType): EmptyBorder;
}

function Message.getAlign(type: MessageType): String
{
    return if (type == INCOMING:MessageType)
        then "left"
    else if (type == OUTGOING:MessageType)
        then "right"
    else //if (type == COMMENT:MessageType)
        "center";
}

function Message.getForeground(type: MessageType): Color
{
    return if (type == INCOMING:MessageType)
        then theme:ThemeManager.messageOutForeground
    else if (type == OUTGOING:MessageType)
        then theme:ThemeManager.messageInForeground
    else //if (type == COMMENT:MessageType)
        theme:ThemeManager.commentForeground;
}

function Message.getBackground(type: MessageType): AbstractColor
{
    return if (type == INCOMING:MessageType)
        then theme:ThemeManager.messageOutBackground
    else if (type == OUTGOING:MessageType)
        then theme:ThemeManager.messageInBackground
    else //if (type == COMMENT:MessageType)
        theme:ThemeManager.commentBackground;
}

function Message.getFont(type: MessageType): Font
{
    return if (type == INCOMING:MessageType)
        then theme:ThemeManager.messageFont
    else if (type == OUTGOING:MessageType)
        then theme:ThemeManager.messageFont
    else //if (type == COMMENT:MessageType)
        theme:ThemeManager.commentFont;
}

function Message.getBorder(type: MessageType): EmptyBorder
{
    return if (type == INCOMING:MessageType)
        then theme:ThemeManager.messageBorder
    else if (type == OUTGOING:MessageType)
        then theme:ThemeManager.messageBorder
    else //if (type == COMMENT:MessageType)
        theme:ThemeManager.commentBorder;
}

function Message.composeNode() = Group
{
    var label = Label
    {
        var margin = if (type == COMMENT:MessageType) then 1 else 0
        var offsets = (margin + theme:ThemeManager.messageBorder.left + theme:ThemeManager.messageBorder.right) 
	onHyperlinkActivated: operation(url) {context:UIContext.browse(url);}
        focusable: true
        border: bind getBorder(type)
        font: bind getFont(type)
        foreground: bind getForeground(type)
        background: bind getBackground(type)
        text: bind "<html><body><div width='{width-offsets}' style='text-align:{getAlign(type)};'>{message}</div></body></html>"
    }
    
    content:
    [
        View
        {
            antialiasText: false
            content: label
        },
        Rect
        {
            visible: bind (type <> COMMENT:MessageType)
            stroke: bind theme:ThemeManager.messageBorderColor
            x: 0
            y: -1
            width: bind label.width-1
            height: bind label.height
        },
    ]
};

