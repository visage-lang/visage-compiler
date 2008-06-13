/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 
 * version 2 for more details (a copy is included in the LICENSE file that 
 * accompanied this code). 
 * 
 * You should have received a copy of the GNU General Public License version 
 * 2 along with this work; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, 
 * CA 95054 USA or visit www.sun.com if you need additional information or 
 * have any questions. 
 */ 

package javafx.xml;
import java.lang.System;
import java.util.logging.Logger;
import org.w3c.dom.NodeList;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * 
 * <p>The <code>Document</code> interface represents the entire HTML or XML
 * document. Conceptually, it is the root of the document tree, and provides
 * the primary access to the document's data.</p>
 * <p>Since elements, text nodes, comments, processing instructions, etc.
 * cannot exist outside the context of a <code>Document</code>, the
 * <code>Document</code> interface also contains the factory methods needed
 * to create these objects. The <code>Node</code> objects created have a
 * <code>ownerDocument</code> attribute which associates them with the
 * <code>Document</code> within whose context they were created.
 * <p>See also the <a href='http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040
407'>Document Object Model (DOM) Level 3 Core Specification</a>.
 *
 * @author jclarke
 */

public class Document {
    static attribute tfactory:TransformerFactory  = TransformerFactory.newInstance();
    static attribute xfactory:XPathFactory = XPathFactory.newInstance();

    /**
     * holds the internal org.w3c.dom.Document
     */
    public attribute document:org.w3c.dom.Document on replace {
        if(document.getDocumentElement() <> null ) {
            documentElement = Element{domNode:document.getDocumentElement(), document:this};
        }
    }
    
    /**
     * Holds an optional namespace context used for serializing or printing the
     * document
     */
    public attribute namespace:NamespaceContext;
    
    /**
     *  holds the document element 
     */
    public attribute documentElement:Element on replace old {
        if(documentElement.domNode <> document.getDocumentElement()) {
            if(document.getDocumentElement() <> null) {
                document.removeChild(document.getDocumentElement());
            }
            document.appendChild(documentElement.domNode);
            
        }
    };
    
    
    /**
     * Holds the number of spaces for indenting when printing or serializing.
     * doIndent must be true for this to take effect. Default indent is 2.
     * @defaultvalue 2
     */
    public attribute indent:Integer = 2;
    
    /**
     * indicates whether or not indenting should be used when printing or serializing.
     * Default is true
     * @defaultvalue true
     */
    public attribute doIndent:Boolean = true;
    
    /**
     * indicates whether or not to include the xml declaration when printing or serializing.
     * Default is false (xml declaration is included).
     * @defaultvalue false
     */    
    public attribute omitXMLDeclaration:Boolean = false;
    
    /**
     * indicates which encoding should be used when printing or serializing.
     * Default is null, use system default.
     * @defaultvalue null
     */    
    public attribute encoding:String;
    
    /**
     * indicates whether or not the document is standalone.
     * Default is false
     * @defaultvalue false
     */    
    public attribute standalone:Boolean = false;
    
    /**
     * Creates an element of the type specified.
     * @param tagName The name of the element type to instantiate
     * @return A new <code>Element</code>.
     * @see org.w3c.dom.Document#createElement
     */
    public function createElement(tagName:String):Element{
        var elem = document.createElement(tagName);
        Element { domNode:elem, document:this };
    }
    
    /**
     * Creates an element of the given qualified name and namespace URI.
     * 
     * @param namespaceURI The namespace URI of the element to create.
     * @param qualifiedName The qualified name of the element type to instantiate.
     * @return A new <code>Element</code> 
     * @see org.w3c.dom.Document#createElementNS
     */
    public function createElementNS(namespaceURI:String, qualifiedName:String):Element{
        var elem = document.createElementNS(namespaceURI, qualifiedName);
        Element { domNode:elem, document:this };
    }    
    
    /**
     * Creates a <code>Text</code> node given the specified string.
     * @param data The data for the node.
     * @return The new <code>Text</code> node.
     * @see org.w3c.dom.Document#createTextNode
     */    
    public function createText(data:String):Text {
        var txtNode = document.createTextNode(data);
        Text { domNode:txtNode, document:this };
    }
    
     /**
     * Creates an <code>Attr</code> of the given name.
     * @param name The name of the attribute.
     * @return A new <code>Attr</code> node.
     * @see org.w3c.dom.Document#createAttribute
     */
    public function createAttribute(name:String, value:String):Attribute {
        var attr = document.createAttribute(name);
        attr.setValue(value);
        Attribute{domNode:attr, document:this};
    }
    

