/* JFXC-272:  Bad class cast attribution with MI.
 *
 * @test
 * @compile jfxc272Canvas.fx
 * @compile jfxc272CanvasElement.fx 
 * @compile jfxc272Node.fx
 * @compile jfxc272Widget.fx 
 * @run
 */
class jfxc272Canvas extends jfxc272Widget, jfxc272CanvasElement {
    public attribute content: jfxc272Node[]
        on replace oldValue[a..b] = newElements {
            for (newValue in newElements)
                newValue.parentCanvasElement = this;
        }
}
