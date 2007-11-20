package hello.canvas;

import hello.Image;
import com.sun.scenario.scenegraph.SGImage;
import com.sun.scenario.scenegraph.SGNode;

public class ImageNode extends Node {
    private attribute awtImage: java.awt.Image;
    private attribute sgimage: SGImage;
    protected function createNode(): SGNode {
	sgimage = new SGImage();
        sgimage.setImage(awtImage);
	return sgimage;
    }

    public attribute image: Image;
}