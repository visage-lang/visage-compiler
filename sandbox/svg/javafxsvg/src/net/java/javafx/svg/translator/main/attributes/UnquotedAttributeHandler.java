/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;
import net.java.javafx.svg.translator.main.util.Utils;

import org.w3c.dom.Attr;


public class UnquotedAttributeHandler implements AttributeHandler {
    private static UnquotedAttributeHandler instance = new UnquotedAttributeHandler();
    public static UnquotedAttributeHandler getInstance() {
        return instance;
    }
    private UnquotedAttributeHandler(){}
    
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        builder.append(Utils.camelize(attr.getName()) + ": " + attr.getValue());
    }
}
