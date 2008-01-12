/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.ElementHandler;
import net.java.javafx.svg.translator.main.util.NamedValue;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SVGBuilder {
    private Builder builder = new Builder();
    
    public SVGBuilder(int level, String indentation, String namespaceURI) {
        builder.setLevel(level);
        builder.setIndentation(indentation);
        builder.setNamespaceURI(namespaceURI);

        builder.setAttributeHandlers(SVGAttributeHandlers.getHandlers());
        builder.setElementProcessors(SVGElementProcessors.getProcessors());

        // Collect elements with an 'id' attribute
        final Map<String, Element> idElements = new LinkedHashMap<String, Element>();
        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
           public void handleElement(Element element, Builder builder) {
               if (element.hasAttribute("id")) {
		   if (idElements.get(element.getAttribute("id")) == null) {
		       idElements.put(element.getAttribute("id"), element);
		   }
		   else {
		       System.out.println("Warning duplicate id : "+element.getAttribute("id"));
		   }
               }
           }
        });
        
        // Recursively inherit gradient attributes and subelements from parent via 'href'
        final List<Element> gradientElements = new ArrayList<Element>(); 
        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
           public void handleElement(Element element, Builder builder) {
               if ("linearGradient".equals(element.getLocalName()) ||
                   "radialGradient".equals(element.getLocalName()))
               {
                   gradientElements.add(element);
               }
           }
        });
        builder.getElementPreprocessor().addAfterPreprocessing(new ElementHandler() {
            public void handleElement(Element root, Builder builder) throws Exception {
                for (Element element: gradientElements) {
                    Utils.inheritElement(element, idElements);
                }
            }
        });

        // Expand 'class' style attributes prior to individual element processing
        // 'style' overrides attributes, 'class' overrides 'style'
        final Map<String, List<NamedValue>> cssRules = new HashMap<String, List<NamedValue>>();
        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if ("style".equals(element.getLocalName())) {
                    String cssRegex = "\\s*(\\.[A-Za-z_][A-Za-z_0-9]*)\\s*\\{\\s*([^}]+)\\s*\\}\\s*";
                    Pattern cssPattern = Pattern.compile(cssRegex);
                    Matcher matcher = cssPattern.matcher(element.getTextContent());
                    while (matcher.find()) {
                        String selector = matcher.group(1).substring(1);
                        String style = matcher.group(2);
                        List<NamedValue> attrs = Utils.parseStyle(style);
                        cssRules.put(selector, attrs);
                    }
                }
            }
         });
        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if (element.hasAttribute("style")) {
                    List<NamedValue> styles = Utils.parseStyle(element.getAttribute("style"));
                    for (NamedValue namedValue: styles) {
                        element.setAttribute(namedValue.getName(), namedValue.getValue());
                    }
                }
            }
         });
        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if (element.hasAttribute("class")) {
                    String className = element.getAttribute("class");
                    
                    List<NamedValue> cssProperties = cssRules.get(className);
                    if (cssProperties != null) {
                        for (NamedValue namedValue: cssProperties) {
                            element.setAttribute(namedValue.getName(), namedValue.getValue());
                        }
                    }
                }
            }
         });

        // FIXME <g> cannot propagate attributes unconditionally; only those that apply
        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if ("svg".equals(element.getLocalName()) || "g".equals(element.getLocalName())) {
                    Utils.copyAttributes(element,
                    		             false,
                    		             new String[] { // Element exceptions
                    						"linearGradient",
                    						"radialGradient",
                    					 },
                       		             new String[] { // Attribute exceptions
                            				"transform",
                            				"height",
                            				"width",
                            			 });
                }
            }
         });

        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if ("text".equals(element.getLocalName())) {
                    NamedNodeMap attrs = element.getAttributes();
                    int attrCount = attrs.getLength();

                    NodeList children = element.getChildNodes();
                    int childCount = children.getLength();
                    for (int i = 0; i < childCount; i++) {
                        Node child = children.item(i);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            Element subElement = (Element) child;
                            for (int j = 0; j < attrCount; j++) {
                                Attr attr = (Attr) attrs.item(j);
                                if (!"id".equals(attr.getName()) &&
                                	!"transform".equals(attr.getName()) &&
                                    "tspan".equals(subElement.getLocalName()) &&
                                    !subElement.hasAttribute(attr.getName()))
                                {
                                    subElement.setAttribute(attr.getName(),
                                    		                attr.getValue());
                                }
                            }
                        }
                    }
                }
            }
         });

        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if ("svg".equals(element.getLocalName())) {
                	if (element.hasAttribute("width")) {
                		builder.getStore().put("width", Utils.getLengthRepresentation(element, "width"));
                	}
                	if (element.hasAttribute("height")) {
                		builder.getStore().put("height", Utils.getLengthRepresentation(element, "height"));
                	}
                }
            }
         });

        builder.getElementPreprocessor().addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) {
                if ("title".equals(element.getLocalName())) {
            		builder.getStore().put("title", Utils.escapeQuotes(element.getTextContent().trim()));
                }
            }
         });

        // Generate functions for all elements with and 'id' attribute
        builder.getElementPreprocessor().addAfterPreprocessing(new ElementHandler() {
            public void handleElement(Element root, Builder builder) throws Exception {
                builder.getStore().put("idElements", idElements);
		String fileName = (String)builder.getStore().get("FILE_NAME");
		String funPrefix = fileName == null ?
		    "" : ""+builder.printId(fileName)+".";
		if (fileName != null) {
		    builder.decreaseIndentation();
		    builder.append("\npublic class "+builder.printId(fileName)+" extends CompositeNode {");
		    builder.increaseIndentation();
                    if (false) {
                        for (String id: idElements.keySet()) {
                            String className = "Node";
                            Element element = idElements.get(id);
                            if (element.getLocalName().equals("radialGradient")) {
                                className = "RadialGradient";
                            } else if (element.getLocalName().equals("linearGradient")) {
                                className = "LinearGradient";
                            } else if (element.getLocalName().equals("stop")) {
                                className = "Stop";
                            } else if (element.getLocalName().equals("pattern")) {
                                className = "Pattern";
                            } else if (element.getLocalName().equals("path")) {
                                className = "Path";
                            } else if (element.getLocalName().equals("rect")) {
                                className = "Rect";
                            } else if (element.getLocalName().equals("circle")) {
                                className = "Circle";
                            } else if (element.getLocalName().equals("ellipse")) {
                                className = "Ellipse";
                            } else if (element.getLocalName().equals("image")) {
                                className = "ImageView";
                            } else if (element.getLocalName().equals("line")) {
                                className = "Line";
                            } else if (element.getLocalName().equals("polygon")) {
                                className = "Polygon";
                            } else if (element.getLocalName().equals("polyline")) {
                                //className = "Polyline";
                                className = "Polygon";
                            } else if (element.getLocalName().equals("text")) {
                                className = "Text";
                            }
                            builder.append("public function "+builder.printId(id)+"(): "+className+";");
                        }
                        builder.decreaseIndentation();
                        builder.append("}");
                    }
		}
                for (String id: idElements.keySet()) {
                    Element element = idElements.get(id);
		    builder.increaseIndentation();
                    builder.append("\npublic function "+builder.printId(id)+ "() { ");
                    builder.processElement(element, true);
                    builder.append("; }");
		    builder.decreaseIndentation();
                }
		if (fileName != null) {
		    builder.increaseIndentation();
		    builder.append("public function composeNode():Node {");
		}
            }
        });
    }
    
    public Builder getBuilder() {
        return builder;
    }
}
