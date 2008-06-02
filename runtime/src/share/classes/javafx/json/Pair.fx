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
import java.lang.Object;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.StringBuffer;
import java.lang.System;
import java.util.Map;

/**
 * Contains a JSON value identified with a name
 * @author jclarke
 */

public class Pair extends JSONBase {
    
    /**
     *  the name of the pair
     */
    public attribute name:String;
    
    /**
     * holds the value of the pair. if value is not null
     * then the array attribute will be emptied.
     * 
     * @see #array
     */
    public attribute value:Object on replace {
        if(value <> null ) {
            delete array;
        }
        if(value instanceof java.util.List) {
            var list = value as java.util.List;
            value = null;
            var iter = list.iterator();
            while(iter.hasNext()) {
                insert iter.next() into array;
            }
        }
    }
    
    /**
     * holds an array value of the pair. if array is not empty and 
     * then the value attribute will be set to null
     * 
     * @see #value
     */    
    public attribute array:Object[] on replace oldValue[lo..hi]=newVals {
        if(sizeof array > 0 and value <> null) {
            value = null;
        }
    };
    
    /**
     * Determines whether this pair equals another pair
     * @param obj the other pair
     * @return true if this pair is equal to the other pair
     * @see Object#equals(Object)
     */
    public function equals(obj:Object) {
        if(obj instanceof Pair) {
            var oth = obj as Pair;
            return this.name.equals(oth.name);
        }
        return false;
    }
    
    /**
     * returns a hash code 
     * @return the hash code
     * @see Object#hashCode()
     */
    public function hashCode():Integer {
        return this.name.hashCode();
    }
    
    /**
     * gets the value as a Boolean
     * @return the Boolean representation of the value
     * @see java.lang.Boolean#valueOf(String)
     */
    public function getValueAsBoolean():Boolean {
        if(value instanceof java.lang.Boolean) {
            return value as java.lang.Boolean;
        } else {
            return java.lang.Boolean.valueOf(value.toString());
        }
    }
    
    /**
     * gets the value as a Number
     * @return the Number representation of the value
     * @see java.lang.Double#valueOf(String)
     */    
    public function getValueAsNumber():Number {
        if(value instanceof java.lang.Number) {
            return (value as java.lang.Number).doubleValue();
        } else {
            return java.lang.Double.valueOf(value.toString());
        }
    }
    /**
     * gets the value as a JSONObject
     * @return the JSONObject representation of the value
     * @see JSONObject
     */    
    public function getValueAsJSONObject(): JSONObject {
        return value as JSONObject;
    }    
    
    /**
     * gets the value as a String
     * @return the String representation of the value
     */    
    public function getValueAsString(): String {
        return value.toString();
    }
    /**
     * gets the pair as a formatted JSON pair
     * @return the formatted JSON pair
     */    
    public function toString():String {
        var writer = new StringWriter();
        serialize(writer, 0, 0);
        return writer.toString();
    }    

    /**
     * Convert the JSON Object to JSON format.
     * Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated JSON stream.
     */    
    public function serialize(writer:Writer, curIndent:Integer, indentAmount:Integer):Void {
        writer.write('"{name}": ');
        if(value == null) {
            Array.serialize(array, writer, curIndent, indentAmount);
        }else if(value instanceof JSONObject) {
            var arr = value as JSONObject;
            arr.serialize(writer, curIndent + indentAmount, indentAmount);
        }else if(value instanceof String) {
            writer.write('"{value.toString()}"');
        }else if(value instanceof Map) {
            var map = value as Map;
            var jo = JSONObject{map:map};
            jo.serialize(writer, curIndent + indentAmount, indentAmount);
        }else {
            writer.write(value.toString());
        }
    }    

}
