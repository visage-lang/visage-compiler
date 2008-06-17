package assortis.sources.language.javafx.api.gui;

import javafx.gui.component.*;

var index = 0;

var animals = ["Bird","Cat","Dog","Rabbit","Pig"];

ComboBox{
  items: for (animal  in animals )[ 
      ComboBoxItem { text: animal}
  ]
  selectedIndex: bind index with inverse
}
