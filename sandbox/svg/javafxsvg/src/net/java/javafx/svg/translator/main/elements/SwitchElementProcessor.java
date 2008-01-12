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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SwitchElementProcessor extends BaseElementProcessor {
    public SwitchElementProcessor() {
        setOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                NodeList children = element.getChildNodes();
                int childCount = children.getLength();
                for (int i = 0; i < childCount; i++) {
                    Node child = children.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element subElement = (Element) child;
                        // FIXME: Switch may do nothing for unsupported element types
                        if (Utils.SVG_URI.equals(subElement.getNamespaceURI())) {
                            builder.processElement(subElement);
                        }
                    }
                }
            }
         });
    }
}
