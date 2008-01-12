/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import net.java.javafx.svg.translator.BaseElementProcessor;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Element;


public class GroupElementProcessor extends BaseElementProcessor {
    public GroupElementProcessor() {
        setBeforeElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                builder.append("Group {");
            }
        });
        
        setOnAttributes(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                if (element.hasAttribute("transform")) {
                    builder.processAttribute(element.getAttributeNode("transform"));
                }
                if (element.hasAttribute("display")) {
                    builder.processAttribute(element.getAttributeNode("display"));
                }
            }
        });
        
        setOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                builder.increaseIndentation();
                builder.append("content: [");
                Utils.copyAttributes(element,
                		             false,
                		             new String[] {"linearGradient", "radialGradient"},
	             					 new String[] {"transform"});
                Utils.processSubElements(element, builder);
            }
        });
        
        setAfterElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                builder.append("]");
                builder.decreaseIndentation();
                builder.append("},");
            }
        });
    }
}
