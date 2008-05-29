
package demo;

import javafx.gui.*;
import javafx.dev.*;

import java.lang.System;

var code = "import javafx.gui.*;\nLabel\{ text: \"Hello World!\"\}";

Frame{
    width: 300
    height: 300
    title: "Preview Example"
    closeAction: function(){ System.exit(0); }
    content: BorderPanel{
        center: Preview{
            code:  bind code 
        }
        bottom: TextField{
            text:  bind code with inverse
        } 
    }
    visible: true
}