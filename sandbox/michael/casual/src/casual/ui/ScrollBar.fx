/*

todo:

- better page scrolling, pause after first scroll then continue (like in iChat/Apple apps)

*/

package casual.ui;

import casual.theme.*;

import javafx.ui.AbstractColor;
import javafx.ui.Color;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Translate;
import javafx.ui.canvas.Polygon;
import javafx.ui.canvas.CanvasMouseEvent;
import javafx.ui.Orientation;

import java.lang.Math;

public class ScrollBar extends CompositeNode
{
    public attribute pageScrollFactor: Number;
    public attribute unitScrollFactor: Number;
    public attribute scrollFactor: Number; // proportional location of scroll position
    public attribute size: Number;  // size of this scrollbar in pixels
    public attribute orientation: Orientation = Orientation.HORIZONTAL; 
    public attribute showThumb: Boolean = false;
    public attribute showButtons: Boolean = false;

    // bug workaround
//    class Workaround extends Group
//    {
//    }
//    trigger on Workaround.visible = value
//    {
//        if (ag<>null and value==true)
//        {
//            do later
//            {
//                ag.repaint();
//            }
//        }
//    }

    function composeNode() {
        Group {
            var thickness = 19    
            var thumbSize = bind (3*thickness)
            var runSize = bind (size - thumbSize - 2*thickness) // run area of thumb where thumb is 3*thickness and 2 buttons are 2*thickness
            var scrollOffset = bind Math.max(runSize*scrollFactor, 0)
            var strokeWidth = bind theme.uiStrokeWidth
            var buttonAutorepeat = 1
            var thumbAutorepeat = 500

            focusable: false
            transform: bind if (orientation == Orientation.VERTICAL) then rotate(90, (thickness/2), (thickness/2)) else null

            content:
            [
                // track area background
                Rect
                {
                    width: bind size
                    height: thickness
                    fill: bind theme.chatPanelBackgroundDark
                },

                // track area
                Rect
                {
                    width: bind runSize + thumbSize - strokeWidth
                    height: thickness
                    stroke: bind if (showThumb==true) then theme.uiBorderColor else null
                    strokeWidth: bind strokeWidth
                    fill: bind theme.chatPanelBackgroundDark

                    var pressed = bind false
                    var pressHover = bind this.hover and pressed
                    var up = false
                    onMousePressed: function(e:CanvasMouseEvent)
                    {
                        pressed = true;
                        up = e.localX < scrollOffset;
                        if (up)
                        {
                            scrollFactor = Math.max(0.0, scrollFactor - pageScrollFactor);
                        }
                        else
                        {
                            scrollFactor = Math.min(1.0, scrollFactor + pageScrollFactor);
                        }
                    }
                    onMouseReleased: function(e:CanvasMouseEvent)
                    {
                        pressed = false;
                    }
                },

                // thumb
                Group
                {
                    visible: bind showThumb
                    content: Group
                    {
                        var pressed = bind false
                        var pressHover = bind pressed //and self.hover
                        var tx = Translate {x: bind scrollOffset, y: 0}
                        transform: tx
                        onMousePressed: function(e:CanvasMouseEvent)
                        {
                            pressed = true;
                        }
                        onMouseDragged: function(e:CanvasMouseEvent)
                        {
                            var x = scrollOffset + e.localDragTranslation.x;
                            var p = x/runSize;
                            if (p < 0)
                            {
                                p = 0;
                            }
                            if (p > 1)
                            {
                                p = 1;
                            }
                            scrollFactor = p;
                        }
                        onMouseReleased: function(e:CanvasMouseEvent)
                        {
                            pressed = false;
                        }

                        content:
                        [
                            Rect
                            {
                                focusable: false
                                selectable: true
                                fill: bind if (pressHover) then theme.uiBackground.darker() else theme.uiBackground
                                stroke: theme.uiBorderColor
                                strokeWidth: bind strokeWidth
                                height: thickness
                                width: bind (thumbSize-strokeWidth)
                            },
                            Rect
                            {
                                fill: bind theme.uiForeground
                                x: bind (thumbSize/2 - 4) -1.5
                                y: 3
                                height: thickness-7
                                width: 2
                            },
                            Rect
                            {
                                fill: bind theme.uiForeground
                                x: bind (thumbSize/2) -1.5
                                y: 3
                                height: thickness-7
                                width: 2
                            },
                            Rect
                            {
                                fill: bind theme.uiForeground
                                x: bind (thumbSize/2 + 4) -1.5
                                y: 3
                                height: thickness-7
                                width: 2
                            },
                            Rect
                            {
                                fill: bind if (pressHover) then theme.uiBackground.darker() else theme.uiBackground
                                x: 4
                                y: (thickness/2) - 1.5
                                height: 2
                                width: bind (thumbSize-8)
                            },
                        ]
                    }
                },

                // left/top button
                Group
                {
                    visible: bind showButtons
                    transform: bind translate(size-thickness-thickness-1, 0)
                    focusable: false
                    var pressed = bind false
                    var pressHover = bind pressed and this.hover
                        on replace {
                            if (pressHover)
                            {
                                 scrollFactor = Math.max(0.0, scrollFactor - unitScrollFactor);
                            }
                        }
                    onMousePressed: function(e:CanvasMouseEvent)
                    {
                        pressed = true;
                    }
                    onMouseReleased: function(e:CanvasMouseEvent)
                    {
                        pressed = false;
                    }

                    content:
                    [
                        Rect
                        {
                            focusable: false
                            selectable: true
                            width: thickness-1
                            height: thickness
                            fill: bind if (pressHover) then theme.uiBackground.darker() else theme.uiBackground
                            stroke: theme.uiBorderColor
                            strokeWidth: bind strokeWidth
                        },
                        Polygon
                        {
                            transform: translate((thickness/2), (thickness/2))
                            valign: HorizontalAlignment.CENTER
                            halign: HorizontalAlignment.CENTER
                            fill: bind theme.uiForeground
                            points: [0,4.5, 7,-0.5, 7,9.5]

                        }
                    ]
                },

                // right/bottom button
                Group
                {
                    visible: bind showButtons
                    transform: bind translate(size, 0)
                    focusable: false
                    halign: HorizontalAlignment.TRAILING
                    var pressed = bind false
                    var pressHover = bind pressed and this.hover
                        on replace {
                            if (newValue)
                            {
                                 scrollFactor = Math.min(1.0, scrollFactor + unitScrollFactor);
                            }
                        }
                    onMousePressed: function(e:CanvasMouseEvent)
                    {
                        pressed = true;
                    }
                    onMouseReleased: function(e:CanvasMouseEvent)
                    {
                        pressed = false;
                    }

                    content:
                    [
                        Rect
                        {
                            focusable: false
                            selectable: true
                            width: thickness-1
                            height: thickness
                            fill: bind if (pressHover) then theme.uiBackground.darker() else theme.uiBackground
                            stroke: theme.uiBorderColor
                            strokeWidth: bind strokeWidth
                        },
                        Polygon
                        {
                            transform: translate((thickness/2), (thickness/2))
                            valign: HorizontalAlignment.CENTER
                            halign: HorizontalAlignment.CENTER
                            fill: bind theme.uiForeground
                            points: [0,-0.5, 7,4.5, 0,9.5]
                        }
                    ]
                },
            ]
        }
    };
}

