
package demo;

import javafx.gui.*;
import javafx.tools.*;

import java.lang.System;

var code = "import javafx.gui.*;\nLabel\{ text: \"Hello World!\"\}\n";

var error: ErrorMessage;
var errors: ErrorMessage[];

Frame{
    width: 300
    height: 300
    title: "Preview Example"
    closeAction: function(){ System.exit(0); }
    
    content: BorderPanel{
        top: Preview{
            code:  bind code 
            errors: bind errors with inverse
            selectedError:  bind error with inverse
        }
        center: BorderPanel{
            center: ScrollPane { 
                view: Editor{
                    dropEnable: true
                    caretPosition: bind error.position
                    ErrorMessages: bind errors
                    text:  bind code with inverse
                } 
            }
            right: Palette{
                dragEnable: true
                items: [
                    PaletteItem{
                        name: "Label"
                        value: "Label\{ text: \"Hello World!\"\}\n"
                    },
                    PaletteItem{
                        name: "Button"
                        value: "Button\{ text: \"Click Me!\"\}\n"
                    },
                    PaletteItem{
                        name: "TextField"
                        value: "TextField\{ text: \"Text\"\}\n"
                    }
                ]
            }
        }
    }
    
    visible: true
}