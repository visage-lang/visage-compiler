/*
 * This file is created with Netbeans 6 Plug-In for JavaFX
 */

package lightsout;

import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;


public class LightsOutMain extends Group {
    // main game screen
    var locan:LightsOutCanvas = LightsOutCanvas { opacity:0 };
    //splash screen
    var screen:LightsOutSplash = LightsOutSplash { main:this, opacity: 1.0 };
}

// do init stuff
var main = LightsOutMain { };
main.screen.locan = main.locan;
main.locan.model.randomize();

//show on screen
main.content = [main.screen];
Frame{
    title: "Lights Out"
    visible: true
    width: 400,
    height: 300+16,
    resizable: false,
    background: Color.BLACK
    content: Canvas { content: [main] }
    /**
    content: RootPane {
        background: Color.BLACK
        content: Canvas { content: [main] }
    }
     * **/
    onClose: function() {System.exit(0);}
}

main.screen.transitionIn();
