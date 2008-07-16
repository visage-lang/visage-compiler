package javafx.scene.geometry;

import java.awt.geom.GeneralPath;
import com.sun.javafx.scene.geometry.svgpath.parser.AWTPathProducer;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape; 
import java.io.StringReader;
import javafx.scene.geometry.*;
import java.lang.System;

// PENDING_DOC_REVIEW
/*
 * Shape/Path which is constructed by parsing SVG path data from a String.
 * 
 * @profile common
 */
public class SVGPath extends Shape {

    private attribute path2D: GeneralPath;
    
    function impl_createSGNode():SGNode { new SGShape() }
    function getSGShape():SGShape { impl_getSGNode() as SGShape }

    
    // PENDING_DOC_REVIEW
    /** 
     * Fill rule.
     */
    public attribute fillRule:FillRule = FillRule.NON_ZERO on replace { 
        updateSVGPath();
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Path defining SVG Path encoded string as specigied at 
     * {@link http://www.w3.org/TR/SVG/paths.html}
     *
     * @profile common
     */
    public attribute content: String on replace {
        updateSVGPath();
    }
         
    private function updateSVGPath():Void {
        if (content != null) {
            path2D = AWTPathProducer.createPath(new StringReader(content), fillRule.getToolkitValue());        
            getSGShape().setShape(path2D);
        } else {
            getSGShape().setShape(null);
        }
    }
}
