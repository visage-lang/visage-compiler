/*
 * SimpleApplet.fx
 *
 * Created on Dec 20, 2007, 2:49:07 PM
 */

import javafx.ui.*;

public class SimpleApplet extends Applet {
    init {
        content = Button { text: "JavaFX Script Button" }
    }
}

var applet = new SimpleApplet;
