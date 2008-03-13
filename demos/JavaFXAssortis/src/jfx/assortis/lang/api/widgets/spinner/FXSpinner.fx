package jfx.assortis.lang.api.widgets.spinner;

import javafx.ui.*;

var value = 20.0;

FlowPanel{
    content: [
    Label{ text: bind "Spinner value: {value}"},
    Spinner{
        min: -100
        max: 100
        stepSize: 5
        value: bind value with inverse
    }]
}