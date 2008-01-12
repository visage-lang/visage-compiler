/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import net.java.javafx.svg.translator.main.util.Utils;
import java.util.*;
import org.w3c.dom.*;



public class GradientElementProcessor extends DefaultElementProcessor {
    private static GradientElementProcessor instance = new GradientElementProcessor();
    public static GradientElementProcessor getInstance() {
        return instance;
    }

    static final String[] radialOnly = new String[] {
        "cx", "cy", "r", "fx", "fy", "stops", "gradientTransform", "spreadMethod", "gradientUnits"
    };
    static final String[] linearOnly = new String[] {
        "x1", "y1", "x2", "y2", "stops", "gradientTransform", "spreadMethod", "gradientUnits"
    };


    public void onAttributes(Element element, Builder builder) throws Exception {
        if (element.getLocalName().equals("linearGradient")) {
            Utils.filter(element, linearOnly);
        } else if (element.getLocalName().equals("radialGradient")) {
            Utils.filter(element, radialOnly);
        }
        super.onAttributes(element, builder);
    }

    private GradientElementProcessor() {
        setGenerable(false);
        setOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                builder.increaseIndentation();
                builder.append("stops: [");
                Utils.processSubElements(element, builder);
                builder.append("]");
                builder.decreaseIndentation();
            }
        });
    }
}