    /**
     * Creates an attribute of the given qualified name and namespace URI.
     * @param namespaceURI The namespace URI of the attribute to create.
     * @param qualifiedName The qualified name of the attribute to
     *   instantiate.
     * @return A new <code>Attr</code> node.
     * @see org.w3c.dom.Document#createAttributeNS
     */
    public function createAttributeNS(namespaceURI:String, qualifiedName:String, value:String):Attribute {
        var attr = document.createAttributeNS(namespaceURI, qualifiedName);
        attr.setValue(value);
        Attribute{domNode:attr, document:this};
    }    
    
    /**
     * Creates a <code>Comment</code> node given the specified string.
     * @param comment The comment data for the node.
     * @return The new <code>Comment</code> node.
     * @see org.w3c.dom.Document#createComment
     */    
    public function createComment(comment:String):Comment {
        var node = document.createComment(comment);
        Comment{domNode:node, document:this};
    }
    
    /**
     * Creates a <code>CDATASection</code> node whose value is the specified
     * string.
     * @param data The data for the <code>CDATASection</code> contents.
     * @return The new <code>CDATASection</code> node.
     * @see org.w3c.dom.Document#createCDATASection
     */
    public function createCDATASection(cdata:String):Node {
        var node = document.createCDATASection(cdata);
        Node{domNode:node, document:this};
    }   
    
    /**
     * Creates an <code>EntityReference</code> object. 
     * @param name The name of the entity to reference.
     * @return The new <code>EntityReference</code> node.
     * @see org.w3c.dom.Document#createEntityReference
     */
    public function createEntityReference(name:String):Node {
        var node = document.createEntityReference(name);
        Node{domNode:node, document:this};
    }  
    
    /**
     * Creates a <code>ProcessingInstruction</code> node
     * @param target The target part of the processing instruction.
     * @param data The data for the node.
     * @return The new <code>ProcessingInstruction</code> node.
     * @see org.w3c.dom.Document#createProcessingInstruction
     */
    public function createProcessingInstruction(target:String, data:String):Node {
        var node = document.createProcessingInstruction(target, data);
        Node{domNode:node, document:this};
    }     
    
    /**
     * Returns the <code>Element</code> that has an ID attribute with the
     * given value. If no such element exists, this returns <code>null</code>
     * . If more than one element has an ID attribute with that value, what
     * is returned is undefined.
     * @param elementId The unique <code>id</code> value for an element.
     * @return The matching element or <code>null</code> if there is none.
     * @see org.w3c.dom.Document#getElementById
     */
    public function getElementById(elementId:String) : Element {
        var elem = document.getElementById(elementId);
        return if(elem <> null) elem.getUserData("FX") as Element else null;
    }
    
    
    /**
     * Returns a sequence of all the <code>Elements</code> in
     * document order with a given tag name and are contained in the
     * document.
     * @param tagname  The name of the tag to match on. The special value "*"
     *   matches all tags. For XML, the <code>tagname</code> parameter is
     *   case-sensitive, otherwise it depends on the case-sensitivity of the
     *   markup language in use.
     * @return A new sequence containing all the matched
     *   <code>Elements</code>.
     * @see org.w3c.dom.Document#getElementsByTagName
     */
    public function getElementsByTagName(tagName:String) : Element[] {
        var elements:Element[] = [];
        var result:NodeList = document.getElementsByTagName(tagName);
        for( i in [0..<result.getLength()]) {
            var n = result.item(i);
            var fxnode = n.getUserData("FX") as Element;
            if(fxnode <> null) {
                insert fxnode into elements;
            }else {
                Logger.getLogger(this.getClass().getName()).warning("dom node has no FX node assigned: {n}");
            }
        }        
        return elements;
    }
    
    /**
     * Returns a sequence of all the <code>Elements</code> with a
     * given local name and namespace URI in document order.
     * @param namespaceURI The namespace URI of the elements to match on. The
     *   special value <code>"*"</code> matches all namespaces.
     * @param localName The local name of the elements to match on. The
     *   special value "*" matches all local names.
     * @return A new sequence containing all the matched
     *   <code>Elements</code>.
     * @see org.w3c.dom.Document#getElementsByTagNameNS
     */
    public function getElementsByTagNameNS(namespaceURI:String, localName:String): Element[] {
        var elements:Element[] = [];
        var result:NodeList = document.getElementsByTagNameNS(namespaceURI, localName);
        for( i in [0..<result.getLength()]) {
            var n = result.item(i);
            var fxnode = n.getUserData("FX") as Element;
            if(fxnode <> null) {
                insert fxnode into elements;
            }else {
                Logger.getLogger(this.getClass().getName()).warning("dom node has no FX node assigned: {n}");
            }
        }        
        return elements;
    }
    
