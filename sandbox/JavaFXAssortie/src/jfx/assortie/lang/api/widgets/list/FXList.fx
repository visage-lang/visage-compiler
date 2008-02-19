package jfx.assortie.lang.api.widgets.list;

import javafx.ui.*;
import java.lang.System;


var n = 0.0;

Frame{
    width: 200
    height: 100
    title: "JavaFX List"
    content: ListBox{
        cells: for( i in [1..10])  ListCell{
            text: "{i}"
        }
        selection: bind n with inverse
        action: function(){
            System.out.println("double-click on cell {n + 1}");
        }
        
    }
    visible: true
}