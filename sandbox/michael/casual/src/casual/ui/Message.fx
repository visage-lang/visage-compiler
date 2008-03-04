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

public class Message extends CompositeNode
{
    public attribute type: MessageType;
    public attribute message: String;
    attribute width: Number;
    attribute height: Number;
    
    private function getAlign(type: MessageType): String {
        return if (type == MessageType.INCOMING)
            then "left"
        else if (type == MessageType.OUTGOING)
            then "right"
        else //if (type == MessageType.COMMENT)
            "center";
    };
    
    private function getForeground(type: MessageType): Color {
        return if (type == MessageType.INCOMING)
            then theme.messageOutForeground
        else if (type == MessageType.OUTGOING)
            then theme.messageInForeground
        else //if (type == MessageType.COMMENT)
            theme.commentForeground;
    };
    
    private function getBackground(type: MessageType): AbstractColor {
        return if (type == MessageType.INCOMING)
            then theme.messageOutBackground
        else if (type == MessageType.OUTGOING)
            then theme.messageInBackground
        else //if (type == COMMENT:MessageType)
            theme.commentBackground;
    };
    
    private function getFont(type: MessageType): Font {
        return if (type == MessageType.INCOMING)
            then theme.messageFont
        else if (type == MessageType.OUTGOING)
            then theme.messageFont
        else //if (type == MessageType.COMMENT)
            theme.commentFont;
    };
    
    private function getBorder(type: MessageType): EmptyBorder {
        return if (type == MessageType.INCOMING)
            then theme.messageBorder
        else if (type == MessageType.OUTGOING)
            then theme.messageBorder
        else //if (type == COMMENT:MessageType)
            theme.commentBorder;
    };

    function composeNode() {
        Group {
            var label = Label
            {
                var margin = if (type == MessageType.COMMENT) then 1 else 0
                var offsets = (margin + theme.messageBorder.left + theme.messageBorder.right) 
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
                    visible: bind (type <> MessageType.COMMENT)
                    stroke: bind theme.messageBorderColor
                    x: 0
                    y: -1
                    width: bind label.width-1
                    height: bind label.height
                },
            ]
        }
    };
}
