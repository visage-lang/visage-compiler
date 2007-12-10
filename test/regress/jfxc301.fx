/* JFXC-301: cannot cast "this" in Rect.fx to super class SizeableCanvasElement
 * @test
 */
package javafx.ui.canvas;

import javafx.ui.canvas.Node;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import java.lang.Object;
import javafx.ui.canvas.*;

class jfxc301 extends Node, Container {
    public attribute content: Node[] on insert [indx] (newValue) {
        newValue.parentCanvasElement = this as CanvasElement;
    } on replace [indx] (oldValue) {
        var newValue = content[indx];
        if (newValue <> null) {
            if (oldValue.parentCanvasElement == (this as CanvasElement)) {
            }
        }
    }
    on delete [indx] (oldValue) {
    };

    public function createNode(): SGNode {
        return null;
    }

    public function raiseNode(n:Node):Void {
    }

    public function lowerNode(n:Node):Void {
    }
    
    public function moveNodeToFront(n:Node):Void {
    }

    public function moveNodeToBack(n:Node):Void {
    }
}