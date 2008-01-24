package anim;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.Pointer;
import com.sun.javafx.runtime.PointerFactory;
import java.lang.System;

var x = 0;
var pf = PointerFactory {};
var bpx = bind pf.make(x);
var px = bpx.unwrap();
var interpolate = NumberValue.LINEAR;
var t = Timeline {
    keyFrames:
    [KeyFrame {
        keyTime: 0s;
        keyValues:
        NumberValue {
            target: px;
            value: 0;
        }
    },
    KeyFrame {
        keyTime: 2s;
        keyValues:
        NumberValue {
            target: px;
            value: 400;
            interpolate: bind interpolate
        }
    }]
};

Frame {
    onClose: function():Void { System.exit(0); }
    height: 500, width: 500;
    visible: true
    content:
    BorderPanel {
        center: Canvas {
            background: Color.WHITE
            content:
            [View {
                transform: Transform.translate(75, 75);
                content:
                Label {
                    font: Font { face: FontFace.ARIAL, size: 30 };
                    foreground: Color.BLUE;
                    text: "<html><div width='400'>This simple example demostrates the various built-in interpolation functions. Click one of the buttons to animate the green Rect's x coordinate from 0 to 400 over 2 seconds</div></html>";
                }
            },
            Rect {
                x: bind x;
                height: 50;
                width: 50;
                fill: Color.GREEN
            }]
        }
        bottom: GridPanel {
            rows: 0
            columns: 3        
            cells:
            [Button {
                text: "EaseIn";
                action: function():Void { interpolate = NumberValue.EASEIN; t.start(); }
            },
            Button {
                text: "EaseOut";
                action: function():Void { interpolate = NumberValue.EASEOUT; t.start(); }
            },
            Button {
                text: "EaseBoth";
                action: function():Void { interpolate = NumberValue.EASEOUT; t.start(); }
            },
            Button {
                text: "Quad-EaseIn";
                action: function():Void { interpolate = NumberValue.QuadEase.EASEIN; t.start(); }
            },
            Button {
                text: "Quad-EaseOut";
                action: function():Void { interpolate = NumberValue.QuadEase.EASEOUT; t.start(); }

            },
            Button {
                text: "Quad-EaseBoth";
                action: function():Void { interpolate = NumberValue.QuadEase.EASEBOTH; t.start(); }

            },
            Button {
                text: "Cubic-EaseIn";
                action: function():Void { interpolate = NumberValue.CubicEase.EASEIN; t.start(); }
            },
            Button {
                text: "Cubic-EaseOut";
                action: function():Void { interpolate = NumberValue.CubicEase.EASEOUT; t.start(); }

            },
            Button {
                text: "Cubic-EaseBoth";
                action: function():Void { interpolate = NumberValue.CubicEase.EASEBOTH; t.start(); }

            },
            Button {
                text: "Quint-EaseIn";
                action: function():Void { interpolate = NumberValue.QuintEase.EASEIN; t.start(); }
            },
            Button {
                text: "Quint-EaseOut";
                action: function():Void { interpolate = NumberValue.QuintEase.EASEOUT; t.start(); }

            },
            Button {
                text: "Quint-EaseBoth";
                action: function():Void { interpolate = NumberValue.QuintEase.EASEBOTH; t.start(); }

            },
            Button {
                text: "LINEAR";
                action: function():Void { interpolate = NumberValue.LINEAR; t.start(); }

            },
            Button {
                text: "DISCRETE";
                action: function():Void { interpolate = NumberValue.DISCRETE; t.start(); }

            }]
        }
    }
}
