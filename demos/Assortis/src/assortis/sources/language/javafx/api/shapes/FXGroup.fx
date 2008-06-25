package assortis.sources.language.javafx.api.shapes;

import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;

var x1 = 40;
var y1 = 30;

var x2 = 120;
var y2 = 30;

var radius = 20;

var color = Color.BLUE;

Group{
    content: [
        Circle{ centerX: x1, centerY: y1, radius: radius, fill: color  },
        Line{ startX: x1, startY: y1 endX: x2, endY: y2, stroke: color strokeWidth: 4},
        Circle{ centerX: x2, centerY: y2, radius: radius, fill: color  },
    ]
}
