/*
* @test
* @compilefirst jfxc559a.fx
*/

import javafx.ui.*;
public class jfxc559b extends jfxc559a {
    init {
        center = BorderPanel {
            top: Label{text: "label"}
        }
    }
}

Frame {
  width: 300
  height: 100
  visible: true
  content: jfxc559b {}
}
