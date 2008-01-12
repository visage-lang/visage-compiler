/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

    package net.java.javafx.svg.translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Writer;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.resolver.tools.CatalogResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class Builder {
    //private static final String lineSeparator = System.getProperty("line.separator");
    
    // Configuration properties
    private Map<String, ElementProcessor> elementProcessors;
    private Map<String, AttributeHandler> attributeHandlers;
    private int level = 0;
    private String indentation = "    ";
    private String namespaceURI = "";
    
    // Read-only properties
    private Map<String, Object> store = new HashMap<String, Object>();
    private ElementPreprocessor elementPreprocessor = new ElementPreprocessor();
    
    public Builder()  {
        elementPreprocessor.addOnElement(new ElementHandler() {
            public void handleElement(Element element, Builder builder) throws Exception {
                ElementProcessor processor = elementProcessors.get(element.getLocalName());
                if (processor != null) {
                    processor.beforeProcessing(element, builder);
                }
            }
        });
    }
    
    // Private properties
    private Appendable writer;
    
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    {
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
    }
    CatalogResolver catalogResolver = new CatalogResolver();

    // Public methods
    public void build(InputSource inputSource,
                      Appendable writer,
                      ContentProvider provider)
        throws Exception
    {
        setWriter(writer);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(catalogResolver);
        Document document = db.parse(inputSource);
        
        Element root = document.getDocumentElement();

        writer.append(provider.getContentBeforePreprocessing(store));
        elementPreprocessor.preprocess(root);
    	
        writer.append(provider.getContentBeforeProcessing(store));
        processElement(root);
        writer.append(provider.getContentAfterProcessing(store));
    }

    public void processElement(Element element) throws Exception {
        processElement(element, false);
    }
    public void processElement(Element element, boolean force) throws Exception {
        if (namespaceURI.equals(element.getNamespaceURI())) {
            ElementProcessor elementProcessor = elementProcessors.get(element.getLocalName());
            if (elementProcessor != null && (force || elementProcessor.isGenerable())) {
                increaseIndentation();
                elementProcessor.beforeElement(element, this);
    
                increaseIndentation();
                elementProcessor.onAttributes(element, this);
                decreaseIndentation();
    
                elementProcessor.onElement(element, this);
    
                elementProcessor.afterElement(element, this);
                decreaseIndentation();
            }
        }
    }
    
    public void processAttribute(Attr attr) throws Exception {
        // NOTE No namespace support in attribute processing
        AttributeHandler handler = (AttributeHandler) attributeHandlers.get(attr.getName());
        if (handler != null) {
            handler.handleAttribute(attr, this);
        } else if ("".equals(attr.getNamespaceURI())) {
            append("/* " + attr.getName() + ": " + attr.getValue() + " */");
        }
    }

    boolean comma = false;

    void flushComma() throws IOException {
        if (comma) {
            comma = false;
            writer.append(",\n");
        } 
    }

    public void append(String text) {
        try {
            if (text != null) {
                if (text.startsWith("]") || text.startsWith("}") ||
                    text.startsWith(";")) {
                    comma = false;
                } else {
                    flushComma();
                }
                int len = text.length();
                if (text.endsWith(",")) {
                    comma = true;
                    len--;
                }
                for (int i = 0; i < level; i++) {
                    writer.append(indentation);
                }
                writer.append(text, 0, len);
                if (!comma) {
                    writer.append("\n");
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Error while appending in builder", ioe);
        }
    }
    
    public void increaseIndentation() {
        level++;
    }
    
    public void decreaseIndentation() {
        if (level > 0) {
            level--;
        }
    }
    
    // Inner classes
    public class ElementPreprocessor {
        private List<ElementHandler> beforeProprocessing = new ArrayList<ElementHandler>();
        private List<ElementHandler> onElement = new ArrayList<ElementHandler>();
        private List<ElementHandler> afterPreprocessing = new ArrayList<ElementHandler>();

        public void preprocess(Element root) throws Exception {
            for (ElementHandler handler: beforeProprocessing) {
                handler.handleElement(root, Builder.this);
            }
            
            for (ElementHandler handler: onElement) {
                handler.handleElement(root, Builder.this);
            }
            NodeList elements = root.getElementsByTagNameNS(namespaceURI, "*");
            int elementCount = elements.getLength();
            for (int i = 0; i < elementCount; i++) {
                Element element = (Element) elements.item(i);
                for (ElementHandler handler: onElement) {
                    handler.handleElement(element, Builder.this);
                }
            }

            for (ElementHandler handler: afterPreprocessing) {
                handler.handleElement(root, Builder.this);
            }
        }
        
        public void addBeforeProprocessing(ElementHandler elementHandler) {
            beforeProprocessing.add(elementHandler);
        }
        
        public void addOnElement(ElementHandler elementHandler) {
            onElement.add(elementHandler);
        }
        
        public void addAfterPreprocessing(ElementHandler elementHandler) {
            afterPreprocessing.add(elementHandler);
        }
    }

    // Property accessors
    public Map<String, AttributeHandler> getAttributeHandlers() {
        return attributeHandlers;
    }

    public void setAttributeHandlers(Map<String, AttributeHandler> attributeHandlers) {
        this.attributeHandlers = attributeHandlers;
    }

    public Map<String, ElementProcessor> getElementProcessors() {
        return elementProcessors;
    }

    public void setElementProcessors(Map<String, ElementProcessor> elementProcessors) {
        this.elementProcessors = elementProcessors;
    }

    public String getIndentation() {
        return indentation;
    }

    public void setIndentation(String indentation) {
        this.indentation = indentation;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public Map<String, Object> getStore() {
        return store;
    }

    public ElementPreprocessor getElementPreprocessor() {
        return elementPreprocessor;
    }

    public Appendable getWriter() {
        return writer;
    }

    public void setWriter(Appendable output) {
        this.writer = output;
    }

    static void printEscape(Writer writer, char[] ch) {
	try {
	    writer.write("<<");
	    for (int i = 0; i < ch.length; i++) {
		if (ch[i] == '\\' || ch[i] == '>') {
		    writer.write('\\');
		}
		writer.write(ch[i]);
	    }
	    writer.write(">>");
	} catch (IOException ioe) {
	    throw new RuntimeException(ioe);
	}
    }

    public static String printId(String ident) {
	return printId(ident, false);
    }

    public static String printId(String ident, boolean allowDot) {
	if (ident != null) {
	    char[] ch = ident.toCharArray();
	    if (ch.length > 0) {
		boolean escape = !Character.isJavaIdentifierStart(ch[0]);
		if (!escape) {
		    for (int i = 1; i < ch.length; i++) {
			if (!Character.isJavaIdentifierPart(ch[i])) {
			    if (!allowDot || ch[i] != '.') {
				escape = true;
				break;
			    }
			}
		    }
		}
		if (escape) {
		    StringWriter writer = new StringWriter();
		    printEscape(writer, ch);
		    return writer.toString();
		}
	    }
	}
	return ident;
    }
}
