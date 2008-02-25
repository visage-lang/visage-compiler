import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

struct Point {
    attribute x: Number;
    attribute y: Number;
    operation translate(dx:Number, dy:Number);
    operation moo();
    
}

operation Point.translate(dx, dy) {
    x += dx;
    y += dy;
}

operation Point.moo() { translate(5, 5); translate(8, 8); x += 7;}

struct Line {
    attribute p1: Point;
    attribute p2: Point;
}


var p1 = Point {x: 2, y: 3};
p1.translate(4, 4);

readonly var line = Line {p1: {x: 20 }, p2: p1};

var p2 = line.p2;
p2.moo();
//line.p2.translate(9, 9);

"p1={p1},\np2={p2},\nline={line}";
