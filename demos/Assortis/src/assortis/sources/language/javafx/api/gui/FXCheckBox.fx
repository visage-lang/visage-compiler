package assortis.sources.language.javafx.api.gui;

import javafx.ext.swing.*;

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals )[
        SwingCheckBox { text: animal } 
    ]
}

