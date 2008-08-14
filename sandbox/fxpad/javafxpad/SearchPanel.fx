package javafxpad;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.canvas.Group;
import javafx.ui.Font;

public class SearchPanel extends CompositeNode {
    attribute closeAction: function()?;
    attribute searchNextAction: function()?;
    attribute searchPrevAction: function()?;
    attribute highlightAllAction: function()?;
    attribute searchValue: String;    
    attribute matchCase: Boolean;
    attribute open: Boolean;
}

public class CloseButton extends CompositeNode {
    attribute pressed: Boolean;
    public attribute action: operation()?;
}

function CloseButton.composeNode() =
Group {
    var: g
    var pressHover = bind pressed and g.hover
    var r = 6
    var fill:Paint = bind if g.hover then gray else new Color(.7, .7, .7, 1)
    var stroke:Paint = bind if pressHover then new Color(.8, .8, .8, 1) else white
    content: 
    [Circle {
        onMousePressed: operation(e) {pressed = true;}
        onMouseReleased: operation(e) {if (g.hover) {(this.action)();} pressed = false;}
        selectable: true
        cx: r
        cy: r
        radius: r
        fill: bind fill
        stroke: bind fill
    },
    Line {
        x1: r/2
        y1: r/2
        x2: r + r/2
        y2: r + r/2
        stroke: bind stroke
    },
    Line {
        x1: r/2
        y1: r + r/2
        x2: r + r/2
        y2: r/2
        stroke: bind stroke
    }]
    
};

public class SearchField extends CompositeNode {
    attribute columns: Integer;
    attribute value: String;
    attribute action: operation()?;
    attribute cancel: operation()?;
    attribute textField: TextField;
    attribute baseline: Number;
}

attribute SearchField.columns = 13;


function SearchField.composeNode() =
Group {
    var tf = TextField {
        attribute: textField
        font: new Font("VERDANA", "PLAIN", 12)
        focused: true
        columns: bind columns, 
        border: EmptyBorder, 
        background: new Color(0, 0, 0, 0)
        action: bind action
        text: bind value
        onKeyDown: operation(e) {
            if (e.keyStroke == ESCAPE:KeyStroke) {
                (this.cancel)();
            }
        }
    }
    //var dummy = println(tf.getComponent())
    var view = View {content: tf, baseline: bind baseline}
    content:
    [Rect {
        height: bind view.currentHeight + 4
        width: bind view.currentWidth + 20
        arcHeight: 20
        arcWidth: 20
        stroke: bind if tf.focused then transparent(dodgerblue:Color, 0.8) else gray
        strokeWidth: 2
        fill: white
    },
    Group {
        transform: translate(10, 2)
        content: view
    }]
};

class SearchButton extends CompositeNode {
    attribute icon: Image;
    attribute text: String;
    attribute height: Number;
    attribute baseline: Number;
    attribute font: Font?;
    attribute action: function()?;
    attribute pressed: Boolean;
}

attribute SearchButton.font = new Font("VERDANA", "BOLD", 11);

function SearchButton.composeNode() =
Group {
    var content = HBox {
        content:
        [ImageView {
            image: bind icon
            transform: bind translate(0, height/2)
            valign: MIDDLE
        },
        Text {
            var: self
            font: bind font
            //valign: MIDDLE
            verticalAlignment: BASELINE
            transform: bind translate(5, baseline)
            content: bind text
            fill: bind if hover then white else black
        }]
    }
    content:
    [Rect {
        onMousePressed: operation(e) {pressed = true;}
        onMouseReleased: operation(e) {
            if (hover) {
                (this.action)();
            }
            pressed = false;
        }
        var grad = LinearGradient {
            x2: 0
            y2: 1
            stops:
            [Stop {
                offset: 0
                color: new Color(.6, .6, .6, 1)
            },
            Stop {
                offset: 1
                color: new Color(.75, .75, .75, 1)
            }]
        }
        height: bind height
        width: bind content.currentWidth + 20
        arcHeight: 20
        arcWidth: 20
        selectable: true
        fill: bind if hover then grad else new Color(0, 0, 0, 0)
        stroke: bind if pressed then gray else new Color(0, 0, 0, 0)
    },
    Group {
        transform: translate(5, 0)
        content: content
    }]
};

function SearchPanel.composeNode() =

HBox {
    
    var searchField = SearchField {
        var: field
        trigger on (newValue = open) {
            if (newValue) {
                do later {
                    field.textField.getComponent().requestFocus(); // hack: fix me
                    }
            }
        }
        transform: translate(10, 0)
        value: bind searchValue
        cancel: bind closeAction
    }
    //trigger on (newValue = searchField.baseline) {println("baseline = {newValue}");}
    var b = bind searchField.baseline
    content:
    [CloseButton {
        transform: translate(5, 24/2)
        action: bind closeAction
        valign: MIDDLE
    },
    Text {
        var: self
        transform: bind translate(10, 2+b)
        verticalAlignment: BASELINE
        font: new Font("VERDANA", "BOLD", 11)
        content: "Find:" 
    },
    searchField,
    SearchButton {
        height: 18
        baseline: bind b
        transform: translate(5, 2)
        text: "Next"
        action: bind searchNextAction
    },
    SearchButton {
        height: 18
        baseline: bind b
        transform: translate(5, 2)
        text: "Previous"
        action: bind searchPrevAction
    },
    SearchButton {
        height: 18
        baseline: bind b
        transform: translate(5, 2)
        text: "Highlight all"
        action: bind highlightAllAction
    },
    View {
        transform: translate(0, -1)
        content: CheckBox {
            text: "Match Case", 
            font: new Font("VERDANA", "BOLD", 11), 
            focusable: false
            selected: bind matchCase
        }
    },
    Text {
        visible: false
        var: self
        transform: bind translate(0, 2+b)
        verticalAlignment: BASELINE
        font: new Font("VERDANA", "BOLD", 11)
        content: "Match case" 
    },
    SearchButton {
        visible: false
        height: 18
        transform: translate(5, 2)
        text: "Replace"
    },
    SearchButton {
        visible: false
        height: 18
        transform: translate(5, 2)
        text: "Replace All"
    }]
};

SearchPanel {
    
}
