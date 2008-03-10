package jfx.assortie.lang.api.widgets.table;

import javafx.ui.*;
import java.lang.System;


var N=5;


Table {
    columns: [
    TableColumn {
        text: "number"
    },
    TableColumn {
        text: "square"
    },
    TableColumn {
        text: "cube"
    }]
    
    cells: for (i in [1..N])[
    TableCell {
        text: "{i}"
    },
    TableCell {
        text:  "{i*i}"
    },
    TableCell {
        text: "{i*i*i}"
    }
    ]
}
