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
 * @author jclarke
 */

public class ElementType {

    public attribute name:String;
    
    public static attribute OBJECT:ElementType = ElementType{name:"JSONObject"};
    public static attribute ARRAY:ElementType = ElementType{name:"Array"};
    public static attribute PAIR:ElementType = ElementType{name:"Pair"};
    public static attribute VALUE:ElementType = ElementType{name:"VALUE"};
    public static attribute STRING:ElementType = ElementType{name:"String"};
    public static attribute NUMBER:ElementType = ElementType{name:"Number"};
    public static attribute NULL:ElementType = ElementType{name:"Null"};
    public static attribute BOOLEAN:ElementType = ElementType{name:"Boolean"};
    public static attribute START:ElementType = ElementType{name:"Start"};
    public static attribute END:ElementType = ElementType{name:"End"};
    
   
    
}