package casual.ui;

import casual.theme.*;

import javafx.ui.Label;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.EmptyBorder;
import javafx.ui.Font;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

public class Message extends CompositeNode
{
    public attribute type: MessageType;
    public attribute message: String;
    public attribute width: Number;
    public attribute height: Number;
    
    function getAlign(type: MessageType): String {
        return if (type == MessageType.INCOMING)
            then "left"
        else if (type == MessageType.OUTGOING)
            then "right"
        else //if (type == MessageType.COMMENT)
            "center";
    };
    
    function getForeground(type: MessageType): Color {
        return if (type == MessageType.INCOMING)
            then ThemeManager.getInstance().messageOutForeground
        else if (type == MessageType.OUTGOING)
            then ThemeManager.getInstance().messageInForeground
        else //if (type == MessageType.COMMENT)
            ThemeManager.getInstance().commentForeground;
    };
    
    function getBackground(type: MessageType): Color {
        return if (type == MessageType.INCOMING)
            then ThemeManager.getInstance().messageOutBackground
        else if (type == MessageType.OUTGOING)
            then ThemeManager.getInstance().messageInBackground
        else //if (type == COMMENT:MessageType)
            ThemeManager.getInstance().commentBackground;
    };
    
    function getFont(type: MessageType): Font {
        return if (type == MessageType.INCOMING)
            then ThemeManager.getInstance().messageFont
        else if (type == MessageType.OUTGOING)
            then ThemeManager.getInstance().messageFont
        else //if (type == MessageType.COMMENT)
            ThemeManager.getInstance().commentFont;
    };
    
    function getBorder(type: MessageType): EmptyBorder {
        return if (type == MessageType.INCOMING)
            then ThemeManager.getInstance().messageBorder
        else if (type == MessageType.OUTGOING)
            then ThemeManager.getInstance().messageBorder
        else //if (type == COMMENT:MessageType)
            ThemeManager.getInstance().commentBorder;
    };

    function composeNode() {
        Group {
            var label = Label
            {
                var margin = if (type == MessageType.COMMENT) then 1 else 0
                var offsets = (margin + ThemeManager.getInstance().messageBorder.left + ThemeManager.getInstance().messageBorder.right) 
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
                    visible: bind (type != MessageType.COMMENT)
                    stroke: bind ThemeManager.getInstance().messageBorderColor
                    x: 0
                    y: -1
                    width: bind label.width-1
                    height: bind label.height
                } as Node,
            ]
        }
    };
}
