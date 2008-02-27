package casual.ui;

import casual.theme.*;

import javafx.ui.TextArea;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.EmptyBorder;
import javafx.ui.Font;
import javafx.ui.KeyEvent;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

import java.awt.Dimension;

public class TextInput extends CompositeNode
{
    private attribute input: TextArea;
    
    public attribute text: String;
    public attribute height: Integer;
    public attribute size: Dimension;
    public operation requestFocus();
}

operation TextInput.requestFocus()
{
    input.requestFocus();
}

function TextInput.composeNode() = Group
{
    var strokeWidth = 2
    
    content:
    [        
        View
        {
            size: bind size // gznote: (need it here since there is a bug in Widget.fx size)
            content: TextArea
            {
                attribute: input
                
                //size: bind size // gznote: (bug in Widget.fx size)
                lineWrap: true
                height: bind height
                viewportBorder: EmptyBorder{}
                scrollPaneBorder: EmptyBorder{}
                border: bind theme:ThemeManager.messageInputAreaBorder
                verticalScrollBarPolicy: NEVER
                horizontalScrollBarPolicy: NEVER
                font: bind theme:ThemeManager.chatPanelFont
                foreground: bind theme:ThemeManager.messageInputForeground
                background: bind theme:ThemeManager.messageInputBackground
                text: bind text

                onKeyDown: operation(e:KeyEvent)
                {
                    if (onKeyDown <> null)
                    {
                        (this.onKeyDown)(e);
                    }
                }
            }
        },
        
        // focus rectangle
        Rect
        {
            visible: bind (input.focused)
            x: bind input.x
            y: bind input.y
            width: bind (input.width-strokeWidth)
            height: bind (input.height-strokeWidth+1)
            strokeWidth: strokeWidth
            stroke: bind theme:ThemeManager.messageInputBorderColor
        },
    ]
};

