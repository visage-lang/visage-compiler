/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main;

import java.util.HashMap;
import java.util.Map;

import net.java.javafx.svg.translator.ElementProcessor;
import net.java.javafx.svg.translator.main.elements.DefaultElementProcessor;
import net.java.javafx.svg.translator.main.elements.GradientElementProcessor;
import net.java.javafx.svg.translator.main.elements.GroupElementProcessor;
import net.java.javafx.svg.translator.main.elements.ImageElementProcessor;
import net.java.javafx.svg.translator.main.elements.ClipPathElementProcessor;
import net.java.javafx.svg.translator.main.elements.PathElementProcessor;
import net.java.javafx.svg.translator.main.elements.PatternElementProcessor;
import net.java.javafx.svg.translator.main.elements.PolygonElementProcessor;
import net.java.javafx.svg.translator.main.elements.PolylineElementProcessor;
import net.java.javafx.svg.translator.main.elements.SVGElementProcessor;
import net.java.javafx.svg.translator.main.elements.SwitchElementProcessor;
import net.java.javafx.svg.translator.main.elements.TextElementProcessor;
import net.java.javafx.svg.translator.main.elements.UseElementProcessor;
import net.java.javafx.svg.translator.main.elements.SymbolElementProcessor;


public class SVGElementProcessors {
    static public Map<String, ElementProcessor> processors = new HashMap<String, ElementProcessor>();
    
    public static Map<String, ElementProcessor> getProcessors() {
        return processors;
    }
    
    static {
        PathElementProcessor path = new PathElementProcessor();
        processors.put("circle", path);
        processors.put("ellipse", path);
        processors.put("clipPath", ClipPathElementProcessor.getInstance());
        processors.put("g", new GroupElementProcessor());
        processors.put("image", new ImageElementProcessor());
        processors.put("line", new PolylineElementProcessor());
        processors.put("linearGradient", GradientElementProcessor.getInstance());
        processors.put("pattern", new PatternElementProcessor());
        processors.put("path", path);
        processors.put("polygon", new PolygonElementProcessor());
        //processors.put("polyline", new PolylineElementProcessor());
        // map polyline to Polygon so it gets filled
        processors.put("polyline", new PolygonElementProcessor());
        processors.put("radialGradient", GradientElementProcessor.getInstance());
        processors.put("rect", path);
        processors.put("stop", DefaultElementProcessor.getInstance());
        processors.put("svg", new SVGElementProcessor());
        processors.put("switch", new SwitchElementProcessor());
        processors.put("text", new TextElementProcessor());
        processors.put("use", new UseElementProcessor());
        processors.put("symbol", SymbolElementProcessor.getInstance());
    }
}
