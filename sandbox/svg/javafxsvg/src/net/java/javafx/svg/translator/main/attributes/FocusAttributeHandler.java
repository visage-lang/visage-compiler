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

import org.w3c.dom.Attr;


public class FocusAttributeHandler implements AttributeHandler {
    private String name;
    
    public FocusAttributeHandler(String name) {
        this.name = name;
    }
    
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        Length length = Length.parseLength(attr.getValue());
        String methodName = Length.methodName(length.getUnit());
        
        if (methodName == null) {
            builder.append(name + ": " +  length.getValue());
        } else {
            builder.append(name + ": " + methodName + "(" + length.getValue() + ")");
        }
    }
}
