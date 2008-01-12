/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.attributes;

import java.text.DecimalFormat;

import net.java.javafx.svg.translator.AttributeHandler;
import net.java.javafx.svg.translator.Builder;

import org.w3c.dom.Attr;


public class OffsetAttributeHandler implements AttributeHandler {
    public void handleAttribute(Attr attr, Builder builder) throws Exception {
        int pos = 0;
        if (attr.getValue().endsWith("%")) {
            pos = 1;
        }
        String offset =
            attr.getValue().substring(0, attr.getValue().length() - pos);
        float result = new DecimalFormat("#").parse(offset).floatValue();
        if (pos == 1) {
            result /= 100;
        }
        builder.append("offset: " + result);
    }
}
