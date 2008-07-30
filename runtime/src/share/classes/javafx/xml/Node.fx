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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.xpath.XPathConstants;
;


/**
 * A Node represents a single node in the Document tree.
 * 
 * @see org.w3c.dom.Node
 * @author jclarke
 */
public class Node {

    /**
     * flag indicating child is being added to the children
     */
    private attribute addingChild:Boolean;

    /**
     * holds the associated document
     */
    public attribute document:Document on replace {
        if(document != null) {
            initDomNode();
        }
    };
    
    /**
     *  holds the associated dom node
     */
    public attribute domNode:org.w3c.dom.Node on replace {
        if(domNode != null) {
            setDomNode(domNode);
        }
    };
    
    
    
    // Transform properties for formatting xml
    /**
     * Holds the number of spaces for indenting when printing or serializing.
     * doIndent must be true for this to take effect. Default indent is 2.
     */
    public attribute indent:Integer = 2;
    
    /**
     * indicates whether or not indenting should be used when printing or serializing.
     * Default is true
     */
    public attribute doIndent:Boolean = true;
    
    /**
     * indicates whether or not to include the xml declaration when printing or serializing.
     * Default is false (xml declaration is included).
     */    
    public attribute omitXMLDeclaration:Boolean = false;
    
    /**
     * indicates which encoding should be used when printing or serializing.
     * Default is null, use system default.
     */    
    public attribute encoding:String;
    
    /**
     * indicates whether or not the document is standalone.
     * Default is false
     */    
    public attribute standalone:Boolean = false; 
    
    /**
     * set the associated properties from the dom Node
     * @param domNode the dom node
     */
    protected function setDomNode(domNode:org.w3c.dom.Node):Void {
        
        domNode.setUserData("FX", this, null);
        name = domNode.getNodeName();
        namespaceURI = domNode.getNamespaceURI();
        baseURI = domNode.getBaseURI();
        prefix = domNode.getPrefix();
        localName = domNode.getLocalName();    
        value = domNode.getTextContent();
        type = NodeType.getNodeType(domNode.getNodeType());
        var nl:NodeList = domNode.getChildNodes();
        for(i in [0..<nl.getLength()]) {
            var dnode:org.w3c.dom.Node = nl.item(i);
            if(dnode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element{domNode:dnode, document:document, parent:this};
            } else {
                Node { domNode:dnode, document:document, parent:this};
            }
        }        
    }
    
    /**
     * Holds the parent node for this node. 
     * When changed, the parent children sequence is modified
     * to include this node.
     */
    public attribute parent:Node on replace oldValue {
        if(oldValue != null ) {
            delete this from oldValue.children;
        }
        if(parent != null and type != NodeType.ATTRIBUTE) {
            if(not parent.addingChild) {
                insert this into parent.children;
            }
            document = parent.document;
        }
    };
    
    /**
     * Holds the children node to this node
     * When a new child node is added, the parent node
     * for the child node is set to this node.
     */
    public attribute children:Node[]   on replace oldValue[lo..hi]=newVals {
        addingChild = true;
        if(domNode == null) {
            initDomNode();
        }
        try {
            for(n in oldValue[lo..hi]) { 
                if(n.parent != null) {
                    delete n from n.parent.children;
                    n.parent = null;
                }
                if(domNode != null and isChild(n)) {
                    domNode.removeChild(n.domNode);
                }

            }
            for(n in newVals) {
                if(n.parent != null and n.parent != this) {
                    delete n from n.parent.children;
                }
                if(n.parent != this) {
                    n.parent = this;
                }
                if(domNode != null and not isChild(n)) {
                    if(n.domNode == null) {
                        n.domNode = n.createNode();
                    }
                    domNode.appendChild(n.domNode);
                }
            }
        } finally {
            addingChild = false;
        }
    };
    
    /**
     *  holds the node's value
     */  
    public attribute value:String on replace {
        if(type == NodeType.ATTRIBUTE or type == NodeType.ELEMENT or
           type == NodeType.CDATA_SECTION or type == NodeType.COMMENT or
           type == NodeType.TEXT or type == NodeType.PROCESSING_INSTRUCTION) {
              domNode.setNodeValue(value);
        }
    };
    
