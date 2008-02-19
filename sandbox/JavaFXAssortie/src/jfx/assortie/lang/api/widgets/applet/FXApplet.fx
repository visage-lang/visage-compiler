package jfx.assortie.lang.api.widgets.applet;

import javafx.ui.*;

class FXApplet extends Applet{
}

var name = "";

FlowPanel{
    content: [
    Label { text: "Enter name:"},
    TextField { value: bind name },
    Label{ text: bind "Hello {name}!"}    
    ]
}


