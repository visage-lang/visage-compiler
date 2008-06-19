package assortis.sources.language.javafx.api.gui;

import javafx.gui.swing.*;

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals )[
        CheckBox { text: animal } 
    ]
}

