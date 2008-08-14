package casual.ui;

import casual.theme.*;

import javafx.ui.Canvas;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;
import javafx.ui.HorizontalAlignment;
import javafx.ui.VerticalAlignment;

import javafx.ui.animation.Timeline;
import javafx.ui.animation.KeyFrame;
import javafx.ui.animation.NumberValue;
import com.sun.javafx.runtime.PointerFactory;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Text;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.View;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.CanvasMouseEvent;

class ClickRect extends Rect
{
    attribute pressedHover: Boolean;
}

public class Button extends CompositeNode
{
    public attribute x: Integer;
    public attribute y: Integer;
    public attribute width: Integer;
    public attribute height: Integer;
    public attribute text: String;
    public attribute font: Font;
    
    public attribute onClick: function();
    
    attribute clickTarget: ClickRect;
    
    attribute clickTrigger: Number
        on replace {
            if (clickTrigger < 1)
            {
                hover = true;

                var event:CanvasMouseEvent = null;
                (clickTarget.onMousePressed)(event);
            }
            else
            {
                var event:CanvasMouseEvent = null;
                (clickTarget.onMouseReleased)(event);

                hover = false;
            }
        }

    ;
    
    attribute pf: PointerFactory = PointerFactory{};
    attribute __clickTrigger = bind pf.make(clickTrigger);
    attribute _clickTrigger = __clickTrigger.unwrap();
    attribute clickTriggerAnim: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _clickTrigger
                        value: 0
                    }
                ]
             },
             KeyFrame {
               keyTime: 150ms
               keyValues:  [
                    NumberValue {
                        target: _clickTrigger
                        value: 1
                        interpolate: NumberValue.EASEBOTH
                    }
                ]
             }             
        ]
    };

    public function doClick() {
        clickTriggerAnim.start();
    }
    
    override attribute isSelectionRoot = true;

    function composeNode() { Group
        {
            var clickRect: ClickRect =
                ClickRect {
                    var pressed = false
                    var strokeWidth = bind ThemeManager.getInstance().uiStrokeWidth

                    selectable: true
                    pressedHover: bind (pressed and hover)

                    onMousePressed: function(e:CanvasMouseEvent)
                    {
                        pressed = true;
                    }
                    onMouseDragged: function(e:CanvasMouseEvent)
                    {
                    }
                    onMouseReleased: function(e:CanvasMouseEvent)
                    {   
                        if ((clickRect.pressedHover == true) and (onClick != null))
                        {
                            pressed = false;

                           //TODO DO LATER - this is a work around until a more permanent solution is provided
                            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                                public function run():Void {
                                    onClick();
                                }
                            });
                        }
                        else
                        {
                            pressed = false;
                        }
                    }

                    x: bind this.x
                    y: bind this.y
                    width: bind this.width-1
                    height: bind this.height-1
                    fill: bind if (clickRect.pressedHover==true) then ThemeManager.getInstance().uiBackground.darker() else ThemeManager.getInstance().uiBackground
                    stroke: bind ThemeManager.getInstance().uiBorderColor
                    strokeWidth: bind strokeWidth
                }
            content:
            [
                clickRect as Node,
                Text
                {
                    selectable: true
                    x: bind (this.x+this.width/2)
                    y: bind (this.y+this.height/2)
                    valign: VerticalAlignment.MIDDLE
                    halign: HorizontalAlignment.CENTER	
                    fill: bind ThemeManager.getInstance().uiForeground
                    content: bind text
                    font: bind font
                }  as Node
            ]
        }
    };
}



