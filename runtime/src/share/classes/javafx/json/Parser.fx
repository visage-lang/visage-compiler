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
    
    /**
     * a handler that will be called as each type is identified during
     * parsing, may be null
     */
    public attribute handler: function(type:ElementType, object:Object):Void;
    
    /**
     * Parse an input stream into a JSONObject
     * 
     * @param instream the input stream
     * @return the JSONObject
     * @see java.io.InputStream
     */
    public function parse(instream:InputStream):JSONObject {
        return parse(new InputStreamReader(instream));
    }

    /**
     * Parse an input stream into a JSONObject
     * 
     * @param instream the input stream
     * @param jsonObject the JSONObject to hold the result, may be null
     * @return the JSONObject
     * @see java.io.InputStream
     */
    public function parse(instream:InputStream, jsonObject:JSONObject):JSONObject {
        return parse(new InputStreamReader(instream), jsonObject);
    }
    
    /**
     * Parse a string into a JSONObject
     * 
     * @param str the string
     * @return the JSONObject
     */
    public function parse(str:String):JSONObject {
        return parse(new StringReader(str));
    }

    /**
     * Parse a string into a JSONObject
     * 
     * @param str the string
     * @param jsonObject the JSONObject to hold the result, may be null
     * @return the JSONObject
     */
    public function parse(str:String, jsonObject:JSONObject):JSONObject {
        return parse(new StringReader(str), jsonObject);
    }
    
    /**
     * Parse an reader into a JSONObject
     * 
     * @param reader the reader
     * @return the JSONObject
     * @see java.io.Reader
     */
    public function parse(reader:Reader):JSONObject {
            return parse(reader, null);
    }
    /**
     * Parse an reader into a JSONObject
     * 
     * @param reader the reader
     * @param jsonObject the JSONObject to hold the result, may be null
     * @return the JSONObject
     * @see java.io.Reader
     */
    public function parse(reader:Reader, jsonObject:JSONObject):JSONObject {
        
        var c = reader.read();
        while(c <> 0x7B and c >= 0) { // '{'
            c = reader.read();
        }
        //System.out.println("parse() callsing parseJSONObject");
        var result:JSONObject;
        
        if(handler <> null)
            handler(ElementType.START, result);
        result = parseJSONObject(reader, jsonObject);
        if(handler <> null)
            handler(ElementType.END, result);
        return result;
    }
    /**
     * process a JSONObject
     * 
     * @param reader the input stream
     * @return the JSONObject
     */
    private function parseJSONObject(reader:Reader):JSONObject {
        parseJSONObject(reader, null);
    }
    
    /**
     * process a JSONObject
     * 
     * @param reader the input stream
     * @param jsonObject the JSONObject to hold the result, may be null
     * @return the JSONObject
     */
    private function parseJSONObject(reader:Reader, jsonObject:JSONObject):JSONObject {
        //System.out.println("parseJSONObject");
        var myObject = ++endObject;
        var result = if(jsonObject <> null) {jsonObject} else {JSONObject{};} ;
        var c = reader.read();
        var string:String;
        var value:Object;
        while(endObject >= myObject and c >= 0) { // '}'
            if(isTerminator(c)) {
                 break;
            }else if(c == 0x22) { // '"'
                string = parseString(reader);
            }else if(c == 0x3A) { // ':'
                value = parseValue(reader);
                var pair = Pair{name:string, value:value};
                insert pair into result.pairs;
                if(handler <> null)
                    handler(ElementType.PAIR, pair);
            }
            c = reader.read();
        }
        if(handler <> null)
            handler(ElementType.OBJECT, result);
        return result;
    }
    
    /**
     * process a JSON String
     * 
     * @param reader the input stream
     * @return the String
     */
    private function parseString(reader:Reader):String {
        //System.out.println("parseString");
        var sb = new StringBuffer();
        var c = reader.read();
        while(c <> 0x22 and c >= 0) { // '"'
            if(c == 0x5C){ // '\\'
                c = escapeChar(reader);
            }
            //System.out.println("parseString: c = {%#x c}");
            sb.appendCodePoint(c);
            c = reader.read();
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
     * @param reader the input stream
     * @return the Object
     */    
    private function parseValue(reader:Reader):Object {
        //System.out.println("parseValue");
        var c = reader.read();
        var result:Object = null;
        while(not isTerminator(c) and c >= 0) { 
            //System.out.println("c = {%#x c}");
            if(not Character.isWhitespace(c)) {
                if(c == 0x7B) { // '{'
                    result =parseJSONObject(reader);
                    break;
                }else if (c == 0x5B) { // '['
                    result = parseJSONArray(reader);
                    break;
                } else if (c == 0x22) { // '"'
                    var str = parseString(reader);
                    c = reader.read();
                    while(not isTerminator(c)) {
                        c = reader.read();
                    }
                    result = str;
                    break;
                } else if (Character.isDigit(c) or c == 0x2d or c == 0x2b) { // - = 0x2d, + = 0x2b
                    result = parseNumber(reader, c);
                    break;
                } else {
                    result = parseLiteral(reader, c);
                    break;
                }
            }
            c = reader.read();
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
     * @param reader the input stream
     * @return the Number
     */     
    private function parseNumber(reader:Reader, firstDigit:Integer): Number {
        //System.out.println("parseNumber");
        var sb = new StringBuffer();
        sb.appendCodePoint(firstDigit);
        var c = reader.read();
        while(not Character.isWhitespace(c) and not isTerminator(c) and c > 0) {
            sb.appendCodePoint(c);
            c = reader.read();
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
     * @param reader the input stream
     * @return the JSONArray
     */     
    private function parseJSONArray(reader:Reader):Object {
        //System.out.println("parseJSONArray");
        var myArray = ++endArray;
        var items:Object[] = [];
        while(myArray <= endArray) {
            var item = parseValue(reader);
            insert item into items;
        }
        var a =  JSONArray{array: items };
        //System.out.println("parseJSONArray = {a}");
        if(handler <> null)
            handler(ElementType.JSONARRAY, a);
        return a;
    }
    
    /**
     * process a Literal for "true","false", "null"
     * 
     * @param reader the input stream 
     * @return the Object representing the Litera
     */     
    private function parseLiteral(reader:Reader, c:Integer):Object {
        //System.out.println("parseLiteral");
        var sb = new StringBuffer();
        while(Character.isWhitespace(c)) {
            c = reader.read();
        }
        //sb.appendCodePoint(c);
        while(not Character.isWhitespace(c) and not isTerminator(c) and c > 0) {
            sb.appendCodePoint(c);
            c = reader.read();
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
            var result = JSONNull{}
            if(handler <> null)
                handler(ElementType.JSONNULL, result);            
            return result;
        }else {
            if(handler <> null)
                handler(ElementType.STRING, "");            
            return "";
        }
    }

    /**
     * process an escape character
     * @param reader the input stream 
     * @return the integer value representing the escape character
     */
    private function escapeChar(reader:Reader):Integer {
        var c = reader.read();
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
            var hex1 = reader.read();
            var hex2 = reader.read();
            var hex3 = reader.read();
            var hex4 = reader.read();
            return getUnicode(hex1, hex2, hex3, hex4);
        }else {
            return c;
        }
        
    }
    
    /**
     * process an unicode character
     * @param reader the input stream 
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
            //System.out.println("EndArray = {endArray}");
        }
    }
    
    /**
     * Determine if the next character represents a terminating
     * character for a JSONObject, Array or Value
     */
    private function isTerminator(c:Integer):Boolean {
        //if(c == 0x7D or c == 0x5D or c == 0x2C or c == -1) {
        //    System.out.println("Terminator is {c}");
        //}
        return (c == 0x7D or c == 0x5D or c == 0x2C); // '}', ']', ','
        
    }
    

}




