/* 
 * Regression test: JFXC-602: Compiler warning points an incorrect line number and position.
 *
 * @test/compile-error
 */

import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.*;

Frame {
    title: "javafx example"
    width: 800
    height: 600
    centerOnScreen: true
    undecorated: false
    visible: true
    
    var selected: Number = 0
    var widgets: Widget[] = [
        SimpleLabel{text: "Hello World!"},
        Label{text: "<html><h1>Hello World!</h1></html>"}
    ]
    
    content: FlowPanel {
        content: widgets[selected]
    }
}
