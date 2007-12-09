import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.*;

Frame {
    title: "Circle Demo"
    width: 320
    height: 200
    background: Color.GREY
    content: Canvas {
        content: [
            Circle {
                cx: 100
                cy: 100
                radius: 100
                fill: Color.BLUE
            }
        ]
    }
    visible: true
}
