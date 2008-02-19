package jfx.assortie.lang.api.widgets.box;

import javafx.ui.*;
import java.lang.System;

var list = [0];
var n = 0;



Frame{
    
    title: "Box Example"
    
    content: BorderPanel{
        center: Box{
            content: Label{ text: "Box"}
        }
        bottom: FlowPanel{
            content: [ 
            Button{ 
                text: "Add"
                action: function(){
                    n = n+1;
                    insert n  into list;
                    System.out.println("Hello World!");
                }
                }
            ]
        }
    }
    
    visible: true
}