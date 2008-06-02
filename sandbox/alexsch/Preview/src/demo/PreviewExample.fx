
package demo;

import javafx.gui.*;
import javafx.tools.*;

import java.lang.System;

var code = "import javafx.gui.*;\nLabel\{ text: \"Hello World!\"\}\n";

Frame{
    width: 300
    height: 300
    title: "Preview Example"
    closeAction: function(){ System.exit(0); }
    
//        content: Panel{
//            content: [
//                BorderPanel{ center: Preview{} } , 
//                BorderPanel{ center: Editor{} }
//            ]
//        }
    
    
    content: BorderPanel{
        top: Preview{
            code:  bind code 
        }
        center: BorderPanel{
            center: Editor{
                dropEnable: true
                text:  bind code with inverse
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