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

import java.io.*;
import java.lang.Object;
import java.lang.Character;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.lang.System;

/**
 * Parses a JSON stream into a JSONObject
 * 
 * @author jclarke
 * @see javafx.json.JSONObject
 */

public class Parser {
    private attribute endObject:Integer = 0;
    private attribute endArray:Integer = 0;
    
    public attribute handler: function(type:ElementType, object:Object):Void;
    
    /**
     * Parse an input stream into a JSONObject
     * 
     * @param instream the input stream
     * @return the JSONObject
     */
    public function parse(instream:InputStream):JSONObject {
        
        var c = instream.read();
        while(c <> 0x7B and c >= 0) { // '{'
            c = instream.read();
        }
        //System.out.println("parse() callsing parseJSONObject");
        var result:JSONObject;
        
        if(handler <> null)
            handler(ElementType.START, result);
        result = parseJSONObject(instream);
        if(handler <> null)
            handler(ElementType.END, result);
        return result;
    }
    
    /**
     * process a JSONObject
     * 
     * @param instream the input stream
     * @return the JSONObject
     */
    private function parseJSONObject(instream:InputStream):JSONObject {
        //System.out.println("parseJSONObject");
        var myObject = ++endObject;
        var result = JSONObject{};
        var c = instream.read();
        var string:String;
        var value:Object;
        while(endObject >= myObject and c >= 0) { // '}'
            if(c == 0x22) { // '"'
                string = parseString(instream);
            }else if(c == 0x3A) { // ':'
                value = parseValue(instream);
                var pair = Pair{name:string, value:value};
                insert pair into result.pairs;
                if(handler <> null)
                    handler(ElementType.PAIR, pair);
            }
            c = instream.read();
        }
        if(handler <> null)
            handler(ElementType.OBJECT, result);
        return result;
    }
    
    /**
     * process a JSON String
     * 
     * @param instream the input stream
     * @return the String
     */
    private function parseString(instream:InputStream):String {
        //System.out.println("parseString");
        var sb = new StringBuffer();
        var c = instream.read();
        while(c <> 0x22 and c >= 0) { // '"'
            if(c == 0x5C){ // '\\'
                c = escapeChar(instream);
            }
            //System.out.println("parseString: c = {%#x c}");
            sb.appendCodePoint(c);
            c = instream.read();
        }
        //System.out.println("parseString = {sb}");
        var result = sb.toString();
        if(handler <> null)
            handler(ElementType.STRING, result);        
        return result;
    }
    
    /**
     * process a JSON value
     * 
     * @param instream the input stream
     * @return the Object
     */    
    private function parseValue(instream:InputStream):Object {
        //System.out.println("parseValue");
        var c = instream.read();
        var result:Object = null;
        while(not isTerminator(c) and c >= 0) { 
            //System.out.println("c = {%#x c}");
            if(not Character.isWhitespace(c)) {
                if(c == 0x7B) { // '{'
                    result =parseJSONObject(instream);
                    break;
                }else if (c == 0x5B) { // '['
                    result = parseJSONArray(instream);
                    break;
                } else if (c == 0x22) { // '"'
                    var str = parseString(instream);
                    c = instream.read();
                    while(not isTerminator(c)) {
                        c = instream.read();
                    }
                    result = str;
                    break;
                } else if (Character.isDigit(c) or c == 0x2d or c == 0x2b) { // - = 0x2d, + = 0x2b
                    result = parseNumber(instream, c);
                    break;
                } else {
                    result = parseLiteral(instream, c);
                    break;
                }
            }
            c = instream.read();
        }
        checkEnd(c);
        //System.out.println("parseValue = {result}");
        if(handler <> null)
            handler(ElementType.VALUE, result);
        return result;
    }
    
    /**
     * process a Number
     * 
     * @param instream the input stream
     * @return the Number
     */     
    private function parseNumber(instream:InputStream, firstDigit:Integer): Number {
        //System.out.println("parseNumber");
        var sb = new StringBuffer();
        sb.appendCodePoint(firstDigit);
        var c = instream.read();
        while(not Character.isWhitespace(c) and not isTerminator(c) and c > 0) {
            sb.appendCodePoint(c);
            c = instream.read();
        }     
        checkEnd(c);
        //System.out.println("parseNumber = {sb}");
        var d =  java.lang.Double.parseDouble(sb.toString());
        //System.out.println("parseNumber(d) = {d}");
        if(handler <> null)
            handler(ElementType.NUMBER, d);        
        return d;
            
    }
    
