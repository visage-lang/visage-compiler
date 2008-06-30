package jfx.assortis.lang.api.widgets.combobox;

import javafx.ui.*;

var animals = [ "dog", "cat", "mouse" ];
var index = -1;


ComboBox{
    cells: bind for (animal in animals){
        ComboBoxCell{
            text: animal
        }
    }
    selection: bind index with inverse
}
