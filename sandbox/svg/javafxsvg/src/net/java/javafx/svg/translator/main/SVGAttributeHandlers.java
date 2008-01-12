/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.*;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.main.attributes.ArcRadiusAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.DAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.FillAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.FillRuleAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.FocusAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.GradientUnitsAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.DisplayAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.LengthAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.NamedAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.NamedLengthAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.OffsetAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.StopColorAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.StrokeAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.StrokeLinecapAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.StrokeLinejoinAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.TransformAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.UnquotedAttributeHandler;
import net.java.javafx.svg.translator.main.attributes.UppercaseAttributeHandler;


public class SVGAttributeHandlers {
    private static Map<String, AttributeHandler> handlers = new HashMap<String, AttributeHandler>();
    

    public static Map<String, AttributeHandler> getHandlers() {
        return handlers;
    }
    
    static {
            handlers.put("cx", LengthAttributeHandler.getInstance());
            handlers.put("cy", LengthAttributeHandler.getInstance());
            handlers.put("d", new DAttributeHandler());
            handlers.put("fill", new FillAttributeHandler());
            handlers.put("clip-path", new NamedAttributeHandler("clip") {
                    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        
                        String value = attr.getValue();
        
                        if (value.length() > 5 && "url(#".equals(value.substring(0, 5))) {
                            builder.append("clip: {shape: " +
                                           builder.printId(value.substring(5, value.length() - 1)) + "()}");                    
                            
                        }
                    }
                });
            handlers.put("fill-opacity", new NamedAttributeHandler("fillOpacity"));
            handlers.put("fill-rule", new FillRuleAttributeHandler());
            handlers.put("fx", new FocusAttributeHandler("focusX"));
            handlers.put("fy", new FocusAttributeHandler("focusY"));
            handlers.put("gradientTransform", TransformAttributeHandler.getInstance());
            handlers.put("gradientUnits", new GradientUnitsAttributeHandler());
            handlers.put("display", new DisplayAttributeHandler());
            handlers.put("height", LengthAttributeHandler.getInstance());
            handlers.put("offset", new OffsetAttributeHandler());
            handlers.put("opacity", UnquotedAttributeHandler.getInstance());
            handlers.put("r", new NamedLengthAttributeHandler("radius"));
            handlers.put("rx", new ArcRadiusAttributeHandler("arcWidth", "radiusX"));
            handlers.put("ry", new ArcRadiusAttributeHandler("arcHeight", "radiusY"));
            handlers.put("spreadMethod", new UppercaseAttributeHandler("SpreadMethod"));
            handlers.put("stop-color", new StopColorAttributeHandler());
            handlers.put("stroke", new StrokeAttributeHandler());
            handlers.put("stroke-linecap", new StrokeLinecapAttributeHandler());
            handlers.put("stroke-linejoin", new StrokeLinejoinAttributeHandler());
            handlers.put("stroke-width", LengthAttributeHandler.getInstance());
            handlers.put("transform", TransformAttributeHandler.getInstance());
            handlers.put("x", LengthAttributeHandler.getInstance());
            handlers.put("x1", new LengthAttributeHandler("startX"));
            handlers.put("x2", new LengthAttributeHandler("endX"));
            handlers.put("y", LengthAttributeHandler.getInstance());
            handlers.put("y1", new LengthAttributeHandler("startY"));
            handlers.put("y2", new LengthAttributeHandler("endY"));
            handlers.put("width", LengthAttributeHandler.getInstance());
    }
}
