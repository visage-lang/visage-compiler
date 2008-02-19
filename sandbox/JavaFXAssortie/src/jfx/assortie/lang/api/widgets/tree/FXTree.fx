package jfx.assortie.lang.api.widgets.tree;

import javafx.ui.*;
import java.lang.System;


class A{
    attribute selected: Boolean on replace{
        System.out.println("selected: {selected}");
    };
}

var a = A{};

Frame {
    title: "JavaFX Tree"
    width:  300
    height: 150
    
    content: Tree{
        root: TreeCell{
            text: "Root Node"
            cells: [TreeCell {
                text: "colors"
                cells:
                    [TreeCell {
                    text: "<html><font color='blue'>blue</font></html>"
                },
                TreeCell {
                    text: "<html><font color='red'>red</font></html>"
                },
                TreeCell {
                    text: "<html><font color='green'>green</font></html>"
                }]
            },
            TreeCell {
                text: "food"
                cells:
                    [TreeCell {
                    text: "hot dogs"
                },
                TreeCell {
                    text: "pizza"
                },
                TreeCell {
                    text: "ravioli"
                }]
            }]
        }
    }
    
    visible: true
}