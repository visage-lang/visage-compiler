package assortis.sources.language.javafx.api.shapes;

import javafx.gui.*;

var x1 = 40;
var y1 = 30;

var x2 = 120;
var y2 = 30;

var radius = 20;

var color = Color.BLUE;

Group{
    content: [
        Circle{ centerX: x1, centerY: y1, radius: radius, fill: color  },
        Line{ x1: x1, y1: y1 x2: x2, y2: y2, stroke: color strokeWidth: 4},
        Circle{ centerX: x2, centerY: y2, radius: radius, fill: color  },
    ]
}
