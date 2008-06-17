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

import java.util.HashMap;
import java.util.Map;
import java.io.StringWriter;
import java.io.Writer;
import java.io.InputStream;
import java.io.Reader;
import java.lang.StringBuffer;
import java.lang.Object;
import java.lang.System;


/**
 * Represents a JSON Object
 * 
 * @author jclarke
 */

public class JSONObject extends JSONBase {
    /**
     * Flag indicating wheter the pairs are being updated 
     * from a java.util.Map or not
     */
    private attribute updateFromMap = false;

    /**
     * a handler that will be called as each type is identified during
     * parsing, may be null
     */
    public attribute handler: function(type:ElementType, object:Object):Void;



    /**
     *  Holds the pairs for this JSONObject
     */
    public attribute pairs:Pair[]  on replace oldValue[lo..hi]=newVals {
        if(not updateFromMap) {
            for(pair in oldValue[lo..hi]) {
                map.remove(pair.name);
            }            
            for(pair in newVals) {
                map.put(pair.name, pair);
            }
        }
    };
    
    /**
     * Holds a map of the pairs based on the pair name
     */
    public attribute map:Map = new HashMap() on replace old {
        var keyIter = map.keySet().iterator();
        updateFromMap = true;
        try {
            while(keyIter.hasNext()) {
                var key = keyIter.next() as String;
                var value = map.get(key);
                if(value instanceof Pair) {
                    var p = value as Pair;
                    value = p.value;
                }
                
                var pair = Pair{name:key, value:value};
                var found = false;
                for(p in pairs where p.name == pair.name) {
                    found = true;
                    p.value = pair.value;
                }
                if(not found) {
                    insert pair into pairs;
                }
            }
        } finally {
            updateFromMap = false;
        }
        
    };

    /**
     * Holds a string representation that will be parsed to populate
     * this JSONObject. The attributes <code>text</code>, <code>reader</code>, 
     * and <code>stream</code> are mutually exclusive with each
     * causing a new parsing and replacement of the contents of this JSONObject.
     * @see #reader
     * @see #stream
     */
    public attribute text:String on replace {
        if(text <> null) {
            var parser = Parser{handler: handler};
            parser.parse(text, this);
        }
    };

    /**
     * Holds a java.io.Reader that provides the string representation 
     * that will be parsed to populate this JSONObject.
     * The attributes <code>text</code>, <code>reader</code>, 
     * and <code>stream</code> are mutually exclusive with each
     * causing a new parsing and replacement of the contents of this JSONObject.
     * @see #text
     * @see #stream
     */
    public attribute reader:Reader on replace {
        if(reader <> null) {
            var parser = Parser{handler: handler};
            parser.parse(reader, this);
        }
    };

    /**
     * holds java.io.InputStream that provides the string representation 
     * that will be parsed to populate this JSONObject.
     * The attributes <code>text</code>, <code>reader</code>, 
     * and <code>stream</code> are mutually exclusive with each
     * causing a new parsing and replacement of the contents of this JSONObject.
     * @see #text
     * @see #reader
     */
    public attribute stream:InputStream on replace {
        if(stream <> null) {
            var parser = Parser{handler: handler};
            parser.parse(stream, this);
        }
    };
    
    /**
     * Add a new pair to this JSONObject
     * 
     * @param name the name of the pair
     * @param value the value for the pair
     */
    public function addPair(name:String, value:Object):Void {
        var pair = Pair{name:name, value:value};
        insert pair into pairs;
    }
    /**
     * Add a JSON array to this JSONObject
     * 
     * @param name the name of the pair
     * @param value the array
     */    
    public function addArray(name:String, value:Object[]):Void {
        var pair = Pair{name:name, sequence:value};
        insert pair into pairs;
    }
    
    /**
     * Add a Null to this JSONObject
     * 
     * @param name the name of the pair
     */     
    public function addNullPair(name:String):Void {
        addPair(name, JSONNull{});
    }
    
    /**
     * Remove a pair from this JSON Object
     * 
     * @param name the name of the pair
     */     
    public function removePair(name:String):Void {
        var pair = map.get(name) as Pair;
        if(pair <> null) {  
            delete pair from pairs;
        }
    }  
    
    /**
     * Get a pair 
     * @param name the name of the pair
     * @return the pair or null if not found
     */ 
    public function getPair(name:String):Pair {
        return map.get(name) as Pair;
    }
    
    /**
     * Get a value from a pair 
     * @param name the name of the pair
     * @return the value or null if not found
     */ 
    public function getValue(name:String):Object {
        var pair = map.get(name) as Pair;
        return if (pair <> null) pair.value else null;
    } 
    
    /**
     * Get an array from a pair 
     * @param name the name of the pair
     * @return the array or null if not found
     */     
    public function getArray(name:String):Object[] {
        var pair = map.get(name) as Pair;
        return if (pair <> null and pair.value instanceof JSONArray) (pair.value as JSONArray).array else null;
    }    
    /**
     * Determine whether a pair is a JSON array or not
     * @param name the name of the pair
     * @return true if the pair contains an array, false if not
     */ 
    public function isArray(name:String):Boolean {
        var pair = map.get(name) as Pair;
        return pair.value instanceof JSONArray;
    }
    


    /**
     * Convert the JSON Object to JSON format. 
     * Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated JSON stream.
     * @param curIndent the current indent amount
     * @param indentAmount the amount to indent from the curIndent for the next indent level
     */    
    protected function serialize(writer:Writer, curIndent:Integer, indentAmount:Integer):Void {
        if(sizeof pairs == 0) {
            writer.write("\{}");
        }else if (sizeof pairs == 1) {
            writer.write("\{");
            //writer.write(pairs[0].toString());
            pairs[0].serialize(writer, curIndent + indentAmount, indentAmount);
            writer.write("}");
        }else {
            writer.write("\{");
            var indentStr = "";
            if(doIndent) {
                if(curIndent > 0) {
                    var sb = new StringBuffer();
                    for(i in [0..<curIndent]) {
                        sb.append(" ");
                    }
                    indentStr = sb.toString();
                }
            }
            var firstPair = true;
            for(p in pairs) {
                if(not firstPair) {
                    writer.write(",\n");
                }else {
                    writer.write("\n");
                }
                writer.write(indentStr);
                p.serialize(writer, curIndent + indentAmount, indentAmount);
                firstPair = false;
            }
            writer.write("\n{indentStr}}");
         }
    }    
    
}
