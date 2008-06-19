package assortis.sources.language.javafx.api.gui;

import javafx.gui.*;
import javafx.gui.swing.*;

var toggleGroup = ToggleGroup{};

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals )[ 
        RadioButton { text: animal  toggleGroup: toggleGroup}
    ]
}
