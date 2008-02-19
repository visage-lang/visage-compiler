package jfx.assortie.lang.api.widgets.button;

import javafx.ui.*;
import java.lang.System;

Frame {
    title: "JavaFX Button"
    width:  200
    height: 100
    content: Button {
        text: "Click Me"
        action: function(){
            System.out.println("Button is pressed");
        }
    }
    visible: true
}