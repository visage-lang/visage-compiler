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

/**
 * Indicates the type of DOM Node
 * 
 * @author jclarke
 */
public enum NodeType {

    /**
     * the node is an Attribute
     */
    ATTRIBUTE(org.w3c.dom.Node.ATTRIBUTE_NODE, "Attr"),

    /**
     * the node is an CDATASection
     */    
    CDATA_SECTION(org.w3c.dom.Node.CDATA_SECTION_NODE, "CDATASection"),

    /**
     * the node is a Comment
     */    
    COMMENT(org.w3c.dom.Node.COMMENT_NODE, "Comment"),

    /**
     * the node is a Document
     */    
    DOCUMENT(org.w3c.dom.Node.DOCUMENT_NODE, "Document"),

    /**
     * the node is a DocumentFragment
     */    
    DOCUMENT_FRAGMENT(org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE, "DocumentFragment"),

    /**
     * the node is a DocumentType
     */    
    DOCUMENT_TYPE(org.w3c.dom.Node.DOCUMENT_TYPE_NODE, "DocumentType"),

    /**
     * the node is an Element
     */
    ELEMENT(org.w3c.dom.Node.ELEMENT_NODE, "Element"),

    /**
     * the node is an Entity
     */    
    ENTITY(org.w3c.dom.Node.ENTITY_NODE, "Entity"),

    /**
     * the node is an EntityReference
     */    
    ENTITY_REFERENCE(org.w3c.dom.Node.ENTITY_REFERENCE_NODE, "EntityReference"),

    /**
     * the node is a Notation
     */    
    NOTATION(org.w3c.dom.Node.NOTATION_NODE, "Notation"),

    /**
     * the node is a ProcessingInstruction
     */    
    PROCESSING_INSTRUCTION(org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE, "ProcessingInstruction"),

    /**
     * the node is a Text Node
     */    
    TEXT(org.w3c.dom.Node.TEXT_NODE, "Text");

    /**
     * the id for the node type
     * @see org.w3c.dom.Node
     */
    public final int id;
    
    /**
     * the name for the node type
     * @see org.w3c.dom.Node
     */    
    public final String name;

    private NodeType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Determine the NodeType from the dom node type integer
     * @param domType the dom node type 
     * @return the matching NodeType
     */
    public static NodeType getNodeType(int domType) {
        if(domType == org.w3c.dom.Node.ATTRIBUTE_NODE) {
            return ATTRIBUTE;
        } else if (domType == org.w3c.dom.Node.CDATA_SECTION_NODE) {
            return CDATA_SECTION;       
        } else if (domType == org.w3c.dom.Node.COMMENT_NODE) {
            return COMMENT;
        } else if (domType == org.w3c.dom.Node.DOCUMENT_NODE) {
            return DOCUMENT;            
        } else if (domType == org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE) {
            return DOCUMENT_FRAGMENT;            
        } else if (domType == org.w3c.dom.Node.DOCUMENT_TYPE_NODE) {
            return DOCUMENT_TYPE;
        } else if (domType == org.w3c.dom.Node.ELEMENT_NODE) {
            return ELEMENT;
        } else if (domType == org.w3c.dom.Node.ENTITY_NODE) {
            return ENTITY;
        } else if (domType == org.w3c.dom.Node.ENTITY_REFERENCE_NODE) {
            return ENTITY_REFERENCE;
        } else if (domType == org.w3c.dom.Node.NOTATION_NODE) {
            return NOTATION;
        } else if (domType == org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE) {
            return PROCESSING_INSTRUCTION;
        } else if (domType == org.w3c.dom.Node.TEXT_NODE) {
            return TEXT;
        } else {
            return null;
        }
    }

}
