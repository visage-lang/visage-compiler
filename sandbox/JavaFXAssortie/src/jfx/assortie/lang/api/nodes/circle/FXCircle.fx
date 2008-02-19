package jfx.assortie.lang.api.nodes.circle;

import javafx.ui.*;
import javafx.ui.canvas.*;

Frame {
    title: "JavaFX Circle"
    width:  200
    height: 200
    content: Canvas{
	content: Circle{
		cx: 100
		cy: 100
		radius: 50
		fill: Color.ORANGE
		}
    }
    visible: true   
}