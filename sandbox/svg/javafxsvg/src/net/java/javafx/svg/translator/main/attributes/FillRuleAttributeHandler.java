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


public class FillRuleAttributeHandler implements AttributeHandler {
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        String value = attr.getValue();
        if ("nonzero".equals(value)) {
           value = "NON_ZERO";
        } else if ("evenodd".equals(value)) {
           value = "EVEN_ODD";
        }
        builder.append("fillRule: FillRule." + value);
    }
}
