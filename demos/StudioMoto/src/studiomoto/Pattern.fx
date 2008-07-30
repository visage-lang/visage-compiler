/*
 * Pattern.fx
 *
 * Created on Jun 29, 2008, 6:57:46 PM
 */

package studiomoto;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.ext.swing.*;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import com.sun.scenario.scenegraph.JSGPanel;
/**
 * @author jclarke
 */

public class Pattern extends Canvas, Paint {
     private attribute texturePaint: TexturePaint;
     
    override function getAWTPaint(): java.awt.Paint {
    	makeTexture();
        if (texturePaint == null) {
            return java.awt.Color.BLACK;
	}
        return texturePaint;
 
    }
    
    public function makeTexture():Void {
        if (texturePaint == null) {
            var im:BufferedImage ;
            try {
                im  = (getJComponent() as JSGPanel).getIconImage() as BufferedImage;
            } catch (e) {
                return;
            }
            var w:Number = im.getWidth();
            var h:Number = im.getHeight();
            if (height != 0) {
                h = height;
            }
            if (width != 0) {
                w = width;
            }
            if (im != null) {
                var rect = new Rectangle2D.Double(x, y, w, h);
                texturePaint = new TexturePaint(im, rect);
            }
        }
    }
    
}