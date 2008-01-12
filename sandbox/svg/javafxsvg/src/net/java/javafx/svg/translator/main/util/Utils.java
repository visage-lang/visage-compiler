/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.java.javafx.svg.translator.Builder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import java.util.LinkedList;
import java.util.Iterator;


public class Utils {
    public static final String SVG_URI = "http://www.w3.org/2000/svg";

    public static final String XLINK_URI = "http://www.w3.org/1999/xlink";
    public static final String XLINK_HREF = "href";
    
    public static String camelize(String qName) {
        StringBuffer buffer = new StringBuffer();
        String[] segments = qName.split("-");
        buffer.append(segments[0]);
        for (int i = 1; i < segments.length; i++) {
            buffer.append(segments[i].substring(0, 1).toUpperCase());
            buffer.append(segments[i].substring(1));
        }
        return buffer.toString();
    }

    public static String capitalize(String sourceName) {
        sourceName = camelize(sourceName);
        String targetName = sourceName.substring(0, 1).toUpperCase();
        if (sourceName.length() > 1) {
            targetName = targetName.concat(sourceName.substring(1));
        }
        return targetName;
    }
   
    public static Element cloneElement(Element source) {
        Document document = source.getOwnerDocument();
        Element target = document.createElementNS(source.getNamespaceURI(), source.getLocalName());
        NamedNodeMap attrs = source.getAttributes();
        int attrCount = attrs.getLength();
        for (int i = 0; i < attrCount; i++) {
            Attr attr = (Attr) attrs.item(i);
            if (attr.getName() != "id") {
                Attr newAttr = document.createAttribute(attr.getName());
                newAttr.setValue(attr.getValue());
                target.setAttributeNode(newAttr);
            }
        }
        NodeList children = source.getChildNodes();
        int childCount = children.getLength(); 
        for (int i = 0; i < childCount; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) child;
                target.appendChild(cloneElement(subElement));
            }
            if (child.getNodeType() == Node.TEXT_NODE) {
            	Text text = (Text) child;
            	target.appendChild(document.createTextNode(text.getTextContent()));
            }
        }
        return target;
    }

    public static void colorAttributeWithAlpha(Attr attr, Builder builder, float alpha) throws Exception {
        if (!"none".equals(attr.getValue())) {
            String value = attr.getValue();
            String alphaStr = "0x" + String.format("%x", Float.valueOf((alpha * 255)).intValue());
            if (value.startsWith("#")) {
                if (value.length() == 4) {
                    value = "#" +
                            value.charAt(1) +
                            value.charAt(1) +
                            value.charAt(2) +
                            value.charAt(2) +
                            value.charAt(3) +
                            value.charAt(3);
                }
                String red = value.substring(1, 3).toLowerCase();
                String green = value.substring(3, 5).toLowerCase();
                String blue = value.substring(5, 7).toLowerCase();
                if (red.equals("00") &&
                    green.equals("00") &&
                    blue.equals("00") &&
                    alphaStr.equals("0xff")) {
                    builder.append(camelize(attr.getName()) + ": Color.BLACK");
                } else if (red.equals("ff") &&
                           green.equals("ff") &&
                           blue.equals("ff") &&
                           alphaStr.equals("0xff")) {
                    builder.append(camelize(attr.getName()) + ": Color.WHITE");
                } else {

                    builder.append(camelize(attr.getName()) + ": " +
                                   "Color.rgba(0x" + red +
                                   ", 0x" + green +
                                   ", 0x" + blue +
                                   ", " + alphaStr + ")");
                }
            } else if (value.length() > 3 && "rgb".equals(value.substring(0, 3))) {
                String rgb = value.substring(4, value.length() - 1);
                String[] st = rgb.split("\\s*,\\s*");
                String red = st[0];
                String green = st[1];
                String blue = st[2];
                builder.append(camelize(attr.getName()) + ":" +
                        "Color.rgba(" + red + "" +
                        ", " + green + 
                        ", " + blue +
                        ", " + alphaStr + ")");
            } else if (value.length() > 5 && "url(#".equals(value.substring(0, 5))) {
                builder.append(camelize(attr.getName()) +
                        ": " +
                            builder.printId(value.substring(5, value.length() - 1)) +
                        "()");
            } else {
                builder.append(camelize(attr.getName()) + ": Color." + value.toUpperCase());
            }
        }
    }
    
    public static void copyAttributes(Element source, Element target, boolean force) {
        for (Attr attr: attributes(source)) {
            if (!"id".equals(attr.getName())) {
                if (target.hasAttribute(attr.getName())) {
                    if (force) {
                        target.setAttribute(attr.getName(), attr.getValue());
                    }
                } else {
                    target.setAttribute(attr.getName(), attr.getValue());
                }
            }
        }
    }
    
    public static void copyAttributes(Element source, Element target, boolean force, String[] exceptions) {
        for (Attr attr: attributes(source)) {
            if (!"id".equals(attr.getName())) {
            	boolean doSet = true;
                for (String exception: exceptions) {
                    if (attr.getName().equals(exception)) {
                        doSet = false;
                        break;
                    }
                }
                if (doSet) {
                    if (target.hasAttribute(attr.getName())) {
                        if (force) {
                            target.setAttribute(attr.getName(), attr.getValue());
                        }
                    } else {
                        target.setAttribute(attr.getName(), attr.getValue());
                    }
                }
            }
        }
    }
    
    public static void copyElements(Element source, Element target) {
        for (Element subElement: subElements(source)) {
            Element clone = cloneElement(subElement);
            target.appendChild(clone);
        }
    }

    public static List<Element> subElements(Element element) {
    	return subElements(element, null);
    }
    
    public static List<Element> subElements(Element element, String[] exceptions) {
        List<Element> list = new ArrayList<Element>();
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();
        for (int i = 0; i < childCount; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) child;

                boolean add = true;
                if (exceptions != null) {
                	for (String exception: exceptions) {
                		if (subElement.getLocalName().equals(exception)) {
                			add = false;
                			break;
                		}
                	}
                }
                if (add) {
                    list.add(subElement);
                }
            }
        }
        return list;
    }
    
    public static List<Attr> attributes(Element element) {
        List<Attr> list = new ArrayList<Attr>();
        NamedNodeMap attrs = element.getAttributes();
        int attrCount = attrs.getLength();
        for (int i = 0; i < attrCount; i++) {
            Attr attr = (Attr) attrs.item(i);
            list.add(attr);
        }
        return list;
    }
    
    public static void copyAttributes(Element source, boolean force, String[] exceptions) {
        List<Element> targets = subElements(source);
        for (Attr attr: attributes(source)) {
            if (!"id".equals(attr.getName())) {
            	boolean doSet = true;
                for (String exception: exceptions) {
                    if (attr.getName().equals(exception)) {
                        doSet = false;
                        break;
                    }
                }
                if (doSet) {
                    for (Element target: targets) {
                        if (target.hasAttribute(attr.getName())) {
                            if (force) {
                                target.setAttribute(attr.getName(), attr.getValue());
                            }
                        } else {
                            target.setAttribute(attr.getName(), attr.getValue());
                        }
                    }
                }
            }
        }
    }
    
    public static void copyAttributes(Element source, boolean force, String[] elementExceptions, String[] attributeExceptions) {
        List<Element> targets = subElements(source, elementExceptions);
        for (Attr attr: attributes(source)) {
            if (!"id".equals(attr.getName())) {
            	boolean doSet = true;
            	if (attributeExceptions != null) {
                    for (String attributeException: attributeExceptions) {
                        if (attr.getName().equals(attributeException)) {
                            doSet = false;
                            break;
                        }
                    }
            	}
            	if (doSet) {
                    for (Element target: targets) {
                        if (target.hasAttribute(attr.getName())) {
                            if (force) {
                                target.setAttribute(attr.getName(), attr.getValue());
                            }
                        } else {
                            target.setAttribute(attr.getName(), attr.getValue());
                        }
                    }
            	}
            }
        }
    }
    
    public static String escapeQuotes(String text) {
        text = text.replace("'", "\\'");
        return text;
    }

    public static String getAttribute(Element element, String name, String defaultValue) {
        String value = element.getAttribute(name);
        if ("".equals(value)) {
            return defaultValue;
        }
        return value;
    }
    
    public static String getLengthRepresentation(Element element, String attrName) {
        String value = element.getAttribute(attrName);
        Length length = Length.parseLength(value);
        String methodName = Length.methodName(length.getUnit());
        if (methodName == null) {
            value = Float.valueOf(length.getValue()).toString();
        } else {
            value = methodName + "(" + length.getValue() + ")";
        }
        return value;
    }

    public static boolean hasSubElements(Element element) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();
        for (int i = 0; i < childCount; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }
    
    public static void inheritElement(Element element, Map<String, Element> idElements) {
        if (!element.hasAttributeNS("<<inherit>>", "visited") &&
             element.hasAttributeNS(XLINK_URI, XLINK_HREF))
        {
            String referencedId = element.getAttributeNS(XLINK_URI, XLINK_HREF).substring(1);
            Element referencedElement = idElements.get(referencedId);

            inheritElement(referencedElement, idElements);
            
            NamedNodeMap attrs = referencedElement.getAttributes();
            int attrCount = attrs.getLength();
            for (int i = 0; i < attrCount; i++) {
                Attr attr = (Attr) attrs.item(i);
                if (!(attr == null || element.hasAttribute(attr.getName()))) {
                    Attr newAttr = element.getOwnerDocument().createAttribute(attr.getName());
                    newAttr.setValue(attr.getValue());
                    element.setAttributeNode(newAttr);
                }
            }
            
            if (!Utils.hasSubElements(element)) {
                NodeList children = referencedElement.getChildNodes();
                int childCount = children.getLength();
                for (int i = 0; i < childCount; i++) {
                    Node child = children.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        element.appendChild(Utils.cloneElement((Element) child));
                    }
                }
            }
        }
                
        element.setAttributeNS("<<inherit>>", "visited", "true");
    }

    public static Attr newAttr(String name, Attr attr) {
        Attr newAttr = attr.getOwnerDocument().createAttribute(name);
        newAttr.setValue(attr.getValue());
        return newAttr;
    }
    
    public static List<NamedValue> parseStyle(String styleSpec) {
        List<NamedValue> list = new ArrayList<NamedValue>();
        String[] rules = styleSpec.split("\\s*;\\s*");
        for (int i = 0; i < rules.length; i++) {
            String[] pair = rules[i].split("\\s*:\\s*");
            if (pair.length >= 2) {
                NamedValue namedValue = new NamedValue(pair[0].trim(), pair[1].trim());
                list.add(namedValue);
            }
        }
        return list;
    }
    
    public static void processAttributes(Element element, Builder builder) throws Exception {
        List<Attr> attrs = attributes(element);
        for (Attr attr: attrs) {
            builder.processAttribute(attr);
        }
    }

    public static void processSubElements(Element element, Builder builder) throws Exception {
        for (Element subElement: subElements(element)) {
            builder.processElement(subElement);
        }
    }


    public static void filter(Element elm, String[] expectedAttrs) {
        List toBeRemoved = new LinkedList();
        NamedNodeMap attrs = elm.getAttributes();
        for(int i = 0, len = attrs.getLength(); i < len; i++) {
            Attr attr = (Attr)attrs.item(i);
            String localName = attr.getLocalName();
            boolean expected = false;
            for (int j = 0; j < expectedAttrs.length; j++) {
                if (expectedAttrs[j].equals(localName)) {
                    expected = true;
                    break;
                }
            }
            if (!expected) {
                toBeRemoved.add(attr);
            }
        }
        Iterator iter = toBeRemoved.iterator();
        while (iter.hasNext()) {
            Attr attr = (Attr)iter.next();
            elm.removeAttributeNode(attr);
        }
    }
}
