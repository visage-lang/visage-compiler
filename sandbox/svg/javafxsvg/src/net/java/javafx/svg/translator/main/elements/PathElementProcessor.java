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


public class PathElementProcessor extends DefaultElementProcessor {
    public PathElementProcessor() {
        super();
        setBeforeElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                builder.append("StringPath {");
            }
        });
        setBeforeProcessing(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
		element.removeAttribute("x");
		element.removeAttribute("y");
                if (!element.hasAttribute("fill")) {
                    element.setAttribute("fill", "black");
                }
            }
        });
    }
}
