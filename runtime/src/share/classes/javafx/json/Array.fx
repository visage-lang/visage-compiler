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
import java.io.Writer;
import java.lang.StringBuffer;
import java.util.List;

/**
 * Represents a JSON Array
 * 
 * @author jclarke
 */

public class Array extends JSONBase {
     /**
     * Contains the  items in the array
     */
    public attribute array:Object[];
    
    /**
     * Convenience attribute to load array from a java.util.List
     */
    public attribute list: List on replace {
        var iter = list.iterator();
        while(iter.hasNext()) {
            insert iter.next() into array;
        }
    }

    
    /**
     * Convert the JSON array to JSON format.
     * Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated JSON stream.
     * @param curIndent the current indent amount
     * @param indentAmount the amount to indent from the curIndent for the next indent level
     */    
    protected function serialize(writer:Writer, curIndent:Integer, indentAmount:Integer):Void {
        serialize(this.array, writer, curIndent, indentAmount);
        
    }  
    
    /**
     * Convert the JSON array to JSON format.
     * Output is written to the Writer.
     * @param array the array items
     * @param writer the java.io.Writer that will receive the formated JSON stream.
     * @param curIndent the current indent amount
     * @param indentAmount the amount to indent from the curIndent for the next indent level
     */ 
    static function serialize(array:Object[], writer:Writer, curIndent:Integer, indentAmount:Integer):Void {
            var indentStr = "";
            if(curIndent > 0) {
                var sb = new StringBuffer();
                for(i in [0..<curIndent]) {
                    sb.append(" ");
                }
                indentStr = sb.toString();
            }
            if(sizeof array == 0) {
                writer.write("[]");
            }else if( sizeof array == 1) {
                writer.write("[");
                var e = array[0];
                if(e instanceof JSONBase) {
                    (e as JSONBase).serialize(writer, curIndent, indentAmount);
                }else if(e instanceof String) {
                    writer.write('"{e.toString()}"');
                }else {
                    writer.write('{e.toString()}');
                }
                writer.write("]");
            }else {
                var newIndent = curIndent + indentAmount;
                writer.write("[");

                var firstElement = true;
                writer.write("\n");
                for(e in array) {
                    if(not firstElement) {
                        writer.write(",\n");
                    }
                    writer.write(indentStr);
                    if(e instanceof JSONBase) {
                        (e as JSONBase).serialize(writer, curIndent, indentAmount);
                    }else if(e instanceof String) {
                        writer.write('{indentStr}"{e.toString()}"');
                    }else {
                        writer.write('{indentStr}{e.toString()}');
                    }
                    firstElement = false;
                }
                writer.write("\n{indentStr}]");                
            }
         }
}