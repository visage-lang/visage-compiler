package jfx.assortie.lang.api.widgets.button;

import javafx.ui.*;
import java.lang.System;

Button {
    text: "Click Me"
    action: function(){
        System.out.println("Button is pressed");
    }
}
