package casual.ui;

import casual.theme.*;

import javafx.ui.TextArea;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.EmptyBorder;
import javafx.ui.Font;
import javafx.ui.KeyEvent;
import javafx.ui.VerticalScrollBarPolicy;
import javafx.ui.HorizontalScrollBarPolicy;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

import java.awt.Dimension;

public class TextInput extends CompositeNode
{
    public attribute text: String;
    public attribute height: Integer;
    public attribute size: Dimension;
    
    private attribute input: TextArea = TextArea {
        size: bind size 
        lineWrap: true
        height: bind height
        viewportBorder: EmptyBorder{}
        scrollPaneBorder: EmptyBorder{}
        border: bind ThemeManager.getInstance().messageInputAreaBorder
        verticalScrollBarPolicy: VerticalScrollBarPolicy.NEVER
        horizontalScrollBarPolicy: HorizontalScrollBarPolicy.NEVER
        font: bind ThemeManager.getInstance().chatPanelFont
        foreground: bind ThemeManager.getInstance().messageInputForeground
        background: bind ThemeManager.getInstance().messageInputBackground
        text: bind text

        onKeyDown: function(e:KeyEvent)
        {
            if (onKeyDown <> null)
            {
                (this.onKeyDown)(e);
            }
        }
    }
    
    public function requestFocus()
    {
        input.requestFocus();
    }

    function composeNode() {
        Group {
            var strokeWidth = 2

            content:
            [        
                input as Node,

                // focus rectangle
                Rect
                {
                    visible: bind (input.focused)
                    x: bind input.x
                    y: bind input.y
                    width: bind (input.width-strokeWidth)
                    height: bind (input.height-strokeWidth+1)
                    strokeWidth: strokeWidth
                    stroke: bind ThemeManager.getInstance().messageInputBorderColor
                } as Node,
            ]
        }
    };
}
