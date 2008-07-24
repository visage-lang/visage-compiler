package assortis.sources.language.javafx.events;

import javafx.ext.swing.*;
import java.lang.System;

SwingButton {
    text: "Click Me"
    action: function(){
        System.out.println("Button is pressed");
    }
}
