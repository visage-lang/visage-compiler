/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.json;

/**
 * Contains static definitions of the JSON Element Types that are identified
 * during parsing of a JSON stream
 * @author jclarke
 */

public class ElementType {

    /** the name of the element type */
    public attribute name:String;
    
    /** Indicates that a JSONObject has been idenfitied*/
    public static attribute OBJECT:ElementType = ElementType{name:"JSONObject"};
    /** Indicates that a JSON Array has been idenfitied*/
    public static attribute ARRAY:ElementType = ElementType{name:"Array"};
    /** Indicates that a JSON Pair has been idenfitied*/
    public static attribute PAIR:ElementType = ElementType{name:"Pair"};
    /** Indicates that a JSON VALUE has been idenfitied*/
    public static attribute VALUE:ElementType = ElementType{name:"VALUE"};
    /** Indicates that a JSON String has been idenfitied*/
    public static attribute STRING:ElementType = ElementType{name:"String"};
    /** Indicates that a JSON Number has been idenfitied*/
    public static attribute NUMBER:ElementType = ElementType{name:"Number"};
    /** Indicates that a JSON Null has been idenfitied*/
    public static attribute NULL:ElementType = ElementType{name:"Null"};
    /** Indicates that a JSON Boolean has been idenfitied*/
    public static attribute BOOLEAN:ElementType = ElementType{name:"Boolean"};
    /** Start indicates that parsing has started*/
    public static attribute START:ElementType = ElementType{name:"Start"};
    /** End indicates that parsing has ended*/
    public static attribute END:ElementType = ElementType{name:"End"};
    
   
    
}