package assortis.sources.language.javafx.api.gui;

import javafx.ext.swing.*;

var toggleGroup = SwingToggleGroup{};

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals )[ 
        SwingRadioButton { text: animal  toggleGroup: toggleGroup}
    ]
}