    /**
     * holds the node's qualified name
     */
    public attribute name:String on replace old {
        if(domNode == null) {
            initDomNode();
        }
        if(document != null and domNode != null and 
                (type == NodeType.ATTRIBUTE or type == NodeType.ELEMENT) ) {
            if(name != domNode.getNodeName()) {
                var newNode = document.document.renameNode(domNode, namespaceURI, name);
                if(newNode != domNode) {
                    setDomNode(newNode);
                }else {
                    namespaceURI = domNode.getNamespaceURI();
                    baseURI = domNode.getBaseURI();
                    prefix = domNode.getPrefix();
                    localName = domNode.getLocalName();                    
                }
            }
        }
    }
    
    /**
     * holds the node's namespace
     */
    public attribute namespaceURI:String on replace {
        if(document != null and (type == NodeType.ATTRIBUTE or type == NodeType.ELEMENT) ) {
            var newNode = document.document.renameNode(domNode, namespaceURI, name);
            if(newNode != domNode) {
                setDomNode(newNode);
                }else {
                    name = domNode.getNodeName();
                    baseURI = domNode.getBaseURI();
                    prefix = domNode.getPrefix();
                    localName = domNode.getLocalName();                    
                }
        }
    }
    
    /**
     * holds the nodes base URI
     */
    public attribute baseURI:String;
    
    /**
     * holds the node's prefix
     */
    public attribute prefix:String on replace {
        if(domNode != null and prefix != null and prefix.length() > 0 and
            (type == NodeType.ELEMENT or type == NodeType.ATTRIBUTE) ) {
            if(prefix != domNode.getPrefix()) {
                domNode.setPrefix(prefix);
            }
        }
    };
    /**
     * holds the node's local Nnme
     */
    public attribute localName:String;
    
    /**
     * holds the node's type
     */
    public attribute type:NodeType;    
    
