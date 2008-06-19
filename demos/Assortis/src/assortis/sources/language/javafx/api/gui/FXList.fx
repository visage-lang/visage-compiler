package assortis.sources.language.javafx.api.gui;

import javafx.gui.swing.*;

var numbers = ["one", "two", "three", "four", "five"];

List{
    items: for( number in numbers)[
        ListItem{ text: number }
    ]
}
