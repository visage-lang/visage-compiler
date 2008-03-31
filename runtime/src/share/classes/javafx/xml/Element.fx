/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
 * particular file as subject to the "Classpath" exception as provided 
 * by Sun in the LICENSE file that accompanied this code. 
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

/**
 * Represents a DOM Element
 * 
 * @see  org.w3c.dom.Element
 * @author jclarke
 */
public class Element extends Node {

    /**
     * sets the node type to ELEMENT
     */
    override attribute type = NodeType.ELEMENT;
    
    /**
     * Holds the node's attributes
     */
    public attribute attributes:Attribute[]  on replace oldValue[lo..hi]=newVals {
        for(n in oldValue[lo..hi]) { 
            n.parent = null;
            var elem = domNode as org.w3c.dom.Element;
            if(elem.hasAttribute(n.name)) {
                elem.removeAttributeNode(n.domNode as org.w3c.dom.Attr); 
            }
        }
        for(n in newVals) {
            n.parent = this;
            var elem = domNode as org.w3c.dom.Element;
            if(not elem.hasAttribute(n.name)) {
                elem.setAttributeNode(n.domNode as org.w3c.dom.Attr);
            }
        }
    };
    
    /**
     * Retrieves an attribute value by name.
     * @param name The name of the attribute to retrieve.
     * @return The <code>Attr</code> value as a string, or the empty string
     *   if that attribute does not have a specified or default value.
     * @see org.w3c.dom.Element.getAttribute();
     */
    public function getAttribute(name:String):String {
        var elem = domNode as org.w3c.dom.Element;
        return elem.getAttribute(name);
    }
    
    /**
     * Removes an attribute by name.
     * 
     * @param name The name of the attribute to remove.
     * @see org.w3c.dom.Element.removeAttribute();
     */
    public function removeAttribute(name:String):Void {
        var f = for(a in attributes where a.name == name) a;
        for(n in f) {
            delete n from attributes;
        }
    }
    /**
     * Adds or updates an attribute
     * 
     * @param name The name of the attribute.
     * @param value the value for the attribute.
     * @see org.w3c.dom.Element.setAttribute();
     */
    public function setAttribute(name:String, value:String):Void {
        var f = for(a in attributes where a.name == name) a;
        if(sizeof f == 0) {
            var attr = document.createAttribute(name, value);
            insert attr into attributes;
        }else {
            for(a in f) {
                a.value = value;
            }
        }
    }
    /**
     * Adds or updates an attribute
     * 
     * @param namespaceURI the namespace of the element
     * @param qualifiedName the qualified name of the element 
     * @param value the value for the attribute.
     * @see org.w3c.dom.Element.setAttribute();
     */
    public function setAttributeNS(namespaceURI:String, qualifiedName:String, value:String):Void {
        var f = for(a in attributes where a.name == name) a;
        if(sizeof f == 0) {
            var attr = document.createAttributeNS(namespaceURI, qualifiedName, value);
            insert attr into attributes;
        }else {
            for(a in f) {
                a.value = value;
            }
        }
    }    
    /**
     * Adds an attribute
     * 
     * @param name The name of the attribute.
     * @param value the value for the attribute.
     */    
    public function addAttribute(name:String, value:String):Void {
        var attr = document.createAttribute(name, value);
        insert attr into attributes;
    }    
    
    /**
     * Adds an attribute
     * 
     * @param namespaceURI the namespace of the element
     * @param qualifiedName the qualified name of the element 
     * @param value the value for the attribute.
     */    
    public function addAttributeNS(namespaceURI:String, qualifiedName:String, value:String):Void {
        var attr = document.createAttributeNS(namespaceURI, qualifiedName, value);
        insert attr into attributes;
    }      
    
    /**
     * add an element as a child to this element
     * @param name the name of the element
     * @return the new element
     */
    public function addElement(name:String):Element {
        var elem = document.createElement(name);
        insert elem into children;
        elem;
    }
    /**
     * add an element as a child to this element
     * @param namespaceURI the namespace of the element
     * @param qualifiedName the qualified name of the element 
     * @return the new element
     */    
    public function addElementNS(namespaceURI:String, qualifiedName:String):Element{
        var elem = document.createElementNS(namespaceURI, qualifiedName);
        insert elem into children;
        elem;
    }
    /**
     * add data to this element
     * @param data the data
     * @return the new Node
     */
    public function addText(data:String):Node {
        var node = document.createText(data);
        insert node into children;
        node;
    }
    /**
     * add a comment to this element
     * @param comment the comment
     * @return the new Node
     */    
    public function addComment(comment:String):Node {
        var node = document.createComment(comment);
        insert node into children;
        node;        
    }
    
    /**
     * set the associated properties from the dom Node
     * @param domNode the dom node
     */    
    protected function setDomNode(domNode:org.w3c.dom.Node):Void {
        //delete attributes;
        super.setDomNode(domNode);
        
        var attrs = domNode.getAttributes();
        if(attrs <> null) {
            for(i in [0..<attrs.getLength()]) {
                var attrNode = Attribute { domNode:attrs.item(i), document:document};
                insert attrNode into attributes;
            }
        }
    }
    
    init {
        var elem = domNode as org.w3c.dom.Element;
        if(domNode == null) {
            domNode = createNode();

            for(e in children) {
                if(e.parent <> null and e.parent <> this) {
                    delete e from e.parent.children;
                }                
                if(e.parent <> this) {
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
        for(a in attributes) {
            if(a.domNode == null ) {
                var attr = document.document.createAttributeNS(a.namespaceURI, a.name);
                attr.setValue(a.value);
                a.domNode = attr;
            }            
            if(a.parent <> this) {
                a.parent = this;
            }
            if(not elem.hasAttributeNS(a.namespaceURI, a.name)) {
                elem.setAttributeNode(a.domNode as org.w3c.dom.Attr);
            }
        }
    }
}
