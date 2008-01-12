/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import org.w3c.dom.Element;
import net.java.javafx.svg.translator.main.util.Utils;

public class PolylineElementProcessor extends DefaultElementProcessor {
    public PolylineElementProcessor() {
        super();
        setOnAttributes(new ElementHandler() {
                public void handleElement(Element element, Builder builder) throws Exception {
                    if (!element.hasAttribute("stroke")) {
                        element.setAttribute("stroke", "black");
                    }
                    Utils.processAttributes(element, builder);
                }
            });
        setOnElement(GeneratePointsElementHandler.getInstance());
    }
}
