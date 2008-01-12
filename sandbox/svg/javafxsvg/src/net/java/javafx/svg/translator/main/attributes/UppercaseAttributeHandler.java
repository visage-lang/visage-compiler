/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;

import org.w3c.dom.Attr;


public class UppercaseAttributeHandler implements AttributeHandler {
    String className;
    public UppercaseAttributeHandler(String clazz) {
        className = clazz;
    }
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        builder.append(attr.getName() + ": " + className + "." + attr.getValue().toUpperCase());
    }

}
