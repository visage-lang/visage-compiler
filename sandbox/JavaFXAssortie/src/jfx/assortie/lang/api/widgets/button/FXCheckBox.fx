
package jfx.assortie.lang.api.widgets.button;

import javafx.ui.*;

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

FlowPanel{
    content: for (animal  in animals ) 
        CheckBox { text: animal}
}