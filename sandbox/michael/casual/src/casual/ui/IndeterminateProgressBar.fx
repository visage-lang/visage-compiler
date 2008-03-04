package casual.ui;

import javafx.ui.AbstractColor;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Circle;
import javafx.ui.canvas.Rect;

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
        repeatCount: Timeline.INFINITY
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
            transform: translate(x, y)
            content:
            [
                Circle
                {
                    stroke: fill
                    strokeWidth: bind strokeWidth
                    cx: bind size
                    cy: bind size
                    radius: bind size
                },
                Rect
                {
                    fill: fill
                    height: bind (2*strokeWidth)
                    width: bind (size - (2*strokeWidth))
                    arcHeight: bind (2*strokeWidth)
                    arcWidth: bind (2*strokeWidth)
                    transform: bind [translate(size-strokeWidth, size-strokeWidth), rotate(if (active) then rot1 else 0, strokeWidth, strokeWidth)]
                },
                Rect
                {
                    fill: fill
                    height: bind (2*strokeWidth)
                    width: bind (size - (3*strokeWidth))
                    arcHeight: bind (2*strokeWidth)
                    arcWidth: bind (2*strokeWidth)
                    transform: bind [translate(size-strokeWidth, size-strokeWidth), rotate(if (active) then rot2 else 0, strokeWidth, strokeWidth)]
                }
            ]
        }
    };
}
