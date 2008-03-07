package jfx.assortie.lang.api.widgets.box;

import javafx.ui.*;
import java.lang.System;

var list = [1];



Frame{
    width: 200
    height: 200
    title: "Box Example"
    
    content: BorderPanel{
        center: Box{
            content: bind for( i in list) Label{ text: "Box {i}"}
        }
        bottom: FlowPanel{
            content: [
            Button{
                text: "Add"
                action: function(){
                    insert list[sizeof list - 1] into list;
                }
            }
            ]
        }
    }
    
    visible: true
}