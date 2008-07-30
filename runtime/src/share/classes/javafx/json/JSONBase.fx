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

import java.io.StringWriter;
import java.io.Writer;

/**
 *  Provides base functionality for JSON types
 * 
 * @author jclarke
 */

public abstract class JSONBase  {
    /**
     * Holds the number of spaces for indenting when printing or serializing.
     * doIndent must be true for this to take effect. Default indent is 2.
     * 
     * @see #doIndent
     */
    public attribute indent:Integer = 2;
    
    /**
     * indicates whether or not indenting should be used when printing or serializing.
     * Default is true
     */
    public attribute doIndent:Boolean = true;
    
    /**
     * Convert the JSON Object to JSON format.
     * @return an JSON formatted string
     */
    override function toString():String {
        var writer = new StringWriter();
        serialize(writer);
        return writer.toString();
    }
    
    /**
     * Convert the JSON Object to JSON format.
     * Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated JSON stream.
     */    
    public function serialize(writer:Writer):Void {
        serialize(writer, 0, indent);
        writer.write("\n");
    }   
    
    /**
     * Convert the JSON Object to JSON format.
     * Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated JSON stream
     * @param curIndent the current indent amount
     * @param indentAmount the amount to indent from the curIndent for the next indent level
     */    
    protected abstract function serialize(writer:Writer, curIndent:Integer, indentAmount:Integer):Void;

}
