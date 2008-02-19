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
import java.lang.Object;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.StringBuffer;
import java.lang.System;
import java.util.Map;

/**
 * @author jclarke
 */

public class Pair extends JSONBase {
    
    public attribute name:String;
    // cna't have array and value at same time.
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
    public attribute array:Object[] on replace oldValue[lo..hi]=newVals {
        if(sizeof array > 0 and value <> null) {
            value = null;
        }
    };
    
    public function equals(obj:Object) {
        if(obj instanceof Pair) {
            var oth = obj as Pair;
            return this.name.equals(oth.name);
        }
        return false;
    }
    
    public function hashCode():Integer {
        return this.name.hashCode();
    }
    
    public function getValueAsBoolean():Boolean {
        if(value instanceof java.lang.Boolean) {
            return value as java.lang.Boolean;
        } else {
            return java.lang.Boolean.valueOf(value.toString());
        }
    }
    
    public function getValueAsNumber():Number {
        if(value instanceof java.lang.Number) {
            return (value as java.lang.Number).doubleValue();
        } else {
            return java.lang.Double.valueOf(value.toString());
        }
    }
    
    public function getValueAsJSONObject(): JSONObject {
        return value as JSONObject;
    }    
    
    public function getValueAsString(): String {
        return value.toString();
    }
    
    public function toString():String {
        var writer = new StringWriter();
        serialize(writer, 0, 0);
        return writer.toString();
    }    

    /**
     * Convert the JSON Object to a JSON format
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