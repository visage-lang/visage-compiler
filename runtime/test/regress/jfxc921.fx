/**
 * Regression test JFXC-921 : bind for (... ) [ ] construction is not compiled
 *
 * @test
 */

import javafx.ui.*;

var list = [1,3,5];

Frame{
   width: 300
   height: 200
   title: "JavaFX Frame"
   content: ListBox{
       cells: bind for (i in list)[
         ListCell{ text: "+{i}" },
         ListCell{ text: "-{i}" },
         ]
   }
   visible: true
}
