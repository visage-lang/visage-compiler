/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;

import org.apache.batik.parser.NumberListHandler;
import org.apache.batik.parser.NumberListParser;
import org.w3c.dom.Element;


public class GeneratePointsElementHandler implements ElementHandler {
    private static GeneratePointsElementHandler instance = new GeneratePointsElementHandler();
    
    public static GeneratePointsElementHandler getInstance() {
        return instance;
    }

    public void handleElement(Element element, final Builder builder) throws Exception {
        String points = element.getAttribute("points");
        if (points.length() == 0) {
            return;
        }
        builder.increaseIndentation();
        builder.append("points: [");
        NumberListParser parser = new NumberListParser();
        parser.setNumberListHandler(new NumberListHandler() {
            public void startNumberList(){}
            public void startNumber(){}
                public void numberValue(float value) {
                    builder.append(value + ",");
                }
                public void endNumber(){}
                public void endNumberList(){}
        });
        parser.parse(points);            
        builder.append("]");
        builder.decreaseIndentation();
   }
}
