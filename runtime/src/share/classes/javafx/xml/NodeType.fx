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

/**
 * Indicates the type of DOM Node
 * 
 * @author jclarke
 */

public class NodeType {
    /**
     * the id for the node type
     * @see org.w3c.dom.Node
     */
    public attribute id:Integer;
    
    /**
     * the name for the node type
     * @see org.w3c.dom.Node
     */    
    public attribute name:String;

    /**
     * the node is an Attribute
     */
    public static attribute ATTRIBUTE = NodeType{ id:org.w3c.dom.Node.ATTRIBUTE_NODE, name:"Attr"};
     /**
     * the node is an CDATASection
     */    
    public static attribute CDATA_SECTION = NodeType{ id:org.w3c.dom.Node.CDATA_SECTION_NODE, name:"CDATASection"};
     /**
     * the node is a Comment
     */    
    public static attribute COMMENT = NodeType{ id:org.w3c.dom.Node.COMMENT_NODE, name:"Comment"};
     /**
     * the node is a Document
     */    
    public static attribute DOCUMENT = NodeType{ id:org.w3c.dom.Node.DOCUMENT_NODE, name:"Document"};
     /**
     * the node is a DocumentFragment
     */    
    public static attribute DOCUMENT_FRAGMENT = NodeType{ id:org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE, name:"DocumentFragment"};
     /**
     * the node is a DocumentType
     */    
    public static attribute DOCUMENT_TYPE = NodeType{ id:org.w3c.dom.Node.DOCUMENT_TYPE_NODE, name:"DocumentType"};
     /**
     * the node is an Element
     */
    public static attribute ELEMENT = NodeType{ id:org.w3c.dom.Node.ELEMENT_NODE, name:"Element"};
     /**
     * the node is an Entity
     */    
    public static attribute ENTITY = NodeType{ id:org.w3c.dom.Node.ENTITY_NODE, name:"Entity"};
     /**
     * the node is an EntityReference
     */    
    public static attribute ENTITY_REFERENCE = NodeType{ id:org.w3c.dom.Node.ENTITY_REFERENCE_NODE, name:"EntityReference"};
     /**
     * the node is a Notation
     */    
    public static attribute NOTATION = NodeType{ id:org.w3c.dom.Node.NOTATION_NODE, name:"Notation"};
     /**
     * the node is a ProcessingInstruction
     */    
    public static attribute PROCESSING_INSTRUCTION = NodeType{ id:org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE, name:"ProcessingInstruction"};
     /**
     * the node is a Text Node
     */    
    public static attribute TEXT = NodeType{ id:org.w3c.dom.Node.TEXT_NODE, name:"Text"};
    
    /**
     * Determine the NodeType from the dom node type integer
     * @param domType the dom node type 
     * @return the matching NodeType
     */
    public static function getNodeType(domType:Integer):NodeType {
        if(domType == org.w3c.dom.Node.ATTRIBUTE_NODE) {
            NodeType.ATTRIBUTE;
        }else if (domType == org.w3c.dom.Node.CDATA_SECTION_NODE) {
            NodeType.CDATA_SECTION            
        }else if (domType == org.w3c.dom.Node.COMMENT_NODE) {
            NodeType.COMMENT;
        }else if (domType == org.w3c.dom.Node.DOCUMENT_NODE) {
            NodeType.DOCUMENT;            
        }else if (domType == org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE) {
            NodeType.DOCUMENT_FRAGMENT;            
        }else if (domType == org.w3c.dom.Node.DOCUMENT_TYPE_NODE) {
            NodeType.DOCUMENT_TYPE;
        }else if (domType == org.w3c.dom.Node.ELEMENT_NODE) {
            NodeType.ELEMENT;
        }else if (domType == org.w3c.dom.Node.ENTITY_NODE) {
            NodeType.ENTITY;
        }else if (domType == org.w3c.dom.Node.ENTITY_REFERENCE_NODE) {
            NodeType.ENTITY_REFERENCE;
        }else if (domType == org.w3c.dom.Node.NOTATION_NODE) {
            NodeType.NOTATION;
        }else if (domType == org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE) {
            NodeType.PROCESSING_INSTRUCTION;
        }else if (domType == org.w3c.dom.Node.TEXT_NODE) {
            NodeType.TEXT;
        }else {
            return null;
        };
    }

}