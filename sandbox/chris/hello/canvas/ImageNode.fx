package hello.canvas;

import hello.Image;
import com.sun.scenario.scenegraph.SGImage;
import com.sun.scenario.scenegraph.SGNode;

public class ImageNode extends Node {
    var awtImage: java.awt.Image;
    var sgimage: SGImage;
    protected function createNode(): SGNode {
	sgimage = new SGImage();
        sgimage.setImage(awtImage);
	return sgimage;
    }

    public var image: Image;
}
