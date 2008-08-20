/* JFXC-272:  Bad class cast attribution with MI.
 *
 * @test
 */
class Widget {
}

class CanvasElement {
	public var parentCanvasElement: CanvasElement;
}

class Node extends CanvasElement {
    var cachedCanvas: Canvas;
    public function getCanvas(): Canvas {
        var n = this.parentCanvasElement;
        while (n != null) {
            if (n instanceof Canvas) {
                cachedCanvas = n as Canvas;
                return cachedCanvas;
            }
            n = n.parentCanvasElement;
        }
        return cachedCanvas;
    }
}

class Canvas extends Widget, CanvasElement {
    public var content: Node[]
        on replace oldValue[a..b] = newElements {
            for (newValue in newElements) {
                newValue.parentCanvasElement = this as CanvasElement;
            }
        };
}
	
