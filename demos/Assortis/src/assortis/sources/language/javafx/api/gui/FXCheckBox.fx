package assortis.sources.language.javafx.api.gui;

import javafx.gui.*;

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals )[
        CheckBox { text: animal } 
    ]
}