    /**
     *  determine whether the node's dom node is already a child
     * of this dom node.
     * @param node the node to check
     * @return true if the node's dom node is already a child of this dom node.
     */
    protected function isChild(node:Node):Boolean {
        var nl = domNode.getChildNodes();
        for( i in [0..<nl.getLength()]) {
            var n = nl.item(i) as org.w3c.dom.Node;
            if(node.domNode.isSameNode(n)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * normalize the node tree
     * @see  org.w3c.dom.Node#normalize()
     */
    public function nomalize():Void {
        domNode.normalize();
        setDomNode(domNode);
    }
    
    /**
     * Perform an xpath query using this node as the root
     * @param query the xpath query
     * @return a sequence of Nodes that match the query
     * @see javax.xml.xpath.XPath
     */    
    public function query(query:String):Node[] {
        var xpath = Document.xfactory.newXPath();
        if(document.namespace != null) {
            xpath.setNamespaceContext(document.namespace);
        }        
        var expr = xpath.compile(query);
        var result:NodeList = expr.evaluate(domNode, XPathConstants.NODESET) as NodeList;
        var nodes:Node[] = [];
        for( i in [0..<result.getLength()]) {
            var n = result.item(i);
            var fxnode = n.getUserData("FX") as Node;
            if(fxnode != null) {
                insert fxnode into nodes;
            }else {
                Logger.getLogger(this.getClass().getName()).warning("dom node has no FX node assigned: {n}");
            }
        }
        return nodes;
    }    
    
    /**
     * Perform an xpath query using this node as the root expecting a Boolean result
     * @param query the xpath query
     * @return a boolean value representing the result of the query
     * @see javax.xml.xpath.XPath
     */     
    public function queryBoolean(query:String):Boolean {
        var xpath = Document.xfactory.newXPath();
        if(document.namespace != null) {
            xpath.setNamespaceContext(document.namespace);
        }          
        var expr = xpath.compile(query);
        return expr.evaluate(domNode, XPathConstants.BOOLEAN) as java.lang.Boolean;
    }
    
    /**
     * Perform an xpath query using this node as the root expecting a Number result
     * @param query the xpath query
     * @return a number value representing the result of the query
     * @see javax.xml.xpath.XPath
     */      
    public function queryNumber(query:String):Number {
        var xpath = Document.xfactory.newXPath();
        if(document.namespace != null) {
            xpath.setNamespaceContext(document.namespace);
        }          
        var expr = xpath.compile(query);
        return expr.evaluate(domNode, XPathConstants.NUMBER) as java.lang.Double;
    }  
    
    /**
     * Perform an xpath query using this node as the root expecting a String result
     * @param query the xpath query
     * @return a string value representing the result of the query
     * @see javax.xml.xpath.XPath
     */    
    public function queryString(query:String):String {
        var xpath = Document.xfactory.newXPath();
        if(document.namespace != null) {
            xpath.setNamespaceContext(document.namespace);
        }          
        var expr = xpath.compile(query);
        return expr.evaluate(domNode, XPathConstants.STRING) as String;
    }    
    
    /**
     * Perform an xpath query using this node as the root expecting a Node result
     * @param query the xpath query
     * @return a Node representing the result of the query
     * @see javax.xml.xpath.XPath
     */     
    public function queryNode(query:String):Node {
        var xpath = Document.xfactory.newXPath();
        if(document.namespace != null) {
            xpath.setNamespaceContext(document.namespace);
        }          
        var expr = xpath.compile(query);
        var n = expr.evaluate(domNode, XPathConstants.NODE) as org.w3c.dom.Node;
        return if(n != null) then n.getUserData("FX") as Node  else null;
    }    
    
    /**
     * Convert this node to an XML formatted string based on 
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
    override function toString():String {
        var writer = new StringWriter();
        serialize(writer);
        return writer.toString();
    } 

    /**
     * get the indent string for a particular depth. Used for printing.
     * @param depth the depth within the tree 
     * @return the indent string 
     */
    public function getIndent(depth):String {
        var indentStr:String;
        if(doIndent) {
            var i = 0;
            while(i < depth) {
                var j = 0;
                while(j < indent) {
                    indentStr += " ";
                    j++;
                }
                i++;
            }
        }
        indentStr;
    }
    
    /**
     * Convert this node to an XML format based on 
     * the attributes indent, doIndent, omitXMLDeclaration, encoding and
     * standalone. Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated xml.
     * @see indent
     * @see doIndent
     */     
    public function serialize(writer:Writer):Void {
        serialize(writer, 0);
    }
    /**
     * Convert this node to an XML format based on 
     * the attributes indent, doIndent, omitXMLDeclaration, encoding and
     * standalone. Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated xml.
     * @param depth the depth of this node in the tree being serialized
     * @see indent
     * @see doIndent
     */     
    public function serialize(writer:Writer, depth:Integer):Void {
        writer.write("{value}");
        writer.flush();
    }

    
    
    /**
     * create a default node based on this node's node type
     * @return the node
     */
    protected function createNode():org.w3c.dom.Node {
        if(type == NodeType.ATTRIBUTE) {
            return document.document.createAttributeNS(namespaceURI, name);
        }else if (type == NodeType.CDATA_SECTION) { 
            return document.document.createCDATASection(value);
        }else if (type == NodeType.COMMENT) {
            return document.document.createComment(value);
        //}else if (type == NodeType.DOCUMENT) {            
        //}else if (type == NodeType.DOCUMENT_FRAGMENT) {            
        //}else if (type == NodeType.DOCUMENT_TYPE) {
        }else if (type == NodeType.ELEMENT) {
            return document.document.createElementNS(namespaceURI, name);
        //}else if (type == NodeType.ENTITY) {
        }else if (type == NodeType.ENTITY_REFERENCE) {
            return document.document.createEntityReference(name);
        //}else if (type == NodeType.NOTATION) {
        //}else if (type == NodeType.PROCESSING_INSTRUCTION) {
        }else if (type == NodeType.TEXT) {
            return document.document.createTextNode(value);
        }else {
            return null;
        };
    }

    protected function initDomNode():Void {
        if(document != null and domNode == null) {
            domNode = createNode();

            for(e in children) {
                if(e.parent != null and e.parent != this) {
                    delete e from e.parent.children;
                }                
                if(e.parent != this) {
                    e.parent = this;
                }
                if(not isChild(e)) {
                    if(e.domNode == null) {
                        e.domNode = e.createNode();
                    }
                    domNode.appendChild(e.domNode);
                }
            }
        }    
    }
    
    /**
     * initialize the domNode if it is null
     */
    postinit {
        initDomNode();
    }

    
}
