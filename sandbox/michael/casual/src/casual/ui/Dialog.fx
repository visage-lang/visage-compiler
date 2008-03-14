package casual.ui;

import casual.theme.*;

import javafx.ui.Canvas;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;
import javafx.ui.Cursor;
import javafx.ui.KeyEvent;
import javafx.ui.KeyStroke;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Text;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;

public class DialogType
{
    public attribute id: String;

    public static attribute ERROR:DialogType = DialogType
    {
        id: "ERROR"
    };

    public static attribute WARNING:DialogType = DialogType
    {
        id: "WARNING"
    };

    public static attribute INFO:DialogType = DialogType
    {
        id: "INFO"
    };
}

public class Dialog extends CompositeNode
{
    public attribute frame: Frame;
    
    public attribute x: Integer;
    public attribute y: Integer;
    public attribute width: Integer;
    public attribute height: Integer;
    public attribute buttonText: String = "OK";
    
    public attribute active: Boolean;
    public attribute interactive: Boolean;
    public attribute type: DialogType;
    public attribute text: String;
    public attribute headline: String;
    
    function getFontSmall(type: DialogType): Font {
        return ThemeManager.getInstance().windowFont.bold();
    };
    
    function getFontBig(type: DialogType): Font {
        return getFontSmall(type).bigger().bigger();
    };
    
    function getForeground(type: DialogType): Color {
        return if (type == DialogType.ERROR)
            then ThemeManager.getInstance().errorForeground
        else if (type == DialogType.WARNING)
            then ThemeManager.getInstance().warningForeground
        else
            ThemeManager.getInstance().infoForeground;
    };
    
    function getBackgroundInside(type: DialogType): AbstractColor {
        return if (type == DialogType.ERROR)
            then ThemeManager.getInstance().errorBackgroundInside
        else if (type == DialogType.WARNING)
            then ThemeManager.getInstance().warningBackgroundInside
        else
            ThemeManager.getInstance().infoBackgroundInside;
    };
    
    function getBackgroundOutside(type: DialogType): AbstractColor {
        return if (type == DialogType.ERROR)
            then ThemeManager.getInstance().errorBackgroundOutside
        else if (type == DialogType.WARNING)
            then ThemeManager.getInstance().warningBackgroundOutside
        else
            ThemeManager.getInstance().infoBackgroundOutside;
    };
    
    function getBorderColor(type: DialogType): AbstractColor {
        return if (type == DialogType.ERROR)
            then ThemeManager.getInstance().errorBorderColor
        else if (type == DialogType.WARNING)
            then ThemeManager.getInstance().warningBorderColor
        else
            ThemeManager.getInstance().infoBorderColor;
    };

    override attribute isSelectionRoot = true;
    
    private attribute strokeWidth = 2;
    private attribute margin = 10;
    private attribute dialogWidth = bind if (width<>0) then width else (frame.width - 100);
    private attribute dialogHeight = bind if (height<>0) then height else (0.4 * frame.height);
    private attribute dialogX = bind if (x<>0) then x else ((frame.width/2) - (dialogWidth/2));
    private attribute dialogY = bind if (y<>0) then y else ((frame.height/2) - (dialogHeight/2));

    private attribute button: Button =
        Button {
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
            onClick: function()
            {
                active = false;
               //TODO DO LATER - this is a work around until a more permanent solution is provided
                javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                    public function run():Void {
                        frame.requestFocus();
                    }
                });
            }
            onKeyDown: function(e:KeyEvent)
            {
                if (e.keyStroke == KeyStroke.ENTER)
                {
                    e.source.consume();

                    button.doClick();
                }
            }
        };


    function composeNode() { 
        Group {
            visible: bind active
            cursor: Cursor.DEFAULT

            content:
            [
                Rect
                {
                    x: bind 0
                    y: bind 0
                    width: bind frame.width
                    height: bind frame.height
                    fill: bind getBackgroundOutside(type)
                    onMousePressed: function(e)
                    {
                        e.source.consume();
                    }
                    onMouseDragged: function(e)
                    {
                        e.source.consume();
                    }
                    onMouseReleased: function(e)
                    {
                        e.source.consume();
                    }
                } as Node,
                Rect
                {
                    x: bind dialogX
                    y: bind dialogY
                    width: bind dialogWidth
                    height: bind dialogHeight
                    fill: bind getBackgroundInside(type)
                    strokeWidth: bind strokeWidth
                    stroke: bind getBorderColor(type)
                } as Node,
                Text
                {
                    visible: bind (headline<>null)
                    x: bind (dialogX + margin)
                    y: bind (dialogY + 2*margin)
                    valign: VerticalAlignment.MIDDLE
                    halign: HorizontalAlignment.LEADING	
                    fill: bind getForeground(type)
                    content: bind headline
                    font: bind getFontBig(type)
                } as Node,
                Text
                {
                    var offset = if (headline<>null) then 0 else margin

                    x: bind (dialogX + (dialogWidth/2))
                    y: bind (dialogY + (dialogHeight/2) - offset)
                    valign: VerticalAlignment.MIDDLE
                    halign: HorizontalAlignment.CENTER
                    fill: bind getForeground(type)
                    content: bind text
                    font: bind getFontSmall(type)
                } as Node,
                button as Node,
                IndeterminateProgressBar
                {
                    var size = 10

                    halign: HorizontalAlignment.CENTER
                    valign: VerticalAlignment.CENTER
                    active: bind ((active==true) and (interactive==false))
                    x: bind (dialogX + dialogWidth - size - 2*margin)
                    y: bind (dialogY + dialogHeight - size - 2*margin)
                    size: bind size
                    fill: bind getForeground(type)
                }
            ]
        }
    };

}