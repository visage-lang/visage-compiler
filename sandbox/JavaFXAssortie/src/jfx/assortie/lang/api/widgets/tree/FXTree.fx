package jfx.assortie.lang.api.widgets.tree;

import javafx.ui.*;
import java.lang.System;


Tree{
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
