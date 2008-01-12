/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.elements;

import static net.java.javafx.svg.translator.main.util.Utils.XLINK_HREF;
import static net.java.javafx.svg.translator.main.util.Utils.XLINK_URI;

import java.util.Map;

import net.java.javafx.svg.translator.BaseElementProcessor;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


public class UseElementProcessor extends BaseElementProcessor {
    public UseElementProcessor() {
        setBeforeElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                /*
                builder.append("Group {");
                String x = "0";
                String y = "0";
                String w = "0";
                String h = "0";
                if (element.hasAttribute("x")) {
                    x = element.getAttribute("x");
                }
                if (element.hasAttribute("y")) {
                    y = element.getAttribute("y");
                }
                if (element.hasAttribute("w")) {
                    w = element.getAttribute("width");
                }
                if (element.hasAttribute("h")) {
                    h = element.getAttribute("height");
                }
                builder.append("transform: [");
                if (x != "0" || y != "0") {
                    builder.append("Translate { x: " + x + ", y: " + y +  "},");
                }
                if (w != "0" || h != "0") {
                    builder.append("Scale { x: " + w + ", y: " + h + "},");
                }
                builder.append("]");
                builder.append("content: [");
                */
            }
        });

        setOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                String useId = element.getAttributeNS(XLINK_URI, XLINK_HREF);
                if (!"".equals(useId)) {
                    useId = useId.substring(1);
                    Map<String, Element> idElements = (Map<String, Element>) builder.getStore().get("idElements");
                    Element prototypeElement = idElements.get(useId);
                    builder.append("// use: " + useId);
                    if (false) {
                        Element newElement = Utils.cloneElement(prototypeElement);
                        
                        // FIXME <use> cannot propagate attributes unconditionally, only those that apply
                        // FIXME <use> attr propagation should take place at preprocessing time and using the DOM
                        NamedNodeMap attrs = element.getAttributes();
                        Document document = element.getOwnerDocument();
                        int attrCount = attrs.getLength();
                        for (int i = 0; i < attrCount; i++) {
                            Attr attr = (Attr) attrs.item(i);
                            if (!(
                                  "x".equals(attr.getName()) ||
                                  "y".equals(attr.getName()) ||
                                  "width".equals(attr.getName()) ||
                                  "height".equals(attr.getName()) ||
                                  attr.getName().indexOf(':') >= 0 ||
                                  newElement.hasAttribute(attr.getName())
                                  ))
                                {
                                    Attr newAttr = document.createAttribute(attr.getName());
                                    newAttr.setValue(attr.getValue());
                                    newElement.setAttributeNode(newAttr);
                                }
                        }
                    } else {
                        String x = element.getAttribute("x");
                        String y = element.getAttribute("y");
                        if ("".equals(x)) x = "0";
                        if ("".equals(y)) y = "0";
                        String height = element.getAttribute("height");
                        String width = element.getAttribute("width");
                        builder.append("Group {");
                        Element source = element;
                        Document document = source.getOwnerDocument();
                        Element target = document.createElementNS(source.getNamespaceURI(), source.getLocalName());
                        NamedNodeMap attrs = source.getAttributes();
                        int attrCount = attrs.getLength();
                        for (int i = 0; i < attrCount; i++) {
                            Attr attr = (Attr) attrs.item(i);
                            if (!attr.getName().equals("id") && !attr.getName().equals("x") && !attr.getName().equals("y") && !attr.getName().equals("width") && !attr.getName().equals("height")) {
                                Attr newAttr = document.createAttribute(attr.getName());
                                newAttr.setValue(attr.getValue());
                                target.setAttributeNode(newAttr);
                            }
                        }
                        Element newElement = target;
                        builder.append("content: Group {");
			if (newElement.hasAttribute("transform")) {
			    builder.processAttribute(newElement.getAttributeNode("transform"));
			}
			if (newElement.hasAttribute("display")) {
			    builder.processAttribute(element.getAttributeNode("display"));
			}
                        builder.append("content: Group {");
                        builder.append("transform: translate("+x+","+y+")");
                        builder.append("var g = Group {");
                        builder.append("content: "+builder.printId(useId)+"()}");
                        builder.append("content: g");
                        if (!"".equals(height) && !"".equals(width)) {
                            builder.append("transform: bind scale("+width+"/g.currentWidth,"+height+"/g.currentHeight)");
                        }
                        builder.append("}}},");
                    }
                }
            }
        });
        
        setAfterElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                //builder.append("]},");n
            }
        });
    }
}
