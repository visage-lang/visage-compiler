package jfx.assortis.lang.api.widgets.button;

import javafx.ui.*;

var buttonGroup = ButtonGroup{};

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals ) 
        RadioButton { text: animal buttonGroup: buttonGroup}
}
