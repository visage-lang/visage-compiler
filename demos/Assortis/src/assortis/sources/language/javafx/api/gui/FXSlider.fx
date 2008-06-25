package assortis.sources.language.javafx.api.gui;

import javafx.ext.swing.*;

var value = 20;

BorderPanel{
    top: Label{ text: bind "Slider value: {value}"},
    center: Slider{
        minimum: -50
        maximum: 50
        value: bind value with inverse
    }
}
