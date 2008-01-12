/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.main.util.Length;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Attr;


public class LengthAttributeHandler implements AttributeHandler {
    private static LengthAttributeHandler instance = new LengthAttributeHandler();
    String attrName;
    
    public static LengthAttributeHandler getInstance() {
        return instance;
    }
    
    protected LengthAttributeHandler() {}
    public LengthAttributeHandler(String name) { attrName = name; }
    
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        Length length = Length.parseLength(attr.getValue());
        String methodName = Length.methodName(length.getUnit());
        String name = attrName;
        if (name == null) {
            name = attr.getName();
        }
        if (methodName == null) {
            builder.append(Utils.camelize(name) + ": " +  length.getValue());
        } else {
            builder.append(Utils.camelize(name) + ": " +
                           methodName + "(" + length.getValue() + ")");
        }
    }

}
