package casual.ui;

import casual.theme.*;

import javafx.ui.Canvas;
import javafx.ui.AbstractColor;
import javafx.ui.Color;
import javafx.ui.Font;
import javafx.ui.EmptyBorder;

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
    
    attribute clickTrigger: Boolean
        on replace {
            if (clickTrigger == true)
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
    
    private attribute pf: PointerFactory = PointerFactory{};
    private attribute __clickTrigger = bind pf.make(clickTrigger);
    private attribute _clickTrigger = __clickTrigger.unwrap();
    private attribute clickTriggerAnim: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _clickTrigger
                        value: true
                    }
                ]
             },
             KeyFrame {
               keyTime: 150ms
               keyValues:  [
                    NumberValue {
                        target: _clickTrigger
                        value: false
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
            content:
            [
                ClickRect
                {
                    var pressed = false
                    var strokeWidth = bind theme.uiStrokeWidth

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
                        if ((pressedHover == true) and (onClick <> null))
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
                    fill: bind if (pressedHover==true) then theme.uiBackground.darker() else theme.uiBackground
                    stroke: bind theme.uiBorderColor
                    strokeWidth: bind strokeWidth
                },
                Text
                {
                    selectable: true
                    editable: false
                    x: bind (this.x+this.width/2)
                    y: bind (this.y+this.height/2)
                    valign: MIDDLE
                    halign: CENTER	
                    fill: bind theme.uiForeground
                    content: bind text
                    font: bind font
                }
            ]
        }
    };
}



