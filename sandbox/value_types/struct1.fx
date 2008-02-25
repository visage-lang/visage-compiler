import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

struct Point {
    attribute x: Number;
    attribute y: Number;
    operation translate(dx:Number, dy:Number):Point;
    operation makeTranslated(dx:Number, dy:Number):Point;
    operation moo();
    
}

operation Point.makeTranslated(dx, dy) {
    return Point {x: x + dx, y: y + dy};
}

operation Point.translate(dx, dy) {
    x += dx;
    y += dy;
    return this;
}

operation Point.moo() { translate(5, 5); translate(8, 8); x += 7;}

struct Line {
    attribute p1: Point;
    attribute p2: Point;
}


var p = Point;
p.moo();
var p1 = p.makeTranslated(5, 5).translate(100, 100);
p1.translate(1, 1);
[p, p1];