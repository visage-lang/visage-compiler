/*
 * Regression test: returning assignment
 *
 * @test
 * @run
 */

import java.awt.Dimension;

public class CanvasViewport {
    public attribute currentHeight: Integer;
    public attribute currentWidth: Integer;

    public attribute currentSize: Dimension = new Dimension(0, 0)
        on replace {
            currentWidth = currentSize.width;
            currentHeight = currentSize.height;
    };
    public function setSize(w:Integer, h:Integer){
        currentSize = new Dimension(w, h);
    }
}
