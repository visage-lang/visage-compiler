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
    public var id: String;

    public static var ERROR:DialogType = DialogType
    {
        id: "ERROR"
    };

    public static var WARNING:DialogType = DialogType
    {
        id: "WARNING"
    };

    public static var INFO:DialogType = DialogType
    {
        id: "INFO"
    };
}

public class Dialog extends CompositeNode
{
    public var frame: CasualFrame;
    
    public var x: Integer;
    public var y: Integer;
    public var width: Integer;
    public var height: Integer;
    public var buttonText: String = "OK";
    
    public var active: Boolean;
    public var interactive: Boolean;
    public var type: DialogType;
    public var text: String;
    public var headline: String;
    
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

    override var isSelectionRoot = true;
    


    function composeNode() { 
        Group {
            var strokeWidth = 2;
            var dialogWidth = bind if (width!=0) then width else (frame.width - 100);
            var dialogHeight = bind if (height!=0) then height else (0.4 * frame.height);
            var dialogX = bind if (x!=0) then x else ((frame.width/2) - (dialogWidth/2));
            var dialogY = bind if (y!=0) then y else ((frame.height/2) - (dialogHeight/2));
            var margin = 10;

            visible: bind active
            cursor: Cursor.DEFAULT

            var button: Button =
                Button {
                    var w = 60
                    var h = 20

                    visible: bind (interactive==true)
                    focused: bind active
                    focusable: true
                    text: bind buttonText
                    x: bind (dialogX + dialogWidth - w - margin).intValue()
                    y: bind (dialogY + dialogHeight - h - margin).intValue()
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
                        visible: bind (headline!=null)
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
                        var offset = if (headline!=null) then 0 else margin

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
                        x: bind (dialogX + dialogWidth - size - 2*margin).intValue()
                        y: bind (dialogY + dialogHeight - size - 2*margin).intValue()
                        size: bind size
                        fill: bind getForeground(type)
                    }
                ]
        }
    };

}