    /**
     * process a JSON Array
     * 
     * @param instream the input stream
     * @return the Array
     */     
    private function parseJSONArray(instream:InputStream):Object {
        //System.out.println("parseJSONArray");
        var myArray = ++endArray;
        var list = new ArrayList();
        while(myArray <= endArray) {
            var item = parseValue(instream);
            //System.out.println("Add Array item = {item}");
            list.add(item);
        }
        var a =  Array{list : list };
        //System.out.println("parseJSONArray = {a}");
        if(handler <> null)
            handler(ElementType.ARRAY, a);
        return a;
    }
    
    /**
     * process a Literal for "true","false", "null"
     * 
     * @param instream the input stream 
     * @return the Object representing the Litera
     */     
    private function parseLiteral(instream:InputStream, c:Integer):Object {
        //System.out.println("parseLiteral");
        var sb = new StringBuffer();
        while(Character.isWhitespace(c)) {
            c = instream.read();
        }
        //sb.appendCodePoint(c);
        while(not Character.isWhitespace(c) and not isTerminator(c) and c > 0) {
            sb.appendCodePoint(c);
            c = instream.read();
        }
        checkEnd(c);
        var str = sb.toString();
        //System.out.println("parseLiteral = {str}");
        if(str.equalsIgnoreCase("false")) {
            //System.out.println("parseLiteral return FALSE");
            if(handler <> null)
                handler(ElementType.BOOLEAN, java.lang.Boolean.FALSE);
            return java.lang.Boolean.FALSE;
        } else if (str.equalsIgnoreCase("true")) {
            //System.out.println("parseLiteral return TRUE");
            if(handler <> null)
                handler(ElementType.BOOLEAN, java.lang.Boolean.TRUE);            
            return java.lang.Boolean.TRUE;
        } else if(str.equalsIgnoreCase("null")) {
            //System.out.println("parseLiteral return NULL");
            var result = Null{}
            if(handler <> null)
                handler(ElementType.NULL, result);            
            return result;
        }else {
            if(handler <> null)
                handler(ElementType.STRING, "");            
            return "";
        }
    }

    /**
     * process an escape character
     * @param instream the input stream 
     * @return the integer value representing the escape character
     */
    private function escapeChar(instream:InputStream):Integer {
        var c = instream.read();
        if(c == 0x73) { // 't'
            return 0x09; 
        }else if (c == 0x22) { // '\"'
            return c;
        }else if (c == 0x5C) { // '\\'
            return c;
        }else if (c == 0x2F) { // '\/'
            return c;
        }else if (c == 0x62) { // '\b'
            return 0x08;
        }else if (c == 0x66) { // '\f'
            return 0x0C;
        }else if (c == 0x6E) { // '\n'
            return 0x0A;
        }else if (c == 0x72) { // '\r'
            return 0x0D;            
        }else if (c == 0x75) { // '\u'
            var hex1 = instream.read();
            var hex2 = instream.read();
            var hex3 = instream.read();
            var hex4 = instream.read();
            return getUnicode(hex1, hex2, hex3, hex4);
        }else {
            return c;
        }
        
    }
    
    /**
     * process an unicode character
     * @param instream the input stream 
     * @return the integer value representing the unicode character
     */    
    private function getUnicode(hex1:Integer, hex2:Integer, hex3:Integer, hex4:Integer):Integer {
        return Character.digit(hex1, 16) * 0x1000 + Character.digit(hex2, 16) * 0x100 +
            Character.digit(hex3, 16) * 0x10 + Character.digit(hex4, 16);
    }
    
    /**
     * check to see if the character represents the end on a JSON Object or JSONArray
     * @param c the next chracter
     */
    private function checkEnd(c:Integer):Void {
        if(c == 0x7D) { // '}'
            endObject--;
        }else if (c == 0x5D) { //']'
            endArray--;
        }
    }
    
    /**
     * Determine if the next character represents a terminating
     * character for a JSONObject, Array or Value
     */
    private function isTerminator(c:Integer):Boolean {
        return (c == 0x7D or c == 0x5D or c == 0x2C); // '}', ']', ','
        
    }
    

}




