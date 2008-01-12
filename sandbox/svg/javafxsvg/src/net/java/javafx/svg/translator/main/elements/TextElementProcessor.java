/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import java.text.DecimalFormat;

import net.java.javafx.svg.translator.BaseElementProcessor;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class TextElementProcessor extends BaseElementProcessor {
    public TextElementProcessor() {
        setOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                NodeList children = element.getChildNodes();
                int childCount = children.getLength();
                for (int i = 0; i < childCount; i++) {
                    Node child = children.item(i);
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        Text text = (Text) child;
                        String content = Utils.escapeQuotes(text.getTextContent().trim());
                        if (content.length() > 0) {
                            builder.append("Text {");
                            builder.increaseIndentation();
			    builder.append("verticalAlignment: BASELINE");
                            builder.append("content: '" + content + "'");
                            generateFont(element, builder);
                            Utils.processAttributes(element, builder);
                            builder.decreaseIndentation();
                            builder.append("},");
                        }
                    }
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element subElement = (Element) child;
                        if ("tspan".equals(subElement.getLocalName())) {
                            String content = Utils.escapeQuotes(subElement.getTextContent().trim());
                            if (content.length() > 0) {
                                builder.append("Text {");
                                builder.increaseIndentation();
				builder.append("verticalAlignment: BASELINE");
                                builder.append("content: '" + content + "'"); 
                                generateFont(element, builder);
                                Utils.processAttributes(subElement, builder);
                                builder.decreaseIndentation();
                                builder.append("},");
                            }
                        }
                        if ("tref".equals(subElement.getLocalName())) {
                            String id = subElement.getAttributeNS(Utils.XLINK_URI, Utils.XLINK_HREF);
                            id = id.substring(1);
                            Element referencedElement = element.getOwnerDocument().getElementById(id);
                            Element newElement = Utils.cloneElement(referencedElement);
                            Utils.copyAttributes(element, newElement, false);
                            builder.processElement(newElement);
                        }
                    }
                }
            }
        });
    }

    private static void generateFont(Element element, Builder builder) throws Exception {
        String fontFamily = null;
        String fontStyle = null;
        String fontWeight = null;
        float  fontSize = 0f;
        
        if (element.hasAttribute("font-family")) {
            fontFamily = Utils.escapeQuotes(element.getAttribute("font-family"));
        }
        if (fontFamily != null) {
            if (element.hasAttribute("font-style")) {
                fontStyle = element.getAttribute("font-style");
            }
            if (element.hasAttribute("font-weight")) {
                fontWeight = element.getAttribute("font-weight");
            }
            if (element.hasAttribute("font-size")) {
                DecimalFormat fmt = new DecimalFormat("#");
                fontSize = fmt.parse(element.getAttribute("font-size")).floatValue();
            }
            
            if (fontStyle == null && fontWeight == null && fontSize == 0d){
                builder.append("font: Font {faceName:'" + fontFamily + "'}");
            } else {
                StringBuffer style = new StringBuffer();
                style.append("[");
                String sep = "";
                if (fontStyle == "italic") {
                    style.append(sep);
                    sep = ", ";
                    style.append("ITALIC");
                }
                if (fontWeight == "bold") {
                    style.append(sep);
                    sep = ", ";
                    style.append("BOLD");
                }
                if (style.length() == 0) {
                    style.append(sep);
                    sep = ", ";
                    style.append("PLAIN");
                }
                style.append("]");
                String styleString = style.toString();
                if (styleString.equals("[]")) {
                    styleString = "PLAIN";
                }
                builder.append("font: Font {faceName: '" + fontFamily + "', style: " + styleString + ", size: " + fontSize + "}");
            }
        }
    }
}
