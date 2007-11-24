/* JFXC-272:  Bad class cast attribution with MI.
 *
 * @test
 */
class Widget {
}

class CanvasElement {
	public attribute parentCanvasElement: CanvasElement;
}

class Node extends CanvasElement {
//    private attribute cachedCanvas: Canvas;
//    public function getCanvas(): Canvas {
//        var n = this.parentCanvasElement;
//        while (n <> null) {
//            if (n instanceof Canvas) {
//                cachedCanvas = n as Canvas;
//                return cachedCanvas;
//            }
//            n = n.parentCanvasElement;
//        }
//        return cachedCanvas;
//    }

}

class Canvas extends Widget, CanvasElement {
    public attribute content: Node[]
        on insert [indx] (newValue) {
            newValue.parentCanvasElement = this as CanvasElement;
        }
}
	
