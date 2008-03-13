package jfx.assortis.lang.api.widgets.slider;

import javafx.ui.*;

var value = 20.0;

BorderPanel{
    top: Label{ text: bind "Slider value: {value}"}
    center: Slider{
        min: -100
        max: 100
        minorTickSpacing: 10
        majorTickSpacing: 50
        paintTicks: true
        paintTrack: true
        paintLabels: true
        filled: true
        value: bind value with inverse
    }
}