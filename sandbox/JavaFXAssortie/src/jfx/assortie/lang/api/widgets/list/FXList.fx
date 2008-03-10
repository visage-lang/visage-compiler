package jfx.assortie.lang.api.widgets.list;

import javafx.ui.*;
import java.lang.System;


var n = 0;

ListBox{
    cells: for( i in [1..10])  ListCell{
        text: "{i}"
    }
    selection: bind n with inverse
    action: function(){
        System.out.println("double-click on cell {n + 1}");
    }
}