    /**
     * Perform an xpath query on the document
     * @param query the xpath query
     * @return a sequence of Nodes that match the query
     * @see javax.xml.xpath.XPath
     */
    public function query(query:String):Node[] {
        var xpath = xfactory.newXPath();
        if(namespace <> null) {
            xpath.setNamespaceContext(namespace);
        }
        var expr = xpath.compile(query);
        
        var result:NodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList;
        var nodes:Node[] = [];
        for( i in [0..<result.getLength()]) {
            var n = result.item(i);
            var fxnode = n.getUserData("FX") as Node;
            if(fxnode <> null) {
                insert fxnode into nodes;
            }else {
                Logger.getLogger(this.getClass().getName()).warning("dom node has no FX node assigned: {n}");
            }
        }
        return nodes;
    }
    
    /**
     * Perform an xpath query on the document expecting a Boolean result
     * @param query the xpath query
     * @return a boolean value representing the result of the query
     * @see javax.xml.xpath.XPath
     */    
    public function queryBoolean(query:String):Boolean {
        var xpath = xfactory.newXPath();
        if(namespace <> null) {
            xpath.setNamespaceContext(namespace);
        }
        var expr = xpath.compile(query);
        return expr.evaluate(document, XPathConstants.BOOLEAN) as java.lang.Boolean;
    }
    
    /**
     * Perform an xpath query on the document expecting a Number result
     * @param query the xpath query
     * @return a number value representing the result of the query
     * @see javax.xml.xpath.XPath
     */   
    public function queryNumber(query:String):Number {
        var xpath = xfactory.newXPath();
        if(namespace <> null) {
            xpath.setNamespaceContext(namespace);
        }
        var expr = xpath.compile(query);
        return expr.evaluate(document, XPathConstants.NUMBER) as java.lang.Double;
    }  
    
    /**
     * Perform an xpath query on the document expecting a String result
     * @param query the xpath query
     * @return a string value representing the result of the query
     * @see javax.xml.xpath.XPath
     */   
    public function queryString(query:String):String {
        var xpath = xfactory.newXPath();
        if(namespace <> null) {
            xpath.setNamespaceContext(namespace);
        }
        var expr = xpath.compile(query);
        return expr.evaluate(document, XPathConstants.STRING) as String;
    }    
    
    /**
     * Perform an xpath query on the document expecting a Node result
     * @param query the xpath query
     * @return a Node representing the result of the query
     * @see javax.xml.xpath.XPath
     */    
    public function queryNode(query:String):Node {
        var xpath = xfactory.newXPath();
        if(namespace <> null) {
            xpath.setNamespaceContext(namespace);
        }
        var expr = xpath.compile(query);
        var n = expr.evaluate(document, XPathConstants.NODE) as org.w3c.dom.Node;
        return if(n <> null) then n.getUserData("FX") as Node  else null;
    }      
    
    /**
     * Convert the document to an XML formatted string based on 
     * the attributes indent, doIndent, omitXMLDeclaration, encoding and
     * standalone.
     * @return an XML formatted string
     * @see #serialize()
     * @see #indent
     * @see #doIndent
     * @see #omitXMLDeclaration
     * @see #encoding
     * @see #standalone
     */
    public function toString():String {
        var writer = new StringWriter();
        serialize(writer);
        return writer.toString();
    } 
    
    /**
     * Convert the document to an XML format based on 
     * the attributes indent, doIndent, omitXMLDeclaration, encoding and
     * standalone. Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated xml.
     * @see #indent
     * @see #doIndent
     * @see #omitXMLDeclaration
     * @see #encoding
     * @see #standalone
     */    
    public function serialize(writer:Writer):Void {
        var serializer = tfactory.newTransformer();
        serializer.setOutputProperty(OutputKeys.INDENT, if(doIndent)then "yes" else "no");
        serializer.setOutputProperty("\{http://xml.apache.org/xslt}indent-amount", "{indent}");
        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, 
                                if(omitXMLDeclaration) then "yes" else "no");
        if( encoding <> null) {
            serializer.setOutputProperty(OutputKeys.ENCODING, encoding);
        }
        serializer.setOutputProperty(OutputKeys.STANDALONE, 
                                if(standalone) then "yes" else "no");
        serializer.transform(new DOMSource(document), new StreamResult(writer));
    }
}
