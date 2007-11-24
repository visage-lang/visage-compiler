/* JFXC-272:  Bad class cast attribution with MI.
 *
 * @test
 * @compile Canvas.fx
 * @compile CanvasElement.fx 
 * @compile Node.fx
 * @compile Widget.fx 
 * @run
 */
class Canvas extends Widget, CanvasElement {
    public attribute content: Node[]
        on insert [indx] (newValue) {
            newValue.parentCanvasElement = this;
        }
}
