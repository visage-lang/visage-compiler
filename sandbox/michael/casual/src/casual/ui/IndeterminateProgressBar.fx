package casual.ui;

import javafx.ui.AbstractColor;

import javafx.ui.canvas.Node;
import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Circle;
import javafx.ui.canvas.Rect;
import javafx.ui.canvas.Translate;
import javafx.ui.canvas.Rotate;

import javafx.ui.animation.Timeline;
import javafx.ui.animation.KeyFrame;
import javafx.ui.animation.NumberValue;
import com.sun.javafx.runtime.PointerFactory;

class IndeterminateProgressBar extends CompositeNode
{
    public attribute active: Boolean
        on replace {
            if (active) {
                rot1Timeline.start();
                rot2Timeline.start();
            } else {
                rot1Timeline.stop();
                rot2Timeline.stop();
            }
        }
    ;
    public attribute size: Integer;
    public attribute x: Integer;
    public attribute y: Integer;
    public attribute fill: AbstractColor;

    private attribute pf: PointerFactory = PointerFactory{};
    private attribute rot1: Integer;
    private attribute __rot1 = bind pf.make(rot1);
    private attribute _rot1 = __rot1.unwrap();
    private attribute rot1Timeline: Timeline = Timeline {
        repeatCount: java.lang.Double.POSITIVE_INFINITY
        keyFrames: [
             KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _rot1
                        value: 0
                    }
                ]
             },
             KeyFrame {
               keyTime: 1000ms
               keyValues:  [
                    NumberValue {
                        target: _rot1
                        value: 360
                        interpolate: NumberValue.LINEAR
                    }
                ]
             }             
        ]
    };

    private attribute rot2: Integer;
    private attribute __rot2 = bind pf.make(rot2);
    private attribute _rot2 = __rot2.unwrap();
    private attribute rot2Timeline: Timeline = Timeline {
        keyFrames: [
             KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _rot2
                        value: 0
                    }
                ]
             },
             KeyFrame {
               keyTime: 12000ms
               keyValues:  [
                    NumberValue {
                        target: _rot2
                        value: 360
                        interpolate: NumberValue.LINEAR
                    }
                ]
             }             
        ]
    };

    function composeNode() {
        Group {
            var strokeWidth = bind (size/8)

            visible: bind active
            transform: Translate {x: x, y: y}
            content:
            [
                Circle
                {
                    stroke: fill
                    strokeWidth: bind strokeWidth
                    cx: bind size
                    cy: bind size
                    radius: bind size
                } as Node,
                Rect
                {
                    fill: fill
                    height: bind (2*strokeWidth)
                    width: bind (size - (2*strokeWidth))
                    arcHeight: bind (2*strokeWidth)
                    arcWidth: bind (2*strokeWidth)
                    transform: bind [Translate {x: size-strokeWidth, y: size-strokeWidth}, Rotate {angle: if (active) then rot1 else 0, cx: strokeWidth, cy: strokeWidth}]
                } as Node,
                Rect
                {
                    fill: fill
                    height: bind (2*strokeWidth)
                    width: bind (size - (3*strokeWidth))
                    arcHeight: bind (2*strokeWidth)
                    arcWidth: bind (2*strokeWidth)
                    transform: bind [Translate {x: size-strokeWidth, y: size-strokeWidth}, Rotate {angle: if (active) then rot2 else 0, cx: strokeWidth, cy: strokeWidth}]
                } as Node
            ]
        }
    };
}
