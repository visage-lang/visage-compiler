import javafx.ui.*;
import javafx.ui.canvas.*;

public class SRadioButton extends CompositeNode {
    attribute enabled: Boolean;
    attribute selected: Boolean;
}

attribute SRadioButton.enabled = true;

function SRadioButton.composeNode() =
Group {
    transform: translate(-4, -4)
    content:
    [Circle {
        cx: 10
        cy: 11.5
        radius: 10
        strokeWidth: 1
        fill: white
        stroke: new Color(.9, .9, .9, .7)
    },
    Circle {
        onMouseClicked: operation(e) { if (enabled) { selected = not selected; } }
        cx: 10
        cy: 10
        radius: 10
        strokeWidth: .75
        stroke: bind if selected then new Color(.4, .4, .4, 1) else new Color(.2, .2, .2, 1)
        fill: RadialGradient {
            cx: 10,  radius: 20
            cy: bind if selected then 12 else -2
            stops:
            [Stop {
                offset: 0.33
                color: bind if selected then new Color(.85, .85, .85, 1) else white
                
            },
            Stop {
                offset: 1
                color: bind if not selected then new Color(.62, .62, .62, 1) else new Color(.2, .2, .2, 1)
            }]
        }
        var fill = 
        LinearGradient {
            x2: 0, y2: 1
            stops:
            [Stop {
                offset: 0.2
                //color: new Color(1, 1, 1, 1)
                color: bind if selected then new Color(.7, .7, .7, 1) else white
                
            },
            Stop {
                offset: 1
                color: new Color(.62, .62, .62, 1)
            }]
        }
        
    },
    Circle {
        strokeWidth: 1
        opacity: 0.3
        cx: 10
        cy: 10
        radius: 9
        stroke: gray
        visible: bind selected
    },
    Circle {
        cx: 10
        cy: 10
        radius: 3
        strokeWidth: 0.5
        stroke: bind if selected then new Color(.8, .8, .8, 1) else white
        fill: bind if enabled then black else gray
    }]
};

public class SCheckBox extends CompositeNode {
    public attribute textColor: Paint;
    public attribute text: String;
    public attribute selected: Boolean;
    public attribute font: Font;
}
attribute SCheckBox.font = Font { face: LUCIDA_SANS,  size: 12 };
function SCheckBox.composeNode() =
Group {
    transform: translate(6, 6)
    content:
    [HBox {
        spacing: 4
        content:
        [Rect {
            onMouseClicked: operation(e) { selected = not selected; }
            arcHeight: 4
            arcWidth: 4
            height: 12
            width: 12
            fill: LinearGradient {
                x2: 0, y2: 1
                stops:
                [Stop {
                    offset: .2
                    color: new Color(.9, .9, 1, 1) 
                },
                Stop {
                    offset: 1
                    color: new Color(.6, .6, .9, 1)
                }]
            }
            stroke: gray
        },
        Text {
            transform: translate(0, 10)
            verticalAlignment: BASELINE
            content: bind text
            font: bind font
        }]
    },
    Path {
        opacity: bind if selected then .8 else 0
        transform: translate(-1, -2)
        strokeWidth: 1
        stroke: new Color(.3, .3, .3, 1)
        fill: black
        d:
        [MoveTo {x: 3, y: 5},
        LineTo {
            x: 7, y: 10
        },
        LineTo {
            x: 9, y: 10
        },
        LineTo {
            x: 16, y: 0
        },
        LineTo {
            x: 14, y: 0
        },
        LineTo {
            x: 8, y: 9
        },
        LineTo {
            x: 5, y: 5
        },
        ClosePath]
    }]
};

public class SButton extends CompositeNode {
    public attribute textColor: Paint;
    public attribute text: String;
    public attribute icon: Node?;
    public attribute height: Number;
    public attribute width: Number;
    public attribute font: Font;
    attribute pressed: Boolean;
    operation mousePress(mouseEvent:CanvasMouseEvent);
    operation mouseRelease(mouseEvent:CanvasMouseEvent);
}
attribute SButton.font = Font { face: LUCIDA_SANS,  size: 12 };
attribute SButton.height = 21;
attribute SButton.width = 80;
attribute SButton.textColor = black;

operation SButton.mousePress(mouseEvent) {
    pressed = true;
}

operation SButton.mouseRelease(mouseEvent) {
    pressed = false;
}

function SButton.composeNode() =
Group {
    content:
    [Rect {
        onMousePressed: operation(mouseEvent) { mousePress(mouseEvent); }
        onMouseReleased: operation(mouseEvent) { mouseRelease(mouseEvent); }
        arcHeight: 21
        arcWidth: 21
        height: bind height
        width: bind width
        var topColor = new Color(1, 1, 1, 1)
        var topPressColor = new Color(.5, .5, .5, 1)
        var botColor = new Color(.6, .6, .6, 1)
        fill: LinearGradient {
            x2: 0, y2: 1
            stops:
            [Stop {
                offset: .2
                color: bind if hover and pressed then topPressColor else topColor
            },
            Stop {
                offset: 1
                color: botColor
            }]
        }
        stroke: new Color(.2, .2, .2, 1)
        strokeWidth: 0.5
    //stroke: new Color(.6, .6, .6, 1)
    },
    Text {
        transform: bind translate(width/2, height/2)
        valign: MIDDLE, halign: CENTER
        content: bind text
        font: bind font
        fill: bind textColor
    }]
};

SButton {
    text: "Refresh"
}
SCheckBox {
    text: "Refresh"
}
Group {
    content:
    [Rect {
        height: 100
        width: 300
        fill: black
        fill: new Color(.7, .7, .7, 1)
    },
    HBox {
        spacing: 5
        content:
        [foreach (i in [1..3])
        SRadioButton {
            transform: translate(5, 5)
        },
        SButton { text: "Refresh" },
        SCheckBox {
            text: "Refresh"
        }]
    }]
}
