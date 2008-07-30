/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.json;

/**
 * Contains definitions of the JSON Element Types that are identified
 * during parsing of a JSON stream
 * @author jclarke
 */
public enum ElementType {

    /** Indicates that a JSONObject has been idenfitied*/
    OBJECT("JSONObject"),

    /** Indicates that a JSON Array has been idenfitied*/
    JSONARRAY("JSONArray"),

    /** Indicates that a JSON Pair has been idenfitied*/
    PAIR("Pair"),

    /** Indicates that a JSON VALUE has been idenfitied*/
    VALUE("VALUE"),

    /** Indicates that a JSON String has been idenfitied*/
    STRING("String"),

    /** Indicates that a JSON Number has been idenfitied*/
    NUMBER("Number"),

    /** Indicates that a JSON Null has been idenfitied*/
    JSONNULL("JSONNull"),

    /** Indicates that a JSON Boolean has been idenfitied*/
    BOOLEAN("Boolean"),

    /** Start indicates that parsing has started*/
    START("Start"),

    /** End indicates that parsing has ended*/
    END("End");

    /** the name of the element type */
    public final String name;

    private ElementType(String name) {
        this.name = name;
    }

}
